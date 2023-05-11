package com.dkh.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkh.dto.Result;
import com.dkh.dto.UserDTO;
import com.dkh.mapper.UserMapper;
import com.dkh.pojo.LoginVo;
import com.dkh.pojo.User;
import com.dkh.service.UserService;
import com.dkh.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dkh.utils.RedisConstants.*;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 修改用户
     * @param user 用户
     * @return
     */
    @Override
    public Result updateUser(User user) {
        log.info(user.toString());

        //判断手机号格式的合法性
        if (!Validator.isMobile(user.getPhone())) {

        }
        //设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        //更新数据
        boolean isSuccess = updateById(user);
        if (isSuccess){
            //删除缓存
            stringRedisTemplate.delete(LOGIN_USER_KEY + user.getId());
            return Result.success(user, "OK");
        }
        return Result.fail(400,"修改失败");
    }

    /**
     * 分页查询
     * @param current 当前页
     * @param size 每页大小
     * @return
     */
    @Override
    public Result getUserListByPage(Integer current, Integer size) {
        //如果开始查询的位置大于总数，则返回
        int start = (current - 1) * size + 1;
        //先查询Redis的count
        String countStr = stringRedisTemplate.opsForValue().get(CACHE_USER_COUNT_KEY);
        //判断Redis中的是否有数据，没有则查找数据库
        int count = StringUtils.isBlank(countStr) ? (int) getTotal().getData() : Integer.valueOf(countStr);

        if (start > count) {
            return Result.fail(400, "无数据");
        }

        //先查询redis中是否有缓存，有则直接返回,无则查询数据库
        String data = stringRedisTemplate.opsForValue().get(CACHE_USER_KEY + current + ":" + size);
        if (StringUtils.isNotBlank(data)) {
            //将JSN数据转成对象数组
            List<UserDTO> userDTOList = JSONUtil.parseArray(data).toList(UserDTO.class);
            return Result.success(userDTOList);
        }

        //设置查询页的数据
        IPage page = new Page(current, size);

        userService.page(page);
        List<User> userList = page.getRecords();

        //去除敏感信息
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setIcon(user.getIcon());
            userDTO.setId(user.getId());
            //将手机中间四位抹除
            String phone = user.getPhone();
            if (StringUtils.isNotBlank(phone)) {
                phone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
            }
            userDTO.setPhone(phone);
            userDTO.setNickName(user.getNickName());
            userDTO.setCreateTime(user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //将信息添加刀userDTOList
            userDTOList.add(userDTO);
        }

        //将信息存到Redis
        //将userList转成JSON字符串
        data = JSONUtil.toJsonStr(userDTOList);
        stringRedisTemplate.opsForValue().set(CACHE_USER_KEY + current + ":" + size, data, CACHE_USER_TTL, TimeUnit.MINUTES);

        return Result.success(userDTOList);
    }

    /**
     * 获取用户个数
     * @return
     */
    @Override
    public Result getTotal() {
        //先查询Redis
        String count = stringRedisTemplate.opsForValue().get(CACHE_USER_COUNT_KEY);
        if (StringUtils.isNotBlank(count)) {
            return Result.success(Integer.valueOf(count));
        }
        count = String.valueOf(userService.count());
        stringRedisTemplate.opsForValue().set(CACHE_USER_COUNT_KEY, count, CACHE_USER_COUNT_TTL, TimeUnit.MINUTES);

        return Result.success(Integer.valueOf(count));
    }


    /**
     * 登录
     * @param loginVo
     * @return
     */
    @Override
    public Result login(LoginVo loginVo) {

        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)){
            return Result.fail(400,"手机号和验证码不能为空");
        }
        //判断验证码是否正确
        String redisCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (!code.equals(redisCode)){
            return Result.fail(400, "验证码错误");
        }
        //登录成功
        //判断该用户是否第一次登录
        User user = query().eq("phone", phone).one();
        if (user == null){
            //第一次登录,注册信息
            user = new User();
            user.setPhone(phone);
            user.setCreateTime(LocalDateTime.now());
            String username = "user_" + RandomUtil.randomString(6);
            user.setUsername(username);
            //设置随机昵称
            user.setNickName(username);
            user.setUpdateTime(LocalDateTime.now());
            boolean isSuccess = save(user);
            if (!isSuccess){
                return Result.fail(400, "登录失败");
            }
        }
        //将用户信息存入redis中
        //生成token
        String token = JWTUtils.createToken(user.getId(), user.getUsername());
        //将user转成map存储到redis中
        Map<String, String> userMap = new HashMap<>();
        userMap.put("id", String.valueOf(user.getId()));
        userMap.put("nickName", user.getNickName());
        userMap.put("username", user.getUsername());
        userMap.put("getPhone", user.getPhone());
        userMap.put("icon", user.getIcon());
        userMap.put("token", token);

        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + user.getId(), userMap);
        //设置token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + user.getId(), LOGIN_USER_TTL, TimeUnit.SECONDS);

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);
        return Result.success(map);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    public Result deleteUserById(Long id) {
        //删除数据库数据
        boolean remove = removeById(id);
        if (!remove){
            return new Result(400, null, "删除失败");
        }
        //删除redis中的数据
        Set<String> keys = stringRedisTemplate.keys(CACHE_USER_KEY + "*");
        stringRedisTemplate.delete(CACHE_USER_COUNT_KEY);
        stringRedisTemplate.delete(keys);
        return Result.success(null);
    }

    /**
     * 根据条件查询用户
     * @param id 用户id
     * @param username 用户名
     * @param phone 手机号
     * @return 用户
     */
    @Override
    public List<User> searchUserByCondition(Long id, String username, String phone) {
        return userMapper.searchUserByCondition(id, username, phone);
    }


}

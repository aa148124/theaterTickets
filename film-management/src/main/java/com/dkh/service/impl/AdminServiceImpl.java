package com.dkh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkh.dto.AdminDTO;
import com.dkh.dto.Result;
import com.dkh.mapper.AdminMapper;
import com.dkh.pojo.Admin;
import com.dkh.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dkh.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired()
    private StringRedisTemplate stringRedisTemplate;



    @Override
    public Result login(String username, String password) {

        if (username.length() < 6 || password.length() < 6){
            return new Result(400, null, "账号或者密码长度小于6位");
        }
        //查询数据库
        Admin admin = query().eq("username", username).eq("password", password).one();

        //判断该用户的账号密码，返回null为找不到该用户或者用户名或密码错误
        if (admin == null){
            //用户不存在
            return new Result(400, null, "用户名或密码错误！");
        }

        //将用户信息存入redis中
        //生存随机token作为登录令牌
        String token = UUID.randomUUID().toString(true);
        //将user转成map存储到redis中
        AdminDTO adminDTO = BeanUtil.copyProperties(admin, AdminDTO.class);
        log.info(adminDTO.toString());

        Map<String, String> userMap = new HashMap<>();
        userMap.put("id", String.valueOf(adminDTO.getId()));
        userMap.put("nickName", adminDTO.getNickName());
        userMap.put("username", adminDTO.getUsername());

        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + token, userMap);
        //设置token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.SECONDS);


        return new Result(200, token, "成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatePwd(String token, String oldPassword, String newPassword) {
        //判断密码长度
        if (oldPassword.length() < 6 || newPassword.length() <6){
            return new Result(400, null, "密码不能小于6位");
        }
        //查询旧密码是否正确
        Admin admin = query().eq("password", oldPassword).one();

        if (admin == null){
            //旧密码不对
            return new Result(400, null, "旧密码不正确");
        }

        //旧密码正确,更新数据库
        //从redis中获取用户id
        String id = stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id").toString();
        admin.setPassword(newPassword);
        boolean flag = updateById(admin);
        if (!flag){
            return new Result(400, null, "修改失败");
        }
        //删除redis数据
        Boolean delete = stringRedisTemplate.delete(LOGIN_USER_KEY + token);

        return new Result(200, null, "修改成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateAdmin(Admin admin) {
        log.info(admin.toString());
        //更新数据
        updateById(admin);
        //删除缓存
        Set<String> keys = stringRedisTemplate.keys(CACHE_ADMIN_KEY + "*");
        stringRedisTemplate.delete(keys);
        return new Result(200, null, "OK");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteAdmin(Long id) {
        removeById(id);
        Set<String> keys = stringRedisTemplate.keys(CACHE_ADMIN_KEY + "*");
        stringRedisTemplate.delete(CACHE_ADMIN_COUNT_KEY);
        stringRedisTemplate.delete(keys);
        return new Result(200, null, "OK");
    }

    @Override
    public Result getAdminList(Integer current, Integer size) {
        //如果开始查询的位置大于总数，则返回
        int start = (current - 1) * size + 1;
        //先查询Redis的count
        String countStr = stringRedisTemplate.opsForValue().get(CACHE_ADMIN_COUNT_KEY);
        //判断Redis中的是否有数据，没有则查找数据库
        int count = StringUtils.isBlank(countStr) ? (int) getAdminTotal().getData() : Integer.valueOf(countStr);

        if (start > count) {
            return new Result(400, null, "无数据");
        }

        //先查询redis中是否有缓存，有则直接返回,无则查询数据库
        String data = stringRedisTemplate.opsForValue().get(CACHE_ADMIN_KEY + current + ":" + size);
        if (StringUtils.isNotBlank(data)) {
            //将JSN数据转成对象数组
            List<AdminDTO> adminDTOList = JSONUtil.parseArray(data).toList(AdminDTO.class);
            return new Result(200, adminDTOList, "OK");
        }

        //设置查询页的数据
        IPage page = new Page(current, size);
        page(page);
        List<Admin> adminList = page.getRecords();

        //去除敏感信息
        List<AdminDTO> adminDTOList = new ArrayList<>();
        for (Admin admin : adminList) {
            AdminDTO adminDTO = BeanUtil.copyProperties(admin, AdminDTO.class);
            //将手机中间四位抹除
            String phone = adminDTO.getPhone();
            if (StringUtils.isNotBlank(phone)) {
                phone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
            }
            adminDTO.setPhone(phone);
            adminDTOList.add(adminDTO);
        }

        //将信息存到Redis
        //将adminList转成JSON字符串
        data = JSONUtil.toJsonStr(adminDTOList);
        stringRedisTemplate.opsForValue().set(CACHE_ADMIN_KEY + current + ":" + size, data, CACHE_ADMIN_TTL, TimeUnit.MINUTES);

        return new Result(200, adminDTOList, "OK");
    }

    @Override
    public Result getAdminTotal() {
        //先查询Redis
        String count = stringRedisTemplate.opsForValue().get(CACHE_ADMIN_COUNT_KEY);
        if (StringUtils.isNotBlank(count)) {
            return new Result(200, Integer.valueOf(count), "OK");
        }

        count = String.valueOf(count());
        stringRedisTemplate.opsForValue().set(CACHE_ADMIN_COUNT_KEY, count, CACHE_ADMIN_COUNT_TTL, TimeUnit.MINUTES);

        return new Result(200, Integer.valueOf(count), "OK");
    }
}

package com.dkh.controller;


import com.dkh.dto.Result;
import com.dkh.pojo.LoginVo;
import com.dkh.pojo.User;
import com.dkh.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        return userService.login(loginVo);
    }

    @GetMapping("/getUserList/{current}/{size}")
    public Result getUserList(@PathVariable Integer current, @PathVariable Integer size) {
        return userService.getUserListByPage(current, size);
    }


    @GetMapping("/getUserTotal")
    public Result getUserTotal() {

        return userService.getTotal();
    }

    @PutMapping("/updateUser")
    public Result updateAdmin(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public Result deleteUserById(@PathVariable Long id){
        return userService.deleteUserById(id);
    }

    @PostMapping("/searchUser")
    public Result searchUser(Long id, String username, String phone){
        //判断是否全为空
        if (id == null && StringUtils.isBlank(username) && StringUtils.isBlank(phone)){
            return userService.getUserListByPage(1,5);
        }

        //查询数据库
        List<User> userList = userService.searchUserByCondition(id, username, phone);
        return new Result(200, userList);
    }
}

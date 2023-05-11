package com.dkh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkh.dto.Result;
import com.dkh.pojo.LoginVo;
import com.dkh.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService extends IService<User> {



    Result updateUser(User user);

    Result getUserListByPage(Integer current, Integer size);

    Result getTotal();


    Result login(LoginVo loginVo);

    Result deleteUserById(Long id);

    List<User> searchUserByCondition(@Param("id") Long id, @Param("username") String username, @Param("phone") String phone);

}

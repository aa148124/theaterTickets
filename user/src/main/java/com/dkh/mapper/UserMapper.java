package com.dkh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dkh.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    public List<User> searchUserByCondition(@Param("id") Long id, @Param("username") String username, @Param("phone") String phone);
}

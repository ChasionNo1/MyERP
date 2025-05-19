package com.chasion.erpbackend.mapper;

import com.chasion.erpbackend.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where username=#{username}")
    User findUserByUserName(String userName);

    int insertUser(User user);
}

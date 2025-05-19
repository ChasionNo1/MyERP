package com.chasion.erpbackend.mapper;

import com.chasion.erpbackend.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where username=#{username}")
    User findUserByUserName(String userName);

    int insertUser(User user);

    @Select("select position from user where id = {id}")
    String getPositionById(int id);

    @Select("select * from user where id = #{id}")
    User getUserById(Long id);
}

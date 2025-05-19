package com.chasion.erpbackend.service.impl;

import com.chasion.erpbackend.entities.User;
import com.chasion.erpbackend.mapper.UserMapper;
import com.chasion.erpbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findUserByUserName(username);

    }

    @Override
    public void register(String username, String password, String email) {
        User user = new User();
        userMapper.insertUser(user);
    }


}

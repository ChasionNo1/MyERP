package com.chasion.erpbackend.service.impl;

import com.chasion.erpbackend.entities.User;
import com.chasion.erpbackend.exception.BusinessException;
import com.chasion.erpbackend.mapper.UserMapper;
import com.chasion.erpbackend.service.UserService;
import com.chasion.erpbackend.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findUserByUserName(username);

    }

    // 注册方法
    @Override
    public void register(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        String salt = MyUtils.getSalt(6);
        user.setSalt(salt);
        password = MyUtils.Md5(password + salt);
        user.setPassword(password);
        user.setAvatar("/images/user.png");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insertUser(user);
    }

    @Override
    public void login(String username, String password) {
        User user = userMapper.findUserByUserName(username);
        if (!user.getPassword().equals(MyUtils.Md5(password + user.getSalt()))) {
            throw new BusinessException(400, "用户名或密码错误");
        }
    }
}

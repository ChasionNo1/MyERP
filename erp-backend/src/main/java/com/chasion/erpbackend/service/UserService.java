package com.chasion.erpbackend.service;

import com.chasion.erpbackend.entities.User;

public interface UserService {

    User findByUsername(String username);

    User findById(Long id);

    void register(String username, String password, String email);

    void login(String username, String password);

    String getPositionById(int id);


}

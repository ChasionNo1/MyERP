package com.chasion.erpbackend.service;

import com.chasion.erpbackend.entities.User;

public interface UserService {

    User findByUsername(String username);

    void register(String username, String password, String email);
}

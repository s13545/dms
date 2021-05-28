package com.ssp.service;

import com.github.pagehelper.PageInfo;
import com.ssp.entity.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    void updateById(User user);

    List<User> findAll();

    void deleteById(String uid);

    boolean register(User user);

 }

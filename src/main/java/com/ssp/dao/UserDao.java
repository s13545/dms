package com.ssp.dao;

import com.ssp.entity.User;

import java.util.List;

public interface UserDao {

    User findByUsername(String username);

    void updateById(User user);

    List<User> findAll();

    void deleteById(String uid);

    void register(User user);
}

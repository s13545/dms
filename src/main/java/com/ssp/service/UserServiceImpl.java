package com.ssp.service;

import com.ssp.dao.UserDao;
import com.ssp.entity.User;
import com.ssp.util.BCryptUtil;
import com.ssp.util.CreateID;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;



@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void updateById(User user) {
        userDao.updateById(user);
    }

    @Override
    public List<User> findAll() {

        List<User> users = userDao.findAll();
        if (users.contains(null)){
            users.remove(null);
        }
        return users;
    }

    @Override
    public void deleteById(String uid) {
        userDao.deleteById(uid);
    }

    @Override
    public boolean register(User user) {
        //如果username已经存在，就反回false
        if (userDao.findByUsername(user.getUsername())!=null){
            return false;
        }else {
            String uid = CreateID.createID();
            user.setUid(uid);
            System.out.println(user);

            //对用户的密码进行加密
            SimpleHash sh=new SimpleHash("MD5", user.getPassword(), user.getUsername(), 3);
            user.setPassword(sh.toString());
            userDao.register(user);
            return true;
        }

    }


}

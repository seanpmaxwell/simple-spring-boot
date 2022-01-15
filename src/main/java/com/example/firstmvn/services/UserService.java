package com.example.firstmvn.services;

import java.util.List;

import com.example.firstmvn.daos.UserDao;
import com.example.firstmvn.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    
    @Autowired
    UserDao userDao;
    

    public List<User> getUsers() {
        return this.userDao.findById(5);
    }
}

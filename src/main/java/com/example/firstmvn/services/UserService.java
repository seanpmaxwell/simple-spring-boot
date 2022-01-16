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
    

    /**
     * Get all users.
     * 
     * @return
     */
    public List<User> getAll() {
        return this.userDao.getAll(5);
    }


    /**
     * Add one user.
     */
    public void addOne(User user) {
        this.userDao.addOne(user);
    }
}

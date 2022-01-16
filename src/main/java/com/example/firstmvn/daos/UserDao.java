package com.example.firstmvn.daos;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.repositories.IUserRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserDao {

    @Autowired
    private IUserRepo studentRepo;
    

    /**
     * Find a user by id
     * 
     * @param id
     * @return
     */
    public List<User> findById(int id) {
        return this.studentRepo.findAll();
    }
}

package com.example.firstmvn.daos;

import java.util.ArrayList;
import java.util.List;

import com.example.firstmvn.entities.User;
import org.springframework.stereotype.Repository;


@Repository
public class UserDao {
    

    /**
     * Find a user by id
     * 
     * @param id
     * @return
     */
    public List<User> findById(int id) {
        var users = new ArrayList<User>();
        users.add(new User());
        users.add(new User());
        users.add(new User("sean maxwell"));
        return users;
    }
}

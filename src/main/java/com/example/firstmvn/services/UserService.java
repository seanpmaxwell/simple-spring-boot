/**
 * Business logic for users.
 * 
 * created by Sean Maxwell, 1/15/2022
 */

package com.example.firstmvn.services;

import com.example.firstmvn.daos.UserDao;
import com.example.firstmvn.entities.User;

import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;


@Service
public class UserService {
    
    private final UserDao userDao;


    /**
     * Constructor()
     * 
     * @param userDao
     */
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    

    /**
     * Get all users.
     * 
     * @return
     */
    public List<User> getAll() {
        return userDao.getAll();
    }


    /**
     * Get one user by id.
     * 
     * @param id
     * @return
     */
    public User getOne(Long id) throws EntityNotFoundException {
        return userDao.getOne(id);
    }


    /**
     * Add one user.
     * 
     * @param user
     * @throws EntityExistsException
     */
    public void addOne(User user) throws EntityExistsException {
        userDao.addOne(user);
    }


    /**
     * Update one user.
     * 
     * @param user
     */
    public void updateOne(User user) throws RuntimeException {
        userDao.updateOne(user);
    }


    /**
     * Delete user by id.
     * 
     * @param id
     */
    public void deleteOne(Long id) throws EntityNotFoundException {
        userDao.deleteOne(id);
    }
}

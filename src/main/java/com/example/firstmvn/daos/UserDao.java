/**
 * Handle db requests for users.
 * 
 * created by Sean Maxwell, 1/15/2022
 */

package com.example.firstmvn.daos;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.repositories.IUserRepo;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserDao {

    @Autowired
    private IUserRepo userRepo;

    private static final String ID_NOT_FOUND_MSG_1 = "User with id \"";
    private static final String ID_NOT_FOUND_MSG_2 = "\" not found.";
    private static final String ID_EXISTS_MSG = "User with that id and/or email already persists";
    private static final String EMAIL_TAKEN_MSG_1 = "Then email \"";
    private static final String EMAIL_TAKEN_MSG_2 = "\"has already been taken by another user.";
    

    /**
     * Find a user by id.
     * 
     * @return
     */
    public List<User> getAll() {
        return this.userRepo.findAll();
    }


    /**
     * Find one user by id.
     * 
     * @param id
     * @return
     */
    public User getOne(Long id) {
        Optional<User> resp = this.userRepo.findById(id);
        if (resp.isPresent()) {
            return resp.get();
        } else {
            String msg = UserDao.getIdNotFoundMsg(id);
            throw new EntityNotFoundException(msg);
        }
    }


    /**
     * Add one user.
     * 
     * @param user
     */
    public void addOne(User user) {
        // Check if id exists
        List<User> users = this.userRepo.findByIdOrEmail(user.getId(), user.getEmail());
        if (users.size() > 0) {
            String msg = UserDao.getAlreadyPersistsMsg(user.getId(), user.getEmail());
            throw new EntityExistsException(msg);
        }
        // Do db query
        this.userRepo.save(user);
    }


    /**
     * Update one user by id.
     * 
     * @param user
     */
    public void updateOne(User user) {
        // Check id not found
        Optional<User> resp = this.userRepo.findById(user.getId());
        if (!resp.isPresent()) {
            String msg = UserDao.getIdNotFoundMsg(user.getId());
            throw new EntityNotFoundException(msg);
        }
        // Check email already persists
        User userWithEmail = this.userRepo.findByEmail(user.getEmail());
        if (userWithEmail != null && userWithEmail.getId() != user.getId()) {
            String msg = UserDao.getEmailAlreadyTakenMsg(user.getEmail());
            throw new RuntimeException(msg);
        }
        // Update user
        this.userRepo.updateOne(user.getId(), user.getEmail(), user.getName(), user.getPwdHash());
    }


    /**
     * Delete one user by id.
     * 
     * @param id
     */
    public void deleteOne(Long id) {
        this.userRepo.deleteById(id);
    }


    /**
     * Get message for id not found.
     * 
     * @return
     */
    public static String getIdNotFoundMsg(Long id) {
        return UserDao.ID_NOT_FOUND_MSG_1 + id + UserDao.ID_NOT_FOUND_MSG_2;
    }


    /**
     * Get message for taken id or email
     * 
     * @return
     */
    public static String getAlreadyPersistsMsg(Long id, String email) {
        return UserDao.ID_EXISTS_MSG + " [id: " + id + ", email: " + email + "]";
    }


    /**
     * Get message for email already taken.
     * 
     * @param email
     * @return
     */
    public static String getEmailAlreadyTakenMsg(String email) {
        return UserDao.EMAIL_TAKEN_MSG_1 + email + UserDao.EMAIL_TAKEN_MSG_2;
    }
}

/**
 * Handle db requests for users.
 * 
 * created by Sean Maxwell, 1/15/2022
 */

package com.example.firstmvn.daos;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.repositories.UserRepo;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;


@Repository
@Transactional
public class UserDao {

    private static final String ID_NOT_FOUND_MSG_1 = "User with id \"";
    private static final String ID_NOT_FOUND_MSG_2 = "\" not found.";
    private static final String ADD_ERR_MSG = "User with that id and/or email already persists";
    private static final String EMAIL_TAKEN_MSG_1 = "The email \"";
    private static final String EMAIL_TAKEN_MSG_2 = "\" has already been taken by another user.";

    private final UserRepo userRepo;


    /**
     * Constructor()
     * 
     * @param userRepo
     */
    public UserDao(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    

    /**
     * Find a user by id.
     * 
     * @return
     */
    public List<User> getAll() {
        return userRepo.findAll();
    }


    /**
     * Find one user by id.
     * 
     * @param id
     * @return
     */
    public User getOne(Long id) {
        Optional<User> resp = userRepo.findById(id);
        return resp.orElseThrow(() -> {
            String msg = UserDao.getIdNotFoundMsg(id);
            return new EntityNotFoundException(msg);
        });
    }


    /**
     * Add one user.
     * 
     * @param user
     */
    public void addOne(User user) {
        User resp = userRepo.findByIdOrEmail(user.getId(), user.getEmail());
        if (resp != null) {
            String msg = UserDao.getAlreadyPersistsMsg(user.getId(), user.getEmail());
            throw new EntityExistsException(msg);
        };
        // Do db query
        userRepo.save(user);
    }


    /**
     * Update one user by id.
     * 
     * @param user
     */
    public void updateOne(User user) {
        // Check id not found
        Optional<User> resp = userRepo.findById(user.getId());
        resp.orElseThrow(() -> {
            String msg = UserDao.getIdNotFoundMsg(user.getId());
            throw new EntityNotFoundException(msg);
        });
        // Check email already persists
        User userWithEmail = userRepo.findByEmail(user.getEmail());
        if (userWithEmail != null && userWithEmail.getId() != user.getId()) {
            String msg = UserDao.getEmailAlreadyTakenMsg(user.getEmail());
            throw new RuntimeException(msg);
        }
        // Update user
        userRepo.updateOne(user.getId(), user.getEmail(), user.getName(), user.getPwdHash());
    }


    /**
     * Delete one user by id.
     * 
     * @param id
     */
    public void deleteOne(Long id) {
        // Check id not found
        Optional<User> resp =userRepo.findById(id);
        if (!resp.isPresent()) {
            String msg = UserDao.getIdNotFoundMsg(id);
            throw new EntityNotFoundException(msg);
        }
        // Delete by id
        userRepo.deleteById(id);
    }


    /*****************************************************************************************
     *                                       Helpers
     ****************************************************************************************/
    
    /**
     * Get message for id not found.
     * 
     * @return
     */
    public static String getIdNotFoundMsg(Long id) {
        return ID_NOT_FOUND_MSG_1 + id + ID_NOT_FOUND_MSG_2;
    }


    /**
     * Get message for taken id or email
     * 
     * @return
     */
    public static String getAlreadyPersistsMsg(Long id, String email) {
        return ADD_ERR_MSG + " [id: " + id + ", email: " + email + "]";
    }


    /**
     * Get message for email already taken.
     * 
     * @param email
     * @return
     */
    public static String getEmailAlreadyTakenMsg(String email) {
        return EMAIL_TAKEN_MSG_1 + email + EMAIL_TAKEN_MSG_2;
    }
}

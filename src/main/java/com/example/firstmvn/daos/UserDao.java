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
    private static final String ID_EXISTS_MSG_1 = "User with id \"";
    private static final String ID_EXISTS_MSG_2 = "\" already exists.";
    

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
     * Add one user. ConstraintViolationException should handle email already persisting.
     * 
     * @param user
     */
    public void addOne(User user) throws ConstraintViolationException {
        boolean exists = this.userRepo.existsById(user.getId());
        if (!exists) {
            this.userRepo.save(user);
        } else {
            String msg = UserDao.getIdExistsMsg(user.getId());
            throw new EntityExistsException(msg);
        }
    }


    /**
     * Update one user by id.
     * 
     * @param user
     */
    public void updateOne(User user) {
        // TODO maybe need an entity not found exception here
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
     * So the test classes can get the message
     * @return
     */
    public static String getIdNotFoundMsg(Long id) {
        return UserDao.ID_NOT_FOUND_MSG_1 + id + UserDao.ID_NOT_FOUND_MSG_2;
    }


    /**
     * So the test classes can get the message
     * @return
     */
    public static String getIdExistsMsg(Long id) {
        return UserDao.ID_EXISTS_MSG_1 + id + UserDao.ID_EXISTS_MSG_2;
    }
}

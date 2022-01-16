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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserDao {

    @Autowired
    private IUserRepo userRepo;
    

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
            String msg = "User with id \"" + id + "\" not found.";
            throw new EntityNotFoundException(msg);
        }
    }


    /**
     * Add one user.
     * 
     * @param user
     */
    public void addOne(User user) {
        boolean exists = this.userRepo.existsById(user.getId());
        if (!exists) {
            this.userRepo.save(user);
        } else {
            String msg = "User with id \"" + user.getId() + "\" already exists.";
            throw new EntityExistsException(msg);
        }
    }


    /**
     * Update one user by id.
     * 
     * @param user
     */
    public void updateOne(User user) {
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
}

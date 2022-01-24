/**
 * Handle Rest requests for users.
 * 
 * created by Sean Maxwell, 1/15/2022
 */

package com.example.firstmvn.controllers;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;

import java.util.List;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {

    UserService userService;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
  
    public final static String SUCCESSFUL_POST_MSG = "Thanks For Posting!!";
    public final static String SUCCESSFUL_UPDATE_MSG = "Thanks for Updating!!";
    public final static String SUCCESSFUL_DELETE_MSG = "Thanks for Deleting!!";


    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    /**
     * Fetch all users.
     * 
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        try {
            List<User> users = this.userService.getAll();
            return new ResponseEntity<Object>(users, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Find one user by id.
     * 
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable Long id) {
        try {
            User user = this.userService.getOne(id);
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Add a user.
     * 
     * @param user
     * @return
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<String> addOne(@RequestBody User user) {
        try {
            this.userService.addOne(user);
            return new ResponseEntity<String>(SUCCESSFUL_POST_MSG, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Update a user.
     * 
     * @param user
     * @return
     */
    @PutMapping("")
    @ResponseBody
    public ResponseEntity<String> updateOne(@RequestBody User user) {
        try {
            this.userService.updateOne(user);
            return new ResponseEntity<String>(SUCCESSFUL_UPDATE_MSG, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Delete a user by id.
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteOne(@PathVariable Long id) {
        try {
            this.userService.deleteOne(id);
            return new ResponseEntity<String>(SUCCESSFUL_DELETE_MSG, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

/**
 * Handle Rest requests for users.
 * 
 * created by Sean Maxwell, 1/15/2022
 */

package com.example.firstmvn.controllers;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
  
    private final String successfulPostMsg = "Thanks For Posting!!";
    private final String successfulUpdateMsg = "Thanks for Updating!!";
    

    /**
     * Fetch all users.
     * 
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = this.userService.getAll();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


    /**
     * Find one user by id.
     * 
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable Long id) {
        ResponseEntity<Object> response = null;
        try {
            User user = this.userService.getOne(id);
            response = new ResponseEntity<Object>(user, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
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
        ResponseEntity<String> response = null;
        try {
            this.userService.addOne(user);
            response = new ResponseEntity<String>(this.successfulPostMsg, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
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
        ResponseEntity<String> response = null;
        try {
            this.userService.updateOne(user);
            response = new ResponseEntity<String>(this.successfulUpdateMsg, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    /**
     * Delete a user by id.
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteOne(@PathVariable Long id) {
        this.userService.deleteOne(id);
        return "Thanks for deleting";
    }
}

/**
 * Handle Rest requests for users.
 * 
 * created by Sean Maxwell, 1/15/2022
 */

package com.example.firstmvn.controllers;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;

import java.util.List;

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

    private final UserService userService;
  
    public final static String SUCCESSFUL_POST_MSG = "Thanks For Posting!!";
    public final static String SUCCESSFUL_UPDATE_MSG = "Thanks for Updating!!";
    public final static String SUCCESSFUL_DELETE_MSG = "Thanks for Deleting!!";


    /**
     * Constructor()
     * 
     * @param userService
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Fetch all users.
     * 
     * @return
     */
    @GetMapping("")
    public ResponseEntity<Object> getAll() {
        List<User> users = userService.getAll();
        // pick up here wrap in object
        return new ResponseEntity<Object>(users, HttpStatus.OK);
    }


    /**
     * Find one user by id.
     * 
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable Long id) {
        User user = userService.getOne(id);
        return new ResponseEntity<Object>(user, HttpStatus.OK);
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
        userService.addOne(user);
        return new ResponseEntity<String>(SUCCESSFUL_POST_MSG, HttpStatus.OK);
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
        userService.updateOne(user);
        return new ResponseEntity<String>(SUCCESSFUL_UPDATE_MSG, HttpStatus.OK);
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
        userService.deleteOne(id);
        return new ResponseEntity<String>(SUCCESSFUL_DELETE_MSG, HttpStatus.OK);
    }
}

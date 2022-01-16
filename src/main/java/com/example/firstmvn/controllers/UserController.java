/**
 * Handle Rest requests for users.
 * 
 * created by Sean Maxwell, 1/15/2022
 */

package com.example.firstmvn.controllers;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {
  
    @Autowired
    UserService userService;
    

    /**
     * Fetch all users.
     * 
     * @return
     */
    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = this.userService.getAll();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


    /**
     * Add a user.
     * 
     * @param user
     * @return
     */
    @PostMapping("")
    @ResponseBody
    public String addOne(@RequestBody User user) {
        this.userService.addOne(user);
        return "Thanks For Posting!!";
    }


    /**
     * Update a user.
     * 
     * @param user
     * @return
     */
    @PutMapping("")
    @ResponseBody
    @ExceptionHandler({EntityExistsException.class})
    public String updateOne(@RequestBody User user) {
        this.userService.updateOne(user);
        return "Thanks for updating!!";
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

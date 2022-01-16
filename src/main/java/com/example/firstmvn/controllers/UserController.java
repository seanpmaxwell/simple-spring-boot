package com.example.firstmvn.controllers;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String postResponseController(@RequestBody User user) {
        this.userService.addOne(user);
        return "Thanks For Posting!!!";
    }
}

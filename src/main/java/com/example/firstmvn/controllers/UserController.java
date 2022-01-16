package com.example.firstmvn.controllers;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class UserController {
    
    @Autowired
    UserService userService;
    

    /**
     * Base path.
     * 
     * @return
     */
    @RequestMapping(value = "/")
    public String available() {
      return "Spring in Action";
    }


    /**
     * Fetch all users
     * 
     * @return
     */
    @RequestMapping(value = "/users")
    public ResponseEntity<List<User>> users() {
      List<User> users = this.userService.getUsers();
      
      return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
}

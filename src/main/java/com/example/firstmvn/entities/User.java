package com.example.firstmvn.entities;

import org.springframework.stereotype.Component;


@Component
public class User {
    
    String firstName;
    String lastName;


    public User() {
        this.firstName = "";
        this.lastName = "";
    }


    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getFirstName() {
        return this.firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return this.lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

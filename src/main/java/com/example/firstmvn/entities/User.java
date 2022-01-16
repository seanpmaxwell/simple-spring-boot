package com.example.firstmvn.entities;

import java.sql.Date;

import org.springframework.stereotype.Component;


@Component
public class User {
    
    Long id;
    String email;
    String name;
    String pwdHash;
    Date created;


    public User() {
        this.id = -1L;
        this.email = "";
        this.name = "";
        this.pwdHash = "";
        this.created = new Date(System.currentTimeMillis());
    }


    public User(String name) {
        this.id = -1L;
        this.email = "";
        this.name = name;
        this.pwdHash = "";
        this.created = new Date(System.currentTimeMillis());
    }


    public User(Long id, String email, String name, String pwdHash, Date created) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.pwdHash = pwdHash;
        this.created = created;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return this.email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPwdHash() {
        return this.pwdHash;
    }


    public void setPwdHash(String pwdHash) {
        this.pwdHash = pwdHash;
    }
}

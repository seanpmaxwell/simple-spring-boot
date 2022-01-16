package com.example.firstmvn.repositories;

import com.example.firstmvn.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUserRepo extends JpaRepository<User, Integer> {
    
}

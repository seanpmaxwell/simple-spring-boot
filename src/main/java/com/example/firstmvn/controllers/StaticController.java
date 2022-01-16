package com.example.firstmvn.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class StaticController {

    /**
     * Base path.
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String available() {
      return "Spring in Action";
    }
}

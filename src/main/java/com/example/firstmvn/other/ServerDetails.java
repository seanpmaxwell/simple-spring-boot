package com.example.firstmvn.other;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class ServerDetails {

    private final ServletWebServerApplicationContext server;


    /**
     * Constructor()
     * 
     * @param server
     */
    @Autowired
    public ServerDetails(ServletWebServerApplicationContext server) {
        this.server = server;
    }


    public ServletWebServerApplicationContext getServer() {
        return server;
    }
}

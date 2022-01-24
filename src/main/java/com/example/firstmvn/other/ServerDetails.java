package com.example.firstmvn.other;

import javax.inject.Inject;

import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class ServerDetails {

    private ServletWebServerApplicationContext server;


    @Inject
    public void setServer(ServletWebServerApplicationContext server) {
        this.server = server;
    }


    public ServletWebServerApplicationContext getServer() {
        return server;
    }
}

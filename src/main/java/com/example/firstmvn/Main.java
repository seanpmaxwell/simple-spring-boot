package com.example.firstmvn;

import com.example.firstmvn.services.ServerDetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

// Use this option if you want to not install a jdbc
// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Main {

	public static void main(String[] args) {
		// Start server
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
		// Print out the host:port
		ServerDetails serverDetails = applicationContext.getBean(ServerDetails.class);
		int port = serverDetails.getServer().getWebServer().getPort();
		System.out.println("Spring Boot server started on localhost:" + port);
	}
}

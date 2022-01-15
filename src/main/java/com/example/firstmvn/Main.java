package com.example.firstmvn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

// Use this option if you want to not install a jdbc
// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication()
public class Main {


	/**
	 * Main()
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Start server
		ConfigurableApplicationContext cac = SpringApplication.run(Main.class, args);
		// Print out the host:port
		ServerDetails serverDetails = cac.getBean(ServerDetails.class);
		int port = serverDetails.getServer().getWebServer().getPort();
		System.out.println("Spring Boot server started on localhost:" + port);
	}
}

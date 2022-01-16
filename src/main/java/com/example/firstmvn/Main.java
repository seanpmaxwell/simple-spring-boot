package com.example.firstmvn;

import com.example.firstmvn.other.ServerDetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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

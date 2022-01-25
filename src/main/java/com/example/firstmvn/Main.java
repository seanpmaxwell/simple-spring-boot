package com.example.firstmvn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication()
public class Main {

	private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);


	/**
	 * Main()
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Start server
		ConfigurableApplicationContext cac = SpringApplication.run(Main.class, args);
		// Print out the host:port
		String port = cac.getEnvironment().getProperty("server.port");
		LOGGER.info("Spring Boot server started on localhost:" + port);
	}
}

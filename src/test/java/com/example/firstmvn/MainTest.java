/**
 * Check that the context loads the controllers
 * 
 * created by Sean Maxwell, 1/16/2021
 */

package com.example.firstmvn;

import com.example.firstmvn.controllers.UserController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ContextConfiguration(classes = ServletWebServerApplicationContext.class)
class MainTest {


	@Test
	void contextLoads() {
		// assertThat(controller).isNotNull();
	}
}

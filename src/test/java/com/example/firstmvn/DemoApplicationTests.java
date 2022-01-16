package com.example.firstmvn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest
@ContextConfiguration(classes = ServletWebServerApplicationContext.class)
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}
}

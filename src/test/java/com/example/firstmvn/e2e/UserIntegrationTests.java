/**
 * Integration tests for user routes.
 * Example: https://howtodoinjava.com/spring-boot2/testing/spring-boot-2-junit-5/
 * 
 * created by Sean Maxwell, 1/20/2022
 */

package com.example.firstmvn.e2e;

import com.example.firstmvn.Main;
import com.example.firstmvn.controllers.UserController;
import com.example.firstmvn.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest(classes = Main.class)
@ContextConfiguration(classes = {ServletWebServerApplicationContext.class})
public class UserIntegrationTests {

    User dummyUser1;
    User dummyUser2;
    User dummyUser3;

    @Autowired
    UserController userController;


    /**
     * Setup dummy data.
     */
    @BeforeEach
    public void setUp() {
        this.dummyUser1 = new User(5L, "sean");
        this.dummyUser2 = new User(5L, "max");
        this.dummyUser3 = new User(5L, "jane");
    }


    /**
     * Test reading creating and deleting users.
     */
    @Test
    public void testPostAndGetUsers() {
 
        this.userController.addOne(this.dummyUser1);
 
        // Iterable<Employee> employees = employeeController.read();
        // Assertions.assertThat(employees).first().hasFieldOrPropertyWithValue("firstName", "Lokesh");
 
        // employeeController.delete(employeeResult.getId());
        // Assertions.assertThat(employeeController.read()).isEmpty();
    }
}

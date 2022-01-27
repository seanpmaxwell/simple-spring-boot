/**
 * Integration tests for user routes.
 * Example: https://spring.io/guides/gs/testing-web/
 * 
 * created by Sean Maxwell, 1/20/2022
 */

package com.example.firstmvn.e2e;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.firstmvn.Main;
import com.example.firstmvn.controllers.UserController;
import com.example.firstmvn.entities.User;
import com.example.firstmvn.repositories.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.example.firstmvn.daos.UserDao.getAlreadyPersistsMsg;
import static com.example.firstmvn.daos.UserDao.getIdNotFoundMsg;
import static com.example.firstmvn.daos.UserDao.getEmailAlreadyTakenMsg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.containsString;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ServletWebServerApplicationContext.class})
public class UserIntegrationTests {

    private final String DUMMY_EMAIL = "foo@bar.com";
    private final String DUMMY_NAME = "foo bar";

    private final UserRepo userRepo;
    private final MockMvc mvc;

    private List<User> savedUsers;
    private User savedUser;
    private User unsavedUser;

	
    /**
     * Constructor()
     * 
     * @param userRepo
     */
    @Autowired
    public UserIntegrationTests(UserRepo userRepo, MockMvc mvc) {
        this.userRepo = userRepo;
        this.mvc = mvc;
    }


    /**
     * Setup dummy data.
     */
    @BeforeEach
    void setUp() {
        savedUsers = new ArrayList<User>();
        savedUsers.add(new User("sean@example.com", "sean maxwell"));
        savedUsers.add(new User("john@gmail.com", "john smith"));
        savedUsers.add(new User("jane@yahoo.com", "jane doe"));
        userRepo.saveAll(savedUsers);
        savedUser = userRepo.findAll().get(0);
        unsavedUser = new User("someone@exampl.com", "someone");
    }


    /**
     * Clean up dummy data.
     */
    @AfterEach
    void cleanUp() {
        userRepo.deleteAll();
    }


    /**
     * Test fetching all users.
     */
    @Test
    void getAll() throws Exception {
        var users = savedUsers;
        // Setup request
        var req = get("/api/users")
                    .contentType("application/json");
        // Perform test
        mvc.perform(req)
            .andExpect(jsonPath("[*].id").exists())
            .andExpect(jsonPath("[0].name").value(users.get(0).getName()))
            .andExpect(jsonPath("[1].name").value(users.get(1).getName()))
            .andExpect(jsonPath("[2].name").value(users.get(2).getName()))
            .andExpect(status().isOk());
    }


    /**
     * Test getting one user by id.
     */
    @Test
    void getOne() throws Exception {
        // Setup req
        var req = get("/api/users/" + savedUser.getId())
                    .contentType("application/json");
        // Test
        mvc.perform(req)
            .andExpect(jsonPath("$.id").value(savedUser.getId()))
            .andExpect(jsonPath("$.name").value(savedUser.getName()))
            .andExpect(status().isOk());
    }


    /**
     * Test fetching one who's id is not found.
     */
    @Test
    void getOne_idNotFound() throws Exception {
        Long id = Long.MAX_VALUE;
        var errMsg = getIdNotFoundMsg(id);
        // Setup req
        var req = get("/api/users/" + id)
                    .contentType("application/json");
        // Perform test 
        mvc.perform(req)
            .andExpect(content().string(containsString(errMsg)))
            .andExpect(status().isBadRequest());
    }


    /**
     * Test adding one user.
     */
    @Test
    void addOne() throws Exception {
        // Setup dummy data
        String content = asJsonString(unsavedUser);
        // Setup request
        var req = post("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test
        mvc.perform(req)
            .andExpect(content().string(UserController.SUCCESSFUL_POST_MSG))
            .andExpect(status().isOk());
        // Test data in db
        User user = userRepo.findByEmail(unsavedUser.getEmail());
        assertEquals(unsavedUser.getEmail(), user.getEmail());
    }


    /**
     * Test reading creating and deleting users.
     */
    @Test
    void addOne_alreadyPersistsErr() throws Exception {
        String content = asJsonString(savedUser);
        var errMsg = getAlreadyPersistsMsg(savedUser.getId(), savedUser.getEmail());
        // Setup request
        var req = post("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        mvc.perform(req)
            .andExpect(content().string(containsString(errMsg)))
            .andExpect(status().isBadRequest());
    }


    /**
     * Test updating one user.
     */
    @Test
    void updateOne() throws Exception {
        savedUser.setEmail(DUMMY_EMAIL);
        savedUser.setName(DUMMY_NAME);
        // Setup dummy data
        String content = asJsonString(savedUser);
        // Setup request
        var req = put("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        mvc.perform(req)
            .andExpect(content().string(UserController.SUCCESSFUL_UPDATE_MSG))
            .andExpect(status().isOk());
        // Test data in db
        Optional<User> res = userRepo.findById(savedUser.getId());
        assertEquals(DUMMY_EMAIL, res.get().getEmail());
        assertEquals(DUMMY_NAME, res.get().getName());
    }


    /**
     * Testing updating one id not found.
     */
    @Test 
    void updateOne_idNotFound() throws Exception {
        // Setup dummy data
        var content = asJsonString(unsavedUser);
        var errMsg = getIdNotFoundMsg(unsavedUser.getId());
        // Setup request
        var req = put("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        mvc.perform(req)
            .andExpect(content().string(containsString(errMsg)))
            .andExpect(status().isBadRequest());
    }


    /**
     * PUT "/api/users" email already taken.
     */
    @Test 
    void updateOne_emailTaken() throws Exception {
        // Setup dummy data
        String newEmail = savedUsers.get(1).getEmail();
        savedUser.setEmail(newEmail);
        var content = asJsonString(savedUser);
        // Err msg
        var errMsg = getEmailAlreadyTakenMsg(newEmail);
        // Setup request
        var req = put("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        mvc.perform(req)
            .andExpect(content().string(containsString(errMsg)))
            .andExpect(status().isBadRequest());
    }


    /**
     * Testing delete one.
     */
    @Test 
    void deleteOne() throws Exception {
        // Setup dummy data
        Long id = savedUser.getId();
        // Setup request
        var req = delete("/api/users/" + id)
                    .contentType("application/json");
        // Perform test 
        mvc.perform(req)
            .andExpect(content().string(UserController.SUCCESSFUL_DELETE_MSG))
            .andExpect(status().isOk());
        // Test data in db
        Optional<User> user = userRepo.findById(id);
        assertFalse(user.isPresent());
    }


    /**
     * Testing delete one.
     */
    @Test 
    void deleteOne_idNotFound() throws Exception {
        // Setup dummy data
        Long id = unsavedUser.getId();
        // Throw Error
        var errMsg = getIdNotFoundMsg(id);
        // Setup request
        var req = delete("/api/users/" + id)
                    .contentType("application/json");
        // Perform test 
        mvc.perform(req)
            .andExpect(content().string(containsString(errMsg)))
            .andExpect(status().isBadRequest());
    }


    /**
     * Convert object to json string
     * 
     * @param obj
     * @return
     */
    String asJsonString(final Object obj) {
        try {
          return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }
}

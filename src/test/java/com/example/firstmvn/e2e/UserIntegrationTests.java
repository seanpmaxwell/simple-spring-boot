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
import com.example.firstmvn.repositories.IUserRepo;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = Main.class)
@ContextConfiguration(classes = {ServletWebServerApplicationContext.class})
@AutoConfigureMockMvc
public class UserIntegrationTests {

    private final String DUMMY_EMAIL = "foo@bar.com";
    private final String DUMMY_NAME = "foo bar";

    private final IUserRepo userRepo;

    private List<User> dummyUsers;
    private User savedUser;
    private User unsavedUser;

    @Autowired
	private MockMvc mvc;

    
    /**
     * Constructor()
     * 
     * @param userRepo
     */
    @Autowired
    public UserIntegrationTests(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }


    /**
     * Setup dummy data.
     */
    @BeforeEach
    void setUp() {
        this.dummyUsers = new ArrayList<User>();
        this.dummyUsers.add(new User("sean@example.com", "sean maxwell"));
        this.dummyUsers.add(new User("john@gmail.com", "john smith"));
        this.dummyUsers.add(new User("jane@yahoo.com", "jane doe"));
        this.userRepo.saveAll(this.dummyUsers);
        this.savedUser = this.userRepo.findAll().get(0);
        this.unsavedUser = new User("someone@exampl.com", "someone");
    }


    /**
     * Clean up dummy data.
     */
    @AfterEach
    void cleanUp() {
        this.userRepo.deleteAll();
    }


    /**
     * Test fetching all users.
     */
    @Test
    void getAll() throws Exception {
        var users = this.dummyUsers;
        // Setup request
        var req = get("/api/users/all")
                    .contentType("application/json");
        // Perform test
        this.mvc.perform(req)
            .andExpect(jsonPath("[*].id").exists())
            .andExpect(jsonPath("[0].name").value(users.get(0).getName()))
            .andExpect(jsonPath("[1].name").value(users.get(1).getName()))
            .andExpect(jsonPath("[2].name").value(users.get(2).getName()))
            .andExpect(status().isOk());
    }


    // /**
    //  * Test getting one user by id.
    //  */
    // @Test
    // void getOne() {
    //     Long id = this.savedUser.getId();
    //     ResponseEntity<Object> resp = this.userController.getOne(id);
    //     User user = (User)resp.getBody();
    //     assertEquals(user.getId(), this.savedUser.getId());
    // }


    // /**
    //  * Test fetching one who's id is not found.
    //  */
    // @Test
    // void getOne_idNotFound() {
    //     Long id = Long.MAX_VALUE;
    //     ResponseEntity<Object> resp = this.userController.getOne(id);
    //     String body = (String)resp.getBody();
    //     String msg = getIdNotFoundMsg(id);
    //     assertTrue(body.contains(msg));
    // }


    // /**
    //  * Test adding one user.
    //  */
    // @Test
    // void addOne() {
    //     this.userController.addOne(this.unsavedUser);
    //     User res = this.userRepo.findByEmail(this.unsavedUser.getEmail());
    //     assertEquals(this.unsavedUser.getEmail(), res.getEmail());
    // }


    // /**
    //  * Test reading creating and deleting users.
    //  */
    // @Test
    // void addOne_alreadyPersistsErr() {
    //     ResponseEntity<String> resp = this.userController.addOne(this.savedUser);
    //     String body = (String)resp.getBody();
    //     var errMsg = getAlreadyPersistsMsg(this.savedUser.getId(), this.savedUser.getEmail());
    //     assertTrue(body.contains(errMsg));
    // }


    // /**
    //  * Test updating one user.
    //  */
    // @Test
    // void updateOne() {
    //     User user = this.savedUser;
    //     user.setEmail(this.DUMMY_EMAIL);
    //     user.setName(this.DUMMY_NAME);
    //     this.userController.updateOne(user);
    //     Optional<User> res = this.userRepo.findById(this.savedUser.getId());
    //     assertEquals(this.DUMMY_EMAIL, res.get().getEmail());
    //     assertEquals(this.DUMMY_NAME, res.get().getName());
    // }


    // /**
    //  * Testing updating one id not found.
    //  */
    // @Test 
    // void updateOne_idNotFound() {
    //     User user = this.unsavedUser;
    //     ResponseEntity<String> resp = this.userController.updateOne(user);
    //     String body = (String)resp.getBody();
    //     String msg = getIdNotFoundMsg(user.getId());
    //     assertTrue(body.contains(msg));
    // }


    /**
     * PUT "/api/users" email already taken.
     */
    @Test 
    void updateOne_emailTaken() throws Exception {
        // Setup dummy data
        String newEmail = this.dummyUsers.get(1).getEmail();
        User user = this.savedUser;
        user.setEmail(newEmail);
        var content = this.asJsonString(user);
        // Throw Error
        var errMsg = getEmailAlreadyTakenMsg(newEmail);
        // var exception = new RuntimeException(errMsg);
        // Setup request
        var req = put("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        this.mvc.perform(req)
                .andExpect(content().string(errMsg))
                .andExpect(status().isBadRequest());
    }


    // /**
    //  * Testing delete one.
    //  */
    // @Test 
    // void deleteOne() {
    //     Long id = this.savedUser.getId();
    //     this.userController.deleteOne(id);
    //     Optional<User> user = this.userRepo.findById(id);
    //     assertFalse(user.isPresent());
    // }


    // /**
    //  * Testing delete one.
    //  */
    // @Test 
    // void deleteOne_idNotFound() {
    //     User user = this.unsavedUser;
    //     ResponseEntity<String> resp = this.userController.deleteOne(user.getId());
    //     String body = (String)resp.getBody();
    //     String msg = getIdNotFoundMsg(user.getId());
    //     assertTrue(body.contains(msg));
    // }


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

package com.example.firstmvn.controller;

import com.example.firstmvn.controllers.UserController;
import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.example.firstmvn.daos.UserDao.getIdNotFoundMsg;
import static com.example.firstmvn.daos.UserDao.getAlreadyPersistsMsg;
import static com.example.firstmvn.daos.UserDao.getEmailAlreadyTakenMsg;

import java.util.ArrayList;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;


    private final User TEST_USER = new User(5L, "sean");


    /**
     * GET "/api/users/all"
     * 
     * @throws Exception
     */
    @Test
    void getAll() throws Exception {
        // Setup dummy data
        var users = new ArrayList<User>();
        users.add(new User("Sean maxwell"));
        users.add(new User("Arnold Schwarzenegger"));
        users.add(new User("Sylvestor Stalone"));
        // Mock db call
        when(this.userService.getAll()).thenReturn(users);
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


    /**
     * GET "/api/users/{valid user id}"
     * 
     * @throws Exception
     */
    @Test
    void getOne() throws Exception {
        final Long id = this.TEST_USER.getId();
        // Mock db call
        when(this.userService.getOne(id)).thenReturn(this.TEST_USER);
        // Setup req
        var req = get("/api/users/" + id)
                    .contentType("application/json");
        // Perform test
        this.mvc.perform(req)
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(this.TEST_USER.getName()))
            .andExpect(status().isOk());
    }


    /**
     * GET "/api/users/{invalid user id}"
     * 
     * @throws Exception
     */
    @Test
    void getOne_idNotFound() throws Exception {
        final Long id = this.TEST_USER.getId();
        var errMsg = getIdNotFoundMsg(id);
        var exception = new EntityNotFoundException(errMsg);
        doThrow(exception).when(this.userService).getOne(id);
        // Setup req
        var req = get("/api/users/" + id)
                    .contentType("application/json");
        // Perform test 
        this.mvc.perform(req)
            .andExpect(content().string(errMsg))
            .andExpect(status().isBadRequest());
    }


    /**
     * POST "/api/users"
     * 
     * @throws Exception
     */
    @Test
    void addOne() throws Exception {
        // Setup dummy data
        String content = this.asJsonString(this.TEST_USER);
        // Mock db call
        doNothing().when(this.userService).addOne(any(User.class));
        // Setup request
        var req = post("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        this.mvc
            .perform(req)
            .andExpect(status().isOk());
    }


    /**
     * Test for the exception when a user with the id already exists.
     * 
     * @throws Exception
     */        
    @Test
    void addOne_alreadyPersistsErr() throws Exception {
        // Setup dummy data
        var content = this.asJsonString(this.TEST_USER);
        // Throw Error
        var errMsg = getAlreadyPersistsMsg(this.TEST_USER.getId(), this.TEST_USER.getEmail());
        var exception = new EntityExistsException(errMsg);
        doThrow(exception).when(this.userService).addOne(any(User.class));
        // Setup request
        var req = post("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        this.mvc.perform(req)
                .andExpect(content().string(errMsg))
                .andExpect(status().isBadRequest());
    }


    /**
     * PUT "/api/users"
     * 
     * @throws Exception
     */
    @Test
    void updateOne() throws Exception {
        // Setup dummy data
        String content = this.asJsonString(this.TEST_USER);
        // Mock db call
        doNothing().when(this.userService).updateOne(any(User.class));
        // Setup request
        var req = put("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        this.mvc
            .perform(req)
            .andExpect(status().isOk());
    }


    /**
     * PUT "/api/users" id not found.
     * 
     * @throws Exception
     */
    @Test 
    void updateOne_idNotFound() throws Exception {
        // Setup dummy data
        var content = this.asJsonString(this.TEST_USER);
        // Throw Error
        var errMsg = getIdNotFoundMsg(this.TEST_USER.getId());
        var exception = new RuntimeException(errMsg);
        doThrow(exception).when(this.userService).updateOne(any(User.class));
        // Setup request
        var req = put("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        this.mvc.perform(req)
                .andExpect(content().string(errMsg))
                .andExpect(status().isBadRequest());
    }


    /**
     * PUT "/api/users" email already taken
     * 
     * @throws Exception
     */
    @Test 
    void updateOne_emailTaken() throws Exception {
        // Setup dummy data
        var content = this.asJsonString(this.TEST_USER);
        // Throw Error
        var errMsg = getEmailAlreadyTakenMsg(this.TEST_USER.getEmail());
        var exception = new RuntimeException(errMsg);
        doThrow(exception).when(this.userService).updateOne(any(User.class));
        // Setup request
        var req = put("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        this.mvc.perform(req)
                .andExpect(content().string(errMsg))
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

/**
 * Unit-tests for the user controller. Example taken from here:
 * https://howtodoinjava.com/spring-boot2/testing/spring-boot-2-junit-5/.
 * 
 * created by Sean Maxwell, 1/20/2022
 */

package com.example.firstmvn.controller;

import com.example.firstmvn.controllers.UserController;
import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.example.firstmvn.daos.UserDao.getIdNotFoundMsg;
import static com.example.firstmvn.daos.UserDao.getAlreadyPersistsMsg;
import static com.example.firstmvn.daos.UserDao.getEmailAlreadyTakenMsg;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    private User dummyUser;
    private List<User> dummyUsers;
    
    private MockMvc mvc;

    @MockBean
    private UserService userService;


    @Inject
    public void setMvc(MockMvc mvc) {
        this.mvc = mvc;
    }
    

    /**
     * Setup dummy-data
     */
    @BeforeEach
    public void setUp() {
        this.dummyUser = new User(5L, "sean");
        this.dummyUsers = new ArrayList<User>();
        this.dummyUsers.add(new User("Sean maxwell"));
        this.dummyUsers.add(new User("Arnold Schwarzenegger"));
        this.dummyUsers.add(new User("Sylvestor Stalone"));
    }


    /**
     * GET "/api/users/all"
     * 
     * @throws Exception
     */
    @Test
    void getAll() throws Exception {
        var users = this.dummyUsers;
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
        final Long id = this.dummyUser.getId();
        // Mock db call
        when(this.userService.getOne(id)).thenReturn(this.dummyUser);
        // Setup req
        var req = get("/api/users/" + id)
                    .contentType("application/json");
        // Perform test
        this.mvc.perform(req)
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(this.dummyUser.getName()))
            .andExpect(status().isOk());
    }


    /**
     * GET "/api/users/{invalid user id}"
     * 
     * @throws Exception
     */
    @Test
    void getOne_idNotFound() throws Exception {
        final Long id = this.dummyUser.getId();
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
        String content = this.asJsonString(this.dummyUser);
        // Mock db call
        doNothing().when(this.userService).addOne(any(User.class));
        // Setup request
        var req = post("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test
        this.mvc
            .perform(req)
            .andExpect(content().string(UserController.SUCCESSFUL_POST_MSG))
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
        var content = this.asJsonString(this.dummyUser);
        // Throw Error
        var errMsg = getAlreadyPersistsMsg(this.dummyUser.getId(), this.dummyUser.getEmail());
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
        String content = this.asJsonString(this.dummyUser);
        // Mock db call
        doNothing().when(this.userService).updateOne(any(User.class));
        // Setup request
        var req = put("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        this.mvc
            .perform(req)
            .andExpect(content().string(UserController.SUCCESSFUL_UPDATE_MSG))
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
        var content = this.asJsonString(this.dummyUser);
        // Throw Error
        var errMsg = getIdNotFoundMsg(this.dummyUser.getId());
        var exception = new EntityNotFoundException(errMsg);
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
        var content = this.asJsonString(this.dummyUser);
        // Throw Error
        var errMsg = getEmailAlreadyTakenMsg(this.dummyUser.getEmail());
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
     * DELETE "/api/users" 
     * 
     * @throws Exception
     */
    @Test
    void deleteOne() throws Exception {
        // Setup dummy data
        Long id = this.dummyUser.getId();
        // Mock db call
        doNothing().when(this.userService).deleteOne(id);
        // Setup request
        var req = delete("/api/users/" + id)
                    .contentType("application/json");
        // Perform test 
        this.mvc
            .perform(req)
            .andExpect(content().string(UserController.SUCCESSFUL_DELETE_MSG))
            .andExpect(status().isOk());
    }


    /**
     * DELETE "/api/users" 
     * 
     * @throws Exception
     */
    @Test
    void deleteOne_idNotFound() throws Exception {
        // Setup dummy data
        Long id = this.dummyUser.getId();
        // Throw Error
        var errMsg = getIdNotFoundMsg(this.dummyUser.getId());
        var exception = new EntityNotFoundException(errMsg);
        doThrow(exception).when(this.userService).deleteOne(id);
        // Setup request
        var req = delete("/api/users/" + id)
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

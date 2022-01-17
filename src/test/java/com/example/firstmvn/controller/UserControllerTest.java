package com.example.firstmvn.controller;

import com.example.firstmvn.controllers.UserController;
import com.example.firstmvn.entities.User;
import com.example.firstmvn.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;


    private final User testUser = new User(5L, "sean");


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
        when(userService.getAll()).thenReturn(users);
        // Setup request
        var req = get("/api/users/all")
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
     * GET "/api/users/one"
     * 
     * @throws Exception
     */
    @Test
    void getOne() throws Exception {
        final Long id = this.testUser.getId();
        // Mock db call
        when(userService.getOne(id)).thenReturn(this.testUser);
        // Setup req
        var req = get("/api/users/" + id)
                    .contentType("application/json");
        // Perform test
        mvc.perform(req)
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(this.testUser.getName()))
            .andExpect(status().isOk());
    }


    /**
     * POST "/api/users"
     * 
     * @throws Exception
     */
    @Test
    void addOne() throws Exception {
        // Setup dummy data
        String content = this.asJsonString(this.testUser);
        // Mock db call
        doNothing().when(userService).addOne(any(User.class));
        // Setup request
        var req = post("/api/users")
                    .content(content)
                    .contentType("application/json");
        // Perform test 
        mvc.perform(req)
            .andExpect(status().isOk());
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

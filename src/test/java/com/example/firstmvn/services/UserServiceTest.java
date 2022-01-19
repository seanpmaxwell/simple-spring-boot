/**
 * Unit-tests for the user service.
 * 
 * created by Sean Maxwell, 1/19/2022
 */

package com.example.firstmvn.services;

import com.example.firstmvn.daos.UserDao;
import com.example.firstmvn.entities.User;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
public class UserServiceTest {

    User dummyUser;
    List<User> dummyUsers;
    

    @MockBean
    private UserDao userDao;


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
     * Test getAll() valid.
     * 
     * @throws Exception
     */
    @Test
    void getAll() throws Exception {
        // Mock db call
        when(this.userDao.getAll()).thenReturn(this.dummyUsers);
        var resp = this.userDao.getAll();
        assertEquals(this.dummyUsers, resp);
    }


    /**
     * Test getOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void getOne() throws Exception {
        Long id = this.dummyUser.getId();
        when(this.userDao.getOne(id)).thenReturn(this.dummyUser);
        var resp = this.userDao.getOne(id);
        assertEquals(this.dummyUser, resp);
    }


    @Test 
    void getOne_idNotFound() throws Exception {

    }
}

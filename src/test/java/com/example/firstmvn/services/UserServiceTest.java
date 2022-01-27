/**
 * Unit-tests for the user service.
 * Example from here: https://howtodoinjava.com/spring-boot2/testing/spring-boot-2-junit-5/
 * 
 * created by Sean Maxwell, 1/19/2022
 */

package com.example.firstmvn.services;

import com.example.firstmvn.daos.UserDao;
import com.example.firstmvn.entities.User;
import static com.example.firstmvn.daos.UserDao.getIdNotFoundMsg;
import static com.example.firstmvn.daos.UserDao.getAlreadyPersistsMsg;
import static com.example.firstmvn.daos.UserDao.getEmailAlreadyTakenMsg;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private User dummyUser;
    private List<User> dummyUsers;
    
    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;


    /**
     * Setup dummy-data
     */
    @BeforeEach
    public void setUp() {
        dummyUser = new User(5L, "sean");
        dummyUsers = new ArrayList<User>();
        dummyUsers.add(new User("Sean maxwell"));
        dummyUsers.add(new User("Arnold Schwarzenegger"));
        dummyUsers.add(new User("Sylvestor Stalone"));
    }
    

    /**
     * Test getAll() valid.
     * 
     * @throws Exception
     */
    @Test
    void getAll() throws Exception {
        // Mock db call
        when(userDao.getAll()).thenReturn(dummyUsers);
        var resp = userService.getAll();
        assertEquals(dummyUsers, resp);
    }


    /**
     * Test getOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void getOne() throws Exception {
        Long id = dummyUser.getId();
        when(userDao.getOne(id)).thenReturn(dummyUser);
        var resp = userService.getOne(id);
        assertEquals(dummyUser, resp);
    }


    /**
     * Test getOne() id not found.
     * 
     * @throws Exception
     */
    @Test 
    void getOne_idNotFound() throws Exception {
        Long id = dummyUser.getId();
        // Setup exception
        var errMsg = getIdNotFoundMsg(id);
        var exception = new EntityNotFoundException(errMsg);
        doThrow(exception).when(userDao).getOne(id);
        // Do test
        var ex = assertThrows(EntityNotFoundException.class, () -> userService.getOne(id));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test addOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void addOne() throws Exception {
        doNothing().when(userDao).addOne(dummyUser);
        userService.addOne(dummyUser);
        verify(userDao, times(1)).addOne(dummyUser);
    }


    /**
     * Test addOne() id or email taken.
     * 
     * @throws Exception
     */
    @Test
    void addOne_idOrEmailTaken() throws Exception {
        var user = dummyUser;
        // Setup exception
        var errMsg = getAlreadyPersistsMsg(user.getId(), user.getEmail());
        var exception = new EntityExistsException(errMsg);
        doThrow(exception).when(userDao).addOne(user);
        // Do test
        var ex = assertThrows(EntityExistsException.class, () -> userService.addOne(user));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test updateOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void updateOne() throws Exception {
        doNothing().when(userDao).updateOne(dummyUser);
        userService.updateOne(dummyUser);
        verify(userDao, times(1)).updateOne(dummyUser);
    }


    /**
     * Test updateOne() id not found.
     * 
     * @throws Exception
     */
    @Test
    void updateOne_idNotFound() throws Exception {
        // Setup exception
        var errMsg = getIdNotFoundMsg(dummyUser.getId());
        var exception = new EntityNotFoundException(errMsg);
        doThrow(exception).when(userDao).updateOne(dummyUser);
        // Do test
        var ex = assertThrows(EntityNotFoundException.class, () -> 
            userService.updateOne(dummyUser));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test updateOne() email taken.
     * 
     * @throws Exception
     */
    @Test
    void updateOne_emailTaken() throws Exception {
        // Setup exception
        var errMsg = getEmailAlreadyTakenMsg(dummyUser.getEmail());
        var exception = new RuntimeException(errMsg);
        doThrow(exception).when(userDao).updateOne(dummyUser);
        // Do test
        var ex = assertThrows(RuntimeException.class, () -> 
            userService.updateOne(dummyUser));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test deleteOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void deleteOne() throws Exception {
        doNothing().when(userDao).deleteOne(dummyUser.getId());
        userService.deleteOne(dummyUser.getId());
        verify(userDao, times(1)).deleteOne(dummyUser.getId());
    }


    /**
     * Test deleteOne() id not found.
     * 
     * @throws Exception
     */
    @Test
    void deleteOne_idNotFound() throws Exception {
        // Setup exception
        var errMsg = getIdNotFoundMsg(dummyUser.getId());
        var exception = new EntityNotFoundException(errMsg);
        doThrow(exception).when(userDao).deleteOne(dummyUser.getId());
        // Do test
        var ex = assertThrows(EntityNotFoundException.class, () -> 
            userService.deleteOne(dummyUser.getId()));
        assertEquals(ex.getMessage(), errMsg);
    }
}

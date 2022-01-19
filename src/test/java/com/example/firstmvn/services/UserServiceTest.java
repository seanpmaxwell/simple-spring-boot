/**
 * Unit-tests for the user service.
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
public class UserServiceTest {

    User dummyUser;
    List<User> dummyUsers;
    
    @Autowired
    private UserService userService;

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
        var resp = this.userService.getAll();
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
        var resp = this.userService.getOne(id);
        assertEquals(this.dummyUser, resp);
    }


    /**
     * Test getOne() id not found.
     * 
     * @throws Exception
     */
    @Test 
    void getOne_idNotFound() throws Exception {
        Long id = this.dummyUser.getId();
        // Setup exception
        var errMsg = getIdNotFoundMsg(id);
        var exception = new EntityNotFoundException(errMsg);
        doThrow(exception).when(this.userDao).getOne(id);
        // Do test
        var ex = assertThrows(EntityNotFoundException.class, () -> this.userService.getOne(id));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test addOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void addOne() throws Exception {
        doNothing().when(this.userDao).addOne(this.dummyUser);
        userService.addOne(this.dummyUser);
        verify(this.userDao, times(1)).addOne(this.dummyUser);
    }


    /**
     * Test addOne() id or email taken.
     * 
     * @throws Exception
     */
    @Test
    void addOne_idOrEmailTaken() throws Exception {
        var user = this.dummyUser;
        // Setup exception
        var errMsg = getAlreadyPersistsMsg(user.getId(), user.getEmail());
        var exception = new EntityExistsException(errMsg);
        doThrow(exception).when(this.userDao).addOne(user);
        // Do test
        var ex = assertThrows(EntityExistsException.class, () -> this.userService.addOne(user));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test updateOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void updateOne() throws Exception {
        doNothing().when(this.userDao).updateOne(this.dummyUser);
        userService.updateOne(this.dummyUser);
        verify(this.userDao, times(1)).updateOne(this.dummyUser);
    }


    /**
     * Test updateOne() id not found.
     * 
     * @throws Exception
     */
    @Test
    void updateOne_idNotFound() throws Exception {
        // Setup exception
        var errMsg = getIdNotFoundMsg(this.dummyUser.getId());
        var exception = new EntityNotFoundException(errMsg);
        doThrow(exception).when(this.userDao).updateOne(this.dummyUser);
        // Do test
        var ex = assertThrows(EntityNotFoundException.class, () -> 
            this.userService.updateOne(this.dummyUser));
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
        var errMsg = getEmailAlreadyTakenMsg(this.dummyUser.getEmail());
        var exception = new RuntimeException(errMsg);
        doThrow(exception).when(this.userDao).updateOne(this.dummyUser);
        // Do test
        var ex = assertThrows(RuntimeException.class, () -> 
            this.userService.updateOne(this.dummyUser));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test deleteOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void deleteOne() throws Exception {
        doNothing().when(this.userDao).deleteOne(this.dummyUser.getId());
        userService.deleteOne(this.dummyUser.getId());
        verify(this.userDao, times(1)).deleteOne(this.dummyUser.getId());
    }


    /**
     * Test deleteOne() id not found.
     * 
     * @throws Exception
     */
    @Test
    void deleteOne_idNotFound() throws Exception {
        // Setup exception
        var errMsg = getIdNotFoundMsg(this.dummyUser.getId());
        var exception = new EntityNotFoundException(errMsg);
        doThrow(exception).when(this.userDao).deleteOne(this.dummyUser.getId());
        // Do test
        var ex = assertThrows(EntityNotFoundException.class, () -> 
            this.userService.deleteOne(this.dummyUser.getId()));
        assertEquals(ex.getMessage(), errMsg);
    }
}

/**
 * Unit-tests for the UserDao class.
 * 
 * created by Sean Maxwell, 1/19/2022
 */

package com.example.firstmvn.daos;

import com.example.firstmvn.entities.User;
import com.example.firstmvn.repositories.IUserRepo;
import static com.example.firstmvn.daos.UserDao.getIdNotFoundMsg;
import static com.example.firstmvn.daos.UserDao.getAlreadyPersistsMsg;
import static com.example.firstmvn.daos.UserDao.getEmailAlreadyTakenMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserDao.class)
public class UserDaoTest {

    User dummyUser;
    List<User> dummyUsers;
    List<User> persistingUser;
    
    @Autowired
    private UserDao userDao;

    @MockBean
    private IUserRepo userRepo;


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
        this.persistingUser = new ArrayList<User>();
        this.persistingUser.add(new User(101L, "Tina"));
    }

    
    /**
     * Test getAll() valid.
     * 
     * @throws Exception
     */
    @Test
    void getAll() throws Exception {
        when(this.userRepo.findAll()).thenReturn(this.dummyUsers);
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
        // Setup db response
        Long id = this.dummyUser.getId();
        Optional<User> dbResp = Optional.ofNullable(this.dummyUser);
        when(this.userRepo.findById(id)).thenReturn(dbResp);
        // Do test
        var resp = this.userDao.getOne(id);
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
        Optional<User> dbResp = Optional.ofNullable(null);
        when(this.userRepo.findById(id)).thenReturn(dbResp);
        // Do test
        var errMsg = getIdNotFoundMsg(id);
        var ex = assertThrows(EntityNotFoundException.class, () -> this.userDao.getOne(id));
        assertEquals(ex.getMessage(), errMsg);
    }
    

    /**
     * Test addOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void addOne() throws Exception {
        Long id = this.dummyUser.getId();
        String email = this.dummyUser.getEmail();
        var emptyList = new ArrayList<User>();
        // Dummy data
        when(this.userRepo.findByIdOrEmail(id, email)).thenReturn(emptyList);
        when(this.userRepo.save(this.dummyUser)).thenReturn(this.dummyUser);
        // Do test
        this.userDao.addOne(this.dummyUser);
        verify(this.userRepo, times(1)).save(this.dummyUser);
    }


    /**
     * Test addOne() id or email taken.
     * 
     * @throws Exception
     */
    @Test
    void addOne_idOrEmailTaken() throws Exception {
        Long id = this.dummyUser.getId();
        String email = this.dummyUser.getEmail();
        // Dummy data
        when(this.userRepo.findByIdOrEmail(id, email)).thenReturn(this.persistingUser);
        // Do test
        var errMsg = getAlreadyPersistsMsg(id, email);
        var ex = assertThrows(EntityExistsException.class, () -> 
            this.userDao.addOne(this.dummyUser));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test updateOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void updateOne() throws Exception {
        var user = this.dummyUser;
        // Setup Mocks
        Optional<User> dbUser = Optional.ofNullable(user);
        when(this.userRepo.findById(user.getId())).thenReturn(dbUser);
        when(this.userRepo.findByEmail(user.getEmail())).thenReturn(user);
        // Do test
        this.userDao.updateOne(user);
        verify(this.userRepo, times(1)).updateOne(user.getId(), user.getEmail(), user.getName(), 
            user.getPwdHash());
    }


    /**
     * Test updateOne() id not found.
     * 
     * @throws Exception
     */
    @Test
    void updateOne_idNotFound() throws Exception {
        var user = this.dummyUser;
        // Setup Mocks
        Optional<User> dbUser = Optional.ofNullable(null);
        when(this.userRepo.findById(user.getId())).thenReturn(dbUser);
        // Do test
        var errMsg = getIdNotFoundMsg(user.getId());
        var ex = assertThrows(EntityNotFoundException.class, () -> this.userDao.updateOne(user));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test updateOne() email taken.
     * 
     * @throws Exception
     */
    @Test
    void updateOne_emailTaken() throws Exception {
        var takenEmail = "foo@bar.com";
        var user = this.dummyUser;
        user.setEmail(takenEmail);
        var diffUser = new User(1L, takenEmail, "sam");
        // Setup Mocks
        Optional<User> dbUser = Optional.ofNullable(user);
        when(this.userRepo.findById(user.getId())).thenReturn(dbUser);
        when(this.userRepo.findByEmail(user.getEmail())).thenReturn(diffUser);
        // Do test
        var errMsg = getEmailAlreadyTakenMsg(user.getEmail());
        var ex = assertThrows(RuntimeException.class, () -> this.userDao.updateOne(user));
        assertEquals(ex.getMessage(), errMsg);
    }


    /**
     * Test deleteOne() valid.
     * 
     * @throws Exception
     */
    @Test
    void deleteOne() throws Exception {
        var id = this.dummyUser.getId();
        // Setup mocks
        Optional<User> dbUser = Optional.ofNullable(this.dummyUser);
        when(this.userRepo.findById(id)).thenReturn(dbUser);
        // Do test
        this.userDao.deleteOne(id);
        verify(this.userRepo, times(1)).deleteById(id);
    }


    /**
     * Test deleteOne() id not found.
     * 
     * @throws Exception
     */
    @Test
    void deleteOne_idNotFound() throws Exception {
        var id = this.dummyUser.getId();
        // Setup mocks
        Optional<User> dbUser = Optional.ofNullable(null);
        when(this.userRepo.findById(id)).thenReturn(dbUser);
        // Do test
        var errMsg = getIdNotFoundMsg(id);
        var ex = assertThrows(RuntimeException.class, () -> this.userDao.deleteOne(id));
        assertEquals(ex.getMessage(), errMsg);
    }
}

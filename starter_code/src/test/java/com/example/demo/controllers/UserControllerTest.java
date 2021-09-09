package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import javassist.tools.web.BadHttpRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws BadHttpRequest {
        //arrange
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        //act
        ResponseEntity<User> response = userController.createUser(request);
        User user = response.getBody();
        //assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void test_findById() {
        //arrange
        long id = 222;
        User user = new User();
        user.setUsername("user_1");
        user.setPassword("testPassword1");
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        //act
        final ResponseEntity<User> response = userController.findById(id);
        User u = response.getBody();
        //assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(u);
        assertEquals(222, u.getId());
        assertEquals("user_1", u.getUsername());
        assertEquals("testPassword1", u.getPassword());
    }

    @Test
    public void test_findByUserName() throws BadHttpRequest {
        //arrange
        long id = 333;
        User user = new User();
        user.setUsername("user_2");
        user.setPassword("testPassword2");
        user.setId(id);
        when(userRepository.findByUsername("test")).thenReturn(user);
        //act
        final ResponseEntity<User> response = userController.findByUserName("test");
        User u = response.getBody();
        //assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(u);
        assertEquals(333, u.getId());
        assertEquals("user_2", u.getUsername());
        assertEquals("testPassword2", u.getPassword());
    }
}

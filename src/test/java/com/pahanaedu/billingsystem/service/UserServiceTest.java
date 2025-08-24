package com.pahanaedu.billingsystem.service;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dao.UserDAO;
import com.pahanaedu.billingsystem.dto.SignInDetailsDTO;
import com.pahanaedu.billingsystem.dto.UserDTO;
import com.pahanaedu.billingsystem.entity.User;
import com.pahanaedu.billingsystem.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:16:23
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // Mock the UserDAO to simulate database interactions
    @Mock
    private UserDAO userDAO;

    // Inject the mocked UserDAO into the service implementation
    @InjectMocks
    private UserServiceImpl userService;

    // DTO and Entity objects for testing
    private SignInDetailsDTO signInDetailsDTO;
    private UserDTO userDTO;
    private User userEntity;

    @BeforeEach
    void setUp() {
        // Initialize common objects before each test
        signInDetailsDTO = new SignInDetailsDTO("testuser", "password123");
        userDTO = new UserDTO(1, "testuser", "password123");
        userEntity = new User(1, "testuser", "password123");
    }

    //------------------------------------------ verifyPassword Tests ------------------------------------------//

    @Test
    void testVerifyPassword_Success() throws SQLException, NotFoundException, ConstrainViolationException {
        // Mock the DAO to return a user entity when getByPk is called
        when(userDAO.getByPk("testuser", any(Connection.class))).thenReturn(userEntity);

        // Call the service method
        boolean result = userService.verifyPassword(signInDetailsDTO);

        // Assert that the result is true
        assertTrue(result);

        // Verify that the getByPk method was called exactly once
        verify(userDAO, times(1)).getByPk("testuser", any(Connection.class));
    }

    @Test
    void testVerifyPassword_InvalidPassword() throws SQLException, NotFoundException, ConstrainViolationException {
        // Create a user entity with a different password to simulate an invalid password case
        User userEntityWithWrongPassword = new User(1, "testuser", "wrongpassword");
        when(userDAO.getByPk("testuser", any(Connection.class))).thenReturn(userEntityWithWrongPassword);

        // Call the service method
        boolean result = userService.verifyPassword(signInDetailsDTO);

        // Assert that the result is false
        assertFalse(result);

        // Verify that the getByPk method was called exactly once
        verify(userDAO, times(1)).getByPk("testuser", any(Connection.class));
    }

    @Test
    void testVerifyPassword_NotFoundException() throws SQLException {
        // Mock the DAO to return null, simulating that the user was not found
        when(userDAO.getByPk("nonexistentuser", any(Connection.class))).thenReturn(null);

        // Assert that the service method throws a NotFoundException
        assertThrows(NotFoundException.class, () -> userService.verifyPassword(new SignInDetailsDTO("nonexistentuser", "password123")));

        // Verify that the getByPk method was called exactly once
        verify(userDAO, times(1)).getByPk("nonexistentuser", any(Connection.class));
    }

    @Test
    void testVerifyPassword_SQLException() throws SQLException {
        // Mock the DAO to throw a SQLException
        when(userDAO.getByPk(anyString(), any(Connection.class))).thenThrow(new SQLException("Database error"));

        // Assert that the service method throws a SQLException
        assertThrows(SQLException.class, () -> userService.verifyPassword(signInDetailsDTO));

        // Verify that the getByPk method was called exactly once
        verify(userDAO, times(1)).getByPk(anyString(), any(Connection.class));
    }

    //------------------------------------------ getUser Tests ------------------------------------------//

    @Test
    void testGetUser_Success() throws SQLException, NotFoundException {
        // Mock the DAO to return the user entity
        when(userDAO.getByPk("testuser", any(Connection.class))).thenReturn(userEntity);

        // Call the service method
        UserDTO result = userService.getUser("testuser");

        // Assert that the returned DTO is not null and has the correct username
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());

        // Verify that the getByPk method was called exactly once
        verify(userDAO, times(1)).getByPk("testuser", any(Connection.class));
    }

    @Test
    void testGetUser_NotFoundException() throws SQLException {
        // Mock the DAO to return null
        when(userDAO.getByPk("nonexistentuser", any(Connection.class))).thenReturn(null);

        // Assert that the service method throws a NotFoundException
        assertThrows(NotFoundException.class, () -> userService.getUser("nonexistentuser"));

        // Verify that the getByPk method was called exactly once
        verify(userDAO, times(1)).getByPk("nonexistentuser", any(Connection.class));
    }
}

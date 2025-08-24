package com.pahanaedu.billingsystem.controller;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dto.SignInDetailsDTO;
import com.pahanaedu.billingsystem.service.UserService;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-20
 * Time:23:08
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserController userController;

    private StringWriter responseWriter;
    private Jsonb jsonb;
    private SignInDetailsDTO signInDetailsDTO;

    @BeforeEach
    void setUp() throws IOException {
        // Mock the response's writer to capture the JSON output
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Initialize Jsonb for JSON serialization/deserialization
        jsonb = JsonbBuilder.create();

        // Create mock data for SignInDetailsDTO
        signInDetailsDTO = new SignInDetailsDTO("testuser", "password123");
    }

    // ------------------------------------------ DoPost Tests ------------------------------------------//

    @Test
    void testDoPost_Success() throws Exception {
        // Mock the request with a valid JSON payload
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(signInDetailsDTO))));

        // Mock the service to return true, simulating a successful login
        when(userService.verifyPassword(any(SignInDetailsDTO.class))).thenReturn(true);

        // Call the method under test
        userController.doPost(request, response);

        // Verify that the response status is set to SC_OK
        verify(response).setStatus(HttpServletResponse.SC_OK);

        // Verify that the service method was called
        verify(userService).verifyPassword(any(SignInDetailsDTO.class));

        // Verify the response content
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Login successfully"));
    }

    @Test
    void testDoPost_InvalidPassword() throws Exception {
        // Mock the request with a valid JSON payload
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(signInDetailsDTO))));

        // Mock the service to return false, simulating an invalid password
        when(userService.verifyPassword(any(SignInDetailsDTO.class))).thenReturn(false);

        // Call the method under test
        userController.doPost(request, response);

        // Verify that the response status is set to SC_UNAUTHORIZED
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Verify the response content
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Invalid password!"));
    }

    @Test
    void testDoPost_NotFoundException() throws Exception {
        // Mock the request with a valid JSON payload
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(signInDetailsDTO))));

        // Mock the service to throw a NotFoundException, e.g., if the user doesn't exist
        when(userService.verifyPassword(any(SignInDetailsDTO.class))).thenThrow(new NotFoundException("User not found"));

        // Call the method under test
        userController.doPost(request, response);

        // Verify that the response status is set to SC_INTERNAL_SERVER_ERROR
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        // Verify the response content
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Login Failed!"));
        assertTrue(responseJson.contains("User not found"));
    }

    @Test
    void testDoPost_SQLException() throws Exception {
        // Mock the request with a valid JSON payload
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(signInDetailsDTO))));

        // Mock the service to throw a SQLException, e.g., a database error
        when(userService.verifyPassword(any(SignInDetailsDTO.class))).thenThrow(new SQLException("Database connection failed"));

        // Call the method under test
        userController.doPost(request, response);

        // Verify that the response status is set to SC_INTERNAL_SERVER_ERROR
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        // Verify the response content
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Login Failed!"));
        assertTrue(responseJson.contains("Database connection failed"));
    }
}


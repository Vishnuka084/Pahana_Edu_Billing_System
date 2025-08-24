package com.pahanaedu.billingsystem.controller;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dto.OrderDTO;
import com.pahanaedu.billingsystem.dto.OrderDetailsDTO;
import com.pahanaedu.billingsystem.service.OrderService;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-19
 * Time:14:36
 */
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private OrderController orderController;

    private StringWriter responseWriter;
    private Jsonb jsonb;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() throws IOException {
        // Mock the response's writer to capture the JSON output
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Initialize Jsonb for JSON serialization/deserialization
        jsonb = JsonbBuilder.create();

        // Create mock data for OrderDTO
        List<OrderDetailsDTO> orderDetails = Arrays.asList(
                new OrderDetailsDTO("I002","O002",8.0,2,230.00),
                new OrderDetailsDTO("I001","O001",2.0,2,200.00)
        );
        orderDTO = new OrderDTO("O001", "C001", new Date(), 25.00, orderDetails);
    }

    // ------------------------------------------ DoPost Tests ------------------------------------------//

    @Test
    void testDoPost_Success() throws ServletException, IOException, SQLException, NotFoundException, ConstrainViolationException {
        // Mock the request to simulate a valid JSON payload
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(orderDTO))));

        // Mock the service method to do nothing, indicating a successful operation
        doNothing().when(orderService).addNewOrder(any(OrderDTO.class));

        // Call the method under test
        orderController.doPost(request, response);

        // Verify that the service method was called exactly once with any OrderDTO
        verify(orderService, times(1)).addNewOrder(any(OrderDTO.class));

        // Verify that the response contains the success message
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Order successfully recoded!"));

        // Note: The original controller code does not set a success status (e.g., SC_CREATED).
        // It's good practice to add a test for this, but the current implementation doesn't allow it.
        // For example: verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPost_SQLException() throws ServletException, IOException, SQLException, NotFoundException, ConstrainViolationException {
        // Mock the request with valid data
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(orderDTO))));

        // Mock the service to throw a SQLException
        doThrow(new SQLException("Database connection error")).when(orderService).addNewOrder(any(OrderDTO.class));

        // Call the method under test
        orderController.doPost(request, response);

        // Verify that the response status is set to SC_INTERNAL_SERVER_ERROR
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        // Verify that the response contains the error message
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Order not record!"));
        assertTrue(responseJson.contains("Database connection error"));
    }

    @Test
    void testDoPost_NotFoundException() throws ServletException, IOException, SQLException, NotFoundException, ConstrainViolationException {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(orderDTO))));
        doThrow(new NotFoundException("Customer not found")).when(orderService).addNewOrder(any(OrderDTO.class));

        orderController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Order not record!"));
        assertTrue(responseJson.contains("Customer not found"));
    }

    @Test
    void testDoPost_ConstrainViolationException() throws ServletException, IOException, SQLException, NotFoundException, ConstrainViolationException {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(orderDTO))));
        doThrow(new ConstrainViolationException("Constraint violation")).when(orderService).addNewOrder(any(OrderDTO.class));

        orderController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Order not record!"));
        assertTrue(responseJson.contains("Constraint violation"));
    }
}
package com.pahanaedu.billingsystem.service;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dao.CustomerDAO;
import com.pahanaedu.billingsystem.dao.ItemDAO;
import com.pahanaedu.billingsystem.dao.OrderDAO;
import com.pahanaedu.billingsystem.dto.OrderDTO;
import com.pahanaedu.billingsystem.dto.OrderDetailsDTO;
import com.pahanaedu.billingsystem.entity.Customer;
import com.pahanaedu.billingsystem.entity.Item;
import com.pahanaedu.billingsystem.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:19
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    // Mock the DAO dependencies to simulate database interactions
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private CustomerDAO customerDAO;
    @Mock
    private ItemDAO itemDAO;

    // Inject the mocked DAOs into the service implementation
    @InjectMocks
    private OrderServiceImpl orderService;

    // DTO objects for testing
    private OrderDTO orderDTO;
    private OrderDetailsDTO orderDetailsDTO;

    // Entity objects for mocking DAO responses
    private Customer customerEntity;
    private Item itemEntity;

    @BeforeEach
    void setUp() {
        // Initialize common DTOs and entities before each test
        orderDetailsDTO = new OrderDetailsDTO("O001", "I001", 1200.00, 1, 1200.00);
        itemEntity = new Item("I001", "Laptop", 1200.00, 50);
        customerEntity = new Customer("C001", "John Doe", "123 Main St", "123-456-7890", 0);

        orderDTO = new OrderDTO();
        orderDTO.setOrderId("O001");
        orderDTO.setCustomerId("C001");
        orderDetailsDTO.setOrderID("O001"); // Ensure DTO has a valid order ID
        orderDTO.setOrderDetails(Collections.singletonList(orderDetailsDTO));
    }

    //------------------------------------------ addNewOrder Tests ------------------------------------------//

    @Test
    void testAddNewOrder_Success() throws SQLException, NotFoundException, ConstrainViolationException {
        // Mock the DAO methods to simulate a successful order
        when(customerDAO.getByPk(anyString(), any(Connection.class))).thenReturn(customerEntity);
        when(itemDAO.getByPk(anyString(), any(Connection.class))).thenReturn(itemEntity);
        // The CrudDAO interface's add method returns a boolean, so we mock it to return true
        when(orderDAO.add(any(), any(Connection.class))).thenReturn(true);
        when(itemDAO.update(any(), any(Connection.class))).thenReturn(itemEntity);

        // Call the service method
        assertDoesNotThrow(() -> orderService.addNewOrder(orderDTO));

        // Verify that the required DAO methods were called
        verify(customerDAO, times(1)).getByPk("C001", any(Connection.class));
        verify(itemDAO, times(1)).getByPk("I001", any(Connection.class));
        verify(orderDAO, times(1)).add(any(), any(Connection.class));
        verify(itemDAO, times(1)).update(any(), any(Connection.class));
    }

    @Test
    void testAddNewOrder_NotFoundException_Customer() throws SQLException {
        // Mock the DAO to simulate that the customer is not found
        when(customerDAO.getByPk(anyString(), any(Connection.class))).thenReturn(null);

        // Assert that the service method throws a NotFoundException
        assertThrows(NotFoundException.class, () -> orderService.addNewOrder(orderDTO));

        // Verify that the customerDAO was called, but the orderDAO and itemDAO were not
        verify(customerDAO, times(1)).getByPk("C001", any(Connection.class));
        verify(orderDAO, never()).add(any(), any(Connection.class));
    }

    @Test
    void testAddNewOrder_NotFoundException_Item() throws SQLException, NotFoundException {
        // Mock the DAO to simulate that the item is not found
        when(customerDAO.getByPk(anyString(), any(Connection.class))).thenReturn(customerEntity);
        when(itemDAO.getByPk(anyString(), any(Connection.class))).thenReturn(null);

        // Assert that the service method throws a NotFoundException
        assertThrows(NotFoundException.class, () -> orderService.addNewOrder(orderDTO));

        // Verify that the customerDAO and itemDAO were called, but the orderDAO was not
        verify(customerDAO, times(1)).getByPk("C001", any(Connection.class));
        verify(itemDAO, times(1)).getByPk("I001", any(Connection.class));
        verify(orderDAO, never()).add(any(), any(Connection.class));
    }

    @Test
    void testAddNewOrder_ConstrainViolationException_InsufficientStock() throws SQLException, NotFoundException {
        // Adjust mock data to simulate insufficient stock
        orderDetailsDTO.setQty(100);

        // Mock the DAOs to return existing entities
        when(customerDAO.getByPk(anyString(), any(Connection.class))).thenReturn(customerEntity);
        when(itemDAO.getByPk(anyString(), any(Connection.class))).thenReturn(itemEntity);

        // Assert that the service method throws a ConstrainViolationException
        assertThrows(ConstrainViolationException.class, () -> orderService.addNewOrder(orderDTO));

        // Verify that the DAOs were called, but the add method was not
        verify(customerDAO, times(1)).getByPk("C001", any(Connection.class));
        verify(itemDAO, times(1)).getByPk("I001", any(Connection.class));
        verify(orderDAO, never()).add(any(), any(Connection.class));
    }

    @Test
    void testAddNewOrder_SQLException() throws SQLException, NotFoundException {
        // Mock the DAO to throw a SQLException
        when(customerDAO.getByPk(anyString(), any(Connection.class))).thenThrow(new SQLException("Database connection error"));

        // Assert that the service method throws a SQLException
        assertThrows(SQLException.class, () -> orderService.addNewOrder(orderDTO));

        // Verify that the DAO method was called
        verify(customerDAO, times(1)).getByPk("C001", any(Connection.class));
        verify(itemDAO, never()).getByPk(anyString(), any(Connection.class));
    }
}

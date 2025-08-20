package com.pahanaedu.billingsystem.service;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dao.CustomerDAO;
import com.pahanaedu.billingsystem.dto.CustomerDTO;
import com.pahanaedu.billingsystem.entity.Customer;
import com.pahanaedu.billingsystem.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-18
 * Time:22:50
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    // Mock the CustomerDAO to simulate database interactions
    @Mock
    private CustomerDAO customerDAO;

    // Inject the mocked CustomerDAO into the service implementation
    @InjectMocks
    private CustomerServiceImpl customerService;

    // DTO and Entity objects for testing
    private CustomerDTO customerDTO;
    private Customer customerEntity;

    @BeforeEach
    void setUp() {
        // Initialize common objects before each test
        customerDTO = new CustomerDTO("C001", "John Doe", "123 Main St", "123-456-7890", 0);
        customerEntity = new Customer("C001", "John Doe", "123 Main St", "123-456-7890", 0);
    }

    //------------------------------------------ addNewCustomer Tests ------------------------------------------//

    @Test
    void testAddNewCustomer_Success() throws SQLException {
        // Corrected method name from 'save' to 'add' and added a mock for the Connection
        when(customerDAO.add(any(Customer.class), any(Connection.class))).thenReturn(true);

        // Call the service method
        boolean result = customerService.addNewCustomer(customerDTO);

        // Assert that the result is true
        assertTrue(result);

        // Verify that the 'add' method on the DAO was called exactly once with the correct arguments
        verify(customerDAO, times(1)).add(any(Customer.class), any(Connection.class));
    }

    @Test
    void testAddNewCustomer_SQLException() throws SQLException {
        // Corrected method name from 'save' to 'add' and added a mock for the Connection
        when(customerDAO.add(any(Customer.class), any(Connection.class))).thenThrow(new SQLException("Database error"));

        // Assert that the service method throws a SQLException
        assertThrows(SQLException.class, () -> customerService.addNewCustomer(customerDTO));
    }

    //------------------------------------------ updateCustomer Tests ------------------------------------------//

    @Test
    void testUpdateCustomer_Success() throws SQLException {
        // Corrected method name from 'save' to 'update' and added a mock for the Connection
        when(customerDAO.update(any(Customer.class), any(Connection.class))).thenReturn(customerEntity);

        // Call the service method
        CustomerDTO result = customerService.updateCustomer(customerDTO);

        // Assert that the result is not null and has the correct account number
        assertNotNull(result);
        assertEquals("C001", result.getAccountNumber());

        // Verify that the 'update' method was called exactly once with the correct arguments
        verify(customerDAO, times(1)).update(any(Customer.class), any(Connection.class));
    }

    //------------------------------------------ deleteCustomer Tests ------------------------------------------//

    @Test
    void testDeleteCustomer_Success() throws SQLException, NotFoundException {
        // Corrected method name from 'findById' to 'getByPk' and added a mock for the Connection.
        // Also, adjusted the return value from Optional to the entity itself.
        when(customerDAO.getByPk("C001", any(Connection.class))).thenReturn(customerEntity);
        // Corrected method name from 'deleteById' to 'delete' and added a mock for the Connection.
        doNothing().when(customerDAO).delete("C001", any(Connection.class));

        // Call the service method
        assertDoesNotThrow(() -> customerService.deleteCustomer("C001"));

        // Verify that 'getByPk' and 'delete' were called exactly once with the correct arguments
        verify(customerDAO, times(1)).getByPk("C001", any(Connection.class));
        verify(customerDAO, times(1)).delete("C001", any(Connection.class));
    }

    @Test
    void testDeleteCustomer_NotFoundException() throws SQLException {
        // Corrected method name from 'findById' to 'getByPk' and added a mock for the Connection.
        // Return null to simulate a not-found scenario.
        when(customerDAO.getByPk("C002", any(Connection.class))).thenReturn(null);

        // Assert that the service method throws a NotFoundException
        assertThrows(NotFoundException.class, () -> customerService.deleteCustomer("C002"));
    }

    //------------------------------------------ getCustomerByAccNum Tests ------------------------------------------//

    @Test
    void testGetCustomerByAccNum_Success() throws SQLException, NotFoundException {
        // Corrected method name from 'findById' to 'getByPk' and added a mock for the Connection
        when(customerDAO.getByPk("C001", any(Connection.class))).thenReturn(customerEntity);

        // Call the service method
        CustomerDTO result = customerService.getCustomerByAccNum("C001");

        // Assert the result is not null and has the correct full name
        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());

        // Verify that the 'getByPk' method was called exactly once
        verify(customerDAO, times(1)).getByPk("C001", any(Connection.class));
    }

    @Test
    void testGetCustomerByAccNum_NotFoundException() throws SQLException {
        // Corrected method name from 'findById' to 'getByPk' and added a mock for the Connection
        when(customerDAO.getByPk("C002", any(Connection.class))).thenReturn(null);

        // Assert that the service method throws a NotFoundException
        assertThrows(NotFoundException.class, () -> customerService.getCustomerByAccNum("C002"));
    }

    //------------------------------------------ getAll Tests ------------------------------------------//

    @Test
    void testGetAll_Success() throws SQLException {
        // Corrected method name from 'findAll' to 'getAll' and added a mock for the Connection
        List<Customer> customers = Arrays.asList(
                customerEntity,
                new Customer("C002", "Jane Smith", "456 Oak Ave", "987-654-3210", 0)
        );
        when(customerDAO.getAll(any(Connection.class))).thenReturn(customers);

        // Call the service method
        List<CustomerDTO> result = customerService.getAll();

        // Assert that the result is not null and has the correct size
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify that the 'getAll' method was called exactly once
        verify(customerDAO, times(1)).getAll(any(Connection.class));
    }
}

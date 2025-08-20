package com.pahanaedu.billingsystem.service;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dao.ItemDAO;
import com.pahanaedu.billingsystem.dto.ItemDTO;
import com.pahanaedu.billingsystem.entity.Item;
import com.pahanaedu.billingsystem.service.impl.ItemServiceImpl;
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
 * Date:2025-08-16
 * Time:14:19
 */
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    // Mock the ItemDAO to simulate database interactions
    @Mock
    private ItemDAO itemDAO;

    // Inject the mocked ItemDAO into the service implementation
    @InjectMocks
    private ItemServiceImpl itemService;

    // DTO and Entity objects for testing
    private ItemDTO itemDTO;
    private Item itemEntity;

    @BeforeEach
    void setUp() {
        // Initialize common objects before each test
        itemDTO = new ItemDTO("I001", "Laptop", 1200.00, 50);
        itemEntity = new Item("I001", "Laptop", 1200.00, 50);
    }

    //------------------------------------------ addNewItem Tests ------------------------------------------//

    @Test
    void testAddNewItem_Success() throws SQLException {
        // Mock the DAO to return true when a new item is added
        when(itemDAO.add(any(Item.class), any(Connection.class))).thenReturn(true);

        // Call the service method
        boolean result = itemService.addNewItem(itemDTO);

        // Assert that the result is true
        assertTrue(result);

        // Verify that the 'add' method on the DAO was called exactly once
        verify(itemDAO, times(1)).add(any(Item.class), any(Connection.class));
    }

    @Test
    void testAddNewItem_SQLException() throws SQLException {
        // Mock the DAO to throw a SQLException when adding a new item
        when(itemDAO.add(any(Item.class), any(Connection.class))).thenThrow(new SQLException("Database error"));

        // Assert that the service method throws a SQLException
        assertThrows(SQLException.class, () -> itemService.addNewItem(itemDTO));
    }

    //------------------------------------------ updateItem Tests ------------------------------------------//

    @Test
    void testUpdateItem_Success() throws SQLException {
        // Mock the DAO to return the updated entity
        when(itemDAO.update(any(Item.class), any(Connection.class))).thenReturn(itemEntity);

        // Call the service method
        ItemDTO result = itemService.updateItem(itemDTO);

        // Assert that the result is not null and has the correct item code
        assertNotNull(result);
        assertEquals("I001", result.getItemCode());

        // Verify that the 'update' method was called exactly once
        verify(itemDAO, times(1)).update(any(Item.class), any(Connection.class));
    }

    //------------------------------------------ deleteItem Tests ------------------------------------------//

    @Test
    void testDeleteItem_Success() throws SQLException, NotFoundException {
        // Mock the DAO's getByPk method to return the entity, indicating it exists
        when(itemDAO.getByPk("I001", any(Connection.class))).thenReturn(itemEntity);
        // Mock the DAO's delete method to do nothing, indicating success
        doNothing().when(itemDAO).delete("I001", any(Connection.class));

        // Call the service method
        assertDoesNotThrow(() -> itemService.deleteItem("I001"));

        // Verify that getByPk and delete were called exactly once
        verify(itemDAO, times(1)).getByPk("I001", any(Connection.class));
        verify(itemDAO, times(1)).delete("I001", any(Connection.class));
    }

    @Test
    void testDeleteItem_NotFoundException() throws SQLException {
        // Mock the DAO to return null, indicating the item was not found
        when(itemDAO.getByPk("I002", any(Connection.class))).thenReturn(null);

        // Assert that the service method throws a NotFoundException
        assertThrows(NotFoundException.class, () -> itemService.deleteItem("I002"));
    }

    //------------------------------------------ getAll Tests ------------------------------------------//

    @Test
    void testGetAll_Success() throws SQLException {
        // Create a list of mock item entities
        List<Item> items = Arrays.asList(
                itemEntity,
                new Item("I002", "Mouse", 25.00, 150)
        );
        // Mock the DAO to return the list of items
        when(itemDAO.getAll(any(Connection.class))).thenReturn(items);

        // Call the service method
        List<ItemDTO> result = itemService.getAll();

        // Assert that the result is not null and has the correct size
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify that the 'getAll' method was called exactly once
        verify(itemDAO, times(1)).getAll(any(Connection.class));
    }

    //------------------------------------------ getItemByCode Tests ------------------------------------------//

    @Test
    void testGetItemByCode_Success() throws SQLException, NotFoundException {
        // Mock the DAO to return the item entity
        when(itemDAO.getByPk("I001", any(Connection.class))).thenReturn(itemEntity);

        // Call the service method
        ItemDTO result = itemService.getItemByCode("I001");

        // Assert the result is not null and has the correct description
        assertNotNull(result);
        assertEquals("Laptop", result.getDescription());

        // Verify that the 'getByPk' method was called exactly once
        verify(itemDAO, times(1)).getByPk("I001", any(Connection.class));
    }

    @Test
    void testGetItemByCode_NotFoundException() throws SQLException {
        // Mock the DAO to return null
        when(itemDAO.getByPk("I002", any(Connection.class))).thenReturn(null);

        // Assert that the service method throws a NotFoundException
        assertThrows(NotFoundException.class, () -> itemService.getItemByCode("I002"));
    }
}

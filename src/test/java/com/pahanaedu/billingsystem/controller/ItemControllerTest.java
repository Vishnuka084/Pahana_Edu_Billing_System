package com.pahanaedu.billingsystem.controller;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dto.ItemDTO;
import com.pahanaedu.billingsystem.service.ItemService;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-18
 * Time:23:02
 */

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ItemController itemController;

    private StringWriter responseWriter;
    private Jsonb jsonb;
    private ItemDTO itemDTO;

    @BeforeEach
    void setUp() throws IOException {
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jsonb = JsonbBuilder.create();
        itemDTO = new ItemDTO("I001", "Laptop", 1200.00, 50);
    }

    //------------------------------------------ DoGet Tests ------------------------------------------//

    @Test
    void testDoGetWithItemCode() throws Exception {
        when(request.getParameter("code")).thenReturn("I001");
        when(itemService.getItemByCode("I001")).thenReturn(itemDTO);

        itemController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(itemService).getItemByCode("I001");
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Item found successfully"));
    }

    @Test
    void testDoGetWithItemCode_NotFound() throws Exception {
        when(request.getParameter("code")).thenReturn("I002");
        when(itemService.getItemByCode("I002")).thenThrow(new NotFoundException("Item not found"));

        itemController.doGet(request, response);

        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Item not found"));
    }

    @Test
    void testDoGetWithoutItemCode() throws Exception {
        when(request.getParameter("code")).thenReturn(null);
        when(itemService.getAll()).thenReturn(Collections.singletonList(itemDTO));

        itemController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(itemService).getAll();
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Item list found successfully"));
    }

    //------------------------------------------ DoPost Tests ------------------------------------------//

    @Test
    void testDoPost_Success() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(itemDTO))));
        when(itemService.addNewItem(any(ItemDTO.class))).thenReturn(true);

        itemController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(itemService).addNewItem(any(ItemDTO.class));
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Item successfully added!"));
    }

    @Test
    void testDoPost_UnsupportedMediaType() throws Exception {
        when(request.getContentType()).thenReturn("text/plain");

        itemController.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        verify(itemService, never()).addNewItem(any(ItemDTO.class));
    }

    //------------------------------------------ DoPut Tests ------------------------------------------//

    @Test
    void testDoPut_Success() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(itemDTO))));
        when(itemService.updateItem(any(ItemDTO.class))).thenReturn(itemDTO);

        itemController.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(itemService).updateItem(any(ItemDTO.class));
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Item successfully updated!"));
    }

    @Test
    void testDoPut_ItemNotUpdated() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(itemDTO))));
        when(itemService.updateItem(any(ItemDTO.class))).thenReturn(null);

        itemController.doPut(request, response);

        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Item not updated!"));
    }

    //------------------------------------------ DoDelete Tests ------------------------------------------//

    @Test
    void testDoDelete_Success() throws Exception {
        when(request.getParameter("code")).thenReturn("I001");
        doNothing().when(itemService).deleteItem("I001");

        itemController.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(itemService).deleteItem("I001");
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Item successfully deleted"));
    }

    @Test
    void testDoDelete_ConstrainViolation() throws Exception {
        when(request.getParameter("code")).thenReturn("I002");
        doThrow(new ConstrainViolationException("Cannot delete item")).when(itemService).deleteItem("I002");

        itemController.doDelete(request, response);

        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Item not deleted!"));
        assertTrue(responseJson.contains("Cannot delete item"));
    }
}

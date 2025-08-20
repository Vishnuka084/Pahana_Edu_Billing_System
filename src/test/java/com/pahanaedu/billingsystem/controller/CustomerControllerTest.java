package com.pahanaedu.billingsystem.controller;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dto.CustomerDTO;
import com.pahanaedu.billingsystem.service.CustomerService;
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
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-18
 * Time:22:51
 */
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private CustomerController customerController;

    private StringWriter responseWriter;
    private Jsonb jsonb;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() throws IOException {
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jsonb = JsonbBuilder.create();
        // Correctly initialize CustomerDTO with all fields, including accountNumber.
        customerDTO = new CustomerDTO("C001", "Vishnuka", "Galle", "0783456234", 12);
    }

    //------------------------------------------ DoGet Tests ------------------------------------------//

    @Test
    void testDoGetWithCustomerId() throws Exception {
        // Use the account number "C001" which is in the customerDTO object.
        when(request.getParameter("id")).thenReturn("C001");
        when(customerService.getCustomerByAccNum("C001")).thenReturn(customerDTO);

        customerController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(customerService).getCustomerByAccNum("C001");

        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Customer found successfully"));
    }

    @Test
    void testDoGetWithCustomerId_NotFound() throws Exception {
        when(request.getParameter("id")).thenReturn("C002");
        when(customerService.getCustomerByAccNum("C002")).thenThrow(new NotFoundException("Customer not found"));

        customerController.doGet(request, response);

        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Customer not found"));
    }

    @Test
    void testDoGetWithoutCustomerId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);
        when(customerService.getAll()).thenReturn(Collections.singletonList(customerDTO));

        customerController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(customerService).getAll();
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Customers found successfully"));
    }

    //------------------------------------------ DoPost Tests ------------------------------------------//

    @Test
    void testDoPost_Success() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(customerDTO))));
        when(customerService.addNewCustomer(any(CustomerDTO.class))).thenReturn(true);

        customerController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(customerService).addNewCustomer(any(CustomerDTO.class));
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Customer successfully added!"));
    }

    @Test
    void testDoPost_UnsupportedMediaType() throws Exception {
        when(request.getContentType()).thenReturn("text/plain");

        customerController.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        verify(customerService, never()).addNewCustomer(any(CustomerDTO.class));
    }

    //------------------------------------------ DoPut Tests ------------------------------------------//

    @Test
    void testDoPut_Success() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(customerDTO))));
        when(customerService.updateCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        customerController.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(customerService).updateCustomer(any(CustomerDTO.class));
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Customer successfully updated!"));
    }

    @Test
    void testDoPut_CustomerNotFound() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonb.toJson(customerDTO))));
        when(customerService.updateCustomer(any(CustomerDTO.class))).thenReturn(null);

        customerController.doPut(request, response);

        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Customer not updated!"));
    }

    //------------------------------------------ DoDelete Tests ------------------------------------------//

    @Test
    void testDoDelete_Success() throws Exception {
        // Use a consistent ID. "C001" is a good choice.
        when(request.getParameter("id")).thenReturn("C001");
        doNothing().when(customerService).deleteCustomer("C001");

        customerController.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(customerService).deleteCustomer("C001");
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Customer successfully deleted"));
    }

    @Test
    void testDoDelete_CustomerNotFound() throws Exception {
        when(request.getParameter("id")).thenReturn("C002");
        doThrow(new NotFoundException("Customer not found")).when(customerService).deleteCustomer("C002");

        customerController.doDelete(request, response);

        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
        String responseJson = responseWriter.toString();
        assertTrue(responseJson.contains("Customer not deleted!"));
        assertTrue(responseJson.contains("Customer not found"));
    }
}
package com.pahanaedu.billingsystem.controller;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.config.ServiceFactory;
import com.pahanaedu.billingsystem.dto.CustomerDTO;
import com.pahanaedu.billingsystem.dto.RespondsDTO;
import com.pahanaedu.billingsystem.service.CustomerService;
import com.pahanaedu.billingsystem.type.ServiceTypes;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:33
 */
public class CustomerController extends HttpServlet {
    private CustomerService customerService;
    private BasicDataSource bds;

    public void init() {
        bds = (BasicDataSource) this.getServletContext().getAttribute("bds");

        customerService = (CustomerService) ServiceFactory.getServiceFactory().getService(ServiceTypes.CUSTOMER,bds);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Jsonb jsonb = JsonbBuilder.create();
        String customerID = req.getParameter("id");
        resp.setContentType("application/json");

        try {

            if (customerID!=null){
                // find specific customer
                CustomerDTO customerByAccNum = customerService.getCustomerByAccNum(customerID);

                resp.setStatus(HttpServletResponse.SC_OK);
                jsonb.toJson(new RespondsDTO(200, "Customer found successfully", customerByAccNum), resp.getWriter());

            }else {
                List<CustomerDTO> allCustomers = customerService.getAll();

                resp.setStatus(HttpServletResponse.SC_OK);
                jsonb.toJson(new RespondsDTO(200, "Customers found successfully", allCustomers), resp.getWriter());
            }

        } catch (SQLException | NotFoundException e) {
            jsonb.toJson(new RespondsDTO(400, "Error", e.getLocalizedMessage()), resp.getWriter());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // ensure req type is json
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        // set resp type to json
        resp.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);

        System.out.println(customerDTO.toString());

        try {
            // call service class
            if (customerService.addNewCustomer(customerDTO)){
                resp.setStatus(HttpServletResponse.SC_CREATED);
                jsonb.toJson(
                        new RespondsDTO(
                                200,
                                "Customer successfully added!",
                                ""), resp.getWriter());
            }
        }catch (SQLException | NotFoundException e){
            e.printStackTrace();
            jsonb.toJson(new RespondsDTO(400, "Error !", e.getLocalizedMessage()), resp.getWriter());
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // ensure req type is json
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        // set resp type to json
        resp.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);

        System.out.println(customerDTO.toString());

        try {
            // call service class
            CustomerDTO updateCustomer = customerService.updateCustomer(customerDTO);
            if (updateCustomer!=null){
                resp.setStatus(HttpServletResponse.SC_OK);
                jsonb.toJson(
                        new RespondsDTO(
                                200,
                                "Customer successfully updated!",
                                updateCustomer), resp.getWriter());
            }else {
                jsonb.toJson(new RespondsDTO(400, "Customer not updated!",""), resp.getWriter());
            }
        }catch (SQLException e){
            e.printStackTrace();
            jsonb.toJson(new RespondsDTO(400, "Error !", e.getLocalizedMessage()), resp.getWriter());
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // set resp type to json
        resp.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        String customerID = req.getParameter("id");

        System.out.println("Customer ID : "+customerID);

        try {
            customerService.deleteCustomer(customerID);
            resp.setStatus(HttpServletResponse.SC_OK);
            jsonb.toJson(new RespondsDTO(200, "Customer successfully deleted", ""), resp.getWriter());
        }catch (SQLException | ConstrainViolationException | NotFoundException e){
            jsonb.toJson(new RespondsDTO(400, "Customer not deleted!", e.getLocalizedMessage()), resp.getWriter());
            e.printStackTrace();
        }

    }
}

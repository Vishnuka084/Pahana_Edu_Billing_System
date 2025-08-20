package com.pahanaedu.billingsystem.controller;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.config.ServiceFactory;
import com.pahanaedu.billingsystem.dto.OrderDTO;
import com.pahanaedu.billingsystem.dto.RespondsDTO;
import com.pahanaedu.billingsystem.service.OrderService;
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

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-19
 * Time:14:36
 */
public class OrderController extends HttpServlet {

    private OrderService orderService;
    private BasicDataSource bds;

    public void init() {
        bds = (BasicDataSource) this.getServletContext().getAttribute("bds");

        orderService = (OrderService) ServiceFactory.getServiceFactory().getService(ServiceTypes.ORDER,bds);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Jsonb jsonb = JsonbBuilder.create();
        OrderDTO orderDTO = jsonb.fromJson(req.getReader(), OrderDTO.class);

        System.out.println(orderDTO.toString());
        System.out.println(orderDTO.getOrderDetails().toString());

        // call order service add method
        try {

            orderService.addNewOrder(orderDTO);
            jsonb.toJson(new RespondsDTO(200, "Order successfully recoded!",""), resp.getWriter());

        }catch (SQLException | NotFoundException | ConstrainViolationException e){

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonb.toJson(new RespondsDTO(400, "Order not record!", e.getLocalizedMessage()), resp.getWriter());
        }



    }
}

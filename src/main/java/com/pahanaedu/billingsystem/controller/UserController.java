package com.pahanaedu.billingsystem.controller;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.config.ServiceFactory;
import com.pahanaedu.billingsystem.dto.RespondsDTO;
import com.pahanaedu.billingsystem.dto.SignInDetailsDTO;
import com.pahanaedu.billingsystem.dto.UserDTO;
import com.pahanaedu.billingsystem.service.UserService;
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
 * Date:2025-08-16
 * Time:16:39
 */
public class UserController extends HttpServlet {
    private UserService userService;
    private BasicDataSource bds;

    public void init() {
        bds = (BasicDataSource) this.getServletContext().getAttribute("bds");

        userService = (UserService) ServiceFactory.getServiceFactory().getService(ServiceTypes.USER,bds);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Jsonb jsonb = JsonbBuilder.create();
        try {

            SignInDetailsDTO signInDetailsDTO = jsonb.fromJson(req.getReader(), SignInDetailsDTO.class);

            boolean verifyPassword = userService.verifyPassword(signInDetailsDTO);

            if (verifyPassword) {
                resp.setStatus(HttpServletResponse.SC_OK);
                jsonb.toJson(new RespondsDTO(200, "Login successfully", ""), resp.getWriter());
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonb.toJson(new RespondsDTO(401, "Invalid password!", ""), resp.getWriter());
            }


        } catch (SQLException | NotFoundException | ConstrainViolationException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonb.toJson(new RespondsDTO(400, "Login Failed!", e.getLocalizedMessage()), resp.getWriter());
        }

    }
}

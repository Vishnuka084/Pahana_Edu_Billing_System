package com.pahanaedu.billingsystem.api;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.config.ServiceFactory;
import com.pahanaedu.billingsystem.dto.ItemDTO;
import com.pahanaedu.billingsystem.dto.RespondsDTO;
import com.pahanaedu.billingsystem.service.ItemService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:35
 */
public class ItemHandler extends HttpServlet {
    private ItemService itemService;
    private BasicDataSource bds;

    public void init() {
        bds = (BasicDataSource) this.getServletContext().getAttribute("bds");

        itemService = (ItemService) ServiceFactory.getServiceFactory().getService(ServiceTypes.ITEM,bds);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Jsonb jsonb = JsonbBuilder.create();
        String itemCode = req.getParameter("code");
        resp.setContentType("application/json");

        try {

            if (itemCode!=null){
                // find specific customer
                ItemDTO itemByCode = itemService.getItemByCode(itemCode);

                resp.setStatus(HttpServletResponse.SC_OK);
                jsonb.toJson(new RespondsDTO(200, "Item found successfully", itemByCode), resp.getWriter());
            }else {
                List<ItemDTO> list = itemService.getAll();

                resp.setStatus(HttpServletResponse.SC_OK);
                jsonb.toJson(new RespondsDTO(200, "Item list found successfully", list), resp.getWriter());
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
        ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

        System.out.println(itemDTO.toString());

        try {
            // call service class
            if (itemService.addNewItem(itemDTO)){
                resp.setStatus(HttpServletResponse.SC_CREATED);
                jsonb.toJson(
                        new RespondsDTO(
                                200,
                                "Item successfully added!",
                                ""), resp.getWriter());
            }
        }catch (SQLException e){
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
        ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

        System.out.println(itemDTO.toString());

        try {
            // call service class
            ItemDTO updateItem = itemService.updateItem(itemDTO);
            if (updateItem!=null){
                resp.setStatus(HttpServletResponse.SC_OK);
                jsonb.toJson(
                        new RespondsDTO(
                                200,
                                "Item successfully updated!",
                                updateItem), resp.getWriter());
            }else {
                jsonb.toJson(new RespondsDTO(400, "Item not updated!",""), resp.getWriter());
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
        String code = req.getParameter("code");

        System.out.println("Item Code : "+code);

        try {
            itemService.deleteItem(code);
            resp.setStatus(HttpServletResponse.SC_OK);
            jsonb.toJson(new RespondsDTO(200, "Item successfully deleted", ""), resp.getWriter());
        }catch (SQLException | ConstrainViolationException e){
            jsonb.toJson(new RespondsDTO(400, "Item not deleted!", e.getLocalizedMessage()), resp.getWriter());
            e.printStackTrace();
        }
    }
}

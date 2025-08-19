package com.pahanaedu.billingsystem.api;

import java.io.IOException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-19
 * Time:14:36
 */
public class OrderHandler extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Jsonb jsonb = JsonbBuilder.create();
        OrderDTO orderDTO = jsonb.fromJson(req.getReader(), OrderDTO.class);

        System.out.println(orderDTO.toString());
        System.out.println(orderDTO.getOrderDetails().toString());

        jsonb.toJson(new RespondsDTO(400, "Item not updated!",""), resp.getWriter());

    }
}

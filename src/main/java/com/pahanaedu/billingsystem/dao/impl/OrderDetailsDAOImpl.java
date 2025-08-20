package com.pahanaedu.billingsystem.dao.impl;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.dao.OrderDetailsDAO;
import com.pahanaedu.billingsystem.entity.OrderDetails;
import com.pahanaedu.billingsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-20
 * Time:13:09
 */
public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public boolean add(OrderDetails orderDetails, Connection connection) throws SQLException {

        return DBUtil.executeUpdate(connection, "INSERT INTO OrderDetail VALUES(?,?,?,?)", orderDetails.getOrderID(),
                orderDetails.getItemCode(), orderDetails.getQty(), orderDetails.getUnitPrice());

    }

    @Override
    public void delete(String s, Connection connection) throws SQLException, ConstrainViolationException {

    }

    @Override
    public OrderDetails update(OrderDetails entity, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<OrderDetails> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public OrderDetails getByPk(String s, Connection connection) throws SQLException {
        return null;
    }
}

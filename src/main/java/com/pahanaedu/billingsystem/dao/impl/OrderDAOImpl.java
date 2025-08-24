package com.pahanaedu.billingsystem.dao.impl;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.dao.OrderDAO;
import com.pahanaedu.billingsystem.entity.Orders;
import com.pahanaedu.billingsystem.entity.SuperEntity;
import com.pahanaedu.billingsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:34
 */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean add(Orders order, Connection connection) throws SQLException {
        return DBUtil.executeUpdate(connection, "INSERT INTO Orders VALUES(?,?,?,?)", order.getOrderId(),
                order.getCustomerId(), order.getOrderDate(), order.getTotal());
    }

    @Override
    public void delete(String s, Connection connection) throws SQLException, ConstrainViolationException {

    }

    @Override
    public Orders update(Orders entity, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Orders> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public Orders getByPk(String s, Connection connection) throws SQLException {
        return null;
    }
}

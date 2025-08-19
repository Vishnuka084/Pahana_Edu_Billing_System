package com.pahanaedu.billingsystem.dao.impl;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.dao.OrderDAO;
import com.pahanaedu.billingsystem.entity.SuperEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:34
 */
public class OrderDAOImpl  implements OrderDAO {

    @Override
    public boolean add(SuperEntity entity, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public void delete(Object o, Connection connection) throws SQLException, ConstrainViolationException {

    }

    @Override
    public SuperEntity update(SuperEntity entity, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public SuperEntity getByPk(Object o, Connection connection) throws SQLException {
        return null;
    }
}

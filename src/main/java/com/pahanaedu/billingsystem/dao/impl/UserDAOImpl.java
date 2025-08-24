package com.pahanaedu.billingsystem.dao.impl;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.dao.UserDAO;
import com.pahanaedu.billingsystem.entity.User;
import com.pahanaedu.billingsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:16:25
 */
public class UserDAOImpl implements UserDAO {
    @Override
    public boolean add(User entity, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public void delete(String s, Connection connection) throws SQLException, ConstrainViolationException {

    }

    @Override
    public User update(User entity, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<User> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public User getByPk(String pk, Connection connection) throws SQLException {
        ResultSet resultSet =
                DBUtil.executeQuery(connection, "SELECT * FROM Users WHERE username=?",pk);
        User user = null;

        if (resultSet.next()){
            user = new User(
                    resultSet.getInt("userId"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
        }

        return user;
    }
}

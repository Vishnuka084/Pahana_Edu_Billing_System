package com.pahanaedu.billingsystem.dao;

import com.pahanaedu.billingsystem.entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:22
 */

public interface CustomerDAO extends CrudDAO<Customer, String> {
    String getNextCustomerId(Connection connection) throws SQLException;
}

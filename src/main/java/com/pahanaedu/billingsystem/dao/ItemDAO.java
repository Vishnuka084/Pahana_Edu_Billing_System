package com.pahanaedu.billingsystem.dao;

import com.pahanaedu.billingsystem.entity.Item;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:22
 */

public interface ItemDAO extends CrudDAO<Item, String> {

    boolean reduceItemQty(String itemCode,int qty, Connection connection) throws SQLException;

    public String getNextItemCode(Connection connection) throws SQLException;

}

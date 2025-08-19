package com.pahanaedu.billingsystem.dao;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.entity.SuperEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:33
 */

public interface CrudDAO <T extends SuperEntity,ID> extends SuperDAO {

    boolean add(T entity, Connection connection) throws SQLException;

    void delete(ID id, Connection connection) throws SQLException, ConstrainViolationException;

    T update(T entity,Connection connection) throws SQLException;

    List<T> getAll(Connection connection) throws SQLException;

    T getByPk(ID id,Connection connection) throws SQLException;

}

package com.pahanaedu.billingsystem.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:49
 */
public class DBUtil {

    private static PreparedStatement getPreparedStatement(Connection connection, String sql, Object[] args) throws SQLException {

        PreparedStatement pstm = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        for (int i = 0; i < args.length; i++) {
            pstm.setObject(i + 1, args[i]);
        }
        return pstm;
    }

    // return affected row count
    public static boolean executeUpdate(Connection connection, String sql, Object... args) throws SQLException {
        return DBUtil.getPreparedStatement(connection, sql, args).executeUpdate() > 0;
    }

    // return result set
    public static ResultSet executeQuery(Connection connection, String sql, Object... args) throws SQLException {
        return DBUtil.getPreparedStatement(connection, sql, args).executeQuery();
    }
}

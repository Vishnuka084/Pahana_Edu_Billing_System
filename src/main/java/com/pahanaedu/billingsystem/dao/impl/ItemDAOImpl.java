package com.pahanaedu.billingsystem.dao.impl;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.dao.ItemDAO;
import com.pahanaedu.billingsystem.entity.Item;
import com.pahanaedu.billingsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:34
 */
public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Item item, Connection connection) throws SQLException {
        return DBUtil.executeUpdate(
                connection,
                "INSERT INTO Item VALUES(?,?,?,?)",
                item.getItemCode(),
                item.getDescription(),
                item.getUnitPrice(),
                item.getQtyOnHand()
        );
    }

    @Override
    public void delete(String code, Connection connection) throws SQLException, ConstrainViolationException {
        if (!DBUtil.executeUpdate(connection, "DELETE FROM Item WHERE itemCode=?", code)){
            throw new ConstrainViolationException("Item not deleted!");
        }
    }

    @Override
    public Item update(Item item, Connection connection) throws SQLException {
        boolean isSuccess = DBUtil.executeUpdate(
                connection,
                "UPDATE Item SET description=?, unitPrice=?, qtyOnHand=? WHERE itemCode=?",
                item.getDescription(),
                item.getUnitPrice(),
                item.getQtyOnHand(),
                item.getItemCode()
        );
        return isSuccess ? item : null;
    }

    @Override
    public List<Item> getAll(Connection connection) throws SQLException {
        return getList(DBUtil.executeQuery(connection,"SELECT * FROM Item"));
    }

    @Override
    public Item getByPk(String pk, Connection connection) throws SQLException {
        System.out.println("In Item  ID : "+pk);
        ResultSet resultSet =
                DBUtil.executeQuery(connection, "SELECT * FROM Item WHERE itemCode=?",pk);
        Item itemById = null;

        if (resultSet.next()){
            itemById = new Item(
                    resultSet.getString("itemCode"),
                    resultSet.getString("description"),
                    resultSet.getDouble("unitPrice"),
                    resultSet.getInt("qtyOnHand")
            );
        }

//        System.out.println(itemById);

        return itemById;
    }

    private List<Item> getList(ResultSet resultSet) throws SQLException {

        List<Item> itemList = new ArrayList<>();

        while (resultSet.next()) {

            itemList.add(
                    new Item(
                            resultSet.getString("itemCode"),
                            resultSet.getString("description"),
                            resultSet.getDouble("unitPrice"),
                            resultSet.getInt("qtyOnHand")
                    ));
        }

        return itemList;
    }

    @Override
    public boolean reduceItemQty(String itemCode, int qty, Connection connection) throws SQLException {
        return DBUtil.executeUpdate(connection, "UPDATE Item SET qtyOnHand=qtyOnHand-? WHERE itemCode=?", qty, itemCode);
    }

    @Override
    public String getNextItemCode(Connection connection) throws SQLException {
        ResultSet resultSet =
                DBUtil.executeQuery(connection,"SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1");
        String lastCode="";
        if (resultSet.next()){
            System.out.println("recode ekak set una");
            lastCode=resultSet.getString("itemCode");
        }
        return generateNextItemCode(lastCode);
    }

    private static String generateNextItemCode(String lastCode) {

        System.out.println("Old code : "+lastCode);

        if (!lastCode.isEmpty()){
            String[] strings = lastCode.split("-");
            int num = Integer.parseInt(strings[1]);
            num += 1;

            String newOrderId=String.format("-%04d",num);
            return "I"+newOrderId;
        }

        return "I-0001";
    }
}

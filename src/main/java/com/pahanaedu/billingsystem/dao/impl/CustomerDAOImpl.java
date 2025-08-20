package com.pahanaedu.billingsystem.dao.impl;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.dao.CustomerDAO;
import com.pahanaedu.billingsystem.entity.Customer;
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
 * Time:13:33
 */
public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean add(Customer customer, Connection connection) throws SQLException {

        return DBUtil.executeUpdate(
                connection,
                "INSERT INTO Customer VALUES(?,?,?,?,?)",
                customer.getAccountNumber(),
                customer.getFullName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getUniteConsumed()
        );
//        return null;
    }

    @Override
    public void delete(String id, Connection connection) throws SQLException , ConstrainViolationException{
        if (!DBUtil.executeUpdate(connection, "DELETE FROM Customer WHERE accountNumber=?", id)){
            throw new ConstrainViolationException("Customer not deleted!");
        }
    }

    @Override
    public Customer update(Customer customer,Connection connection) throws SQLException {
        boolean isSuccess = DBUtil.executeUpdate(
                connection,
                "UPDATE Customer SET fullName=?, address=?, phoneNumber=?, uniteConsumed=? WHERE accountNumber=?",
                customer.getFullName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getUniteConsumed(),
                customer.getAccountNumber()
        );
        return isSuccess ? customer : null;
    }

    @Override
    public List<Customer> getAll(Connection connection) throws SQLException {

        return getList(DBUtil.executeQuery(connection,"SELECT * FROM Customer"));
    }

    @Override
    public Customer getByPk(String pk, Connection connection) throws SQLException {
        ResultSet resultSet =
                DBUtil.executeQuery(connection, "SELECT * FROM Customer WHERE accountNumber=?", pk);
        Customer customerById = null;
        while (resultSet.next()){
            customerById = new Customer(
                    resultSet.getString("accountNumber"),
                    resultSet.getString("fullName"),
                    resultSet.getString("address"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getInt("uniteConsumed")
            );
        }

        return customerById;

    }

    private List<Customer> getList(ResultSet resultSet) throws SQLException {

        List<Customer> customerList = new ArrayList<>();

        while (resultSet.next()) {

            customerList.add(
                    new Customer(
                            resultSet.getString("accountNumber"),
                            resultSet.getString("fullName"),
                            resultSet.getString("address"),
                            resultSet.getString("phoneNumber"),
                            resultSet.getInt("uniteConsumed")
                    ));
        }

        return customerList;
    }

    @Override
    public String getNextCustomerId(Connection connection) throws SQLException {
        ResultSet resultSet =
                DBUtil.executeQuery(connection,"SELECT accountNumber FROM Customer ORDER BY accountNumber DESC LIMIT 1");
        String lastCode="";
        if (resultSet.next()){
            System.out.println("recode ekak set una");
            lastCode=resultSet.getString("accountNumber");
        }
        return generateNextCustomerID(lastCode);
    }

    private static String generateNextCustomerID(String lastCode) {

        System.out.println("Old cus code : "+lastCode);

        if (!lastCode.isEmpty()){
            String[] strings = lastCode.split("-");
            int num = Integer.parseInt(strings[1]);
            num += 1;

            String newOrderId=String.format("-%04d",num);
            return "C"+newOrderId;
        }

        return "C-0001";
    }
}

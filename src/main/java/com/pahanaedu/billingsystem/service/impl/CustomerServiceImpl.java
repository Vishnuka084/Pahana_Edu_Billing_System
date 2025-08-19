package com.pahanaedu.billingsystem.service.impl;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.config.DaoFactory;
import com.pahanaedu.billingsystem.dao.CustomerDAO;
import com.pahanaedu.billingsystem.dto.CustomerDTO;
import com.pahanaedu.billingsystem.entity.Customer;
import com.pahanaedu.billingsystem.service.CustomerService;
import com.pahanaedu.billingsystem.type.DaoTypes;
import com.pahanaedu.billingsystem.util.Mapper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:21
 */
public class CustomerServiceImpl implements CustomerService {

    private final BasicDataSource bds;

    private final CustomerDAO customerDAO = (CustomerDAO) DaoFactory.getDaoFactory().getDAO(DaoTypes.CUSTOMER);

    public CustomerServiceImpl(BasicDataSource bds){
        this.bds=bds;
    }

    @Override
    public boolean addNewCustomer(CustomerDTO customerDTO) throws SQLException {

        try (Connection connection = this.bds.getConnection();){
            return customerDAO.add(Mapper.toCustomer(customerDTO),connection);
        }

    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) throws SQLException {

        try (Connection connection = this.bds.getConnection();){
            Customer updatedCustomer =
                    customerDAO.update(Mapper.toCustomer(customerDTO), connection);
            if (updatedCustomer!=null){
                return Mapper.toCustomerDTO(updatedCustomer);
            }else {
                return null;
            }
        }

    }

    @Override
    public void deleteCustomer(String id) throws SQLException,NotFoundException {

        try (Connection connection = this.bds.getConnection()){
            // find customer first
            Customer customer = customerDAO.getByPk(id, connection);
            if (customer == null){
                throw new NotFoundException("Customer new found!");
            }

            // delete customer
            customerDAO.delete(id, connection);
        }

    }

    @Override
    public List<CustomerDTO> getAll() throws SQLException {
        try (Connection connection = this.bds.getConnection()){
            // delete customer
            return customerDAO.getAll(connection).stream().map(Mapper::toCustomerDTO).collect(Collectors.toList());
        }
    }

    @Override
    public CustomerDTO getCustomerByAccNum(String accNum) throws SQLException,NotFoundException {
        try (Connection connection = this.bds.getConnection()){
            Customer customer = customerDAO.getByPk(accNum, connection);

            if (customer == null){
                throw new NotFoundException("Customer not found!");
            }
            return new CustomerDTO(
                    customer.getAccountNumber(),
                    customer.getFullName(),
                    customer.getAddress(),
                    customer.getPhoneNumber(),
                    customer.getUniteConsumed());
        }
    }
}

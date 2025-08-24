package com.pahanaedu.billingsystem.service;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:18
 */

public interface CustomerService extends SuperService {
    boolean addNewCustomer(CustomerDTO customerDTO) throws SQLException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO) throws SQLException;

    void deleteCustomer(String id) throws SQLException,NotFoundException;

    List<CustomerDTO> getAll() throws SQLException;

    CustomerDTO getCustomerByAccNum(String accNum) throws SQLException, NotFoundException;
}

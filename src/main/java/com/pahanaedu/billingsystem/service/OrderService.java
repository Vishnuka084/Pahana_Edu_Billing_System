package com.pahanaedu.billingsystem.service;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dto.OrderDTO;

import java.sql.SQLException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:19
 */

public interface OrderService extends SuperService {

    void addNewOrder(OrderDTO orderDTO) throws SQLException, NotFoundException, ConstrainViolationException;

}

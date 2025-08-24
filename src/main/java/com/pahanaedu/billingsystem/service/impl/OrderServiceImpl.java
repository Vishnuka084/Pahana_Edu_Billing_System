package com.pahanaedu.billingsystem.service.impl;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.config.DaoFactory;
import com.pahanaedu.billingsystem.dao.CustomerDAO;
import com.pahanaedu.billingsystem.dao.ItemDAO;
import com.pahanaedu.billingsystem.dao.OrderDAO;
import com.pahanaedu.billingsystem.dao.OrderDetailsDAO;
import com.pahanaedu.billingsystem.dto.OrderDTO;
import com.pahanaedu.billingsystem.dto.OrderDetailsDTO;
import com.pahanaedu.billingsystem.entity.Customer;
import com.pahanaedu.billingsystem.entity.Item;
import com.pahanaedu.billingsystem.service.OrderService;
import com.pahanaedu.billingsystem.type.DaoTypes;
import com.pahanaedu.billingsystem.util.Convertor;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:21
 */
public class OrderServiceImpl implements OrderService {

    private final BasicDataSource bds;
    private final ItemDAO itemDAO = (ItemDAO) DaoFactory.getDaoFactory().getDAO(DaoTypes.ITEM);
    private final OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DaoFactory.getDaoFactory().getDAO(DaoTypes.ORDER_DETAIL);
    private final OrderDAO orderDAO = (OrderDAO) DaoFactory.getDaoFactory().getDAO(DaoTypes.ORDER);
    private final CustomerDAO customerDAO = (CustomerDAO) DaoFactory.getDaoFactory().getDAO(DaoTypes.CUSTOMER);

    public OrderServiceImpl(BasicDataSource bds){
        this.bds=bds;
    }

    @Override
    public void addNewOrder(OrderDTO orderDTO) throws SQLException,NotFoundException,ConstrainViolationException  {

        try (Connection connection = bds.getConnection();){

            //set auto commit to false
            connection.setAutoCommit(false);

            try {

                //validate customer
                Customer customer = customerDAO.getByPk(orderDTO.getCustomerId(), connection);

                if (customer == null){
                    throw new NotFoundException("Customer ("+orderDTO.getCustomerId()+") not found!");
                }

                // add order
                if (!orderDAO.add(Convertor.toOrders(orderDTO),connection)){
                    throw new ConstrainViolationException("Failed to save order!");
                }

                if (!orderDTO.getOrderDetails().isEmpty()){

                    // update items available qty
                    for (OrderDetailsDTO detailsDTO : orderDTO.getOrderDetails()){

                        //get item details
                        Item item= itemDAO.getByPk(detailsDTO.getItemCode(), connection);

                        System.out.println("ooooo");
                        System.out.println(item);

                        //validate item
                        if (item == null){
                            throw new NotFoundException("Item ("+detailsDTO.getItemCode()+") not found!");
                        }

                        if (!itemDAO.reduceItemQty(detailsDTO.getItemCode(), detailsDTO.getQty(), connection)){
                            throw new ConstrainViolationException("Failed to reduce item qty!");
                        }
                    }

                    // add order details
                    for (OrderDetailsDTO detailsDTO : orderDTO.getOrderDetails()){

                        if (!orderDetailsDAO.add(Convertor.toOrderDetail(detailsDTO),connection)){
                            throw new ConstrainViolationException("Failed to save order details!");
                        }
                    }

                    // if not any issue commit the transaction
                    connection.commit();

                }else {
                    throw new ConstrainViolationException("Order details is empty!");
                }

            }catch (ConstrainViolationException | SQLException | NotFoundException e){
                connection.rollback();
                throw e;
//                throw new SQLException(e.getLocalizedMessage());
            }finally {
                connection.setAutoCommit(true);
            }

        }

    }
}

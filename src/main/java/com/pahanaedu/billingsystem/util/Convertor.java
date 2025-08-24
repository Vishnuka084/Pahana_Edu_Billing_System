package com.pahanaedu.billingsystem.util;

import com.pahanaedu.billingsystem.dto.*;
import com.pahanaedu.billingsystem.entity.*;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:16:28
 */
public class Convertor {

    public static Customer toCustomer (CustomerDTO dto){
        return new Customer(
                dto.getAccountNumber(),
                dto.getFullName(),
                dto.getAddress(),
                dto.getPhoneNumber(),
                dto.getUniteConsumed()
        );
    }

    public static CustomerDTO toCustomerDTO(Customer customer){
        return new CustomerDTO(
                customer.getAccountNumber(),
                customer.getFullName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getUniteConsumed()
        );
    }

    public static Item toItem(ItemDTO dto){
        return new Item(
                dto.getItemCode(),
                dto.getDescription(),
                dto.getUnitPrice(),
                dto.getQtyOnHand()
        );
    }

    public static ItemDTO toItemDTO(Item item){
        return new ItemDTO(
                item.getItemCode(),
                item.getDescription(),
                item.getUnitPrice(),
                item.getQtyOnHand()
        );
    }

    public static OrderDetails toOrderDetail(OrderDetailsDTO dto){
        return new OrderDetails(
                dto.getOrderID(),
                dto.getItemCode(),
                dto.getUnitPrice(),
                dto.getQty()
        );
    }

    public static Orders toOrders(OrderDTO dto){
        return new Orders(
                dto.getOrderId(),
                dto.getCustomerId(),
                dto.getOrderDate(),
                dto.getTotal()
        );
    }

    public static User toUser(UserDTO dto){
        return new User(
                dto.getUserId(),
                dto.getUsername(),
                dto.getPassword()
        );
    }

    public static UserDTO toUserDTO(User user){
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getPassword()
        );
    }
}

package com.pahanaedu.billingsystem.util;

import com.pahanaedu.billingsystem.dto.CustomerDTO;
import com.pahanaedu.billingsystem.dto.ItemDTO;
import com.pahanaedu.billingsystem.entity.Customer;
import com.pahanaedu.billingsystem.entity.Item;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:15
 */
public class Mapper {
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
}

package com.pahanaedu.billingsystem.service;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dto.ItemDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:19
 */

public interface ItemService extends SuperService {
    boolean addNewItem(ItemDTO itemDTO) throws SQLException;

    ItemDTO updateItem(ItemDTO itemDTO) throws SQLException;

    void deleteItem(String code) throws SQLException;

    List<ItemDTO> getAll() throws SQLException;

    ItemDTO getItemByCode(String code) throws SQLException, NotFoundException;
}

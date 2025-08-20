package com.pahanaedu.billingsystem.service.impl;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.config.DaoFactory;
import com.pahanaedu.billingsystem.dao.ItemDAO;
import com.pahanaedu.billingsystem.dto.ItemDTO;
import com.pahanaedu.billingsystem.entity.Item;
import com.pahanaedu.billingsystem.service.ItemService;
import com.pahanaedu.billingsystem.type.DaoTypes;
import com.pahanaedu.billingsystem.util.Convertor;
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
public class ItemServiceImpl implements ItemService {

    private final BasicDataSource bds;

    private final ItemDAO itemDAO = (ItemDAO) DaoFactory.getDaoFactory().getDAO(DaoTypes.ITEM);

    public ItemServiceImpl(BasicDataSource bds){
        this.bds=bds;
    }

    @Override
    public boolean addNewItem(ItemDTO itemDTO) throws SQLException {
        try (Connection connection = this.bds.getConnection();){
            //get new id
            String nextItemCode = itemDAO.getNextItemCode(connection);

            System.out.println("Next item code :"+nextItemCode);

            itemDTO.setItemCode(nextItemCode);
            return itemDAO.add(Convertor.toItem(itemDTO),connection);
        }
    }

    @Override
    public ItemDTO updateItem(ItemDTO itemDTO) throws SQLException {
        try (Connection connection = this.bds.getConnection();){
            Item updatedItem =
                    itemDAO.update(Convertor.toItem(itemDTO), connection);
            if (updatedItem!=null){
                return Convertor.toItemDTO(updatedItem);
            }else {
                return null;
            }
        }
    }

    @Override
    public void deleteItem(String code) throws SQLException {
        try (Connection connection = this.bds.getConnection()){
            // find customer first

            // delete customer
            itemDAO.delete(code, connection);
        }
    }

    @Override
    public List<ItemDTO> getAll() throws SQLException {
        try (Connection connection = this.bds.getConnection()){
            // delete customer
            return itemDAO.getAll(connection).stream().map(Convertor::toItemDTO).collect(Collectors.toList());
        }
    }

    @Override
    public ItemDTO getItemByCode(String code) throws SQLException, NotFoundException {
        try (Connection connection = this.bds.getConnection()){
            Item item = itemDAO.getByPk(code, connection);

            if (item == null){
                throw new NotFoundException("Item not found!");
            }
//            return new ItemDTO(
//                    item.getItemCode(),
//                    item.getDescription(),
//                    item.getUnitPrice(),
//                    item.getQtyOnHand());
            return Convertor.toItemDTO(item);
        }
    }
}

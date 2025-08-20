package com.pahanaedu.billingsystem.service.impl;

import com.pahanaedu.billingsystem.Exception.ConstrainViolationException;
import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.config.DaoFactory;
import com.pahanaedu.billingsystem.dao.UserDAO;
import com.pahanaedu.billingsystem.dto.SignInDetailsDTO;
import com.pahanaedu.billingsystem.dto.UserDTO;
import com.pahanaedu.billingsystem.entity.User;
import com.pahanaedu.billingsystem.service.UserService;
import com.pahanaedu.billingsystem.type.DaoTypes;
import com.pahanaedu.billingsystem.util.Convertor;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-19
 * Time:16:24
 */
public class UserServiceImpl implements UserService {

    private final BasicDataSource bds;

    private final UserDAO userDAO = (UserDAO) DaoFactory.getDaoFactory().getDAO(DaoTypes.USER);

    public UserServiceImpl(BasicDataSource bds){
        this.bds=bds;
    }

    @Override
    public boolean verifyPassword(SignInDetailsDTO signInDetailsDTO) throws SQLException,ConstrainViolationException,NotFoundException{

        boolean passwordISMatched = false;

        if (!signInDetailsDTO.getUsername().isEmpty()){
            //get userDetails from table
            UserDTO user = this.getUser(signInDetailsDTO.getUsername());

            //verify password
            if (signInDetailsDTO.getPassword().equals(user.getPassword())){
                passwordISMatched = true;
            }

        }else {
            throw new ConstrainViolationException("Password is empty");
        }

        return passwordISMatched;
    }

    @Override
    public UserDTO getUser(String username) throws SQLException,NotFoundException {

        try (Connection connection=bds.getConnection()){
            User userByUsername = userDAO.getByPk(username, connection);

            if (userByUsername == null){
                throw new NotFoundException("User not found!");
            }

            return Convertor.toUserDTO(userByUsername);
        }
    }
}

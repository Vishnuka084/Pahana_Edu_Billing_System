package com.pahanaedu.billingsystem.service;

import com.pahanaedu.billingsystem.Exception.NotFoundException;
import com.pahanaedu.billingsystem.dto.UserDTO;

import java.sql.SQLException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:16:23
 */

public interface UserService extends SuperService {

    boolean verifyPassword(UserDTO userDTO) throws SQLException;

    UserDTO getUser(String username) throws SQLException, NotFoundException;

}

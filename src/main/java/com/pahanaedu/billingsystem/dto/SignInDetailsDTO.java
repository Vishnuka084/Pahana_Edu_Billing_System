package com.pahanaedu.billingsystem.dto;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-17
 * Time:13:11
 */
public class SignInDetailsDTO {
    private String username;
    private String password;

    public SignInDetailsDTO() {
    }

    public SignInDetailsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDetailsDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}



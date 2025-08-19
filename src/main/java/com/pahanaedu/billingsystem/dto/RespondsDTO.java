package com.pahanaedu.billingsystem.dto;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:12:58
 */
public class RespondsDTO {
    private int status;
    private String massage;
    private Object data;

    public RespondsDTO() {
    }

    public RespondsDTO(int status, String massage, Object data) {
        this.status = status;
        this.massage = massage;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

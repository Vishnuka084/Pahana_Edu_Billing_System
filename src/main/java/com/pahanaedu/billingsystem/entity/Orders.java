package com.pahanaedu.billingsystem.entity;

import java.util.Date;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-19
 * Time:16:01
 */
public class Orders implements SuperEntity {

    private String orderId;
    private String customerId;
    private Date orderDate;
    private Double total;

    public Orders() {
    }

    public Orders(String orderId, String customerId, Date orderDate, Double total) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orderDate=" + orderDate +
                ", total=" + total +
                '}';
    }
}
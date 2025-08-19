package com.pahanaedu.billingsystem.dto;

import java.util.Date;
import java.util.List;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:12:58
 */
public class OrderDTO {
    private String orderId;
    private String customerId;
    private Date orderDate;
    private Double total;
    private List<OrderDetailsDTO> orderDetails;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String customerId, Date orderDate, Double total, List<OrderDetailsDTO> orderDetails) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.total = total;
        this.orderDetails = orderDetails;
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

    public List<OrderDetailsDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailsDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orderDate=" + orderDate +
                ", total=" + total +
                ", orderDetails=" + orderDetails +
                '}';
    }
}

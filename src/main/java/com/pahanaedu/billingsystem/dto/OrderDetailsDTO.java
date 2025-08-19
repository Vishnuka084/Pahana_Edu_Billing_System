package com.pahanaedu.billingsystem.dto;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:12:58
 */
public class OrderDetailsDTO {
    private String orderID;
    private String itemCode;
    private Double unitPrice;
    private int qty;
    private Double total;

    public OrderDetailsDTO() {
    }

    public OrderDetailsDTO(String orderID, String itemCode, Double unitPrice, int qty, Double total) {
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.total = total;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDetailsDTO{" +
                "orderID='" + orderID + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", total=" + total +
                '}';
    }
}

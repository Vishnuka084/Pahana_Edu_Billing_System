package com.pahanaedu.billingsystem.entity;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:12:57
 */
public class Customer implements SuperEntity{
    private String accountNumber;
    private String fullName;
    private String address;
    private String phoneNumber;
    private int uniteConsumed;

    public Customer() {
    }

    public Customer(String fullName, String address, String phoneNumber, int uniteConsumed) {
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.uniteConsumed = uniteConsumed;
    }

    public Customer(String accountNumber, String fullName, String address, String phoneNumber, int uniteConsumed) {
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.uniteConsumed = uniteConsumed;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getUniteConsumed() {
        return uniteConsumed;
    }

    public void setUniteConsumed(int uniteConsumed) {
        this.uniteConsumed = uniteConsumed;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "accountNumber='" + accountNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", uniteConsumed=" + uniteConsumed +
                '}';
    }
}

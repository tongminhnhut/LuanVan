package com.tongminhnhut.luanvan.Model;

import java.util.List;

public class RequestOrder {
    private String phone ;
    private String name;
    private String address;
    private String total;
    private List<Order> orderList;

    public RequestOrder() {
    }

    public RequestOrder(String phone, String name, String address, String total, List<Order> orderList) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.orderList = orderList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

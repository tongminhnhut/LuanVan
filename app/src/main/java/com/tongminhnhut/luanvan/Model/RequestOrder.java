package com.tongminhnhut.luanvan.Model;

import java.util.List;

public class RequestOrder {
    private String Phone ;
    private String Name;
    private String Address;
    private String Total;
    private String Status;
    private String Comment;
    private String Latlng ;
    private List<Order> OrderList;

    public RequestOrder() {
    }

//    public RequestOrder(String phone, String name, String address, String total, String comment, List<Order> orderList) {
//        Phone = phone;
//        Name = name;
//        Address = address;
//        Total = total;
//        Status = "0";
//        Comment = comment;
//        OrderList = orderList;
//    }

    public RequestOrder(String phone, String name, String address, String total, String comment, String latlng, List<Order> orderList) {
        Phone = phone;
        Name = name;
        Address = address;
        Total = total;
        Status = "0";
        Comment = comment;
        Latlng = latlng;
        OrderList = orderList;
    }

    public String getLatlng() {
        return Latlng;
    }

    public void setLatlng(String latlng) {
        Latlng = latlng;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<Order> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<Order> orderList) {
        OrderList = orderList;
    }
}

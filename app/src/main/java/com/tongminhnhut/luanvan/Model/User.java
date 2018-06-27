package com.tongminhnhut.luanvan.Model;

public class User {
    private String Name ;
    private String Password ;
    private String Phone ;
    private String IsStaff ;
    private String HomeAddress;

    public User() {
    }


    public User(String name, String password) {
        Name = name;
        Password = password;
        IsStaff = "false";
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}

package com.tongminhnhut.luanvan.Model;

public class DongHo {
    private String Name;
    private String Image ;
    private String Gia;
    private String Discount;
    private String XuatXu;
    private String BaoHanh;
    private String Size;
    private String ThuongHieu;
    private String May;
    private String DayDeo;
    private String MenuId;
    private String BrandId;
    private String Phone ;


    public DongHo() {
    }

    public DongHo(String name, String image, String gia, String discount, String xuatXu, String baoHanh, String size, String thuongHieu, String may, String dayDeo, String menuId, String brandId) {
        Name = name;
        Image = image;
        Gia = gia;
        Discount = discount;
        XuatXu = xuatXu;
        BaoHanh = baoHanh;
        Size = size;
        ThuongHieu = thuongHieu;
        May = may;
        DayDeo = dayDeo;
        MenuId = menuId;
        BrandId = brandId;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getXuatXu() {
        return XuatXu;
    }

    public void setXuatXu(String xuatXu) {
        XuatXu = xuatXu;
    }

    public String getBaoHanh() {
        return BaoHanh;
    }

    public void setBaoHanh(String baoHanh) {
        BaoHanh = baoHanh;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getThuongHieu() {
        return ThuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        ThuongHieu = thuongHieu;
    }

    public String getMay() {
        return May;
    }

    public void setMay(String may) {
        May = may;
    }

    public String getDayDeo() {
        return DayDeo;
    }

    public void setDayDeo(String dayDeo) {
        DayDeo = dayDeo;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getBrandId() {
        return BrandId;
    }

    public void setBrandId(String brandId) {
        BrandId = brandId;
    }
}

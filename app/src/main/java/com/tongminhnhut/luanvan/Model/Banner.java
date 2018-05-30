package com.tongminhnhut.luanvan.Model;

public class Banner {
    private String Id;
    private String Image ;
    private String Name ;

    public Banner() {
    }

    public Banner(String id, String image, String name) {
        Id = id;
        Image = image;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}


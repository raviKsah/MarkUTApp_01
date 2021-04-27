package com.example.markutapp_01;

public class Messages {
    String description;
    String title;
    String image_path;
    String price;
    //constructors


    public Messages() {
    }

    public Messages(String imageDesc, String imageTitle, String imageUrl, String price) {
        this.description = imageDesc;
        this.title = imageTitle;
        this.image_path = imageUrl;
        this.price = price;
    }

    public String getImageDesc() {
        return description;
    }

    public void setImageDesc(String imageDesc) {
        this.description = imageDesc;
    }

    public String getImageTitle() {
        return title;
    }

    public void setImageTitle(String imageTitle) {
        this.title = imageTitle;
    }

    public String getImageUrl() {
        return image_path;
    }

    public void setImageUrl(String imageUrl) {
        this.image_path = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

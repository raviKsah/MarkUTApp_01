package com.example.markutapp_01;

public class Messages {
    String adID;
    String description;
    String title;
    String image_path;
    String price;
    String date;
    //constructors


    public Messages() {
    }

    public Messages(String adID, String imageDesc, String imageTitle, String imageUrl, String price, String date) {
        this.adID = adID;
        this.description = imageDesc;
        this.title = imageTitle;
        this.image_path = imageUrl;
        this.price = price;
        this.date = date;
    }

    public String getAdID()
    {
        return adID;
    }

    public void setAdID(String adID)
    {
        this.adID = adID;
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

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
}

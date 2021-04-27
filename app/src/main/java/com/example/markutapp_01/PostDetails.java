package com.example.markutapp_01;

public class PostDetails {
String ad_id,advertiser,category,date_completed,date_created,description,edited_date,editor,image_path,price,title;
Boolean is_complete,under_report,was_edited;

    public PostDetails(String ad_id, String advertiser, String category, String date_completed, String date_created, String description, String edited_date, String editor, String image_path, String price, String title, Boolean is_complete, Boolean under_report, Boolean was_edited) {
        this.ad_id = ad_id;
        this.advertiser = advertiser;
        this.category = category;
        this.date_completed = date_completed;
        this.date_created = date_created;
        this.description = description;
        this.edited_date = edited_date;
        this.editor = editor;
        this.image_path = image_path;
        this.price = price;
        this.title = title;
        this.is_complete = is_complete;
        this.under_report = under_report;
        this.was_edited = was_edited;
    }

    public String getAd_id() {
        return ad_id;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public String getCategory() {
        return category;
    }

    public String getDate_completed() {
        return date_completed;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getDescription() {
        return description;
    }

    public String getEdited_date() {
        return edited_date;
    }

    public String getEditor() {
        return editor;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getIs_complete() {
        return is_complete;
    }

    public Boolean getUnder_report() {
        return under_report;
    }

    public Boolean getWas_edited() {
        return was_edited;
    }
}

package com.yasith.fotnewsapp;

public class NewsItem {

    private String title;
    private String description;
    private String date;
    private String time;
    private String imageUrl;
    private String category;

    public NewsItem() {
    }

    public NewsItem(String title, String description, String date, String time, String imageUrl, String category) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}

package com.quantumtech.vido.model;

public class Movie {


    private boolean isBoxOfficeItem;
    private String thumbnailUrl;
    private String title;
    private int rank;
    private int id;
    private float rate;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isBoxOfficeItem() {
        return isBoxOfficeItem;
    }

    public void setBoxOfficeItem(boolean boxOfficeItem) {
        isBoxOfficeItem = boxOfficeItem;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

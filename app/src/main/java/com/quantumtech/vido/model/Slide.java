package com.quantumtech.vido.model;

public class Slide {

    private int mImage;
    private String mTitle;
    private String mDescription;
    float rate;

    public Slide(int mImage, String mTitle, String mDescription, float rate) {
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.rate = rate;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int Image) {
        this.mImage = Image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        this.mTitle = Title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String Description) {
        this.mDescription = Description;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}

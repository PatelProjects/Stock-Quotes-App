package com.example.financealertapp;

public class example_item {
    private int mImageResource;
    private String mText1;
    private String mText2;
    private String changeNumber;
    private String changePercentage;

    public example_item(int mImageResource, String mText1, String mText2, String changeNumber, String changePercentage){
        this.mImageResource = mImageResource;
        this.mText1 = mText1;
        this.mText2 = mText2;
        this.changeNumber = changeNumber;
        this.changePercentage = changePercentage;
    }

    public int getmImageResource(){
        return this.mImageResource;
    }

    public String getmText1(){
        return this.mText1;
    }

    public String getmText2(){
        return this.mText2;
    }

    public String getchangeNumber(){
        return this.changeNumber;
    }

    public String getChangePercentage(){
        return this.changePercentage;
    }
}


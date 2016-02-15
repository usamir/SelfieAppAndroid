package com.samir.selfieapp;

import android.graphics.Bitmap;

/**
 * Created by usamir on 14.2.2016.
 */
public class SelfieRecord {
    private Bitmap mPictureBitmap;
    private String mDate;

    static final String TAG = "SelfieRecord";

    public SelfieRecord(Bitmap pictureBitmap, String date) {
        this.mPictureBitmap = pictureBitmap;
        this.mDate = date;
    }

    public SelfieRecord(Bitmap pictureBitmap) {
        this.mPictureBitmap = pictureBitmap;
    }

    public Bitmap getPicture() {

        return mPictureBitmap;
    }

    public void setPicture(Bitmap pictureBitmap) {
        this.mPictureBitmap = pictureBitmap;
    }


    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }
}

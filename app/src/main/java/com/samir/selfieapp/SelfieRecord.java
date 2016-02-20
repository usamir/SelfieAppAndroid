package com.samir.selfieapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by usamir on 14.2.2016.
 */
public class SelfieRecord {
    private Bitmap mPictureBitmap;
    private String mDate;
    private String mURI;

    static final String TAG = "SelfieRecord";

    /** Constructor of Selfie record used when loading pictures from SD Card */
    public SelfieRecord(String url, String date) {
        this.mDate = date;
        this.mURI = url;

        BitmapFactory.Options options;
        try {
            // Try to decode file to bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(url);
            this.mPictureBitmap = bitmap;
            // if picture is to big than resize it
        } catch (OutOfMemoryError er) {
            try {
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(url, options);
                this.mPictureBitmap = bitmap;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void setmURI (String uri) {
        this.mURI = uri;
    }

    public String getmURI () {
        return mURI;
    }

    public Bitmap getPicture() {
        return mPictureBitmap;
    }

    public void setPicture(Bitmap image) {
        this.mPictureBitmap = image;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

}

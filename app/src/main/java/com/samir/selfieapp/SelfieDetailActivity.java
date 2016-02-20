package com.samir.selfieapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SelfieDetailActivity extends AppCompatActivity {

    static final String TAG = "SelfieDetailActivity";

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_detail);
        Log.i(TAG, "Created activity");
        Uri imageUri = getIntent().getData();
        Log.i(TAG, imageUri.toString());
        if (imageUri != null) {
            ImageView mImageView = (ImageView) findViewById(R.id.detailedImage);
            try {
                // Try to decode file to bitmap
                mBitmap = BitmapFactory.decodeFile(imageUri.toString());
                // if picture is to big than resize it
            } catch (OutOfMemoryError er) {
                try {
                    BitmapFactory.Options options;
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    mBitmap = BitmapFactory.decodeFile(imageUri.toString(), options);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            mImageView.setImageBitmap(mBitmap);
            Log.i(TAG, "Set Image");
        }
    }
}
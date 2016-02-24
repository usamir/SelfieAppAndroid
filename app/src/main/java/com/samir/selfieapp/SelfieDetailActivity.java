package com.samir.selfieapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SelfieDetailActivity extends AppCompatActivity {

    static final String TAG = "SelfieDetailActivity";

    private Bitmap mBitmap;
    private ImageView mImageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_detail);
        Log.i(TAG, "Created activity");
        imageUri = getIntent().getData();
        Log.i(TAG, imageUri.toString());
        if (imageUri != null) {
            mImageView = (ImageView) findViewById(R.id.detailedImage);
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

    public void onRotate(View view) {
        Log.i(TAG, "on rotate");
    }

    public void onShare(View view) {
        Log.i(TAG, "On Share!");
    }
}

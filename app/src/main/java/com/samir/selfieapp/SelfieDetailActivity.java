package com.samir.selfieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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


        // Support tollbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.share:
                sharePic();
                break;
            case R.id.rotate:
                rotate();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    //TODO: share picture on social pages
    private void sharePic () {

        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.toString());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                bitmap, "Title", null);
        Uri imageUri =  Uri.parse(path);
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(share, "Select"));

    }

    //TODO: rotate picture
    private void rotate () {
        try {
            byte[] pictureBytes;
            File pictureFile = new File(imageUri.toString());
            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.toString());

            Matrix m = new Matrix();
            m.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

            ByteArrayOutputStream bos = new ByteArrayOutputStream ();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            pictureBytes = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(pictureFile);
            Log.i (TAG, pictureFile.getAbsolutePath ());
            fos.write (pictureBytes);
            fos.close();

            mImageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            Log.i(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "Error accessing file: " + e.getMessage());
        }

    }

}

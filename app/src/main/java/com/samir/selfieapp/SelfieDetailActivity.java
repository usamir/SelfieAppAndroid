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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SelfieDetailActivity extends AppCompatActivity {

    static final String TAG = "SelfieDetailActivity";

    private Bitmap mBitmap;
    private ImageView mImageView;
    private Uri mImageUri;
    private File mTempFile;

    // are we currently rotating image
    boolean mInRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_detail);
        Log.i (TAG, "Created activity");

        // location of image
        mImageUri = getIntent().getData();
        Log.i(TAG, mImageUri.toString());

        if (mImageUri != null) {
            mImageView = (ImageView) findViewById(R.id.detailedImage);
            mBitmap = decodingFile(mImageUri.toString());

            mImageView.setImageBitmap(mBitmap);
            Log.i(TAG, "Set Image");
        }

        // Support tollbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // during creation we are not rotating image
        mInRotation = false;
    }

   /* @Override
    protected void onResume() {
        super.onResume();

        // location of image
        mImageUri = getIntent().getData();
        Log.i(TAG, mImageUri.toString());

        if (mImageUri != null) {
            mImageView = (ImageView) findViewById(R.id.detailedImage);
            mBitmap = decodingFile(mImageUri.toString());

            mImageView.setImageBitmap(mBitmap);
            Log.i(TAG, "Set Image");
        }

        // during creation we are not rotating image
        mInRotation = false;
    }*/

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

    // share picture on social pages
    private void sharePic () {

        mBitmap = decodingFile(mImageUri.toString());

        // action for sharing picture
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, "Title", null);
        Uri imageUri =  Uri.parse(path);
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(share, "Select"));

    }

    //TODO: rotate picture
    private void rotate () {
        try {
            byte[] pictureBytes;
            String imgPath = mImageUri.toString ();
            String tempImgPath = mImageUri.toString () + ".tmp";

            mTempFile = new File(tempImgPath);
            if (!mInRotation) {
                mBitmap = decodingFile(imgPath);
                mInRotation = true;
            } else {
                mBitmap = decodingFile(tempImgPath);
            }

            Matrix m = new Matrix();
            m.postRotate(90);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true);

            ByteArrayOutputStream bos = new ByteArrayOutputStream ();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            pictureBytes = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(mTempFile);
            Log.i (TAG, mTempFile.getAbsolutePath ());
            fos.write (pictureBytes);
            fos.close();

            mImageView.setImageBitmap(mBitmap);

        } catch (FileNotFoundException e) {
            Log.i(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "Error accessing file: " + e.getMessage());
        }

    }

    private Bitmap decodingFile (String imgPath) {
        try {
            // Try to decode file to bitmap
            return BitmapFactory.decodeFile(imgPath);
            // if picture is to big than resize it
        } catch (OutOfMemoryError er) {
            try {
                BitmapFactory.Options options;
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                return BitmapFactory.decodeFile(imgPath, options);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // if it comes here then return null
        return  null;
    }
/*
    @Override
    protected void onPause () {
        super.onPause();

        // check for null pointer, we did not made changes to image
        if (mTempFile != null) {
            if (mTempFile.isFile ()) {
                Log.i (TAG, "Destroying file " + mTempFile.toString ());
                mTempFile.delete ();
            }
        }

        // check for null pointer for bitmap, we did not load bitmap
        if (mBitmap != null) {
            Log.i(TAG, "Recycle bitmap");
            mBitmap.recycle();
            mBitmap = null;
        }

    }*/

    @Override
    protected void onStop() {
        super.onStop();

        // check for null pointer, we did not made changes to image
        if (mTempFile != null) {
            if (mTempFile.isFile ()) {
                Log.i (TAG, "Destroying file " + mTempFile.toString ());
                mTempFile.delete ();
            }
        }

        // check for null pointer for bitmap, we did not load bitmap
        if (mBitmap != null) {
            Log.i(TAG, "Recycle bitmap");
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}

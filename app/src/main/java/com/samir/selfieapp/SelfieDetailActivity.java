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

    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_selfie_detail);
        Log.i (TAG, "Created activity");

        // location of image
        mImageUri = getIntent().getData();
        Log.i (TAG, mImageUri.toString ());

        if (mImageUri != null) {
            mImageView = (ImageView) findViewById(R.id.detailedImage);
            try {
                // Try to decode file to bitmap
                mBitmap = BitmapFactory.decodeFile(mImageUri.toString());
                // if picture is to big than resize it
            } catch (OutOfMemoryError er) {
                try {
                    BitmapFactory.Options options;
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    mBitmap = BitmapFactory.decodeFile(mImageUri.toString(), options);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            mImageView.setImageBitmap(mBitmap);
            Log.i(TAG, "Set Image");

            // instance of gesture detector
            mGestureDetector = new GestureDetector(this, new GestureListener());

            // animation for scalling
            mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener()
            {
                @Override
                public boolean onScale(ScaleGestureDetector detector)
                {
                    float scale = 1 - detector.getScaleFactor();

                    float prevScale = mScale;
                    mScale += scale;

                    if (mScale < 0.1f) // Minimum scale condition:
                        mScale = 0.1f;

                    if (mScale > 10f) // Maximum scale condition:
                        mScale = 10f;
                    ScaleAnimation scaleAnimation = new ScaleAnimation (1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                    scaleAnimation.setDuration(0);
                    scaleAnimation.setFillAfter(true);
                    ScrollView layout =(ScrollView) findViewById(R.id.scrollView);
                    layout.startAnimation(scaleAnimation);
                    return true;
                }
            });
        }


        // Support tollbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        super.dispatchTouchEvent (ev);
        mScaleDetector.onTouchEvent(ev);
        mGestureDetector.onTouchEvent(ev);
        return mGestureDetector.onTouchEvent (ev);
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

        Bitmap bitmap = BitmapFactory.decodeFile (mImageUri.toString ());
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
            String imgPath = mImageUri.toString ();
            mTempFile = new File("tmp_" + imgPath);
            Bitmap bitmap = BitmapFactory.decodeFile (imgPath);

            Matrix m = new Matrix();
            m.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

            ByteArrayOutputStream bos = new ByteArrayOutputStream ();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            pictureBytes = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(mTempFile);
            Log.i (TAG, mTempFile.getAbsolutePath ());
            fos.write (pictureBytes);
            fos.close();

            mImageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            Log.i(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "Error accessing file: " + e.getMessage());
        }

    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();

        Log.i (TAG, "Destroying file " + mTempFile.toString ());
        if (mTempFile.isFile ()) {
            mTempFile.delete ();
        }
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // double tap fired.
            Log.i (TAG, "Double tap!");
            return true;
        }
    }
}

package com.samir.selfieapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraActivity extends Activity {
    private static final int PREVIEW_PAUSE = 2000;
    private static final String TAG = "CameraActivity";
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private RelativeLayout mFrame;
    private boolean mIsPreviewing;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_camera);

        Log.e (TAG, "Camera activity ");

        mFrame = (RelativeLayout) findViewById(R.id.frame);

        // Disable touches on mFrame
        mFrame.setEnabled(false);

        // Receive intent
        Intent intent = getIntent();

        // Setup touch listener for taking pictures
        mFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Only respond to ACTION_UP
                if (event.getActionMasked() == (MotionEvent.ACTION_UP)) {

                    // Take picture
                    // Pass in shutterCallback and PictureCallback Objects
                    mCamera.takePicture(mShutterCallback, null,
                            mPictureCallback);

                }

                return true;
            }
        });

        // Setup SurfaceView for previewing camera image
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraView);

        // Get SurfaceHolder for accessing the SurfaceView's Surface
        mSurfaceHolder = surfaceView.getHolder();

        // Set callback Object for the SurfaceHolder
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null == mCamera) {
            try {

                // Returns first front-facing camera or null if no camera is
                // available.
                // May take a long time to complete
                // Consider moving this to an AsyncTask
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

            } catch (RuntimeException e) {
                Log.e(TAG, "Failed to acquire camera");
            }

            // Ensure presence of camera or finish()
            if (null == mCamera)
                finish();
        }
    }

    @Override
    protected void onPause() {

        // Disable touches on mFrame
        mFrame.setEnabled(false);

        // Shutdown preview
        stopPreview();

        // Release camera resources
        releaseCameraResources();

        super.onPause ();

    }

    // Start the preview
    private void startPreview() {
        if (null != mCamera) {
            try {
                mCamera.startPreview();
                mIsPreviewing = true;
            } catch (Exception e) {
                Log.e(TAG, "Failed to start preview");
            }
        }
    }

    // Shutdown preview
    private void stopPreview() {
        if (null != mCamera && mIsPreviewing) {
            try {
                mCamera.stopPreview();
                mIsPreviewing = false;
            } catch (Exception e) {
                Log.e(TAG, "Failed to stop preview");
            }
        }
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile (getOutputMediaFile (type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory (
                Environment.DIRECTORY_PICTURES), "SelfieApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format(new Date ());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            Log.d(TAG, "creating folder" + mediaFile.toString ());
        } else {
            return null;
        }

        return mediaFile;
    }

    // Plays system shutter Sound
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback () {
        @Override
        public void onShutter () {
            // do nothing for now
        }
    };

    // Freeze the Preview for a few seconds and then restart the preview, also save picture to storage
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback () {
        @Override
        public void onPictureTaken (byte[] data, Camera camera) {
            // preview stopped by system
            mIsPreviewing = false;

            // give user some time to preview picture
            try {
                Thread.sleep (PREVIEW_PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

            // Save image here
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions ");
                return;
            }

            try {
                byte[] pictureBytes;
                Bitmap thePicture = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix m = new Matrix();
                m.postRotate(270);
                thePicture = Bitmap.createBitmap(thePicture, 0, 0, thePicture.getWidth(), thePicture.getHeight(), m, true);

                ByteArrayOutputStream bos = new ByteArrayOutputStream ();
                thePicture.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                pictureBytes = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(pictureFile);
                Log.i (TAG, pictureFile.getAbsolutePath ());
                fos.write (pictureBytes);
                fos.close();

            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d (TAG, "Error accessing file: " + e.getMessage ());
            }

            // Restart preview
            startPreview ();
        }
    };



    // Release camera so other applications can use it.
    private void releaseCameraResources() {
        if (null != mCamera) {
            mCamera.release();
            mCamera = null;
        }
    }

    // SurfaceHolder callback Object
    SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // Do nothing
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

            if (mSurfaceHolder.getSurface() == null) {
                return;
            }

            // Disable touches on mFrame
            mFrame.setEnabled(false);

            // Shutdown current preview
            stopPreview();

            setCameraParameters(width, height);

            // Initialize preview display
            try {
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                Log.e(TAG, "Failed to set preview display in ");
            }

            // Start preview
            try {
                startPreview();
                mFrame.setEnabled(true);
            } catch (RuntimeException e) {
                Log.e(TAG, "Failed to start preview in surfaceChanged()");
            }
        }

        // Change camera parameters
        private void setCameraParameters(int width, int height) {

            // Get camera parameters object
            Camera.Parameters p = mCamera.getParameters();

            // Find closest supported preview size
            Camera.Size bestSize = findBestSize(p, width, height);

            // FIX - Should lock in landscape mode?

            int tmpWidth = bestSize.width;
            int tmpHeight = bestSize.height;

            if (bestSize.width < bestSize.height) {
                tmpWidth = bestSize.height;
                tmpHeight = bestSize.width;
            }

            p.setPreviewSize(tmpWidth, tmpHeight);
            mCamera.setParameters(p);
        }

        // Determine the largest supported preview size
        private Camera.Size findBestSize(Camera.Parameters parameters,
                                         int width, int height) {

            List<Camera.Size> supportedSizes = parameters
                    .getSupportedPreviewSizes();

            Camera.Size bestSize = supportedSizes.remove(0);

            for (Camera.Size size : supportedSizes) {
                if ((size.width * size.height) > (bestSize.width * bestSize.height)) {
                    bestSize = size;
                }
            }

            return bestSize;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Do Nothing
        }
    };
}

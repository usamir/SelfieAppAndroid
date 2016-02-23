package com.samir.selfieapp;

import android.app.AlarmManager;
import android.app.ListFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelfieListFragment extends ListFragment {

    private static final String TAG = "SelfieListFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int NOTIFICATION_ID = 11151990;

    private SelfieListAdapter mAdapter;
    private List<SelfieRecord> selfieRecords;
    private Date mLatestPic = new Date(0,0,0);

    private static PendingIntent mNotificationReceiverPendingIntent;
    private static Intent mNotificationReceiverIntent;

    private static final long INITIAL_ALARM_DELAY = 1 * 60 * 1000;
    private static final long REPEAT_ALARM_DELAY = 1 * 60 * 1000;

    private static Context mContext;

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "Resumed activity!");
        List<SelfieRecord> selfieList = new ArrayList<SelfieRecord>();

        // Iterate throught the directory where this App have stored pics
        // and check is new pic, if it is add to the list of pictures

        File storageDir = new File(Environment.getExternalStoragePublicDirectory (
                Environment.DIRECTORY_PICTURES), "SelfieApp");
        for (File f : storageDir.listFiles()) {
            if (f.isFile()) {
                Date lastModDate = new Date(f.lastModified());
                if (lastModDate.compareTo(mLatestPic) > 0) {
                    mLatestPic = lastModDate;
                } else {
                    continue;
                }
                Log.i(TAG, f.getAbsolutePath());
                String timeStamp = new SimpleDateFormat("yyyy-MM-d_HH:mm")
                        .format(lastModDate);

                Log.i(TAG, timeStamp);
                SelfieRecord selfie = new SelfieRecord(f.getAbsolutePath(), timeStamp);

                Log.i(TAG, "Add new one!");
                selfieList.add(selfie);

            }
        }
        mAdapter.add(selfieList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "View is created!");

        return inflater.inflate(R.layout.activity_selfie_list_fragment, null, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mContext = getActivity().getApplicationContext();

        Log.i(TAG, "Set up array of selfies");
        selfieRecords = new ArrayList<SelfieRecord>();

        // Iterate throught the directory where this App have stored pics
        // and add those pictures and time stamps to Selfie record
        File storageDir = new File(Environment.getExternalStoragePublicDirectory (
                Environment.DIRECTORY_PICTURES), "SelfieApp");
        for (File f : storageDir.listFiles()) {
            if (f.isFile()) {
                Log.i(TAG, f.getAbsolutePath());
                Date lastModDate = new Date(f.lastModified());
                mLatestPic = lastModDate;
                String timeStamp = new SimpleDateFormat("yyyy-MM-d_HH:mm")
                        .format(lastModDate);
                Log.i(TAG, timeStamp);
                selfieRecords.add(new SelfieRecord(f.getAbsolutePath(), timeStamp));
            }
        }

        Log.i(TAG, "Set up Adapter");
        // Create List Adapter with Selfie already recorded
        mAdapter = new SelfieListAdapter(getActivity(), selfieRecords);


        Log.i(TAG, "Setting up Adapter");
        setListAdapter(mAdapter);

        // Set up Alarm
        setupAlarm();

    }

    /** Showing details of clicked selfie */
    private void showDetail(String imageUri) {
        Intent intent;
        intent = new Intent(mContext, SelfieDetailActivity.class);
        intent.setData(Uri.parse(imageUri));
        startActivity(intent);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i(TAG, "On list item is clicked!!");

        SelfieRecord selfie = (SelfieRecord) mAdapter.getItem(position);
        showDetail(selfie.getmURI());
    }

    private static void setupNotificationIntent() {
        mNotificationReceiverIntent = new Intent(mContext, SelfieNotificationReceiver.class);
        Log.i(TAG, "Intent is created");
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                mNotificationReceiverIntent, 0);
        Log.i(TAG, "Pending intent is created");
    }

    static public void setupAlarm() {

        // set up notification receiver intent
        setupNotificationIntent();

        Log.i(TAG, "Setting up Alarm");
        AlarmManager alarmManager =
                (AlarmManager) mContext
                        .getSystemService(mContext.ALARM_SERVICE);
        Log.i(TAG, "Set up repeating alarm");
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                REPEAT_ALARM_DELAY, mNotificationReceiverPendingIntent);
    }

    public static void cancelAlarm() {
        if (null == mNotificationReceiverPendingIntent)
            return;
        AlarmManager alarmManager =
                (AlarmManager) mContext
                        .getSystemService(mContext.ALARM_SERVICE);
        alarmManager.cancel(mNotificationReceiverPendingIntent);
    }

}

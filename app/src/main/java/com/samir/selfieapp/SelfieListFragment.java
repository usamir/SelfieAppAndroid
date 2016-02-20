package com.samir.selfieapp;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelfieListFragment extends ListFragment {

    private String TAG = "SelfieListFragment";
    private SelfieListAdapter mAdapter;
    private List<SelfieRecord> selfieRecords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "View is created!");
        return inflater.inflate(R.layout.activity_selfie_list_fragment, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
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
    }
}

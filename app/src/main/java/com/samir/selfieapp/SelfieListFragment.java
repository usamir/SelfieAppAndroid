package com.samir.selfieapp;

import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelfieListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "SelfieListFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private SelfieListAdapter mAdapter;
    private List<SelfieRecord> selfieRecords;
    private Date mLatestPic = new Date(0,0,0);

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
    }

    private void showDetail(String imageUri) {
        Intent intent;
        intent = new Intent(getActivity().getApplicationContext(), SelfieDetailActivity.class);
        intent.setData(Uri.parse(imageUri));
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Item is clicked!!");
        SelfieRecord selfie = (SelfieRecord) mAdapter.getItem(position);
        showDetail(selfie.getmURI());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i(TAG, "On list item is clicked!!");

        SelfieRecord selfie = (SelfieRecord) mAdapter.getItem(position);
        showDetail(selfie.getmURI());
    }
}

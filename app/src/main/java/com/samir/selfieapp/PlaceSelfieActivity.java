package com.samir.selfieapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.samir.selfieapp.R;

import java.util.ArrayList;

/**
 * Created by usamir on 14.2.2016.
 */
public class PlaceSelfieActivity extends ListActivity {

    private static final String TAG = "Selfie Activity";

    private SelfieListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        ListView selfiesListView = getListView ();

        Log.i (TAG, "Inflate view ... ");
        View footerView = getLayoutInflater().
                inflate (R.layout.activity_main, null);
        footerView.setEnabled (false);

        selfiesListView.addFooterView (footerView);
        mAdapter = new SelfieListAdapter(getApplicationContext());
        setListAdapter (mAdapter);
    }
}

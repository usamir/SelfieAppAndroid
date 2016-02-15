package com.samir.selfieapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.samir.selfieapp.R;

import java.util.ArrayList;

/**
 * Created by usamir on 14.2.2016.
 */
public class PlaceSelfieActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ListView listview = (ListView) findViewById(R.id.listView);
    }
}

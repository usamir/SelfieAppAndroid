package com.samir.selfieapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int IS_ALIVE = Activity.RESULT_FIRST_USER;
    public static final String DATA_REFRESHED_ACTION = "com.samir.selfieapp.DATA_REFRESHED";

    private BroadcastReceiver mRefreshReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Started main activity!");

        // Support tollbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate (R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_camera:
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                Log.e(TAG, "Starting activity ");
                this.startActivity(intent);
                break;
            case R.id.cancel_alarm:
                SelfieListFragment.cancelAlarm();
                Toast.makeText(MainActivity.this,
                        "Your alarm is cancelled", Toast.LENGTH_SHORT).show();
                break;
            case R.id.start_alarm:
                SelfieListFragment.setupAlarm();
                Toast.makeText(MainActivity.this,
                        "Your alarm is set up", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}



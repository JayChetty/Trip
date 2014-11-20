package com.jaypaulchetty.trip;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;


public class TripActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TripActivity", "Starting the Activity YO");
        setContentView(R.layout.activity_trip);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new TripFragment();
            fm.beginTransaction()
                .add(R.id.fragmentContainer,fragment)
                .commit();
        }

    }
}

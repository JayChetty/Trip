package com.jaypaulchetty.trip;

import android.app.Fragment;


public class TripActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TripFragment();
    }
}

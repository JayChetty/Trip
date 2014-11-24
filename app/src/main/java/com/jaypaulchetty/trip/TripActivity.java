package com.jaypaulchetty.trip;

import android.support.v4.app.Fragment;


public class TripActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TripFragment();
    }
}

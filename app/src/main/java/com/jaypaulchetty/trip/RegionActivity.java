package com.jaypaulchetty.trip;

import android.app.Fragment;

/**
 * Created by jay on 26/11/14.
 */
public class RegionActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegionFragment();
    }

}


package com.jaypaulchetty.trip;

import android.app.Fragment;

/**
 * Created by jay on 03/12/14.
 */
public class RegionMapActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment(){
        return new RegionMapFragment();
    };
}

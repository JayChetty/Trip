package com.jaypaulchetty.trip;
import android.support.v4.app.Fragment;
/**
 * Created by jay on 23/11/14.
 */
public class RegionActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment(){
        return new RegionFragment();
    };
}
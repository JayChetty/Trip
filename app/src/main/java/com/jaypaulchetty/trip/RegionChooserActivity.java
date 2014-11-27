package com.jaypaulchetty.trip;
import android.support.v4.app.Fragment;
/**
 * Created by jay on 23/11/14.
 */
public class RegionChooserActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment(){
        return new RegionChooserFragment();
    };
}

package com.jaypaulchetty.trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RegionChooserFragment extends ListFragment {
    private static final String TAG = "RegionChooserFragment";
    public static final String REGION_FOR_TRIPS = "com.jaypaulchetty.trip.region";
    public String[] mRegions;

    private RegionTimes mRegionTimes;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        TripCreator tripCreator =  TripCreator.get(getActivity());
        mRegionTimes = RegionTimes.get();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, RegionTimes.getRegions());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "Starting Click");
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity(),RegionActivity.class);
        String[] regions =  RegionTimes.getRegions();
        Log.d(TAG, "regions " + regions);
        String region = regions[position];
        Log.d(TAG, region);
        i.putExtra(REGION_FOR_TRIPS, region);
        int time = mRegionTimes.getTime(region);
        Log.d(TAG, Integer.toString(time));
        i.putExtra(RegionFragment.REGION_BEST_TIME, time);
        Log.d(TAG, region);
        startActivity(i);
    }
}

package com.jaypaulchetty.trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegionChooserFragment extends ListFragment {
    private static final String TAG = "RegionChooserFragment";
    public static final String REGION_FOR_TRIPS = "com.jaypaulchetty.trip.region";
    public String[] mRegions;

    private RegionScores mRegionScores;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        TripCreator.setCountries(getActivity());
        mRegionScores = RegionScores.get(getActivity());
        ArrayList regionList = new ArrayList(Arrays.asList(RegionScores.getRegions()));
        RegionAdapter adapter= new RegionAdapter(regionList);
//        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, RegionScores.getRegions());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "Starting Click");
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity(),RegionActivity.class);
        String region = RegionScores.getRegions()[position];
        i.putExtra(REGION_FOR_TRIPS, region);
//        i.putExtra(RegionFragment.REGION_BEST_TIME, mRegionTimes.getTime(region));
        startActivity(i);
    }

    private class RegionAdapter extends ArrayAdapter<String>{
        public RegionAdapter(ArrayList<String> regions){
            super(getActivity(),0, regions);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_region,null);
            }

            String regionName = getItem(position);
            TextView textview = (TextView) convertView.findViewById(R.id.region_list_item_title);
            textview.setText(regionName);

            return convertView;
        }

    }
}

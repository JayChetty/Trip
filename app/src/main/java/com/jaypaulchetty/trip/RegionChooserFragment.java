package com.jaypaulchetty.trip;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;


public class RegionChooserFragment extends ListFragment {
    private static final String TAG = "RegionChooserFragment";
    public static final String REGION_FOR_TRIPS = "com.jaypaulchetty.region.region";
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
        getActivity().getActionBar().setTitle("Regions");
        setListAdapter(adapter);

        //hack to force menu to show on action bar
        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
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

            ImageView oneBottom = (ImageView) convertView.findViewById(R.id.one);
            ImageView twoBottom = (ImageView) convertView.findViewById(R.id.two);
            ImageView threeBottom = (ImageView) convertView.findViewById(R.id.three);

            RegionScores scores = RegionScores.get(getActivity());

            if(scores.getGrade(regionName,1)==0) {
                oneBottom.setImageResource(R.drawable.one_new);
            } else if(scores.getGrade(regionName,1)==1){
                oneBottom.setImageResource(R.drawable.one_completed);
            } else {
                oneBottom.setImageResource(R.drawable.one_bossed);
            }

            if(scores.getGrade(regionName,2)==0) {
                if(scores.getGrade(regionName,1)>0) {
                    twoBottom.setImageResource(R.drawable.two_new);
                }
                else{
                    twoBottom.setImageResource(R.drawable.two_restricted);
                }
            } else if(scores.getGrade(regionName,2)==1){
                twoBottom.setImageResource(R.drawable.two_completed);
            } else {
                twoBottom.setImageResource(R.drawable.two_bossed);
            }

            if(scores.getGrade(regionName,3)==0) {
                if(scores.getGrade(regionName,2)> 0) {
                    threeBottom.setImageResource(R.drawable.three_new);
                }
                else{
                    threeBottom.setImageResource(R.drawable.three_restricted);
                }
            } else if(scores.getGrade(regionName,3)==1){
                threeBottom.setImageResource(R.drawable.three_completed);
            } else {
                threeBottom.setImageResource(R.drawable.three_bossed);
            }


//            ImageView oneTop = (ImageView) convertView.findViewById(R.id.one_top);
//            oneTop.setImageResource(R.drawable.blank);
//            ImageView twoTop = (ImageView) convertView.findViewById(R.id.two_top);
//            twoTop.setImageResource(R.drawable.blank);
//            ImageView threeTop = (ImageView) convertView.findViewById(R.id.three_top);
//            threeTop.setImageResource(R.drawable.blank);
//            ImageView finishTop = (ImageView) convertView.findViewById(R.id.finish_top);
//            finishTop.setImageResource(R.drawable.blank);


            
            

            TextView titleView = (TextView) convertView.findViewById(R.id.region_list_item_title);
            titleView.setText(regionName);


            return convertView;
        }

    }
}

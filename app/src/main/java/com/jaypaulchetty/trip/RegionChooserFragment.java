package com.jaypaulchetty.trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RegionChooserFragment extends ListFragment {
    private static final String TAG = "ListFragment";

    private String[] mRegions= {"Africa", "Americas", "Europe", "Asia", "World"};
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, mRegions);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity(),TripActivity.class);
        String region = mRegions[position];
        i.putExtra(TripFragment.REGION_FOR_TRIPS, region);
        Log.d(TAG, region);
        startActivity(i);
    }
}

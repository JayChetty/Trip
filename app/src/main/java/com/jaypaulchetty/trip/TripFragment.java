package com.jaypaulchetty.trip;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class TripFragment extends ListFragment {
    private ArrayList<Country> mCountries;
    private Trip mTrip;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("TripFragement", "Starting the Fragment YO");
        CountryJSONLoader loader = new CountryJSONLoader(getActivity());
        try {
            mCountries = loader.loadCountries();
        } catch (Exception e){
            Log.e("TRip", "error loading crimes", e);
            mCountries = new ArrayList<Country>();
        }
        mTrip = new TripCreator(mCountries).createTrip();

        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getActivity(),android.R.layout.simple_list_item_1,mTrip.getRoute(0));
        setListAdapter(adapter);

    }

//    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
//        View v = inflater.inflate(R.layout.fragment_trip, parent, false);
//        return v;
//    }

}

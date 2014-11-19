package com.jaypaulchetty.trip;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;


public class TripActivity extends Activity {

    private ArrayList<Country> mCountries;
    private Trip mTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Trip", "blalalalalal");
        setContentView(R.layout.activity_trip);
        CountryJSONLoader loader = new CountryJSONLoader(this);
        try {
            mCountries = loader.loadCountries();
        } catch (Exception e){
            Log.e("TRip", "error loading crimes", e);
            mCountries = new ArrayList<Country>();
        }
        mTrip = new TripCreator(mCountries).createTrip();

        Log.e("TRip", "trip" + mTrip);
    }
}

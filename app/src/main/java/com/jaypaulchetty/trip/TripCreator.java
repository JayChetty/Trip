package com.jaypaulchetty.trip;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jay on 19/11/14.
 */
public class TripCreator {
    private ArrayList<Country> mCountries;
    private static TripCreator sTripCreator;
    private Context mContext;

    public static TripCreator get(Context context){
        if(sTripCreator == null){
            sTripCreator = new TripCreator(context);
        }
        return sTripCreator;
    }

    private TripCreator(Context context){
        mContext = context;
        mCountries = getCountries();//This should be done on a background thread
    }

    private ArrayList<Country> getCountries(){
        CountryJSONLoader loader = new CountryJSONLoader(mContext);
        ArrayList<Country> countries = new ArrayList<Country>();
        try {
            countries = loader.loadCountries();
        } catch (Exception e){
            Log.e("TRip", "error loading crimes", e);
            countries = new ArrayList<Country>();
        }
        return countries;
    }

    public Trip createTrip(){
        Trip trip = new Trip();
        // test route
        ArrayList<Country> route = new ArrayList<Country>(mCountries.subList(10,14));
        trip.addRoute(route);
        return trip;
    }
}

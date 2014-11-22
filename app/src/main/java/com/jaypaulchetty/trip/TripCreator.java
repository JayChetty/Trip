package com.jaypaulchetty.trip;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jay on 19/11/14.
 */
public class TripCreator {
    private static TripCreator sTripCreator;
    private ArrayList<Country> mCountries;
    private Context mContext;

    public static TripCreator get(Context context){
        if(sTripCreator == null){
            sTripCreator = new TripCreator(context);
        }
        return sTripCreator;
    }

    public ArrayList<String> getCountryNames(){
        ArrayList<String> countryNames = new ArrayList<String>();
        int length = mCountries.size();
        for(int i=0;i<length;i++){
            countryNames.add(mCountries.get(i).getName());
        }
        return countryNames;
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
        Random random = new Random();
        int start = random.nextInt(mCountries.size()-6) + 1;
        ArrayList<Country> route = new ArrayList<Country>(mCountries.subList(start,start+4));
        trip.addRoute(route);
        return trip;
    }
}

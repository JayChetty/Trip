package com.jaypaulchetty.trip;

import java.util.ArrayList;

/**
 * Created by jay on 19/11/14.
 */
public class TripCreator {
    private ArrayList<Country> mCountries;

    public TripCreator(ArrayList<Country> countries){
        mCountries = countries;
    }

    public Trip createTrip(){
        Trip trip = new Trip();
        // test route
        ArrayList<Country> route = (ArrayList<Country>) mCountries.subList(0,3);
        trip.addRoute(route);
        return trip;
    }
}

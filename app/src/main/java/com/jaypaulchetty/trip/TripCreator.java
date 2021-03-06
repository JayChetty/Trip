package com.jaypaulchetty.trip;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by jay on 19/11/14.
 */
public class TripCreator {
    private static TripCreator sTripCreator;
    private static Map<String, ArrayList<Country>> sCountriesByRegion = new HashMap<String, ArrayList<Country>>();
    private static final String TAG="TripCreator";

    public static void setCountries(Context context){
        CountryJSONLoader loader = new CountryJSONLoader(context);
        try {
            sCountriesByRegion = loader.loadCountries();
        } catch (Exception e){
            Log.e(TAG, "error loading crimes", e);
        }
    }

    public static void setCountries(Map<String, ArrayList<Country>>  countryList){
        TripCreator.sCountriesByRegion = countryList;
    }

    public static TripCreator get(){
        if(sTripCreator == null){
            sTripCreator = new TripCreator();
        }
        return sTripCreator;
    }


    public ArrayList<String> getCountryNames(String region){
        ArrayList<Country> countries = sCountriesByRegion.get(region);
        ArrayList<String> countryNames = new ArrayList<String>();
        int length = countries.size();
        for(int i=0;i<length;i++){
            countryNames.add(countries.get(i).getName());
        }
        return countryNames;
    }

    public ArrayList<ArrayList<Country>> createSeedRoutes(Country country){
        ArrayList<Country> seed = new ArrayList<Country>();
        seed.add(country);
        ArrayList<ArrayList<Country>> seedRoutes = new ArrayList<ArrayList<Country>>();
        seedRoutes.add(seed);
        return seedRoutes;
    }

    public ArrayList<ArrayList<Country>> extendRoutes(String region, ArrayList<ArrayList<Country>> inRoutes, int numExtensions){
        ArrayList<ArrayList<Country>> outRoutes = inRoutes;
        for(int i=0;i<numExtensions;i++){
            outRoutes = extendRoutes(region, outRoutes);
        }
        return outRoutes;
    }

    public ArrayList<ArrayList<Country>> extendRoutes(String region,
                                                      ArrayList<ArrayList<Country>> inRoutes
                                                      ){
        Boolean hasExtendedARoute = false;
        //Make unique list of countries that exist
        ArrayList<Country> existingCountries = new ArrayList<Country>();
        for(ArrayList<Country> route : inRoutes){
            for(Country country: route){
                if(!existingCountries.contains(country)) {
                    existingCountries.add(country);
                }
            }
        }
        //Create new routes from existing routes
        ArrayList<ArrayList<Country>> outRoutes = new ArrayList<ArrayList<Country>>();
        for(ArrayList<Country> route : inRoutes) {
            Country lastCountryOfRoute = route.get(route.size()-1);
            ArrayList<Country> neighbours = getNeighbours(lastCountryOfRoute, region);
            for(Country country: neighbours) {
                if (!existingCountries.contains(country)){
                    ArrayList<Country> newRoute = new ArrayList<Country>(route);
                    newRoute.add(country);
                    outRoutes.add(newRoute);
                    hasExtendedARoute = true;
                }
            }
        }

        return outRoutes;
    }
    public Trip createTrip(String region, int length) {
        ArrayList<Country> countries = sCountriesByRegion.get(region);
        Random random = new Random();
        int start;
        Country startCountry;


        ArrayList<ArrayList<Country>> routes = new ArrayList<ArrayList<Country>>();
        while(routes.size() == 0) {
//            Log.d(TAG,"Starting loop");
            start = random.nextInt(countries.size()-1);
            startCountry = countries.get(start);
            Log.d(TAG, "Start Country" + startCountry);

            routes = extendRoutes(region, createSeedRoutes(startCountry),length-1);
            Log.d(TAG, "Got Routes" + routes);
        }

        Trip trip = new Trip();
        int routeSelect = 0;
        if (routes.size() > 1 ) {
            routeSelect = random.nextInt(routes.size() - 1);
        }

        ArrayList<Country> targetRoute = routes.get(routeSelect);
        Log.d(TAG, "adding target" + targetRoute);
        trip.addRoute(targetRoute);


        //find other routes with this target
        Country endCountry = targetRoute.get(targetRoute.size()-1);
        Log.d(TAG, "target country" + endCountry);
        for(int i = 0;i<routes.size();i++){
            if(i != routeSelect) {
                ArrayList<Country> route = routes.get(i);
                Country lastCountry = route.get(route.size()-1);
                if (lastCountry == endCountry) {
                    trip.addRoute(route);
                }
            }
        }

        Log.d(TAG, "returning region with routes " + trip.getRoutes());

        return trip;

    }

    public Trip createTrip(String region){

        ArrayList<ArrayList<Country>> routes = new ArrayList<ArrayList<Country>>();
        Random random = new Random();

        Log.d(TAG, "creating region");

        while(routes.size() == 0) {
            ArrayList<Country> countries = sCountriesByRegion.get(region);
            Log.d(TAG, "creating region for region"+ region);
            Log.d(TAG, "creating region for region countries"+ countries);
            //randomly select start country
            int start = random.nextInt(countries.size()-1);
//            int start = 25; //test Bolivia
            Log.d(TAG, "start" + start);
            Country startCountry = countries.get(start);
            Log.d(TAG, "start country" + startCountry);

            //create routes
            ArrayList<Country> neighbours = getNeighbours(startCountry, region);

            Log.d(TAG, "neigbours" + neighbours);

            for (int i = 0; i < neighbours.size(); i++) {
                Country neighbour = neighbours.get(i);
                Log.d(TAG, "from neigbour" + neighbour);
                ArrayList<Country> secondNeighbours = getNeighbours(neighbour, region);
                Log.d(TAG, "its neigbours" + secondNeighbours);
                for (int j = 0; j < secondNeighbours.size(); j++) {
                    Country secondNeighbour = secondNeighbours.get(j);
                    Log.d(TAG, "second neighbour" + secondNeighbour);
                    if (secondNeighbour != startCountry && !neighbours.contains(secondNeighbour)) {

                        ArrayList<Country> route = new ArrayList<Country>();
                        route.add(startCountry);
                        route.add(neighbour);
                        route.add(secondNeighbour);
                        Log.d(TAG, "second neighbour adding route" + route);
                        routes.add(route);
                    }
                }
            }
        Log.d(TAG, "and the routes are" + routes);
        }
        // randomly select route from selection
        Trip trip = new Trip();
        int routeSelect = 0;
        if (routes.size() > 1 ) {
             routeSelect = random.nextInt(routes.size() - 1);
        }

        ArrayList<Country> targetRoute = routes.get(routeSelect);
        Log.d(TAG, "adding target" + targetRoute);
        trip.addRoute(targetRoute);


        //find other routes with this target
        Country endCountry = targetRoute.get(targetRoute.size()-1);
        Log.d(TAG, "target country" + endCountry);
        for(int i = 0;i<routes.size();i++){
            if(i != routeSelect) {
                ArrayList<Country> route = routes.get(i);
                Country lastCountry = route.get(route.size()-1);
                if (lastCountry == endCountry) {
                    trip.addRoute(route);
                }
            }
        }

        Log.d(TAG, "returning region with routes " + trip.getRoutes());

        return trip;
    }

    public ArrayList<Country> getNeighbours(Country country, String region){
        ArrayList<String> neighbourStrings = country.getNeighboursCodes();
        ArrayList<Country> neighbours = new ArrayList<Country>();
        int length = neighbourStrings.size();
        for(int i=0; i<length;i++){
            Country neighbour = getCountry(neighbourStrings.get(i), region);
            if (neighbour != null) {
                neighbours.add(getCountry(neighbourStrings.get(i), region));
            }
        }
        return neighbours;
    }

    public Country getCountry(String code, String region){
        ArrayList<Country> countries = sCountriesByRegion.get(region);
        int length = countries.size();
        for(int i = 0; i < length ; i++){
            Country country = countries.get(i);
            if(country.getCode().equals(code)){
               return country;
            }
        }
        return null;
    }
}

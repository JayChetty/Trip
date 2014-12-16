package com.jaypaulchetty.trip;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CountryJSONLoader {
    private Context mContext;
    private static final String TAG = "CountryJSONLoader";

    public CountryJSONLoader(Context c){
        mContext = c;
    }
    public Map<String, ArrayList<Country>> loadCountries() throws IOException, JSONException {
        Log.d(TAG,"loading JSON from asset");

        Map<String, ArrayList<Country>> map = new HashMap<String, ArrayList<Country>>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.getAssets().open("countries.json");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            ArrayList<Country> allCountries = new ArrayList<Country>();
            //Build the array of from JSONObjects
            for(int i = 0; i < array.length(); i++){
                String region = array.getJSONObject(i).getString("region");
                ArrayList<Country> countriesForRegion = map.get(region);
                if (countriesForRegion == null) {
                    countriesForRegion = new ArrayList<Country>();
                    map.put(region, countriesForRegion );
                }
                Country country = new Country(array.getJSONObject(i));
                countriesForRegion.add(country);
                allCountries.add(country);
            }

            map.put("World", allCountries);


        } catch(FileNotFoundException e){

        } finally{
            if (reader != null)
                reader.close();
        }
        return map;
    }
}

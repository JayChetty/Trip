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

public class CountryJSONLoader {
    private Context mContext;
    public CountryJSONLoader(Context c){
        mContext = c;
    }

    public ArrayList<Country> loadCountries() throws IOException, JSONException {
        Log.d("JSON","loading JSON from asset");
        ArrayList<Country> countries = new ArrayList<Country>();
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
            //Build the array of from JSONObjects
            for(int i = 0; i < array.length(); i++){
                countries.add(new Country(array.getJSONObject(i)));
            }


        } catch(FileNotFoundException e){

        } finally{
            if (reader != null)
                reader.close();
        }
        return countries;
    }
}

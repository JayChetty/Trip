package com.jaypaulchetty.trip;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Country {
    private String mName;
    private String mCode;
    private ArrayList<String> mNeighboursCodes;

    public String getCode() {
        return mCode;
    }

    public Country(JSONObject json) throws JSONException {
        mName = json.getString("name");
        mCode = json.getString("alpha3Code");
        mNeighboursCodes = new ArrayList<String>();
        JSONArray tempNeighbours= json.getJSONArray("borders");


        int len = tempNeighbours.length();
        if(len > 0) {
            for (int i = 0; i < len; i++) {
                mNeighboursCodes.add(tempNeighbours.get(i).toString());
            }
        }
    }

    public String getName() { return mName; }

    public ArrayList<String> getNeighboursCodes(){
        return mNeighboursCodes;
    }

    public String toString(){
        return mName;
    }
}

package com.jaypaulchetty.trip;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegionTimes {
//    private static final String[] sRegions= {"Africa", "Americas", "Europe", "Asia", "World"};

    private HashMap<String, Integer> mTimes = new HashMap<String, Integer>();
    private static RegionTimes sRegionTimes;
    public static final String TIMES_NAME = "MyTimesFile";
    private Context mContext;
    private static final String TAG="RegionTimes";

    public static String[] getRegions(){
        return new String[] {"Africa", "Americas", "Europe", "Asia", "World"};
    }

    public static RegionTimes get(Context context){
        if(sRegionTimes == null){
            sRegionTimes = new RegionTimes(context);
        }
        return sRegionTimes;
    }
    private RegionTimes(Context context){
        mContext = context;
        loadTimes();
    }

    public void loadTimes(){
        Log.d(TAG, "Loading times");
        SharedPreferences settings = mContext.getSharedPreferences(TIMES_NAME, 0);
        for(String region : RegionTimes.getRegions()){
            int time = settings.getInt(region,999999);
            Log.d(TAG, "region" + region + "time " + time);
            setTime(region, time);
        }
    }

    public void saveTimes(){
        Log.d(TAG, "Saving times");
        SharedPreferences settings = mContext.getSharedPreferences(TIMES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        for(String region : RegionTimes.getRegions()){
            int time = getTime(region);
            editor.putInt(region, time);
        }
        editor.commit();
    }
    public HashMap<String, Integer> getTimes(){
        return mTimes;
    }

    public int getTime(String region){
        return mTimes.get(region);
    }

    public void setTime(String region, int time){
        mTimes.put(region, time);
    }
}

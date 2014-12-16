package com.jaypaulchetty.trip;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegionTimes {
    private static final String[] sRegions= {"Africa", "Americas", "Europe", "Asia", "World"};
    private HashMap<String, int[]> mTimes = new HashMap<String, int[]>();
    private static RegionTimes sRegionTimes;
    public static final String TIMES_NAME = "MyTimesFile";
    private Context mContext;
    private static final String TAG="RegionTimes";

    public static String[] getRegions(){
       return sRegions;
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
        for(String region : sRegions){
            Log.d(TAG, "getting times for region" + region);
            mTimes.put(region, new int[3]);//init
            for (int i = 0; i < 3; i++) {
                int length = i+1;
                String lengthString = Integer.toString(length);
                int time = settings.getInt(region+lengthString,-1);
                Log.d(TAG, "got time for length "+ length + " time  "  + time);
                setTime(region,length,time);
            }

        }
    }

    public void saveTimes(){
        SharedPreferences settings = mContext.getSharedPreferences(TIMES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        for(String region : sRegions){
            for (int i = 0; i < 3; i++) {
                int length = i+1;
                String lengthString = Integer.toString(length);
                int time = getTime(region, length);
                editor.putInt(region+lengthString, time);
            }
        }
        editor.commit();
    }

    public HashMap<String, int[]> getTimes(){
        return mTimes;
    }

    public int getTime(String region, int length){
        int[] timesArray = mTimes.get(region);
        int time = timesArray[length-1];
        return time;
    }

    public int[] getTimes(String region){
        return mTimes.get(region);
    }

    public void setTime(String region, int length, int time){
        int[] timesArray = mTimes.get(region);
        timesArray[length-1] = time;
    }
}

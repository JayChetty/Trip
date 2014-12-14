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
//    private static final String[] sRegions= {"Africa", "Americas", "Europe", "Asia", "World"};

//    private HashMap<String, Integer> mTimes = new HashMap<String, Integer>();
    private HashMap<String, int[]> mTimes = new HashMap<String, int[]>();
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
        String[] regions = RegionTimes.getRegions();
        Log.d(TAG, "got regions yah" + regions.toString());
        for(String region : regions){
            Log.d(TAG, "loading times for region " + region );
            Set<String> set = settings.getStringSet("region", null);
            Log.d(TAG, "got set " + set );
            int[] timesArray = new int[3];


            if(set != null) {
                String[] stringArray=  set.toArray(new String[0]);
                Log.d(TAG, "and string array " + stringArray );
                for (int i = 0; i < 3; i++) {
                    timesArray[i] = Integer.parseInt(stringArray[i]);
                }
            } else{
                for (int i = 0; i < 3; i++) {
                    timesArray[i] = -1;
                }
            }

            Log.d(TAG, "about to put something in"  + region );

            mTimes.put(region, timesArray);

            Log.d(TAG, "put something in"  + region );

        }
        Log.d(TAG, "Loaded times");
    }

    public void saveTimes(){
        Log.d(TAG, "Saving times");
        SharedPreferences settings = mContext.getSharedPreferences(TIMES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        for(String region : RegionTimes.getRegions()){
            int[] times = getTimes(region);
            Set<String> set = new HashSet<String>();
            for(int i: times){
                set.add(Integer.toString(i));
            }
            editor.putStringSet(region, set);
        }
        editor.commit();
    }

    public HashMap<String, int[]> getTimes(){
        return mTimes;
    }

    public int getTime(String region, int length){
        Log.d(TAG,"Getting time Region" + region);
        Log.d(TAG,"Getting time length" + length);
        int[] timesArray = mTimes.get(region);
        return timesArray[length-1];
    }

    public int[] getTimes(String region){
        return mTimes.get(region);
    }

    public void setTime(String region, int length, int time){
        Log.d(TAG,"Setting time Region" + region);
        Log.d(TAG,"Setting time length" + length);
        Log.d(TAG,"Setting time" + time);
        int[] timesArray = mTimes.get(region);
        timesArray[length-1] = time;
    }
}

package com.jaypaulchetty.trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegionTimes {
//    private static final String[] sRegions= {"Africa", "Americas", "Europe", "Asia", "World"};

    private HashMap<String, Integer> mTimes = new HashMap<String, Integer>();
    private static RegionTimes sRegionTimes;

    public static String[] getRegions(){
        return new String[] {"Africa", "Americas", "Europe", "Asia", "World"};
    }

    public static RegionTimes get(){
        if(sRegionTimes == null){
            sRegionTimes = new RegionTimes();
        }
        return sRegionTimes;
    }
    private RegionTimes(){
        for(String region : RegionTimes.getRegions()){
            mTimes.put(region,0);
        }
    }
    public HashMap<String, Integer> getTimes(){
        return mTimes;
    }

    public int getTime(String region){
        return mTimes.get(region);
    }

    public int setTime(String region, int time){
        return mTimes.put(region, time);
    }
}

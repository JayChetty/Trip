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

public class RegionScores {
    private static final String[] sRegions= {"Africa", "Americas", "Europe", "Asia", "World"};
    private HashMap<String, int[]> mScores = new HashMap<String, int[]>();
    private static RegionScores sRegionScores;
    public static final String SCORES_NAME = "MyScoresFile";
    private Context mContext;
    private static final String TAG="RegionScores";
    private int mPassMark = 8;
    private int mMeritMark = 10;
    private int mDistinctionMark = 12;

    public static String[] getRegions(){
       return sRegions;
    }

    public static RegionScores get(Context context){
        if(sRegionScores == null){
            sRegionScores = new RegionScores(context);
        }
        return sRegionScores;
    }
    private RegionScores(Context context){
        mContext = context;
        loadScores();
    }

    public int getPassMark(){
        return mPassMark;
    }
    public int getMeritMark(){
        return mMeritMark;
    }
    public int getDistinctionMark(){
        return mDistinctionMark;
    }


    public void loadScores(){
        Log.d(TAG, "Loading scores");
        SharedPreferences settings = mContext.getSharedPreferences(SCORES_NAME, 0);
        for(String region : sRegions){
            Log.d(TAG, "getting Scores for region" + region);
            mScores.put(region, new int[3]);//init
            for (int i = 0; i < 3; i++) {
                int length = i+1;
                String lengthString = Integer.toString(length);
                int score = settings.getInt(region+lengthString,-1);
                Log.d(TAG, "got Score for length "+ length + " score  "  + score);
                setScore(region,length,score);
            }

        }
    }

    public void saveScores(){
        SharedPreferences settings = mContext.getSharedPreferences(SCORES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        for(String region : sRegions){
            for (int i = 0; i < 3; i++) {
                int length = i+1;
                String lengthString = Integer.toString(length);
                int score = getScore(region, length);
                editor.putInt(region+lengthString, score);
            }
        }
        editor.commit();
    }

    public HashMap<String, int[]> getScores(){
        return mScores;
    }

    public int getScore(String region, int length){
        int[] scoresArray = mScores.get(region);
        int score = scoresArray[length-1];
        return score;
    }

    public int[] getScores(String region){
        return mScores.get(region);
    }

    public void setScore(String region, int length, int score){
        int[] scoresArray = mScores.get(region);
        scoresArray[length-1] = score;
    }

    public int getGrade(String region, int length){
        //0-not passed, 1-passed, 2-merit, 3 distinction
        int score = getScore(region,length);
        int mark = 0;
        if (score >= mPassMark){
               mark++;}
        if (score >= mMeritMark){
               mark++;}
        if (score >= mDistinctionMark){
               mark++;}

        return mark;

    }
}

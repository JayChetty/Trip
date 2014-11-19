package com.jaypaulchetty.trip;

import java.util.ArrayList;

/**
 * Created by jay on 18/11/14.
 */
public class Trip {
    private ArrayList<ArrayList<Country>> mRoutes;

    public Trip(){
        mRoutes = new ArrayList<ArrayList<Country>>();
    }

    public void addRoute(ArrayList<Country> route){
        mRoutes.add(route);
    }

    public ArrayList<Country> getRoute(int pos){
        return mRoutes.get(pos);
    }
}

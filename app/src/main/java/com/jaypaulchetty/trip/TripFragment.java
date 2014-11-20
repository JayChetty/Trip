package com.jaypaulchetty.trip;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TripFragment extends ListFragment {
    private ArrayList<Country> mCountries;
    private Trip mTrip;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("TripFragement", "Starting the Fragment YO");
        CountryJSONLoader loader = new CountryJSONLoader(getActivity());
        try {
            mCountries = loader.loadCountries();
        } catch (Exception e){
            Log.e("TRip", "error loading crimes", e);
            mCountries = new ArrayList<Country>();
        }
        mTrip = new TripCreator(mCountries).createTrip();

        TripArrayAdapter adapter = new TripArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,mTrip.getRoute(0));
        setListAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
       super.onCreateOptionsMenu(menu,inflater);
       inflater.inflate(R.menu.trip,menu);
    }


    public class TripArrayAdapter extends ArrayAdapter<Country>{

        public TripArrayAdapter(Context context, int resource, List<Country> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("GettingView","position" + position);
            View out;
            if(position == getCount()-1 || position == 0 ) {
                out = super.getView(position, convertView, parent);
            }
            else {
                EditText view = new EditText(getContext());
                out = view;
                view = null;
            }
            return out;

        }
    }

}

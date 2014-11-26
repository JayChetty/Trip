package com.jaypaulchetty.trip;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TripFragment extends ListFragment {
    public static final String REGION_FOR_TRIPS = "com.jaypaulchetty.trip.region";
    private Trip mTrip;
    private TripArrayAdapter mAdapter;
    private static final String TAG ="ListFragment";
    String mRegion;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRegion = getActivity().getIntent().getStringExtra(REGION_FOR_TRIPS);
        Log.d(TAG, "fragment starting with region " +  mRegion);
        TripCreator tripCreator =  TripCreator.get(getActivity());
        mTrip = tripCreator.createTrip( mRegion);
        mAdapter = new TripArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,mTrip.getRoute(0));
        setListAdapter(mAdapter);
    }

    private void createNewTrip(){
        mTrip = TripCreator.get(getActivity()).createTrip(mRegion);
        mAdapter.clear();
        mAdapter.addAll(mTrip.getRoute(0));
        mAdapter.notifyDataSetChanged();
    }

    private Boolean answerPasses(ArrayList<String> answers){
        Boolean passed = false;
        for(int i=0;i<mTrip.numRoutes(); i++){//loop through routes
            Boolean correctRoute = true;
            for(int j=0; j<answers.size(); j++) {
                String answerString = answers.get(j);
                String solutionString = mTrip.getRoute(i).get(j+1).toString();
                    if(!answerString.equals(solutionString)){
                        correctRoute = false;
                        break;
                    }
            }
            if(correctRoute == true){
                passed=true;
                break;
            }
        }
        return passed;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_item_show_answer:
                String answer= "";
                for(int i=0;i<mTrip.numRoutes(); i++) {
                    answer = answer.concat("Route " + (i+1) + mTrip.getRoute(i).toString() +"\n");
                    Log.d(TAG,"concating string" + mTrip.getRoute(i).toString());
                }
                Toast answerToast = Toast.makeText(this.getActivity(), answer, Toast.LENGTH_LONG);
                answerToast.show();
                return true;
            case R.id.menu_item_check_route:
                Log.d(TAG,"item clicked");
                ArrayList<AutoCompleteTextView> answerViews = mAdapter.getAnswerViews();
                int length = answerViews.size();
                ArrayList<String> answers = new ArrayList<String>();
                //put together string of arrays
                for(int j=0; j<length; j++){
                    String answerString = answerViews.get(j).getText().toString();
                    answers.add(answerString);
                }
                if(answerPasses(answers)){
                    Log.d(TAG,"CORRECT");
                    Toast toast = Toast.makeText(this.getActivity(), "You did it!", Toast.LENGTH_LONG);
                    toast.show();
                    createNewTrip();
                }else{
                    Log.d(TAG,"FALSE");
                    Toast toast = Toast.makeText(this.getActivity(), "Sorry, not correct!", Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
       super.onCreateOptionsMenu(menu,inflater);
       inflater.inflate(R.menu.trip,menu);
    }


    public class TripArrayAdapter extends ArrayAdapter<Country>{
        private ArrayList<AutoCompleteTextView> mAnswerViews;
        private ArrayAdapter<String> mCountryAdapter;


        public ArrayList<AutoCompleteTextView> getAnswerViews() {
            return mAnswerViews;
        }

        public void clearAnswerViews(){
            mAnswerViews.clear();
        };

        public TripArrayAdapter(Context context, int resource, List<Country> objects) {
            super(context, resource, objects);
            mAnswerViews = new ArrayList<AutoCompleteTextView>();
            TripCreator tripCreator =  TripCreator.get(getActivity());
            mCountryAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, tripCreator.getCountryNames(mRegion));
        }

        @Override
        public void clear() {
            super.clear();
            mAnswerViews.clear();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("GettingView","position" + position);
            View out;
            if(position == getCount()-1 || position == 0 ) {
                out = super.getView(position, convertView, parent);
            }
            else {
                AutoCompleteTextView view = new AutoCompleteTextView(getContext());
                view.setAdapter(mCountryAdapter);
                mAnswerViews.add(view);
                out = view;
                view = null;
            }
            return out;

        }
    }

}

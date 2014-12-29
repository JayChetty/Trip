package com.jaypaulchetty.trip;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ListFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class TripFragment extends Fragment {
    public static final String TRIP_PASSED = "TripPassed";
    public static final String TRIP_SCORE = "TripScore";
    private Trip mTrip;
    private TripArrayAdapter mAdapter;
    private static final String TAG ="ListFragment";
    private String mRegion;
    private int mTripNum = 1;
    private int mNumTrips = 10;
    private int mNumCompleted = 0;
    private long mStartTime = 0;
    private long mEndTime = 0;
    private static final int sMistakesAllowed = 3;
    private int mTripLength = 3;
    private long mDuration = 60000;
    TextView mTimeView;
    CountDownTimer mTimer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRegion = getActivity().getIntent().getStringExtra(RegionChooserFragment.REGION_FOR_TRIPS);
        mTripLength = getActivity().getIntent().getIntExtra(RegionFragment.TRIP_LENGTH, 1);

        Log.d(TAG, "fragment starting with region " +  mRegion);
        setTrip();

        Log.d(TAG, "the length is" + mTripLength);

        switch(mTripLength) {
            case 3:
                mDuration = 60000;
                break;
            case 4:
                mDuration = 90000;
                break;
            case 5:
                mDuration = 120000;
                break;
        }



    }

    public void onPause(){
        mTimer.cancel();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trip, container, false);
        mAdapter = new TripArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,mTrip.getRoute(0));
        ListView list = (ListView) v.findViewById(R.id.trip_list);
        list.setAdapter(mAdapter);
        mTimeView = (TextView) v.findViewById(R.id.time_view);

        mTimer = new CountDownTimer(mDuration, 1000) {

            public void onTick(long millisUntilFinished) {

                mTimeView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Toast answerToast = Toast.makeText(getActivity(), "lalala", Toast.LENGTH_LONG);
                answerToast.show();
                int timeTaken = (int) (mEndTime - mStartTime);
                Intent i = new Intent(getActivity(),RegionActivity.class);
                i.putExtra(RegionChooserFragment.REGION_FOR_TRIPS, mRegion);
                i.putExtra(TRIP_PASSED, true);
                i.putExtra(TRIP_SCORE, mNumCompleted);
                Log.d(TAG,"setting result");
                mNumCompleted++;
                getActivity().setResult(Activity.RESULT_OK, i);
                getActivity().finish();
//                mTextField.setText("done!");
            }
        }.start();

        return v;
    }

    private void setTrip(){
        mTrip = TripCreator.get().createTrip(mRegion,mTripLength);
    }

    private void createNewTrip(){
        setTrip();
        mAdapter.clear();
        mAdapter.addAll(mTrip.getRoute(0));
        mAdapter.notifyDataSetChanged();
        mTripNum++;
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
                Toast answerToast = Toast.makeText(getActivity(), answer, Toast.LENGTH_LONG);
                answerToast.show();
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
            TripCreator tripCreator =  TripCreator.get();
            mCountryAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, tripCreator.getCountryNames(mRegion));
        }

        @Override
        public void clear() {
            super.clear();
            mAnswerViews.clear();
        }

        private void checkAnswer(){
            Log.d(TAG, "Item has been sheclected Yo");
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
                mNumCompleted++;
//                            Toast toast = Toast.makeText(this.getActivity(), "Correct!", Toast.LENGTH_LONG);
//                            toast.show();
                createNewTrip();
            }else{
                Log.d(TAG,"FALSE");
//                            Toast toast = Toast.makeText(this.getActivity(), "Sorry, not correct!", Toast.LENGTH_LONG);
//                            toast.show();
            }
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

                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        checkAnswer();
                    }
                });

                mAnswerViews.add(view);
                out = view;
            }
            return out;

        }
    }

}

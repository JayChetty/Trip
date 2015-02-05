package com.jaypaulchetty.trip;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;

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
    TextView mTimeView, mCountView;
    CountDownTimer mTimer;
    ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mRegion = getActivity().getIntent().getStringExtra(RegionChooserFragment.REGION_FOR_TRIPS);
        mTripLength = getActivity().getIntent().getIntExtra(RegionFragment.TRIP_LENGTH, 1);
        mDuration = getActivity().getIntent().getLongExtra(RegionFragment.TRIP_DURATION, 1);
        Log.d(TAG, "fragment starting with region " +  mRegion);
        setTrip();
    }

    public void onPause(){
        super.onPause();
        mTimer.cancel();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trip, container, false);
        mAdapter = new TripArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,mTrip.getRoute(0));
        mList = (ListView) v.findViewById(R.id.trip_list);
        mList.setAdapter(mAdapter);
        mCountView = (TextView) v.findViewById(R.id.count_view);
        mCountView.setText("Completed:" + mNumCompleted);
        mTimeView = (TextView) v.findViewById(R.id.time_view);


        mTimer = new CountDownTimer(mDuration, 1000) {

            public void onTick(long millisUntilFinished) {

                mTimeView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Toast answerToast = Toast.makeText(getActivity(), "Time Up", Toast.LENGTH_LONG);
                answerToast.show();
//                int timeTaken = (int) (mEndTime - mStartTime);
                Intent i = new Intent(getActivity(),RegionActivity.class);
                i.putExtra(RegionChooserFragment.REGION_FOR_TRIPS, mRegion);
                i.putExtra(TRIP_PASSED, true);
                i.putExtra(TRIP_SCORE, mNumCompleted);
                getActivity().setResult(Activity.RESULT_OK, i);
                getActivity().finish();
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
                String solutionString = mTrip.getRoute(i).get(j).toString();
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

    public class TripArrayAdapter extends ArrayAdapter<Country>{
        private ArrayAdapter<String> mCountryAdapter;

        public TripArrayAdapter(Context context, int resource, List<Country> objects) {
            super(context, resource, objects);
            TripCreator tripCreator =  TripCreator.get();
            mCountryAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, tripCreator.getCountryNames(mRegion));
        }


        private void checkAnswer(){
            ArrayList<String> answers = new ArrayList<String>();
            for(int i=0;i<getCount();i++){
                TextView textView = (TextView) mList.getChildAt(i);
                String answerString = textView.getText().toString();
                answers.add(answerString);
            }

            if(answerPasses(answers)){
                mNumCompleted++;
                mCountView.setText("Completed:" + mNumCompleted);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // this is happening  a lot and we are adding many
            View out;
            if(position == getCount()-1 || position == 0 ) {
                out = super.getView(position, convertView, parent);
            }
            else {
                AutoCompleteTextView view = new AutoCompleteTextView(getContext());
                view.setAdapter(mCountryAdapter);

                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        if (position < getCount()-2) {//move to next answer box if there is one
                            mList.getChildAt(position + 1).requestFocus();
                        }
                        checkAnswer();
                    }
                });

                if(position == 1) {
                    view.requestFocus();
                }

                out = view;
            }
            return out;

        }
    }

}

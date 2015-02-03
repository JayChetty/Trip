package com.jaypaulchetty.trip;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RegionFragment extends Fragment {
    public static final String REGION_BEST_TIME = "com.jaypaulchetty.region.region_best_time";
    public static final String TRIP_LENGTH = "com.jaypaulchetty.region.trip_length";
    private static final String TAG = "RegionFragment";
    private String mRegion;
    private TextView mRegionView,mBestScoreView, mTargetScoreView;
    private static final int REQUEST_PASSED = 1;
    private RegionScores mRegionScores;
    private int mTripLength = 1;
    private RadioButton mLevel1Button,mLevel2Button,mLevel3Button;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Intent intent = getActivity().getIntent();
        mRegionScores = RegionScores.get(getActivity());
        mRegion = intent.getStringExtra(RegionChooserFragment.REGION_FOR_TRIPS);
        getActivity().getActionBar().setTitle(mRegion);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.region,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_item_show_map:
                Intent i = new Intent(getActivity(), RegionMapActivity.class);
                i.putExtra(RegionChooserFragment.REGION_FOR_TRIPS, mRegion);
                startActivity(i);
                Log.d(TAG, "Showing Map");
                return true;
            case R.id.menu_item_start_trip:
                Intent iTrip = new Intent(getActivity(), TripActivity.class);
                iTrip.putExtra(RegionChooserFragment.REGION_FOR_TRIPS, mRegion);
                iTrip.putExtra(RegionFragment.TRIP_LENGTH, (mTripLength+2));
                Log.d(TAG, "Length is" + mTripLength);
                startActivityForResult(iTrip, REQUEST_PASSED);
                Log.d(TAG, "Starting Trip");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_region, container, false);

        mBestScoreView = (TextView) v.findViewById(R.id.best_score_view);
        displayBestScore();

        mTargetScoreView = (TextView) v.findViewById(R.id.target_score_view);
        mTargetScoreView.setText("Target: " + mRegionScores.getPassMark() + "   Merit: " +  mRegionScores.getMeritMark() + "   Distinction: " +mRegionScores.getDistinctionMark());


        mLevel1Button = (RadioButton) v.findViewById(R.id.radio_level_1);
        mLevel2Button = (RadioButton) v.findViewById(R.id.radio_level_2);
        mLevel3Button = (RadioButton) v.findViewById(R.id.radio_level_3);
//        b.setEnabled(false);

        RadioGroup rg = (RadioGroup) v.findViewById(R.id.radio_level_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radio_level_1:
                        mTripLength = 1;
                        break;
                    case R.id.radio_level_2:
                        mTripLength = 2;
                        break;
                    case R.id.radio_level_3:
                        mTripLength = 3;
                        break;
                }
                displayBestScore();
            }
        });

        disableLevels(true);



        return v;
    }

    private void disableLevels(boolean goToHighest){
        mLevel1Button.setEnabled(true);
        mLevel2Button.setEnabled(true);
        mLevel3Button.setEnabled(true);
        if(mRegionScores.getGrade(mRegion, 1) == 0) {//not passed any
            mLevel2Button.setEnabled(false);
            mLevel3Button.setEnabled(false);
            if(goToHighest){
                mLevel1Button.setChecked(true);
            }
        }

        else if(mRegionScores.getGrade(mRegion, 2) == 0){//passed 1
            mLevel3Button.setEnabled(false);
            if(goToHighest){
                mLevel2Button.setChecked(true);
            }
        }
        else{//passed 2
            if(goToHighest){
                mLevel3Button.setChecked(true);
            }
        }

    }


    private void displayBestScore(){
        long bestScore = (long) mRegionScores.getScore(mRegion, mTripLength);
        Log.d(TAG, "displaing best scre" + bestScore);
        mBestScoreView.setText("");
        String out = "";
        if (bestScore > 0) {
            out = "Best Score: " + bestScore;
        }
        else{
            out = "Not Completed Yet";
        }
        mBestScoreView.setText(out);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PASSED) {
            Log.d(TAG, "Got the result yo");
            if(resultCode == Activity.RESULT_OK) {
                Boolean passed = data.getBooleanExtra(TripFragment.TRIP_PASSED, false);
                if (passed) {
                    Log.d(TAG, "Trip was passed yo");
                    int tripScore = data.getIntExtra(TripFragment.TRIP_SCORE, 0);
                    Log.d(TAG, "Trip taken was" + tripScore);
                    int currentBestScore = mRegionScores.getScore(mRegion, mTripLength);
                    if(currentBestScore <= 0 || tripScore > currentBestScore) {
                        mRegionScores.setScore(mRegion, mTripLength, tripScore);
                        displayBestScore();
                        disableLevels(false);
                    }
                } else {
                    Log.d(TAG, "Trip failed");
                }
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        RegionScores.get(getActivity()).saveScores();
    }


}

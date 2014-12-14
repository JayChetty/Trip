package com.jaypaulchetty.trip;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RegionFragment extends Fragment {
    public static final String REGION_BEST_TIME = "com.jaypaulchetty.trip.region_best_time";
    public static final String TRIP_LENGTH = "com.jaypaulchetty.trip.trip_length";
    private static final String TAG = "RegionFragment";
    private String mRegion;
    private TextView mRegionView,mBestTimeView;
    private Button mStartButton, mMapButton;
    private Spinner mLengthSpinner;
    private static final int REQUEST_PASSED = 1;
    private RegionTimes mRegionTimes;
    private int mTripLength = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mRegionTimes = RegionTimes.get(getActivity());
        mRegion = intent.getStringExtra(RegionChooserFragment.REGION_FOR_TRIPS);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_region, container, false);
        mRegionView = (TextView) v.findViewById(R.id.region_view);
        mRegionView.setText(mRegion);
        mBestTimeView = (TextView) v.findViewById(R.id.best_time_view);
        displayBestTime();
        mStartButton = (Button) v.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked");
                Intent i = new Intent(getActivity(), TripActivity.class);
                i.putExtra(RegionChooserFragment.REGION_FOR_TRIPS, mRegion);
                i.putExtra(RegionFragment.TRIP_LENGTH, (mTripLength+2));
                Log.d(TAG, "Length is" + mTripLength);
                startActivityForResult(i, REQUEST_PASSED);
            }
        });
        mMapButton = (Button) v.findViewById(R.id.map_button);
        mMapButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.d(TAG, "Clicked Map");
                Intent i = new Intent(getActivity(), RegionMapActivity.class);
                i.putExtra(RegionChooserFragment.REGION_FOR_TRIPS, mRegion);

                startActivity(i);
            }
        });

        mLengthSpinner  = (Spinner) v.findViewById(R.id.length_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.length_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLengthSpinner.setAdapter(adapter);
        mLengthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTripLength = Integer.parseInt((mLengthSpinner.getItemAtPosition(position).toString()));
                displayBestTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private void displayBestTime(){
        long bestTime = (long) mRegionTimes.getTime(mRegion, mTripLength);
        Log.d(TAG, "displaing best time" + bestTime);
        mBestTimeView.setText("");
        String out = "";
        if (bestTime > 0) {
            out = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(bestTime),
                    TimeUnit.MILLISECONDS.toSeconds(bestTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(bestTime))
            );
        }
        else{
            out = "Not Completed Yet";
        }
        mBestTimeView.setText(out);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PASSED) {
            Log.d(TAG, "Got the result yo");
            if(resultCode == Activity.RESULT_OK) {
                Boolean passed = data.getBooleanExtra(TripFragment.TRIP_PASSED, false);
                if (passed) {
                    Log.d(TAG, "Trip was passed yo");
                    int tripTime = data.getIntExtra(TripFragment.TRIP_TIME, 0);
                    Log.d(TAG, "Trip taken was" + tripTime);
                    int currentBestTime = mRegionTimes.getTime(mRegion, mTripLength);
                    if(currentBestTime < 0 || tripTime < currentBestTime) {
                        mRegionTimes.setTime(mRegion, mTripLength, tripTime);
                        displayBestTime();
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
        RegionTimes.get(getActivity()).saveTimes();
    }


}

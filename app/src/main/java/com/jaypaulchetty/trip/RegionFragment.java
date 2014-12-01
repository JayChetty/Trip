package com.jaypaulchetty.trip;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RegionFragment extends Fragment {
    public static final String REGION_BEST_TIME = "com.jaypaulchetty.trip.region_best_time";
    private static final String TAG = "RegionFragment";
    private String mRegion;
    private TextView mRegionView;
    private Button mStartButton;
    private TextView mBestTimeView;
    private long mBestTime;
    private static final int REQUEST_PASSED = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mRegion = intent.getStringExtra(RegionChooserFragment.REGION_FOR_TRIPS);
        if (intent.hasExtra(REGION_BEST_TIME)) {
            mBestTime = getActivity().getIntent().getIntExtra(REGION_BEST_TIME, 1);
        } else{
            mBestTime = 9999;
        }
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
                startActivityForResult(i, REQUEST_PASSED);
            }
        });

        return v;
    }

    private void displayBestTime(){
        mRegionView.setText("");
        String out = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(mBestTime),
                TimeUnit.MILLISECONDS.toSeconds(mBestTime)
        );
        mRegionView.setText(out);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PASSED) {
            Log.d(TAG, "Got the result yo");
            Boolean passed = data.getBooleanExtra(TripFragment.TRIP_PASSED, false);
            if(passed){
                Log.d(TAG, "Trip was passed yo");
                int tripTime = data.getIntExtra(TripFragment.TRIP_TIME, 0);
                Log.d(TAG, "Trip taken was" + tripTime);
                mBestTime = tripTime;
                //need to also save to the time object RegionTimes
                RegionTimes.get().setTime(mRegion, (int) tripTime);
                ;
            }
            else{
                Log.d(TAG, "Trip failed");
            }
        }
    }


}

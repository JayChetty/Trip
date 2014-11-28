package com.jaypaulchetty.trip;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RegionFragment extends Fragment {
    public static final String REGION_BEST_TIME = "com.jaypaulchetty.trip.region_best_time";
    private String mRegion;
    private TextView mRegionView;
    private Button mStartButton;
    private TextView mBestTimeView;
    private int mBestTime;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mRegion = getActivity().getIntent().getStringExtra(RegionChooserFragment.REGION_FOR_TRIPS);
        mBestTime = getActivity().getIntent().getIntExtra(REGION_BEST_TIME, 1);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_region, container, false);
        mRegionView = (TextView) v.findViewById(R.id.region_view);
        mRegionView.setText(mRegion);
        mBestTimeView = (TextView) v.findViewById(R.id.best_time_view);
        mBestTimeView.setText("Best Time: " + mBestTime);

        return v;
    }


}

package com.jaypaulchetty.trip;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class RegionMapFragment extends Fragment {
    TouchImageView mImage;
    String mRegion;

    public RegionMapFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mRegion = intent.getStringExtra(RegionChooserFragment.REGION_FOR_TRIPS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment]
        View r = inflater.inflate(R.layout.fragment_region_map, container, false);
        mImage = (TouchImageView) r.findViewById(R.id.map_view);
//        Drawable map = getResources().getDrawable(R.drawable.world);
//        Bitmap bImage = BitmapFactory.decodeResource(this.getResources(),R.drawable.world);
//        mImage.setImageBitmap(bImage);
        switch (mRegion) {
            case "Europe": mImage.setImageResource(R.drawable.europe);break;
            case "Africa": mImage.setImageResource(R.drawable.africa);break;
            case "Americas": mImage.setImageResource(R.drawable.americas);break;
            case "Asia": mImage.setImageResource(R.drawable.asia);break;
            case "World": mImage.setImageResource(R.drawable.asia);break;
        }
        return r;

    }


}

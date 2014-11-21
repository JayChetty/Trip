package com.jaypaulchetty.trip;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TripFragment extends ListFragment {
    private Trip mTrip;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mTrip = TripCreator.get(getActivity()).createTrip();

        TripArrayAdapter adapter = new TripArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,mTrip.getRoute(0));
        setListAdapter(adapter);

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
            case R.id.menu_item_check_route:
                Log.d("TripFragment","item clicked");
                TripArrayAdapter adapter = (TripArrayAdapter)getListView().getAdapter();
                ArrayList<EditText> answerViews = adapter.getAnswerViews();
                int length = answerViews.size();
                ArrayList<String> answers = new ArrayList<String>();
                //put together string of arrays
                for(int j=0; j<length; j++){
                    String answerString = answerViews.get(j).getText().toString();
                    answers.add(answerString);
                }
                if(answerPasses(answers)){
                    Log.d("TripFragment","CORRECT");
                    Toast toast = Toast.makeText(this.getActivity(), "You did it!", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Log.d("TripFragment","FALSE");
                    Toast toast = Toast.makeText(this.getActivity(), "Sorry not correct!", Toast.LENGTH_LONG);
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
        private ArrayList<EditText> answerViews;

        public ArrayList<EditText> getAnswerViews() {
            return answerViews;
        }

        public TripArrayAdapter(Context context, int resource, List<Country> objects) {
            super(context, resource, objects);
            answerViews = new ArrayList<EditText>();
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
                answerViews.add(view);
                out = view;
                view = null;
            }
            return out;

        }
    }

}

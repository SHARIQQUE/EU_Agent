package adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import agent.activities.R;


/**
 * Created by AdityaBugalia on 24/10/16.
 */

public class CountryFlagAdapter extends BaseAdapter {

    private Activity activity;

    public Resources res;
    String[] countryNameArray;
    LayoutInflater inflater;

    HashMap<String, Integer> countryFlagsList;

    /*************
     * CustomAdapter Constructor
     *****************/
    public CountryFlagAdapter(String[] countryNameArray, Resources res, LayoutInflater inflater) {
        this.countryNameArray = countryNameArray;
        this.inflater = inflater;
        this.res = res;
        this.inflater = inflater;
        populateCountryFlag();
    }

    void populateCountryFlag() {
        countryFlagsList = new HashMap<String, Integer>();
        countryFlagsList.put("cameroon", R.drawable.cameroon_flag);
        countryFlagsList.put("gabon", R.drawable.gabon_flag);
        countryFlagsList.put("congo", R.drawable.congo_flag);
        countryFlagsList.put("chad", R.drawable.chad_flag);
        countryFlagsList.put("central african republic",R.drawable.centralafricanrepublic_flag);
        countryFlagsList.put("democratic republic of congo",R.drawable.drcongo_flag);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return countryNameArray.length;
    }

    @Override
    public Object getItem(int i) {
        return countryNameArray[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.countryflag_item_structure, parent, false);

        /***** Get each Model object from Arraylist ********/

        TextView countryName = (TextView) row.findViewById(R.id.countryNameTextView_CountryFlag_item_structure);
        ImageView countryLogo = (ImageView) row.findViewById(R.id.imageView_CountryFlag_item_structure);
        try {
            if (position == 0) {

                // Default selected Spinner item
                //countryName.setText("Please select Country");
                countryName.setText(res.getString(R.string.pleaseSelectCountry));
                countryLogo.setVisibility(View.GONE);

            } else {
                // Set values for spinner each row
                countryName.setText(countryNameArray[position]);
                countryLogo.setImageResource(countryFlagsList.get(countryNameArray[position].toLowerCase()));
            }
        } catch (Exception e) {



        }
        return row;
    }
}
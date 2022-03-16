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

public class CountryFlagAdapterIDDocumnetType extends BaseAdapter {

    private Activity activity;

    public Resources res;
    String[] countryNameArray;
    LayoutInflater inflater;
  String selectionType;
    HashMap<String, Integer> countryFlagsList;

    /*************
     * CustomAdapter Constructor
     *****************/
    public CountryFlagAdapterIDDocumnetType(String selectionType,String[] countryNameArray, Resources res, LayoutInflater inflater) {
        this.selectionType = selectionType;
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
        countryFlagsList.put("tchad", R.drawable.chad_flag); // TCHAD
        countryFlagsList.put("republique centrafricaine",R.drawable.centralafricanrepublic_flag);  // REPUBLIQUE CENTRAFRICAINE
        countryFlagsList.put("republique democratique du congo",R.drawable.drcongo_flag);  // REPUBLIQUE DEMOCRATIQUE DU CONGO



        countryFlagsList.put("cote d'ivoire",R.drawable.cote3); // COTE DIVOIRE
        countryFlagsList.put("benin",R.drawable.beninflag); //benin
        countryFlagsList.put("bukina faso",R.drawable.burkina5); // BURKINA FASO
        countryFlagsList.put("mali",R.drawable.malitemp); // BURKINA FASO
        countryFlagsList.put("niger",R.drawable.niger7); // BURKINA FASO
        countryFlagsList.put("togo",R.drawable.togo9); // BURKINA FASO
        countryFlagsList.put("senegal",R.drawable.senegalicon); // BURKINA FASO
        countryFlagsList.put("guinee conakry",R.drawable.guineaflag); // BURKINA FASO
        countryFlagsList.put("france",R.drawable.franch2);
        countryFlagsList.put("suisse",R.drawable.switzerland2); // SUISSE
        countryFlagsList.put("canada",R.drawable.canada2); // CANADA

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
        View row = inflater.inflate(R.layout.countryflag_item_iddocumne_ttype_structure, parent, false);

        /***** Get each Model object from Arraylist ********/

        TextView countryName = (TextView) row.findViewById(R.id.countryNameTextView_CountryFlag_item_structure);
        ImageView countryLogo = (ImageView) row.findViewById(R.id.imageView_CountryFlag_item_structure);
        try {
            if (position == 0) {

                // Default selected Spinner item
                //countryName.setText("Please select Country");


                if(selectionType.equalsIgnoreCase("Country"))
                {
                    countryName.setText(res.getString(R.string.country));

                }
                else if(selectionType.equalsIgnoreCase("destinationCountry"))
                {
                    countryName.setText(res.getString(R.string.destinationCountry_cashtocash));

                }
                else if(selectionType.equalsIgnoreCase("nationallity"))
                {
                    countryName.setText(res.getString(R.string.nationality));

                }


                else {
                    countryName.setText(res.getString(R.string.id_documentCountryOfissue_cashtocash));

                }

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
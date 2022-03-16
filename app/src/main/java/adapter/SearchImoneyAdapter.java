package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import agent.activities.R;
import callback.CallbackFromAdapterImoney;


public class SearchImoneyAdapter extends BaseAdapter {
    Context context;
    SharedPreferences sharedpreferencesforCheckbox;
    ArrayList<String> dList;
    String TxID = "", amt = "", txType = "", stat = "";
    boolean isOtherAccount;
    Integer selected_position = -1;
    CheckBox checkBox;
    String selectCheckBox;
    String sourceNumberString;

    String referenceNumberString;
    String dateString;
    String amountString;
    String destinationNamseString;
    String currencyString;
    String destinationNumber;
    String sourceNameString;


    public SearchImoneyAdapter(Context context, ArrayList<String> list, boolean isOtherAccount) {

        this.context = context;
        this.dList = list;
        this.isOtherAccount = isOtherAccount;
        Log.e("", "" + dList.toString());
    }

    @Override
    public int getCount() {
        return dList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return dList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_imoney_listview_item, null);

        }


        TextView transactionId_textview, referenceNumber_textview, currency_TxtViewTitle, amtTxtView, sourceNumber_textview, sourceNameTxtViewTitle, destinationNameTxtView, destinationMobileNoTxtViewTitle, dateTxtView;
        referenceNumber_textview = (TextView) convertView.findViewById(R.id.referenceNumber_textview);
        transactionId_textview = (TextView) convertView.findViewById(R.id.transactionId_textview);
        amtTxtView = (TextView) convertView.findViewById(R.id.amountTxtView);
        sourceNumber_textview = (TextView) convertView.findViewById(R.id.sourceNumber_textview);
        destinationNameTxtView = (TextView) convertView.findViewById(R.id.destinationNameTxtView);
        dateTxtView = (TextView) convertView.findViewById(R.id.dateTxtView);
        sourceNameTxtViewTitle = (TextView) convertView.findViewById(R.id.sourceNameTxtViewTitle);
        destinationMobileNoTxtViewTitle = (TextView) convertView.findViewById(R.id.destinationMobileNoTxtViewTitle);
        currency_TxtViewTitle = (TextView) convertView.findViewById(R.id.currency_TxtViewTitle);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);



        final String[] data = dList.get(position).split("\\|");


        referenceNumber_textview.setText(data[1]);
        amtTxtView.setText(data[7]);
        sourceNumber_textview.setText(data[11]);  // also change parametr Request Receive cas

        dateTxtView.setText(data[5]);
        sourceNameTxtViewTitle.setText(data[6]);
        destinationMobileNoTxtViewTitle.setText(data[4]);
        destinationNameTxtView.setText(data[9]);
        currency_TxtViewTitle.setText(data[10]);
        //   transactionId_textview.setText("null");


        referenceNumberString = data[1];
        dateString = data[5];
        amountString = data[7];
        destinationNamseString = data[9];
        sourceNameString = data[6];
        currencyString = data[10];
        destinationNumber = data[4];
        sourceNumberString = data[11];



     //   selectCheckBox = "checkboxNotSelect";
      //  System.out.println(selectCheckBox);

       // ((CallbackFromAdapterImoney) context).imoneyCallbackFromAdapter(referenceNumberString, dateString, amountString, destinationNamseString, currencyString, destinationNumber, sourceNameString,"source nu demo parameter", selectCheckBox);


        checkBox.setChecked(position == selected_position);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    selected_position = position;
                    System.out.println(selected_position);


                    String selectCheckBox = "checkboxSelect";
                    ((CallbackFromAdapterImoney) context).imoneyCallbackFromAdapter(referenceNumberString, dateString, amountString, destinationNamseString, currencyString, destinationNumber, sourceNameString,sourceNumberString, selectCheckBox);


                } else {

                    selected_position = -1;
                    System.out.println(selected_position);
                    selectCheckBox = "checkboxNotSelect";
                    ((CallbackFromAdapterImoney) context).imoneyCallbackFromAdapter(referenceNumberString, dateString, amountString, destinationNamseString, currencyString, destinationNumber, sourceNameString,sourceNumberString, selectCheckBox);


                }

               // System.out.println(selectCheckBox);
                notifyDataSetChanged();
            }
        });




        return convertView;
    }


}
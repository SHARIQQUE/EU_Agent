/*
package billpay;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import commonutilities.ComponentMd5SharedPre;
import PrepaidBill;
import franchise.activities.R;


*/
/**
 * Created by AdityaBugalia on 12/06/17.
 *//*


public class BillPayIntroduction extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

    Spinner billPaymentTypeSpinner;


    String  agentName = "", agentCode = "";

    ComponentMd5SharedPre mComponentInfo;
    Toolbar mToolbar;
    Button payBill_Btn, prepaid_Btn;
    ArrayAdapter spinnerArrayAdapter;
    int layoutLevel = 0;
    private ProgressDialog mDialog;
    String[] billTypeArray;

Intent i;
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
                // validateDetails();
                //validateMpin();
                validateDetails_payBill_Btn();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billpayintroduction);


        mComponentInfo=(ComponentMd5SharedPre)getApplicationContext();
        String languageToUse=mComponentInfo.getmSharedPreferences().getString("languageToUse","");
        if(languageToUse.trim().length()==0){
            languageToUse="en";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        mToolbar = (Toolbar) findViewById(R.id.tool_bar_BillPayment);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");


        mToolbar.setTitle(getString(R.string.billpayment_title));
        mToolbar.setSubtitle("" + agentName);

        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        try {

            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {

            Log.e("", "" + e.toString());
        }



        payBill_Btn = (Button) findViewById(R.id.nextButton_Billpayment);

        prepaid_Btn = (Button) findViewById(R.id.previousButton_MoneyTransfer);

        payBill_Btn.setOnClickListener(this);
        prepaid_Btn.setOnClickListener(this);

        payBill_Btn.setVisibility(View.VISIBLE);
        billTypeArray=new String[3];
        billTypeArray[0]="Please Select Bill Payment Type";
        billTypeArray[1]="Bill Payment";

        billTypeArray[2]="Prepaid Electricity Payment";



        billPaymentTypeSpinner = (Spinner) findViewById(R.id.spinner_BillType_BillPayment);

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billTypeArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        billPaymentTypeSpinner.setAdapter(spinnerArrayAdapter);


    }

    private boolean validateDetails() {
        boolean ret = false;

       if(billPaymentTypeSpinner.getSelectedItemPosition()==1){
           i=new Intent(BillPayIntroduction.this, BillPayActivity.class);
           ret=true;
       }else if(billPaymentTypeSpinner.getSelectedItemPosition()==2){

           i=new Intent(BillPayIntroduction.this, PrepaidBill.class);
           ret=true;

       }else {
           Toast.makeText(BillPayIntroduction.this,"Please Select Bill Payment Type", Toast.LENGTH_LONG).show();

       }

        return ret;
    }



    public void validateDetails_payBill_Btn(){


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_Billpayment:

                i=new Intent(BillPayIntroduction.this, BillPayActivity.class);
                   proceedBillPay();


                break;
            case R.id.previousButton_MoneyTransfer:

                i=new Intent(BillPayIntroduction.this, PrepaidBill.class);
                proceedBillPay();


                break;

        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(BillPayIntroduction.this);
        mDialog.setMessage(message);
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(false);
        mDialog.show();
    }

    private void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    private void proceedBillPay() {
startActivity(i);
        BillPayIntroduction.this.finish();


    }
















    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.spinner_BillType_BillPayment:

                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


        switch (adapterView.getId()) {
            case R.id.spinner_BillType_BillPayment:

                break;


        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            BillPayIntroduction.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }


}
*/

package agent.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptPrintBill;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class PrintBillPay extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
                validateDetails_NextButton();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public AutoCompleteTextView transactionNumber_EditText, mpinEditText, customerMobileNo_EditText;
    Spinner operationTypeSpinner, billerSpinner, accountNameSpinner;
    Button nextButton;
    Toolbar mToolbar;
    Spinner spinnerCountry;
    private String[] countryMobileNoLengthArray, countryArray;

    String spinnerCountryString, transferBasisString, countrySelectionString;
    ComponentMd5SharedPre mComponentInfo;
    boolean isBillPay = false;
    LinearLayout input_LL, review_LL;
    ListView statementLsitView;
    ProgressDialog mDialog;

    Dialog successDialog;
    String agentName, agentCode, customerMobileNoString, operationCodeString, operationNameString, transactionNumberString, mpinString, accountTypeCodeString, billerCodeString, destinationString = "";
    private String[] countryCodeArray, countryPrefixArray, operationCodeArray, billerArray, accountCodeArray, accountNameArray;

    int iLevel = 99;
    boolean isMiniStmtMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse=mComponentInfo.getmSharedPreferences().getString("languageToUse","");
        if(languageToUse.trim().length()==0){
            languageToUse="fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.print_billpay);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_prints);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
       // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.bill));
        mToolbar.setSubtitle("" + agentName);
        //  mToolbar.setSubtitle(agentcode);
        // mToolbar.setSubtitle("Please provide details to proceed further");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        try {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {

            Log.e("", "" + e.toString());
        }

        transactionNumber_EditText = (AutoCompleteTextView) findViewById(R.id.transactionNumber_EditText);
        customerMobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.customerMobileNo_EditText);


        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_Tariff);
        mpinEditText.setOnEditorActionListener(this);

        mpinEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });



        // Looper.prepare();


        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {
            PrintBillPay.this.finish();
        }


        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);
        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        operationTypeSpinner = (Spinner) findViewById(R.id.spinner_OperationType_Tariff);
        billerSpinner = (Spinner) findViewById(R.id.spinner_Biller_Tariff);
        accountNameSpinner = (Spinner) findViewById(R.id.spinner_AccountType_Tariff);

        nextButton = (Button) findViewById(R.id.nextButton_Tariff);
        nextButton.setOnClickListener(this);

        input_LL = (LinearLayout) findViewById(R.id.input_Recipient_LL_Tariff);
        review_LL = (LinearLayout) findViewById(R.id.review_LL_Tariff);
        billerArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billerSpinner.setAdapter(spinnerArrayAdapter);

        operationCodeArray = getResources().getStringArray(R.array.TxnType);

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 5) {
            // Looper.prepare();
            String[] data = bankAccounts.split("\\;");

            accountNameArray = new String[data.length + 1];
            accountNameArray[0] = getString(R.string.PleaseSelectAccountType);

            accountCodeArray = new String[data.length + 1];
            accountCodeArray[0] = getString(R.string.PleaseSelectAccountType);

            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    accountNameArray[i + 1] = tData[1];
                    accountCodeArray[i + 1] = tData[4];
                }
            }
        } else {
            accountNameArray = new String[1];
            accountNameArray[0] = getString(R.string.payeraccount);
            accountCodeArray = new String[1];
            accountCodeArray[0] = getString(R.string.payeraccount);
        }
      /*  spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountNameArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountNameSpinner.setAdapter(spinnerArrayAdapter);*/

        billerSpinner.setVisibility(View.GONE);
        operationTypeSpinner.setOnItemSelectedListener(this);
    }

    private boolean validateDetails() {
        boolean ret = false;
        int transferBasisposition = 1;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

          //  if (accountNameSpinner.getSelectedItemPosition() > 0) {

                transactionNumberString = transactionNumber_EditText.getText().toString().trim();
                if (transactionNumberString.length() >= 8) {

                    if (transferBasisposition == 1) {
                        transferBasisString = "Mobile Number";
                        lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                        errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashIn), lengthToCheck + 1 + "");

                    } else {
                        transferBasisString = "Recipient Name";
                        lengthToCheck = 2;
                        errorMsgToDisplay = getString(R.string.receipentname);
                    }
                    customerMobileNoString = customerMobileNo_EditText.getText().toString().trim();

                    if (customerMobileNoString.length() > lengthToCheck) {
                        if (transferBasisposition == 1) {
                            if (customerMobileNoString.length() == ++lengthToCheck) {
                                customerMobileNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + customerMobileNoString;
                            } else {
                                Toast.makeText(PrintBillPay.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                return false;
                            }
                        }

                        mpinString = mpinEditText.getText().toString().trim();
                        if (mpinString.length() == 4) {

                            operationCodeString = operationCodeArray[operationTypeSpinner.getSelectedItemPosition()];
                            operationNameString = operationTypeSpinner.getSelectedItem().toString();
                            billerCodeString = billerSpinner.getSelectedItem().toString();
                       //     accountTypeCodeString = accountCodeArray[accountNameSpinner.getSelectedItemPosition()];

                            ret = true;

                        } else {
                            mpinEditText.requestFocus();
                            Toast.makeText(PrintBillPay.this, getString(R.string.pleaseEnterMpin), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(PrintBillPay.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

                    }

                } else {
                    transactionNumber_EditText.requestFocus();
                    Toast.makeText(PrintBillPay.this, getString(R.string.transactionNumber), Toast.LENGTH_SHORT).show();
                }
         /*   } else {
                accountNameSpinner.requestFocus();
                Toast.makeText(PrintBillPay.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
            }*/
        } else {
            Toast.makeText(PrintBillPay.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }


        return ret;
    }

    public void validateDetails_NextButton() {
        if (isMiniStmtMode) {
            isMiniStmtMode = false;
            mpinEditText.setText("");
            nextButton.setText("Next");
            input_LL.setVisibility(View.VISIBLE);
            review_LL.setVisibility(View.GONE);
        }

        if (validateDetails()) {
            proceedbillPrint();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_Tariff:
                validateDetails_NextButton();
                break;
        }
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                customerMobileNo_EditText.setText("");
                customerMobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));
                // subscriber_MobileNo_EditText.setFilters(null);
                customerMobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] digitsfilters = new InputFilter[2];
                digitsfilters[0] = new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {
                        if (end > start) {

                            char[] acceptedChars = new char[]{'0', '1', '2', '3', '4', '5',
                                    '6', '7', '8', '9'};

                            for (int index = start; index < end; index++) {
                                if (new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                                    return source;
                                } else {
                                    return "";
                                }
                            }
                        }
                        return null;
                    }


                };
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]));
                customerMobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNoAcctoCashIn), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                customerMobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                customerMobileNo_EditText.setFilters(digitsfilters);
                customerMobileNo_EditText.setText("");


            } else if (i == 2) {
                customerMobileNo_EditText.setText("");
                customerMobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));
                // subscriber_MobileNo_EditText.setFilters(null);
                customerMobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] digitsfilters = new InputFilter[2];
                digitsfilters[0] = new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {
                        if (end > start) {

                            char[] acceptedChars = new char[]{'0', '1', '2', '3', '4', '5',
                                    '6', '7', '8', '9'};

                            for (int index = start; index < end; index++) {
                                if (new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                                    return source;
                                } else {
                                    return "";
                                }
                            }
                        }
                        return null;
                    }


                };
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]));
                customerMobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNoAcctoCashIn), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                customerMobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                customerMobileNo_EditText.setFilters(digitsfilters);
                customerMobileNo_EditText.setText("");

            }
        } else {
            Toast.makeText(PrintBillPay.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

    }

    void proceedbillPrint() {
        if (new InternetCheck().isConnected(PrintBillPay.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateBillPrint();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

         //   callApi("billpayPrint",requestData,152);

            new ServerTask(mComponentInfo, PrintBillPay.this, mHandler, requestData, "billpayPrint", 152).start();

        } else {
            Toast.makeText(PrintBillPay.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateBillPrint() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

           /* {"agentcode":"237000271011","pin":"5FFF68DE986617288D4BB5CA37800290","source":"237000271015",
              "pintype":"MPIN","vendorcode":"MICR","clienttype":"GPRS","transrefno":"6572009"}
           */

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", customerMobileNoString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transrefno", transactionNumberString);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(PrintBillPay.this);
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

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());
            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(PrintBillPay.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(PrintBillPay.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(PrintBillPay.this, mComponentInfo, PrintBillPay.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PrintBillPay.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.bill));
        //    String[] temp= data.split("\\|");
        //   builder.setMessage(String.format(getString(R.string.tariffResponseSuccess),operationNameString,temp[0], temp[1]) );

        builder.setMessage(getString(R.string.billPrintSucessReceipt) + " \n " + " \n " + data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                PrintBillPay.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {
                    String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");

                    if (new InternetCheck().isConnected(PrintBillPay.this)) {
                        showProgressDialog(getString(R.string.pleasewait));

                     //   callApi("billpayPrint",requestData,152);

                        new ServerTask(mComponentInfo, PrintBillPay.this, mHandler, requestData, "billpayPrint", 152).start();
                    } else {
                        Toast.makeText(PrintBillPay.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }

                } else {
                }
                break;
        }
    }
    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(PrintBillPay.this,mComponentInfo,PrintBillPay.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(PrintBillPay.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(PrintBillPay.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 152) {
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "TARIFF").commit();
                    Intent i = new Intent(PrintBillPay.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            }
        } else {
            hideProgressDialog();
            Toast.makeText(PrintBillPay.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("spinnerCountryString", spinnerCountryString);
       // bundle.putString("tariffAmountFee", tariffAmountFee);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("spinnerCountryString", spinnerCountryString).commit();
      //  mComponentInfo.getmSharedPreferences().edit().putString("tariffAmountFee", tariffAmountFee).commit();

        Intent intent = new Intent(PrintBillPay.this, SucessReceiptPrintBill.class);
        intent.putExtra("data", data);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
       // intent.putExtra("tariffAmountFee", tariffAmountFee);


        startActivity(intent);
        PrintBillPay.this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            PrintBillPay.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_OperationType_Tariff:

                if (i == 3) {
                    isBillPay = true;
                    billerSpinner.setVisibility(View.VISIBLE);
                } else {
                    isBillPay = false;
                    billerSpinner.setVisibility(View.GONE);
                }
                break;

            case R.id.spinnerCountry:
                setInputType(i);
                break;
        }
    }

    private int getCountrySelection() {
        int ret = 0;
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                ret = i;
            }
        }
        return ret;
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        switch (adapterView.getId()) {

            case R.id.spinner_OperationType_Tariff:

                operationTypeSpinner.setSelection(0);
                billerSpinner.setVisibility(View.GONE);
                break;
        }

    }
}



package agent.cashout;


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
import android.widget.ScrollView;
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
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptCashToCashWithdrawal;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class CashToCashTransferWitthdrawal extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    int slectKeyboard = 0;

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();


                if (slectKeyboard == 0) {
                    validateDetails_FirstPage();
                } else if (slectKeyboard == 1) {
                    validationDetails_secondPage();
                } else if (slectKeyboard == 2) {
                    validationDetails_reviewPage();
                }
                else if (slectKeyboard == 3) {
                    validateDetails_otpPage();
                }



            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    String customerName, accountNumber,idType,idNumber,place,date;

    TextView idtype_textView_secondPage,idNumber_textview_secondPage,place_textView_secondpage,dateExpiry_textView_secondpage;

    public AutoCompleteTextView editText_otp, amountEditText_secondpage, transactionNumber_EditText, mpinEditText, customerMobileNo_EditText;
    Spinner operationTypeSpinner, billerSpinner, accountNameSpinner;
    Button nextButton, buttonSubmit_reviewpage, nextbutton_secondpage, buttonSubmit_OTPPage;
    Toolbar mToolbar;
    boolean isServerOperationInProcess;
    TextView amountTextView_reviewpage, tariffAmmount_textview_privewpage, mobileNumber_textView_reviewpage, recipientCountry_TxtView_reviewPage;
    String otpString;
    Spinner spinnerCountry;
    private String[] countryMobileNoLengthArray, countryArray;

    String spinnerCountryString, transferBasisString, countrySelectionString;
    ComponentMd5SharedPre mComponentInfo;
    boolean isBillPay = false;
    LinearLayout input_LL, review_LL;
    int tariffResponse;
    TextView mobileNumber_textView_secondPage, accountNumber_textview_secondPage, customerName_textView_secondpage;

    ListView statementLsitView;
    ProgressDialog mDialog;
    ScrollView scrollview_confirmationPage, scrollview_firstpage, scrollview_secondPage, scrollview_otpPage;

    String tariffAmountFee, amountString;
    Dialog successDialog;
    String agentName, agentCode, customerMobileNoString, operationCodeString, operationNameString, fdAccountString, mpinString, accountTypeCodeString, billerCodeString, destinationString = "";
    private String[] countryCodeArray, countryPrefixArray, operationCodeArray, billerArray, accountCodeArray, accountNameArray;

    int iLevel = 99;
    boolean isMiniStmtMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.cashtocashtransfer_withdrawal);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_prints);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        //countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashfortransfer_withdrawal));
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
        customerMobileNo_EditText.setOnEditorActionListener(this);

        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_Tariff);
        mpinEditText.setOnEditorActionListener(this);
        // Looper.prepare();

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




        amountEditText_secondpage = (AutoCompleteTextView) findViewById(R.id.amountEditText_secondpage);
        amountEditText_secondpage.setOnEditorActionListener(this);

        amountEditText_secondpage.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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



        editText_otp = (AutoCompleteTextView) findViewById(R.id.editText_otp);
        editText_otp.setOnEditorActionListener(this);

        mobileNumber_textView_secondPage = (TextView) findViewById(R.id.mobileNumber_textView_secondPage);
        accountNumber_textview_secondPage = (TextView) findViewById(R.id.accountNumber_textview_secondPage);
        customerName_textView_secondpage = (TextView) findViewById(R.id.customerName_textView_secondpage);


        //  idtype_textView_secondPage,idNumber_textview_secondPage,place_textView_secondpage,idtype_textView_secondPage;

        idtype_textView_secondPage = (TextView) findViewById(R.id.idtype_textView_secondPage);
        idNumber_textview_secondPage = (TextView) findViewById(R.id.idNumber_textview_secondPage);
        place_textView_secondpage = (TextView) findViewById(R.id.place_textView_secondpage);
        dateExpiry_textView_secondpage = (TextView) findViewById(R.id.dateExpiry_textView_secondpage);



        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {
            CashToCashTransferWitthdrawal.this.finish();
        }


        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);
        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);

        scrollview_confirmationPage = (ScrollView) findViewById(R.id.scrollview_confirmationPage);
        scrollview_firstpage = (ScrollView) findViewById(R.id.scrollview_firstpage);
        scrollview_secondPage = (ScrollView) findViewById(R.id.scrollview_secondPage);
        scrollview_otpPage = (ScrollView) findViewById(R.id.scrollview_otpPage);

        buttonSubmit_reviewpage = (Button) findViewById(R.id.buttonSubmit_reviewpage);
        buttonSubmit_reviewpage.setOnClickListener(this);

        buttonSubmit_OTPPage = (Button) findViewById(R.id.buttonSubmit_OTPPage);
        buttonSubmit_OTPPage.setOnClickListener(this);


        mobileNumber_textView_reviewpage = (TextView) findViewById(R.id.mobileNumber_textView_reviewpage);
        amountTextView_reviewpage = (TextView) findViewById(R.id.amountTextView_reviewpage);
        recipientCountry_TxtView_reviewPage = (TextView) findViewById(R.id.recipientCountry_TxtView_reviewPage);
        tariffAmmount_textview_privewpage = (TextView) findViewById(R.id.tariffAmmount_textview_privewpage);


        operationTypeSpinner = (Spinner) findViewById(R.id.spinner_OperationType_Tariff);
        billerSpinner = (Spinner) findViewById(R.id.spinner_Biller_Tariff);
        accountNameSpinner = (Spinner) findViewById(R.id.spinner_AccountType_Tariff);

        nextButton = (Button) findViewById(R.id.nextButton_Tariff);
        nextButton.setOnClickListener(this);


        nextbutton_secondpage = (Button) findViewById(R.id.nextbutton_secondpage);
        nextbutton_secondpage.setOnClickListener(this);


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


    private boolean validateDetails_mobileNumber() {
        boolean ret = false;
        int transferBasisposition = 1;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            //  if (accountNameSpinner.getSelectedItemPosition() > 0) {


            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNo_imoneyWithdrawal), lengthToCheck + 1 + "");

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
                        Toast.makeText(CashToCashTransferWitthdrawal.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }


                operationCodeString = operationCodeArray[operationTypeSpinner.getSelectedItemPosition()];
                operationNameString = operationTypeSpinner.getSelectedItem().toString();
                billerCodeString = billerSpinner.getSelectedItem().toString();
                //     accountTypeCodeString = accountCodeArray[accountNameSpinner.getSelectedItemPosition()];

                ret = true;


            } else {
                Toast.makeText(CashToCashTransferWitthdrawal.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }


         /*   } else {
                accountNameSpinner.requestFocus();
                Toast.makeText(PrintBillPay.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
            }*/
        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }


        return ret;
    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(CashToCashTransferWitthdrawal.this,mComponentInfo, CashToCashTransferWitthdrawal.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(CashToCashTransferWitthdrawal.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(CashToCashTransferWitthdrawal.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
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
                customerMobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNo_imoneyWithdrawal), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
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
                customerMobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNo_imoneyWithdrawal), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                customerMobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                customerMobileNo_EditText.setFilters(digitsfilters);
                customerMobileNo_EditText.setText("");

            }
        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

    }

    void proceedTariffAmount() {
        if (new InternetCheck().isConnected(CashToCashTransferWitthdrawal.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

          //  callApi("getTarifInJSON",requestData,114);
            new ServerTask(mComponentInfo, CashToCashTransferWitthdrawal.this, mHandler, requestData, "getTarifInJSON", 114).start();
        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void proceedSerachApi() {
        if (new InternetCheck().isConnected(CashToCashTransferWitthdrawal.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateSerachData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

        //    callApi("getImoneyInfoAccountJson",requestData,169);
            new ServerTask(mComponentInfo, CashToCashTransferWitthdrawal.this, mHandler, requestData, "getImoneyInfoAccountJson", 169).start();
        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateSerachData() {
        String jsonString = "";

       /* SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);
      */

        //    String pinNeW = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("vendorcode", "MICR");
            countryObj.put("agentcode", customerMobileNoString);  // acording to syam sir agent code is souce code
            countryObj.put("source", customerMobileNoString);
            countryObj.put("clienttype", "GPRS");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private String generateTariffAmmount() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("destination", accountNumber);
            countryObj.put("amount", amountString);
            countryObj.put("transtype", "IMONEYWITHDRAWA");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            //   String fromCity = mComponentInfo.getmSharedPreferences().getString("state", "");

            //   countryObj.put("fromcity", fromCity);   //  change from server
            //   countryObj.put("tocity", toCity);     //  Change from Server
            countryObj.put("comments", "");
            countryObj.put("udv1", accountNumber);
            countryObj.put("accountType", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            //  countryObj.put("destination", nameNumberString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private boolean validateAmount(String input) {
        boolean ret = false;

        try {
            int amt = Integer.parseInt(input);
            if (amt > 0) {
                ret = true;
                amountString = "" + amt;
            }
        } catch (Exception e) {
        }
        return ret;
    }

    public void validateDetails_FirstPage() {
        if (isMiniStmtMode) {
            isMiniStmtMode = false;
            mpinEditText.setText("");
            nextButton.setText("Next");
            input_LL.setVisibility(View.VISIBLE);
            review_LL.setVisibility(View.GONE);
        }

        if (validateDetails_mobileNumber()) {

            slectKeyboard = 1;

            ServerRequestSendSms();
        }
    }

    private boolean validateDetails_otp() {
        boolean ret = false;

        //  if (payerAccountsSpinner.getSelectedItemPosition() > 0) {

        otpString = editText_otp.getText().toString();
        if (otpString.trim().length() > 2) {

            editText_otp.setText(otpString);

            ret = true;


        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.otp_new), Toast.LENGTH_SHORT).show();
        }

        return ret;

    }

    private boolean validateDetailsMpin() {
        boolean ret = false;

        mpinString = mpinEditText.getText().toString();
        if (mpinString.trim().length() == 4) {

            ret = true;

        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.mpinAccountBalance), Toast.LENGTH_SHORT).show();
        }


        return ret;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.nextButton_Tariff:

                validateDetails_FirstPage();

                break;

            case R.id.nextbutton_secondpage:

                validationDetails_secondPage();

                break;

            case R.id.buttonSubmit_reviewpage:

                validationDetails_reviewPage();

                break;

            case R.id.buttonSubmit_OTPPage:

                validateDetails_otpPage();

                break;


        }
    }

    void validateDetails_otpPage() {
        if (validateDetails_otp()) {

            otpVerify();

            // proceedSerachApi();

        }
    }


    void validationDetails_reviewPage() {
        if (validateDetailsMpin()) {
            imoneyWithdrawJson();
        }
    }

    public void validationDetails_secondPage() {
        if (validateDetailsAmount_secondPAge()) {

            scrollview_confirmationPage.setVisibility(View.VISIBLE);


            proceedTariffAmount();

        }
    }

    private boolean validateDetailsAmount_secondPAge() {
        boolean ret = false;

        amountString = amountEditText_secondpage.getText().toString().trim();
        if (amountString.length() > 0 && validateAmount(amountString)) {

            ret = true;

        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.amountNew), Toast.LENGTH_LONG).show();
        }
        return ret;

    }


    void imoneyWithdrawJson() {
        if (new InternetCheck().isConnected(CashToCashTransferWitthdrawal.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateWithdrwalJson();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

        //    callApi("ImoneyWithdrawJson",requestData,172);
            new ServerTask(mComponentInfo, CashToCashTransferWitthdrawal.this, mHandler, requestData, "ImoneyWithdrawJson", 172).start();
        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void otpVerify() {
        if (new InternetCheck().isConnected(CashToCashTransferWitthdrawal.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateOtpVerifyJson();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

          //  callApi("getOTPInJSON",requestData,170);

            new ServerTask(mComponentInfo, CashToCashTransferWitthdrawal.this, mHandler, requestData, "getOTPInJSON", 170).start();
        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateOtpVerifyJson() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);


        //   String pinNeW = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "IMONEYWITHDRAWA");
            countryObj.put("accounttype", "");   // acording to shya, sir type null  3 jan 2018
            countryObj.put("otpCode", otpString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private String generateWithdrwalJson() {
        String jsonString = "";

       /* SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);
*/

        String pinNeW = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


        try {

            /*{
                "vendorcode": "MICR",
                    "agentcode": "237000271014",
                    "pin": "963A8D298502F0AA36C2824F7F9F069C",
                    "destination": "10F3147653701",
                    "source": "237000271016",
                    "amount": "100",
                    "pintype": "IPIN",
                    "comments": "Rajesh mishra"
            }*/

            JSONObject countryObj = new JSONObject();

            countryObj.put("vendorcode", "MICR");
            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pinNeW);
            // countryObj.put("pintype", "MPIN");
            countryObj.put("destination", accountNumber);
            countryObj.put("source", agentCode);   // acording to rajesh  source is agent code // 18 dec 2017
            countryObj.put("amount", amountString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("comments", "");
            countryObj.put("confcode", otpString);
            countryObj.put("transtype", "IMONEYWITHDRAWA");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CashToCashTransferWitthdrawal.this);
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
                Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(CashToCashTransferWitthdrawal.this, mComponentInfo, CashToCashTransferWitthdrawal.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashToCashTransferWitthdrawal.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.bill));
        //    String[] temp= data.split("\\|");
        //   builder.setMessage(String.format(getString(R.string.tariffResponseSuccess),operationNameString,temp[0], temp[1]) );

        builder.setMessage(getString(R.string.billPrintSucessReceipt) + " \n " + " \n " + data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                CashToCashTransferWitthdrawal.this.finish();
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

                    if (new InternetCheck().isConnected(CashToCashTransferWitthdrawal.this)) {
                        showProgressDialog(getString(R.string.pleasewait));

                    //    callApi("ImoneyWithdrawJson",requestData,172);
                        new ServerTask(mComponentInfo, CashToCashTransferWitthdrawal.this, mHandler, requestData, "ImoneyWithdrawJson", 172).start();
                    } else {
                        Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }

                } else {
                }
                break;
        }
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 172) {
                /*String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                String resultdesriptionNull = temp[7];
                System.out.print(tariffAmountFee);

                if(resultdesriptionNull.equalsIgnoreCase(""))
                {
                    Toast.makeText(CashToCashTransferWitthdrawal.this, "" + getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();

                }
                else {
                     showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
              */
                showSuccessReceipt(generalResponseModel.getUserDefinedString());

            } else if (requestNo == 114) {

                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                tariffAmountFee = temp[0];
                System.out.print(tariffAmountFee);



                mobileNumber_textView_reviewpage.setText(customerMobileNoString);
                amountTextView_reviewpage.setText(amountString);
                recipientCountry_TxtView_reviewPage.setText(spinnerCountryString);
                tariffAmmount_textview_privewpage.setText(tariffAmountFee);

                scrollview_firstpage.setVisibility(View.GONE);
                scrollview_otpPage.setVisibility(View.GONE);
                scrollview_secondPage.setVisibility(View.GONE);
                scrollview_confirmationPage.setVisibility(View.VISIBLE);
                // scrollview_secondPage.setVisibility(View.VISIBLE);

                slectKeyboard=2;
                tariffResponse = 1;

            } else if (requestNo == 168) {

                /*if(tariffResponse==0)
                {
                   proceedTariffAmount();
                }

                else
                {
                    scrollview_confirmationPage.setVisibility(View.VISIBLE);

                }*/


                scrollview_firstpage.setVisibility(View.GONE);
                scrollview_secondPage.setVisibility(View.GONE);
                scrollview_confirmationPage.setVisibility(View.GONE);

                scrollview_otpPage.setVisibility(View.VISIBLE);

                slectKeyboard = 3;
                System.out.println(slectKeyboard);


            } else if (requestNo == 169) {

                scrollview_firstpage.setVisibility(View.GONE);
                scrollview_confirmationPage.setVisibility(View.GONE);
                scrollview_secondPage.setVisibility(View.VISIBLE);

                String[] tempData = generalResponseModel.getUserDefinedString().split("\\|");
                accountNumber = tempData[1];
                customerName = tempData[2];
                idType = tempData[3];
                idNumber = tempData[4];
                place= tempData[5];
                date= tempData[6];

                idtype_textView_secondPage.setText(idType);     // from server
                idNumber_textview_secondPage.setText(idNumber);                 // from server
                place_textView_secondpage.setText(place);                       // from server
                dateExpiry_textView_secondpage.setText(date);                   // from server
                mobileNumber_textView_secondPage.setText(customerMobileNoString);   // from server
                accountNumber_textview_secondPage.setText(accountNumber);       // from server
                customerName_textView_secondpage.setText(customerName);         // from server


                slectKeyboard = 1;
                System.out.println(slectKeyboard);


            } else if (requestNo == 170) {


                proceedSerachApi();

                scrollview_firstpage.setVisibility(View.GONE);
                scrollview_otpPage.setVisibility(View.GONE);
                scrollview_secondPage.setVisibility(View.VISIBLE);




            }

        } else {
            hideProgressDialog();
            Toast.makeText(CashToCashTransferWitthdrawal.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();



          /*  proceedSerachApi();
            scrollview_otpPage.setVisibility(View.GONE);
            scrollview_secondPage.setVisibility(View.VISIBLE);*/


        }
    }

    private void ServerRequestSendSms() {


        if (new InternetCheck().isConnected(CashToCashTransferWitthdrawal.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateResendSmsConfcode();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

        //    callApi("resendOTPInJSON",requestData,168);

            new ServerTask(mComponentInfo, CashToCashTransferWitthdrawal.this, mHandler, requestData, "resendOTPInJSON", 168).start();
        } else {
            Toast.makeText(CashToCashTransferWitthdrawal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }



    private String generateResendSmsConfcode() {
        String jsonString = "";

        // String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {


            /*

            {
              "agentcode": "237699325872",
              "requestcts": "25/05/2016 18:01:51",
              "vendorcode": "MICR",
              "clienttype": "GPRS",
              "transtype": "BALANCE",
              "accounttype": "FD"
            }

             */

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentcode", agentCode);
            countryObj.put("requestcts", "");
            countryObj.put("destination", customerMobileNoString);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "IMONEYWITHDRAWA");
            countryObj.put("accounttype", "MA");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("spinnerCountryString", spinnerCountryString);
        // bundle.putString("tariffAmountFee", tariffAmountFee);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("spinnerCountryString", spinnerCountryString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("customerMobileNoString", customerMobileNoString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("amountString", amountString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("tariffAmountFee", tariffAmountFee).commit();

        Intent intent = new Intent(CashToCashTransferWitthdrawal.this, SucessReceiptCashToCashWithdrawal.class);
        intent.putExtra("data", data);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        intent.putExtra("customerMobileNoString", customerMobileNoString);
        intent.putExtra("amountString", amountString);
        intent.putExtra("tariffAmountFee", tariffAmountFee);


        startActivity(intent);
        CashToCashTransferWitthdrawal.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            CashToCashTransferWitthdrawal.this.finish();
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



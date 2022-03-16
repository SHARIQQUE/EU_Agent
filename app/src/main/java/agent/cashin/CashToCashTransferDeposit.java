package agent.cashin;


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
import sucess_receipt.SucessReceiptCashToCashDeposit;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class CashToCashTransferDeposit extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

    int slectKeyboard=0;

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


                if(slectKeyboard==0) {

                    validationDetails_FirstPage();
                }

                else if(slectKeyboard==1)
                {
                    validationDetails_SecondPage();
                }

                else if(slectKeyboard==2)
                {
                    validationDetails_ConfirmationPage();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    TextView mobileNumber_textView_secondPage, accountNumber_textview_secondPage, customerName_textView_secondpage;


    public AutoCompleteTextView amountEditText_secondpage, transactionNumber_EditText, mpinEditText, customerMobileNo_EditText;
    Spinner operationTypeSpinner, billerSpinner, accountNameSpinner;
    Button nextbutton_secondpage, nextButton_firstpage, buttonSubmit_reviewpage;
    Toolbar mToolbar;
    Spinner spinnerCountry;
    private String[] countryMobileNoLengthArray, countryArray;

    String spinnerCountryString, transferBasisString, countrySelectionString;
    ComponentMd5SharedPre mComponentInfo;
    boolean isBillPay = false;
    LinearLayout input_LL, review_LL;
    ListView statementLsitView;
    ScrollView scrollview_firstpage, scrollview_secondPage, scrollview_confirmationPage;
    ProgressDialog mDialog;
    String customerName,accountNumber,amountString;
    TextView amountTextView_reviewpage, tariffAmmount_textview_privewpage, mobileNumber_textView_reviewpage,customerName_textView_confirmationPage,accountNumber_textview_confirmationpage, recipientCountry_TxtView_reviewPage;
    Dialog successDialog;
    String tariffAmountFee, agentName, agentCode, customerMobileNoString, operationCodeString, operationNameString, mpinString, accountTypeCodeString, billerCodeString, destinationString = "";
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

        setContentView(R.layout.cashtocash_transferdeposit);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_prints);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

       // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashfortransfer_deposit));
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




        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {
            CashToCashTransferDeposit.this.finish();
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

        operationTypeSpinner = (Spinner) findViewById(R.id.spinner_OperationType_Tariff);
        billerSpinner = (Spinner) findViewById(R.id.spinner_Biller_Tariff);
        accountNameSpinner = (Spinner) findViewById(R.id.spinner_AccountType_Tariff);

        nextButton_firstpage = (Button) findViewById(R.id.nextButton_firstpage);
        nextButton_firstpage.setOnClickListener(this);

        nextbutton_secondpage = (Button) findViewById(R.id.nextbutton_secondpage);
        nextbutton_secondpage.setOnClickListener(this);


        buttonSubmit_reviewpage = (Button) findViewById(R.id.buttonSubmit_reviewpage);
        buttonSubmit_reviewpage.setOnClickListener(this);

        mobileNumber_textView_reviewpage = (TextView) findViewById(R.id.mobileNumber_textView_reviewpage);
        amountTextView_reviewpage = (TextView) findViewById(R.id.amountTextView_reviewpage);
        recipientCountry_TxtView_reviewPage = (TextView) findViewById(R.id.recipientCountry_TxtView_reviewPage);
        tariffAmmount_textview_privewpage = (TextView) findViewById(R.id.tariffAmmount_textview_privewpage);

        mobileNumber_textView_secondPage = (TextView) findViewById(R.id.mobileNumber_textView_secondPage);
        accountNumber_textview_secondPage = (TextView) findViewById(R.id.accountNumber_textview_secondPage);
        customerName_textView_secondpage = (TextView) findViewById(R.id.customerName_textView_secondpage);

        customerName_textView_confirmationPage = (TextView) findViewById(R.id.customerName_textView_confirmationPage);
        accountNumber_textview_confirmationpage = (TextView) findViewById(R.id.accountNumber_textview_confirmationpage);



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
                customerMobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNo_imoneyDeposit), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
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
                customerMobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNo_imoneyDeposit), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                customerMobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                customerMobileNo_EditText.setFilters(digitsfilters);
                customerMobileNo_EditText.setText("");

            }
        } else {
            Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

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
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNo_imoneyDeposit), lengthToCheck + 1 + "");

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
                        Toast.makeText(CashToCashTransferDeposit.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }



                operationCodeString = operationCodeArray[operationTypeSpinner.getSelectedItemPosition()];
                operationNameString = operationTypeSpinner.getSelectedItem().toString();
                billerCodeString = billerSpinner.getSelectedItem().toString();
                //     accountTypeCodeString = accountCodeArray[accountNameSpinner.getSelectedItemPosition()];

                ret = true;



            } else {
                Toast.makeText(CashToCashTransferDeposit.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }


         /*   } else {
                accountNameSpinner.requestFocus();
                Toast.makeText(PrintBillPay.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
            }*/
        } else {
            Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }


        return ret;
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.nextButton_firstpage:

                validationDetails_FirstPage();

                break;

            case R.id.nextbutton_secondpage:

                // Toast.makeText(CashToCashTransferDeposit.this, " Second Page  ", Toast.LENGTH_LONG).show();

                validationDetails_SecondPage();

                break;


            case R.id.buttonSubmit_reviewpage:

                // Toast.makeText(CashToCashTransferDeposit.this, " Review page  ", Toast.LENGTH_LONG).show();

                validationDetails_ConfirmationPage();

                break;


        }
    }

    public void validationDetails_FirstPage() {
        if (isMiniStmtMode) {
            isMiniStmtMode = false;
            mpinEditText.setText("");
            nextButton_firstpage.setText(getString(R.string.next));
            input_LL.setVisibility(View.VISIBLE);
            review_LL.setVisibility(View.GONE);
        }

        if (validateDetails_mobileNumber()) {
            slectKeyboard = 1;

            proceedSerachApi();

        }
    }

    void validationDetails_SecondPage() {
        if (validateDetailsAmount_secondPAge()) {
            slectKeyboard = 2;
            proceedTariffAmount();
        }
    }


    void validationDetails_ConfirmationPage() {
        if (validateDetailsMpin()) {
            proceedCashToCashDeposit();
        }
    }

    void proceedCashToCashDeposit() {
        if (new InternetCheck().isConnected(CashToCashTransferDeposit.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateBillPrint();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


          //  callApi("ImoneyDepositeJson",requestData,173);

            new ServerTask(mComponentInfo, CashToCashTransferDeposit.this, mHandler, requestData, "ImoneyDepositeJson", 173).start();
        } else {
            Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(CashToCashTransferDeposit.this,mComponentInfo, CashToCashTransferDeposit.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(CashToCashTransferDeposit.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(CashToCashTransferDeposit.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }

    private String generateTariffAmmount() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("destination", accountNumber);  //
            countryObj.put("amount", amountString);
            countryObj.put("transtype", "IMONEYDEPCASHOU");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            String fromCity = mComponentInfo.getmSharedPreferences().getString("state", "");

            //  countryObj.put("fromcity", fromCity);   //  change from server
            //  countryObj.put("tocity", toCity);     //  Change from Server
            countryObj.put("comments", "");
            countryObj.put("udv1", accountNumber);
            countryObj.put("accountType", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);
            //  countryObj.put("destination", nameNumberString);


         /*   <estel><header><requesttype>TARIFF</requesttype></header><request><agentcode>237000271009</agentcode>
            <pin>08F0CEF78D6EECA3C1C35B28FCF822A7</pin><amount>3000</amount><transtype>REMTSEND</transtype>
            <vendorcode>MICR</vendorcode><requestcts>2017-05-30 17:41:47</requestcts><comments>NOSMS
                    </comments><clienttype>HTTP</clienttype><destination>237768678678
                    </destination><pintype>IPIN</pintype></request></estel>

          */

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    void proceedSerachApi() {
        if (new InternetCheck().isConnected(CashToCashTransferDeposit.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateSerachData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

         //   callApi("getImoneyInfoAccountJson",requestData,169);

            new ServerTask(mComponentInfo, CashToCashTransferDeposit.this, mHandler, requestData, "getImoneyInfoAccountJson", 169).start();
        } else {
            Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void proceedTariffAmount() {
        if (new InternetCheck().isConnected(CashToCashTransferDeposit.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

          //  callApi("getTarifInJSON",requestData,114);

            new ServerTask(mComponentInfo, CashToCashTransferDeposit.this, mHandler, requestData, "getTarifInJSON", 114).start();
        } else {
            Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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
            countryObj.put("agentcode", customerMobileNoString);
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

    private String generateBillPrint() {
        String jsonString = "";

       /* SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);
*/

        String pinNeW = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


        try {


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
            countryObj.put("clienttype", "GPRS");
            countryObj.put("sendermobile", "");    // // acording to rajesh  sender mobile number  is blank// 18 dec 2017
            countryObj.put("requestcts", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CashToCashTransferDeposit.this);
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
                Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(CashToCashTransferDeposit.this, mComponentInfo, CashToCashTransferDeposit.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashToCashTransferDeposit.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.bill));
        //    String[] temp= data.split("\\|");
        //   builder.setMessage(String.format(getString(R.string.tariffResponseSuccess),operationNameString,temp[0], temp[1]) );

        builder.setMessage(getString(R.string.billPrintSucessReceipt) + " \n " + " \n " + data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                CashToCashTransferDeposit.this.finish();
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

                    if (new InternetCheck().isConnected(CashToCashTransferDeposit.this)) {
                        showProgressDialog(getString(R.string.pleasewait));

                  //      callApi("ImoneyDepositeJson",requestData,173);

                        new ServerTask(mComponentInfo, CashToCashTransferDeposit.this, mHandler, requestData, "ImoneyDepositeJson", 173).start();
                    } else {
                        Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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

            if (requestNo == 173) {

                /*if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "TARIFF").commit();
                    Intent i = new Intent(CashToCashTransferDeposit.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }*/

                showSuccessReceipt(generalResponseModel.getUserDefinedString());

            }

            else if (requestNo == 114) {

                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                tariffAmountFee = temp[0];
                System.out.print(tariffAmountFee);



                scrollview_firstpage.setVisibility(View.GONE);
                scrollview_secondPage.setVisibility(View.GONE);
                scrollview_confirmationPage.setVisibility(View.VISIBLE);

                customerName_textView_confirmationPage.setText(customerName); // from server
                accountNumber_textview_confirmationpage.setText(accountNumber);// from server
                mobileNumber_textView_reviewpage.setText(customerMobileNoString);
                amountTextView_reviewpage.setText(amountString);
                recipientCountry_TxtView_reviewPage.setText(spinnerCountryString);
                tariffAmmount_textview_privewpage.setText(tariffAmountFee);

            }

            else if (requestNo == 169) {

                scrollview_firstpage.setVisibility(View.GONE);
                scrollview_confirmationPage.setVisibility(View.GONE);
                scrollview_secondPage.setVisibility(View.VISIBLE);


                String[] tempData = generalResponseModel.getUserDefinedString().split("\\|");
                accountNumber = tempData[1];
                customerName = tempData[2];

                mobileNumber_textView_secondPage.setText(customerMobileNoString);
                accountNumber_textview_secondPage.setText(accountNumber);  // from server
                customerName_textView_secondpage.setText(customerName);   // from server

            }


        } else {
            hideProgressDialog();
            Toast.makeText(CashToCashTransferDeposit.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
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

        Intent intent = new Intent(CashToCashTransferDeposit.this, SucessReceiptCashToCashDeposit.class);
        intent.putExtra("data", data);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        intent.putExtra("customerMobileNoString", customerMobileNoString);
        intent.putExtra("amountString", amountString);
        // intent.putExtra("tariffAmountFee", tariffAmountFee);


        startActivity(intent);
        CashToCashTransferDeposit.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            CashToCashTransferDeposit.this.finish();
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

    private boolean validateDetailsAmount_secondPAge() {
        boolean ret = false;

        amountString = amountEditText_secondpage.getText().toString().trim();
        if (amountString.length() > 0 && validateAmount(amountString)) {

            ret = true;

        } else {
            Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.amountNew), Toast.LENGTH_LONG).show();
        }
        return ret;

    }


    private boolean validateDetailsMpin() {
        boolean ret = false;

        //  if (payerAccountsSpinner.getSelectedItemPosition() > 0) {

        mpinString = mpinEditText.getText().toString();
        if (mpinString.trim().length() == 4) {

            ret = true;

        } else {
            Toast.makeText(CashToCashTransferDeposit.this, getString(R.string.mpinAccountBalance), Toast.LENGTH_SHORT).show();
        }
    /*    } else {
            Toast.makeText(AccountBalance.this, getString(R.string.pleaseselectbankaccount), Toast.LENGTH_SHORT).show();
        }*/

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



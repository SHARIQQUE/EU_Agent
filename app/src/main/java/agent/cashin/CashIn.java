package agent.cashin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import agent.activities.OTPVerificationActivity;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptCashIn;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by Sahrique on 14/03/17.
 */

public class CashIn extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String tariffAmountFee, countrySelectedCode,commentString;
    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    TextView subscriberName_textView,cityReceipt;
    ComponentMd5SharedPre mComponentInfo;

    String stateNameString,agentName, agentCode,subscriberNameString,
            spinnerCountryString, recipientBankString, recipientAccountString,
            recipientMobileNoString, transferBasisString, subscriberNumberString,
            payerBankString, transferTagString, amountString,
            spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button nextButton_MoneyTransfer, previousButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    ImageView imageViewPicture, imageViewSign;
    int transferCase, accToAccLevel = 0;
    String toCity;
    private Spinner spinnerCountry,
            recipientbankSpinner, recipientAccTypeSpinner,
            payerBankSpinner, spinnerAccountToDebit,
            transferTagSpinner, transferBasisSpinner;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;
    private AutoCompleteTextView name_No_EditText, commentsEditText, amountEditText, mpinEditText, recipient_AccountNo_EditText,
            subscriber_MobileNo_EditText;
    private TextView comment_EdittextReview_AccToCash, recipientCountryTxtView_Review, transferBasisTxtView_Review,
            recipientNameNoTitleTxtView_Review, recipientNameNoTxtView_Review,
            recipientAccountTypeTxtView_Review, payerBankTxtView_Review, titleTextView,
            payerAccountTypeTxtView_Review, amountTxtView_Review, recipientBankTxtView_Review, transferTagTxtView_Review;
    private ProgressDialog mDialog;
    EditText tariffAmmount_EdittextReview_AccToCash;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());

            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();

            } else {
                DataParserThread thread = new DataParserThread(CashIn.this, mComponentInfo, CashIn.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;

    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

                validateDetails();


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

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


        setContentView(R.layout.cash_in);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();


        transferTagArray = getResources().getStringArray(R.array.TransferTag);

        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MoneyTransfer);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
       // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashInNewDisplaypage));
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
        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {

            CashIn.this.finish();
        }


        nextButton_MoneyTransfer = (Button) findViewById(R.id.nextButton_MoneyTransfer);
        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);
        nextButton_MoneyTransfer.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton_MoneyTransfer.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.GONE);
        subscriberName_textView = (TextView) findViewById(R.id.subscriberName_textView);
        commentsEditText = (AutoCompleteTextView) findViewById(R.id.comment_EditText_AccToCash);
        commentsEditText.setOnEditorActionListener(this);

        cityReceipt = (TextView) findViewById(R.id.cityReceipt);

        input_SV_AccToCash = (ScrollView) findViewById(R.id.input_SV_AccToCash);

        review_SV_AccToCash = (ScrollView) findViewById(R.id.review_SV_AccToCash);

        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);

        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        imageViewSign = (ImageView) findViewById(R.id.imageViewSign);

        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(CashIn.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);

        tariffAmmount_EdittextReview_AccToCash = (EditText) findViewById(R.id.tariffAmmount_EdittextReview_AccToCash);


        subscriber_MobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.nameNumberEditText_AccToCash);
        subscriber_MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));

        amountEditText = (AutoCompleteTextView) findViewById(R.id.amountEditText_AccToCash);
        amountEditText.setOnEditorActionListener(this);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(CashIn.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_LONG).show();
                    if (enteredString.length() > 0) {
                        amountEditText.setText(enteredString.substring(1));
                    } else {
                        amountEditText.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 0) {
            String[] data = bankAccounts.split("\\;");

            ArrayList<String> accountList = new ArrayList<String>();
            ArrayList<String> accountCodeList = new ArrayList<String>();
            accountList.add(getString(R.string.accounttodebit));
            accountCodeList.add(getString(R.string.accounttodebit));

            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    if (tData[4].equalsIgnoreCase("MA")) {
                        accountList.add(tData[0] + " - " + tData[1]);
                        accountCodeList.add(tData[4]);
                    }
                }
            }

            payerBankAccountsArray = accountList.toArray(new String[accountList.size()]);

            payerAccountCodeArray = accountCodeList.toArray(new String[accountCodeList.size()]);

        } else {
            payerBankAccountsArray = new String[1];
            payerBankAccountsArray[0] = getString(R.string.accounttodebit);
            payerAccountCodeArray = new String[1];
            payerAccountCodeArray[0] = getString(R.string.accounttodebit);
        }

        // spinnerAccountToDebit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));

        titleTextView = (TextView) findViewById(R.id.titleTextView);

        recipientCountryTxtView_Review = (TextView) findViewById(R.id.recipientCountry_TxtView_Review_AccToCash);
        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);
        recipientNameNoTxtView_Review = (TextView) findViewById(R.id.recipientNameNo_TxtView_Review_AccToCash);
        amountTxtView_Review = (TextView) findViewById(R.id.amount_TxtView_Review_AccToCash);
        payerAccountTypeTxtView_Review = (TextView) findViewById(R.id.payerAccountType_TxtView_Review_AccToCash);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText__Review_AccToCash);
        mpinEditText.setOnEditorActionListener(this);

        comment_EdittextReview_AccToCash = (TextView) findViewById(R.id.comment_EdittextReview_AccToCash);


        amountEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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

    }


    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                subscriber_MobileNo_EditText.setText("");
                subscriber_MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));
                // subscriber_MobileNo_EditText.setFilters(null);
                subscriber_MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                subscriber_MobileNo_EditText.setHint(String.format(getString(R.string.hintMobileCashIn), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                subscriber_MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                subscriber_MobileNo_EditText.setFilters(digitsfilters);
                subscriber_MobileNo_EditText.setText("");


            } else if (i == 2) {
                subscriber_MobileNo_EditText.setText("");
                InputFilter[] digitsfilters = new InputFilter[2];
                digitsfilters[0] = new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {
                        if (end > start) {

                            char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f',
                                    'g', 'h', 'i', 'j', 'k', 'l',
                                    'm', 'n', 'o', 'p', 'q', 'r',
                                    's', 't', 'u', 'v', 'w', 'x',
                                    'y', 'z', 'A', 'B', 'C', 'D',
                                    'E', 'F', 'G', 'H', 'I', 'J',
                                    'K', 'L', 'M', 'N', 'O', 'P',
                                    'Q', 'R', 'S', 'T', 'U', 'V',
                                    'W', 'X', 'Y', 'Z', '.'};

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


                digitsfilters[1] = new InputFilter.LengthFilter(16);
                subscriber_MobileNo_EditText.setHint(getString(R.string.pleaseentername));
                subscriber_MobileNo_EditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                subscriber_MobileNo_EditText.setFilters(digitsfilters);
                subscriber_MobileNo_EditText.setText("");
            }
        } else {
            Toast.makeText(CashIn.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:

                setInputType(i);

                break;
            case R.id.spinner_PayerAccountTypeSelection_AccToAcc:

                if (i > 0) {

                    if (amountEditText != null) {
                        amountEditText.requestFocus();
                    }
                }
                break;
            case R.id.spinnerCountry:

                countrySelectedCode = countryCodeArray[i];
                setInputType(transferBasisSpinner.getSelectedItemPosition());

                break;
            ////  case R.id.spinnerAccountToDebit:
               /* if(i == 0){

                }
                if (i > 0) {
                    if(payerAccountTypeSpinner.getSelectedItem().toString() != "Mobile Account"){
                        Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                        payerAccountTypeSpinner.setSelection(0);
                    }*//*else{
                        if (amountEditText != null) {
                            amountEditText.requestFocus();
                        }
                    }*//*

                }*/
            //int pos = payerAccountTypeSpinner.getSelectedItemPosition();
//                if(pos == 0){
//
//                }else if(! (pos ==2)){
//                    Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
//                    payerAccountTypeSpinner.setSelection(0);
//                }
            // String temp = payerAccountTypeSpinner.getSelectedItem().toString();
             /*Please Select Account To Debit*/
               /* switch (temp){
                   *//* case 0:
                        break;
                    case 1:
                        Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                        payerAccountTypeSpinner.setSelection(0);
                        break;
                    case 2:
                        break;
                    case 3:
                        Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                        payerAccountTypeSpinner.setSelection(0);
                        break;
                    case 4:
                        Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                        payerAccountTypeSpinner.setSelection(0);
                        break;*//*
                }*/
            //  break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showAccToCashReview() {

        CashIn.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();
                titleTextView.setText(getString(R.string.cashiInDetailConfirmationPage));
                input_SV_AccToCash.setVisibility(View.GONE);
                review_SV_AccToCash.setVisibility(View.VISIBLE);
                subscriberName_textView.setText(subscriberNameString);
                recipientCountryTxtView_Review.setText(spinnerCountryString);
                transferBasisTxtView_Review.setText(transferBasisString);
                recipientNameNoTxtView_Review.setText(subscriberNumberString);
                amountTxtView_Review.setText(amountString);
                // payerAccountTypeTxtView_Review.setText(spinnerAccountToDebitString);
                nextButton_MoneyTransfer.setText(getString(R.string.submit));
                isReview = true;
                mpinEditText.requestFocus();
                comment_EdittextReview_AccToCash.setText(commentString);

            }
        });


    }

    private boolean validateAccToCash_PartI() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileCashIn), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);
            }

            subscriberNumberString = subscriber_MobileNo_EditText.getText().toString().trim();
            if (subscriberNumberString.length() > lengthToCheck) {
                if (transferBasisposition == 1) {
                    if (subscriberNumberString.length() == ++lengthToCheck) {
                        subscriberNumberString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + subscriberNumberString;
                    } else {
                        Toast.makeText(CashIn.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                //  if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {
                amountString = amountEditText.getText().toString().trim();
                if (amountString.length() > 0 && validateAmount(amountString)) {

                    commentString = commentsEditText.getText().toString().trim();

                    ret = true;

                    spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                    accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];
                          /*  if(payerAccountTypeSpinner.getSelectedItem().toString()!= "Mobile Account"){
                                ret = false;
                                Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_SHORT).show();
                            }else{
                                ret = true;
                                payerAccountString = payerAccountTypeSpinner.getSelectedItem().toString();
                                accountCodeString = payerAccountCodeArray[payerAccountTypeSpinner.getSelectedItemPosition()];

                            }*/


                } else {
                    Toast.makeText(CashIn.this, getString(R.string.amountCashIn), Toast.LENGTH_LONG).show();

                }

               /* } else {
                    Toast.makeText(CashIn.this, getString(R.string.accountofpayer), Toast.LENGTH_LONG).show();

                }*/
            } else {
                Toast.makeText(CashIn.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }

            /*} else {
                Toast.makeText(CashIn.this, getString(R.string.pleaseselectsendmode), Toast.LENGTH_LONG).show();
              }*/

        } else {
            Toast.makeText(CashIn.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }


        return ret;


    }

    public void validateDetails() {
        if (!isReview) {

            if (validateAccToCash_PartI()) {
                pictureSignatureServerRequest();

            }
        } else {
            if (validateAccToCash_PartII()) {

                proceedMoneyTransfer();
            }
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
            countryObj.put("destination", subscriberNumberString);
            countryObj.put("amount", amountString);
            countryObj.put("transtype", "CASHIN");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            String fromCity = mComponentInfo.getmSharedPreferences().getString("state", "");

            countryObj.put("fromcity", fromCity);   //  change from server
            countryObj.put("tocity", toCity);     //  Change from Server
            countryObj.put("comments", commentString);
            countryObj.put("udv1", "");
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

    void proceedTariffAmount() {
        if (new InternetCheck().isConnected(CashIn.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

          //  callApi("getTarifInJSON",requestData,114);

            new ServerTask(mComponentInfo, CashIn.this, mHandler, requestData, "getTarifInJSON", 114).start();

        } else {
            Toast.makeText(CashIn.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(CashIn.this,mComponentInfo, CashIn.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(CashIn.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(CashIn.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    public void pictureSignatureServerRequest() {
        showProgressDialog(getString(R.string.pleasewait));
        String requestData = generatePictureSign();

      //  callApi("getViewProfileInJSON",requestData,148);

        new ServerTask(mComponentInfo, CashIn.this, mHandler, requestData, "getViewProfileInJSON", 148).start();
    }

    private String generatePictureSign() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

           /* {"agentCode":"237000271016","pin":"46A75E89BC40FDD2F70895AB710D5A22",
            "source":"237000271016","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS"}
              */
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            countryObj.put("source", subscriberNumberString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_MoneyTransfer:
                validateDetails();
                break;
        }
    }

    private void proceedMoneyTransfer() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(CashIn.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateAccToCashData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

          //  callApi("getCashInTransaction",requestData,119);

            new ServerTask(mComponentInfo, CashIn.this, mHandler, requestData, "getCashInTransaction", 119).start();


        } else {
            Toast.makeText(CashIn.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private String generateAccToCashData() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();

        try {

            /* {"agentCode":"237000271010","pin":"D3F8D3568D92CA73E4D5A1671529F075","source":"237000271010",
               "comments":"sms","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS","amount":"2000",
              "destination":"237000271015","accountType":"MA","requestcts":"25/05/2016 18:01:51","udv2":"NOSMS"}
            */

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", agentCode);
            countryObj.put("comments", commentString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("amount", amountString);
            countryObj.put("destination", subscriberNumberString);
            countryObj.put("requestcts", "");
            countryObj.put("udv2", "");
            countryObj.put("accountType", "MA");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            //   countryObj.put("country", countrySelectedCode);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private boolean validateAccToCash_PartII() {

        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;

        } else {
            Toast.makeText(CashIn.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();

        }

        return ret;
    }


 /*   private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashIn.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.cashIn));
        // payerAccountString = payerAccountString.replace("_", "-");
        //  payerAccountString = payerAccountString.toUpperCase();


        String[] temp = data.split("\\|");
        //builder.setMessage(String.format(getString(R.string.tariffResponseSuccess)+temp[0], temp[1]) );

        //   check sdsdsd if ammount max 100001

        if (temp[2].matches("commentSms")) {
            builder.setMessage(getString(R.string.cashInSucessReceipt) + getString(R.string.transactionId) + temp[0]);
        } else {
            builder.setMessage(getString(R.string.cashInSucessReceipt) + getString(R.string.transactionId) + temp[0] + " \n" + getString(R.string.cashInSucessReceiptAmountMaximumNote));
        }

        // builder.setMessage(getString(R.string.cashInSucessReceipt) + " \n " +" \n "+ data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                CashIn.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }*/


    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("spinnerCountryString", spinnerCountryString);
        bundle.putString("tariffAmountFee", tariffAmountFee);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("spinnerCountryString", spinnerCountryString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("tariffAmountFee", tariffAmountFee).commit();


        mComponentInfo.getmSharedPreferences().edit().putString("stateNameString", stateNameString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("amountString", amountString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("comment_cashin", commentString).commit();



        Intent intent = new Intent(CashIn.this, SucessReceiptCashIn.class);

        intent.putExtra("data", data);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        intent.putExtra("tariffAmountFee", tariffAmountFee);
        intent.putExtra("amountString", amountString);



        startActivity(intent);
        CashIn.this.finish();
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0 || requestNo == 148) {
            if (requestNo == 119) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(CashIn.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            } else if (requestNo == 148) {
                String responseData = generalResponseModel.getUserDefinedString();
                String[] responseArray = responseData.split("\\|");


                if (responseArray[0].matches("pictureSign")) {

                  //  if (getStreamFromResponse(responseArray[1], responseArray[2]) != null) {


                        toCity = responseArray[4];
                        subscriberNameString = responseArray[5];

                        stateNameString =  responseArray[8];
                        cityReceipt.setText(stateNameString);

                    byte[] imageBytes1 = Base64.decode(responseArray[1], Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                    imageViewSign.setImageBitmap(decodedImage);

                    byte[] imageBytes2 = Base64.decode(responseArray[2], Base64.DEFAULT);
                    Bitmap decodedImage2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);
                    imageViewPicture.setImageBitmap(decodedImage2);


                    //  getByteByString(text);
                     //   setImage();
                        //  showAccToCashReview();
                  /*  } else {
                        //Toast.makeText(this, "Image Data is not in proper format", Toast.LENGTH_SHORT).show();
                    }
*/
                    /*if (getByteByStringSign(text[1]) != null) {
                        setImageSign();
                        //   showAccToCashReview();
                    } else {
                        //Toast.makeText(this, "Sign Data is not in proper format", Toast.LENGTH_SHORT).show();
                    }*/

                    proceedTariffAmount();

                } else {
                    proceedTariffAmount();
                }
            }

            else if (requestNo == 114) {
                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                tariffAmountFee = temp[0];
                System.out.print(tariffAmountFee);
                tariffAmmount_EdittextReview_AccToCash.setText(tariffAmountFee);
                tariffAmmount_EdittextReview_AccToCash.setEnabled(false);
                showAccToCashReview();
                //   showAccToCashReview(tariffAmountFee);
            }


        } else {
            hideProgressDialog();
            Toast.makeText(CashIn.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }

    String[] text = new String[2];
    public static byte[] resultByteArrayImage;
    public static byte[] resultByteArraySign;
    public static byte[][] bytesArray = new byte[2][2];
    InputStream inputImage, inputSign;

    public String[] getStreamFromResponse(String imageBinary, String signBinary) {
        try {
            inputImage = new ByteArrayInputStream(imageBinary.getBytes());
            inputSign = new ByteArrayInputStream(signBinary.getBytes());

            int sizeImageInput = inputImage.available();
            int sizeSignInput = inputSign.available();

            byte[] bufferImage = new byte[sizeImageInput];
            byte[] bufferSign = new byte[sizeSignInput];

            inputImage.read(bufferImage);
            inputImage.close();

            inputSign.read(bufferSign);
            inputSign.close();

            // byte buffer into a string
            text[0] = new String(bufferImage);
            text[1] = new String(bufferSign);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }

    public static byte[] getByteByStringSign(String binaryStringArrayImageSign) {
        int splitSize = 8;
        int index = 0;
        int position = 0;
        int i = 0;
        if (binaryStringArrayImageSign.length() % splitSize == 0) {
            // resultByteArraySign = new byte[binaryStringArrayImageSign[1].length() / splitSize];
            resultByteArraySign = new byte[binaryStringArrayImageSign.length() / splitSize];
            StringBuilder text = new StringBuilder(binaryStringArrayImageSign);
            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArraySign[position] = byteAsInt.byteValue();
                index += splitSize;
                position++;
            }
            return resultByteArraySign;
        } else {
            System.out.println("Cannot convert binary string to byte[], because of the input length. '" + binaryStringArrayImageSign + "' % 8 != 0");
            return null;
        }

    }

    public static byte[] getByteByString(String[] binaryStringArrayImageSign) {
        int splitSize = 8;
        int index = 0;
        int position = 0;
        int i = 0;

        if (binaryStringArrayImageSign[0].length() % splitSize == 0) {
            // resultByteArrayImage = new byte[binaryStringArrayImageSign[0].length() / splitSize];
            resultByteArrayImage = new byte[binaryStringArrayImageSign[0].length() / splitSize];
            StringBuilder text = new StringBuilder(binaryStringArrayImageSign[0]);

            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArrayImage[position] = byteAsInt.byteValue();
                index += splitSize;
                position++;
            }
            return resultByteArrayImage;
        } else {
            System.out.println("Cannot convert binary string to byte[], because of the input length. '" + binaryStringArrayImageSign[0] + "' % 8 != 0");
            return null;
        }


    }

    Bitmap bmpImage, bmpSign;

    public void setImage() {
        try {
            bmpImage = BitmapFactory.decodeByteArray(resultByteArrayImage, 0, resultByteArrayImage.length);
            imageViewPicture.setImageBitmap(bmpImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageSign() {
        try {
            bmpSign = BitmapFactory.decodeByteArray(resultByteArraySign, 0, resultByteArraySign.length);
            imageViewSign.setImageBitmap(bmpSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            CashIn.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }


    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CashIn.this);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            if (!isServerOperationInProcess) {
                CashIn.this.finish();
            }
        }
        return super.onOptionsItemSelected(menuItem);
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


    private boolean validateAmount(String input) {
        boolean ret = false;

        try {
            int amt = Integer.parseInt(input);
            if (amt > 0) {
                ret = true;
                amountString = "" + amt;

            }

        } catch (Exception e)

        {

        }

        return ret;


    }

}

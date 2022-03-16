package agent.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.LinearLayout;
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
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptCashToMarchant;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

/**
 * Created by Sahrique on 14/03/17.
 */


public class CashToMarchant extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    Double feesAdd, totalAmount, amountAdd;
    TextView totalAmount_textview_reviewPage, fees_textview_reviewPage;
    ComponentMd5SharedPre mComponentInfo;
    String feesSupprtyByString, totalAmountSubscriber, tariffAmountFee, agentName, destinationNoString, labelNameString, marchnatPhoneNoString, destinationNameString, spinnerBillerNameString, senderNameString, senderMobileNoString, commentString, agentCode, spinnerCountryString, transferBasisString, billNumberString, amountString, spinnerAccountToDebitString, mpinString, countrySelectedCode, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button nextButton_cashToMarchant, previousButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner spinnerCountry, spinnerAccountToDebit, spinnerBillerName;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;
    private AutoCompleteTextView marchantPhoneNumberEditText, destinationNumberEditText, destinationNameEditText, senderNameEditText, senderMobileNoEditText, commentEditText, amountEditText, mpinEditText, BillerNumberEditText, labelNameEditText;
    private TextView recipientCountryTxtView_Review, transferBasisTxtView_Review, billName_TxtView_Review, titleTextView, payerAccountTypeTxtView_Review, destinationTxtView_Review, billerCodeTxtView_Review, amountTxtView_Review, recipientBankTxtView_Review, transferTagTxtView_Review;
    private ProgressDialog mDialog;
    EditText destinationNameTxtView_Review, destinationNumber_TxtView_Review, senderName_TxtView_Review, senderNumber_TxtView_Review, billNumber_TxtView_Review, comment_TxtView_Review;
    String selectBillerName;
    LinearLayout linearLayout_totalAmount_reviewPage, linearLayout_fees_reviewPage;

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
                DataParserThread thread = new DataParserThread(CashToMarchant.this, mComponentInfo, CashToMarchant.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, BillerNameFetchArray, billerCodeArray, TempBillerNameFetchArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;

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


        setContentView(R.layout.cash_to_marchant);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_deposit);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
      //  countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashmarchnatdisplay));
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
            CashToMarchant.this.finish();
        }
        nextButton_cashToMarchant = (Button) findViewById(R.id.nextButton_cashToMarchant);
        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);
        nextButton_cashToMarchant.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton_cashToMarchant.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.GONE);

        input_SV_AccToCash = (ScrollView) findViewById(R.id.input_SV_AccToCash);
        review_SV_AccToCash = (ScrollView) findViewById(R.id.review_SV_AccToCash);
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);

        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        spinnerBillerName = (Spinner) findViewById(R.id.spinnerBillerName);

        // String[] billerNameDynamicData = getResources().getStringArray(R.array.BillerNameArray);// static Data from String


        /* TempBillerNameFetchArray = new String[BillerNameFetchArray.length + 1];     // AllReady Add add Select Biller name
        TempBillerNameFetchArray[0] = "Select Biller name";
        for (int i = 0; i < BillerNameFetchArray.length; i++) {
            TempBillerNameFetchArray[i + 1] = BillerNameFetchArray[i];
        }*/


        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);
        BillerNumberEditText = (AutoCompleteTextView) findViewById(R.id.BillerNumberEditText);
        labelNameEditText = (AutoCompleteTextView) findViewById(R.id.labelNameEditText);
        amountEditText = (AutoCompleteTextView) findViewById(R.id.amountEditText_AccToCash);


        marchantPhoneNumberEditText = (AutoCompleteTextView) findViewById(R.id.marchantPhoneNumberEditText);
        marchantPhoneNumberEditText.setHint(getString(R.string.PleasEenterMobileNumber));

        destinationNumberEditText = (AutoCompleteTextView) findViewById(R.id.destinationNumberEditText);
        destinationNumberEditText.setHint(getString(R.string.PleasEenterMobileNumber));


        destinationNameEditText = (AutoCompleteTextView) findViewById(R.id.destinationNameEditText);
        senderNameEditText = (AutoCompleteTextView) findViewById(R.id.senderNameEditText);
        senderMobileNoEditText = (AutoCompleteTextView) findViewById(R.id.senderMobileNoEditText);
        commentEditText = (AutoCompleteTextView) findViewById(R.id.commentEditText);
        totalAmount_textview_reviewPage = (TextView) findViewById(R.id.totalAmount_textview_reviewPage);
        fees_textview_reviewPage = (TextView) findViewById(R.id.fees_textview_reviewPage);

        linearLayout_totalAmount_reviewPage = (LinearLayout) findViewById(R.id.linearLayout_totalAmount_reviewPage);
        linearLayout_fees_reviewPage = (LinearLayout) findViewById(R.id.linearLayout_fees_reviewPage);


        amountEditText.setOnEditorActionListener(this);
        commentEditText.setOnEditorActionListener(this);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(CashToMarchant.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        amountEditText.setText(enteredString.substring(1));
                    } else {
                        amountEditText.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
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

        spinnerAccountToDebit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));

        titleTextView = (TextView) findViewById(R.id.titleTextView_AccToCash);

        recipientCountryTxtView_Review = (TextView) findViewById(R.id.recipientCountry_TxtView_Review_AccToCash);
        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);
        billNumber_TxtView_Review = (EditText) findViewById(R.id.billNumber_TxtView_Review);
        billName_TxtView_Review = (TextView) findViewById(R.id.billName_TxtView_Review);
        amountTxtView_Review = (TextView) findViewById(R.id.amount_TxtView_Review_AccToCash);
        destinationTxtView_Review = (TextView) findViewById(R.id.destination_TxtView_Review_AccToCash);
        destinationNameTxtView_Review = (EditText) findViewById(R.id.destinationNameTxtView_Review);
        senderName_TxtView_Review = (EditText) findViewById(R.id.senderName_TxtView_Review);
        billerCodeTxtView_Review = (TextView) findViewById(R.id.billerCode_TxtView_Review_AccToCash);
        comment_TxtView_Review = (EditText) findViewById(R.id.comment_TxtView_Review);
        destinationNumber_TxtView_Review = (EditText) findViewById(R.id.destinationNumber_TxtView_Review);
        senderNumber_TxtView_Review = (EditText) findViewById(R.id.senderNumber_TxtView_Review);
        payerAccountTypeTxtView_Review = (TextView) findViewById(R.id.payerAccountType_TxtView_Review_AccToCash);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText__Review_AccToCash);
        mpinEditText.setOnEditorActionListener(this);



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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_PayerAccountTypeSelection_AccToAcc:
                if (i > 0) {
                    if (amountEditText != null) {
                        amountEditText.requestFocus();
                    }
                }
                break;
            case R.id.spinnerCountry:
                setInputType(1);
                setInputTypeDestination(3);

                if (i > 0) {
                    countrySelectedCode = countryCodeArray[i];
                    startServerInteraction("getBillerJSON", generateBillerData(), 106);

                } else {

                }

                break;


            case R.id.spinnerBillerName:

                if (i == 0) {
                    // billPreferenceSpinner.setSelection(0);
                }
                if (i != 0) {

                    spinnerBillerNameString = spinnerBillerName.getSelectedItem().toString();  // spinner item selected
                    //Toast.makeText(BillpayDepositPtop.this, "" + spinnerBillerNameString, Toast.LENGTH_SHORT).show();
                    String generateBillercodeData = generateBillercodeData();
                    billerCode = billerCodeArray[i];
                    //  new ServerTask(mComponentInfo, CashToMarchant.this, mHandler, generateBillercodeData, "billerCode", 133).start();
                }
                break;
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

    public void startServerInteraction(String apiName, String data, int requestNo) {

        if (new InternetCheck().isConnected(CashToMarchant.this)) {
            showProgressDialog(getString(R.string.pleasewait));

         //   startServerInteraction("getBillerJSON", generateBillerData(), 106);

          //  callApi("getBillerJSON",generateBillerData(),106);

           // startServerInteraction("getBillerJSON", generateBillerData(), 106);


            new ServerTask(mComponentInfo, CashToMarchant.this, mHandler, generateBillerData(), "getBillerJSON", 106).start();


        } else {

            Toast.makeText(CashToMarchant.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

        }
    }
    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(CashToMarchant.this,mComponentInfo,CashToMarchant.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(CashToMarchant.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(CashToMarchant.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    String generateBillerData() {

        /*"agentCode":"237100001012","pin":"C09AF4A983FE8B815C9D130B6B04262E",
         "pintype":"MPIN","requestcts":"25/05/2016 18:01:51",
         "vendorcode":"MICR","clienttype":"GPRS","country":"TCH"*/

        String jsonString = "";
        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);
        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("transtype", "CASHTOM");   // CASH2M
            countryObj.put("clienttype", "GPRS");
            countryObj.put("country", countrySelectedCode);
            countryObj.put("udv2", "BILLERCODEREQUIRED");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                marchantPhoneNumberEditText.setText("");
                marchantPhoneNumberEditText.setHint(getString(R.string.enterMerchantMobileNo));
                // destinationEditText.setFilters(null);
                marchantPhoneNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                marchantPhoneNumberEditText.setHint(String.format(getString(R.string.hintDestinationMobileNoNew), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                marchantPhoneNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                marchantPhoneNumberEditText.setFilters(digitsfilters);
                marchantPhoneNumberEditText.setText("");


            } else if (i == 2) {
                marchantPhoneNumberEditText.setText("");
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
                                    'W', 'X', 'Y', 'Z', '.',
                                    '0', '1', '2', '3', '4', '5',
                                    '6', '7', '8', '9'

                            };

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
                marchantPhoneNumberEditText.setHint(getString(R.string.enterMerchantMobileNo));
                marchantPhoneNumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                marchantPhoneNumberEditText.setFilters(digitsfilters);
                marchantPhoneNumberEditText.setText("");
            }
        } else {
            Toast.makeText(CashToMarchant.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    private void setInputTypeDestination(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == i) {
                destinationNumberEditText.setText("");
                destinationNumberEditText.setHint(getString(R.string.hintDestinationMobileNo));
                // destinationEditText.setFilters(null);
                destinationNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                destinationNumberEditText.setHint(String.format(getString(R.string.hintDestinationMobileNo), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                destinationNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                destinationNumberEditText.setFilters(digitsfilters);
                destinationNumberEditText.setText("");


            } else if (i == 2) {
                marchantPhoneNumberEditText.setText("");
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
                                    'W', 'X', 'Y', 'Z', '.',
                                    '0', '1', '2', '3', '4', '5',
                                    '6', '7', '8', '9'

                            };

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
                marchantPhoneNumberEditText.setHint(getString(R.string.enterMerchantMobileNo));
                marchantPhoneNumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                marchantPhoneNumberEditText.setFilters(digitsfilters);
                marchantPhoneNumberEditText.setText("");
            }
        } else {
            Toast.makeText(CashToMarchant.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    private void showReviewPage() {

        CashToMarchant.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();
                titleTextView.setText(getString(R.string.pleasereviewdetail));
                input_SV_AccToCash.setVisibility(View.GONE);
                review_SV_AccToCash.setVisibility(View.VISIBLE);
                recipientCountryTxtView_Review.setText(spinnerCountryString);


                destinationNumber_TxtView_Review.setText(marchnatPhoneNoString);


                transferBasisTxtView_Review.setText(transferBasisString);
                billNumber_TxtView_Review.setText(billNumberString);
                billName_TxtView_Review.setText(spinnerBillerNameString);
                amountTxtView_Review.setText(amountString);
                senderNumber_TxtView_Review.setText(senderMobileNoString);

                destinationTxtView_Review.setText(marchnatPhoneNoString);
                destinationNameTxtView_Review.setText(destinationNameString);
                senderName_TxtView_Review.setText(senderNameString);
                // billerCodeTxtView_Review.setText(senderMobileNoString);
                comment_TxtView_Review.setText(commentString);

                payerAccountTypeTxtView_Review.setText(spinnerAccountToDebitString);
                nextButton_cashToMarchant.setText(getString(R.string.transfernow));
                isReview = true;
                mpinEditText.requestFocus();


            }
        });
    }

    private boolean validateCashOut_PartI() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();


            int transferBasisposition = 0;

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintDestinationMobileNo), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintDestinationMobileNo), lengthToCheck + 1 + "");
            }

            marchnatPhoneNoString = marchantPhoneNumberEditText.getText().toString().trim();
            // if (marchnatPhoneNoString.length() > lengthToCheck) {
            if (transferBasisposition == 1) {
                if (marchnatPhoneNoString.length() == ++lengthToCheck) {
                    marchnatPhoneNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + marchnatPhoneNoString;
                } else {
                    Toast.makeText(CashToMarchant.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                    return false;
                }
            }


            if (spinnerBillerName.getSelectedItemPosition() != 0) {

                // if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {

                billNumberString = BillerNumberEditText.getText().toString().trim();
                if (billNumberString.length() > 0) {

                    labelNameString = labelNameEditText.getText().toString().trim();
                    if (labelNameString.length() > 0) {

                        destinationNameString = destinationNameEditText.getText().toString().trim();
                        if (destinationNameString.length() > 0) {


                            int transferBasisposition2 = 0;
                            if (transferBasisposition2 == 3) {
                                transferBasisString = "Mobile Number";
                                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                                errorMsgToDisplay = String.format(getString(R.string.hintDestinationMobileNo), lengthToCheck + 1 + "");

                            } else {
                                transferBasisString = "Mobile Number";
                                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                                errorMsgToDisplay = String.format(getString(R.string.hintDestinationMobileNo), lengthToCheck + 1 + "");
                            }

                            destinationNoString = destinationNumberEditText.getText().toString().trim();
                            //   if (destinationNoString.length() > lengthToCheck) {
                            if (transferBasisposition == 3) {
                                if (destinationNoString.length() == ++lengthToCheck) {
                                    destinationNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + destinationNoString;
                                } else {
                                    Toast.makeText(CashToMarchant.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                    return false;
                                }
                            }

                            senderNameString = senderNameEditText.getText().toString().trim();
                            if (senderNameString.length() > 0) {

                                senderMobileNoString = senderMobileNoEditText.getText().toString().trim();
                                //   if (senderMobileNoString.length() > 0) {

                                amountString = amountEditText.getText().toString().trim();
                                if (amountString.length() > 0) {

                                    commentString = commentEditText.getText().toString().trim();
                                    if (commentString.length() > 0) {


                                        ret = true;


                                        marchnatPhoneNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + marchnatPhoneNoString;
                                        senderMobileNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + senderMobileNoString;
                                        spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                                        accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];


                                    } else {
                                        Toast.makeText(CashToMarchant.this, getString(R.string.comment), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(CashToMarchant.this, getString(R.string.pleaseEnterAmount), Toast.LENGTH_LONG).show();
                                }

                                      /*  } else {
                                            Toast.makeText(BillpayDepositPtop.this, getString(R.string.pleaseEnterSenderMobileNo), Toast.LENGTH_LONG).show();
                                        }*/
                            } else {
                                Toast.makeText(CashToMarchant.this, getString(R.string.pleaseEnterSenderName), Toast.LENGTH_LONG).show();
                            }
                               /* } else {
                                    Toast.makeText(BillpayDepositPtop.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

                                }*/

                        } else {
                            Toast.makeText(CashToMarchant.this, getString(R.string.pleaseEnterDestinationName), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CashToMarchant.this, getString(R.string.pleaseEnterLabelNameNew), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(CashToMarchant.this, getString(R.string.pleaseEnterBillNumberNew), Toast.LENGTH_LONG).show();
                }
                  /*  } else {
                        Toast.makeText(BillpayDepositPtop.this, getString(R.string.accountofpayer), Toast.LENGTH_LONG).show();
                    }*/
            } else {
                Toast.makeText(CashToMarchant.this, getString(R.string.billerNameMarchantNew), Toast.LENGTH_LONG).show();
            }

           /* } else {
                Toast.makeText(BillpayDepositPtop.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }*/


        } else {
            Toast.makeText(CashToMarchant.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetails() {
        if (!isReview) {
            if (validateCashOut_PartI()) {
                proceedTariffAmount();

            }
        } else {
            if (validateCashOut_PartII()) {
                BillpayDeposit();
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.nextButton_cashToMarchant:

                validateDetails();

                break;
        }
    }

    String billerCode = "";

    private void BillpayDeposit() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(CashToMarchant.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateBillPayDeposit();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

         //   callApi("getCashToMTransaction",requestData,130);

            new ServerTask(mComponentInfo, CashToMarchant.this, mHandler, requestData, "getCashToMTransaction", 130).start();

        } else {
            Toast.makeText(CashToMarchant.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private String generateBillPayDeposit() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

            destinationNameString = destinationNameTxtView_Review.getText().toString().trim();
            marchnatPhoneNoString = destinationNumber_TxtView_Review.getText().toString().trim();
            senderNameString = senderName_TxtView_Review.getText().toString().trim();
            senderMobileNoString = senderNumber_TxtView_Review.getText().toString().trim();
            billNumberString = billNumber_TxtView_Review.getText().toString().trim();
            commentString = comment_TxtView_Review.getText().toString().trim();

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("source", senderMobileNoString);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("amount", amountString);
            countryObj.put("sourceName", senderNameString);
            countryObj.put("destination", marchnatPhoneNoString);
            countryObj.put("destinationName", destinationNameString);
            countryObj.put("billerName", spinnerBillerNameString);
            countryObj.put("billerCode", billerCode);
            countryObj.put("invoiceno", billNumberString);
            countryObj.put("clientType", "GPRS");
            countryObj.put("comments", commentString);
            countryObj.put("vendorCode", "MICR");
            countryObj.put("udv1", labelNameString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

          /*
            String accountType = "";
            if (data[1].contains("Mobile Account")) {
                accountType = "MA";
            } else if (data[1].equalsIgnoreCase("savings_account")) {
                accountType = "SA";
            } else if (data[1].equalsIgnoreCase("fixed_deposite")) {
                accountType = "FD";
            } else {
                accountType = data[1];
            }
               countryObj.put("accountType", accountType);
            */

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private String generateBillercodeData() {
        String jsonString = "";
       /*
        {"agentCode":"237000271011","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS","billername":"ENEO"}
       */


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("billername", spinnerBillerNameString);

            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private boolean validateCashOut_PartII() {

        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;
        } else {
            Toast.makeText(CashToMarchant.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    private String generateTariffAmount() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("amount", amountString);
            countryObj.put("transtype", "CASHTOM");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            // countryObj.put("fromcity", "YDE");   //  change from server
            //  countryObj.put("tocity", "YDE");     //  Change from Server
            countryObj.put("comments", "");
            countryObj.put("udv1", "");
           // countryObj.put("accountType", "");
            countryObj.put("billerCode", billerCode);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);




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

    void proceedTariffAmount() {
        if (new InternetCheck().isConnected(CashToMarchant.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


          //  callApi("getTarifInJSON",requestData,114);


              new ServerTask(mComponentInfo, CashToMarchant.this, mHandler, requestData, "getTarifInJSON", 114).start();

        } else {
            Toast.makeText(CashToMarchant.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("labelNameString", labelNameString);
        bundle.putString("senderNameString", senderNameString);
        bundle.putString("spinnerCountryString", spinnerCountryString);
        bundle.putString("spinnerBillerNameString", spinnerBillerNameString);


        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("labelNameString", labelNameString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("senderNameString", senderNameString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("spinnerCountryString", spinnerCountryString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("spinnerBillerNameString", spinnerBillerNameString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("amountString", amountString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("destinationNameString", destinationNameString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("commentString", commentString).commit();


        mComponentInfo.getmSharedPreferences().edit().putString("totalAmountSubscriber", totalAmountSubscriber).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("tariffAmountFee", tariffAmountFee).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("feesSupprtyByString", feesSupprtyByString).commit();


        Intent intent = new Intent(CashToMarchant.this, SucessReceiptCashToMarchant.class);

        intent.putExtra("data", data);
        intent.putExtra("labelNameString", labelNameString);
        intent.putExtra("senderNameString", senderNameString);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        intent.putExtra("spinnerBillerNameString", spinnerBillerNameString);
        intent.putExtra("totalAmountSubscriber", totalAmountSubscriber);


        intent.putExtra("tariffAmountFee", tariffAmountFee);
        intent.putExtra("feesSupprtyByString", feesSupprtyByString);

        startActivity(intent);
        CashToMarchant.this.finish();
    }

   /* private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BillpayDepositPtop.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.billPayDeposit));
        // payerAccountString = payerAccountString.replace("_", "-");
        //  payerAccountString = payerAccountString.toUpperCase();

        String[] temp = data.split("\\|");

        if (temp[2].matches(commentString)) {
            builder.setMessage(getString(R.string.billPayDepositSucessReceipt) + getString(R.string.transactionId) + temp[0]);
        } else {
            builder.setMessage(getString(R.string.billPayDepositSucessReceipt) + getString(R.string.transactionId) + temp[0] + " \n" + getString(R.string.BillpayDepositSucessReceiptAmountMaximumNote));
        }


        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                BillpayDepositPtop.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }*/

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (requestNo == 133) {
            hideProgressDialog();
            Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            System.out.println(generalResponseModel.getUserDefinedString());

            //  generateBillPayDeposit(generalResponseModel.getUserDefinedString());

            billerCode = generalResponseModel.getUserDefinedString().toString();

        }


        else if (requestNo == 106)
        {

            if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Fee Not Configured"))
            {
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }



            else if (generalResponseModel.getUserDefinedString().matches("Technical Failure"))   // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }

            else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Please try Again later"))  // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
            else if (generalResponseModel.getUserDefinedString().matches("Adapter Not Available"))   // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
            else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Template Not Found"))  // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
            else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Invalid PIN"))  // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
            else if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Entity General Error"))
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
            else if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Vendor Not Found"))
            {
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
            else if (generalResponseModel.getUserDefinedString().matches("Subscriber/Agent Not Found"))   // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }

            else {
                hideProgressDialog();
                System.out.println(generalResponseModel.getUserDefinedString());

                selectBillerName = getString(R.string.billerNameMarchantNew);
                BillerNameFetchArray = (selectBillerName + "|" + generalResponseModel.getUserDefinedString().split("\\;")[0]).split("\\|");
                billerCodeArray = (selectBillerName + "|" + generalResponseModel.getUserDefinedString().split("\\;")[1]).split("\\|");

                spinnerBillerName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, BillerNameFetchArray));
                spinnerBillerName.setSelection(0);
                spinnerBillerName.setOnItemSelectedListener(CashToMarchant.this);


            }
        }

        else if (requestNo == 114) {

          if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Fee Not Configured"))
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }

          else if (generalResponseModel.getUserDefinedString().matches("Technical Failure"))   // from server
          {
              Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
          }

          else if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Entity General Error"))
          {
              Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
          }
          else if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Vendor Not Found"))
          {
              Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
          }

            else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Please try Again later"))  // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
            else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Template Not Found"))  // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
          else if (generalResponseModel.getUserDefinedString().matches("Adapter Not Available"))   // from server
          {
              hideProgressDialog();
              Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
          }
          else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Invalid PIN "))  // from server
          {
              hideProgressDialog();
              Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
          }
          else if (generalResponseModel.getUserDefinedString().matches("Subscriber/Agent Not Found"))   // from server
            {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }

            else {

                    String[] temp = generalResponseModel.getUserDefinedString().split("\\|");

                    tariffAmountFee = temp[0];
                    System.out.print(tariffAmountFee);
                    feesSupprtyByString = temp[2];
                    System.out.print(feesSupprtyByString);

                    if (feesSupprtyByString.equalsIgnoreCase("SUB")) {

                        if (tariffAmountFee.equalsIgnoreCase("")) {
                            System.out.print(tariffAmountFee);
                        } else {
                            amountAdd = Double.parseDouble(amountString);
                            feesAdd = Double.parseDouble(tariffAmountFee);
                            totalAmount = (amountAdd + feesAdd);
                            totalAmountSubscriber = Double.toString(totalAmount);

                             fees_textview_reviewPage.setText(tariffAmountFee);
                            totalAmount_textview_reviewPage.setText(totalAmountSubscriber);
                      }
                    } else {
                        linearLayout_fees_reviewPage.setVisibility(View.GONE);
                        linearLayout_totalAmount_reviewPage.setVisibility(View.GONE);
                    }
                    showReviewPage();
            }
        }

        else if (requestNo == 130) {
            if (generalResponseModel.getResponseCode() == 0) {
                hideProgressDialog();




                if (generalResponseModel.getUserDefinedString().contains("Subscriber/Agent Not Found"))   // from server
                {
                    hideProgressDialog();
                    Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if (generalResponseModel.getUserDefinedString().matches("Technical Failure"))   // from server
                {
                    Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }

                else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Please try Again later"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }

                else if (generalResponseModel.getUserDefinedString().contains("Template Not Found"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if (generalResponseModel.getUserDefinedString().matches("Adapter Not Available"))   // from server
                {
                    hideProgressDialog();
                    Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Vendor Not Found"))
                {
                    Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Fee Not Configured"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Invalid PIN"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else {
                      showSuccessReceipt(generalResponseModel.getUserDefinedString());
                    }

            } else {
                hideProgressDialog();
                Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            //showSuccess(generalResponseModel.getUserDefinedString());
            Toast.makeText(CashToMarchant.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            CashToMarchant.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CashToMarchant.this);
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
                CashToMarchant.this.finish();
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
        } catch (Exception e) {
        }
        return ret;
    }
}

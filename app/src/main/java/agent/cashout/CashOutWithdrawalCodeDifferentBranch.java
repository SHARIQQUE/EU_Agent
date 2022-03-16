package agent.cashout;

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
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import agent.activities.OTPVerificationActivity;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptCashOutDiffrentBranch;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;
import agent.thread.ServerTaskFTP;

/**
 * Created by Sahrique on 14/03/17.
 */

public class CashOutWithdrawalCodeDifferentBranch extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    CheckBox checkBox_first, checkBox_second;
    boolean isAuthTimerStart = false;


    String[] imageFromServer, responseArray, bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    String stateNameString, tariffAmountFee;
    String amountStringCashoutMenu, mobileNumberCashOutMenu;
    EditText comment_EditText, tariffAmmount_EdittextReview_AccToCash;
    String agenttype_check,amountcashOutAmountSecurity, amountCashout_profileviewinJason, toCity;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, agentCode, spinnerCountryString, recipientBankString, recipientAccountString, recipientMobileNoString, transferBasisString, nameNumberString,
            payerBankString, transferTagString, transactionCodeString, spinnerAccountToDebitString, commentString, mpinString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    int backButton = 0, cashout_authorize = 0, requestCounter = 5;
    Double amountcashOutAmountSecurity_temp, amountStringCashoutMenu_temp, amountCashout_profileviewinJason_temp;


    Button nextButton, previousButton, button_authorize;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    ImageView imageViewPicture, imageViewSign;
    int timeCalculation = 0;
    boolean isTimeUp = true;
    private Spinner spinnerCountry, recipientbankSpinner, recipientAccTypeSpinner, payerBankSpinner, spinnerAccountToDebit,
            transferTagSpinner, transferBasisSpinner;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash, scrollView_waitingtoSubscriberAuthenticate;
    private AutoCompleteTextView name_No_EditText, amountEditText, transactionCodeEditText, mpinEditText, recipient_AccountNo_EditText,
            recipient_MobileNo_EditText;
    private TextView textview_waitingForSubscriberAuthenticate, idproof_number_textview, idproof_issueDate_textview, idproof_issueplace_textview, textview_agentCode, idproof_textView, subscriberName_textView, recipientCountryTxtView_Review, transferBasisTxtView_Review,
            recipientNameNoTitleTxtView_Review, recipientNameNoTxtView_Review,
            recipientAccountTypeTxtView_Review, payerBankTxtView_Review, titleTextView,
            payerAccountTypeTxtView_Review, trasactionCodeTxtView_Review, amountTxtView_Review, recipientBankTxtView_Review, transferTagTxtView_Review;
    private ProgressDialog mDialog;
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
                DataParserThread thread = new DataParserThread(CashOutWithdrawalCodeDifferentBranch.this, mComponentInfo, CashOutWithdrawalCodeDifferentBranch.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;
    String subscriberNameString, idproofString;
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


        setContentView(R.layout.cashout_withdrawal_code);

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
        mToolbar.setTitle(getString(R.string.useaWthdrawalCodeSupplied));
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


            mobileNumberCashOutMenu = mComponentInfo.getmSharedPreferences().getString("mobileNumberCashOutMenu", "");
            amountStringCashoutMenu = mComponentInfo.getmSharedPreferences().getString("amountCashOutMenu", "");
            amountcashOutAmountSecurity = mComponentInfo.getmSharedPreferences().getString("amountcashOutSecurityString", "");

            scrollView_waitingtoSubscriberAuthenticate = (ScrollView) findViewById(R.id.scrollView_waitingtoSubscriberAuthenticate);
            textview_waitingForSubscriberAuthenticate = (TextView) findViewById(R.id.textview_waitingForSubscriberAuthenticate);
            button_authorize = (Button) findViewById(R.id.button_authorize);
            button_authorize.setOnClickListener(this);

        } catch (Exception e) {

            CashOutWithdrawalCodeDifferentBranch.this.finish();
        }

        nextButton = (Button) findViewById(R.id.nextButton_MoneyTransfer);
        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton.setVisibility(View.VISIBLE);
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

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        imageViewSign = (ImageView) findViewById(R.id.imageViewSign);

        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(CashOutWithdrawalCodeDifferentBranch.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);
        recipient_MobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.nameNumberEditText_AccToCash);
        recipient_MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));


        idproof_textView = (TextView) findViewById(R.id.idproof_textView);
        subscriberName_textView = (TextView) findViewById(R.id.subscriberName_textView);


        transactionCodeEditText = (AutoCompleteTextView) findViewById(R.id.transactionEditText_AccToCash);
        transactionCodeEditText.setOnEditorActionListener(this);

        tariffAmmount_EdittextReview_AccToCash = (EditText) findViewById(R.id.tariffAmmount_EdittextReview_AccToCash);


        amountEditText = (AutoCompleteTextView) findViewById(R.id.amountEditText_AccToCash);
        amountEditText.setOnEditorActionListener(this);


        textview_agentCode = (TextView) findViewById(R.id.textview_agentCode);
        textview_agentCode.setText(agentCode);
        comment_EditText = (EditText) findViewById(R.id.comment_EditText);
        checkBox_first = (CheckBox) findViewById(R.id.checkBox_first);
        checkBox_second = (CheckBox) findViewById(R.id.checkBox_second);
        idproof_number_textview = (TextView) findViewById(R.id.idproof_number_textview);
        idproof_issueplace_textview = (TextView) findViewById(R.id.idproof_issueplace_textview);
        idproof_issueDate_textview = (TextView) findViewById(R.id.idproof_issueDate_textview);


        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_SHORT).show();
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


        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 0) {
            String[] data = bankAccounts.split("\\;");

            ArrayList<String> accountList = new ArrayList<String>();
            ArrayList<String> accountCodeList = new ArrayList<String>();
            accountList.add(getString(R.string.pleaseSelectAccountCredit));
            accountCodeList.add(getString(R.string.pleaseSelectAccountCredit));

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
            payerBankAccountsArray[0] = getString(R.string.pleaseSelectAccountCredit);
            payerAccountCodeArray = new String[1];
            payerAccountCodeArray[0] = getString(R.string.pleaseSelectAccountCredit);
        }

        //  spinnerAccountToDebit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));

        titleTextView = (TextView) findViewById(R.id.titleTextView_AccToCash);

        recipientCountryTxtView_Review = (TextView) findViewById(R.id.recipientCountry_TxtView_Review_AccToCash);
        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);
        recipientNameNoTxtView_Review = (TextView) findViewById(R.id.recipientNameNo_TxtView_Review_AccToCash);
        amountTxtView_Review = (TextView) findViewById(R.id.amount_TxtView_Review_AccToCash);
        trasactionCodeTxtView_Review = (TextView) findViewById(R.id.transactionCode_TxtView_Review_AccToCash);
        payerAccountTypeTxtView_Review = (TextView) findViewById(R.id.payerAccountType_TxtView_Review_AccToCash);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText__Review_AccToCash);
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

        pictureSignatureServerRequest_cashoutAmount_authorize();


    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                recipient_MobileNo_EditText.setText("");
                recipient_MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));
                // recipient_MobileNo_EditText.setFilters(null);
                recipient_MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                recipient_MobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNoAcctoCashOut), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                recipient_MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                recipient_MobileNo_EditText.setFilters(digitsfilters);
                recipient_MobileNo_EditText.setText("");


            } else if (i == 2) {
                recipient_MobileNo_EditText.setText("");
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
                recipient_MobileNo_EditText.setHint(getString(R.string.pleaseentername));
                recipient_MobileNo_EditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                recipient_MobileNo_EditText.setFilters(digitsfilters);
                recipient_MobileNo_EditText.setText("");
            }
        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
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

        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }

    }


    void proceedTariffAmount() {
        if (new InternetCheck().isConnected(CashOutWithdrawalCodeDifferentBranch.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            // callApi("getTarifInJSON",requestData,114);

            new ServerTask(mComponentInfo, CashOutWithdrawalCodeDifferentBranch.this, mHandler, requestData, "getTarifInJSON", 114).start();
        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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

            countryObj.put("destination", mobileNumberCashOutMenu);

            countryObj.put("amount", amountStringCashoutMenu);
            countryObj.put("transtype", "CASHOUT");
            countryObj.put("confcode", transactionCodeString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            String fromCity = mComponentInfo.getmSharedPreferences().getString("state", "");

            countryObj.put("fromcity", fromCity);   //  change from server
            countryObj.put("tocity", toCity);     //  Change from Server

            countryObj.put("comments", "");
            countryObj.put("udv1", "");
            countryObj.put("accountType", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            //  countryObj.put("destination", mobileNumberCashOutMenu);


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

    /*private void showAccToCashReview() {

        CashOutWithdrawalCodeDifferentBranch.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();


                proceedTariffAmount();

            }
        });
    }
*/
    public void pictureSignatureServerRequest() {
        showProgressDialog(getString(R.string.pleasewait));
        String requestData = generatePictureSign();

        // callApi("getViewProfileInJSON",requestData,148);

        new ServerTask(mComponentInfo, CashOutWithdrawalCodeDifferentBranch.this, mHandler, requestData, "getViewProfileInJSON", 148).start();
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
            countryObj.put("source", mobileNumberCashOutMenu);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private boolean validateCashOut_PartI() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

           /* int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashOut), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);
            }

            nameNumberString = recipient_MobileNo_EditText.getText().toString().trim();
            if (nameNumberString.length() > lengthToCheck) {
                if (transferBasisposition == 1) {
                    if (nameNumberString.length() == ++lengthToCheck) {
                        nameNumberString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + nameNumberString;
                    } else {
                        Toast.makeText(CashOutWithdrawalCode.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }*/
            //  if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {

            transactionCodeString = transactionCodeEditText.getText().toString().trim();
            if (transactionCodeString.length() == 4) {

                  /*  amountStringCashoutMenu = amountEditText.getText().toString().trim();
                    if (amountStringCashoutMenu.length() > 0 && validateAmount(amountStringCashoutMenu)) {
                 */
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
                   /* } else {
                        Toast.makeText(CashOutWithdrawalCode.this, getString(R.string.pleaseenteramount), Toast.LENGTH_LONG).show();
                    }*/

            } else {
                Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.transactionCode), Toast.LENGTH_LONG).show();
            }
              /*  } else {
                    Toast.makeText(CashOutWithdrawalCode.this, getString(R.string.pleaseSelectAccountCredit), Toast.LENGTH_LONG).show();
                }*/
           /* } else {
                Toast.makeText(CashOutWithdrawalCode.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }*/
            /*} else {
                Toast.makeText(CashOutWithdrawalCode.this, getString(R.string.pleaseselectsendmode), Toast.LENGTH_LONG).show();
              }*/

        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetails() {
        if (!isReview) {
            if (validateCashOut_PartI()) {

                amountcashOutAmountSecurity_temp = Double.parseDouble(amountcashOutAmountSecurity);
                amountStringCashoutMenu_temp = Double.parseDouble(amountStringCashoutMenu);   // from server country api

                amountCashout_profileviewinJason_temp = Double.parseDouble(amountCashout_profileviewinJason);   // from server Profile in Json API

                if (amountCashout_profileviewinJason_temp < amountStringCashoutMenu_temp) {

                    if (agenttype_check.equalsIgnoreCase("SUB"))
                    {
                        authorizeInitiateRequest();
                    }
                    else
                    {
                        pictureSignatureServerRequest();
                    }
                } else {
                    pictureSignatureServerRequest();
                }
            }
        } else {
            if (validateCashOut_PartII()) {
                CashOutTransfer();
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.nextButton_MoneyTransfer:
                validateDetails();
                break;

            case R.id.button_authorize:

                authorizeRequest();

                break;
        }
    }

    private void CashOutTransfer() {

        if (new InternetCheck().isConnected(CashOutWithdrawalCodeDifferentBranch.this)) {


            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateAccToCashData();
            new ServerTask(mComponentInfo, CashOutWithdrawalCodeDifferentBranch.this, mHandler, requestData, "getCashOutTransaction", 121).start();

        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }
    }


    private String generateAccToCashData() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

            /*
            {"agentCode":"237673303089","pin":"2A33F994D57FD64740FBC7BC0A42DE47","source":"237673303089",
             "comments":"sms","pintype":"MPIN","vendorcode":"MICR","clienttype":"GPRS","amount":"1000",
             "destination":"237000271015","requestcts":"25/05/2016 18:01:51","udv2":"NOSMS","transcode":"SAJD"}
          */


            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("source", data[0].trim());
            countryObj.put("source", agentCode);
            countryObj.put("comments", commentString);
            //countryObj.put("requestcts", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("amount", amountStringCashoutMenu);
            countryObj.put("destination", mobileNumberCashOutMenu);
            countryObj.put("requestcts", "25/05/2016 18:01:51");
            countryObj.put("udv2", "");
            countryObj.put("transcode", transactionCodeString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private boolean validateCashOut_PartII() {

        boolean ret = false;

        mpinString = mpinEditText.getText().toString().trim();
        commentString = comment_EditText.getText().toString().trim();

        if (checkBox_first.isChecked() || checkServerimage()) {

            if (checkBox_second.isChecked()) {

                // amountcashOutAmountSecurity="10000.50";
                //  amountStringCashoutMenu="20000";


                if (amountcashOutAmountSecurity_temp < amountStringCashoutMenu_temp) {

                    if (commentString.equalsIgnoreCase("")) {
                        Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseEnterComment), Toast.LENGTH_LONG).show();
                    } else {
                        if (mpinString.length() == 4) {

                            ret = true;

                        } else {
                            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    if (mpinString.length() == 4) {

                        ret = true;


                    } else {
                        Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseSelectCheckbox_second), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseSelectCheckbox_first), Toast.LENGTH_LONG).show();
        }

        return ret;
    }


    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("spinnerCountryString", spinnerCountryString);

        mComponentInfo.getmSharedPreferences().edit().putString("stateNameString", stateNameString).commit();

        Intent intent = new Intent(CashOutWithdrawalCodeDifferentBranch.this, SucessReceiptCashOutDiffrentBranch.class);
        intent.putExtra("data", data);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        startActivity(intent);
        CashOutWithdrawalCodeDifferentBranch.this.finish();
    }

   /* private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashOutWithdrawalCode.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.cashOutDiffrentBranch));
        // payerAccountString = payerAccountString.replace("_", "-");
        //  payerAccountString = payerAccountString.toUpperCase();

        String[] temp = data.split("\\|");

        if (temp[2].matches("comments")) {
            builder.setMessage(getString(R.string.cashOutDiffrentSucessReceipt) + getString(R.string.transactionId) + temp[0]);
        } else {
            builder.setMessage(getString(R.string.cashOutDiffrentSucessReceipt) + getString(R.string.transactionId) + temp[0] + " \n" + getString(R.string.cashOutSucessReceiptAmountMaximumNote));
        }


        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                CashOutWithdrawalCode.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }*/

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0 || requestNo == 148 || requestNo == 114) {

            if (requestNo == 121) {

                hideProgressDialog();


                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(CashOutWithdrawalCodeDifferentBranch.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }

            } else if (requestNo == 148) {
                try {

                    String responseData = generalResponseModel.getUserDefinedString();

                    responseArray = responseData.split("\\|");

                    subscriberNameString = responseArray[5];
                    idproofString = responseArray[6];
                    stateNameString = responseArray[8];
                    toCity = responseArray[4];

                    proceedTariffAmount();

                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

                    Intent intent4 = new Intent(CashOutWithdrawalCodeDifferentBranch.this, CashOutMenu.class);
                    startActivity(intent4);
                    CashOutWithdrawalCodeDifferentBranch.this.finish();

                }


            } else if (requestNo == 215) {

                try {

                    String[] amountFrom_viewProfile = generalResponseModel.getUserDefinedString().split("\\|");
                    amountCashout_profileviewinJason = amountFrom_viewProfile[1];
                    agenttype_check=amountFrom_viewProfile[2]; // check BA FR

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestNo == 114) {


                if (generalResponseModel.getResponseCode() == 0) {


                    String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                    tariffAmountFee = temp[0];
                    System.out.print(tariffAmountFee);


                    tariffAmmount_EdittextReview_AccToCash.setText(tariffAmountFee);
                    tariffAmmount_EdittextReview_AccToCash.setEnabled(false);
                    //   showAccToCashReview(tariffAmountFee);


                    titleTextView.setText(getString(R.string.pleasereviewdetail));
                    input_SV_AccToCash.setVisibility(View.GONE);
                    review_SV_AccToCash.setVisibility(View.VISIBLE);
                    recipientCountryTxtView_Review.setText(spinnerCountryString);

                    idproof_textView.setText(idproofString);
                    subscriberName_textView.setText(subscriberNameString);

                    idproof_number_textview.setText(responseArray[9]);
                    idproof_issueplace_textview.setText(responseArray[10]);
                    idproof_issueDate_textview.setText(responseArray[11]);


                    transferBasisTxtView_Review.setText(transferBasisString);
                    recipientNameNoTxtView_Review.setText(mobileNumberCashOutMenu);
                    trasactionCodeTxtView_Review.setText(transactionCodeString);
                    amountTxtView_Review.setText(amountStringCashoutMenu);
                    // payerAccountTypeTxtView_Review.setText(spinnerAccountToDebitString);
                    nextButton.setText(getString(R.string.submit));
                    isReview = true;
                    mpinEditText.requestFocus();

                    imageDownload();  // comment   Remove 14 dec 2018


                } else {
                    Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

                    Intent intent4 = new Intent(CashOutWithdrawalCodeDifferentBranch.this, CashOutMenu.class);
                    startActivity(intent4);
                    CashOutWithdrawalCodeDifferentBranch.this.finish();
                }
            } else if (requestNo == 204) {


                String[] authorizeResponse = generalResponseModel.getUserDefinedString().split("\\|");

                if (generalResponseModel.getResponseCode() == 234 || generalResponseModel.getResponseCode() == 0) {


                    previousButton.setVisibility(View.GONE);
                    button_authorize.setVisibility(View.GONE);
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);
                    input_SV_AccToCash.setVisibility(View.GONE);
                    mpinEditText.setText("");

                    review_SV_AccToCash.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    titleTextView.setVisibility(View.VISIBLE);


                    pictureSignatureServerRequest();

                } else if (!isTimeUp) {
                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, ((timeCalculation == 0) ? 20000 : 10000));

                } else {
                    Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, generalResponseModel.getUserDefinedString(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(CashOutWithdrawalCodeDifferentBranch.this, CashOutMenu.class);
                    startActivity(intent);
                    CashOutWithdrawalCodeDifferentBranch.this.finish();
                }


            } else if (requestNo == 200) {

                scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);
                mpinEditText.setText("");
                review_SV_AccToCash.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                titleTextView.setVisibility(View.VISIBLE);

                try {

                    imageFromServer = generalResponseModel.getUserDefinedString().split("\\|");

                    byte[] imageBytes1 = android.util.Base64.decode(imageFromServer[5], android.util.Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                    imageViewSign.setImageBitmap(decodedImage);

                    byte[] imageBytes2 = android.util.Base64.decode(imageFromServer[4], android.util.Base64.DEFAULT);
                    Bitmap decodedImage2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);
                    imageViewPicture.setImageBitmap(decodedImage2);

                    if (imageFromServer == null || imageFromServer[4] == null || imageFromServer[4].equalsIgnoreCase("") || imageFromServer[4].equalsIgnoreCase("null")) {
                        checkBox_first.setEnabled(false);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                hideProgressDialog();
                Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
        } else {

            // hideProgressDialog();

            if (requestNo == 203) {


                //  from server
                // 404="Authorization Initiated";  //

                //  405="First Approved the Authorize Agent/Subscriber";
                // 231=Authorization, Already Initiated

                if (generalResponseModel.getResponseCode() == 234 || generalResponseModel.getResponseCode() == 0) {
                    pictureSignatureServerRequest();
                } else if (generalResponseModel.getResponseCode() == 404 || generalResponseModel.getResponseCode() == 405 || generalResponseModel.getResponseCode() == 231) { // from server

                    review_SV_AccToCash.setVisibility(View.GONE);
                    nextButton.setVisibility(View.GONE);
                    previousButton.setVisibility(View.GONE);
                    titleTextView.setVisibility(View.GONE);
                    input_SV_AccToCash.setVisibility(View.GONE);
                    button_authorize.setVisibility(View.GONE);
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(CashOutWithdrawalCodeDifferentBranch.this, R.anim.blink));
                    backButton = 1;
                    cashout_authorize = 1;  // for

                    try {
                        requestCounter = Integer.valueOf(generalResponseModel.getUserDefinedString().split("\\|")[4]);
//                        requestCounter = ++requestCounter;
                    } catch (Exception e) {

                        requestCounter = 5;
                    }

                    Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, ((timeCalculation == 0) ? 20000 : 10000));

                } else {
                    Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
            }

            else if (requestNo == 200) // if image downlaod fail OR  Download source folder not exit
            {
                scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);
                mpinEditText.setText("");
                review_SV_AccToCash.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                titleTextView.setVisibility(View.VISIBLE);
                checkBox_first.setEnabled(false);

            }

            else if (requestNo == 204)  //   Auth response
            {
                // scrollView_createAgent_firstPart.setVisibility(View.GONE);

                String[] authorizeResponse = generalResponseModel.getUserDefinedString().split("\\|");

               if (generalResponseModel.getResponseCode() == 234 || generalResponseModel.getResponseCode() == 0) {
                    pictureSignatureServerRequest();
                }


                else if (!isTimeUp) {
                    hideKeyboard();
                    hideProgressDialog();
                    review_SV_AccToCash.setVisibility(View.GONE);
                    nextButton.setVisibility(View.GONE);
                    previousButton.setVisibility(View.GONE);
                    titleTextView.setVisibility(View.GONE);
                    input_SV_AccToCash.setVisibility(View.GONE);
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(CashOutWithdrawalCodeDifferentBranch.this, R.anim.blink));
                    backButton = 1;
                    cashout_authorize = 1;  // for

                    Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, ((timeCalculation == 0) ? 20000 : 10000));

                } else {

                  //  button_authorize.setVisibility(View.VISIBLE);

                  //  textview_waitingForSubscriberAuthenticate.setText(getString(R.string.timeisClose));
                   // textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(CashOutWithdrawalCodeDifferentBranch.this, R.anim.blink));
                    backButton = 1;

                   Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.timeOutComplete), Toast.LENGTH_SHORT).show();
                   CashOutWithdrawalCodeDifferentBranch.this.finish();



               }



            } else {

                hideProgressDialog();
                Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                // Subscriber/Agent Not Found
            }
        }
    }

    Handler authRequestDelayHandler = new Handler();

    private Runnable sendData_AuthRequestHandler_Runnable = new Runnable() {
        public void run() {
            try {
                //prepare and send the data here..

                timeCalculation = ++timeCalculation;


                authRequestDelayHandler.removeCallbacks(sendData_AuthRequestHandler_Runnable);
                isTimeUp = false;


                if (timeCalculation >= requestCounter) {   //  requestCounter from Server by default 5 if s
                    isTimeUp = true;
                }
                authorizeRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void pictureSignatureServerRequest_cashoutAmount_authorize() {

        if (new InternetCheck().isConnected(CashOutWithdrawalCodeDifferentBranch.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generatePictureSign();
            new ServerTask(mComponentInfo, CashOutWithdrawalCodeDifferentBranch.this, mHandler, requestData, "getViewProfileInJSON", 215).start();
        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkServerimage() {

        boolean ret = false;

        try {


            if (imageFromServer == null || imageFromServer[4] == null || imageFromServer[4].equalsIgnoreCase("") || imageFromServer[4].equalsIgnoreCase("null")) {

                checkBox_first.setChecked(false);
                checkBox_first.setEnabled(false);

                ret = true;

            } else {
                ret = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }


    void imageDownload() {

        if (new InternetCheck().isConnected(CashOutWithdrawalCodeDifferentBranch.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateImageDownloadingRequest();

            new ServerTaskFTP(mComponentInfo, CashOutWithdrawalCodeDifferentBranch.this, mHandler, requestData, "downloadimage", 200).start();

            //  vollyRequestApi_FTP("downloadimage",requestData, 200);

        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    //////////////////////////////////////////   AUTH INITIATE     ////////////////////////////////////////

    private void authorizeInitiateRequest() {

        if (new InternetCheck().isConnected(CashOutWithdrawalCodeDifferentBranch.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            vollyRequestApi_serverTask("authorize", authorizeInitiateparsingData(), 203);

        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private String authorizeInitiateparsingData() {
        String jsonString = "";


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("destination", mobileNumberCashOutMenu);
            countryObj.put("action", "INITIATE");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");

            countryObj.put("cashoutaction", "CASHOUT");
            countryObj.put("amount", amountStringCashoutMenu);
            countryObj.put("transcode", transactionCodeString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    private void authorizeRequest() {


        if (new InternetCheck().isConnected(CashOutWithdrawalCodeDifferentBranch.this)) {

            showProgressDialog(getString(R.string.pleasewait));

            //  new ServerTask(mComponentInfo, ViewProfile.this, mHandler, generateAuthorizeData(), "authorize", 204).start();

            vollyRequestApi_serverTask("authorize", generateAuthorizeData(), 204);


        } else {
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }
    }


    private String generateAuthorizeData() {
        String jsonString = "";


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("destination", mobileNumberCashOutMenu);
            countryObj.put("action", "INITIATE");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");

            countryObj.put("cashoutaction", "CASHOUT");
            countryObj.put("amount", amountStringCashoutMenu);
            countryObj.put("transcode", transactionCodeString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    void vollyRequestApi_serverTask(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServerTask.baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(CashOutWithdrawalCodeDifferentBranch.this, mComponentInfo, CashOutWithdrawalCodeDifferentBranch.this, requestCode, response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(CashOutWithdrawalCodeDifferentBranch.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////


    private String generateImageDownloadingRequest() {
        String jsonString = "";


        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", mobileNumberCashOutMenu);

            countryObj.put("isagentimage", "Y");
            countryObj.put("issignature", "Y");

            countryObj.put("isidfrontimage", "N");
            countryObj.put("isidbackimage", "N");
            countryObj.put("isformimage", "N");
            countryObj.put("isbillimage", "N");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("requestcts", "");

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
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
            CashOutWithdrawalCodeDifferentBranch.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CashOutWithdrawalCodeDifferentBranch.this);
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


            if (backButton == 0) {

                Intent intent3 = new Intent(CashOutWithdrawalCodeDifferentBranch.this, CashOutMenu.class);   // CashOut Withdrawal
                startActivity(intent3);
                CashOutWithdrawalCodeDifferentBranch.this.finish();

            } else if (backButton == 1) {

                Intent intent3 = new Intent(CashOutWithdrawalCodeDifferentBranch.this, CashOutMenu.class);   // CashOut Withdrawal
                startActivity(intent3);
                CashOutWithdrawalCodeDifferentBranch.this.finish();


                backButton = 0;
            } else {
                CashOutWithdrawalCodeDifferentBranch.this.finish();
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

                amountStringCashoutMenu = "" + amt;
            }
        } catch (Exception e) {
        }
        return ret;
    }
}

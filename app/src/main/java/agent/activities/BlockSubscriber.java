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

import java.util.ArrayList;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by Sahrique on 14/03/17.
 */

public class BlockSubscriber extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String tariffAmountFee, countrySelectedCode, commentString;
    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    TextView subscriberName_textView, cityReceipt;
    ComponentMd5SharedPre mComponentInfo;

    String stateNameString, agentName, agentCode, subscriberNameString,
            spinnerCountryString, recipientBankString, recipientAccountString,
            recipientMobileNoString, transferBasisString, subscriberNumberString,
            payerBankString, transferTagString, amountString,
            mpinString, countrySelectionString = "";
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
                DataParserThread thread = new DataParserThread(BlockSubscriber.this, mComponentInfo, BlockSubscriber.this, message.arg1, message.obj.toString());

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


        setContentView(R.layout.block_subscriber);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();


        transferTagArray = getResources().getStringArray(R.array.TransferTag);

        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MoneyTransfer);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

      //  countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.blockSubscriber));
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

            BlockSubscriber.this.finish();
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
        spinnerCountry.setEnabled(false);
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
        transferBasisSpinner.setOnItemSelectedListener(BlockSubscriber.this);

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
                    Toast.makeText(BlockSubscriber.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_LONG).show();
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
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpinEditText);
        mpinEditText.setOnEditorActionListener(this);

        comment_EdittextReview_AccToCash = (TextView) findViewById(R.id.comment_EdittextReview_AccToCash);

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
            Toast.makeText(BlockSubscriber.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

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
                        Toast.makeText(BlockSubscriber.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                mpinString = mpinEditText.getText().toString().trim();
                if (mpinString.length() == 4) {

                    ret = true;

                } else {
                    Toast.makeText(BlockSubscriber.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(BlockSubscriber.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(BlockSubscriber.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }


        return ret;


    }

    public void validateDetails() {
        if (!isReview) {

            if (validateAccToCash_PartI()) {

                serverRequestBlockSubscriber();

            }
        }
    }

    private void serverRequestBlockSubscriber() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(BlockSubscriber.this)) {
            showProgressDialog(getString(R.string.pleasewait));


            String requestData = generateBlockSubscriber();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            // callApi("BlocksubscriberJson",requestData,165);

             new ServerTask(mComponentInfo, BlockSubscriber.this, mHandler, requestData, "BlocksubscriberJson", 165).start();


        } else {
            Toast.makeText(BlockSubscriber.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(BlockSubscriber.this,mComponentInfo,BlockSubscriber.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(BlockSubscriber.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(BlockSubscriber.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }

    private String generateBlockSubscriber() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            /*

            {"agentcode":"237000271009","source":"237000271015","pin":"E7DC69989C587511B6BA545737ABEB98",
                    "pintype":"MPIN","requestcts":"15/12/2017 13:01:51","vendorcode":"MICR",
              "clienttype":"GPRS","comments":"portal comments"}
            */        // Chandan request 15 December


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("source", subscriberNumberString);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("comments", "comments");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 165) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(BlockSubscriber.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString());
                }
            }


        } else {
            hideProgressDialog();
            Toast.makeText(BlockSubscriber.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_MoneyTransfer:
                validateDetails();
                break;
        }
    }


    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BlockSubscriber.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.blockSubscriber));

        String[] temp = data.split("\\|");

        builder.setMessage(getString(R.string.blockSubscriberSucessReceipt) + getString(R.string.transactionId) + temp[0]);

        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                BlockSubscriber.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            BlockSubscriber.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }


    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(BlockSubscriber.this);
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
                BlockSubscriber.this.finish();
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

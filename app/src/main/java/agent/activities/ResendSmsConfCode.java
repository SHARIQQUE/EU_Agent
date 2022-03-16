/*
package agent.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.util.Base64;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

*/
/**
 * Created by Sahrique on 14/03/17.
 *//*


public class ResendSmsConfCode extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String[] bankSelectionArray, transferTagArray, operationCodeArray, accountTypeArray;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, spinnerTransactionTypeString, confCodeString, agentCode, spinnerCountryString, transferBasisString, subscriberNoString,
            amountString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button nextButton, previousButton, submitButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner spinnerCountry, spinnerTransactionType;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;
    private AutoCompleteTextView mpinEditText, recipient_MobileNo_EditText, confcode_EditText_resendSms;
    private TextView titleTextView,titleTextViewClearAll;
    private ProgressDialog mDialog;
    ImageView imageViewPicture, imageViewSign;
    LinearLayout linearLayoutImageView;
    boolean selectKeyboard = false;
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
                DataParserThread agent.thread = new DataParserThread(ResendSmsConfCode.this, mComponentInfo, ResendSmsConfCode.this, message.arg1, message.obj.toString());
                agent.thread.execute();
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


                if (selectKeyboard == false) {

                    validateDetails();
                }
                else {

                    validateDetailsSecondPhase();
                }

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



        setContentView(R.layout.resendsms_confcode);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_resendSms);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.resendSmsconfcodeNew));
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

            ResendSmsConfCode.this.finish();
        }

        nextButton = (Button) findViewById(R.id.nextButton_ResendSms);
        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.GONE);


        submitButton = (Button) findViewById(R.id.submit_ResendSms);
        submitButton.setOnClickListener(this);


        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);

        operationCodeArray = getResources().getStringArray(R.array.TxnTypeSmsConfcode);
        spinnerTransactionType = (Spinner) findViewById(R.id.spinnerTransactionType);
        recipient_MobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.nameNumberEditText_AccToCash);
        recipient_MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));

        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_resendSms);
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



        titleTextView = (TextView) findViewById(R.id.titleTextViewResendSmsDetails);

        titleTextViewClearAll = (TextView) findViewById(R.id.titleTextViewClearAll);
        titleTextViewClearAll.setOnClickListener(this);

        confcode_EditText_resendSms = (AutoCompleteTextView) findViewById(R.id.confcode_EditText_resendSms);
        confcode_EditText_resendSms.setOnEditorActionListener(this);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        imageViewSign = (ImageView) findViewById(R.id.imageViewSign);
        linearLayoutImageView = (LinearLayout) findViewById(R.id.linearLayoutImageView);

   }

    public void pictureSignatureServerRequest() {
        showProgressDialog(getString(R.string.pleasewait));
        String requestData = generatePictureSign();
       // callApi("getViewProfileInJSON",requestData,146);

        new ServerTask(mComponentInfo, ResendSmsConfCode.this, mHandler, requestData, "getViewProfileInJSON", 146).start();
    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread agent.thread = new DataParserThread(ResendSmsConfCode.this,mComponentInfo,ResendSmsConfCode.this,requestCode,response.toString());

                            agent.thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(ResendSmsConfCode.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(ResendSmsConfCode.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }

    private String generatePictureSign() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

           */
/* {"agentCode":"237000271016","pin":"46A75E89BC40FDD2F70895AB710D5A22",
            "source":"237000271016","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS"}
              *//*

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", subscriberNoString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerCountry:
                setInputType(1);
                break;

            case R.id.spinnerTransactionType:
                setInputType(i);
                break;


        }
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
                recipient_MobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNoAcctoCashOut_new), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
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
            Toast.makeText(ResendSmsConfCode.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
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

    int transferBasisposition = 0;
    private boolean validationSendSms() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();
            subscriberNoString = recipient_MobileNo_EditText.getText().toString().trim();
           transferBasisposition = spinnerCountry.getSelectedItemPosition();
            spinnerTransactionTypeString = operationCodeArray[spinnerTransactionType.getSelectedItemPosition()];
            mpinString = mpinEditText.getText().toString().trim();
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashOut), lengthToCheck + 1 + "");

            } else {
               */
/* transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);*//*

                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashOut), lengthToCheck + 1 + "");
            }

            if (subscriberNoString.length() > lengthToCheck) {
                    if (transferBasisposition == 1) {
                        if (subscriberNoString.length() == ++lengthToCheck) {
                            subscriberNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + subscriberNoString;
                        } else {
                            Toast.makeText(ResendSmsConfCode.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }

                    if (spinnerTransactionType.getSelectedItemPosition() != 0) {
                        if (mpinString.length() == 4) {
                            ret = true;
                        } else {
                            Toast.makeText(ResendSmsConfCode.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(ResendSmsConfCode.this, getString(R.string.PleaseSelectTransactionType), Toast.LENGTH_LONG).show();
                    }


            } else {
                    Toast.makeText(ResendSmsConfCode.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                }


            } else {
                Toast.makeText(ResendSmsConfCode.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
            }
            return ret;
        }

    public void validateDetails() {
        selectKeyboard = false;

        if (validationSendSms()) {
            ServerRequestSendSms();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_ResendSms:
                validateDetails();
                break;

            case R.id.submit_ResendSms:
                validateDetailsSecondPhase();
                break;

            case R.id.titleTextViewClearAll:
                  ClearAll();
                break;
        }
    }

    private void ServerRequestSendSms() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(ResendSmsConfCode.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateResendSmsConfcode();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

          //  callApi("otpForResendSms",requestData,136);


            new ServerTask(mComponentInfo, ResendSmsConfCode.this, mHandler, requestData, "otpForResendSms", 136).start();


        } else {
            Toast.makeText(ResendSmsConfCode.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateResendSmsConfcode() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

           */
/* {"agentcode":"237000271009","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS",
                    "transtype":"REMTSEND","pin":"96BD339D2D987EF887E3A3F151F87059","pintype":"MPIN","source":"237000271015"}
            *//*

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", spinnerTransactionTypeString);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("source", subscriberNoString);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResendSmsConfCode.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.resendSmsConfcode));

        nextButton.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);

        recipient_MobileNo_EditText.setEnabled(false);
        spinnerCountry.setEnabled(false);
        spinnerTransactionType.setEnabled(false);

        confcode_EditText_resendSms.setVisibility(View.VISIBLE);
        confcode_EditText_resendSms.requestFocus();
        mpinEditText.setText("");
        titleTextViewClearAll.setVisibility(View.VISIBLE);
        linearLayoutImageView.setVisibility(View.VISIBLE);

        selectKeyboard = true;
    }

    public void ClearAll()
    {
        spinnerCountry.setEnabled(true);
        recipient_MobileNo_EditText.setEnabled(true);
        spinnerTransactionType.setEnabled(true);
        confcode_EditText_resendSms.setVisibility(View.GONE);

       nextButton.setVisibility(View.VISIBLE);
       submitButton.setVisibility(View.GONE);

        recipient_MobileNo_EditText.setText("");
        confcode_EditText_resendSms.setText("");
        spinnerTransactionType.setSelection(0);
        mpinEditText.setText("");
        recipient_MobileNo_EditText.requestFocus();
        titleTextViewClearAll.setVisibility(View.GONE);


    }


    private void showSuccessSecondPhase(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResendSmsConfCode.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.resendSmsConfcode));


       String[] temp= data.split("\\|");

        builder.setMessage(getString(R.string.rersendSmsSucessReceipt));

        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                ResendSmsConfCode.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }

    void validateDetailsSecondPhase() {
        selectKeyboard = true;
        if (validationSecondConfCodeMpin()) {
            ServerRequestSecondPhase();
        }
    }

    boolean validationSecondConfCodeMpin() {
        boolean ret = false;

        confCodeString = confcode_EditText_resendSms.getText().toString().trim();
        if (confCodeString.length() >=4) {

            mpinString = mpinEditText.getText().toString().trim();
            if (mpinString.length() == 4) {

                ret = true;

            } else {
                Toast.makeText(ResendSmsConfCode.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(ResendSmsConfCode.this, getString(R.string.pleaseEnterConfCodeOTP), Toast.LENGTH_LONG).show();
        }

        return ret;
    }


    private void ServerRequestSecondPhase() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(ResendSmsConfCode.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateResendSmsScondPhase();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

         //   callApi("resendsmsfor_agent",requestData,137);

           new ServerTask(mComponentInfo, ResendSmsConfCode.this, mHandler, requestData, "resendsmsfor_agent", 137).start();

        } else {
            Toast.makeText(ResendSmsConfCode.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }



    private String generateResendSmsScondPhase() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            */
/*{"agentcode":"237000271009","source":"237000271015","pin":"96BD339D2D987EF887E3A3F151F87059",
               "pintype":"MPIN","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":
                "GPRS","transtype":"REMTSEND","confcode":"HQO8OY"}*//*


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", spinnerTransactionTypeString);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("confcode", confCodeString);
            countryObj.put("source", subscriberNoString);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 136) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(ResendSmsConfCode.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    pictureSignatureServerRequest();

                }

            }

            else if (requestNo == 137) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(ResendSmsConfCode.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessSecondPhase(generalResponseModel.getUserDefinedString());
                }
            }

            else if (requestNo == 146) {
                String responseData = generalResponseModel.getUserDefinedString();

                String[] responseArray = responseData.split("\\|");

                if (responseArray[0].matches("pictureSign")) {

                    byte[] imageBytes1 = Base64.decode(responseArray[1], Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                    imageViewSign.setImageBitmap(decodedImage);

                    byte[] imageBytes2 = Base64.decode(responseArray[2], Base64.DEFAULT);
                    Bitmap decodedImage2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);
                    imageViewPicture.setImageBitmap(decodedImage2);


                   */
/* if (getStreamFromResponse(responseArray[1], responseArray[2]) != null) {
                        getByteByString(text);
                        setImage();
                      //  showSuccessSecondPhase(generalResponseModel.getUserDefinedString());

                        showSuccess(generalResponseModel.getUserDefinedString());

                    } else {
                        //Toast.makeText(this, "Image Data is not in proper format", Toast.LENGTH_SHORT).show();
                    }

                    if (getByteByStringSign(text[1]) != null) {
                        setImageSign();
                       // showAccToCashReview();
                    } else {
                       // Toast.makeText(this, "Sign Data is not in proper format", Toast.LENGTH_SHORT).show();
                    }*//*

                } else {
                    showSuccess(generalResponseModel.getUserDefinedString());
                }
            }



        } else {
            hideProgressDialog();
            Toast.makeText(ResendSmsConfCode.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
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
            ResendSmsConfCode.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(ResendSmsConfCode.this);
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
                ResendSmsConfCode.this.finish();
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


}
*/

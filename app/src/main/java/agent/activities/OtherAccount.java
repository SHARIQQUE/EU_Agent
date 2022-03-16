package agent.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.*;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
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
import adapter.OtherAccountBaseAdapter;
import callback.CallbackFromAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import model.OtherAccountModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;
import agent.thread.ServerTaskFTP;


/**
 * Created by Sahrique on 14/03/17.
 */


public class OtherAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener, CallbackFromAdapter {
    ListView listView;
    ImageView imageViewPicture, imageViewSign;
    OtherAccountBaseAdapter otherAccountBaseAdapteradapter;
    String mpinString;
    ArrayList<OtherAccountModel> accountsArrayDetailsList;
    String accountNumber, accountType, activeString, accountTypeCode;
    TableRow tableRow;
    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    LinearLayout linearLayoutListview;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, agentCode, spinnerCountryString, transferBasisString, mobileNumberString, amountString, spinnerAccountToDebitString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button submitButton_OterAccount, previousButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner spinnerCountry, spinnerAccountToDebit, transferBasisSpinner;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash, scrolviewSubmitButton, scrollView_listview;
    private AutoCompleteTextView mpinEditText,
            MobileNo_EditText;
    private TextView agentCodeTextView, idproof_TxtView_Review, transactionReferenceNoProofReviewPage, dateOfIssueIdProofReviewPage, placeOfIssueIdProofReviewPage, recipientCountryTxtView_Review, transferBasisTxtView_Review, telephoneNo_TxtView_Review, titleTextView, payerAccountTypeTxtView_Review, customerName_TxtView_Review_AccToCash;
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
                DataParserThread thread = new DataParserThread(OtherAccount.this, mComponentInfo, OtherAccount.this, message.arg1, message.obj.toString());
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

                validation();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    Context context;

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


        setContentView(R.layout.other_account);

        // OtherAccount.this.getSystemService(Context.CONNECTIVITY_SERVICE);


        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        transferTagArray = getResources().getStringArray(R.array.TransferTag);

        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_otherAccount);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");


        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.otherAccount));
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

            OtherAccount.this.finish();
        }


        submitButton_OterAccount = (Button) findViewById(R.id.submitButton_OterAccount);
        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);
        submitButton_OterAccount.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        submitButton_OterAccount.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.GONE);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        imageViewSign = (ImageView) findViewById(R.id.imageViewSign);


        input_SV_AccToCash = (ScrollView) findViewById(R.id.input_SV_AccToCash);

        review_SV_AccToCash = (ScrollView) findViewById(R.id.review_SV_AccToCash);
        //  scrolviewSubmitButton = (ScrollView) findViewById(R.id.scrolviewSubmitButton);
        scrollView_listview = (ScrollView) findViewById(R.id.scrollView_listview);

        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpinEditText);
        mpinEditText.setOnEditorActionListener(this);


        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);

        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(OtherAccount.this);


        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);
        MobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.MobileNo_EditText);
        MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));

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
        agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);


        recipientCountryTxtView_Review = (TextView) findViewById(R.id.recipientCountry_TxtView_Review_AccToCash);
        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);
        telephoneNo_TxtView_Review = (TextView) findViewById(R.id.telephoneNo_TxtView_Review);
        customerName_TxtView_Review_AccToCash = (TextView) findViewById(R.id.customerName_TxtView_Review_AccToCash);

        // linearLayoutListview = (LinearLayout) findViewById(R.id.linearLayoutListview);
        idproof_TxtView_Review = (TextView) findViewById(R.id.idproof_TxtView_Review);
        transactionReferenceNoProofReviewPage = (TextView) findViewById(R.id.transactionReferenceNoProofReviewPage);
        dateOfIssueIdProofReviewPage = (TextView) findViewById(R.id.dateOfIssueIdProofReviewPage);
        placeOfIssueIdProofReviewPage = (TextView) findViewById(R.id.placeOfIssueIdProofReviewPage);

        ////////// TABLE LAYOUT DYNAMIC  START////////////////

        //  tableRow = (TableRow)findViewById(R.id.)


        //////////////////TABEL LAYOUT DYNAMIC END

        agentCodeTextView.setText("AgentCode:-  " + agentCode);
        MobileNo_EditText.setOnEditorActionListener(this);


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

    public void pictureSignatureServerRequest() {
        showProgressDialog(getString(R.string.pleasewait));
        String requestData = generatePictureSign();

        //  callApi("getViewProfileInJSON",requestData,148);

        new ServerTask(mComponentInfo, OtherAccount.this, mHandler, requestData, "getViewProfileInJSON", 148).start();
    }


    void callApi(String apiName, String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.url) + apiName, new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(OtherAccount.this, mComponentInfo, OtherAccount.this, requestCode, response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(OtherAccount.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(OtherAccount.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }

    private String generatePictureSign() {
        String jsonString = "";


        try {

           /* {"agentCode":"237000271016","pin":"46A75E89BC40FDD2F70895AB710D5A22",
            "source":"237000271016","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS"}
              */

           /* SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
            mpinString = prefs.getString("MPIN", null);
            System.out.print(mpinString);
        */

            String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", mobileNumberString);
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

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                MobileNo_EditText.setText("");
                MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));
                // MobileNo_EditText.setFilters(null);
                MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                MobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNo), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                MobileNo_EditText.setFilters(digitsfilters);
                MobileNo_EditText.setText("");


            } else if (i == 2) {
                MobileNo_EditText.setText("");
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
                MobileNo_EditText.setHint(getString(R.string.pleaseentername));
                MobileNo_EditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                MobileNo_EditText.setFilters(digitsfilters);
                MobileNo_EditText.setText("");
            }
        } else {
            Toast.makeText(OtherAccount.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:
                setInputType(i);
                break;
            case R.id.spinner_PayerAccountTypeSelection_AccToAcc:

                break;
            case R.id.spinnerCountry:
                setInputType(transferBasisSpinner.getSelectedItemPosition());
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

    private void OtherAccountReview(final String strData) {
        OtherAccount.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();


                 imageDownload();

                titleTextView.setText(getString(R.string.pleasereviewdetail));
                input_SV_AccToCash.setVisibility(View.GONE);
                review_SV_AccToCash.setVisibility(View.VISIBLE);
                recipientCountryTxtView_Review.setText(spinnerCountryString);
                transferBasisTxtView_Review.setText(transferBasisString);


                String[] data = strData.split("\\|");

                customerName_TxtView_Review_AccToCash.setText(data[0]);
                telephoneNo_TxtView_Review.setText(data[1]);
                placeOfIssueIdProofReviewPage.setText(data[3]);
                dateOfIssueIdProofReviewPage.setText(data[4]);
                idproof_TxtView_Review.setText(data[6]);


                //  submitButton_OterAccount.setText(getString(R.string.transfernow));
                isReview = true;
                //submitButton_OterAccount.setVisibility(View.GONE);

                listView.setVisibility(View.VISIBLE);
                scrollView_listview.setVisibility(View.VISIBLE);


            }
        });

    }

    private boolean ValidationDetails() {
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
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNo), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);
            }

            mobileNumberString = MobileNo_EditText.getText().toString().trim();
            if (mobileNumberString.length() > lengthToCheck) {
                if (transferBasisposition == 1) {
                    if (mobileNumberString.length() == ++lengthToCheck) {
                        mobileNumberString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + mobileNumberString;
                    } else {
                        Toast.makeText(OtherAccount.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];

                mpinString = mpinEditText.getText().toString();
                if (mpinString.trim().length() == 4) {


                    ret = true;


                } else {
                    Toast.makeText(OtherAccount.this, getString(R.string.mpinAccountBalance), Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(OtherAccount.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(OtherAccount.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validation() {
        if (ValidationDetails()) {


            proceedOtherAccount();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton_OterAccount:
                validation();
                break;
        }
    }

    private void proceedOtherAccount() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(OtherAccount.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateAccountDetails();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;


            new ServerTask(mComponentInfo, OtherAccount.this, mHandler, "", requestData, 140).start();

        } else {
            Toast.makeText(OtherAccount.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateAccountDetails() {
        String strAccountData = "";

        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

            http:
            //192.168.0.224:9090/RESTfulWebServiceEU/json/estel/
            // accountsDetails?agentcode=237000271011&mobileno=237000271015

            strAccountData = "accountsDetails?agentcode=" + agentCode + "&mobileno=" + mobileNumberString;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAccountData;
    }


    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OtherAccount.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.otherAccount));

        String[] temp = data.split("\\|");

        builder.setMessage(getString(R.string.ActiverSucessReceipt) + getString(R.string.transactionId) + temp[0]);

        // builder.setMessage(getString(R.string.OtherAccountSucessReceipt) + " \n " +" \n "+ data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                OtherAccount.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }

    public void activerOtherAccountRequestOnServer(String accountNumberString, String accountTypeString, String ACTIVER) {


    }

    private String generateActiverViewData() {
        String jsonString = "";

        try {


            /*  {"agentCode":"237000271011","pin":"B4291A3900D605BF8A02AAC8F716215B", "mobileno":"237000271015",
                    "source":"237000271015","accounttype":"MA","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS"
                    ,"comments":"ACTIVER","accountno":"09610002101","requestcts":"25/05/2016 18:01:51"}
            */

            String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("mobileno", mobileNumberString);
            countryObj.put("source", agentCode);
            countryObj.put("accounttype", accountTypeCode);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("comments", activeString);
            countryObj.put("accountno", accountNumber);
            countryObj.put("requestcts", "");

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

            if (requestNo == 140) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(OtherAccount.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {


                   // String[] temp = generalResponseModel.getUserDefinedString().split("\\|");

                    accountsArrayDetailsList = generalResponseModel.getOtherAccountModels();

                    String strData = generalResponseModel.getUserDefinedString();
                    listView = (ListView) findViewById(R.id.listview);


                  /*
                    accountsArrayDetailsList.add("237696497480|nguemkam kokam ghislain|Mobile Account|MA|Y|Active");
                    accountsArrayDetailsList.add("10F3147653701|f d mapp account|Fixed Deposit Account|FD|N|InActive");
                    accountsArrayDetailsList.add("00633402101|TEST KOKAM|Current Account|CA|P|Pending Approval");
                    accountsArrayDetailsList.add("00633403101|TEST bis SOPRA|Saving Account|SA|N|InActive");
                    accountsArrayDetailsList.add("00633402101|TEST KOKAM|Current Account|CA|P|Pending Approval");

                    */


                    otherAccountBaseAdapteradapter = new OtherAccountBaseAdapter(OtherAccount.this, accountsArrayDetailsList);
                    listView.setAdapter(otherAccountBaseAdapteradapter);


                    OtherAccountReview(strData);
                }
            } else if (requestNo == 145) {
                showSuccess(generalResponseModel.getUserDefinedString());
            }

           /* else if (requestNo == 151) {

                *//*OtherAccountBaseAdapter adapter = new OtherAccountBaseAdapter(OtherAccount.this, accountsArrayDetailsList);
                listView.setAdapter(adapter);*//*
                otherAccountBaseAdapteradapter.notifyDataSetChanged();

            }*/

            else if (requestNo == 200) {


                try {


                    String[] responseImagearray = generalResponseModel.getUserDefinedString().split("\\|");

                    byte[] imageBytes1 = android.util.Base64.decode(responseImagearray[5], android.util.Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                    imageViewSign.setImageBitmap(decodedImage);

                    byte[] imageBytes2 = android.util.Base64.decode(responseImagearray[4], android.util.Base64.DEFAULT);
                    Bitmap decodedImage2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);
                    imageViewPicture.setImageBitmap(decodedImage2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            hideProgressDialog();
            Toast.makeText(OtherAccount.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }


    void imageDownload() {

        if (new InternetCheck().isConnected(OtherAccount.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateImageDownloadingRequest();

            new ServerTaskFTP(mComponentInfo, OtherAccount.this, mHandler, requestData, "downloadimage", 200).start();

            //  vollyRequestApi_FTP("downloadimage",requestData, 200);


        } else {
            Toast.makeText(OtherAccount.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateImageDownloadingRequest() {
        String jsonString = "";


        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", mobileNumberString);

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


    void vollyRequestApi_FTP(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServerTaskFTP.baseUrl_ftp + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(OtherAccount.this, mComponentInfo, OtherAccount.this, requestCode, response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(OtherAccount.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(OtherAccount.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

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
            OtherAccount.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(OtherAccount.this);
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
                OtherAccount.this.finish();
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

    @Override
    public void activerOtherAccountRequestOnServer_CallbackFromAdapter(String accountNumberString, String accountTypeString, String ACTIVER, String accTypeCode) {
        accountNumber = accountNumberString;
        accountType = accountTypeString;
        activeString = ACTIVER;
        accountTypeCode = accTypeCode;
        try {

            if (new InternetCheck().isConnected(OtherAccount.this)) {
                showProgressDialog(getString(R.string.pleasewait));
                String requestData = generateActiverViewData();
                mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

                //  callApi("mobilebankingactivaion",requestData,151);

                new ServerTask(mComponentInfo, OtherAccount.this, mHandler, requestData, "mobilebankingactivaion", 151).start();

            } else {
                Toast.makeText(OtherAccount.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

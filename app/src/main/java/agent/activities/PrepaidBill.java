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
import java.util.List;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptPrepaidBill;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

/**
 * Created by Sahrique on 14/03/17.
 */

public class PrepaidBill extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    String countrySelectedCode, agentName, enelNumberString, destinationPhoneNoString, destinationNameString, spinnerBillerNameString, senderNameString, senderMobileNoString, commentString, agentCode, spinnerCountryString, transferBasisString, meterNumberString, amountString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button nextButton, previousButton, submitButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    String agentBranch;
    String customerName;
    private Spinner spinnerCountry, spinnerAccountToDebit, spinnerBillerName;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;
    private AutoCompleteTextView destinationPhoneNoEditText, amountEditText, mpinEditText, meterNumberEditText, enelNumberEditText;
    private TextView recipientCountryTxtView_Review, serviceFeeTitleReview, transferBasisTxtView_Review, billName_TxtView_Review, meterNumberTitleReview, titleTextView, payerAccountTypeTxtView_Review, enelNumberTitleReview, customerIdTitleReviewPage, meterStatusTitleReview, billerNameTxtView_Review, billerCodeTxtView_Review, invoiceTxtView_Review, amountTxtView_Review, recipientBankTxtView_Review, transferTagTxtView_Review;
    private ProgressDialog mDialog;
    String billerName, metreNumberInvoice, sessionId, fees;

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
                DataParserThread thread = new DataParserThread(PrepaidBill.this, mComponentInfo, PrepaidBill.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, BillerNameFetchArray, TempBillerNameFetchArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;

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


        setContentView(R.layout.prepaid_bill);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_deposit);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
       // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.prepaidBillMenu));
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
            PrepaidBill.this.finish();
        }
        nextButton = (Button) findViewById(R.id.nextButton_MoneyTransfer);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
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


        spinnerBillerName = (Spinner) findViewById(R.id.spinnerBillerName);


        //  BillerNameFetchArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");  //  Biller Name Fetch Data


        //  spinnerBillerName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayListBillerName));
        //  spinnerBillerName.setSelection(0);
        //  spinnerBillerName.setOnItemSelectedListener(PrepaidElectricity.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);
        meterNumberEditText = (AutoCompleteTextView) findViewById(R.id.meterNumberEditText);
        enelNumberEditText = (AutoCompleteTextView) findViewById(R.id.enelNumberEditText);
        amountEditText = (AutoCompleteTextView) findViewById(R.id.amountEditText_AccToCash);
        destinationPhoneNoEditText = (AutoCompleteTextView) findViewById(R.id.destinationPhoneNumberEditText);
        destinationPhoneNoEditText.setHint(getString(R.string.PleasEenterMobileNumber));


        amountEditText.setOnEditorActionListener(this);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(PrepaidBill.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_SHORT).show();
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
        meterNumberTitleReview = (TextView) findViewById(R.id.meterNumberTitleReview);
        billName_TxtView_Review = (TextView) findViewById(R.id.billName_TxtView_Review);
        amountTxtView_Review = (TextView) findViewById(R.id.amount_TxtView_Review_AccToCash);
        enelNumberTitleReview = (TextView) findViewById(R.id.enelNumberTitleReview);
        meterStatusTitleReview = (TextView) findViewById(R.id.meterStatusTitleReview);
        customerIdTitleReviewPage = (TextView) findViewById(R.id.customerIdTitleReviewPage);
        serviceFeeTitleReview = (TextView) findViewById(R.id.serviceFeeTitleReview);


        billerNameTxtView_Review = (TextView) findViewById(R.id.billerName_TxtView_Review_AccToCash);
        billerCodeTxtView_Review = (TextView) findViewById(R.id.billerCode_TxtView_Review_AccToCash);
        invoiceTxtView_Review = (TextView) findViewById(R.id.invoice_TxtView_Review_AccToCash);

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


    }

    List<String> arrayListBillerName;

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

                if (i > 0) {
                    countrySelectedCode = countryCodeArray[i];
                    billerNameRequest();
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

                    //    callApi("billerCode",generateBillercodeData,133);


                    new ServerTask(mComponentInfo, PrepaidBill.this, mHandler, generateBillercodeData, "billerCode", 133).start();
                }
                break;
        }
    }

    void callApi(String apiName, String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.url) + apiName, new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(PrepaidBill.this, mComponentInfo, PrepaidBill.this, requestCode, response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(PrepaidBill.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(PrepaidBill.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    void billerNameRequest() {
        showProgressDialog(getString(R.string.pleasewait));

        //   callApi("getBillerJSON",generateBillerData(),106);

        new ServerTask(mComponentInfo, PrepaidBill.this, mHandler, generateBillerData(), "getBillerJSON", 106).start();
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
            //countryObj.put("transtype", "");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("country", countrySelectedCode);
            countryObj.put("udv2", "BILLERCODEREQUIRED");
            countryObj.put("transtype", "PREPAIDELECTRICITY");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();


        } catch (Exception e) {
            e.printStackTrace();

        }
        return jsonString;
    }


    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                destinationPhoneNoEditText.setText("");
                destinationPhoneNoEditText.setHint(getString(R.string.enterMerchantMobileNo));
                // destinationEditText.setFilters(null);
                destinationPhoneNoEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                destinationPhoneNoEditText.setHint(String.format(getString(R.string.hintDestinationMobileNo), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                destinationPhoneNoEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                destinationPhoneNoEditText.setFilters(digitsfilters);
                destinationPhoneNoEditText.setText("");


            } else if (i == 2) {
                destinationPhoneNoEditText.setText("");
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
                destinationPhoneNoEditText.setHint(getString(R.string.hintDestinationMobileNo));
                destinationPhoneNoEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                destinationPhoneNoEditText.setFilters(digitsfilters);
                destinationPhoneNoEditText.setText("");
            }
        } else {
            Toast.makeText(PrepaidBill.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    private void showAccToCashReview(final String strData) {

        PrepaidBill.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();

                final String[] tempData = strData.split("\\|");

                billerName = tempData[0];
                metreNumberInvoice = tempData[1];
                sessionId = tempData[2];
                fees = tempData[3];

                agentBranch = tempData[4];
                customerName = tempData[5];

                titleTextView.setText(getString(R.string.pleasereviewdetail));
                input_SV_AccToCash.setVisibility(View.GONE);
                review_SV_AccToCash.setVisibility(View.VISIBLE);
                recipientCountryTxtView_Review.setText(spinnerCountryString);
                transferBasisTxtView_Review.setText(transferBasisString);

                billName_TxtView_Review.setText(spinnerBillerNameString);
                amountTxtView_Review.setText(amountString);
                meterNumberTitleReview.setText(metreNumberInvoice);
                enelNumberTitleReview.setText(enelNumberString);
                serviceFeeTitleReview.setText(fees);


                billerNameTxtView_Review.setText(billerName);
                nextButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);
                //meterStatusTitleReview.setText("");

                //customerIdTitleReviewPage.setText("customerIdTitleReviewPage");
                //billerCodeTxtView_Review.setText(senderMobileNoString);
                //invoiceTxtView_Review.setText(commentString);

                payerAccountTypeTxtView_Review.setText(spinnerAccountToDebitString);
                isReview = true;
                mpinEditText.requestFocus();
            }
        });
    }

    private boolean validateCashOut_PartI() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            int lengthToCheck = 3;
            String errorMsgToDisplay = "";
            int transferBasisposition = 0;
            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintDestinationMobileNo), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintDestinationMobileNo), lengthToCheck + 1 + "");
            }

            destinationPhoneNoString = destinationPhoneNoEditText.getText().toString().trim();
            //  if (destinationPhoneNoString.length() > lengthToCheck) {
            if (transferBasisposition == 1) {
                if (destinationPhoneNoString.length() == ++lengthToCheck) {
                    destinationPhoneNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + destinationPhoneNoString;
                } else {
                    Toast.makeText(PrepaidBill.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                    return false;
                }
            }


            if (spinnerBillerName.getSelectedItemPosition() != 0) {
                //  if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {

                meterNumberString = meterNumberEditText.getText().toString().trim();
                if (meterNumberString.length() > 0) {

                    enelNumberString = enelNumberEditText.getText().toString().trim();
                    if (enelNumberString.length() > 0) {

                        amountString = amountEditText.getText().toString().trim();
                        if (amountString.length() > 0) {

                            ret = true;


                            destinationPhoneNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + destinationPhoneNoString;
                            senderMobileNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + senderMobileNoString;
                            spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                            accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];

                        } else {
                            Toast.makeText(PrepaidBill.this, getString(R.string.pleaseEnterAmount), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(PrepaidBill.this, getString(R.string.pleaseEnterENELNumber), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(PrepaidBill.this, getString(R.string.pleaseEnterMeterNumber), Toast.LENGTH_LONG).show();
                }
                  /*  } else {
                        Toast.makeText(PrepaidElectricity.this, getString(R.string.accountofpayer), Toast.LENGTH_LONG).show();
                    }*/
            } else {
                Toast.makeText(PrepaidBill.this, getString(R.string.pleaseSelectBillerName), Toast.LENGTH_LONG).show();
            }

          /*  } else {
                Toast.makeText(PrepaidElectricity.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }*/


        } else {
            Toast.makeText(PrepaidBill.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetails() {
        if (validateCashOut_PartI()) {
            getBillerInfoRequest();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_MoneyTransfer:
                validateDetails();
                break;

            case R.id.submitButton:
                validateDetailsSecondPhase();
                break;
        }


    }

    void validateDetailsSecondPhase() {
        if (validateCashOut_PartII()) {
            getBillPayTransactionRequest();
        }
    }

    public void getBillPayTransactionRequest() {
        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(PrepaidBill.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateGetBillPayData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            //     callApi("getBillPayTransactionInJSON",requestData,150);

            new ServerTask(mComponentInfo, PrepaidBill.this, mHandler, requestData, "getBillPayTransactionInJSON", 150).start();


        } else {
            Toast.makeText(PrepaidBill.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    String generateGetBillPayData() {
        String jsonString = "";

        String pinString = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

/*
            {"agentCode":"237000271015","pin":"2C002225FDF25534C7582E09F9E864BC","source":"237000271015","comments":"OK"
            ,"pintype":"MPIN","vendorcode":"MICR","clienttype":"GPRS","invoiceno":"014266582700","destination":"237100004001",
             "requestcts":"25/05/2016 18:01:51","enelid":"Y2017031800001","sessionid":"01021620089058145143"
              ,"billertype":"prepaid","amount":"1000"}
            */
            // new Change 23/062017
           /* {"agentCode":"237000271015",
                    "pin":"D9D09F2EF2BE7E3F5727C51BEE1C48A7",
                    "source":"237000271015",
                    "comments":"OK",
                    "pintype":"IPIN",
                    "vendorcode":"MICR",
                    "clienttype":"GPRS",
                    "invoiceno":"014266582700",
                    "destination":"237100004001",
                    "requestcts":"25/05/2016 18:01:51",
                    "enelid":"Y2017031800001",
                    "sessionid":"04215118281163323184",
                    "billertype":"prepaid",
                    "amount":"1000"
            }*/

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pinString);
            countryObj.put("source", agentCode);
            countryObj.put("comments", "OK");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("invoiceno", meterNumberString);
            countryObj.put("destination", billerCode);
            countryObj.put("requestcts", "");
            countryObj.put("enelid", enelNumberString);
            countryObj.put("sessionid", sessionId);
            countryObj.put("billertype", "prepaid");
            countryObj.put("amount", amountString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);



            // countryObj.put("billerName", spinnerBillerNameString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;

    }


    String billerCode = "";

    private void getBillerInfoRequest() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(PrepaidBill.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateGetBiller();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            //  callApi("getBillerInfoJSON",requestData,149);

            new ServerTask(mComponentInfo, PrepaidBill.this, mHandler, requestData, "getBillerInfoJSON", 149).start();

        } else {
            Toast.makeText(PrepaidBill.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateGetBiller() {
        String jsonString = "";

        //  String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

          /*  {"agentCode":"237000271015","pin":"2C002225FDF25534C7582E09F9E864BC","pintype":"MPIN",
             "requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS","invoiceno":"278797744",
               "acountType":"MA","destination":"237100004001","comments":"OK","agentname":"Bheem Test"
              ,"amount":"1000","billertype":"prepaid","enelid":"Y2017031800001"}
         */
            // Change 23 June 2017
           /* {"agentCode":"237000271501",
                    "pin":"CAFA9BEFE65AC6D3AC7569AABB897EB6",
                    "pintype":"MPIN",
                    "requestcts":"",
                    "vendorcode":"MICR",
                    "clienttype":"GPRS",
                    "invoiceno":"014266582700",
                    "acountType":"MA",
                    "destination":"237100004001",
                    "comments":"ok",
                    "agentname":"shipra Ag2",
                    "amount":"1500",
                    "billertype":"prepaid",
                    "enelid":"Y2017031800001"
            }*/


            SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
            String pin = prefs.getString("MPIN", null);


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("invoiceno", meterNumberString);

         /*   String accountType = "";
            if (data[1].contains("Mobile Account")) {
                accountType = "MA";
            } else if (data[1].equalsIgnoreCase("savings_account")) {
                accountType = "SA";
            } else if (data[1].equalsIgnoreCase("fixed_deposite")) {
                accountType = "FD";
            } else {
                accountType = data[1];
            }
            */

            countryObj.put("acountType", "MA");
            countryObj.put("destination", billerCode);
            countryObj.put("comments", "ok");
            countryObj.put("agentname", agentName);
            countryObj.put("amount", amountString);
            countryObj.put("billertype", "prepaid");
            countryObj.put("enelid", enelNumberString);



            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            // countryObj.put("billerName", spinnerBillerNameString);


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

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


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
            Toast.makeText(PrepaidBill.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
        }
        return ret;
    }


  /*  private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PrepaidElectricity.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.prepaidElectricity));
        // payerAccountString = payerAccountString.replace("_", "-");
        //  payerAccountString = payerAccountString.toUpperCase();

        String[] temp = data.split("\\|");


        builder.setMessage(getString(R.string.prepaidElectricitySucessReceipt) + getString(R.string.transactionId) + temp[1]);


        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                PrepaidElectricity.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }*/

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {


            if (requestNo == 133) {
                hideProgressDialog();
                Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                System.out.println(generalResponseModel.getUserDefinedString());

                //  generateGetBiller(generalResponseModel.getUserDefinedString());

                billerCode = generalResponseModel.getUserDefinedString().toString();

            } else if (requestNo == 130) {
                if (generalResponseModel.getResponseCode() == 0) {
                    hideProgressDialog();
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            } else if (requestNo == 149) {
                if (generalResponseModel.getResponseCode() == 0) {

                    final String strData = generalResponseModel.getUserDefinedString();
                    showAccToCashReview(strData);
                }
            } else if (requestNo == 106) {

                if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Fee Not Configured")) {
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Entity General Error")) {
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else if (generalResponseModel.getUserDefinedString().matches("Adapter Not Available"))   // from server
                {
                    hideProgressDialog();
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else if (generalResponseModel.getUserDefinedString().matches("Technical Failure"))   // from server
                {
                    hideProgressDialog();
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Invalid PIN"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Please try Again later"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Vendor Not Found")) {
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Template Not Found"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else if (generalResponseModel.getUserDefinedString().matches("Subscriber/Agent Not Found"))   // from server
                {
                    hideProgressDialog();
                    Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                } else {

                    hideProgressDialog();
                    System.out.println(generalResponseModel.getUserDefinedString());

                    String selectBillerName;// = "Please Select Biller Name";
                    selectBillerName = getString(R.string.billerNameMarchantNew);
                    BillerNameFetchArray = (selectBillerName + "|" + generalResponseModel.getUserDefinedString().split("\\;")[0]).split("\\|");



                      /*  arrayListBillerName = new ArrayList<>();                   // display only ENEO
                           arrayListBillerName.add(0, "Please Select Biller Name");

                             List list = Arrays.asList(BillerNameFetchArray);
                            int k = 0;
                            for (k = 0; k < list.size(); k++) {
                                if (list.get(k).toString().matches("ENEO")) {       // kokem display only ENEO
                                    arrayListBillerName.add(list.get(k).toString());
                                }
                            }*/

                    spinnerBillerName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, BillerNameFetchArray));
                    spinnerBillerName.setSelection(0);
                    spinnerBillerName.setOnItemSelectedListener(PrepaidBill.this);
                }
            } else if (requestNo == 150) {
                if (generalResponseModel.getResponseCode() == 0) {
                    hideProgressDialog();
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            }
        } else {
            //showSuccess(generalResponseModel.getUserDefinedString());
            Toast.makeText(PrepaidBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessReceipt(String data) {


        ;

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        //  bundle.putString("labelNameString", labelNameString);
        bundle.putString("spinnerCountryString", spinnerCountryString);
        bundle.putString("meterNumberString", meterNumberString);
        bundle.putString("enelNumberString", enelNumberString);
        bundle.putString("agentBranch", agentBranch);
        bundle.putString("customerName", customerName);


        Intent intent = new Intent(PrepaidBill.this, SucessReceiptPrepaidBill.class);

        intent.putExtra("data", data);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        intent.putExtra("meterNumberString", meterNumberString);
        intent.putExtra("enelNumberString", enelNumberString);

        intent.putExtra("agentBranch", agentBranch);
        intent.putExtra("customerName", customerName);


        startActivity(intent);
        PrepaidBill.this.finish();
    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            PrepaidBill.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(PrepaidBill.this);
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
                PrepaidBill.this.finish();
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

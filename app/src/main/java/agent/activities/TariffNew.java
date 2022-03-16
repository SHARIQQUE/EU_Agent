package agent.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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


import adapter.BillSelectionAdapter;
import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.BillModel;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by AdityaBugalia on 31/08/16.
 */
public class TariffNew extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

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
    TextInputLayout amount_EditText_Tariff_TIL,mpin_EditText_Tariff_TIL;
    TextView destination_heading_tariff, source_heading_tariff;
    public AutoCompleteTextView mpin_EditText_Tariff_sendCashReceiveCash,amountEditText_tariff_sendCash_receivecash, subscriber_MobileNo_EditText,amountEditText, mpinEditText;
    Spinner operationTypeSpinner,transferBasisSpinner, spinnerCountry_tariff_cashsend_receiveCash,billerSpinner, accountNameSpinner, sourceCountrySpinner, destinationCountrySpinner, branchSelectionSpinner, sourceCitySpinner, destinationCitySpinner;
    Button nextButton,nextButton_tariff_sendash_receivecash;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    boolean isBillPay = false, isRequestForSource = false,isCashToM = false;
    LinearLayout input_LL, review_LL,linearLayout_tariff_cashSend_receiveCash;
    ListView statementLsitView;
    ProgressDialog mDialog;
    ArrayAdapter<String> spinnerArrayAdapter;
    Dialog successDialog;

    String mpinStringSendCashReceiveCash,transferBasisString,amountStringSendcashReceivecash,subscriberNumberString,spinnerCountrySendCashReceiveCashString,agentName, agentCode, operationCodeString, operationNameString,countrySelectedCode,


    amountString, branchSelectionString = "", branchSelectionCodeString = "", countrySelectedCodeString,
            sourceCityCodeString = "", sourceCountryCodeString = "", destinationCityCodeString = "",
            destinationCountryCodeString = "", defaultCountrySelectionString,
            defaultCityString, destinationString = "", mpinString, accountTypeCodeString, billerCodeString, billerNameString;


    private String[] operationCodeArray, billerArray, accountCodeArray, accountNameArray;
    private String[] countryArray, billerNameArray, countryCodeArray, countryPrefixArray, destinationCityNameArray, destinationCityCodeArray, sourceCityNameArray, sourceCityCodeArray, payerBankAccountsArray, countryMobileNoLengthArray, recipientBankAccountsArray, payerAccountCodeArray, branchArray, branchCodeArray;

    private enum TransTypes {
        REMTSEND, PTOP, BILLPAY, PAYMENTREQUEST, PAYMENTREQAUTH, BALANCE, TARIFF, ACTIVER, CASHIN, CASHOUT
    }

    int iLevel = 99;
    boolean isMiniStmtMode = false, isBranchRequired = false, isCityRequired = false, isCountryRequired = false;

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


        setContentView(R.layout.tariff_new);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();


        mToolbar = (Toolbar) findViewById(R.id.tool_bar_Tariff);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.Tariff_capital));
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


        amountEditText = (AutoCompleteTextView) findViewById(R.id.amount_EditText_Tariff);
        amountEditText_tariff_sendCash_receivecash = (AutoCompleteTextView) findViewById(R.id.amountEditText_tariff_sendCash_receivecash);
        mpin_EditText_Tariff_sendCashReceiveCash = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_Tariff_sendCashReceiveCash);
        mpin_EditText_Tariff_sendCashReceiveCash.setOnEditorActionListener(this);
        subscriber_MobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.subscriber_MobileNo_EditText);
        source_heading_tariff = (TextView) findViewById(R.id.source_heading_tariff);
        destination_heading_tariff = (TextView) findViewById(R.id.destination_heading_tariff);

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

        operationTypeSpinner = (Spinner) findViewById(R.id.spinner_OperationType_Tariff);
        billerSpinner = (Spinner) findViewById(R.id.spinner_Biller_Tariff);
        accountNameSpinner = (Spinner) findViewById(R.id.spinner_AccountType_Tariff);

      //  defaultCountrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        defaultCountrySelectionString = prefs.getString("countrySelectionString", null);

        defaultCityString = mComponentInfo.getmSharedPreferences().getString("state", "");

        amount_EditText_Tariff_TIL = (TextInputLayout) findViewById(R.id.amount_EditText_Tariff_TIL);
        mpin_EditText_Tariff_TIL = (TextInputLayout) findViewById(R.id.mpin_EditText_Tariff_TIL);


        try {

            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {

            TariffNew.this.finish();
        }

        nextButton = (Button) findViewById(R.id.nextButton_Tariff);
        nextButton.setOnClickListener(this);

        nextButton_tariff_sendash_receivecash=(Button)findViewById(R.id.nextButton_tariff_sendash_receivecash);
        nextButton_tariff_sendash_receivecash.setOnClickListener(this);

        input_LL = (LinearLayout) findViewById(R.id.input_Recipient_LL_Tariff);
        review_LL = (LinearLayout) findViewById(R.id.review_LL_Tariff);
        linearLayout_tariff_cashSend_receiveCash = (LinearLayout) findViewById(R.id.linearLayout_tariff_cashSend_receiveCash);


        billerArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billerSpinner.setAdapter(spinnerArrayAdapter);

        operationCodeArray = getResources().getStringArray(R.array.TxnTypeCodeTariffNew);
        branchArray = getResources().getStringArray(R.array.BranchArray);
        branchCodeArray = getResources().getStringArray(R.array.BranchCodeArray);



        spinnerCountry_tariff_cashsend_receiveCash = (Spinner) findViewById(R.id.spinnerCountry_tariff_cashsend_receiveCash);
        CountryFlagAdapter adaptercash = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry_tariff_cashsend_receiveCash.setAdapter(adaptercash);

        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry_tariff_cashsend_receiveCash.setSelection(getCountrySelection());
        spinnerCountry_tariff_cashsend_receiveCash.requestFocus();
        spinnerCountry_tariff_cashsend_receiveCash.setOnItemSelectedListener(this);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(TariffNew.this);


        //{"agentCode":"24199901789",
        // "pin":"A076ACB625CC0233F7F5D69C3300A0E0",
        // "amount":"544534",
        // "transtype":"BILLPAY",
        // "pintype":"IPIN",
        // "vendorcode":"MICR",
        // "clienttype":"GPRS",
        // "fromcity":"YDE",
        // "tocity":"YDE",
        // "comments":"NOSMS",
        // "udv1":"sb",
        // "accountType":"MA",
        // "billerCode":"123654",
        // "destination":"ENEO",
        // "destcountry":"CAM"}


        sourceCountrySpinner = (Spinner) findViewById(R.id.spinner_SourceCountry_Tariff);
        destinationCountrySpinner = (Spinner) findViewById(R.id.spinner_DestinationCountry_Tariff);

        branchSelectionSpinner = (Spinner) findViewById(R.id.spinner_BranchSelection_Tariff);

        sourceCitySpinner = (Spinner) findViewById(R.id.spinner_SourceCity_Tariff);
        destinationCitySpinner = (Spinner) findViewById(R.id.spinner_DestinationCity_Tariff);
        countryArray[0] = "Please Select Source Country";
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        sourceCountrySpinner.setAdapter(adapter);
        sourceCountrySpinner.setSelection(getCountrySelection());
        sourceCountrySpinner.requestFocus();
        sourceCountrySpinner.setOnItemSelectedListener(this);
        countryArray[0] = "Please Select Destination Country";
        adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());

        destinationCountrySpinner.setAdapter(adapter);
        //   destinationCountrySpinner.setSelection(getCountrySelection());
        destinationCountrySpinner.requestFocus();
        destinationCountrySpinner.setOnItemSelectedListener(this);

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
       /* spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountNameArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountNameSpinner.setAdapter(spinnerArrayAdapter);*/

        billerSpinner.setVisibility(View.GONE);
        operationTypeSpinner.setOnItemSelectedListener(this);
        branchSelectionSpinner.setOnItemSelectedListener(this);

//startServerInteraction(generateCityData(getCountryCodeSelection()),"getStateListInJSON",128);


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

        amountEditText_tariff_sendCash_receivecash.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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

        mpin_EditText_Tariff_sendCashReceiveCash.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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



    private boolean validateOperationType_Selection() {
        boolean ret = false;
        if (operationTypeSpinner.getSelectedItemPosition() > 0) {
            operationCodeString = operationCodeArray[operationTypeSpinner.getSelectedItemPosition()];
            operationNameString = operationTypeSpinner.getSelectedItem().toString();
            ret = true;
        } else {
            Toast.makeText(TariffNew.this, getString(R.string.PleaseSelectOperationType), Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateBiller_Selection() {
        boolean ret = false;
        if (billerSpinner.getSelectedItemPosition() > 0) {
            billerNameString = billerSpinner.getSelectedItem().toString();
            System.out.println(billerNameString);
            ret = true;
        } else {
            Toast.makeText(TariffNew.this, getString(R.string.PleaseSelectBiller), Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

  /*  private boolean validateAccountName_Selection() {
        boolean ret = false;
        if (accountNameSpinner.getSelectedItemPosition() > 0) {
            accountTypeCodeString = accountCodeArray[accountNameSpinner.getSelectedItemPosition()];
            ret = true;
        } else {
            Toast.makeText(TariffNew.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
        }


        return ret;
    }*/

    private boolean validateAmount_Entry() {
        boolean ret = false;
        amountString = amountEditText.getText().toString().trim();
        if (amountString.length() > 0 && validateAmount(amountString)) {

            ret = true;
        } else {
            Toast.makeText(TariffNew.this, getString(R.string.pleaseenteramount), Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateMpin_Entry() {
        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {

            ret = true;

        } else {
            Toast.makeText(TariffNew.this, getString(R.string.prompt_mPin), Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateBranchSelectionFinal() {
        boolean ret = false;

        if (sourceCountryCodeString.equalsIgnoreCase(destinationCountryCodeString)) {

            if (sourceCityCodeString.equalsIgnoreCase(destinationCityCodeString)) {
                ret = true;


            } else {
                Toast.makeText(TariffNew.this, "For Same Branch, Source and Destination City need to be same.", Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(TariffNew.this, "For Same Branch, Source and Destination Country need to be same.", Toast.LENGTH_LONG).show();

        }
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {

            ret = true;

        } else {
            Toast.makeText(TariffNew.this, getString(R.string.prompt_mPin), Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateBranch_Selection() {
        boolean ret = false;
        branchSelectionString = branchSelectionSpinner.getSelectedItem().toString().trim();
        if (branchSelectionSpinner.getSelectedItemPosition() > 0) {
            branchSelectionCodeString = branchCodeArray[branchSelectionSpinner.getSelectedItemPosition()];
            ret = true;

        } else {
            Toast.makeText(TariffNew.this, "Please Select Branch", Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateSourceCountry_Selection() {
        boolean ret = false;

        if (sourceCountrySpinner.getSelectedItemPosition() > 0) {

            sourceCountryCodeString = countryCodeArray[sourceCountrySpinner.getSelectedItemPosition()];
            ret = true;

        } else {
            Toast.makeText(TariffNew.this, "Please Select Source Country", Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateSourceCity_Selection() {
        boolean ret = false;

        if (sourceCitySpinner.getSelectedItemPosition() > 0) {

            sourceCityCodeString = sourceCityCodeArray[sourceCitySpinner.getSelectedItemPosition()];
            ret = true;

        } else {
            Toast.makeText(TariffNew.this, "Please select source City", Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateDestinationCountry_Selection() {
        boolean ret = false;

        if (destinationCountrySpinner.getSelectedItemPosition() > 0) {

            destinationCountryCodeString = countryCodeArray[destinationCountrySpinner.getSelectedItemPosition()];
            ret = true;

        } else {
            Toast.makeText(TariffNew.this, "Please select Destination country", Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateDestinationCity_Selection() {
        boolean ret = false;

        if (destinationCitySpinner.getSelectedItemPosition() > 0) {

            destinationCityCodeString = destinationCityCodeArray[destinationCitySpinner.getSelectedItemPosition()];
            ret = true;

        } else {
            Toast.makeText(TariffNew.this, "Please select Destination City", Toast.LENGTH_SHORT).show();
        }


        return ret;
    }

    private boolean validateDetails(String transTypes) {
        boolean ret = false;
        switch (transTypes) {

            case "REMTSEND":

                if (validateOperationType_Selection()) {
                    if (validateSourceCountry_Selection()) {

                        if (validateDestinationCountry_Selection()) {

                            if (validateAmount_Entry()) {

                                if (validateMpin_Entry()) {


                                    ret = true;

                                }
                            }

                        }
                    }
                }


                sourceCityCodeString = sourceCountryCodeString;
                destinationCityCodeString = destinationCountryCodeString;
                //destinationCountryCodeString="";

                branchSelectionCodeString = "";
                billerCodeString = "";
                billerNameString = "";


                break;

            case "REMTRECV":
                if (validateOperationType_Selection()) {
                    if (validateAmount_Entry()) {
                        if (validateMpin_Entry()) {
                            ret = true;
                        }

                    }
                }
                break;

            case "PREPAIDELECTRICITY":
                if (validateOperationType_Selection()) {
                    if (validateAmount_Entry()) {
                        if (validateMpin_Entry()) {
                            ret = true;
                        }

                    }
                }
                break;



            case "PTOP":


                if (validateOperationType_Selection()) {
                    if (validateSourceCountry_Selection()) {

                        if (validateDestinationCountry_Selection()) {

                            if (validateAmount_Entry()) {

                                if (validateMpin_Entry()) {
                                    ret = true;

                                }
                            }

                        }
                    }
                }

                sourceCityCodeString = "";
                destinationCityCodeString = "";
                //destinationCountryCodeString="";

                branchSelectionCodeString = "";
                billerCodeString = "";
                billerNameString = "";

                break;

            case "BILLPAY":
                if (validateOperationType_Selection()) {
                    if (validateBiller_Selection()) {


                        if (validateAmount_Entry()) {

                            if (validateMpin_Entry()) {
                                ret = true;

                            }

                        }
                    }
                }

                sourceCityCodeString = "";
                destinationCityCodeString = "";
                destinationCountryCodeString = "";

                branchSelectionCodeString = "";
                //billerCodeString="";
                // billerNameString="";
                break;

            case "CASHTOM":
                if (validateOperationType_Selection()) {
                    if (validateBiller_Selection()) {



                        if (validateAmount_Entry()) {

                            if (validateMpin_Entry()) {
                                ret = true;

                            }

                        }
                    }
                }

                sourceCityCodeString = "";
                destinationCityCodeString = "";
                destinationCountryCodeString = "";

                branchSelectionCodeString = "";
                //billerCodeString="";
                // billerNameString="";
                break;
            case "PAYMENTREQUEST":
                if (validateOperationType_Selection()) {


                    if (validateAmount_Entry()) {

                        if (validateMpin_Entry()) {
                            ret = true;

                        }

                    }
                }
                sourceCityCodeString = "";
                destinationCityCodeString = "";
                destinationCountryCodeString = "";

                branchSelectionCodeString = "";
                billerCodeString = "";
                billerNameString = "";
                break;
            case "PAYMENTREQAUTH":
                if (validateOperationType_Selection()) {


                    if (validateAmount_Entry()) {

                        if (validateMpin_Entry()) {
                            ret = true;

                        }
                    }

                }
                sourceCityCodeString = "";
                destinationCityCodeString = "";
                destinationCountryCodeString = "";

                branchSelectionCodeString = "";
                billerCodeString = "";
                billerNameString = "";
                break;
            case "BALANCE":
                if (validateOperationType_Selection()) {


                    if (validateAmount_Entry()) {

                        if (validateMpin_Entry()) {
                            ret = true;

                        }

                    }
                }
                sourceCityCodeString = "";
                destinationCityCodeString = "";
                destinationCountryCodeString = "";

                branchSelectionCodeString = "";
                billerCodeString = "";
                billerNameString = "";
                break;
            case "TARIFF":
                if (validateOperationType_Selection()) {


                    if (validateAmount_Entry()) {

                        if (validateMpin_Entry()) {
                            ret = true;

                        }

                    }
                }
                sourceCityCodeString = "";
                destinationCityCodeString = "";
                destinationCountryCodeString = "";

                branchSelectionCodeString = "";
                billerCodeString = "";
                billerNameString = "";
                break;
            case "ACTIVER":
                if (validateOperationType_Selection()) {


                    if (validateAmount_Entry()) {

                        if (validateMpin_Entry()) {
                            ret = true;


                        }
                    }
                }
                sourceCityCodeString = "";
                destinationCityCodeString = "";
                destinationCountryCodeString = "";

                branchSelectionCodeString = "";
                billerCodeString = "";
                billerNameString = "";
                break;
            case "CASHIN":


                if (validateOperationType_Selection()) {
                    if (validateBranch_Selection()) {
                        if (validateSourceCountry_Selection()) {
                            if (validateSourceCity_Selection()) {
                                if (validateDestinationCountry_Selection()) {
                                    if (validateDestinationCity_Selection()) {

                                        if (validateAmount_Entry()) {

                                            if (validateMpin_Entry()) {

                                                if (branchSelectionCodeString.equalsIgnoreCase("SB")) {
                                                    if (validateBranchSelectionFinal()) {
                                                        ret = true;
                                                    }
                                                } else {
                                                    ret = true;

                                                }


                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                billerCodeString = "";
                billerNameString = "";


                break;


            case "CASHOUT":

                if (validateOperationType_Selection()) {
                    if (validateBranch_Selection()) {
                        if (validateSourceCountry_Selection()) {
                            if (validateSourceCity_Selection()) {
                                if (validateDestinationCountry_Selection()) {
                                    if (validateDestinationCity_Selection()) {

                                        if (validateAmount_Entry()) {

                                            if (validateMpin_Entry()) {

                                                if (branchSelectionCodeString.equalsIgnoreCase("SB")) {
                                                    if (validateBranchSelectionFinal()) {
                                                        ret = true;
                                                    }
                                                } else {
                                                    ret = true;

                                                }


                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
                billerCodeString = "";
                billerNameString = "";


                break;


        }

        return ret;
    }

    private void setlayout(String transTypes) {

        switch (transTypes) {

            case "REMTSEND":

                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);

                source_heading_tariff.setVisibility(View.VISIBLE);
                destination_heading_tariff.setVisibility(View.VISIBLE);

                sourceCountrySpinner.setVisibility(View.VISIBLE);
                destinationCountrySpinner.setVisibility(View.VISIBLE);
                destinationCitySpinner.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = true;
                break;


            case"REMTRECV":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);

                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                break;

            case"PREPAIDELECTRICITY":

                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);

                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);


                break;




            case "CASHTOM":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);
                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = false;
                break;

            case "PTOP":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                source_heading_tariff.setVisibility(View.VISIBLE);
                destination_heading_tariff.setVisibility(View.VISIBLE);
                sourceCountrySpinner.setVisibility(View.VISIBLE);
                destinationCountrySpinner.setVisibility(View.VISIBLE);
                destinationCitySpinner.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = true;
                break;

            case "BILLPAY":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);
                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = false;
                break;

            case "PAYMENTREQUEST":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = false;
                break;

            case "PAYMENTREQAUTH":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);

                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = false;
                break;

            case "BALANCE":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);

                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = false;
                break;

            case "TARIFF":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);
                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = false;
                break;

            case "ACTIVER":
                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);
                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = false;
                isCityRequired = false;
                isCountryRequired = false;
                break;

            case "CASHIN":

                branchSelectionSpinner.setVisibility(View.VISIBLE);
                sourceCitySpinner.setVisibility(View.VISIBLE);
                sourceCountrySpinner.setVisibility(View.VISIBLE);
                destinationCountrySpinner.setVisibility(View.VISIBLE);
                destinationCitySpinner.setVisibility(View.VISIBLE);

                source_heading_tariff.setVisibility(View.VISIBLE);
                destination_heading_tariff.setVisibility(View.VISIBLE);


                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);

                isBranchRequired = true;
                isCityRequired = true;
                isCountryRequired = true;

                break;


            case "CASHOUT":

                branchSelectionSpinner.setVisibility(View.VISIBLE);
                sourceCitySpinner.setVisibility(View.VISIBLE);
                sourceCountrySpinner.setVisibility(View.VISIBLE);
                destinationCountrySpinner.setVisibility(View.VISIBLE);
                destinationCitySpinner.setVisibility(View.VISIBLE);

                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.GONE);
                amount_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                mpin_EditText_Tariff_TIL.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                isBranchRequired = true;
                isCityRequired = true;
                isCountryRequired = true;
                break;

            case "SENDCASH":

                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);
                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);


                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);

                amount_EditText_Tariff_TIL.setVisibility(View.GONE);
                mpin_EditText_Tariff_TIL.setVisibility(View.GONE);


                break;

            case "RECEIVECASH":

                branchSelectionSpinner.setVisibility(View.GONE);
                sourceCitySpinner.setVisibility(View.GONE);
                sourceCountrySpinner.setVisibility(View.GONE);
                destinationCountrySpinner.setVisibility(View.GONE);
                destinationCitySpinner.setVisibility(View.GONE);
                source_heading_tariff.setVisibility(View.GONE);
                destination_heading_tariff.setVisibility(View.GONE);


                linearLayout_tariff_cashSend_receiveCash.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);

                amount_EditText_Tariff_TIL.setVisibility(View.GONE);
                mpin_EditText_Tariff_TIL.setVisibility(View.GONE);


                // SENDCASH;
               // RECEIVECASH


        }


    }

    private String getCountryCodeSelection() {
        String ret = "";
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(defaultCountrySelectionString)) {
                ret = countryCodeArray[i];
            }

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

        if (validateDetails(operationCodeString)) {
            proceedTariff();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.nextButton_Tariff:

                validateDetails();

                break;

            case R.id.nextButton_tariff_sendash_receivecash:

                validationDetailsSendCashReceiveCash();

                break;
        }
    }

    void validationDetailsSendCashReceiveCash()
    {

      if(validateSendCashReceiveCash())
       {
           proceedtarifSendCashReceiveCash();
       }

    }


    private boolean validateSendCashReceiveCash() {
        boolean ret = false;


        if (operationTypeSpinner.getSelectedItemPosition() > 0) {
            operationNameString = operationCodeArray[operationTypeSpinner.getSelectedItemPosition()];


        if (spinnerCountry_tariff_cashsend_receiveCash.getSelectedItemPosition() != 0) {
            spinnerCountrySendCashReceiveCashString = spinnerCountry_tariff_cashsend_receiveCash.getSelectedItem().toString();
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry_tariff_cashsend_receiveCash.getSelectedItemPosition()]) - 1;
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
                        subscriberNumberString = countryPrefixArray[spinnerCountry_tariff_cashsend_receiveCash.getSelectedItemPosition()] + subscriberNumberString;
                    } else {
                        Toast.makeText(TariffNew.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                //  if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {
                amountStringSendcashReceivecash = amountEditText_tariff_sendCash_receivecash.getText().toString().trim();
                if (amountStringSendcashReceivecash.length() > 0 && validateAmount(amountStringSendcashReceivecash)) {


                    mpinStringSendCashReceiveCash = mpin_EditText_Tariff_sendCashReceiveCash.getText().toString().trim();

                    if (mpinStringSendCashReceiveCash.length() == 4) {

                    ret = true;

                    } else {
                        Toast.makeText(TariffNew.this, getString(R.string.pleaseEnterMpin), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(TariffNew.this, getString(R.string.amountNew), Toast.LENGTH_LONG).show();

                }


            } else {
                Toast.makeText(TariffNew.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }



        } else {
            Toast.makeText(TariffNew.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

        } else {
            Toast.makeText(TariffNew.this, getString(R.string.PleaseSelectOperationType), Toast.LENGTH_SHORT).show();
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

                            DataParserThread thread = new DataParserThread(TariffNew.this,mComponentInfo,TariffNew.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(TariffNew.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(TariffNew.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    void proceedtarifSendCashReceiveCash() {
        if (new InternetCheck().isConnected(TariffNew.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffSendCashReceiveCashData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

          //  callApi("getTarifInJSON",requestData,114);

            new ServerTask(mComponentInfo, TariffNew.this, mHandler, requestData, "getTarifInJSON", 114).start();
        } else {
            Toast.makeText(TariffNew.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }



    public void validateDetails() {

        if (validateDetails_PartI()) {
            validateDetails_NextButton();
        }
    }

    private boolean validateDetails_PartI() {
        boolean ret = false;

        if (operationTypeSpinner.getSelectedItemPosition() > 0) {
            operationNameString = operationCodeArray[operationTypeSpinner.getSelectedItemPosition()];

            //   if (accountNameSpinner.getSelectedItemPosition() > 0) {
            //     accountNameSpinner = accountCodeArray[accountNameSpinner.getSelectedItemPosition()];


            amountString = amountEditText.getText().toString().trim();
            if (amountString.length() > 0 && validateAmount(amountString)) {
                mpinString = mpinEditText.getText().toString().trim();
                if (mpinString.length() == 4) {

                    ret = true;


                } else {
                    mpinEditText.requestFocus();
                    Toast.makeText(TariffNew.this, getString(R.string.pleaseEnterMpin), Toast.LENGTH_SHORT).show();
                }
            } else {
                amountEditText.requestFocus();
                Toast.makeText(TariffNew.this, getString(R.string.pleaseenteramount), Toast.LENGTH_SHORT).show();
            }

           /* } else {
               // SpinnerAccountName.requestFocus();
                Toast.makeText(TariffNew.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
            }*/
        } else {
            Toast.makeText(TariffNew.this, getString(R.string.PleaseSelectOperationType), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    void proceedTariff() {
        if (new InternetCheck().isConnected(TariffNew.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

         //   callApi("getTarifInJSON",requestData,114);

            new ServerTask(mComponentInfo, TariffNew.this, mHandler, requestData, "getTarifInJSON", 114).start();
        } else {
            Toast.makeText(TariffNew.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private int getCountrySelection() {
        int ret = 0;
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(defaultCountrySelectionString)) {
                ret = i;
            }

        }


        return ret;
    }

    private String generateTariffSendCashReceiveCashData() {
        String jsonString = "";


        try {

            /*{"agentCode":"237000271011",
                    "pin":"5FFF68DE986617288D4BB5CA37800290",
                    "amount":"500",
                    "transtype":"CASHIN",
                    "pintype":"MPIN",
                    "vendorcode":"MICR",
                    "clienttype":"GPRS",
                    "comments":"NOSMS",
                    "udv1":"SAMEBRANCH",
                    "accountType":"MA",
                    "destination":"237971819684"
            }*/
            String pin = mComponentInfo.getMD5(agentCode + mpinStringSendCashReceiveCash).toUpperCase();

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("amount", amountStringSendcashReceivecash);
            countryObj.put("transtype", operationCodeString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("accountType", "MA");
            countryObj.put("destination", subscriberNumberString);
            countryObj.put("udv1", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

            e.printStackTrace();
        }


        return jsonString;
    }


    private String generateTariffData() {
        String jsonString = "";


        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            //{"agentCode":"24199901789",
            // "pin":"A076ACB625CC0233F7F5D69C3300A0E0",
            // "amount":"544534",
            // "transtype":"BILLPAY",
            // "pintype":"IPIN",
            // "vendorcode":"MICR",
            // "clienttype":"GPRS",
            // "fromcity":"YDE",
            // "tocity":"YDE",
            // "comments":"NOSMS",
            // "udv1":"sb",
            // "accountType":"MA",
            // "billerCode":"123654",
            // "destination":"ENEO",
            // "destcountry":"CAM"}

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("amount", amountString);
            countryObj.put("transtype", operationCodeString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("fromcity", sourceCityCodeString);// source
            countryObj.put("tocity", destinationCityCodeString);// destination
            countryObj.put("comments", "NOSMS");
            countryObj.put("udv1", branchSelectionCodeString);
            countryObj.put("accountType", accountTypeCodeString);
            countryObj.put("billerCode", billerCodeString);
            countryObj.put("destination", billerNameString);
            countryObj.put("destcountry", destinationCountryCodeString);


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
        mDialog = new ProgressDialog(TariffNew.this);
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
                Toast.makeText(TariffNew.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(TariffNew.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(TariffNew.this, mComponentInfo, TariffNew.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TariffNew.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.tariff));
        String[] temp = data.split("\\|");
        builder.setMessage(String.format(getString(R.string.tariffResponseSuccessNew), operationNameString, temp[0], temp[1]));

        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                TariffNew.this.finish();
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

                    if (new InternetCheck().isConnected(TariffNew.this)) {
                        showProgressDialog(getString(R.string.pleasewait));

                      //  callApi("getTarifInJSON",requestData,114);

                        new ServerTask(mComponentInfo, TariffNew.this, mHandler, requestData, "getTarifInJSON", 114).start();
                    } else {
                        Toast.makeText(TariffNew.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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

            if (requestNo == 114) {
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "TARIFF").commit();
                    Intent i = new Intent(TariffNew.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString(), 1);
                }


            } else if (requestNo == 154) {
                hideProgressDialog();
                System.out.println("aaa");

                mComponentInfo.getmSharedPreferences().edit().putString("cityList", generalResponseModel.getUserDefinedString()).commit();
                DataSetter setter = new DataSetter(0);
                setter.execute();

            } else if (requestNo == 119) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("accountTypeCodeString", "MA").commit();

                    mComponentInfo.getmSharedPreferences().edit().putString("process", "BILLPAY").commit();
                    Intent i = new Intent(TariffNew.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    billerCodeString = generalResponseModel.getUserDefinedString();
                    mComponentInfo.getmSharedPreferences().edit().putString("billerCode", billerCodeString).commit();

                }


            } else if (requestNo == 106) {


                hideProgressDialog();
                SharedPreferences.Editor editor = mComponentInfo.getmSharedPreferences().edit();
                editor.putString("billerDetails", getString(R.string.pleaseselectbiller) + generalResponseModel.getUserDefinedString());
                editor.commit();

                try {

                    billerNameArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");

                    spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billerNameArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    billerSpinner.setAdapter(spinnerArrayAdapter);
                    billerSpinner.setOnItemSelectedListener(this);
                    billerSpinner.setVisibility(View.VISIBLE);




                } catch (Exception e) {

                    TariffNew.this.finish();
                }


            }
        } else {
            hideProgressDialog();
            Toast.makeText(TariffNew.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }
    }


    class DataSetter extends AsyncTask<Void, Void, Void> {
        int viewGenerationCase;

        DataSetter(int viewGenerationCase) {
            this.viewGenerationCase = viewGenerationCase;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideProgressDialog();
            showProgressDialog(getString(R.string.pleasewait));


        }

        @Override
        protected Void doInBackground(Void... voids) {

            switch (viewGenerationCase) {
                case 0:

                    String cityData = mComponentInfo.getmSharedPreferences().getString("cityList", "'");
                    String[] tempCity = cityData.split("\\|");
                    BillModel billModel;

                    if (isRequestForSource) {
                        sourceCityCodeArray = new String[tempCity.length + 1];
                        sourceCityNameArray = new String[tempCity.length + 1];

                        sourceCityNameArray[0] = "Please Select City";
                        sourceCityCodeArray[0] = "Please Select City";
                        for (int i = 0; i < tempCity.length; i++) {
                            billModel = new BillModel();
                            String cityDetailString = tempCity[i];
                            String[] tempCityDetailData = cityDetailString.split("\\;");
                            sourceCityCodeArray[i + 1] = tempCityDetailData[0];
                            sourceCityNameArray[i + 1] = tempCityDetailData[1];


                        }
                    } else {
                        destinationCityCodeArray = new String[tempCity.length + 1];
                        destinationCityNameArray = new String[tempCity.length + 1];

                        destinationCityNameArray[0] = "Please Select City";
                        destinationCityCodeArray[0] = "Please Select City";
                        for (int i = 0; i < tempCity.length; i++) {
                            billModel = new BillModel();
                            String cityDetailString = tempCity[i];
                            String[] tempCityDetailData = cityDetailString.split("\\;");
                            destinationCityCodeArray[i + 1] = tempCityDetailData[0];
                            destinationCityNameArray[i + 1] = tempCityDetailData[1];


                        }


                    }


                    break;


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
            BillSelectionAdapter adapter;
            switch (viewGenerationCase) {
                case 0:


                    TariffNew.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestForSource) {
                                spinnerArrayAdapter = new ArrayAdapter<String>(TariffNew.this, android.R.layout.simple_spinner_item, sourceCityNameArray); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sourceCitySpinner.setAdapter(spinnerArrayAdapter);
                            } else {

                                spinnerArrayAdapter = new ArrayAdapter<String>(TariffNew.this, android.R.layout.simple_spinner_item, destinationCityNameArray); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                destinationCitySpinner.setAdapter(spinnerArrayAdapter);
                            }

                        }
                    });


                    break;


            }
        }

    }

    private void setInputType(int i) {

        if (spinnerCountry_tariff_cashsend_receiveCash.getSelectedItemPosition() > 0) {
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinnerCountry_tariff_cashsend_receiveCash.getSelectedItemPosition()]));
                subscriber_MobileNo_EditText.setHint(String.format(getString(R.string.hintMobileCashIn), countryMobileNoLengthArray[spinnerCountry_tariff_cashsend_receiveCash.getSelectedItemPosition()] + " "));
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
            Toast.makeText(TariffNew.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            TariffNew.this.finish();


        }
        return super.onOptionsItemSelected(menuItem);
    }

    private String generateCityData(String CountryCode) {

        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", mComponentInfo.getmSharedPreferences().getString("agentCode", ""));

            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("countrycode", CountryCode);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }

        return jsonString;
    }

    public void startServerInteraction(String data, String apiName, int requestNo) {


        showProgressDialog(getString(R.string.pleasewait));
        if (new InternetCheck().isConnected(TariffNew.this)) {

          //  callApi(apiName,data,requestNo);

            new ServerTask(mComponentInfo, TariffNew.this, mHandler, data, apiName, requestNo).start();
        } else {

            Toast.makeText(TariffNew.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

        }
    }

    String generateBillerData(String transType) {
        String jsonString = "";

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", mComponentInfo.getmSharedPreferences().getString("agentCode", ""));
            countryObj.put("pin", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", transType);
            countryObj.put("country", getCountryCodeSelection());
            countryObj.put("udv2", "BILLERCODEREQUIRED");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }

        return jsonString;

    }

    String generateBillercodeData() {

        String jsonString = "";
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);

            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("billername", billerNameString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }

        return jsonString;


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_OperationType_Tariff:

                if (i == 3) {
                    isBillPay = true;
                    billerSpinner.setVisibility(View.VISIBLE);
                    startServerInteraction(generateBillerData("BILLPAY"), "getBillerJSON", 106);

                } else {
                    isBillPay = false;
                    billerSpinner.setVisibility(View.GONE);
                }


            if (i == 7) {
                isBillPay = true;
                billerSpinner.setVisibility(View.VISIBLE);
                startServerInteraction(generateBillerData("CASHTOM"),"getBillerJSON", 106);

            } else {
                isBillPay = false;
                billerSpinner.setVisibility(View.GONE);
            }



            if (i != 0) {
                operationCodeString = operationCodeArray[i];
                setlayout(operationCodeString);
            }



            break;


            case R.id.spinner_Biller_Tariff:

                if (i != 0) {

                    billerNameString = billerSpinner.getSelectedItem().toString();


                    startServerInteraction(generateBillercodeData(), "billerCode", 119);
                }
                break;

            case R.id.spinnerCountry_tariff_cashsend_receiveCash:

                countrySelectedCode = countryCodeArray[i];
                setInputType(transferBasisSpinner.getSelectedItemPosition());

                break;

            case R.id.spinner_SourceCountry_Tariff:

                if (i != 0) {

                    isRequestForSource = true;
                    sourceCountryCodeString = countryCodeArray[i];
                    startServerInteraction(generateCityData(sourceCountryCodeString), "getStateListInJSON", 154);

                } else {
                    sourceCityCodeString = "";
                    sourceCountryCodeString = "";

                }
                break;

            case R.id.spinner_DestinationCountry_Tariff:

                if (i != 0) {

                    isRequestForSource = false;
                    destinationCountryCodeString = countryCodeArray[i];
                    startServerInteraction(generateCityData(destinationCountryCodeString), "getStateListInJSON", 154);
                } else {
                    destinationCountryCodeString = "";
                    destinationCityCodeString = "";

                }
                break;

            case R.id.spinner_SourceCity_Tariff:

                if (i != 0) {

                    sourceCityCodeString = sourceCityCodeArray[i];
                } else {
                    sourceCityCodeString = "";
                }
                break;
            case R.id.spinner_DestinationCity_Tariff:

                if (i != 0) {
                    destinationCityCodeString = destinationCityCodeArray[i];

                } else {
                    destinationCityCodeString = "";
                }
                break;
            case R.id.spinner_BranchSelection_Tariff:

                if (i != 0) {
                    branchSelectionCodeString = branchCodeArray[i];
                    if (branchSelectionCodeString.equalsIgnoreCase("SB")) {
                        destinationCountrySpinner.setSelection(sourceCountrySpinner.getSelectedItemPosition());


                    } else {
                        branchSelectionCodeString = "";
                    }

                } else {
                    branchSelectionCodeString = "";
                }
                break;


        }
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



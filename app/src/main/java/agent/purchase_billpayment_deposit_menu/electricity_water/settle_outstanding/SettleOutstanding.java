package agent.purchase_billpayment_deposit_menu.electricity_water.settle_outstanding;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.KeyEvent;
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
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class SettleOutstanding extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String  amountString,payerAmountString,countrySelectedCode, electricCompanyString,paymentOptionString;
    Spinner spinner_electricCompany,spinner_paymentOption;
    TextView totalAmount_textView_mpinPage,vat_textview_mpinPage,fees_textview_mpinPage,amount_textview_mpinPage,option_amount_textview_mpinPage,amountSubscription_textview_mpinPage,option_textview_mpinPage,subscriptionType_textview_mpinPage,payerMobileNumber_textview_mpinPage,subscriberName_textview_mpinpage,destinationCountry_textview_mpinpage,tvCompanyDistributor_textview_mpinPage,subscriberNumber_textview_mpinPage;

    String[] electricCompany_arrayName = {"Electricity company","Tata Sky", "Dish", "Videocon", "Airtel", "Relience"};
    String[] electricCompany_arrayCode = {"Electricity company","Tata Sky", "Dish", "Videocon", "Airtel", "Relience"};

    String[] paymentOption_arrayName = {"Subscription Type","type1", "Type2", "Type3", "Type4", "Type5"};
    String[] paymentOption_arrayCode = {"Subscription Type","type1", "Type2", "Type3", "Type4", "Type5"};

    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    LinearLayout firstPage_linearlayout, secondPage_linearlayout, mpinpage_linearlayout;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    String stateNameString, agentName, agentCode, spinnerCountryString, transferBasisString, subscriberNumberString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    Button button_search, button_secondPage,button_mpinPage;
    boolean isReview, isServerOperationInProcess;
    int transferCase, accToAccLevel = 0;
    String toCity;
    private Spinner spinnerCountry, spinnerAccountToDebit, transferBasisSpinner;
    private AutoCompleteTextView  mobileNumber_autoCompletetextView,amount_autoCompleteTextView,payerAmountEditText_autoCompleteTextView;
    private EditText mpinEditText;
    private ProgressDialog mDialog;
    EditText tariffAmmount_EdittextReview_AccToCash;

    TextView outStanding_textview,destinationCountry_textview_secondpage, subscriberNumber_textview, subscriberName_textview;

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
                DataParserThread thread = new DataParserThread(SettleOutstanding.this, mComponentInfo, SettleOutstanding.this, message.arg1, message.obj.toString());

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


               // validation_details_first_one();


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


        setContentView(R.layout.settle_outstanding);



        transferTagArray = getResources().getStringArray(R.array.TransferTag);

        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MoneyTransfer);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.tv_capital)+" "+getString(R.string.subscriptin_capital));

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


            button_search = (Button) findViewById(R.id.button_search);
            button_search.setOnClickListener(this);


            button_secondPage = (Button) findViewById(R.id.button_secondPage);
            button_secondPage.setOnClickListener(this);

            button_mpinPage= (Button) findViewById(R.id.button_mpinPage);
            button_mpinPage.setOnClickListener(this);



            spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
            CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
            spinnerCountry.setAdapter(adapter);

            // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
            spinnerCountry.setSelection(getCountrySelection());
            spinnerCountry.requestFocus();
            spinnerCountry.setOnItemSelectedListener(this);


            transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
            String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
            transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
            transferBasisSpinner.setSelection(1);
            transferBasisSpinner.setOnItemSelectedListener(SettleOutstanding.this);

            spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
            spinnerAccountToDebit.setOnItemSelectedListener(this);

            tariffAmmount_EdittextReview_AccToCash = (EditText) findViewById(R.id.tariffAmmount_EdittextReview_AccToCash);


            destinationCountry_textview_secondpage = (TextView) findViewById(R.id.destinationCountry_textview_secondpage);
            subscriberNumber_textview = (TextView) findViewById(R.id.subscriberNumber_textview);
            subscriberName_textview = (TextView) findViewById(R.id.subscriberName_textview);
            outStanding_textview = (TextView) findViewById(R.id.outStanding_textview);


            firstPage_linearlayout = (LinearLayout) findViewById(R.id.firstPage_linearlayout);
            secondPage_linearlayout = (LinearLayout) findViewById(R.id.secondPage_linearlayout);
            mpinpage_linearlayout = (LinearLayout) findViewById(R.id.mpinpage_linearlayout);


            mobileNumber_autoCompletetextView = (AutoCompleteTextView) findViewById(R.id.mobileNumber_autoCompletetextView);
            mobileNumber_autoCompletetextView.setHint(getString(R.string.subscriberNumber_electricityBill));
            mobileNumber_autoCompletetextView.setOnEditorActionListener(this);



            spinner_electricCompany = (Spinner) findViewById(R.id.spinner_electricCompany);


            spinner_paymentOption = (Spinner) findViewById(R.id.spinner_paymentOption);




        } catch (Exception e) {
            Toast.makeText(SettleOutstanding.this, getString(R.string.plzTryAgainLater), Toast.LENGTH_LONG).show();
            SettleOutstanding.this.finish();
        }


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


        mpinEditText = (EditText) findViewById(R.id.mpinEditText);
        mpinEditText.setOnEditorActionListener(this);


        amount_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.amount_autoCompleteTextView);
        amount_autoCompleteTextView.setOnEditorActionListener(this);

        amount_autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                   // Toast.makeText(CashIn.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_LONG).show();
                    if (enteredString.length() > 0) {
                        amount_autoCompleteTextView.setText(enteredString.substring(1));
                    } else {
                        amount_autoCompleteTextView.setText("");
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

        /////////

        payerAmountEditText_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.payerAmountEditText_autoCompleteTextView);
        payerAmountEditText_autoCompleteTextView.setOnEditorActionListener(this);

        payerAmountEditText_autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                  //  Toast.makeText(CashIn.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_LONG).show();
                    if (enteredString.length() > 0) {
                        payerAmountEditText_autoCompleteTextView.setText(enteredString.substring(1));
                    } else {
                        payerAmountEditText_autoCompleteTextView.setText("");
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


    }






    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:

                setInputType(i);

                break;


            case R.id.spinnerCountry:

                countrySelectedCode = countryCodeArray[i];
                setInputType(transferBasisSpinner.getSelectedItemPosition());

                electricCompany_request();

                break;


            case R.id.spinner_electricCompany:

                electricCompanyString = electricCompany_arrayCode[i];
                Toast.makeText(SettleOutstanding.this, electricCompanyString, Toast.LENGTH_LONG).show();


                break;



            case R.id.spinner_paymentOption:

                paymentOptionString = paymentOption_arrayCode[i];
                Toast.makeText(SettleOutstanding.this, paymentOptionString, Toast.LENGTH_LONG).show();


                optionType_request();

                break;

       }
    }


    void electricCompany_request() {

        if (new InternetCheck().isConnected(SettleOutstanding.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generatElectricCompany_request();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  callApi("getTarifInJSON",requestData,259);   // Also Change Request

            new ServerTask(mComponentInfo, SettleOutstanding.this, mHandler, requestData, "getTarifInJSON", 259).start();

        } else {
            Toast.makeText(SettleOutstanding.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void subscriptionType_request() {

        if (new InternetCheck().isConnected(SettleOutstanding.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateSubscriptionType_request();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  callApi("getTarifInJSON",requestData,249);   // Also Change Request

            new ServerTask(mComponentInfo, SettleOutstanding.this, mHandler, requestData, "getTarifInJSON", 249).start();

        } else {
            Toast.makeText(SettleOutstanding.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }
    void optionType_request() {

        if (new InternetCheck().isConnected(SettleOutstanding.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateOptionType_request();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  callApi("getTarifInJSON",requestData,250);   // Also Change Request

            new ServerTask(mComponentInfo, SettleOutstanding.this, mHandler, requestData, "getTarifInJSON", 250).start();

        } else {
            Toast.makeText(SettleOutstanding.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                mobileNumber_autoCompletetextView.setText("   ");
                mobileNumber_autoCompletetextView.setHint("   "+getString(R.string.subscriberNumber_electricityBill));
                // mobileNumber_autoCompletetextView.setFilters(null);
                mobileNumber_autoCompletetextView.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                //    mobileNumber_autoCompletetextView.setHint(String.format(getString(R.string.hintMobileCashIn), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                mobileNumber_autoCompletetextView.setHint("   "+getString(R.string.subscriberNumber_electricityBill));
                mobileNumber_autoCompletetextView.setInputType(InputType.TYPE_CLASS_NUMBER);
                mobileNumber_autoCompletetextView.setFilters(digitsfilters);
                mobileNumber_autoCompletetextView.setText("");


            } else if (i == 2) {
                mobileNumber_autoCompletetextView.setText("");
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
                mobileNumber_autoCompletetextView.setHint("   "+getString(R.string.subscriberNumber_electricityBill));
                mobileNumber_autoCompletetextView.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                mobileNumber_autoCompletetextView.setFilters(digitsfilters);
                mobileNumber_autoCompletetextView.setText("");
            }
        } else {
            Toast.makeText(SettleOutstanding.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

    }



    private boolean validation_page_one() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {

            spinnerCountryString = spinnerCountry.getSelectedItem().toString();
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.payerMobileNumber_format), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);
            }

            if (spinner_electricCompany.getSelectedItemPosition() != 0) {


                        subscriberNumberString = mobileNumber_autoCompletetextView.getText().toString().trim();
                        if (subscriberNumberString.length() > lengthToCheck) {
                            if (transferBasisposition == 1) {
                                if (subscriberNumberString.length() == ++lengthToCheck) {
                                    subscriberNumberString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + subscriberNumberString;
                                } else {
                                    Toast.makeText(SettleOutstanding.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                    return false;
                                }
                            }

                            ret = true;

                            spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                            accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];

                        } else {
                            Toast.makeText(SettleOutstanding.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

                        }




            } else {
                Toast.makeText(SettleOutstanding.this, getString(R.string.electricity_company), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(SettleOutstanding.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

        return ret;
    }



    public void validation_details_first_one() {

        if (validation_page_one()) {

            secondPage_reveiwPage();

        }
    }

    void secondPage_reveiwPage() {

        firstPage_linearlayout.setVisibility(View.GONE);
        button_search.setVisibility(View.GONE);

        secondPage_linearlayout.setVisibility(View.VISIBLE);
        button_secondPage.setVisibility(View.VISIBLE);

        destinationCountry_textview_secondpage.setText("INDIA");
        outStanding_textview.setText("yello colour back grond");
        subscriberNumber_textview.setText("9718196849");
        subscriberName_textview.setText("Sharique Anwar");



        destinationCountry_textview_mpinpage=(TextView)findViewById(R.id.destinationCountry_textview_mpinpage);
        tvCompanyDistributor_textview_mpinPage=(TextView)findViewById(R.id.tvCompanyDistributor_textview_mpinPage);
        subscriberNumber_textview_mpinPage=(TextView)findViewById(R.id.subscriberNumber_textview_mpinPage);
        subscriberName_textview_mpinpage=(TextView)findViewById(R.id.subscriberName_textview_mpinpage);
        payerMobileNumber_textview_mpinPage=(TextView)findViewById(R.id.payerMobileNumber_textview_mpinPage);
        subscriptionType_textview_mpinPage=(TextView)findViewById(R.id.subscriptionType_textview_mpinPage);
        option_textview_mpinPage=(TextView)findViewById(R.id.option_textview_mpinPage);
        amountSubscription_textview_mpinPage=(TextView)findViewById(R.id.amountSubscription_textview_mpinPage);
        option_amount_textview_mpinPage=(TextView)findViewById(R.id.option_amount_textview_mpinPage);
        amount_textview_mpinPage=(TextView)findViewById(R.id.amount_textview_mpinPage);
        fees_textview_mpinPage=(TextView)findViewById(R.id.fees_textview_mpinPage);
        vat_textview_mpinPage=(TextView)findViewById(R.id.vat_textview_mpinPage);
        totalAmount_textView_mpinPage=(TextView)findViewById(R.id.totalAmount_textView_mpinPage);

        subscriptionType_request();
    }


    void validationDetails_mpinPage() {

        if (validation_mpinPage()) {

            proceedMpinPage();
        }
    }


    private String generateSubscriptionType_request() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", subscriberNumberString);
            countryObj.put("amount", "");
            countryObj.put("transtype", "CASHIN");
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

            //  countryObj.put("destination", nameNumberString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private String generateOptionType_request() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", subscriberNumberString);
            countryObj.put("amount", "");
            countryObj.put("transtype", "CASHIN");
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

            //  countryObj.put("destination", nameNumberString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }
    private String generatElectricCompany_request() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", subscriberNumberString);
            countryObj.put("amount", "");
            countryObj.put("transtype", "CASHIN");
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

            //  countryObj.put("destination", nameNumberString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    void callApi(String apiName, String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.url) + apiName, new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(SettleOutstanding.this, mComponentInfo, SettleOutstanding.this, requestCode, response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(SettleOutstanding.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(SettleOutstanding.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_search:

                validation_details_first_one();

                break;

            case R.id.button_secondPage:

                validationDetails_secondPage();

                break;



            case R.id.button_mpinPage:

                validationDetails_mpinPage();

                break;



        }
    }


    void validationDetails_secondPage() {

        if (validation_page_second()) {

            Toast.makeText(SettleOutstanding.this,"DISPLAY MPIN PAGE DISPLAY", Toast.LENGTH_LONG).show();

            firstPage_linearlayout.setVisibility(View.GONE);
            secondPage_linearlayout.setVisibility(View.GONE);
            secondPage_linearlayout.setVisibility(View.GONE);
            button_search.setVisibility(View.GONE);
            button_secondPage.setVisibility(View.GONE);

            mpinEditText.setVisibility(View.VISIBLE);
            button_mpinPage.setVisibility(View.VISIBLE);
            mpinpage_linearlayout.setVisibility(View.VISIBLE);


            destinationCountry_textview_mpinpage.setText("INDIA");
            tvCompanyDistributor_textview_mpinPage.setText("TataSky");
            subscriberNumber_textview_mpinPage.setText("9718196849");
            subscriberName_textview_mpinpage.setText("Sharique");
            payerMobileNumber_textview_mpinPage.setText(subscriberNumberString);
            subscriptionType_textview_mpinPage.setText("YES");
            option_textview_mpinPage.setText("NO");
            amountSubscription_textview_mpinPage.setText("1000 subscription");
            option_amount_textview_mpinPage.setText("1000 option ");
            amount_textview_mpinPage.setText("100");
            fees_textview_mpinPage.setText("20");
            vat_textview_mpinPage.setText("2 %");
            totalAmount_textView_mpinPage.setText("10002025250");

        }
    }

    private boolean validation_page_second() {

        boolean ret = false;

        paymentOptionString = spinner_paymentOption.getSelectedItem().toString();

        if (spinner_paymentOption.getSelectedItemPosition() != 0) {

            amountString = amount_autoCompleteTextView.getText().toString().trim();

            if (amountString.length() > 0) {

                payerAmountString = payerAmountEditText_autoCompleteTextView.getText().toString().trim();
                if (payerAmountString.length() > 0) {


                ret = true;

            } else {
                Toast.makeText(SettleOutstanding.this, getString(R.string.payerAmount), Toast.LENGTH_LONG).show();
            }


            } else {
                Toast.makeText(SettleOutstanding.this, getString(R.string.paymentOption), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(SettleOutstanding.this, getString(R.string.paymentOption), Toast.LENGTH_LONG).show();
        }

        return ret;
    }


    private void proceedMpinPage() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(SettleOutstanding.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateAccToCashData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            // callApi("getCashInTransaction",requestData,251);
            new ServerTask(mComponentInfo, SettleOutstanding.this, mHandler, requestData, "getCashInTransaction", 251).start();


        } else {
            Toast.makeText(SettleOutstanding.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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
            countryObj.put("comments", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("amount", "");
            countryObj.put("destination", subscriberNumberString);
            countryObj.put("requestcts", "");
            countryObj.put("udv2", "");
            countryObj.put("accountType", "MA");
            //   countryObj.put("country", countrySelectedCode);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);



            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private boolean validation_mpinPage() {

        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;

        } else {
            Toast.makeText(SettleOutstanding.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();

        }

        return ret;
    }


    private void showSuccessReceipt(String data) {

        Toast.makeText(SettleOutstanding.this, "" + "Transaction Successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 251) {

                Toast.makeText(SettleOutstanding.this, "" + "251  "+"Successful", Toast.LENGTH_SHORT).show();

            } else if (requestNo == 259) {
                spinner_electricCompany.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, electricCompany_arrayName));
                spinner_electricCompany.setSelection(0);
                spinner_electricCompany.setOnItemSelectedListener(SettleOutstanding.this);
            }

            else if (requestNo == 260) {
                spinner_paymentOption.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paymentOption_arrayName));
                spinner_paymentOption.setSelection(0);
                spinner_paymentOption.setOnItemSelectedListener(SettleOutstanding.this);
            }



        } else {
            hideProgressDialog();
            Toast.makeText(SettleOutstanding.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(SettleOutstanding.this);
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
                SettleOutstanding.this.finish();
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

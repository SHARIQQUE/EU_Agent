package agent.purchase_billpayment_deposit_menu.taxes;

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
import agent.purchase_billpayment_deposit_menu.electricity_water.electricity.CheckBoxMpinPageActivity;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class RechargeCashDeposit extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String amountString, commentString, cashDepositReasonString, billNo_subscriberNo_temp, countrySelectedCode, categoryTypeString, companyPartnerString;
    Spinner spinner_category, spinner_companyPartnerType, spinner_cashDepositReason;

    String[] category_array_arrayName = {"Select category", "category 1", "category 2", "category 3", "category4", "category 5"};
    String[] category_array_arrayCode = {"Select category", "category 1", "category 2", "category 3", "category4", "category 5"};


    String[] companyPartner_array_name = {"Select company / partner", "company 1", "company 2", "company 3", "company 4"};
    String[] companypartner_array_code = {"Select company / partner", "company 1", "company 2", "company 3", "company 4"};

    String[] cashDepositReason_array_name = {"Select cash deposit's reason", "Cash deposit 1", "Cash deposit 2", "Cash deposit 3", "Cash deposit 4"};
    String[] cashDepositReason_array_code = {"Select cash deposit's reason", "Cash deposit 1", "Cash deposit 2", "Cash deposit 3", "Cash deposit 4"};


    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    LinearLayout firstPage_linearlayout, secondPage_linearlayout, mpinpage_linearlayout;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    String stateNameString, payerNameString, agentName, agentCode, spinnerCountryString, transferBasisString, payerMobileNumberString, keyValueString, keyDetailsString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    Button button_search, button_secondPage;
    boolean isReview, isServerOperationInProcess;
    int transferCase, accToAccLevel = 0;
    String toCity;
    private Spinner spinnerCountry, spinnerAccountToDebit, transferBasisSpinner;
    private AutoCompleteTextView comments_autoCompleteTextView, keyValue_autoCompleteTextView, payerName_autoCompleteTextView, amountEditText_autoCompleteTextView, keyDetails_autoCompleteTextView, payerMobileNumber_autoCompletetextView;
    private EditText mpinEditText;
    private ProgressDialog mDialog;
    EditText tariffAmmount_EdittextReview_AccToCash;

    TextView payerMobileNumber_textview_secondPage, billdueDate_textview, amount_textview, fees_textview, billDate_textview, destinationCountry_textview_secondpage, subscriberNumber_textview, subscriberName_textview, totalAmount_textview_secondPage, billnumber_textview, vat_textview;

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
                DataParserThread thread = new DataParserThread(RechargeCashDeposit.this, mComponentInfo, RechargeCashDeposit.this, message.arg1, message.obj.toString());

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

                validation_details_first_one();


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


        setContentView(R.layout.recharge_cash_deposit);


        transferTagArray = getResources().getStringArray(R.array.TransferTag);

        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MoneyTransfer);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.recharge_cashdeposit_capital));

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
            button_search.setVisibility(View.VISIBLE);


            button_secondPage = (Button) findViewById(R.id.button_secondPage);
            button_secondPage.setOnClickListener(this);
            button_secondPage.setVisibility(View.GONE);


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
            transferBasisSpinner.setOnItemSelectedListener(RechargeCashDeposit.this);

            spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
            spinnerAccountToDebit.setOnItemSelectedListener(this);

            tariffAmmount_EdittextReview_AccToCash = (EditText) findViewById(R.id.tariffAmmount_EdittextReview_AccToCash);


            destinationCountry_textview_secondpage = (TextView) findViewById(R.id.destinationCountry_textview_secondpage);
            subscriberNumber_textview = (TextView) findViewById(R.id.subscriberNumber_textview);
            subscriberName_textview = (TextView) findViewById(R.id.subscriberName_textview);
            billnumber_textview = (TextView) findViewById(R.id.billnumber_textview);
            vat_textview = (TextView) findViewById(R.id.vat_textview);
            billDate_textview = (TextView) findViewById(R.id.billDate_textview);
            totalAmount_textview_secondPage = (TextView) findViewById(R.id.totalAmount_textview_secondPage);

            billdueDate_textview = (TextView) findViewById(R.id.billdueDate_textview);
            amount_textview = (TextView) findViewById(R.id.amount_textview);
            fees_textview = (TextView) findViewById(R.id.fees_textview);
            payerMobileNumber_textview_secondPage = (TextView) findViewById(R.id.payerMobileNumber_textview_secondPage);


            firstPage_linearlayout = (LinearLayout) findViewById(R.id.firstPage_linearlayout);
            secondPage_linearlayout = (LinearLayout) findViewById(R.id.secondPage_linearlayout);
            mpinpage_linearlayout = (LinearLayout) findViewById(R.id.mpinpage_linearlayout);


            payerMobileNumber_autoCompletetextView = (AutoCompleteTextView) findViewById(R.id.payerMobileNumber_autoCompletetextView);
            payerMobileNumber_autoCompletetextView.setHint(getString(R.string.payerMobileNumber));
            payerMobileNumber_autoCompletetextView.setOnEditorActionListener(this);

            keyValue_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.keyValue_autoCompleteTextView);
            keyDetails_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.keyDetails_autoCompleteTextView);
            payerName_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.payerName_autoCompleteTextView);
            comments_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.comments_autoCompleteTextView);


            spinner_category = (Spinner) findViewById(R.id.spinner_category);
            spinner_companyPartnerType = (Spinner) findViewById(R.id.spinner_companyPartnerType);
            spinner_cashDepositReason = (Spinner) findViewById(R.id.spinner_cashDepositReason);


            amountEditText_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.amountEditText_autoCompleteTextView);
            amountEditText_autoCompleteTextView.setOnEditorActionListener(this);

            amountEditText_autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String enteredString = s.toString();
                    if (enteredString.startsWith("0")) {
                        //   Toast.makeText(RechargeCashDeposit.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_LONG).show();
                        if (enteredString.length() > 0) {
                            amountEditText_autoCompleteTextView.setText(enteredString.substring(1));
                        } else {
                            amountEditText_autoCompleteTextView.setText("");
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


        } catch (Exception e) {
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.plzTryAgainLater), Toast.LENGTH_LONG).show();
            RechargeCashDeposit.this.finish();
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


        category_request();




    }

    void cashDepositReasonTypeRequest() {
        if (new InternetCheck().isConnected(RechargeCashDeposit.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateCashDepositReason_request();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  callApi("getTarifInJSON",requestData,256);   // Also Change Request

            new ServerTask(mComponentInfo, RechargeCashDeposit.this, mHandler, requestData, "getTarifInJSON", 256).start();

        } else {
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                payerMobileNumber_autoCompletetextView.setText("   ");
                payerMobileNumber_autoCompletetextView.setHint("   " + getString(R.string.payerMobileNumber));
                // payerMobileNumber_autoCompletetextView.setFilters(null);
                payerMobileNumber_autoCompletetextView.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                //    payerMobileNumber_autoCompletetextView.setHint(String.format(getString(R.string.hintMobileCashIn), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                payerMobileNumber_autoCompletetextView.setHint("   " + getString(R.string.payerMobileNumber));
                payerMobileNumber_autoCompletetextView.setInputType(InputType.TYPE_CLASS_NUMBER);
                payerMobileNumber_autoCompletetextView.setFilters(digitsfilters);
                payerMobileNumber_autoCompletetextView.setText("");


            } else if (i == 2) {
                payerMobileNumber_autoCompletetextView.setText("");
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
                payerMobileNumber_autoCompletetextView.setHint("   " + getString(R.string.payerMobileNumber));
                payerMobileNumber_autoCompletetextView.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                payerMobileNumber_autoCompletetextView.setFilters(digitsfilters);
                payerMobileNumber_autoCompletetextView.setText("");
            }
        } else {
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

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

                break;


            case R.id.spinner_category:

                categoryTypeString = category_array_arrayCode[i];
                Toast.makeText(RechargeCashDeposit.this, categoryTypeString, Toast.LENGTH_LONG).show();

                break;


            case R.id.spinner_companyPartnerType:

                companyPartnerString = companypartner_array_code[i];

                break;

            case R.id.spinner_cashDepositReason:

                cashDepositReasonString = cashDepositReason_array_code[i];
                Toast.makeText(RechargeCashDeposit.this, cashDepositReasonString, Toast.LENGTH_LONG).show();

                break;
        }
    }


    void category_request() {

        if (new InternetCheck().isConnected(RechargeCashDeposit.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateCategory_request();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  callApi("getTarifInJSON",requestData,254);   // Also Change Request

            new ServerTask(mComponentInfo, RechargeCashDeposit.this, mHandler, requestData, "getTarifInJSON", 254).start();

        } else {
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void companyPartner_request() {

        if (new InternetCheck().isConnected(RechargeCashDeposit.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generate_searchBillNumberSubscriberNumber_request();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  callApi("getTarifInJSON",requestData,255);   // Also Change Request

            new ServerTask(mComponentInfo, RechargeCashDeposit.this, mHandler, requestData, "getTarifInJSON", 255).start();

        } else {
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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

            if (spinner_category.getSelectedItemPosition() != 0) {

                if (spinner_companyPartnerType.getSelectedItemPosition() != 0) {


                    amountString = amountEditText_autoCompleteTextView.getText().toString().trim();
                    if (amountString.length() > 0) {


                        keyValueString = keyValue_autoCompleteTextView.getText().toString().trim();
                        if (keyValueString.length() > 0) {

                            keyDetailsString = keyDetails_autoCompleteTextView.getText().toString().trim();
                            if (keyDetailsString.length() > 0) {

                                payerNameString = payerName_autoCompleteTextView.getText().toString().trim();
                                if (payerNameString.length() > 0) {

                                    payerMobileNumberString = payerMobileNumber_autoCompletetextView.getText().toString().trim();
                                    if (payerMobileNumberString.length() > lengthToCheck) {
                                        if (transferBasisposition == 1) {
                                            if (payerMobileNumberString.length() == ++lengthToCheck) {
                                                payerMobileNumberString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + payerMobileNumberString;
                                            } else {
                                                Toast.makeText(RechargeCashDeposit.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                                return false;
                                            }
                                        }

                                        ret = true;

                                        commentString = comments_autoCompleteTextView.getText().toString().trim();
                                        spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                                        accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];

                                    } else {
                                        Toast.makeText(RechargeCashDeposit.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

                                    }

                                } else {
                                    Toast.makeText(RechargeCashDeposit.this, getString(R.string.payerName), Toast.LENGTH_LONG).show();

                                }

                            } else {
                                Toast.makeText(RechargeCashDeposit.this, getString(R.string.keyDetails), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(RechargeCashDeposit.this, getString(R.string.keyValue), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(RechargeCashDeposit.this, getString(R.string.amountNew), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(RechargeCashDeposit.this, getString(R.string.billNumber_subscriberNumber), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(RechargeCashDeposit.this, getString(R.string.select_category), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
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
        mpinpage_linearlayout.setVisibility(View.VISIBLE);
        button_secondPage.setVisibility(View.VISIBLE);

        destinationCountry_textview_secondpage.setText("INDIA");
        subscriberNumber_textview.setText("9718196849");
        subscriberName_textview.setText("Sharique Anwar");
        billnumber_textview.setText("40000");
        billDate_textview.setText("21-11-2019");
        billdueDate_textview.setText("21-11-2002");
        payerMobileNumber_textview_secondPage.setText(payerMobileNumberString);
        amount_textview.setText("6000");
        fees_textview.setText("2 %");
        vat_textview.setText("18");
        totalAmount_textview_secondPage.setText("5000000");
    }


    void validationDetails_mpinPage() {

        if (validation_mpinPage()) {

            proceedMpinPage();
        }
    }

    private String generateCashDepositReason_request() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", payerMobileNumberString);
            countryObj.put("amount", keyValueString);
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


    private String generateCategory_request() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", payerMobileNumberString);
            countryObj.put("amount", keyValueString);
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

    private String generate_searchBillNumberSubscriberNumber_request() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", payerMobileNumberString);
            countryObj.put("amount", keyValueString);
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

                            DataParserThread thread = new DataParserThread(RechargeCashDeposit.this, mComponentInfo, RechargeCashDeposit.this, requestCode, response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(RechargeCashDeposit.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
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

                validationDetails_mpinPage();

                break;


        }
    }

    private void proceedMpinPage() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(RechargeCashDeposit.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateAccToCashData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            // callApi("getCashInTransaction",requestData,247);
            new ServerTask(mComponentInfo, RechargeCashDeposit.this, mHandler, requestData, "getCashInTransaction", 247).start();


        } else {
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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
            countryObj.put("amount", keyValueString);
            countryObj.put("destination", payerMobileNumberString);
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


    private boolean validation_mpinPage() {

        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;

        } else {
            Toast.makeText(RechargeCashDeposit.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();

        }

        return ret;
    }


    private void showSuccessReceipt(String data) {

        Toast.makeText(RechargeCashDeposit.this, "" + "Transaction Successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 24722555) {

                Intent intent = new Intent(RechargeCashDeposit.this, CheckBoxMpinPageActivity.class);
                startActivity(intent);

            } else if (requestNo == 254) {

                spinner_category.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, category_array_arrayName));
                spinner_category.setSelection(0);
                spinner_category.setOnItemSelectedListener(RechargeCashDeposit.this);

                companyPartner_request();
            }

            else if (requestNo == 255) {

                spinner_companyPartnerType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, companyPartner_array_name));
                spinner_companyPartnerType.setSelection(1);
                spinner_companyPartnerType.setOnItemSelectedListener(RechargeCashDeposit.this);

                cashDepositReasonTypeRequest();

            } else if (requestNo == 256) {

                spinner_cashDepositReason.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cashDepositReason_array_name));
                spinner_cashDepositReason.setSelection(0);
                spinner_cashDepositReason.setOnItemSelectedListener(RechargeCashDeposit.this);
            }


        } else {
            hideProgressDialog();
            Toast.makeText(RechargeCashDeposit.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(RechargeCashDeposit.this);
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
                RechargeCashDeposit.this.finish();
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

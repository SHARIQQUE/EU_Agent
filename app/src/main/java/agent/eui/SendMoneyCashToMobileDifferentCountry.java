package agent.eui;

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
import agent.activities.OTPVerificationActivity;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptSendMoneyToMobileDiffrentCountry;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class SendMoneyCashToMobileDifferentCountry extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String[] bankSelectionArray, transferTagArray, currencyArray, accountTypeArray;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, commentString, spinnerCurrenceyString, onItemSelected, tariffAmountFee, spinnerCountryDestinationString, destinationNoString, agentCode,
            spinnerCountryString, transferBasisString, sourceMobileLength, sourcePrefixString, sourceMoNumberString, currencySelectionString, amountString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    TextView destinationname_TxtView_Review_AccToCash;
    View viewForContainer;
    String SenderNameAgentIdentity, destinationNameAgentIdentity;
    Button nextButton, transferButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner spinnerCountry, spinnerCountryTempory, spinnerCurrency, spinnerCountryDestination, recipientbankSpinner, recipientAccTypeSpinner, payerBankSpinner, spinnerAccountToDebit,
            transferTagSpinner, transferBasisSpinner;
    private ScrollView scrollview_sendMoney, scrollview_reviewpage;
    private AutoCompleteTextView name_No_EditText, sourceName_EditText, amountEditText, mpinEditText, recipient_AccountNo_EditText,
            sourceMobileNumberEditText, destination_NumberEditText;
    private TextView deviceTextView, mobileNumberofBenificairay_textView, recipientCountryTxtView_Review, transferBasisTxtView_Review,
            recipientNameNoTitleTxtView_Review, recipientNameNoTxtView_Review,
            recipientAccountTypeTxtView_Review, payerBankTxtView_Review, titleTextView, editTextViewSendMoney, cancelTextViewSendMoney,
            destinationNumberTitleReview, amountTxtView_Review, recipientBankTxtView_Review, transferTagTxtView_Review;
    private ProgressDialog mDialog;
    EditText tariffAmmount_EdittextReview_AccToCash, sourceName_edittext_reveiwPage, commentEdittext;
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
                DataParserThread thread = new DataParserThread(SendMoneyCashToMobileDifferentCountry.this, mComponentInfo, SendMoneyCashToMobileDifferentCountry.this, message.arg1, message.obj.toString());
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


        setContentView(R.layout.sendmoney_cashtomobile_differentcountry);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_remmetanceSendMoneyToMobile);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // currencySelectionString = mComponentInfo.getmSharedPreferences().getString("currency", "");

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.sendMoney));
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
        String[] temArr = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
        String[] temArrCode = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
        String[] temArrprefix = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
        String[] temArrlenght = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");


        try {

            countryArray = new String[temArr.length - 1];
            countryCodeArray = new String[temArr.length - 1];
            countryPrefixArray = new String[temArr.length - 1];
            countryMobileNoLengthArray = new String[temArr.length - 1];
            int p = 0;
            for (int i = 0; i < temArr.length; i++) {

                if (!temArr[i].equalsIgnoreCase(countrySelectionString)) {
                    countryArray[p] = temArr[i];
                    countryCodeArray[p] = temArrCode[i];
                    countryPrefixArray[p] = temArrprefix[i];
                    countryMobileNoLengthArray[p] = temArrlenght[i];
                    p++;
                } else {

                    sourceMobileLength = temArrlenght[i];
                    sourcePrefixString = temArrprefix[i];
                }

            }
            temArr = null;
            temArrCode = null;
            temArrprefix = null;
            temArrlenght = null;

//            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
//            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
//            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
//            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {

            SendMoneyCashToMobileDifferentCountry.this.finish();
        }

        nextButton = (Button) findViewById(R.id.nextButton_MoneyTransfer);
        transferButton = (Button) findViewById(R.id.transferButton_MoneyTransfer);
        nextButton.setOnClickListener(this);
        transferButton.setOnClickListener(this);
        nextButton.setVisibility(View.VISIBLE);
        transferButton.setVisibility(View.GONE);

        scrollview_sendMoney = (ScrollView) findViewById(R.id.scrollview_sendMoney);
        scrollview_reviewpage = (ScrollView) findViewById(R.id.scrollview_reviewpage);


        sourceName_edittext_reveiwPage = (EditText) findViewById(R.id.sourceName_edittext_reveiwPage);
        commentEdittext = (EditText) findViewById(R.id.commentEdittext);
        deviceTextView = (TextView) findViewById(R.id.deviceTextView);
        mobileNumberofBenificairay_textView = (TextView) findViewById(R.id.mobileNumberofBenificairay_textView);

        // Destination Mobile number is Benifeciry //


        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);
        // spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        spinnerCountryDestination = (Spinner) findViewById(R.id.spinnerCountryDestination);
        CountryFlagAdapter adapter2 = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountryDestination.setAdapter(adapter2);
        //spinnerCountryDestination.setEnabled(false);
        // spinnerCountryDestination.setSelection(getCountrySelection());
        spinnerCountryDestination.requestFocus();
        spinnerCountryDestination.setOnItemSelectedListener(this);


       /* CountryFlagAdapter adapter2 = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountryDestination.setAdapter(adapter2);

        // spinnerCountryDestination.setSelection(getCountrySelection());
        spinnerCountryDestination.requestFocus();
        spinnerCountryDestination.setOnItemSelectedListener(this);*/


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(SendMoneyCashToMobileDifferentCountry.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);

        sourceMobileNumberEditText = (AutoCompleteTextView) findViewById(R.id.sourceMobileNumberEditText);
        sourceMobileNumberEditText.setHint(getString(R.string.PleasEenterMobileNumber));

        destination_NumberEditText = (AutoCompleteTextView) findViewById(R.id.destination_NumberEditText);
        destination_NumberEditText.setHint(getString(R.string.PleasEnterDestinationMobilePhoneNumber));


        amountEditText = (AutoCompleteTextView) findViewById(R.id.amountEditText_AccToCash);
        sourceName_EditText = (AutoCompleteTextView) findViewById(R.id.sourceName_EditText);
      //  DestinationName_EditText = (AutoCompleteTextView) findViewById(R.id.DestinationName_EditText);

        currencySelectionString = "FCR";

        if (currencySelectionString.contains("FCR"))  //
        {
            currencySelectionString = "CFA-XAF";      // Kokem Change 31072017
        }

        currencyArray = new String[5];
        currencyArray[0] = (getString(R.string.SelectCurrencyNew));
        currencyArray[1] = "CFA-XAF";
        currencyArray[2] = "EURO";
        currencyArray[3] = "FRDC";
        currencyArray[4] = "USD";
        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        spinnerCurrency.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerCurrency.setAdapter(arrayAdapter);


        amountEditText.setOnEditorActionListener(this);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_SHORT).show();
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

        cancelTextViewSendMoney = (TextView) findViewById(R.id.cancelTextViewSendMoney);
        editTextViewSendMoney = (TextView) findViewById(R.id.editTextViewSendMoney);

        cancelTextViewSendMoney.setOnClickListener(this);
        editTextViewSendMoney.setOnClickListener(this);

        recipientCountryTxtView_Review = (TextView) findViewById(R.id.recipientCountry_TxtView_Review_AccToCash);
        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);
        recipientNameNoTxtView_Review = (TextView) findViewById(R.id.recipientNameNo_TxtView_Review_AccToCash);
        amountTxtView_Review = (TextView) findViewById(R.id.amount_TxtView_Review_AccToCash);
        tariffAmmount_EdittextReview_AccToCash = (EditText) findViewById(R.id.tariffAmmount_EdittextReview_AccToCash);


        destinationname_TxtView_Review_AccToCash = (EditText) findViewById(R.id.destinationname_TxtView_Review_AccToCash);
        destinationNumberTitleReview = (TextView) findViewById(R.id.destinationNumberTitleReview);


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


        tariffAmmount_EdittextReview_AccToCash.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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






        setInputType(1);
        setInputTypeDestination(1);
    }


    private void setInputType(int i) {


        if (i == 1) {
            sourceMobileNumberEditText.setText("");
            sourceMobileNumberEditText.setHint(getString(R.string.PleasEenterSourceMobileNumber));
            // sourceMobileNumberEditText.setFilters(null);
            sourceMobileNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
            digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(sourceMobileLength));
            sourceMobileNumberEditText.setHint(String.format(getString(R.string.hintSendMoneyPhoneNumber), sourceMobileLength + " "));
            sourceMobileNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            sourceMobileNumberEditText.setFilters(digitsfilters);
            sourceMobileNumberEditText.setText("");


        } else if (i == 2) {
            sourceMobileNumberEditText.setText("");
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
            sourceMobileNumberEditText.setHint(getString(R.string.pleaseentername));
            sourceMobileNumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            sourceMobileNumberEditText.setFilters(digitsfilters);
            sourceMobileNumberEditText.setText("");
        }


    }


    private void setInputTypeDestination(int i) {

        if (spinnerCountryDestination.getSelectedItemPosition() > 0) {


            if (i == 1) {
                destination_NumberEditText.setText("");
                destination_NumberEditText.setHint(getString(R.string.PleasEnterDestinationMobilePhoneNumber));
                // sourceMobileNumberEditText.setFilters(null);
                destination_NumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinnerCountryDestination.getSelectedItemPosition()]));
                destination_NumberEditText.setHint(String.format(getString(R.string.hintSendMoneyPhoneNumberRecipient), countryMobileNoLengthArray[spinnerCountryDestination.getSelectedItemPosition()] + " "));
                destination_NumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                destination_NumberEditText.setFilters(digitsfilters);
                destination_NumberEditText.setText("");


            } else if (i == 2) {
                destination_NumberEditText.setText("");
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
                destination_NumberEditText.setHint(getString(R.string.pleaseentername));
                destination_NumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                destination_NumberEditText.setFilters(digitsfilters);
                destination_NumberEditText.setText("");
            }
        } else {
            destination_NumberEditText.setHint(getString(R.string.PleasEnterDestinationMobilePhoneNumber));
           // Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.pleaseSelectCountryDestination), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:


                break;

            case R.id.spinner_PayerAccountTypeSelection_AccToAcc:

                if (i > 0) {
                    if (amountEditText != null) {
                        amountEditText.requestFocus();
                    }
                }
                break;

            case R.id.spinnerCountry:
                setInputType(1);
                //setInputTypeDestination(1);
                break;

            case R.id.spinnerCurrency:
                // Toast.makeText(getApplicationContext(),country[position] ,Toast.LENGTH_LONG).show();
                break;

            case R.id.spinnerCountryDestination:

                setInputTypeDestination(1);
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

    private void showSendMoneyReview(final String strTariffAmount) {

        SendMoneyCashToMobileDifferentCountry.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();
                titleTextView.setText(getString(R.string.pleasereviewdetail));
                scrollview_sendMoney.setVisibility(View.GONE);
                scrollview_reviewpage.setVisibility(View.VISIBLE);
                recipientCountryTxtView_Review.setText(spinnerCountryDestinationString);
                transferBasisTxtView_Review.setText(transferBasisString);
                recipientNameNoTxtView_Review.setText(sourceMoNumberString);
                amountTxtView_Review.setText(amountString + " " + spinnerCurrenceyString);


                tariffAmmount_EdittextReview_AccToCash.setText(strTariffAmount);
               // tariffAmmount_EdittextReview_AccToCash.setEnabled(true);
                destinationNumberTitleReview.setText(destinationNoString);


                transferButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
                isReview = true;
                mpinEditText.requestFocus();

                agentIdentitySourceNumber();

            }
        });
    }

    private boolean validateSendMoneyToMobile_PartI() {
        boolean ret = false;

//        if (spinnerCountry.getSelectedItemPosition() != 0) {
        //   spinnerCountryString = spinnerCountry.getSelectedItem().toString();

        int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

        //     if (transferBasisposition != 0) {
        int lengthToCheck = Integer.parseInt(sourceMobileLength);

        String errorMsgToDisplay = "";
        errorMsgToDisplay = "Please enter sender mobile no of " + lengthToCheck + " digits";


        sourceMoNumberString = sourceMobileNumberEditText.getText().toString().trim();
       if (sourceMoNumberString.length() == lengthToCheck) {

            //sourceNameString = sourceName_EditText.getText().toString().trim();
            //    if (sourceNameString.length() >= 2) {

            if (spinnerCountryDestination.getSelectedItemPosition() != 0) {
                spinnerCountryDestinationString = spinnerCountryDestination.getSelectedItem().toString();


                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountryDestination.getSelectedItemPosition()]);
                errorMsgToDisplay = String.format(getString(R.string.hintDestinationMobileNo), lengthToCheck + "");

                destinationNoString = destination_NumberEditText.getText().toString().trim();
                if (destinationNoString.length() == lengthToCheck) {
                    {
                        //  destinationNoString = countryPrefixArray[spinnerCountryDestination.getSelectedItemPosition()] + destinationNoString;
                        sourceMoNumberString = sourcePrefixString + sourceMoNumberString;

                    }

                //    destinationNameString = DestinationName_EditText.getText().toString().trim();
                    //     if (destinationNameString.length() >= 2) {

                    if (spinnerCurrency.getSelectedItemPosition() != 0) {
                        // spinnerCurrenceyString = spinnerCurrency.getSelectedItem().toString();

                        //  if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {
                        amountString = amountEditText.getText().toString().trim();
                        if (amountString.length() > 0 && validateAmount(amountString)) {

                            ret = true;
                            destinationNoString = countryPrefixArray[spinnerCountryDestination.getSelectedItemPosition()] + destinationNoString;

                            spinnerCurrenceyString = spinnerCurrency.getSelectedItem().toString();
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
                            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.amountNew), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.SelectCurrencyNew), Toast.LENGTH_LONG).show();
                    }

                             /*   } else {
                                    Toast.makeText(RemmettanceSendMoneyERAMobile.this, getString(R.string.accountofpayer), Toast.LENGTH_LONG).show();
                                }*/
/*
                            } else {
                                Toast.makeText(RemmettanceSendMoneyERAMobile.this, getString(R.string.pleaseEnterDestationName), Toast.LENGTH_LONG).show();
                            }*/
                } else {
                    Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.pleaseSelectCountryDestination), Toast.LENGTH_LONG).show();
            }
               /* } else {
                    Toast.makeText(RemmettanceSendMoneyERAMobile.this, getString(R.string.pleaseEnterSourceName), Toast.LENGTH_LONG).show();
                }*/
        } else {
            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
        }
            /*} else {
                Toast.makeText(RemmettanceSendMoneyERAMobile.this, getString(R.string.pleaseselectsendmode), Toast.LENGTH_LONG).show();
              }*/

//        } else {
//            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
//        }
        return ret;
    }

    public void validateDetails() {
        if (!isReview) {
            if (validateSendMoneyToMobile_PartI()) {

                proceedTariffAmount();

                // showSendMoneyReview();
            }
        } else {
            if (validateSendMoneyToMobile_PartII()) {
                SendMoneyToMobileTransfer();
            }
        }
    }

    void proceedTariffAmount() {
        if (new InternetCheck().isConnected(SendMoneyCashToMobileDifferentCountry.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

          //  callApi("getTarifInJSON",requestData,114);
             new ServerTask(mComponentInfo, SendMoneyCashToMobileDifferentCountry.this, mHandler, requestData, "getTarifInJSON", 114).start();

        } else {
            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(SendMoneyCashToMobileDifferentCountry.this,mComponentInfo, SendMoneyCashToMobileDifferentCountry.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
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
            countryObj.put("amount", amountString);
            countryObj.put("transtype", "REMTSEND");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            // countryObj.put("fromcity", "YDE");   //  change from server
            //  countryObj.put("tocity", "YDE");     //  Change from Server
            countryObj.put("comments", "");
            countryObj.put("udv1", "");
            countryObj.put("accountType", "");
            countryObj.put("destination", destinationNoString);


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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_MoneyTransfer:
                validateDetails();
                break;

            case R.id.transferButton_MoneyTransfer:
                transferNowValidation();
                break;

            case R.id.cancelTextViewSendMoney:
                isReview = false;
                cancelTaransaction();
                break;

            case R.id.editTextViewSendMoney:
                isReview = false;
                editConfirmationPage();
                break;
        }
    }

    private void agentIdentitySourceNumber() {

        if (new InternetCheck().isConnected(SendMoneyCashToMobileDifferentCountry.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentitySourceNumber();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

          //  callApi("getAgentIdentity",requestData,155);

            new ServerTask(mComponentInfo, SendMoneyCashToMobileDifferentCountry.this, mHandler, requestData, "getAgentIdentity", 155).start();
        } else {
            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateDataAgentIdentitySourceNumber() {
        String jsonString = "";

        //String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", sourceMoNumberString);  // check senderNumber
            countryObj.put("customerid", "");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "SENDMONEY");  // Chec

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private void agentIdentityDestinationNumber() {

        if (new InternetCheck().isConnected(SendMoneyCashToMobileDifferentCountry.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentityDestinationNumber();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

       //     callApi("getAgentIdentity",requestData,157);
           new ServerTask(mComponentInfo, SendMoneyCashToMobileDifferentCountry.this, mHandler, requestData, "getAgentIdentity", 157).start();
        } else {
            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateDataAgentIdentityDestinationNumber() {
        String jsonString = "";

        //String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", destinationNoString);  // check senderNumber
            countryObj.put("customerid", "");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "SENDMONEY");  // Chec

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    public void transferNowValidation() {
        if (validateSendMoneyToMobile_PartII()) {
            SendMoneyToMobileTransfer();
        }

    }

    public void editConfirmationPage() {
        titleTextView.setText(getString(R.string.pleasereviewdetail));
        scrollview_sendMoney.setVisibility(View.VISIBLE);
        scrollview_reviewpage.setVisibility(View.GONE);
        transferButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
    }

    public void cancelTaransaction() {
        titleTextView.setText(getString(R.string.pleasereviewdetail));
        scrollview_sendMoney.setVisibility(View.VISIBLE);
        scrollview_reviewpage.setVisibility(View.GONE);

        transferButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);

        spinnerAccountToDebit.setSelection(0);
        mpinEditText.setText("");
        sourceMobileNumberEditText.setText("");
        amountEditText.setText("");

     /*   sourceMobileNumberEditText.setText("");
        amountEditText.setText("");

        nextButton.setVisibility(View.VISIBLE);
        transferButton.setVisibility(View.GONE);*/
    }

    private void SendMoneyToMobileTransfer() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(SendMoneyCashToMobileDifferentCountry.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateAccToCashData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

         //   callApi("getRemtSendTransactionInJSON",requestData,122);

           new ServerTask(mComponentInfo, SendMoneyCashToMobileDifferentCountry.this, mHandler, requestData, "getRemtSendTransactionInJSON", 122).start();
        } else {
            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateAccToCashData() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

          /*  {"agentCode":"237000151003","pin":"A12DAE7503356159D0895906B4F32C5E",
             "source":"237000151003","comments":"OK","pintype":"MPIN","vendorcode":"MICR",
             "clienttype":"GPRS","destination":"237000399011","requestcts":"25/05/2016
                18:01:51","amount":"10000","accountType":"MA"}*/

            // request Changes 09 06 2017

                /*  {"agentCode":"237000271015","pin":"D9D09F2EF2BE7E3F5727C51BEE1C48A7","source":"237000271015"
                    ,"comments":"asd","pintype":"IPIN","vendorcode":"MICR","clienttype":"HTTP"
                    ,"destination":"237000271111","amount":"1000","fee":"800","currency":"EURO","pintype":"IPIN",
                    "sourcename":"xyz","destinationname":"xyz"}
  */
            SenderNameAgentIdentity = sourceName_edittext_reveiwPage.getText().toString().trim();
            System.out.print(SenderNameAgentIdentity);

            destinationNameAgentIdentity= destinationname_TxtView_Review_AccToCash.getText().toString().trim();
            System.out.print(destinationNameAgentIdentity);

            tariffAmountFee = tariffAmmount_EdittextReview_AccToCash.getText().toString().trim();


            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("source", data[0].trim());
            countryObj.put("source", sourceMoNumberString);
            countryObj.put("comments", commentString);
            //countryObj.put("requestcts", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("destination", destinationNoString);
            countryObj.put("amount", amountString);

            countryObj.put("fee", tariffAmountFee);
            countryObj.put("currency", spinnerCurrenceyString);
            countryObj.put("sourcename", SenderNameAgentIdentity);             // vfg
            countryObj.put("destinationname", destinationNameAgentIdentity);   //

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

           /* String accountType = "";
            if (data[1].contains("Mobile Account")) {
                accountType = "MA";
            } else if (data[1].equalsIgnoreCase("savings_account")) {
                accountType = "SA";
            } else if (data[1].equalsIgnoreCase("fixed_deposite")) {
                accountType = "FD";
            } else {
                accountType = data[1];
            }
            countryObj.put("accountType", accountType);*/


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private boolean validateSendMoneyToMobile_PartII() {
        boolean ret = false;


        commentString = commentEdittext.getText().toString().trim();
        System.out.print(commentString);

        mpinString = mpinEditText.getText().toString().trim();

        if (mpinString.length() == 4) {
            ret = true;
        } else {
            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("spinnerCurrenceyString", spinnerCurrenceyString);
        bundle.putString("spinnerCountryString", spinnerCountryString);
        bundle.putString("spinnerCountryString", destinationNoString);


        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();




        mComponentInfo.getmSharedPreferences().edit().putString("senderNameERA", SenderNameAgentIdentity).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("SenderDestinationERA", destinationNameAgentIdentity).commit();

        mComponentInfo.getmSharedPreferences().edit().putString("spinnerCurrenceyString", spinnerCurrenceyString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("countrySelection", countryArray[spinnerCountryDestination.getSelectedItemPosition()]).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("destinationNoString", destinationNoString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("commentString", commentString).commit();

        int amountPrintbuttonhide=Integer.parseInt(amountString);
        mComponentInfo.getmSharedPreferences().edit().putInt("amountPrintbuttonhide", amountPrintbuttonhide).commit();


        Intent intent = new Intent(SendMoneyCashToMobileDifferentCountry.this, SucessReceiptSendMoneyToMobileDiffrentCountry.class);

        intent.putExtra("data", data);
        intent.putExtra("spinnerCurrenceyString", spinnerCurrenceyString);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        intent.putExtra("destinationNoString", destinationNoString);
        intent.putExtra("amountPrintbuttonhide", amountPrintbuttonhide);
        startActivity(intent);
        SendMoneyCashToMobileDifferentCountry.this.finish();
    }

    /*private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RemmettanceSendMoneyERAMobile.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.remmitance_send_money_tomobie));

        String[] temp = data.split("\\|");

        if (temp[3].matches("comments")) {
            builder.setMessage(getString(R.string.RemmettanceSendMoneyERAMobileSucessReceipt) + getString(R.string.transactionId) + temp[0] + "  " + getString(R.string.ConfCode) + temp[1]);
        } else {
            builder.setMessage(getString(R.string.RemmettanceSendMoneyERAMobileSucessReceipt) + getString(R.string.transactionId) + temp[0] + " \n" + getString(R.string.SendMoneySucessReceiptAmountMaximumNote));
        }

        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                RemmettanceSendMoneyERAMobile.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }
*/


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {
            if (requestNo == 122) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(SendMoneyCashToMobileDifferentCountry.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            } else if (requestNo == 114) {
                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                tariffAmountFee = temp[0];
                showSendMoneyReview(tariffAmountFee);
            } else if (requestNo == 155) {
                firstPartResponse(generalResponseModel.getUserDefinedString(), generalResponseModel.getUserDefinedString2());
                agentIdentityDestinationNumber();
            } else if (requestNo == 157) {
                firstPartResponseDestnationNumber(generalResponseModel.getUserDefinedString(), generalResponseModel.getUserDefinedString2());
            } else {
                hideProgressDialog();
                Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }


        } else {
            hideProgressDialog();
            Toast.makeText(SendMoneyCashToMobileDifferentCountry.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }

    public void firstPartResponse(String localityResponseData, String creationCode) {
        hideProgressDialog();

        switch (creationCode) {

            case "0":

                String[] responseData = localityResponseData.split("\\|");

                sourceName_edittext_reveiwPage.setText(responseData[7]);
                SenderNameAgentIdentity = responseData[7];


                break;

            case "122":

                sourceName_edittext_reveiwPage.setText("");

                //  Toast.makeText(RemmettanceSendMoneyERAMobile.this," CASE 122 ",Toast.LENGTH_LONG).show();

                break;

        }






      /*  obj.getString("transid") + "|"     // locality   // 0
                + obj.getString("resultdescription") + "|"
                + obj.getString("comments") + "|"       // 2
                + obj.getString("responsects") + "|"
                + obj.getString("country") + "|"
                + obj.getString("state") + "|"          // 5
                + obj.getString("dateofbirth") + "|"    // 6
                + obj.getString("agentname") + "|"
                + obj.getString("gender") + "|"    // 8
                + obj.getString("idproof") + "|"
                + obj.getString("idproofissuedate") + "|"    // 10
                + obj.getString("fixphoneno") + "|"
                + obj.getString("idprooftype") + "|"
                + obj.getString("nationality") + "|"
                + obj.getString("customerid") + "|"
                + obj.getString("address") + "|"        // 15
                + obj.getString("clienttype") + "|"
                + obj.getString("birthplace") + "|"
                + obj.getString("idproofissueplace") + "|"
                + obj.getString("residencearea") + "|"
                + obj.getString("language") + "|"             // 20
                + obj.getString("secondmobphoneno") + "|"
                + obj.getString("profession"));    // 22*/


    }

    public void firstPartResponseDestnationNumber(String localityResponseData, String creationCode) {
        hideProgressDialog();

        switch (creationCode) {

            case "0":

                String[] responseData = localityResponseData.split("\\|");
                destinationname_TxtView_Review_AccToCash.setText(responseData[7]);
                destinationNameAgentIdentity = responseData[7];
                System.out.print(destinationNameAgentIdentity);


                break;

            case "122":

                destinationname_TxtView_Review_AccToCash.setText("");

                //  Toast.makeText(RemmettanceSendMoneyERAMobile.this," CASE 122 ",Toast.LENGTH_LONG).show();

                break;

        }






      /*  obj.getString("transid") + "|"     // locality   // 0
                + obj.getString("resultdescription") + "|"
                + obj.getString("comments") + "|"       // 2
                + obj.getString("responsects") + "|"
                + obj.getString("country") + "|"
                + obj.getString("state") + "|"          // 5
                + obj.getString("dateofbirth") + "|"    // 6
                + obj.getString("agentname") + "|"
                + obj.getString("gender") + "|"    // 8
                + obj.getString("idproof") + "|"
                + obj.getString("idproofissuedate") + "|"    // 10
                + obj.getString("fixphoneno") + "|"
                + obj.getString("idprooftype") + "|"
                + obj.getString("nationality") + "|"
                + obj.getString("customerid") + "|"
                + obj.getString("address") + "|"        // 15
                + obj.getString("clienttype") + "|"
                + obj.getString("birthplace") + "|"
                + obj.getString("idproofissueplace") + "|"
                + obj.getString("residencearea") + "|"
                + obj.getString("language") + "|"             // 20
                + obj.getString("secondmobphoneno") + "|"
                + obj.getString("profession"));    // 22*/


    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            SendMoneyCashToMobileDifferentCountry.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(SendMoneyCashToMobileDifferentCountry.this);
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
                SendMoneyCashToMobileDifferentCountry.this.finish();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

//    private int getCountrySelection() {
//        int ret = 0;
//        for (int i = 0; i < countryArray.length; i++) {
//            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
//                ret = i;
//            }
//        }
//        return ret;
//    }

    private int getCurrencySelection() {
        int ret = 0;
        for (int i = 0; i < currencyArray.length; i++) {
            if (currencyArray[i].equalsIgnoreCase(currencySelectionString)) {


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

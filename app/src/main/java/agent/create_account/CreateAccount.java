package agent.create_account;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import agent.activities.OTPVerificationActivity;
import agent.activities.R;
import callback.DateSetNotifier;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.DatePickerFragment;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptCreateAgent;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

/**
 * Created by Sahrique on 14/03/17.
 */

public class CreateAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener, DateSetNotifier {

    String commentString, idprooftype;
    int lengthToCheck;
    String agentName_reload,errorMsgToDisplay = "";

    String[] bankSelectionArray, transferTagArray, idProofArray, idProofCodeArray,
            accountTypeArray, planAccountNameLabel, planCodeArray, agentTypeCode,tempState,
            professionArray, genderArray, genderCodeArray,
            languageArray, languageCodeArray, cityArray, cityCodeArray, nationalityArray, nationalityCodeArray;

    Toolbar mToolbar;
    Button createAccountButton, nextButton_thirdform, idproof_calender_button, idproofExpiredDate_calender_button, dateOfBirth_calender_button;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, idProofNumberString, spinnerCountryCodeString, loyalityCasrdNoString,
            spinnerIdProofTypeTemp, subscriberNameString, emailString,
            agentCode, idProofPlaceString, birthpalceString, agentTypeString, secondMobileNumberString, fixPhonenoString, professionString,
            genderString, idproofDueDate, nationalityString, planNameString,
            residenceAreaString, spinnerCountryString, transferBasisString,
            accountNumber, cityString, spinnerStateString, countryString,
            addressString, confirmationCodeString,
            spinnerIdProofTypeString, spinnerLanguageTypeString,
            spinnerAccountToDebitString, mpinString,
            countrySelectionString = "", accountCodeString,

    idProofCodeString, genderCodeString, languageCodeString, professionCodeString, cityCodeString, nationalityCodeString;


    View viewForContainer;
    Button idproofIssueDate_calender_button, nextButton, previousButton, submitButton_partfirst;
    boolean isReview, isServerOperationInProcess, isOtherProfession;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0, idProofSelectedPosition,
            genderSelectedPosition, professionSelectedPosition,
            languageSelectedPosition, citySelectedPosition, nationalitySelectedPosition, countrySelectedPosition;
    private Spinner spinnerCountryNationality, spinnerCity, spinnerProfession, spinnerPlanAccountProfile, spinnerGenderType, spinnerState, spinnerCountry, spinnerAccountToDebit, transferBasisSpinner, spinnerIdProofType, spinnerLanguageType;
    private ScrollView scrollView_button_thirdform, scrollView_createAgent_thirdPage_form, scrollView_button_confirmationpage_mpin, scrollView_button_secondPart, scrollView_button_firstPart, scrollView_createAgent_firstPart, scrollView_createAgent_secondPart, scrollView_createAgent_confirmationPage;
    private AutoCompleteTextView otherProfessionEditText, idproofEditText_datePicker_manually, accountNoEditText_Part2, loyalityCardNumberEditTex, idProofPlaceEditText, birthpalceEditText, agentTypeEditText, secondMobileNumberEditText, fixPhonenoEditText,
            residenceAreaEditText, emailEditText, stateEditText, countryEditText, idProofNumberEditText, subscriberNameEditText, addressEditText, mpinEditText, accountNumberEditText;
    private TextView address_reviewpage,idProofNumberTextView, accountNumberTextViewReview, recipientCountryTxtView_Review, agentCodeTextView, accountNameTextView_Review, cityTextView_Review, languageTextView_Review, idproofTypeTextView_Review, countryTextView_Review, addressTextView_Review, idproofTxtView_Review, transferBasisTxtView_Review, recipientNameNoTxtView_Review, titleTextView, payerAccountTypeTxtView_Review;
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
                DataParserThread thread = new DataParserThread(CreateAccount.this, mComponentInfo, CreateAccount.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;
    String[] selectDateNew;

    int iLevel = 99;
    boolean isMiniStmtMode = false;
    public AutoCompleteTextView idproofIssuDate_EditText_manually, dateOfBirth_EditText_manullay, idProofExpiredDate_EditText_manually;
    String dateOfBirthString, idProofIssuDateString;
    String dateSetString;
    String idProofIssuDate;
    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

                if (scrollView_createAgent_firstPart.getVisibility() == View.VISIBLE) {
                    validateDetailsFirst();

                } else if (scrollView_createAgent_secondPart.getVisibility() == View.VISIBLE) {

                    validateDetailsSecond();

                } else if (scrollView_createAgent_thirdPage_form.getVisibility() == View.VISIBLE) {
                    validateDetailsThirdForm();
                }

                else if (scrollView_createAgent_confirmationPage.getVisibility() == View.VISIBLE) {
                    if (validationReviewpage()) {
                        createAccountRequest();
                    }
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


        setContentView(R.layout.create_account);

        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_createAccount);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");




        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.createAccount));
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

            //  countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

            SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
            countrySelectionString = prefs.getString("countrySelectionString", null);


//            cityArray = mComponentInfo.getmSharedPreferences().getString("cityList", "").split("\\;")[0].split("\\|");
//            cityCodeArray = mComponentInfo.getmSharedPreferences().getString("cityList", "").split("\\;")[1].split("\\|");
            genderArray = getResources().getStringArray(R.array.genderType);
            genderArray[0]=(getString(R.string.genderType));




            genderCodeArray = getResources().getStringArray(R.array.genderTypeCode);
            professionArray = getResources().getStringArray(R.array.professionArray);

            languageArray = getResources().getStringArray(R.array.TxnTypeLanguage);
            languageArray[0]=(getString(R.string.SubscriberLanguageNew));
            languageCodeArray = getResources().getStringArray(R.array.TxnTypeLanguageCode);
            idProofArray = getResources().getStringArray(R.array.IDProofTypeArray);
            idProofCodeArray = getResources().getStringArray(R.array.IDProofTypeCodeArray);

        } catch (Exception e) {
            e.printStackTrace();
            //  CreateAccount.this.finish();
        }

        nextButton = (Button) findViewById(R.id.nextButton_partSecond);
        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.GONE);

        scrollView_createAgent_secondPart = (ScrollView) findViewById(R.id.scrollView_createAgent_secondPart);
        scrollView_createAgent_firstPart = (ScrollView) findViewById(R.id.scrollView_createAgent_firstPart);

        scrollView_button_secondPart = (ScrollView) findViewById(R.id.scrollView_button_secondPart);
        scrollView_button_firstPart = (ScrollView) findViewById(R.id.scrollView_button_firstPart);
        scrollView_button_thirdform = (ScrollView) findViewById(R.id.scrollView_button_thirdform);
        scrollView_button_confirmationpage_mpin = (ScrollView) findViewById(R.id.scrollView_button_confirmationpage_mpin);


        nextButton_thirdform = (Button) findViewById(R.id.nextButton_thirdform);
        nextButton_thirdform.setOnClickListener(this);

        submitButton_partfirst = (Button) findViewById(R.id.submitButton_partfirst);
        submitButton_partfirst.setOnClickListener(this);

        scrollView_createAgent_confirmationPage = (ScrollView) findViewById(R.id.scrollView_createAgent_confirmationPage);

        scrollView_createAgent_thirdPage_form = (ScrollView) findViewById(R.id.scrollView_createAgent_thirdPage_form);

        agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);
        agentCodeTextView.setText("AgentCode:-  " + agentCode);


        accountNumberTextViewReview = (TextView) findViewById(R.id.accountNumberTextViewReview);
        idProofNumberTextView = (TextView) findViewById(R.id.idProofNumberTextView);
        address_reviewpage = (TextView) findViewById(R.id.address_reviewpage);

        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);

        spinnerCountry.setEnabled(false);
        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        countryArray[0] =(getString(R.string.nationalityNew));

        System.out.print(countryArray);
        spinnerCountryNationality = (Spinner) findViewById(R.id.spinnerCountryNationality);
        spinnerCountryNationality.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountryNationality.setOnItemSelectedListener(CreateAccount.this);

        spinnerPlanAccountProfile = (Spinner) findViewById(R.id.spinnerPlanAccountProfile);
        spinnerPlanAccountProfile.setOnItemSelectedListener(CreateAccount.this);

        spinnerProfession = (Spinner) findViewById(R.id.spinnerProfession);
        spinnerProfession.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, professionArray));

        spinnerProfession.setOnItemSelectedListener(CreateAccount.this);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        spinnerCity.setOnItemSelectedListener(CreateAccount.this);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(CreateAccount.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);

        spinnerIdProofType = (Spinner) findViewById(R.id.spinner_idProofType);
        spinnerIdProofType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, idProofArray));
        spinnerIdProofType.setOnItemSelectedListener(this);

        spinnerLanguageType = (Spinner) findViewById(R.id.spinner_LanguageType);
        spinnerLanguageType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languageArray));
        spinnerLanguageType.setOnItemSelectedListener(this);

        spinnerGenderType = (Spinner) findViewById(R.id.spinnerGenderType);
        spinnerGenderType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderArray));
        spinnerGenderType.setOnItemSelectedListener(this);


        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        //spinnerState.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArray));
        spinnerState.setOnItemSelectedListener(this);

        accountNumberEditText = (AutoCompleteTextView) findViewById(R.id.accountNumberEditText);
        accountNumberEditText.setOnEditorActionListener(this);

        loyalityCardNumberEditTex = (AutoCompleteTextView) findViewById(R.id.loyalityCardNumberEditTex);
        loyalityCardNumberEditTex.setOnEditorActionListener(this);

        emailEditText = (AutoCompleteTextView) findViewById(R.id.emailEditText_AccToCash);
        emailEditText.setOnEditorActionListener(this);

        stateEditText = (AutoCompleteTextView) findViewById(R.id.stateEditText_AccToCash);
        stateEditText.setOnEditorActionListener(this);

        idproof_calender_button = (Button) findViewById(R.id.idproof_calender_button);
        idproof_calender_button.setInputType(InputType.TYPE_NULL);
        idproof_calender_button.setOnTouchListener(this);
        otherProfessionEditText = (AutoCompleteTextView) findViewById(R.id.otherProfessionEditText_CreateAcc);

        idproofExpiredDate_calender_button = (Button) findViewById(R.id.idproofExpiredDate_calender_button);
        idproofExpiredDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofExpiredDate_calender_button.setOnTouchListener(this);

        dateOfBirth_calender_button = (Button) findViewById(R.id.dateOfBirth_calender_button);
        dateOfBirth_calender_button.setInputType(InputType.TYPE_NULL);
        dateOfBirth_calender_button.setOnTouchListener(this);

        idproofIssueDate_calender_button = (Button) findViewById(R.id.idproofIssueDate_calender_button);
        idproofIssueDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofIssueDate_calender_button.setOnTouchListener(this);

        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(this);

        idproofEditText_datePicker_manually = (AutoCompleteTextView) findViewById(R.id.idproofEditText_datePicker_manually);
        idproofEditText_datePicker_manually.setOnEditorActionListener(this);
        accountNoEditText_Part2 = (AutoCompleteTextView) findViewById(R.id.nameNumberEditText_CreateAcc_Part2);
        subscriberNameEditText = (AutoCompleteTextView) findViewById(R.id.subscriberNameEditText);
        idProofNumberEditText = (AutoCompleteTextView) findViewById(R.id.idProofNumberEditText);
        addressEditText = (AutoCompleteTextView) findViewById(R.id.address_EditText_AccToCash);
        addressEditText.setOnEditorActionListener(this);
        countryEditText = (AutoCompleteTextView) findViewById(R.id.countryEditText_AccToCash);
        countryEditText.setOnEditorActionListener(this);
        dateOfBirth_EditText_manullay = (AutoCompleteTextView) findViewById(R.id.dateOfBirth_EditText_manullay);
        idproofIssuDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idproofIssuDate_EditText_manually);
        idProofExpiredDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idProofExpiredDate_EditText_manually);
        idProofPlaceEditText = (AutoCompleteTextView) findViewById(R.id.idProofPlaceEditText_AccToCash);
        idProofPlaceEditText.setOnEditorActionListener(this);
        birthpalceEditText = (AutoCompleteTextView) findViewById(R.id.birthpalceEditText_AccToCash);
        birthpalceEditText.setOnEditorActionListener(this);
        agentTypeEditText = (AutoCompleteTextView) findViewById(R.id.agentTypeEditText_AccToCash);
        agentTypeEditText.setOnEditorActionListener(this);
        secondMobileNumberEditText = (AutoCompleteTextView) findViewById(R.id.secondMobileNumberEditText_AccToCash);
        secondMobileNumberEditText.setOnEditorActionListener(this);
        fixPhonenoEditText = (AutoCompleteTextView) findViewById(R.id.fixPhonenoEditText_AccToCash);
        fixPhonenoEditText.setOnEditorActionListener(this);
        residenceAreaEditText = (AutoCompleteTextView) findViewById(R.id.residenceAreaEditText_AccToCash);
        residenceAreaEditText.setOnEditorActionListener(this);
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

     /*   spinnerAccountToDebit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));
        spinnerState.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tempState));

*/
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        recipientCountryTxtView_Review = (TextView) findViewById(R.id.recipientCountry_TxtView_Review_AccToCash);
        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);
        recipientNameNoTxtView_Review = (TextView) findViewById(R.id.recipientNameNo_TxtView_Review_AccToCash);
        addressTextView_Review = (TextView) findViewById(R.id.address_TxtView_Review_AccToCash);
        idproofTxtView_Review = (TextView) findViewById(R.id.idproof_TxtView_Review_AccToCash);

        languageTextView_Review = (TextView) findViewById(R.id.LanguageType_TxtView_Review_AccToCash);
        idproofTypeTextView_Review = (TextView) findViewById(R.id.idproofType_TxtView_Review_AccToCash);


        accountNameTextView_Review = (TextView) findViewById(R.id.account_TxtView_Review_AccToCash);
        cityTextView_Review = (TextView) findViewById(R.id.city_TxtView_Review_AccToCash);
        countryTextView_Review = (TextView) findViewById(R.id.country_TxtView_Review_AccToCash);

        payerAccountTypeTxtView_Review = (TextView) findViewById(R.id.payerAccountType_TxtView_Review_AccToCash);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpinEditText);
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


        idproofIssuDate_EditText_manually.setOnEditorActionListener(this);
        residenceAreaEditText.setOnEditorActionListener(this);
        DataSetter d = new DataSetter(0);
        d.execute();

        setInputType(1);

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

                    cityArray = new String[tempCity.length + 1];
                    cityCodeArray = new String[tempCity.length + 1];

                    cityArray[0] = "Please Select City";
                    cityCodeArray[0] = "Please Select City";


                    try {

                    for (int i = 0; i < tempCity.length; i++) {

                        String cityDetailString = tempCity[i];
                        String[] tempCityDetailData = cityDetailString.split("\\;");
                        cityCodeArray[i + 1] = tempCityDetailData[0];
                        cityArray[i + 1] = tempCityDetailData[1];
                    }

                }
                catch (Exception e)
                {
                     // city data null
                    e.printStackTrace();
                }

                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();

            switch (viewGenerationCase) {
                case 0:


                    CreateAccount.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //selected item will look like a spinner set from XML
                            cityArray[0]=(getString(R.string.pleaseSelectCity));
                            spinnerCity.setAdapter(new ArrayAdapter<String>(CreateAccount.this, android.R.layout.simple_spinner_item, cityArray));

                        }
                    });
                    break;
            }
        }
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                accountNumberEditText.setText("");
                accountNumberEditText.setHint(getString(R.string.hintCreateAccountNumber));
                // accountNumberEditText.setFilters(null);
                accountNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                accountNumberEditText.setHint(String.format(getString(R.string.hintCreateAccountNumber), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                accountNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                accountNumberEditText.setFilters(digitsfilters);
                accountNumberEditText.setText("");


            } else if (i == 2) {
                accountNumberEditText.setText("");
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
                accountNumberEditText.setHint(getString(R.string.pleaseentername));
                accountNumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                accountNumberEditText.setFilters(digitsfilters);
                accountNumberEditText.setText("");
            }
        } else {
            Toast.makeText(CreateAccount.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:
                setInputType(i);


                break;

            case R.id.spinner_LanguageType:

                if (i != 0) {
                    spinnerLanguageTypeString = languageArray[i];

                    languageCodeString = languageCodeArray[i];
                    languageSelectedPosition = i;
                } else {
                    languageCodeString = "";
                    languageSelectedPosition = 0;
                }


                break;

            case R.id.spinner_idProofType:

                if (i != 0) {

                    spinnerIdProofTypeString = idProofArray[i];
                    idProofCodeString = idProofCodeArray[i];
                    idProofSelectedPosition = i;
                } else {

                    spinnerIdProofTypeString = "";
                    idProofCodeString = "";
                    idProofSelectedPosition = 0;
                }


                break;

            case R.id.spinnerGenderType:
                if (i != 0) {
                    genderString = genderArray[i];
                    genderCodeString = genderCodeArray[i];
                    genderSelectedPosition = i;

                } else {
                    genderString = "";
                    genderCodeString = "";
                    genderSelectedPosition = 0;

                }


                break;

            case R.id.spinnerCity:

                if (i != 0) {
                    citySelectedPosition = i;
                    cityString = cityArray[i];
                    cityCodeString = cityCodeArray[i];

                } else {
                    citySelectedPosition = 0;
                    cityString = "";
                    cityCodeString = "";

                }


                break;

            case R.id.spinnerPlanAccountProfile:

                planNameString = spinnerPlanAccountProfile.getSelectedItem().toString();
                System.out.print(planNameString);

                if (i > 0) {

                    planNameString = planCodeArray[i];

                    agentTypeString = spinnerPlanAccountProfile.getSelectedItem().toString();
                    agentTypeString = agentTypeCode[i];


                } else {

                }
                System.out.print(agentTypeString);
                System.out.print(planNameString);
                System.out.print(agentTypeString);
                System.out.print(planNameString);

                break;


            case R.id.spinnerCountry:

                validateDetailsPartFirst();
                break;

            case R.id.spinnerProfession:
                if (i != 0) {
                    professionString = professionArray[i];
                    professionCodeString = professionArray[i];
                    professionSelectedPosition = i;


                    isOtherProfession = professionCodeString.equalsIgnoreCase("others") ? true : false;
                    otherProfessionEditText.setVisibility(professionCodeString.equalsIgnoreCase("others") ? View.VISIBLE : View.GONE);

                } else {
                    professionString = "";
                    professionCodeString = "";
                    professionSelectedPosition = 0;

                }


                break;

            case R.id.spinnerCountryNationality:
                if (i != 0) {
                    nationalityString = countryArray[i];
                    nationalityCodeString = countryCodeArray[i];
                    nationalitySelectedPosition = i;

                } else {

                    nationalityString = "";
                    nationalityCodeString = "";
                    nationalitySelectedPosition = 0;
                }


                break;


        }
    }

    @Override
    public void onDateSet(final DatePicker var1, final String year, final String month, final String day) {
        CreateAccount.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (iLevel) {

                    case 0:
                      /*  idproofIssuDate_EditText_manually.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idProofIssueDate(dateSetString)) {
                        }
                        idproofIssuDate_EditText_manually.setText(dateSetString);*/

                        break;

                    case 1:
                      /*  dateOfBirth_EditText_manullay.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (dateOfBirthOrder(dateSetString)) {
                        }*/
                        break;

                    case 2:
                      /*  idProofExpiredDate_EditText_manually.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                       *//*  if(dateOfBirthDueDate(dateSetString)) {
                        }*/
                        break;

                    case 3:

                        idproof_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idProofDate(dateSetString)) {
                        }
                        idproofEditText_datePicker_manually.setText(dateSetString);

                        break;

                    case 4:
                        // idproofIssueDate_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idProofIssueDate(dateSetString)) {
                        }
                        break;

                    case 5:
                        //idproofExpiredDate_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idproofExpiredDate(dateSetString)) {
                        }
                        break;

                    case 6:
                        // dateOfBirth_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (dateOfBirthOrder(dateSetString)) {
                        }
                        break;


                }
            }
        });
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

         /*   case R.id.dateOfBirth_EditText_manullay:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 1;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;*/

            /*  case R.id.idproofIssuDate_EditText:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 0;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;*/

            case R.id.idProofExpiredDate_EditText_manually:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 2;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

            case R.id.idproof_calender_button:

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 3;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

            case R.id.idproofIssueDate_calender_button:

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 4;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;


            case R.id.idproofExpiredDate_calender_button:

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 5;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

            case R.id.dateOfBirth_calender_button:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 6;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

        }

        return false;
    }


    public boolean dateOfBirthOrder(String selectDate) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = selectDate.split("\\-");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    Toast.makeText(CreateAccount.this, "Please select Valid Date Of Birth", Toast.LENGTH_LONG).show();
                    dateOfBirth_EditText_manullay.setText("");
                    ret = false;//If start date is before end date
                } else {
                    dateOfBirth_EditText_manullay.setText(dateSetString);
                    dateOfBirth_EditText_manullay.setTextColor(Color.RED);
                }

            /* else if(simpleDateFormat.parse(currentDate).equals(simpleDateFormat.parse(selectDateNew)))
             {
                Toast.makeText(CreateAccount.this, "select date and current date are same", Toast.LENGTH_LONG).show();

                ret = true;//If two dates are equal
            }*/


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // return ret;


            // int resultDate = Integer.parseInt(dateOfBirthString);
      /*  System.out.println(resultDate);

        if (resultDate < currentDay && resultDate == currentYear && resultDate == currentMonth) {
            Toast.makeText(CreateAccount.this, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentMonth && resultDate == currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }
            */
            return ret;
        }
    }


    public boolean idProofDate(String selectDate) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = selectDate.split("\\-");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    Toast.makeText(CreateAccount.this, "Please select Valid Date Of Birth", Toast.LENGTH_LONG).show();
                    dateOfBirth_EditText_manullay.setText("");
                    ret = false;//If start date is before end date
                }

            /* else if(simpleDateFormat.parse(currentDate).equals(simpleDateFormat.parse(selectDateNew)))
             {
                Toast.makeText(CreateAccount.this, "select date and current date are same", Toast.LENGTH_LONG).show();

                ret = true;//If two dates are equal
            }*/


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // return ret;


            // int resultDate = Integer.parseInt(dateOfBirthString);
      /*  System.out.println(resultDate);

        if (resultDate < currentDay && resultDate == currentYear && resultDate == currentMonth) {
            Toast.makeText(CreateAccount.this, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentMonth && resultDate == currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }
            */
            return ret;
        }
    }

    public boolean idProofIssueDate(String selectDate) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = selectDate.split("\\-");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    Toast.makeText(CreateAccount.this, "Please select Valid ID Proof issue date", Toast.LENGTH_LONG).show();
                    idproofIssuDate_EditText_manually.setText("");
                    ret = false;//If start date is before end date
                } else {

                    idproofIssuDate_EditText_manually.setText(dateSetString);
                    idproofIssuDate_EditText_manually.setTextColor(Color.RED);
                }

            /* else if(simpleDateFormat.parse(currentDate).equals(simpleDateFormat.parse(selectDateNew)))
             {
                Toast.makeText(CreateAccount.this, "select date and current date are same", Toast.LENGTH_LONG).show();

                ret = true;//If two dates are equal
            }*/


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // return ret;


            // int resultDate = Integer.parseInt(dateOfBirthString);
      /*  System.out.println(resultDate);

        if (resultDate < currentDay && resultDate == currentYear && resultDate == currentMonth) {
            Toast.makeText(CreateAccount.this, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentMonth && resultDate == currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }
            */
            return ret;
        }
    }

    public boolean idproofExpiredDate(String selectDate) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = selectDate.split("\\-");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    //  Toast.makeText(CreateAccount.this, "Please select Valid Due Date", Toast.LENGTH_LONG).show();
                    idProofExpiredDate_EditText_manually.setText(dateSetString);

                    ret = false;//If start date is before end date
                } else {
                    idProofExpiredDate_EditText_manually.setText(dateSetString);
                    idProofExpiredDate_EditText_manually.setTextColor(Color.RED);
                }

            /* else if(simpleDateFormat.parse(currentDate).equals(simpleDateFormat.parse(selectDateNew)))
             {
                Toast.makeText(CreateAccount.this, "select date and current date are same", Toast.LENGTH_LONG).show();

                ret = true;//If two dates are equal
            }*/


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // return ret;


            // int resultDate = Integer.parseInt(dateOfBirthString);
      /*  System.out.println(resultDate);

        if (resultDate < currentDay && resultDate == currentYear && resultDate == currentMonth) {
            Toast.makeText(CreateAccount.this, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentMonth && resultDate == currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }
            */
            return ret;
        }
    }

    public boolean idProofDateOrder(String dateSetString) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = dateSetString.split("\\/");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    Toast.makeText(CreateAccount.this, "Please select Valid Id Proof Issue Date", Toast.LENGTH_LONG).show();
                    idproofIssuDate_EditText_manually.setText("");
                    ret = false;//If start date is before end date
                }

                  /* else if(simpleDateFormat.parse(currentDate).equals(simpleDateFormat.parse(selectDateNew)))
                  {
                     Toast.makeText(CreateAccount.this, "select date and current date are same", Toast.LENGTH_LONG).show();
                        ret = true;//If two dates are equal
                    }*/


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return ret;
        }
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void reviewPageCreateAccount() {

        CreateAccount.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //  hideKeyboard();
                titleTextView.setText(getString(R.string.pleasereviewdetail));

                scrollView_createAgent_secondPart.setVisibility(View.GONE);
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                scrollView_createAgent_thirdPage_form.setVisibility(View.GONE);
                scrollView_button_thirdform.setVisibility(View.GONE);

                scrollView_createAgent_confirmationPage.setVisibility(View.VISIBLE);
                //recipientCountryTxtView_Review.setText(spinnerCountryString);
                transferBasisTxtView_Review.setText(transferBasisString);
                recipientNameNoTxtView_Review.setText(accountNumber);
                addressTextView_Review.setText(addressString);
                // idproofTxtView_Review.setText(idproofString);
                idProofNumberTextView.setText(idProofNumberString);
                address_reviewpage.setText(addressString);

                languageTextView_Review.setText(spinnerLanguageTypeString);
                idproofTypeTextView_Review.setText(spinnerIdProofTypeString);
                accountNameTextView_Review.setText(subscriberNameString);
                cityTextView_Review.setText(cityString);
                countryTextView_Review.setText(spinnerCountryCodeString);
                // scrollView_button_secondPart.setVisibility(View.VISIBLE);

                accountNumberTextViewReview.setText(accountNumber);
                payerAccountTypeTxtView_Review.setText(spinnerAccountToDebitString);
                // nextButton.setText(getString(R.string.createAccount));
                scrollView_button_confirmationpage_mpin.setVisibility(View.VISIBLE);
                isReview = true;
                mpinEditText.requestFocus();
            }
        });
    }


    private boolean validationSecondForm() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            if (spinnerPlanAccountProfile.getSelectedItemPosition() != 0) {

                subscriberNameString = subscriberNameEditText.getText().toString().trim();
                if (subscriberNameString.length() >= 4) {

                    if (spinnerIdProofType.getSelectedItemPosition() != 0) {

                        idProofNumberString = idProofNumberEditText.getText().toString().toString();
                        if (idProofNumberString.length() >= 4) {

                            idProofIssuDateString = idproofIssuDate_EditText_manually.getText().toString().trim();

                            if (idProofIssuDateString.length() > 9 && idProofIssuDateString.matches("\\d{2}-\\d{2}-\\d{4}")) {


                                idProofPlaceString = idProofPlaceEditText.getText().toString().trim();
                                if (idProofPlaceString.length() >= 3) {


                                    idproofDueDate = idProofExpiredDate_EditText_manually.getText().toString().trim();
                                    if (idproofDueDate.length() >= 9) {

                                        ret = true;

                                    } else {
                                        Toast.makeText(CreateAccount.this, getString(R.string.pleaseidProofDueDate), Toast.LENGTH_LONG).show();
                                    }


                                } else {
                                    Toast.makeText(CreateAccount.this, getString(R.string.PleaseEnterIdProofPlace), Toast.LENGTH_LONG).show();
                                }


                            } else {
                                Toast.makeText(CreateAccount.this, getString(R.string.idproofIssueDateFormat), Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(CreateAccount.this, getString(R.string.idProofNumberCreateAccount), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(CreateAccount.this, getString(R.string.pleaseSelectIdProofType), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CreateAccount.this, getString(R.string.subscriberName), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(CreateAccount.this, getString(R.string.pleaseEnterAccountProfile), Toast.LENGTH_LONG).show();
            }

           /* } else {
                Toast.makeText(CreateAccount.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }*/
            /*} else {
                Toast.makeText(RemmettanceReceiveMoneyToMobile.this, getString(R.string.pleaseselectsendmode), Toast.LENGTH_LONG).show();
              }*/

        } else {
            Toast.makeText(CreateAccount.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetailsSecond() {
        if (!isReview) {
            if (validationSecondForm()) {

                nextThirdForm();
            }
        }/* else {
            if (validationReviewpage()) {
                createAccountRequest();
            }
        }*/
    }


    public void nextThirdForm() {

        scrollView_createAgent_secondPart.setVisibility(View.GONE);
        scrollView_button_thirdform.setVisibility(View.VISIBLE);
        scrollView_createAgent_thirdPage_form.setVisibility(View.VISIBLE);
        scrollView_button_secondPart.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_partSecond:

                validateDetailsSecond();

                break;

            case R.id.submitButton_partfirst:

                validateDetailsFirst();

                break;

            case R.id.nextButton_thirdform:

                if (validateDetailsThirdForm()) {

                    reviewPageCreateAccount();
                }
                break;

            case R.id.createAccountButton:

                if (validationReviewpage()) {
                    createAccountRequest();
                }
                break;


        }
    }

    public void validateDetailsFirst() {
        if (validateDetailsPartFirst()) {

            AgentIdentity();

        }
    }

    private boolean validateDetailsThirdForm() {
        boolean ret = false;


        spinnerCountryCodeString = spinnerCountry.getSelectedItem().toString();
        birthpalceString = birthpalceEditText.getText().toString().trim();
        fixPhonenoString = fixPhonenoEditText.getText().toString().trim();
        residenceAreaString = residenceAreaEditText.getText().toString().trim();
        emailString = emailEditText.getText().toString().trim();
        //     if (emailString.length() >= 4 && validateEmail(emailString)) {
        addressString = addressEditText.getText().toString().trim();
        //  if (addressString.length() >= 4) {
        dateOfBirthString = dateOfBirth_EditText_manullay.getText().toString().trim();
        // if (dateOfBirthString.length() > 9) {

                                                   /* nationalityString = nationalityEditText.getText().toString().trim();
                                                    if (nationalityString.length() >= 4) {
                 */

        countryString = countryEditText.getText().toString().trim();

        lengthToCheck = getMobileNoLength();
        secondMobileNumberString = secondMobileNumberEditText.getText().toString().trim();

        errorMsgToDisplay = String.format(getString(R.string.hintSecondMobileNumber), lengthToCheck + "");
        if (secondMobileNumberString.length() == lengthToCheck || secondMobileNumberString.equalsIgnoreCase("")) {

            errorMsgToDisplay = String.format(getString(R.string.hintSecondFixedMobileNumber), lengthToCheck + "");
            if (fixPhonenoString.length() == lengthToCheck || fixPhonenoString.equalsIgnoreCase("")) {

                ret = true;

            } else {
                Toast.makeText(CreateAccount.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                // ret = false;
            }

        } else {
            Toast.makeText(CreateAccount.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            // ret = false;
        }


        spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
        accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];
        spinnerIdProofTypeString = spinnerIdProofType.getSelectedItem().toString();
        spinnerLanguageTypeString = spinnerLanguageType.getSelectedItem().toString();
        genderString = spinnerGenderType.getSelectedItem().toString();



        return ret;
    }


    private boolean validateDetailsPartFirst() {
        boolean ret = false;


        lengthToCheck = getMobileNoLength();

        errorMsgToDisplay = String.format(getString(R.string.hintCreateAccountNumber), lengthToCheck + "");
        accountNumber = accountNumberEditText.getText().toString().trim();
        if (accountNumber.length() == lengthToCheck) {
            String dummmyMobileCheck="";

            for(int i=0; i<lengthToCheck;i++){

                dummmyMobileCheck=dummmyMobileCheck+"0";
            }


            if(!accountNumber.equalsIgnoreCase(dummmyMobileCheck)){
                ret = true;

            }else {
                Toast.makeText(CreateAccount.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(CreateAccount.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    private void createGetPlans() {
        if (new InternetCheck().isConnected(CreateAccount.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataGetPlans();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;


            // callApi("getPlans",requestData,156);

              new ServerTask(mComponentInfo, CreateAccount.this, mHandler, requestData, "getPlans", 156).start();

        } else {
            Toast.makeText(CreateAccount.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(CreateAccount.this,mComponentInfo, CreateAccount.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(CreateAccount.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(CreateAccount.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    private String generateDataGetPlans() {
        String jsonString = "";

          /*  {"agentCode":"23333336988869","requestcts":"25/05/2016 18:01:51","vendorcode":
            "MICR","clienttype":"GPRS","syncstatus":"1","agenttype":"SUB"}
*/

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("syncstatus", "1");    //  according to Bheem hardCode Set // 18072017
            //   countryObj.put("agenttype", "SUB");   // according to Bheem hardCode Set // 18072017


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private void createAccountRequest() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(CreateAccount.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataCreateAccount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

          //  callApi("CreateAgentInJSON",requestData,131);


            new ServerTask(mComponentInfo, CreateAccount.this, mHandler, requestData, "CreateAgentInJSON", 131).start();


        } else {
            Toast.makeText(CreateAccount.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private void AgentIdentity() {

        if (new InternetCheck().isConnected(CreateAccount.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentity();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

       //     callApi("getAgentIdentity",requestData,155);


           new ServerTask(mComponentInfo, CreateAccount.this, mHandler, requestData, "getAgentIdentity", 155).start();

        } else {
            Toast.makeText(CreateAccount.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    // {"agentCode":"237931349121","customerid":"ASDFSF65120320","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS","transtype":"CREATEAGENT"}


    private String generateDataAgentIdentity() {
        String jsonString = "";

        //String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            JSONObject countryObj = new JSONObject();
            accountNumber = getCountryPrefixCode() + accountNumber;
            countryObj.put("agentCode", accountNumber);
            countryObj.put("customerid", "");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "CREATEAGENT");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private String generateDataCreateAccount() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

                 /*{"agentCode":"237000215002","pin":"5BD1102B7D3DAA13CB978C0F5ED172CC",
                    "pintype":"MPIN","requestcts":"25/05/201618:01:51",
                    "source":"237200021016","vendorcode":"MICR",
                    "idproof":"U123654","idprooftype":"Pancard",
                    "language":"EN","address":"Estel",
                    "city":"Delhi","state":"Delhi","country":"CAM",
                    "email":"estel@esteltelecom.com",
                    "idproofissuedate":"25/05/2016 18:01:51","dob":"25/05/201618:01:51","idproofissueplace":"delhi",
                    "clienttype":"GPRS","accountname":"ESTEL",
                    "comments":"create","birthplace":"delhi",
                    "secondmobphoneno":"9990178903",
                    "agenttype":"SUB","fixphoneno":"9990178903",
                    "profession":"job","gender":"male",
                     "customerid":"123654","nationality":"Indial",
                     "plancode":"SUPERGOLD","residencearea":"delhi"}*/

            // Change Api  13/062017

        /* {"agentCode":"237000215002","pin":"5BD1102B7D3DAA13CB978C0F5ED172CC","pintype":"IPIN","requestcts":"25/05/2016 18:01:51",
         "source":"23723203515236316","vendorcode":"MICR","idproof":"U123654","idprooftype":"Pan card","language":"EN",
         "address":"Estel","city":"Delhi","state":"Delhi","country":"CAM","email":"estel@esteltelecom.com",
         "idproofissuedate":"25/05/2016 18:01:51","dob":"25/05/2016 18:01:51","idproofissueplace":"delhi","clienttype":"GPRS",
         "accountname":"ESTEL","comments":"create","birthplace":"delhi","secondmobphoneno":"9990178903","agenttype":"SUB",
         "fixphoneno":"9990178903","profession":"job","gender":"male","customerid":"123654","nationality":"Indial",
         "plancode":"SUPERGOLD","residencearea":"delhi","typeofcustomer":"MechantType","title":"Mishra","customerfirstname":"Rajesh",
         "maidenname":"xyz","birthdepartment":"Development","countryofbirth":"India","organisationissuingidproof":"Pancard",
         "legalcapacity":"making","dateoftakinglegalcapacityintoaccount":"14/05/2016 12:58:52","numberofchildren":"20",
         "clientfamilycode":"022M","suboccupation":"subocuupationvalue","countryofresidence":"India","companyname":"Estel",
         "acronym":"estel002","companystartdate":"13/05/2016 12:58:52","legalform":"legalform","commercialregisternumber":"56985",
         "commercialregisternumbervaliditydate":"13/05/2016 12:58:52","chamberofcommerce":"chamber","patennumber":"delhichamber",
         "validitydatelicensenumber":"3/05/2016 12:58:52","nationalidentitynumber":"AADHARCARD","taxidnumber":"023655555",
          "middlename":"Kumar","maritalstatus":"married","socialidentitynumber":"0236598",
           "dateoftakingintoaccountthelegalsituation":"14/05/2016 12:58:52","formationonspouse":"Yes"}

            New Tag Add

            countryObj.put("typeofcustomer", typeofcustomer);
            countryObj.put("MechantType", MechantType)
            countryObj.put("title", title)
            countryObj.put("customerfirstname", customerfirstname)
            countryObj.put("maidenname", maidenname)
            countryObj.put("birthdepartment", birthdepartment)
            countryObj.put("countryofbirth", countryofbirth)
            countryObj.put("organisationissuingidproof", organisationissuingidproof)
            countryObj.put("legalcapacity", legalcapacity)
            countryObj.put("dateoftakinglegalcapacityintoaccount", dateoftakinglegalcapacityintoaccount)
            countryObj.put("numberofchildren", numberofchildren)
            countryObj.put("clientfamilycode", clientfamilycode)
            countryObj.put("suboccupation", suboccupation)
            countryObj.put("countryofresidence", countryofresidence)
            countryObj.put("companyname", companyname)
            countryObj.put("acronym", acronym)
            countryObj.put("companystartdate", companystartdate)
            countryObj.put("legalform", legalform)
            countryObj.put("commercialregisternumber", commercialregisternumber)
            countryObj.put("commercialregisternumbervaliditydate", commercialregisternumbervaliditydate)
            countryObj.put("chamberofcommerce", chamberofcommerce)
            countryObj.put("patennumber", patennumber)
            countryObj.put("validitydatelicensenumber", validitydatelicensenumber)
            countryObj.put("nationalidentitynumber", nationalidentitynumber)
            countryObj.put("taxidnumber", taxidnumber)
            countryObj.put("middlename", middlename)
            countryObj.put("maritalstatus", maritalstatus)
            countryObj.put("socialidentitynumber", socialidentitynumber)
            countryObj.put("dateoftakingintoaccountthelegalsituation", dateoftakingintoaccountthelegalsituation)
            countryObj.put("formationonspouse", formationonspouse)

            */


            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("source", accountNumber);


            countryObj.put("idproof", idProofNumberString);


            countryObj.put("idprooftype", idProofCodeString);
            countryObj.put("language", languageCodeString);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("nationality", nationalityCodeString);


            countryObj.put("address", addressString);
            countryObj.put("city", cityCodeString);

            countryObj.put("state", cityCodeString);


            countryObj.put("country", countryCodeArray[getCountrySelection()]);
            countryObj.put("email", emailString);

            idProofIssuDateString = idProofIssuDateString + " 00:00:00";
            dateOfBirthString = dateOfBirthString + " 23:59:59";
            countryObj.put("idproofissuedate", idProofIssuDateString);

            countryObj.put("dob", dateOfBirthString);
            countryObj.put("idproofissueplace", idProofPlaceString);
            countryObj.put("clienttype", "GPRS");
            countryObj.put("accountname", subscriberNameString);
            countryObj.put("comments", commentString);
            countryObj.put("birthplace", birthpalceString);
            countryObj.put("secondmobphoneno", secondMobileNumberString);
            countryObj.put("agenttype", agentTypeString);
            countryObj.put("fixphoneno", fixPhonenoString);
            countryObj.put("profession", isOtherProfession ? professionCodeString : otherProfessionEditText.getText().toString());
            countryObj.put("gender", genderCodeString);
            countryObj.put("customerid", idproofDueDate);
            // countryObj.put("nationality", nationalityCodeString);
            countryObj.put("plancode", planNameString);
            countryObj.put("residencearea", residenceAreaString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private boolean validationReviewpage() {
        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;
        } else {
            Toast.makeText(CreateAccount.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

  /*  private void showSuccess(String data) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(RemmettanceReceiveMoneyToMobile.this);
            String temp[] = data.split("\\|");
            String resAmount = "", resDestination = "", resConfcode = "", resTxnID = "", resWalletBal = "";
            resConfcode = temp[0];
            resAmount = temp[1];
            resDestination = temp[2];
            resTxnID = temp[3];
            resWalletBal = temp[4];
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.RemmettanceReceiveMoneyToMobile));
        *//*Welcome to Express Union Mobile. Dear Customer, You have transferred #1 XAF to #2,
        code is #3 ID: #4, please inform the benef. your New Balance #5 XAF..*//*
            if (data.trim().length() != 0) {
                builder.setMessage(String.format(getString(R.string.accounttocashsuccess), resAmount, resDestination, resConfcode, resTxnID, resWalletBal));
            } else {
                builder.setMessage(getString(R.string.accounttocashsuccessconf) + "  " + data);
            }
            builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    successDialog.cancel();
                    RemmettanceReceiveMoneyToMobile.this.finish();
                }
            });
            successDialog = builder.create();
            successDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();

        Intent intent = new Intent(CreateAccount.this, SucessReceiptCreateAgent.class);
        intent.putExtra("data", data);
        startActivity(intent);
        CreateAccount.this.finish();
    }




/*    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.createAccount));
        // payerAccountString = payerAccountString.replace("_", "-");
        //  payerAccountString = payerAccountString.toUpperCase();

        builder.setMessage(getString(R.string.createAccountSucessReceipt) + " \n " + " \n " + data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                CreateAccount.this.finish();
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
            if (requestNo == 131) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(CreateAccount.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            } else if (requestNo == 155) {


                firstPartResponse(generalResponseModel.getUserDefinedString(), generalResponseModel.getUserDefinedString2());


            } else if (requestNo == 122) {
                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
            }
            else if (requestNo == 156) {

                String[] tempData = generalResponseModel.getUserDefinedString().split("\\$");

                String tempPlanAccountName = tempData[1];
                String tempPlanCode = tempData[2];
                String tempAgentType = tempData[3];

                planAccountNameLabel = tempPlanAccountName.split("\\|");  // plan profile Set in Spinner
                planCodeArray = tempPlanCode.split("\\|");
                agentTypeCode = tempAgentType.split("\\|");

                planAccountNameLabel[0]=(getString(R.string.selectAccountProfile));

                spinnerPlanAccountProfile.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, planAccountNameLabel));

                setInputType(1);
                //   AgentIdentity();

            }


        } else {
            hideProgressDialog();
            Toast.makeText(CreateAccount.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }

    class ViewGeneration extends AsyncTask<Void, Void, Void> {
        String serverResponse;

        ViewGeneration(String serverResponse) {
            this.serverResponse = serverResponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideProgressDialog();
            showProgressDialog(getString(R.string.pleasewait));


        }

        @Override
        protected Void doInBackground(Void... voids) {


            idProofSelectedPosition = 0;
            genderSelectedPosition = 0;
            languageSelectedPosition = 0;
            professionSelectedPosition = 0;
            citySelectedPosition = 0;
            nationalitySelectedPosition = 0;

            String[] responseData = serverResponse.split("\\|");
              agentName_reload = responseData[7];


            commentString = responseData[2];
            dateOfBirthString = responseData[6];

            genderString = responseData[8];

            for (int i = 0; i < genderCodeArray.length; i++) {

                if (genderString.equalsIgnoreCase(genderCodeArray[i])) {
                    genderSelectedPosition = i;
                    genderCodeString = genderCodeArray[i];
                }

            }

            cityString = responseData[5];  // state / city

            for (int i = 0; i < cityCodeArray.length; i++) {

                if (cityString.equalsIgnoreCase(cityCodeArray[i])) {
                    citySelectedPosition = i;
                    cityCodeString = cityCodeArray[i];
                }

            }

            idProofNumberString = responseData[9];
            idProofIssuDate = responseData[10];
            fixPhonenoString = responseData[11];
            idprooftype = responseData[12];

            for (int i = 0; i < idProofCodeArray.length; i++) {
                if (idprooftype.equalsIgnoreCase(idProofCodeArray[i])) {
                    idProofSelectedPosition = i;
                    idProofCodeString = idProofCodeArray[i];
                }

            }

            nationalityString = responseData[13];

            for (int i = 0; i < countryCodeArray.length; i++) {
                if (nationalityString.equalsIgnoreCase(countryCodeArray[i])) {
                    nationalitySelectedPosition = i;
                    nationalityCodeString = countryCodeArray[i];
                }

            }
            // nationalityEditText.setText(nationalityString);

            loyalityCasrdNoString = responseData[14];   // customer Id
            addressString = responseData[15];
            //   clientType = responseData[16];
            birthpalceString = responseData[17];
            idProofPlaceString = responseData[18];
            residenceAreaString = responseData[19];
            spinnerLanguageTypeString = responseData[20];

            for (int i = 0; i < languageCodeArray.length; i++) {

                if (spinnerLanguageTypeString.equalsIgnoreCase(languageCodeArray[i])) {
                    languageSelectedPosition = i;
                    languageCodeString = languageCodeArray[i];
                }
            }

            secondMobileNumberString = responseData[21];
            professionString = responseData[1];

            for (int i = 0; i < professionArray.length; i++) {

                if (professionString.equalsIgnoreCase(professionArray[i])) {
                    professionSelectedPosition = i;
                    professionCodeString = professionArray[i];
                }

            }
            // professionEditText.setText(professionString);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
            spinnerLanguageType.setSelection(languageSelectedPosition);
            spinnerIdProofType.setSelection(idProofSelectedPosition);
            spinnerCity.setSelection(citySelectedPosition);
            spinnerGenderType.setSelection(genderSelectedPosition);
            spinnerCountryNationality.setSelection(nationalitySelectedPosition);
            spinnerProfession.setSelection(professionSelectedPosition);
            titleTextView.setText(getString(R.string.provideSubscriberDetails));


            accountNoEditText_Part2.setText(accountNumber);
            dateOfBirth_EditText_manullay.setText(dateOfBirthString);
            idProofNumberEditText.setText(idProofNumberString);
            idproofIssuDate_EditText_manually.setText(idProofIssuDate);  // 20180003 format set
            fixPhonenoEditText.setText(fixPhonenoString);
            loyalityCardNumberEditTex.setText(loyalityCasrdNoString);
            loyalityCardNumberEditTex.setEnabled(false);
            addressEditText.setText(addressString);
            birthpalceEditText.setText(birthpalceString);
            idProofPlaceEditText.setText(idProofPlaceString);
            secondMobileNumberEditText.setText(secondMobileNumberString);


            subscriberNameEditText.setText(agentName_reload);

            createGetPlans();


        }

    }

    public void firstPartResponse(String localityResponseData, String creationCode) {
        hideProgressDialog();


        scrollView_createAgent_firstPart.setVisibility(View.GONE);
        scrollView_button_firstPart.setVisibility(View.GONE);
        scrollView_createAgent_secondPart.setVisibility(View.VISIBLE);
        scrollView_button_secondPart.setVisibility(View.VISIBLE);


        switch (creationCode) {

            case "0":
                ViewGeneration v = new ViewGeneration(localityResponseData);
                v.execute();


                break;


            case "122":
                createGetPlans();
                titleTextView.setText(getString(R.string.ProvideSubscriberDetails));
                loyalityCardNumberEditTex.setVisibility(View.GONE);
                accountNoEditText_Part2.setText(accountNumber);

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
            CreateAccount.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CreateAccount.this);
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
                CreateAccount.this.finish();
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

    private int getMobileNoLength() {
        int ret = 0;
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                ret = Integer.parseInt(countryMobileNoLengthArray[i]);
            }
        }
        return ret;
    }

    private String getCountryPrefixCode() {
        String ret = "";
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                ret = countryPrefixArray[i];
            }
        }
        return ret;
    }

    private boolean validateEmail(String input) {
        boolean ret = false;

        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (input.matches(emailPattern)) {
            // Toast.makeText(CreateAccount.this, "valid email address",Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(CreateAccount.this, "Invalid email address", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    private boolean validateAmount(String input) {
        boolean ret = false;
        try {
            int amt = Integer.parseInt(input);
            if (amt > 0) {
                ret = true;
                cityString = "" + amt;
            }
        } catch (Exception e) {
        }
        return ret;
    }
}

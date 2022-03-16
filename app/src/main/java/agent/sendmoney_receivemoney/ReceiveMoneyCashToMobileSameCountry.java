package agent.sendmoney_receivemoney;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import agent.activities.R;
import callback.DateSetNotifier;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.DatePickerFragment;
import commonutilities.InternetCheck;
import agent.activities.OTPVerificationActivity;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptReceiveMoneyToMobile;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

/**
 * Created by Sahrique on 14/03/17.
 */

public class ReceiveMoneyCashToMobileSameCountry extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener, DateSetNotifier {

    ImageView imageViewPicture, imageViewSign;
    LinearLayout LinearLayoutImageViewPicture, linearlayout_RecipientNameReview;
    int lengthToCheck;
    String errorMsgToDisplay = "";

    String[] recepienTypeCodeArray, recepientTypeArray, bankSelectionArray, transferTagArray, idProofArray, idProofCodeArray,
            accountTypeArray, planAccountNameLabel, planCodeArray, tempState,
            professionArray, genderArray, genderCodeArray,
            languageArray, languageCodeArray, cityArray, cityCodeArray, nationalityArray, nationalityCodeArray;

    Toolbar mToolbar;
    Button nextButton_thirdform, idproof_calender_button, idproofExpiredDate_calender_button, dateOfBirth_calender_button;
    ComponentMd5SharedPre mComponentInfo;


    String agentName,
            customerBirthPlaceString, customerbirthStreetString,
            idproofIssusePlaceString, customerfixMobielNoString,
            idProofNumberString, spinnerCountryCodeString, loyalityCasrdNoString,
            customerNameString, emailString, agentCode, customerIdString, birthpalceString,
            customerPhoneNumberString, customerSecondmobileNoString,
            professionString, genderString, nationalityString, planNameString,
            residenceAreaString, spinnerCountryString, transferBasisString,
            recipientMobileNoString, cityString, countryString,
            addressString, confirmationCodeString,
            spinnerIdProofTypeString, spinnerLanguageTypeString,
            spinnerAccountToDebitString, mpinString,
            countrySelectionString = "", accountCodeString,
            idProofCodeString, genderCodeString, languageCodeString,
            professionCodeString, cityCodeString, receiptCodeString, nationalityCodeString,
            commentString, currecneyString, tariffAmountFee, idprooftype, amountString;
    ;


    TextInputLayout mobileNumberEditText_TIL, receiptNameEditText_TIL;
    View viewForContainer;
    Button idproofIssueDate_calender_button, nextButton, previousButton, clickButton, submitButton_partfirst;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0, idProofSelectedPosition,
            genderSelectedPosition, professionSelectedPosition, receiptSelectedPosition,
            languageSelectedPosition, citySelectedPosition, nationalitySelectedPosition, countrySelectedPosition;
    private Spinner spinner_receipentType, spinnerRecepientType, spinnerCountryNationality, spinnerCity, spinnerProfession, spinnerPlanAccountProfile, spinnerGenderType, spinnerState, spinnerCountry, spinnerAccountToDebit, transferBasisSpinner, spinnerIdProofType, spinnerLanguageType;
    private ScrollView scrollView_button_thirdform, scrollView_createAgent_thirdPage_form, scrollView_button_secondPart, scrollView_button_firstPart, scrollView_createAgent_firstPart, review_SV_AccToCash;
    private AutoCompleteTextView reciepientNameEdittext, customerbirthStreetEditext, idproofissusePlaceEditText, confirmationCodeEditText, commentsEditText, amountEditText, idproofEditText_datePicker_manually, accountNoEditText_Part2, loyalityCardNumberEditTex, customerIdEditText, birthpalceEditText, agentTypeEditText, customerPhoneNumberEditTtext, customerfixMobielNoEdittext, customerSecondMobielnoEditText,
            residenceAreaEditText, emailEditText, stateEditText, countryEditText, idProofNumberEditText, customerNameEditText, addressEditText, mpinEditText, mobileNumberEditText;
    private TextView commentsTitleReview, recipientName_reviewPage, feestextView_receivemoney, idProofNumberTextView, accountNumberTextViewReview, recipientCountryTxtView_Review, agentCodeTextView, accountNameTextView_Review, cityTextView_Review, languageTextView_Review, idproofTypeTextView_Review, countryTextView_Review, addressTextView_Review, idproofTxtView_Review, amountTextviewReceipt, transferBasisTxtView_Review, recipientNameNo_TxtView_Review, titleTextView, payerAccountTypeTxtView_Review;
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
                DataParserThread thread = new DataParserThread(ReceiveMoneyCashToMobileSameCountry.this, mComponentInfo, ReceiveMoneyCashToMobileSameCountry.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;
    String[] selectDateNew;

    int iLevel = 99;
    boolean isMiniStmtMode = false;
    public AutoCompleteTextView idproofIssuDate_EditText_manually, dateOfBirth_EditText_manullay, idProofExpiredDate_EditText_manually;
    String dateOfBirthString;
    String dateSetString;
    String idProofIssuDate;
    boolean selectKeyboard = false;

    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try { //confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

                if (scrollView_createAgent_firstPart.getVisibility() == View.VISIBLE) {
                    validateDetailsFirst();
                } else if (scrollView_createAgent_thirdPage_form.getVisibility() == View.VISIBLE) {
                    validateDetailsThirdForm();
                } else if (review_SV_AccToCash.getVisibility() == View.VISIBLE) {

                    if (validationReviewpage()) {
                        ReceiveMoneyToMobileTransfer();
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

        setContentView(R.layout.receive_money_to_mobile_same_country);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_createAccount);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
       // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.receiveMoney));
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
//            cityArray = mComponentInfo.getmSharedPreferences().getString("cityList", "").split("\\;")[0].split("\\|");
//            cityCodeArray = mComponentInfo.getmSharedPreferences().getString("cityList", "").split("\\;")[1].split("\\|");
            genderArray = getResources().getStringArray(R.array.genderType);
            genderCodeArray = getResources().getStringArray(R.array.genderTypeCode);
            professionArray = getResources().getStringArray(R.array.professionArray);
            languageArray = getResources().getStringArray(R.array.TxnTypeLanguageReceiveMoney);
            languageCodeArray = getResources().getStringArray(R.array.TxnTypeLanguageCodReceiveMoney);


            idProofArray = getResources().getStringArray(R.array.IDProofTypeArrayRecevieMoney);
            idProofCodeArray = getResources().getStringArray(R.array.IDProofTypeCodeArrayReceiveMoney);

            recepientTypeArray = getResources().getStringArray(R.array.TxnTypeReceipntReceiveMoney);
            recepienTypeCodeArray = getResources().getStringArray(R.array.TxnTypeReceipntReceiveMoneyCode);

//            spinnerRecepientType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, recepientTypeArray));
//            spinnerRecepientType.setOnItemSelectedListener(this);


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

        //scrollView_createAgent_secondPart = (ScrollView) findViewById(R.id.scrollView_createAgent_secondPart);
        scrollView_createAgent_firstPart = (ScrollView) findViewById(R.id.scrollView_createAgent_firstPart);

        scrollView_button_secondPart = (ScrollView) findViewById(R.id.scrollView_button_secondPart);
        scrollView_button_firstPart = (ScrollView) findViewById(R.id.scrollView_button_firstPart);
        scrollView_button_thirdform = (ScrollView) findViewById(R.id.scrollView_button_thirdform);

        nextButton_thirdform = (Button) findViewById(R.id.nextButton_thirdform);
        nextButton_thirdform.setOnClickListener(this);

        submitButton_partfirst = (Button) findViewById(R.id.submitButton_partfirst);
        submitButton_partfirst.setOnClickListener(this);


        clickButton = (Button) findViewById(R.id.clickButton);
        clickButton.setOnClickListener(this);


        review_SV_AccToCash = (ScrollView) findViewById(R.id.review_SV_AccToCash);

        scrollView_createAgent_thirdPage_form = (ScrollView) findViewById(R.id.scrollView_createAgent_thirdPage_form);

        agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);
        agentCodeTextView.setText(getString(R.string.agentCodeNew) + " " + agentCode);


        accountNumberTextViewReview = (TextView) findViewById(R.id.accountNumberTextViewReview);
        idProofNumberTextView = (TextView) findViewById(R.id.idProofNumberTextView);

        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);

        spinnerCountry.setEnabled(false);
        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        countryArray[0] = "Nationality";

        System.out.print(countryArray);
        spinnerCountryNationality = (Spinner) findViewById(R.id.spinnerCountryNationality);
        spinnerCountryNationality.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountryNationality.setOnItemSelectedListener(ReceiveMoneyCashToMobileSameCountry.this);

        spinnerPlanAccountProfile = (Spinner) findViewById(R.id.spinnerPlanAccountProfile);
        spinnerPlanAccountProfile.setOnItemSelectedListener(ReceiveMoneyCashToMobileSameCountry.this);


        spinner_receipentType = (Spinner) findViewById(R.id.spinner_receipentType);
        spinner_receipentType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, recepientTypeArray));
        spinner_receipentType.setOnItemSelectedListener(ReceiveMoneyCashToMobileSameCountry.this);

        mobileNumberEditText_TIL = (TextInputLayout) findViewById(R.id.mobileNumberEditText_TIL);
        receiptNameEditText_TIL = (TextInputLayout) findViewById(R.id.receiptNameEditText_TIL);


        spinnerProfession = (Spinner) findViewById(R.id.spinnerProfession);
        spinnerProfession.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, professionArray));

        spinnerProfession.setOnItemSelectedListener(ReceiveMoneyCashToMobileSameCountry.this);


        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);


        spinnerCity.setOnItemSelectedListener(ReceiveMoneyCashToMobileSameCountry.this);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(ReceiveMoneyCashToMobileSameCountry.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);

        spinnerIdProofType = (Spinner) findViewById(R.id.spinner_idProofType);

        idProofArray[0] = getString(R.string.idproofType);

        spinnerIdProofType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, idProofArray));
        spinnerIdProofType.setOnItemSelectedListener(this);

        spinnerLanguageType = (Spinner) findViewById(R.id.spinner_LanguageType);
        spinnerLanguageType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languageArray));

        spinnerLanguageType.setOnItemSelectedListener(this);

        spinnerGenderType = (Spinner) findViewById(R.id.spinnerGenderType);


        genderArray[0] = getString(R.string.genderType);
        spinnerGenderType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderArray));

        spinnerGenderType.setOnItemSelectedListener(this);


        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        //spinnerState.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArray));
        spinnerState.setOnItemSelectedListener(this);

        mobileNumberEditText = (AutoCompleteTextView) findViewById(R.id.mobileNumberEditText);
        mobileNumberEditText.setOnEditorActionListener(this);


        idproofissusePlaceEditText = (AutoCompleteTextView) findViewById(R.id.idproofissusePlaceEditText);
        idproofissusePlaceEditText.setOnEditorActionListener(this);


        loyalityCardNumberEditTex = (AutoCompleteTextView) findViewById(R.id.loyalityCardNumberEditTex);
        loyalityCardNumberEditTex.setOnEditorActionListener(this);


        emailEditText = (AutoCompleteTextView) findViewById(R.id.emailEditText_AccToCash);
        emailEditText.setOnEditorActionListener(this);

        stateEditText = (AutoCompleteTextView) findViewById(R.id.stateEditText_AccToCash);
        stateEditText.setOnEditorActionListener(this);

        idproof_calender_button = (Button) findViewById(R.id.idproof_calender_button);
        idproof_calender_button.setInputType(InputType.TYPE_NULL);
        idproof_calender_button.setOnTouchListener(this);


        idproofExpiredDate_calender_button = (Button) findViewById(R.id.idproofExpiredDate_calender_button);
        idproofExpiredDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofExpiredDate_calender_button.setOnTouchListener(this);

        dateOfBirth_calender_button = (Button) findViewById(R.id.dateOfBirth_calender_button);
        dateOfBirth_calender_button.setInputType(InputType.TYPE_NULL);
        dateOfBirth_calender_button.setOnTouchListener(this);


        idproofIssueDate_calender_button = (Button) findViewById(R.id.idproofIssueDate_calender_button);
        idproofIssueDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofIssueDate_calender_button.setOnTouchListener(this);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        imageViewSign = (ImageView) findViewById(R.id.imageViewSign);
        LinearLayoutImageViewPicture = (LinearLayout) findViewById(R.id.LinearLayoutImageViewPicture);

        linearlayout_RecipientNameReview = (LinearLayout) findViewById(R.id.linearlayout_RecipientNameReview);
        recipientName_reviewPage = (TextView) findViewById(R.id.recipientName_reviewPage);

        commentsTitleReview = (TextView) findViewById(R.id.commentsTitleReview);


        idproofEditText_datePicker_manually = (AutoCompleteTextView) findViewById(R.id.idproofEditText_datePicker_manually);
        idproofEditText_datePicker_manually.setOnEditorActionListener(this);


        accountNoEditText_Part2 = (AutoCompleteTextView) findViewById(R.id.nameNumberEditText_CreateAcc_Part2);


        customerNameEditText = (AutoCompleteTextView) findViewById(R.id.customerNameEditText);


        idProofNumberEditText = (AutoCompleteTextView) findViewById(R.id.idProofNumberEditText);

        addressEditText = (AutoCompleteTextView) findViewById(R.id.addressEditText);
        addressEditText.setOnEditorActionListener(this);

        countryEditText = (AutoCompleteTextView) findViewById(R.id.countryEditText_AccToCash);
        countryEditText.setOnEditorActionListener(this);

        dateOfBirth_EditText_manullay = (AutoCompleteTextView) findViewById(R.id.dateOfBirth_EditText_manullay);
        idproofIssuDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idproofIssuDate_EditText_manually);
        idProofExpiredDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idProofExpiredDate_EditText_manually);


        customerIdEditText = (AutoCompleteTextView) findViewById(R.id.customerIdEditText);
        customerIdEditText.setOnEditorActionListener(this);

        birthpalceEditText = (AutoCompleteTextView) findViewById(R.id.birthpalceEditText_AccToCash);
        birthpalceEditText.setOnEditorActionListener(this);

        agentTypeEditText = (AutoCompleteTextView) findViewById(R.id.agentTypeEditText_AccToCash);
        agentTypeEditText.setOnEditorActionListener(this);


        customerPhoneNumberEditTtext = (AutoCompleteTextView) findViewById(R.id.customerPhoneNumberEditTtext);
        customerPhoneNumberEditTtext.setOnEditorActionListener(this);

        customerSecondMobielnoEditText = (AutoCompleteTextView) findViewById(R.id.customerSecondMobielnoEditText);
        customerSecondMobielnoEditText.setOnEditorActionListener(this);

        customerfixMobielNoEdittext = (AutoCompleteTextView) findViewById(R.id.customerfixMobielNoEdittext);
        customerfixMobielNoEdittext.setOnEditorActionListener(this);


        customerbirthStreetEditext = (AutoCompleteTextView) findViewById(R.id.customerbirthStreetEditext);
        customerbirthStreetEditext.setOnEditorActionListener(this);

        reciepientNameEdittext = (AutoCompleteTextView) findViewById(R.id.reciepientNameEdittext);
        reciepientNameEdittext.setOnEditorActionListener(this);


        amountEditText = (AutoCompleteTextView) findViewById(R.id.amountEditText_AccToCash);
        amountEditText.setOnEditorActionListener(this);

        /*amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_SHORT).show();
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
*/


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

        amountTextviewReceipt = (TextView) findViewById(R.id.amountTextviewReceipt);
        recipientNameNo_TxtView_Review = (TextView) findViewById(R.id.recipientNameNo_TxtView_Review);
        addressTextView_Review = (TextView) findViewById(R.id.address_TxtView_Review_AccToCash);
        idproofTxtView_Review = (TextView) findViewById(R.id.idproof_TxtView_Review_AccToCash);

        languageTextView_Review = (TextView) findViewById(R.id.LanguageType_TxtView_Review_AccToCash);
        idproofTypeTextView_Review = (TextView) findViewById(R.id.idproofType_TxtView_Review_AccToCash);


        accountNameTextView_Review = (TextView) findViewById(R.id.account_TxtView_Review_AccToCash);
        cityTextView_Review = (TextView) findViewById(R.id.city_TxtView_Review_AccToCash);
        countryTextView_Review = (TextView) findViewById(R.id.country_TxtView_Review_AccToCash);

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



        idproofIssuDate_EditText_manually.setOnEditorActionListener(this);
        residenceAreaEditText.setOnEditorActionListener(this);
        DataSetter d = new DataSetter(0);
        d.execute();
        setInputType(1);

        confirmationCodeEditText = (AutoCompleteTextView) findViewById(R.id.confirmationCodeEditText);
        confirmationCodeEditText.setOnEditorActionListener(this);

        commentsEditText = (AutoCompleteTextView) findViewById(R.id.comment_EditText_AccToCash);
        commentsEditText.setOnEditorActionListener(this);

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


                    try {


                        String cityData = mComponentInfo.getmSharedPreferences().getString("cityList", "'");
                        String[] tempCity = cityData.split("\\|");


                        cityArray = new String[tempCity.length + 1];
                        cityCodeArray = new String[tempCity.length + 1];

                        cityArray[0] = "Please Select City";
                        cityCodeArray[0] = "Please Select City";
                        for (int i = 0; i < tempCity.length; i++) {

                            String cityDetailString = tempCity[i];
                            String[] tempCityDetailData = cityDetailString.split("\\;");
                            cityCodeArray[i + 1] = tempCityDetailData[0];
                            cityArray[i + 1] = tempCityDetailData[1];


                        }
                    }
                    catch (Exception e)
                    {
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


                    ReceiveMoneyCashToMobileSameCountry.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //selected item will look like a spinner set from XML

                            spinnerCity.setAdapter(new ArrayAdapter<String>(ReceiveMoneyCashToMobileSameCountry.this, android.R.layout.simple_spinner_item, cityArray));

                        }
                    });


                    break;


            }
        }

    }


    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                mobileNumberEditText.setText("");
                mobileNumberEditText.setHint(getString(R.string.hintRecipientMobilePhone));
                // mobileNumberEditText.setFilters(null);
                mobileNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                mobileNumberEditText.setHint(String.format(getString(R.string.hintRecipientMobilePhone), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                mobileNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                mobileNumberEditText.setFilters(digitsfilters);
                mobileNumberEditText.setText("");


            } else if (i == 2) {
                mobileNumberEditText.setText("");
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
                mobileNumberEditText.setHint(getString(R.string.pleaseentername));
                mobileNumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                mobileNumberEditText.setFilters(digitsfilters);
                mobileNumberEditText.setText("");
            }
        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
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

                    spinnerLanguageTypeString = spinnerLanguageType.getSelectedItem().toString();

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
                    genderString = spinnerGenderType.getSelectedItem().toString();
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

                } else {

                }
                System.out.print(planNameString);

                break;


            case R.id.spinnerCountry:

                validateDetailsPartFirst();
                break;

            case R.id.spinner_receipentType:

                if (i == 1) {
                    //mobileNumberEditText.setVisibility(View.GONE);
                }
                if (i == 2) {
                    receiptNameEditText_TIL.setVisibility(View.VISIBLE);
                    mobileNumberEditText_TIL.setVisibility(View.GONE);
                }

      /*  if (i != 0) {

            receipentTypeString = spinner_receipentType.getSelectedItem().toString();
            receipentTypeString = recepientTypeArray[i];
            receiptCodeString = recepientTypeArray[i];
            receiptSelectedPosition = i;

        } else {
            receipentTypeString = "";
            receipentTypeString = "";
            receiptSelectedPosition = 0;

        }*/


                break;


            case R.id.spinnerProfession:
                if (i != 0) {
                    professionString = professionArray[i];
                    professionCodeString = professionArray[i];
                    professionSelectedPosition = i;

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
        ReceiveMoneyCashToMobileSameCountry.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, "Please select Valid Date Of Birth", Toast.LENGTH_LONG).show();
                    dateOfBirth_EditText_manullay.setText("");
                    ret = false;//If start date is before end date
                } else {
                    dateOfBirth_EditText_manullay.setText(dateSetString);
                    dateOfBirth_EditText_manullay.setTextColor(Color.RED);
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

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
                    Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, "Please select Valid Date Of Birth", Toast.LENGTH_LONG).show();
                    dateOfBirth_EditText_manullay.setText("");
                    ret = false;//If start date is before end date
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

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
                    Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, "Please select Valid ID Proof issue date", Toast.LENGTH_LONG).show();
                    idproofIssuDate_EditText_manually.setText("");
                    ret = false;//If start date is before end date
                } else {

                    idproofIssuDate_EditText_manually.setText(dateSetString);
                    idproofIssuDate_EditText_manually.setTextColor(Color.RED);
                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

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

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

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
                    Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, "Please select Valid Id Proof Issue Date", Toast.LENGTH_LONG).show();
                    idproofIssuDate_EditText_manually.setText("");
                    ret = false;//If start date is before end date
                }


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

    private void reviewPageReceiveMoney() {

        ReceiveMoneyCashToMobileSameCountry.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //    hideKeyboard();

                titleTextView.setText(getString(R.string.pleasereviewdetail));
                //scrollView_createAgent_secondPart.setVisibility(View.GONE);
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                scrollView_createAgent_thirdPage_form.setVisibility(View.GONE);
                scrollView_button_thirdform.setVisibility(View.GONE);
                review_SV_AccToCash.setVisibility(View.VISIBLE);
                System.out.print(amountString);


                amountTextviewReceipt.setText(amountString);
                transferBasisTxtView_Review.setText(amountString);
                commentsTitleReview.setText(commentString);

                //   recipientMobileNoString = getCountryPrefixCode() + recipientMobileNoString;

                recipientNameNo_TxtView_Review.setText(getCountryPrefixCode() + recipientMobileNoString);

                scrollView_button_secondPart.setVisibility(View.VISIBLE);
                recipientCountryTxtView_Review.setText(countrySelectionString);
                idproofTxtView_Review.setText(idProofNumberString);
                nextButton.setText(getString(R.string.receiveMoneyButtonNew));
                isReview = true;
                mpinEditText.requestFocus();

                // proceedTariffAmount();
            }
        });
    }


    private boolean validationSecondForm() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            amountString = amountEditText.getText().toString().trim();
            if (amountString.length() > 0 && validateAmount(amountString)) {

                confirmationCodeString = confirmationCodeEditText.getText().toString().trim();
                if (confirmationCodeString.length() >= 4) {
                    customerNameString = customerNameEditText.getText().toString().trim();
                    if (customerNameString.length() >= 2) {


                        ret = true;

                        //  amountString , confirmationCodeString,customerNameString , spinner_receipentType


                    } else {
                        Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.customerNameReceiveMoney), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.code), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.amountNew), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetailsSecond() {
        if (!isReview) {
            if (validationSecondForm()) {

                nextThirdForm();
            }
        } else {
            if (validationReviewpage()) {
                ReceiveMoneyToMobileTransfer();
            }
        }
    }

    public void nextThirdForm() {
        // scrollView_createAgent_secondPart.setVisibility(View.GONE);
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

                //  validateDetailsFirst();

                break;

            case R.id.clickButton:

                if (validationAgentIdentityMobileNumber()) {

                    AgentIdentity();

                }
                break;

            case R.id.nextButton_thirdform:

                if (validateDetailsThirdForm()) {
                    proceedTariffAmount();

                    // pictureSignatureServerRequest();
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


        lengthToCheck = getMobileNoLength();
        errorMsgToDisplay = String.format(getString(R.string.hintRecipientMobilePhone), lengthToCheck + "");
        recipientMobileNoString = mobileNumberEditText.getText().toString().trim();

        if (recipientMobileNoString.length() == lengthToCheck) {

            amountString = amountEditText.getText().toString().trim();
            if (amountString.length() > 0 && validateAmount(amountString)) {

                confirmationCodeString = confirmationCodeEditText.getText().toString().trim();
                if (confirmationCodeString.length() >= 4) {
                    customerNameString = customerNameEditText.getText().toString().trim();
                    if (customerNameString.length() >= 2) {


                        if (spinnerIdProofType.getSelectedItemPosition() != 0) {

                            idProofNumberString = idProofNumberEditText.getText().toString().toString();
                            if (idProofNumberString.length() >= 4) {


                                lengthToCheck = getMobileNoLength();
                                customerPhoneNumberString = customerPhoneNumberEditTtext.getText().toString().trim();
                                customerPhoneNumberString = customerPhoneNumberEditTtext.getText().toString().trim();
                                customerSecondmobileNoString = customerSecondMobielnoEditText.getText().toString().trim();
                                customerfixMobielNoString = customerfixMobielNoEdittext.getText().toString().trim();
                                addressString = addressEditText.getText().toString().trim();

                                dateOfBirthString = dateOfBirth_EditText_manullay.getText().toString().trim();
                                idProofIssuDate = idproofIssuDate_EditText_manually.getText().toString().trim();
                                idproofIssusePlaceString = idproofissusePlaceEditText.getText().toString().trim();
                                birthpalceString = birthpalceEditText.getText().toString().trim();
                                customerbirthStreetString = customerbirthStreetEditext.getText().toString().trim();

                                errorMsgToDisplay = String.format(getString(R.string.hintCustomerPhoneNumber), lengthToCheck + "");
                                if (customerPhoneNumberString.length() == lengthToCheck || customerPhoneNumberString.equalsIgnoreCase("")) {

                                    errorMsgToDisplay = String.format(getString(R.string.hintCustomerFixedMobileNumber), lengthToCheck + "");
                                    if (customerfixMobielNoString.length() == lengthToCheck || customerfixMobielNoString.equalsIgnoreCase("")) {

                                        errorMsgToDisplay = String.format(getString(R.string.hintCustomerSecondMobileNumber), lengthToCheck + "");
                                        if (customerSecondmobileNoString.length() == lengthToCheck || customerSecondmobileNoString.equalsIgnoreCase("")) {


                                            recipientMobileNoString = mobileNumberEditText.getText().toString().trim();
                                            amountString = amountEditText.getText().toString().trim();
                                            confirmationCodeString = confirmationCodeEditText.getText().toString().trim();
                                            customerNameString = customerNameEditText.getText().toString().trim();
                                            spinnerIdProofTypeString = spinnerIdProofType.getSelectedItem().toString();
                                            idProofNumberString = idProofNumberEditText.getText().toString().toString();


                                            commentString = commentsEditText.getText().toString().trim();
                                            customerIdString = customerIdEditText.getText().toString().trim();
                                            emailString = emailEditText.getText().toString().trim();
                                            customerPhoneNumberString = customerPhoneNumberEditTtext.getText().toString().trim();
                                            customerSecondmobileNoString = customerSecondMobielnoEditText.getText().toString().trim();
                                            customerfixMobielNoString = customerfixMobielNoEdittext.getText().toString().trim();
                                            addressString = addressEditText.getText().toString().trim();

                                            dateOfBirthString = dateOfBirth_EditText_manullay.getText().toString().trim();
                                            idProofIssuDate = idproofIssuDate_EditText_manually.getText().toString().trim();
                                            idproofIssusePlaceString = idproofissusePlaceEditText.getText().toString().trim();
                                            birthpalceString = birthpalceEditText.getText().toString().trim();
                                            customerbirthStreetString = customerbirthStreetEditext.getText().toString().trim();


                                            countryString = countryEditText.getText().toString().trim();
                                            spinnerCountryCodeString = spinnerCountry.getSelectedItem().toString();


                                            ret = true;
                                        } else {
                                            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                        }


                                    } else {
                                        Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.idProofNumberNew), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseSelectIdProofType), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.customerNameReceiveMoney), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.code), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.amountNew), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
        }

        return ret;
    }

    private boolean validationAgentIdentityMobileNumber() {
        boolean ret = false;


        lengthToCheck = getMobileNoLength();
        errorMsgToDisplay = String.format(getString(R.string.hintRecipientMobilePhone), lengthToCheck + "");
        recipientMobileNoString = mobileNumberEditText.getText().toString().trim();
        if (recipientMobileNoString.length() == lengthToCheck) {

            ret = true;

        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
        }

        return ret;
    }


    private boolean validateDetailsPartFirst() {
        boolean ret = false;

        // if (spinner_receipentType.getSelectedItemPosition() != 0) {


        lengthToCheck = getMobileNoLength();
        errorMsgToDisplay = String.format(getString(R.string.hintRecipientMobilePhone), lengthToCheck + "");
        recipientMobileNoString = mobileNumberEditText.getText().toString().trim();
        if (recipientMobileNoString.length() == lengthToCheck) {
            //   recipientMobileNoString = getCountryPrefixCode() + recipientMobileNoString;

            ret = true;


        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
        }


     /* } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.selectReceiptType), Toast.LENGTH_LONG).show();
        }*/


        return ret;
    }


    public void pictureSignatureServerRequest() {
        if (new InternetCheck().isConnected(ReceiveMoneyCashToMobileSameCountry.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generatePictureSign();

            //     callApi("getViewProfileInJSON",requestData,148);
            new ServerTask(mComponentInfo, ReceiveMoneyCashToMobileSameCountry.this, mHandler, requestData, "getViewProfileInJSON", 148).start();
        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void callApi(String apiName, String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.url) + apiName, new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(ReceiveMoneyCashToMobileSameCountry.this, mComponentInfo, ReceiveMoneyCashToMobileSameCountry.this, requestCode, response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

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
            countryObj.put("source", getCountryPrefixCode() + recipientMobileNoString);
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

    private void AgentIdentity() {

        if (new InternetCheck().isConnected(ReceiveMoneyCashToMobileSameCountry.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentity();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            //     callApi("getAgentIdentity",requestData,155);
            new ServerTask(mComponentInfo, ReceiveMoneyCashToMobileSameCountry.this, mHandler, requestData, "getAgentIdentity", 155).start();
        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    // {"agentCode":"237931349121","customerid":"ASDFSF65120320","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS","transtype":"CREATEAGENT"}


    private void ReceiveMoneyToMobileTransfer() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(ReceiveMoneyCashToMobileSameCountry.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateRecieveMoneyData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            //   callApi("getRemtReceiveTransactionInJSON",requestData,124);
            new ServerTask(mComponentInfo, ReceiveMoneyCashToMobileSameCountry.this, mHandler, requestData, "getRemtReceiveTransactionInJSON", 124).start();
        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateRecieveMoneyData() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("agentname", agentName);
            countryObj.put("destination", getCountryPrefixCode() + recipientMobileNoString);
            countryObj.put("amount", amountString);
            countryObj.put("confcode", confirmationCodeString);
            countryObj.put("destinationname", customerNameString);
            countryObj.put("idprooftype", idProofCodeString);
            countryObj.put("idproof", idProofNumberString);
            countryObj.put("comments", commentString);
            countryObj.put("customerid", customerIdString);
            countryObj.put("email", emailString);
            countryObj.put("phoneno", customerPhoneNumberString);
            countryObj.put("secondmobphoneno", customerSecondmobileNoString);
            countryObj.put("fixphoneno", customerfixMobielNoString);
            countryObj.put("address", addressString);
            countryObj.put("idproofissuedate", idProofIssuDate);
            countryObj.put("idproofissueplace", idproofIssusePlaceString);
            countryObj.put("dob", dateOfBirthString);
            countryObj.put("birthplace", customerBirthPlaceString);
            countryObj.put("street", customerbirthStreetString);
            countryObj.put("profession", professionCodeString);
            countryObj.put("gender", genderCodeString);
            countryObj.put("language", languageCodeString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private String generateDataAgentIdentity() {
        String jsonString = "";


        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", getCountryPrefixCode() + recipientMobileNoString);
            countryObj.put("customerid", "");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "REMTRECV");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private boolean validationReviewpage() {
        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;
        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("currecneyString", currecneyString);
        bundle.putString("countrySelectionString", countrySelectionString);
        bundle.putString("amountString", amountString);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("currecneyString", currecneyString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("spinnerCountryString", spinnerCountryString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("amountString", amountString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("commentString", commentString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("customerNameString", customerNameString).commit();

       // int amountPrintbuttonhide = Integer.parseInt(amountString);
        mComponentInfo.getmSharedPreferences().edit().putString("amountPrintbuttonhide", amountString).commit();


        Intent intent = new Intent(ReceiveMoneyCashToMobileSameCountry.this, SucessReceiptReceiveMoneyToMobile.class);
        intent.putExtra("data", data);

        intent.putExtra("currecneyString", currecneyString);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        intent.putExtra("amountString", amountString);
        intent.putExtra("amountPrintbuttonhide", amountString);
        intent.putExtra("customerNameString", customerNameString);


        startActivity(intent);
        ReceiveMoneyCashToMobileSameCountry.this.finish();
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;
        if (generalResponseModel.getResponseCode() == 0) { // || requestNo == 148)
            if (requestNo == 124) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(ReceiveMoneyCashToMobileSameCountry.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            } else if (requestNo == 155) {
                firstPartResponse(generalResponseModel.getUserDefinedString(), generalResponseModel.getUserDefinedString2());

            } else if (requestNo == 122) {
                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
            } else if (requestNo == 153) {
                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                tariffAmountFee = temp[0];

                try {
                    feestextView_receivemoney = (TextView) findViewById(R.id.feestextView_receivemoney);
                    feestextView_receivemoney.setText(tariffAmountFee);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                reviewPageReceiveMoney();

            } else if (requestNo == 156) {

                String[] tempData = generalResponseModel.getUserDefinedString().split("\\$");

                String tempPlanAccountName = tempData[1];
                String tempPlanCode = tempData[2];

                planAccountNameLabel = tempPlanAccountName.split("\\|");  // plan profile Set in Spinner
                planCodeArray = tempPlanCode.split("\\|");

                System.out.print(planAccountNameLabel);
                System.out.print(planCodeArray);

                spinnerPlanAccountProfile.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, planAccountNameLabel));

                setInputType(1);


            }


        } else {
            hideProgressDialog();
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
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
            customerSecondmobileNoString = responseData[11];
            customerNameString = responseData[7];


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
            customerIdString = responseData[18];
            residenceAreaString = responseData[19];
            spinnerLanguageTypeString = responseData[20];

            for (int i = 0; i < languageCodeArray.length; i++) {

                if (spinnerLanguageTypeString.equalsIgnoreCase(languageCodeArray[i])) {

                    languageSelectedPosition = i;
                    languageCodeString = languageCodeArray[i];
                }

            }

            customerPhoneNumberString = responseData[21];
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
            accountNoEditText_Part2.setText(recipientMobileNoString);
            dateOfBirth_EditText_manullay.setText(dateOfBirthString);
            idProofNumberEditText.setText(idProofNumberString);
            idproofIssuDate_EditText_manually.setText(idProofIssuDate);
            customerNameEditText.setText(customerNameString);
            customerSecondMobielnoEditText.setText(customerSecondmobileNoString);
            loyalityCardNumberEditTex.setText(loyalityCasrdNoString);
            loyalityCardNumberEditTex.setEnabled(false);
            addressEditText.setText(addressString);
            birthpalceEditText.setText(birthpalceString);
            customerIdEditText.setText(customerIdString);
            customerPhoneNumberEditTtext.setText(customerPhoneNumberString);
            commentsTitleReview.setText(commentString);

            // createGetPlans();

        }

    }

    void proceedTariffAmount() {
        if (new InternetCheck().isConnected(ReceiveMoneyCashToMobileSameCountry.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  callApi("getTarifInJSON",requestData,153);

            new ServerTask(mComponentInfo, ReceiveMoneyCashToMobileSameCountry.this, mHandler, requestData, "getTarifInJSON", 153).start();
        } else {
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateTariffAmmount() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

        /*{"agentCode":"237000271010","pin":"FAF618EBB45B1947DF7FDB9D0B8135F8","amount":"600",
        "transtype":"REMTRECV","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS","fromcity":"YDE",
        "tocity":"YDE","comments":"OM","accountType":"MA","destination":"237931349122","confcode":"PBFG"}
        */

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("amount", amountString);
            countryObj.put("transtype", "REMTRECV");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            countryObj.put("fromcity", "YDE");   //  change from server
            countryObj.put("tocity", "YDE");     //  Change from Server

            countryObj.put("comments", "");
            countryObj.put("udv1", "SAMEBRANCH");
            countryObj.put("accountType", "");
            countryObj.put("destination", getCountryPrefixCode() + recipientMobileNoString);
            countryObj.put("confcode", confirmationCodeString);


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


    public void firstPartResponse(String localityResponseData, String creationCode) {
        hideProgressDialog();

        scrollView_createAgent_firstPart.setVisibility(View.GONE);
        scrollView_button_firstPart.setVisibility(View.GONE);
        //scrollView_createAgent_secondPart.setVisibility(View.VISIBLE);
        //  scrollView_button_secondPart.setVisibility(View.VISIBLE);

        switch (creationCode) {

            case "0":
                ViewGeneration v = new ViewGeneration(localityResponseData);
                v.execute();
                break;

            case "122":
                //createGetPlans();

                // pictureSignatureServerRequest();

                titleTextView.setText(getString(R.string.provideSubscriberDetails));
                loyalityCardNumberEditTex.setVisibility(View.GONE);
                accountNoEditText_Part2.setText(recipientMobileNoString);

                break;

        }


    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            ReceiveMoneyCashToMobileSameCountry.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(ReceiveMoneyCashToMobileSameCountry.this);
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
                ReceiveMoneyCashToMobileSameCountry.this.finish();
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
            Toast.makeText(ReceiveMoneyCashToMobileSameCountry.this, "Invalid email address", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    private boolean validateAmount(String input) {
        boolean ret = false;
        try {

            if (input.trim().length() > 0) {

                if (input.contains(".")) {

                    double amt = Double.parseDouble(input);
                    if (amt > 0) {
                        amountString = "" + amt;
                        amountEditText.setText(amountString);
                        ret = true;
                    }

                } else {
                    int amt = Integer.parseInt(input);
                    if (amt > 0) {
                        amountString = "" + amt;
                        amountEditText.setText(amountString);

                        ret = true;

                    }

                }


            }


        } catch (Exception e) {
        }
        return ret;
    }

}

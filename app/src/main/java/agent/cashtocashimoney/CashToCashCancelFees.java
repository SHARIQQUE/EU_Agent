package agent.cashtocashimoney;

import android.app.DialogFragment;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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
import android.widget.EditText;
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
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptCashToCashCancel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

/**
 * Created by Sahrique on 14/03/17.
 */

public class CashToCashCancelFees extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener, View.OnTouchListener, DateSetNotifier {

    String[] idproofReceiveCashToCash_ArrayData, idproofReceiveCashToCash_Code, amountTempString, feesTempString, selectDateNew, currencyArray, bankSelectionArray, transferTagArray, accountTypeArray, responseArray;
    Toolbar mToolbar;
    Double  amountAdd,tariffFees, tempAddFees;

    String[] arrrString;
    int selectButtonfees;
    int iLevel = 99;
    LinearLayout linearLayout_tariff;
    TextView referenceNumber_textView,senderName_TxtView_ReviewPage, recipientName_TxtView_ReviewPage, tariffAmmount_EdittextReview_AccToCash, currency_TxtView_secondPage;

    String idProofCashToCash_code, idProofCashToCash_name, destinationMobileNoString, currencySearchString, amountSearchNoToWord, sourceNumberString, senderNameString, dateSetString, idProofNumberString, currencySelectionString, toCity, refrenceNumberString, tariffAmountFee;
    Button idproofIssueDate_calender_button, button_search_receipientName, button_submit_confirmationPage, cancelWithoutFees_button, cancelWithFees_button;
    ComponentMd5SharedPre mComponentInfo;
    String idProofIssuDateString, agentName, recipientNameString, confirmationCodeString, commentString,
            agentCode, spinnerCountryString, mobileNumberString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    Button nextButton_cashout, previousButton, button_secondPage;
    boolean isReview, isServerOperationInProcess;
    ImageView imageViewPicture, imageViewSign;
    private Spinner spinnerCountry, spinnerIdProofType, spinnerCountryDestination, spinnerAccountToDebit, transferBasisSpinner;
    private ScrollView scrollview_first_page, scrollview_second_page, scrollview_review_page;
    private AutoCompleteTextView idproofIssuDate_EditText_manually, idProofNumberEditText, sourceMobileNumberEditText, destination_NumberEditText, detailsEditText, mpinEditText, amountEditText_AccToCash, comment_EditText, senderName_editText, recipientMobile_EditText, recipientName_editText, confirmationCode_EditText, mobileNumberEditText;
    private TextView recipientCountryTxtView_Review, transferBasisTxtView_Review, recipientMobileno_TxtView_Review, recipientNameNoTitleTxtView_Review, amount_textview_ReviewPage, recipientNo_TxtView_Review,
            recipientAccountTypeTxtView_Review, payerBankTxtView_Review, titleTextView,
            payerAccountTypeTxtView_Review, amount_TxtView_secondPage, recipientBankTxtView_Review, transferTagTxtView_Review;
    private ProgressDialog mDialog;
    String[] oneToNineArray,hundredToCroreArray,tenToNinteenArray,twentyToNinty;

    String stringNumberWord;
    int numberToWord;


    boolean selectKeyboard = false;
    EditText senderName_TxtView_secondPage, senderMobileno_editext_secondPage, receiptName_edittext_secondPage, receiptMobileNo_editText_secondPage;

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
                DataParserThread thread = new DataParserThread(CashToCashCancelFees.this, mComponentInfo, CashToCashCancelFees.this, message.arg1, message.obj.toString());
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

                if (selectKeyboard == false) {


                    // firstPageValidationRequest();   // no need beause two button

                }
                else {
                    secondPageValidationRequest();
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


        setContentView(R.layout.cashtocash_cancel_fee);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.pleaseSelectAccountTypeCreated);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MoneyTransfer);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
     //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashfortransferSendCashCasncel));
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

            CashToCashCancelFees.this.finish();
        }


        oneToNineArray = getResources().getStringArray(R.array.oneToNineArray);
        hundredToCroreArray = getResources().getStringArray(R.array.hundredToCroreArray);
        tenToNinteenArray = getResources().getStringArray(R.array.tenToNinteenArray);
        twentyToNinty = getResources().getStringArray(R.array.twentyToNinty);


        nextButton_cashout = (Button) findViewById(R.id.nextButton_cashout);
        nextButton_cashout.setOnClickListener(this);

        button_secondPage = (Button) findViewById(R.id.button_secondPage);
        button_secondPage.setOnClickListener(this);


        // nextButton_cashout.setVisibility(View.VISIBLE);

        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);
        previousButton.setOnClickListener(this);
        previousButton.setVisibility(View.GONE);

        cancelWithFees_button = (Button) findViewById(R.id.cancelWithFees_button);
        cancelWithFees_button.setOnClickListener(this);

        button_search_receipientName = (Button) findViewById(R.id.button_search_receipientName);
        button_search_receipientName.setOnClickListener(this);

        cancelWithoutFees_button = (Button) findViewById(R.id.cancelWithoutFees_button);
        cancelWithoutFees_button.setOnClickListener(this);


        button_submit_confirmationPage = (Button) findViewById(R.id.button_submit_confirmationPage);
        button_submit_confirmationPage.setOnClickListener(this);


        scrollview_first_page = (ScrollView) findViewById(R.id.scrollview_first_page);
        scrollview_second_page = (ScrollView) findViewById(R.id.scrollview_second_page);
        scrollview_review_page = (ScrollView) findViewById(R.id.scrollview_review_page);

        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);
        spinnerCountry.setVisibility(View.GONE);
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        tariffAmmount_EdittextReview_AccToCash = (TextView) findViewById(R.id.tariffAmmount_EdittextReview_AccToCash);


        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));

        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(CashToCashCancelFees.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);

        mobileNumberEditText = (AutoCompleteTextView) findViewById(R.id.mobileNumberEditText);
        mobileNumberEditText.setHint(getString(R.string.PleasEenterMobileNumber));

        detailsEditText = (AutoCompleteTextView) findViewById(R.id.detailsEditText);
        detailsEditText.setOnEditorActionListener(this);


        senderName_editText = (AutoCompleteTextView) findViewById(R.id.senderName_editText);
        senderName_editText.setOnEditorActionListener(this);

        recipientMobile_EditText = (AutoCompleteTextView) findViewById(R.id.recipientMobile_EditText);
        recipientMobile_EditText.setOnEditorActionListener(this);

        recipientName_editText = (AutoCompleteTextView) findViewById(R.id.recipientName_editText);
        recipientName_editText.setOnEditorActionListener(this);

        confirmationCode_EditText = (AutoCompleteTextView) findViewById(R.id.confirmationCode_EditText);
        confirmationCode_EditText.setOnEditorActionListener(this);


        amountEditText_AccToCash = (AutoCompleteTextView) findViewById(R.id.amountEditText_AccToCash);
        amountEditText_AccToCash.setOnEditorActionListener(this);

        amountEditText_AccToCash.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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





        comment_EditText = (AutoCompleteTextView) findViewById(R.id.comment_EditText);
        comment_EditText.setOnEditorActionListener(this);


        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        imageViewSign = (ImageView) findViewById(R.id.imageViewSign);

        sourceMobileNumberEditText = (AutoCompleteTextView) findViewById(R.id.sourceMobileNumberEditText);
        sourceMobileNumberEditText.setHint(getString(R.string.PleasEenterMobileNumber));

        destination_NumberEditText = (AutoCompleteTextView) findViewById(R.id.destination_NumberEditText);
        destination_NumberEditText.setHint(getString(R.string.PleasEnterDestinationMobilePhoneNumber));


        senderMobileno_editext_secondPage = (EditText) findViewById(R.id.senderMobileno_editext_secondPage);
        senderMobileno_editext_secondPage.setEnabled(false);

        senderName_TxtView_secondPage = (EditText) findViewById(R.id.senderName_TxtView_secondPage);
        senderName_TxtView_secondPage.setEnabled(false);

        receiptMobileNo_editText_secondPage = (EditText) findViewById(R.id.receiptMobileNo_editText_secondPage);
        receiptMobileNo_editText_secondPage.setEnabled(false);

        receiptName_edittext_secondPage = (EditText) findViewById(R.id.receiptName_edittext_secondPage);
        receiptName_edittext_secondPage.setEnabled(false);

        senderName_TxtView_ReviewPage = (TextView) findViewById(R.id.senderName_TxtView_ReviewPage);
        senderName_TxtView_ReviewPage.setEnabled(false);

        referenceNumber_textView = (TextView) findViewById(R.id.referenceNumber_textView);
        linearLayout_tariff = (LinearLayout) findViewById(R.id.linearLayout_tariff);



        recipientName_TxtView_ReviewPage = (TextView) findViewById(R.id.recipientName_TxtView_ReviewPage);
        recipientName_TxtView_ReviewPage.setEnabled(false);

        spinnerIdProofType = (Spinner) findViewById(R.id.spinnerIdProofType);


        idproofReceiveCashToCash_ArrayData = getResources().getStringArray(R.array.idproofReceiveCashToCash_ArrayData);
        idproofReceiveCashToCash_Code = getResources().getStringArray(R.array.idproofReceiveCashToCash_Code);

        spinnerIdProofType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, idproofReceiveCashToCash_ArrayData));
        spinnerIdProofType.setOnItemSelectedListener(this);


        idproofIssueDate_calender_button = (Button) findViewById(R.id.idproofIssueDate_calender_button);
        idproofIssueDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofIssueDate_calender_button.setOnTouchListener(this);


        spinnerCountryDestination = (Spinner) findViewById(R.id.spinnerCountryDestination);
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter2 = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountryDestination.setAdapter(adapter2);
        spinnerCountryDestination.setEnabled(false);
        spinnerCountryDestination.setSelection(getCountrySelection());
        spinnerCountryDestination.requestFocus();
        spinnerCountryDestination.setOnItemSelectedListener(this);

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 0) {
            String[] data = bankAccounts.split("\\;");

            ArrayList<String> accountList = new ArrayList<String>();
            ArrayList<String> accountCodeList = new ArrayList<String>();
            accountList.add(getString(R.string.pleaseSelectAccountCredit));
            accountCodeList.add(getString(R.string.pleaseSelectAccountCredit));

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
            payerBankAccountsArray[0] = getString(R.string.pleaseSelectAccountCredit);
            payerAccountCodeArray = new String[1];
            payerAccountCodeArray[0] = getString(R.string.pleaseSelectAccountCredit);
        }

        //  spinnerAccountToDebit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));

        titleTextView = (TextView) findViewById(R.id.titleTextView_AccToCash);

        recipientCountryTxtView_Review = (TextView) findViewById(R.id.recipientCountry_TxtView_Review_AccToCash);
        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);
        recipientNo_TxtView_Review = (TextView) findViewById(R.id.recipientNo_TxtView_Review);
        amount_textview_ReviewPage = (TextView) findViewById(R.id.amount_textview_ReviewPage);
        recipientMobileno_TxtView_Review = (TextView) findViewById(R.id.recipientMobileno_TxtView_Review);
        amount_TxtView_secondPage = (TextView) findViewById(R.id.amount_TxtView_secondPage);
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



        currency_TxtView_secondPage = (TextView) findViewById(R.id.currency_TxtView_secondPage);
        idProofNumberEditText = (AutoCompleteTextView) findViewById(R.id.idProofNumberEditText);

        idproofIssuDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idproofIssuDate_EditText_manually);
        idproofIssuDate_EditText_manually.setOnEditorActionListener(this);


        // spinnerCurrency.setSelection(getCurrencySelection());

        //setInputType(1);
        // setInputTypeDestination(1);
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[getCountrySelection()]));
                sourceMobileNumberEditText.setHint(String.format(getString(R.string.hintSendMoneyPhoneNumber), countryMobileNoLengthArray[getCountrySelection()] + " "));
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
        } else {
            Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    private void setInputTypeDestination(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {


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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[getCountrySelection()]));
                destination_NumberEditText.setHint(String.format(getString(R.string.hintSendMoneyPhoneNumberRecipient), countryMobileNoLengthArray[getCountrySelection()] + " "));
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
            Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:
                setInputType(position);
                break;

            case R.id.spinner_PayerAccountTypeSelection_AccToAcc:

                break;

            case R.id.spinnerCountry:
                //   setInputType(transferBasisSpinner.getSelectedItemPosition());
                setInputType(1);
                break;

            case R.id.spinnerCountryDestination:
                setInputTypeDestination(1);
                break;

            case R.id.spinnerIdProofType:

                //  String selectedValue = adapterView.getItemAtPosition(position).toString();
                // Toast.makeText(adapterView.getContext(), "selected Value = " + selectedValue, Toast.LENGTH_LONG).show();

                idProofCashToCash_code = spinnerIdProofType.getSelectedItem().toString();

                idProofCashToCash_name = idproofReceiveCashToCash_ArrayData[position];
                idProofCashToCash_code = idproofReceiveCashToCash_Code[position];

                System.out.println(idProofCashToCash_name);
                System.out.println(idProofCashToCash_code);

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

    private boolean validationSearch() {
        boolean ret = false;


        refrenceNumberString = detailsEditText.getText().toString().trim();

        if (refrenceNumberString.length() > 0) {

            ret = true;

        } else {
            Toast.makeText(CashToCashCancelFees.this, getString(R.string.enter_details), Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.nextButton_cashout:

                break;

            case R.id.button_search_receipientName:


                break;


            case R.id.cancelWithoutFees_button:

                firstPageValidationRequest();
                selectButtonfees=0;

                break;


            case R.id.cancelWithFees_button:



                firstPageValidationRequest();

                selectButtonfees=1;

                break;



            case R.id.button_secondPage:

                secondPageValidationRequest();

                break;

            case R.id.button_submit_confirmationPage:

                //  proceedConformationPage();

                break;

        }
    }

    public void secondPageValidationRequest() {

        if (validationSecondPage()) {

            if(selectButtonfees==0)
            {
                cancelRequestWithoutFeesApi();
            }
            else
            {
                cancelRequestWithFeesApi();
            }

        }
    }


    void reviewpage() {

        scrollview_second_page.setVisibility(View.GONE);
        scrollview_review_page.setVisibility(View.VISIBLE);

        recipientNo_TxtView_Review.setText(sourceNumberString);
        amount_textview_ReviewPage.setText(amountTempString[0]);
        recipientMobileno_TxtView_Review.setText(destinationMobileNoString);

        feesTempString = responseArray[7].split("\\.");    // server data 200.000
        tariffAmmount_EdittextReview_AccToCash.setText(feesTempString[0]);
        // receiptName_edittext_secondPage.setText(recipientNameString);

        senderName_TxtView_ReviewPage.setText(senderNameString);
        recipientName_TxtView_ReviewPage.setText(recipientNameString);


        selectKeyboard = true;

    }


    void cancelRequestWithFeesApi() {
        if (new InternetCheck().isConnected(CashToCashCancelFees.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateCancelWithFees();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

          //  callApi("ImoneyReversalJson",requestData,167);

            new ServerTask(mComponentInfo, CashToCashCancelFees.this, mHandler, requestData, "ImoneyReversalJson", 167).start();

        } else {
            Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(CashToCashCancelFees.this,mComponentInfo,CashToCashCancelFees.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(CashToCashCancelFees.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(CashToCashCancelFees.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }

    void cancelRequestWithoutFeesApi() {
        if (new InternetCheck().isConnected(CashToCashCancelFees.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateCancelWithoutFees();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

         //   callApi("ImoneyReversalJson",requestData,166);

            new ServerTask(mComponentInfo, CashToCashCancelFees.this, mHandler, requestData, "ImoneyReversalJson", 166).start();

        } else {
            Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validationSecondPage() {
        boolean ret = false;


        confirmationCodeString = confirmationCode_EditText.getText().toString();


        System.out.print(idProofCashToCash_code);
        if (spinnerIdProofType.getSelectedItemPosition() != 0) {

            idProofNumberString = idProofNumberEditText.getText().toString().toString();
            if (idProofNumberString.length() > 6) {


                idProofIssuDateString = idproofIssuDate_EditText_manually.getText().toString().trim();
                if (idProofIssuDateString.length() > 9 && idProofIssuDateString.matches("\\d{2}-\\d{2}-\\d{4}")) {


                    mpinString = mpinEditText.getText().toString().trim();
                    if (mpinString.length() == 4) {



                        ret = true;
                    }

                    else {
                        Toast.makeText(CashToCashCancelFees.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
                    }



                    senderNameString = senderName_TxtView_secondPage.getText().toString().trim();
                    recipientNameString = receiptName_edittext_secondPage.getText().toString().trim();
                    sourceNumberString = senderMobileno_editext_secondPage.getText().toString().trim();
                    destinationMobileNoString = receiptMobileNo_editText_secondPage.getText().toString().trim();
                    commentString = comment_EditText.getText().toString().trim();



                } else {
                    Toast.makeText(CashToCashCancelFees.this, getString(R.string.idproofDateFormat), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CashToCashCancelFees.this, getString(R.string.idProofNumberCreateAccount), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseSelectIdProofType), Toast.LENGTH_LONG).show();
        }

        return ret;
    }


    public void firstPageValidationRequest() {
        selectKeyboard = true;

        if (validationSearch()) {
            if (new InternetCheck().isConnected(CashToCashCancelFees.this)) {

                searchApiCashToCash();

            } else {
                Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
            }

        }
    }


    /*private boolean validateAmount(String input) {
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
    }*/


    void searchApiCashToCash() {
        if (new InternetCheck().isConnected(CashToCashCancelFees.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateSearchApi();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

         //   callApi("ImoneySearchJSON",requestData,174);

            new ServerTask(mComponentInfo, CashToCashCancelFees.this, mHandler, requestData, "ImoneySearchJSON", 174).start();

        } else {
            Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateCancelWithFees() {
        String jsonString = "";

       /* SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);*/

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


        try {

                            /*

                             {
                   "vendorcode":"MICR",
                   "agentcode":"237000271014",
                   "pin":"963A8D298502F0AA36C2824F7F9F069C",
                   "destination":"23789898985655555",
                   "source":"237000271016",
                   "amount":"100",
                   "pintype":"IPIN",
                   "comments":"Rajesh mishra",
                  "clienttype":"http",
                  "pintype":"IPIN",
                  "requestcts":"2017-10-18 04:21:21",
                  "expirydate":"2017-11-24",
                  "destinationname":"Rajesh",
                  "referencenumber":"P012111170039",
                  "cnitype":"PA",
                  "cninumber":"id12345",
                  "fee":"20"

                }

            */

            JSONObject countryObj = new JSONObject();

            countryObj.put("vendorcode", "MICR");
            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", destinationMobileNoString);
            countryObj.put("source", sourceNumberString);
            countryObj.put("amount", amountTempString[0]);
            countryObj.put("pintype", "MPIN");
            countryObj.put("comments", commentString);
            countryObj.put("clienttype", "GPRS");

            countryObj.put("requestcts", "");

            String[] idProofIssuDateStringTemp = idProofIssuDateString.split("\\-");

            String dd = idProofIssuDateStringTemp[0];
            String mm = idProofIssuDateStringTemp[1];
            String yy = idProofIssuDateStringTemp[2];

            String yymmdd = yy + "-" + mm + "-" + dd;

            countryObj.put("expirydate", yymmdd);
            countryObj.put("destinationname", recipientNameString);
            countryObj.put("referencenumber", refrenceNumberString);
            countryObj.put("cnitype", idProofCashToCash_code);
            countryObj.put("cninumber", idProofNumberString);
            countryObj.put("fee",tariffAmountFee);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            //  countryObj.put("sourcename", senderNameString);
            //  countryObj.put("confcode", confirmationCodeString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return jsonString;
    }

    private String generateCancelWithoutFees() {
        String jsonString = "";

      /*  SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);
       */

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


        try {



            JSONObject countryObj = new JSONObject();

            countryObj.put("vendorcode", "MICR");
            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", destinationMobileNoString);
            countryObj.put("source", sourceNumberString);
            countryObj.put("amount", amountTempString[0]);
            countryObj.put("pintype", "MPIN");
            countryObj.put("comments", commentString);
            countryObj.put("clienttype", "GPRS");

            countryObj.put("requestcts", "");

            String[] idProofIssuDateStringTemp = idProofIssuDateString.split("\\-");

            String dd = idProofIssuDateStringTemp[0];
            String mm = idProofIssuDateStringTemp[1];
            String yy = idProofIssuDateStringTemp[2];

            String yymmdd = yy + "-" + mm + "-" + dd;

            countryObj.put("expirydate", yymmdd);
            countryObj.put("destinationname", recipientNameString);
            countryObj.put("referencenumber", refrenceNumberString);
            countryObj.put("cnitype", idProofCashToCash_code);
            countryObj.put("cninumber", idProofNumberString);
            countryObj.put("fee", "0.0");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            //  countryObj.put("sourcename", senderNameString);
            //  countryObj.put("confcode", confirmationCodeString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return jsonString;
    }

    private String generateSearchApi() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("referencenumber", refrenceNumberString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    void proceedTariffAmount() {
        if (new InternetCheck().isConnected(CashToCashCancelFees.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

        //    callApi("getTarifInJSON",requestData,114);
           new ServerTask(mComponentInfo, CashToCashCancelFees.this, mHandler, requestData, "getTarifInJSON", 114).start();

        } else {
            Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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
                    // idProofExpiredDate_EditText_manually.setText(dateSetString);
                    idproofIssuDate_EditText_manually.setText(dateSetString);

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
                    Toast.makeText(CashToCashCancelFees.this, getString(R.string.pleaseEnterValidIdProofNumber), Toast.LENGTH_LONG).show();

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

    private String generateTariffAmmount() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", responseArray[5]);
            countryObj.put("amount", amountTempString[0]);
            countryObj.put("transtype", "IMONEYCANCEL");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            //   String fromCity = mComponentInfo.getmSharedPreferences().getString("state", "");

            //   countryObj.put("fromcity", fromCity);   //  change from server
            //   countryObj.put("tocity", toCity);     //  Change from Server
            countryObj.put("comments", commentString);
            countryObj.put("udv1", "");
            countryObj.put("accountType", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            //  countryObj.put("destination", nameNumberString);


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



    private void showSuccessReceiptWithFees(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("spinnerCountryString", spinnerCountryString);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("senderNameString", senderNameString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("senderNumberString", sourceNumberString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("currencySearchString", currencySearchString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("amountSearch", amountTempString[0]).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("refrenceNumberString", refrenceNumberString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("recipientNameString", recipientNameString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("feesWithFee", tariffAmountFee).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("feesWithFee", tariffAmountFee).commit();



        String temSelectButtonfees=String.valueOf(selectButtonfees);

        mComponentInfo.getmSharedPreferences().edit().putString("selectButtonfees", "1").commit();   // with fees

        String amountToword_withfees = String.valueOf(stringNumberWord);

        mComponentInfo.getmSharedPreferences().edit().putString("amountToword_withfees", amountToword_withfees).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("commentString", commentString).commit();

        Intent intent = new Intent(CashToCashCancelFees.this, SucessReceiptCashToCashCancel.class);

        intent.putExtra("data", data);
        intent.putExtra("senderNameString", senderNameString);
        intent.putExtra("senderNumberString", sourceNumberString);
        intent.putExtra("currencySearchString", currencySearchString);
        intent.putExtra("amountSearch", amountTempString[0]);
        intent.putExtra("amountSearchNoToWord", amountToword_withfees);
        intent.putExtra("commentString", commentString);
        intent.putExtra("refrenceNumberString", refrenceNumberString);
        intent.putExtra("recipientNameString", recipientNameString);
        intent.putExtra("feesWithFee", tariffAmountFee);
        intent.putExtra("selectButtonfees", selectButtonfees);


        startActivity(intent);
        CashToCashCancelFees.this.finish();
    }

    private void showSuccessReceiptWithoutFees(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("spinnerCountryString", spinnerCountryString);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("senderNameString", senderNameString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("senderNumberString", sourceNumberString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("currencySearchString", currencySearchString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("amountSearch", amountTempString[0]).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("refrenceNumberString", refrenceNumberString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("recipientNameString", recipientNameString).commit();

        String amountToword_withoutfees = String.valueOf(stringNumberWord);

        mComponentInfo.getmSharedPreferences().edit().putString("amountToword_withoutfees", amountToword_withoutfees).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("commentString", commentString).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("feesWithoutFee", "0.0").commit();

        String temSelectButtonfees=String.valueOf(selectButtonfees);

        mComponentInfo.getmSharedPreferences().edit().putString("selectButtonfees", "0").commit();    // without fees


        Intent intent = new Intent(CashToCashCancelFees.this, SucessReceiptCashToCashCancel.class);

        intent.putExtra("data", data);
        intent.putExtra("senderNameString", senderNameString);
        intent.putExtra("senderNumberString", sourceNumberString);
        intent.putExtra("currencySearchString", currencySearchString);
        intent.putExtra("amountSearch", amountTempString[0]);
        intent.putExtra("amountSearchNoToWord", amountToword_withoutfees);
        intent.putExtra("commentString", commentString);
        intent.putExtra("refrenceNumberString", refrenceNumberString);
        intent.putExtra("recipientNameString", recipientNameString);
        intent.putExtra("feesWithoutFee", "0.0");
        intent.putExtra("selectButtonfees", selectButtonfees);

        startActivity(intent);
        CashToCashCancelFees.this.finish();
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            String responseString=generalResponseModel.getUserDefinedString();


            if (requestNo == 174) {

                String statusPaidNotPaid = generalResponseModel.getUserDefinedString();
                String[] statusAlreadypaid = statusPaidNotPaid.split("\\|");

                if (statusAlreadypaid[9].equalsIgnoreCase("RA")) {
                    Toast.makeText(CashToCashCancelFees.this, getString(R.string.alreadypaidCashToCash), Toast.LENGTH_SHORT).show();
                } else {

                    scrollview_first_page.setVisibility(View.GONE);
                    scrollview_second_page.setVisibility(View.VISIBLE);

                    String responseData = generalResponseModel.getUserDefinedString();

                    responseArray = responseData.split("\\|");

                    currencySearchString = responseArray[1];
                    currency_TxtView_secondPage.setText(responseArray[1]);         // set from server
                    senderMobileno_editext_secondPage.setText(responseArray[4]);  //  set from server
                    senderName_TxtView_secondPage.setText(responseArray[2]);       // set from server
                    receiptMobileNo_editText_secondPage.setText(responseArray[5]); // set from server

                    receiptName_edittext_secondPage.setText(responseArray[3]);

                    recipientNameString = responseArray[3];
                    sourceNumberString = responseArray[4];
                    destinationMobileNoString = responseArray[5];

                    amountTempString = responseArray[6].split("\\.");         // set from server split
                    amount_TxtView_secondPage.setText(amountTempString[0]);       // set from server
                    referenceNumber_textView.setText(refrenceNumberString);

                    // amountSearchNoToWord = responseArray[8];

                    numberToWord = Integer.parseInt(amountTempString[0]);
                    System.out.println(numberToWord);


                    String aaaaa=responseArray[6];
                    String  str=aaaaa.replaceAll("\\.", "-");
                    arrrString=str.split("\\-");
                    int result = Integer.parseInt(arrrString[0]);

                    convertNoToWord(result);


                   /* String aaaaa=responseArray[6];
                    String  str=aaaaa.replaceAll("\\.", "-");
                    arrrString=str.split("\\-");


                    Double dtempData = new Double(arrrString[0]);
                    int result = dtempData.intValue();

                    convertNoToWord(result);
                    System.out.println(result);
*/

                    if(selectButtonfees==1)   // tariff if cancel with fees
                    {
                        proceedTariffAmount();
                    }


                }


            }
            else if (requestNo == 114) {
                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                tariffAmountFee = temp[0];
                System.out.print(tariffAmountFee);
                linearLayout_tariff.setVisibility(View.VISIBLE);
                tariffAmmount_EdittextReview_AccToCash.setText(tariffAmountFee);
                tariffAmmount_EdittextReview_AccToCash.setEnabled(false);




                amountAdd = Double.parseDouble(arrrString[0]);
                tariffFees = Double.parseDouble(tariffAmountFee);

                tempAddFees = (amountAdd - tariffFees);


                Double d = new Double(tempAddFees);
                int result = d.intValue();

                convertNoToWord(result);
                System.out.println(result);


            }

            else if (requestNo == 166) {


                showSuccessReceiptWithoutFees(responseString);
            }


            else if (requestNo == 167) {

                showSuccessReceiptWithFees(responseString);

            }



        } else {
            hideProgressDialog();
            Toast.makeText(CashToCashCancelFees.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }



    public String convertNoToWord(int number) {
        int n = 1;
        int word;
        stringNumberWord = "";
        while (number != 0) {
            switch (n) {
                case 1:
                    word = number % 100;
                    pass(word);
                    if (number > 100 && number % 100 != 0) {
                        show(" ");
                    }
                    number /= 100;
                    break;

                case 2:
                    word = number % 10;
                    if (word != 0) {
                        show(" ");
                        show(hundredToCroreArray[0]);
                        show(" ");
                        pass(word);
                    }
                    number /= 10;
                    break;

                case 3:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(hundredToCroreArray[1]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;

                case 4:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(hundredToCroreArray[2]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;

                case 5:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(hundredToCroreArray[3]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;

            }
            n++;
        }

        System.out.println(stringNumberWord);

        if(stringNumberWord.equalsIgnoreCase(""))
        {
            amount_TxtView_secondPage.setText(getString(R.string.zero));
        }
        else
        {
            amount_TxtView_secondPage.setText(amountTempString[0]);
        }


        //Toast.makeText(CashToCashCancelFees.this,stringNumberWord,Toast.LENGTH_LONG).show();

        return stringNumberWord;
    }

    public void pass(int number) {
        int word, q;
        if (number < 10) {
            show(oneToNineArray[number]);
        }
        if (number > 9 && number < 20) {
            show(tenToNinteenArray[number - 10]);
        }
        if (number > 19) {
            word = number % 10;
            if (word == 0) {
                q = number / 10;
                show(twentyToNinty[q - 2]);
            } else {
                q = number / 10;
                show(oneToNineArray[word]);
                show(" ");
                show(twentyToNinty[q - 2]);
            }
        }
    }

    public void show(String s) {
        String st;
        st = stringNumberWord;
        stringNumberWord = s;
        stringNumberWord += st;
        System.out.println(stringNumberWord);

    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            CashToCashCancelFees.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CashToCashCancelFees.this);
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
                CashToCashCancelFees.this.finish();
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


    private int getCurrencySelection() {
        int ret = 0;
        for (int i = 0; i < currencyArray.length; i++) {
            if (currencyArray[i].equalsIgnoreCase(currencySelectionString)) {


                ret = i;
            }
        }
        return ret;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.idproofIssueDate_calender_button:

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 4;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

        }

        return false;
    }

    @Override
    public void onDateSet(final DatePicker var1, final String year, final String month, final String day) {
        CashToCashCancelFees.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (iLevel) {

                    case 4:
                        // idproofIssueDate_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idproofExpiredDate(dateSetString)) {
                        }
                        break;


                }
            }
        });
    }


}

/*
package shariq.eu_agent_new;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptWithdrawalBranch;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

*/
/**
 * Created by Sahrique on 14/03/17.
 *//*


public class CashOutWithdrawalinitiateOtp extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String[] bankSelectionArray, transferTagArray, operationCodeArray, accountTypeArray;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    String amountStringCashoutMenu, mobileNumberCashOutMenu;

    String agentName, spinnerTransactionTypeString, confCodeString, agentCode, spinnerCountryString, transferBasisString, subscriberNoString,
            amountString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button nextButton, previousButton, submitButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner spinnerCountry, spinnerAccountToDebit, spinnerTransactionType;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;
    private AutoCompleteTextView mpinEditText, amountEditText, recipient_MobileNo_EditText, confcode_EditText_resendSms;
    private TextView titleTextView, titleTextViewClearAll;
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
                DataParserThread agent.thread = new DataParserThread(CashOutWithdrawalinitiateOtp.this, mComponentInfo, CashOutWithdrawalinitiateOtp.this, message.arg1, message.obj.toString());
                agent.thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;

    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
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
        setContentView(R.layout.cashout_withdrawal_initiate_otp);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_resendSms);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.withdrawalInitiate));
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

            mobileNumberCashOutMenu = mComponentInfo.getmSharedPreferences().getString("mobileNumberCashOutMenu", "");
            amountStringCashoutMenu = mComponentInfo.getmSharedPreferences().getString("amountCashOutMenu", "");




        } catch (Exception e) {

            CashOutWithdrawalinitiateOtp.this.finish();
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
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);

        operationCodeArray = getResources().getStringArray(R.array.TxnType);
        spinnerTransactionType = (Spinner) findViewById(R.id.spinnerTransactionType);
        recipient_MobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.nameNumberEditText_AccToCash);
        recipient_MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_resendSms);
        mpinEditText.setOnEditorActionListener(this);

        amountEditText = (AutoCompleteTextView) findViewById(R.id.amount_EditText);
        amountEditText.setOnEditorActionListener(this);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.shoudNotAllowZero), Toast.LENGTH_LONG).show();
                    if (enteredString.length() > 0) {
                        amountEditText.setText(enteredString.substring(1));
                    } else {
                        amountEditText.setText("");
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


        titleTextView = (TextView) findViewById(R.id.titleTextViewResendSmsDetails);

        titleTextViewClearAll = (TextView) findViewById(R.id.titleTextViewClearAll);
        titleTextViewClearAll.setOnClickListener(this);

        confcode_EditText_resendSms = (AutoCompleteTextView) findViewById(R.id.confcode_EditText_resendSms);


        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebitWithdrawal);

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


        // spinnerAccountToDebit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));


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
                recipient_MobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNoAcctoCashOut), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
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
            Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
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

        */
/*    int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashOut), lengthToCheck + 1 + "");

            } else {
               *//*
*/
/* transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);*//*
*/
/*
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashOut), lengthToCheck + 1 + "");
            }

            if (subscriberNoString.length() > lengthToCheck) {
                if (transferBasisposition == 1) {
                    if (subscriberNoString.length() == ++lengthToCheck) {
                        subscriberNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + subscriberNoString;
                    } else {
                        Toast.makeText(CashOutWithdrawalinitiateOtp.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                // if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {

                amountString = amountEditText.getText().toString().trim();
                if (amountString.length() > 0 && validateAmount(amountString)) {*//*


            if (mpinString.length() == 4) {


                spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];

                ret = true;
            } else {
                Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
            }
               */
/* } else {
                    Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.pleaseenteramount), Toast.LENGTH_LONG).show();

                }
               *//*
*/
/* } else {
                    Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.pleaseSelectAccountCredit), Toast.LENGTH_LONG).show();

                }
             *//*
*/
/*

            } else {
                Toast.makeText(CashOutWithdrawalinitiateOtp.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }
              *//*


        } else {
            Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    public void validateDetails() {

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

        if (new InternetCheck().isConnected(CashOutWithdrawalinitiateOtp.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateCashWithdrawal();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;
            new ServerTask(mComponentInfo, CashOutWithdrawalinitiateOtp.this, mHandler, requestData, "getwithdrawlInitiated", 141).start();
        } else {
            Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateCashWithdrawal() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

         */
/*   {"agentCode":"237000271015","pin":"2B829383F5DCA0E4C5E93D7032A3542E","source":"237000271015","comments":"OK",
              "pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS","requestcts":"25/05/2016 18:01:51","amount":"10000",
              "accountType":"MA"}*//*


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("source", data[0].trim());
            countryObj.put("source", mobileNumberCashOutMenu);   //
            countryObj.put("comments", "commentSms");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");
            countryObj.put("amount", amountStringCashoutMenu);
            //countryObj.put("destination", nameNumberString);


        */
/*    String accountType = "";
            if (data[1].contains("Mobile Account")) {
                accountType = "MA";
            } else if (data[1].equalsIgnoreCase("savings_account")) {
                accountType = "SA";
            } else if (data[1].equalsIgnoreCase("fixed_deposite")) {
                accountType = "FD";
            } else {
                accountType = data[1];
            }*//*

            countryObj.put("accountType", "MA");
            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }

    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashOutWithdrawalinitiateOtp.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.withdrawal));

        nextButton.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);

        recipient_MobileNo_EditText.setEnabled(false);
        spinnerCountry.setEnabled(false);
        spinnerTransactionType.setEnabled(false);

        confcode_EditText_resendSms.setVisibility(View.VISIBLE);
        confcode_EditText_resendSms.requestFocus();
        mpinEditText.setText("");
        titleTextViewClearAll.setVisibility(View.VISIBLE);


    }

    public void ClearAll() {
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
        amountEditText.setText("");
        titleTextViewClearAll.setVisibility(View.GONE);


    }


    private void showSuccessSecondPhase(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashOutWithdrawalinitiateOtp.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.withdrawal));


        String[] temp = data.split("\\|");

        builder.setMessage(getString(R.string.withdrawal));

        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                CashOutWithdrawalinitiateOtp.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }


    private void showSuccessReceipt(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("spinnerCountryString", spinnerCountryString);

        Intent intent = new Intent(CashOutWithdrawalinitiateOtp.this, SucessReceiptWithdrawalBranch.class);
        intent.putExtra("data", data);
        intent.putExtra("spinnerCountryString", spinnerCountryString);
        startActivity(intent);
        CashOutWithdrawalinitiateOtp.this.finish();
    }

    void validateDetailsSecondPhase() {

        if (validationSecondConfCodeMpin()) {
            ServerRequestSecondPhase();
        }
    }

    boolean validationSecondConfCodeMpin() {
        boolean ret = false;

        confCodeString = confcode_EditText_resendSms.getText().toString().trim();
        if (confCodeString.length() >= 4) {

            mpinString = mpinEditText.getText().toString().trim();
            if (mpinString.length() == 4) {

                ret = true;

            } else {
                Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.pleaseEnterConfCodeOTP), Toast.LENGTH_LONG).show();
        }

        return ret;
    }


    private void ServerRequestSecondPhase() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(CashOutWithdrawalinitiateOtp.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateResendSmsScondPhase();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;
            new ServerTask(mComponentInfo, CashOutWithdrawalinitiateOtp.this, mHandler, requestData, "getCashOutTransaction", 142).start();
        } else {
            Toast.makeText(CashOutWithdrawalinitiateOtp.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateResendSmsScondPhase() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {


            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("source", data[0].trim());
            countryObj.put("source", agentCode);
            countryObj.put("comments", "comments");
            //countryObj.put("requestcts", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("amount", amountStringCashoutMenu);
            countryObj.put("destination", mobileNumberCashOutMenu);
            countryObj.put("requestcts", "25/05/2016 18:01:51");
            countryObj.put("udv2", "NOSMS");
            countryObj.put("transcode", confCodeString);


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
            if (requestNo == 141) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(CashOutWithdrawalinitiateOtp.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString());
                }

            }

            if (requestNo == 142) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(CashOutWithdrawalinitiateOtp.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            }

        } else {
            hideProgressDialog();
            Toast.makeText(CashOutWithdrawalinitiateOtp.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            CashOutWithdrawalinitiateOtp.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CashOutWithdrawalinitiateOtp.this);
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
                CashOutWithdrawalinitiateOtp.this.finish();
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
}
*/

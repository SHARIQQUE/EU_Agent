/*
package billpay;

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
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import model.GeneralResponseModel;
import OTPVerificationActivity;
import franchise.activities.R;
import agent.thread.DataParserThread;


*/
/**
 * Created by Sahrique on 26/04/17.
 *//*


public class BillPaySelf extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    Intent billPayActualStartIntent;
    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, agentCode,
            spinnerCountryString, transferBasisString, nameNumberString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button nextButton, payforSelfButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner spinnerCountry, spinnerAccountToDebit, transferBasisSpinner;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;
    private TextView mpinEditText, recipient_MobileNo_EditText;
    private TextView recipientCountryTxtView_Review, transferBasisTxtView_Review, recipientNameNoTxtView_Review, titleTextView,
            payerAccountTypeTxtView_Review, amountTxtView_Review;
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
                DataParserThread agent.thread = new DataParserThread(BillPaySelf.this, mComponentInfo, BillPaySelf.this, message.arg1, message.obj.toString());

                agent.thread.execute();
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
                validateDetails(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        billPayActualStartIntent = new Intent(BillPaySelf.this, BillPayActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billpay_self);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MoneyTransfer);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.billPay));
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

            BillPaySelf.this.finish();
        }


        nextButton = (Button) findViewById(R.id.nextButton_MoneyTransfer);
        nextButton.setOnClickListener(this);
        nextButton.setVisibility(View.VISIBLE);

        payforSelfButton = (Button) findViewById(R.id.payforSelf_Btn);
        payforSelfButton.setOnClickListener(this);

        input_SV_AccToCash = (ScrollView) findViewById(R.id.input_SV_AccToCash);
        review_SV_AccToCash = (ScrollView) findViewById(R.id.review_SV_AccToCash);

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
        transferBasisSpinner.setOnItemSelectedListener(BillPaySelf.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);
        recipient_MobileNo_EditText = (TextView) findViewById(R.id.nameNumberEditText_AccToCash);
        recipient_MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));

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
        recipientNameNoTxtView_Review = (TextView) findViewById(R.id.recipientNameNo_TxtView_Review_AccToCash);
        amountTxtView_Review = (TextView) findViewById(R.id.amount_TxtView_Review_AccToCash);
        payerAccountTypeTxtView_Review = (TextView) findViewById(R.id.payerAccountType_TxtView_Review_AccToCash);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText__Review_AccToCash);
        mpinEditText.setOnEditorActionListener(this);
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
                recipient_MobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNobillPay), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
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
            Toast.makeText(BillPaySelf.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
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

    private boolean validate() {
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
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashIn), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);
            }

            nameNumberString = recipient_MobileNo_EditText.getText().toString().trim();
            if (nameNumberString.length() > lengthToCheck) {
                if (transferBasisposition == 1) {
                    if (nameNumberString.length() == ++lengthToCheck) {
                        nameNumberString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + nameNumberString;
                    } else {
                        Toast.makeText(BillPaySelf.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                ret = true;

                spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];

            } else {
                Toast.makeText(BillPaySelf.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(BillPaySelf.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetails(boolean isSelf) {

        if (isSelf) {
            mComponentInfo.getmSharedPreferences().edit().putString("billPaySource", agentCode).commit();
            mComponentInfo.getmSharedPreferences().edit().putString("billPayBankAccounts", mComponentInfo.getmSharedPreferences().getString("bankAccounts", "")).commit();

            startActivity(billPayActualStartIntent);
            BillPaySelf.this.finish();

        } else {

            if (validate()) {

                mComponentInfo.getmSharedPreferences().edit().putString("billPaySource", countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + recipient_MobileNo_EditText.getText().toString().trim()).commit();
                mComponentInfo.getmSharedPreferences().edit().putString("billPayBankAccounts", mComponentInfo.getmSharedPreferences().getString("bankAccounts", "")).commit();
                startActivity(billPayActualStartIntent);
                BillPaySelf.this.finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_MoneyTransfer:
                validateDetails(false);
                break;
            case R.id.payforSelf_Btn:
                validateDetails(true);
                break;
        }
    }


    private String generateAccToCashData() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

           */
/* "agentCode":"237000271010","pin":"D3F8D3568D92CA73E4D5A1671529F075",
            "source":"237000271010","comments":"sms","pintype":"IPIN",
            "vendorcode":"MICR","clienttype":"GPRS","amount":"2000",
            "destination":"237000271015","accountType":"MA",
            "requestcts":"25/05/2016 18:01:51","udv2":"NOSMS"*//*


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("source", data[0].trim());
            countryObj.put("source", agentCode);
            countryObj.put("comments", "OK");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            //countryObj.put("amount", amountString);
            countryObj.put("destination", nameNumberString);

            String accountType = "";
            if (data[1].contains("Mobile Account")) {
                accountType = "MA";
            } else if (data[1].equalsIgnoreCase("savings_account")) {
                accountType = "SA";
            } else if (data[1].equalsIgnoreCase("fixed_deposite")) {
                accountType = "FD";
            } else {
                accountType = data[1];
            }
            countryObj.put("accountType", accountType);
            countryObj.put("requestcts", "");
            countryObj.put("udv2", "udv2");

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }

        return jsonString;
    }


    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BillPaySelf.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.billPay));
        // payerAccountString = payerAccountString.replace("_", "-");
        //  payerAccountString = payerAccountString.toUpperCase();
        String[] temp = data.split("\\|");
        //builder.setMessage(String.format(getString(R.string.tariffResponseSuccess)+temp[0], temp[1]) );

        builder.setMessage(getString(R.string.billPayBillSucessReceipt));// + getString(R.string.transactionId) + temp[0]);

        // builder.setMessage(getString(R.string.cashInSucessReceipt) + " \n " +" \n "+ data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                BillPaySelf.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {
            if (requestNo == 119) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(BillPaySelf.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString());
                }


            } else {
                hideProgressDialog();
                Toast.makeText(BillPaySelf.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

            }
        } else {
            hideProgressDialog();
            Toast.makeText(BillPaySelf.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            BillPaySelf.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(BillPaySelf.this);
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
                BillPaySelf.this.finish();
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
*/

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
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

*/
/**
 * Created by Sahrique on 14/03/17.
 *//*


public class BillpayBill extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, agentCode, spinnerCountryString, recipientBankString, recipientAccountString, recipientMobileNoString, transferBasisString, stringDestination,
            payerBankString, transferTagString, billNumberString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button nextButton, previousButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner spinnerCountry, recipientbankSpinner, recipientAccTypeSpinner, payerBankSpinner, spinnerAccountToDebit,
            transferTagSpinner, transferBasisSpinner;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;
    private AutoCompleteTextView name_No_EditText, bilNumberEditText, mpinEditText, recipient_AccountNo_EditText,
            destinationEditText;
    private TextView recipientCountryTxtView_Review, transferBasisTxtView_Review,
            recipientNameNoTitleTxtView_Review, recipientNameNoTxtView_Review,
            recipientAccountTypeTxtView_Review, payerBankTxtView_Review, titleTextView,
            payerAccountTypeTxtView_Review, billpayTxtView_Review, recipientBankTxtView_Review, transferTagTxtView_Review;
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
                DataParserThread agent.thread = new DataParserThread(BillpayBill.this, mComponentInfo, BillpayBill.this, message.arg1, message.obj.toString());
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
        setContentView(R.layout.billpay_bill);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_billpay);
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

            BillpayBill.this.finish();
        }

        nextButton = (Button) findViewById(R.id.nextButton_MoneyTransfer);
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


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(BillpayBill.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);
        destinationEditText = (AutoCompleteTextView) findViewById(R.id.destinationEditText);
        destinationEditText.setHint(getString(R.string.PleasEenterMobileNumber));
        bilNumberEditText = (AutoCompleteTextView) findViewById(R.id.billPayBillEditText_AccToCash);
        bilNumberEditText.setOnEditorActionListener(this);

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
        billpayTxtView_Review = (TextView) findViewById(R.id.billpay_TxtView_Review_AccToCash);
        payerAccountTypeTxtView_Review = (TextView) findViewById(R.id.payerAccountType_TxtView_Review_AccToCash);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText__Review_AccToCash);
        mpinEditText.setOnEditorActionListener(this);
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                destinationEditText.setText("");
                destinationEditText.setHint(getString(R.string.enterMerchantMobileNo));
                // destinationEditText.setFilters(null);
                destinationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                destinationEditText.setHint(String.format(getString(R.string.hintMarchantMobileNo), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                destinationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                destinationEditText.setFilters(digitsfilters);
                destinationEditText.setText("");


            } else if (i == 2) {
                destinationEditText.setText("");
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
                destinationEditText.setHint(getString(R.string.enterMerchantMobileNo));
                destinationEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                destinationEditText.setFilters(digitsfilters);
                destinationEditText.setText("");
            }
        } else {
            Toast.makeText(BillpayBill.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:
                setInputType(i);
                break;

            case R.id.spinner_PayerAccountTypeSelection_AccToAcc:

                if (i > 0) {
                    if (bilNumberEditText != null) {
                        bilNumberEditText.requestFocus();
                    }
                }
                break;
            case R.id.spinnerCountry:
                setInputType(1);
                break;
            ////  case R.id.spinnerAccountToDebit:
               */
/* if(i == 0){

                }
                if (i > 0) {
                    if(payerAccountTypeSpinner.getSelectedItem().toString() != "Mobile Account"){
                        Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                        payerAccountTypeSpinner.setSelection(0);
                    }*//*
*/
/*else{
                        if (bilNumberEditText != null) {
                            bilNumberEditText.requestFocus();
                        }
                    }*//*
*/
/*

                }*//*

            //int pos = payerAccountTypeSpinner.getSelectedItemPosition();
//                if(pos == 0){
//
//                }else if(! (pos ==2)){
//                    Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
//                    payerAccountTypeSpinner.setSelection(0);
//                }
            // String temp = payerAccountTypeSpinner.getSelectedItem().toString();
             */
/*Please Select Account To Debit*//*

               */
/* switch (temp){
                   *//*
*/
/* case 0:
                        break;
                    case 1:
                        Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                        payerAccountTypeSpinner.setSelection(0);
                        break;
                    case 2:
                        break;
                    case 3:
                        Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                        payerAccountTypeSpinner.setSelection(0);
                        break;
                    case 4:
                        Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                        payerAccountTypeSpinner.setSelection(0);
                        break;*//*
*/
/*
                }*//*

            //  break;

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

    private void showAccToCashReview() {

        BillpayBill.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();
                titleTextView.setText(getString(R.string.pleasereviewdetail));
                input_SV_AccToCash.setVisibility(View.GONE);
                review_SV_AccToCash.setVisibility(View.VISIBLE);
                recipientCountryTxtView_Review.setText(spinnerCountryString);
                transferBasisTxtView_Review.setText(transferBasisString);
                recipientNameNoTxtView_Review.setText(stringDestination);
                billpayTxtView_Review.setText(billNumberString);
                payerAccountTypeTxtView_Review.setText(spinnerAccountToDebitString);
                nextButton.setText(getString(R.string.transfernow));
                isReview = true;
                mpinEditText.requestFocus();
            }
        });
    }

    private boolean validateCashOut_PartI() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";
            transferBasisposition = 2;
            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCash), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.enterMerchantMobileNo);
            }

            stringDestination = destinationEditText.getText().toString().trim();
            if (stringDestination.length() > lengthToCheck) {
                if (transferBasisposition == 1) {
                    if (stringDestination.length() == ++lengthToCheck) {


                    } else {
                        Toast.makeText(BillpayBill.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {
                    billNumberString = bilNumberEditText.getText().toString().trim();
                    if (billNumberString.length() > 4) {

                        ret = true;


                        stringDestination = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + stringDestination;
                        spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                        accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];

                          */
/*  if(payerAccountTypeSpinner.getSelectedItem().toString()!= "Mobile Account"){
                                ret = false;
                                Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_SHORT).show();
                            }else{
                                ret = true;
                                payerAccountString = payerAccountTypeSpinner.getSelectedItem().toString();
                                accountCodeString = payerAccountCodeArray[payerAccountTypeSpinner.getSelectedItemPosition()];

                            }*//*

                    } else {
                        Toast.makeText(BillpayBill.this, getString(R.string.enterBillerId), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(BillpayBill.this, getString(R.string.accountofpayer), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(BillpayBill.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }
            */
/*} else {
                Toast.makeText(CashOutSameBranch.this, getString(R.string.pleaseselectsendmode), Toast.LENGTH_LONG).show();
              }*//*


        } else {
            Toast.makeText(BillpayBill.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetails() {
        if (!isReview) {
            if (validateCashOut_PartI()) {
                showAccToCashReview();
            }
        } else {
            if (validateCashOut_PartII()) {
                CashOutTransfer();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_MoneyTransfer:
                validateDetails();
                break;
        }
    }

    private void CashOutTransfer() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(BillpayBill.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateAccToCashData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;
            new ServerTask(mComponentInfo, BillpayBill.this, mHandler, requestData, "getBillPayTransactionInJSON", 129).start();
        } else {
            Toast.makeText(BillpayBill.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateAccToCashData() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

           */
/* {"agentCode":"237675042228","pin":"6512D659F99CDE5CADA863DB2AF8C5DC","source":"237675042228",
              "comments":"FREEFORM","pintype":"MPIN","vendorcode":"MICR","clienttype":"GPRS","invoiceno":"278797744
              ","destination":"237100004001","requestcts":"25/05/2016 18:01:51"}
             *//*



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
            countryObj.put("invoiceno", billNumberString);
            countryObj.put("destination", stringDestination);
            countryObj.put("requestcts", "requestcts");
            //countryObj.put("udv2", "NOSMS");

          */
/*
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
            *//*


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private boolean validateCashOut_PartII() {

        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;
        } else {
            Toast.makeText(BillpayBill.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

  */
/*  private void showSuccess(String data) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(CashOutSameBranch.this);
            String temp[] = data.split("\\|");
            String resAmount = "", resDestination = "", resConfcode = "", resTxnID = "", resWalletBal = "";
            resConfcode = temp[0];
            resAmount = temp[1];
            resDestination = temp[2];
            resTxnID = temp[3];
            resWalletBal = temp[4];
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.CashOutSameBranch));
        *//*
*/
/*Welcome to Express Union Mobile. Dear Customer, You have transferred #1 XAF to #2,
        code is #3 ID: #4, please inform the benef. your New Balance #5 XAF..*//*
*/
/*
            if (data.trim().length() != 0) {
                builder.setMessage(String.format(getString(R.string.accounttocashsuccess), resAmount, resDestination, resConfcode, resTxnID, resWalletBal));
            } else {
                builder.setMessage(getString(R.string.accounttocashsuccessconf) + "  " + data);
            }
            builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    successDialog.cancel();
                    CashOutSameBranch.this.finish();
                }
            });
            successDialog = builder.create();
            successDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*//*


    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BillpayBill.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.billPay));
        // payerAccountString = payerAccountString.replace("_", "-");
        //  payerAccountString = payerAccountString.toUpperCase();

        builder.setMessage(getString(R.string.billPayBillSucessReceipt));// +" \n " + " \n " + data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                BillpayBill.this.finish();
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
            if (requestNo == 129) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(BillpayBill.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString());
                }

            } else {
                hideProgressDialog();
                Toast.makeText(BillpayBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            hideProgressDialog();
            Toast.makeText(BillpayBill.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            BillpayBill.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(BillpayBill.this);
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
                BillpayBill.this.finish();
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
                billNumberString = "" + amt;
            }
        } catch (Exception e) {
        }
        return ret;
    }
}
*/

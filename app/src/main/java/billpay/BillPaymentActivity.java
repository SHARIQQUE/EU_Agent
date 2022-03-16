/*
package billpay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import OTPVerificationActivity;
import franchise.activities.R;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


*/
/**
 * Created by AdityaBugalia on 31/08/16.
 *//*

public class BillPaymentActivity extends AppCompatActivity implements View.OnClickListener, ServerResponseParseCompletedNotifier {

    Spinner billerCountrySpinner, billerNameSpinner, payerAccountsSpinner;
    AutoCompleteTextView billNoEditText, recipientPhoneNoEditText, payementCodeEditText, mpinEditText;
    String billerCountryString, billerNameString, billNoString, recipientNoString, paymentCodeString, payerAccountString, agentName, agentCode, mpinString;
    String[] countryArray, countryCodeArray, countryPrefixArray, billerArray, payerBankAccountsArray;
    ComponentMd5SharedPre mComponentInfo;
    Toolbar mToolbar;
    Button nextButton;
    boolean isReview = false, isServerOperationInProcess = false;

    private ProgressDialog mDialog;

    Dialog successDialog;
    TextView billerCountry_TxtView_Review, billerName_TxtView_Review, billNO_TxtView_Review, paymentCode_TxtView_Review,
            recipientNo_TxtView_Review, payerAccount_TxtView_Review;
    LinearLayout input_LL, review_LL;

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


        setContentView(R.layout.bill_payment);


        mToolbar = (Toolbar) findViewById(R.id.tool_bar_BillPayment);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.billpayment_title));
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
            billerArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");

        } catch (Exception e) {

            BillPaymentActivity.this.finish();
        }


        nextButton = (Button) findViewById(R.id.nextButton_Billpayment);

        nextButton.setOnClickListener(this);

        nextButton.setVisibility(View.VISIBLE);
        billerCountrySpinner = (Spinner) findViewById(R.id.spinner_BillerCountry_BillPayment);
        billerNameSpinner = (Spinner) findViewById(R.id.spinner_BillerName_BillPayment);
        payerAccountsSpinner = (Spinner) findViewById(R.id.spinner_PayerAccount_BillPayment);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        billerCountrySpinner.setAdapter(spinnerArrayAdapter);

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billerNameSpinner.setAdapter(spinnerArrayAdapter);
        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 5) {
            // Looper.prepare();
            String[] data = bankAccounts.split("\\;");

            payerBankAccountsArray = new String[data.length + 1];
            payerBankAccountsArray[0] = getString(R.string.payeraccount);

            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    payerBankAccountsArray[i + 1] = tData[0] + " - " + tData[1];
                }


            }
        } else {
            payerBankAccountsArray = new String[1];
            payerBankAccountsArray[0] = getString(R.string.payeraccount);
        }
        billNoEditText = (AutoCompleteTextView) findViewById(R.id.billNumber_EditText_BillPayment);
        recipientPhoneNoEditText = (AutoCompleteTextView) findViewById(R.id.recipientMobileNo_EditText_BillPayment);
        payementCodeEditText = (AutoCompleteTextView) findViewById(R.id.payementCode_EditText_BillPayment);

        payerAccountsSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));
        input_LL = (LinearLayout) findViewById(R.id.input_Recipient_LL_BillPayment);
        review_LL = (LinearLayout) findViewById(R.id.review_LL_BillPayment);

        billerCountry_TxtView_Review = (TextView) findViewById(R.id.billerCountry_TxtView_Review_BillPayment);
        billerName_TxtView_Review = (TextView) findViewById(R.id.billerName_TxtView_Review_BillPayment);
        billNO_TxtView_Review = (TextView) findViewById(R.id.billerNoCode_TxtView_Review_BillPayment);

        paymentCode_TxtView_Review = (TextView) findViewById(R.id.paymentReferenceCode_TxtView_Review_BillPayment);
        recipientNo_TxtView_Review = (TextView) findViewById(R.id.recipientNo_TxtView_Review_BillPayment);
        payerAccount_TxtView_Review = (TextView) findViewById(R.id.payerAccount_TxtView_Review_BillPayment);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText__Review_BillPayment);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_Billpayment:
                if (!isReview) {
                    if (validateDetails_PartI()) {
                        showReview();
                    }

                } else {
                    if (validateDetails_PartII()) {
                        proceedBillPay();
                    }

                }
                break;

        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());

            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread agent.thread = new DataParserThread(BillPaymentActivity.this, mComponentInfo, BillPaymentActivity.this, message.arg1, message.obj.toString());

                agent.thread.execute();
            }
        }

    };

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(BillPaymentActivity.this);
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

    private void proceedBillPay() {

        if (new InternetCheck().isConnected(BillPaymentActivity.this)) {
            showProgressDialog("Please Wait");
            String requestData = generateBillPayData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            new ServerTask(mComponentInfo, BillPaymentActivity.this, mHandler, requestData, "getBillPayTransactionInJSON", 109).start();
        } else {
            Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {
                    if (new InternetCheck().isConnected(BillPaymentActivity.this)) {
                        showProgressDialog(getString(R.string.pleasewait));
                        String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");
                        isServerOperationInProcess = true;
                        new ServerTask(mComponentInfo, BillPaymentActivity.this, mHandler, requestData, "getBillPayTransactionInJSON", 109).start();
                    } else {
                        Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }

                } else {


                }


                break;


        }
    }

    private String generateBillPayData() {
        String jsonString = "";


        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("source", data[0].trim());
            countryObj.put("source", agentCode);
            countryObj.put("comments", "OK");
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("destination", recipientNoString);


            countryObj.put("invoiceno", billNoString);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }


        return jsonString;
    }

    private boolean validateDetails_PartII() {
        boolean ret = false;
        if (mpinEditText.getText().toString().trim().length() == 4) {

            mpinString = mpinEditText.getText().toString().trim();
            ret = true;
        } else {
            Toast.makeText(BillPaymentActivity.this, getString(R.string.prompt_mPin), Toast.LENGTH_SHORT).show();
        }

        return ret;
    }

    private void showReview() {
        input_LL.setVisibility(View.GONE);
        review_LL.setVisibility(View.VISIBLE);
        isReview = true;
        nextButton.setText(getString(R.string.billpay_title));

        billerCountry_TxtView_Review.setText(billerCountryString);
        billerName_TxtView_Review.setText(billerNameString);
        billNO_TxtView_Review.setText(billNoString);
        recipientNo_TxtView_Review.setText(recipientNoString);
        paymentCode_TxtView_Review.setText(paymentCodeString);
        payerAccount_TxtView_Review.setText(payerAccountString);
        mpinEditText.requestFocus();

    }

    private boolean validateDetails_PartI() {
        boolean ret = false;

        if (billerCountrySpinner.getSelectedItemPosition() > 0) {

            if (billerNameSpinner.getSelectedItemPosition() > 0) {
                billNoString = billNoEditText.getText().toString().trim();


                if (billNoString.length() > 5) {

                    recipientNoString = recipientPhoneNoEditText.getText().toString().trim();
                    if (recipientNoString.length() > 8) {

                        paymentCodeString = payementCodeEditText.getText().toString().trim();
                        if (paymentCodeString.length() >= 4) {
                            if (billerNameSpinner.getSelectedItemPosition() > 0) {
                                billerCountryString = billerCountrySpinner.getSelectedItem().toString();
                                billerNameString = billerNameSpinner.getSelectedItem().toString();
                                payerAccountString = payerAccountsSpinner.getSelectedItem().toString();
                                ret = true;
                            } else {
                                Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseselectaccounttobedebited), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseenterpaymentcode), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseenterreciepientno), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseenterbillno), Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseselectnameofbiller), Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(BillPaymentActivity.this, getString(R.string.pleaseselectcountryofbiller), Toast.LENGTH_SHORT).show();
        }


        return ret;

    }

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BillPaymentActivity.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.moneytransfer_title));

        builder.setMessage(getString(R.string.accounttocashsuccess_final) + data);


        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                BillPaymentActivity.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();


    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        isServerOperationInProcess = false;
        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();
            if (requestNo == 109) {
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "BILLPAY").commit();
                    Intent i = new Intent(BillPaymentActivity.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString(), 1);
                }
            }
        } else {
            hideProgressDialog();
            Toast.makeText(BillPaymentActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            if (!isServerOperationInProcess) {
                BillPaymentActivity.this.finish();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
*/

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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


*/
/**
 * Created by sharique on 09/03/17.
 *//*

public class CashInRnd extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier {

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
                proceedCashIn();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    Spinner payerAccountsSpinner;
    AutoCompleteTextView destination_AutoCompleteTextView,amount_AutoCompleteTextView,mpin_AutoCompleteTextView;
    TextInputLayout destination_TextInputLayout,amount_TextInputLayout,mpin_TextInputLayout;
    ComponentMd5SharedPre mComponentInfo;
    Toolbar mToolbar;
    String agentName, agentCode, mpinString, destinationString, amountString;
    Button nextButton;
    String[] payerBankAccountsArray, payerAccountCodeArray;
    ProgressDialog mDialog;
    Dialog successDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_in_rnd);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();


        mToolbar = (Toolbar) findViewById(R.id.tool_bar_cashIn);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashIn));
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

        nextButton = (Button) findViewById(R.id.nextButton_BalanceCheck);

        nextButton.setOnClickListener(this);
        nextButton.setVisibility(View.VISIBLE);

        destination_AutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.destination_AutoCompleteTextView);
        destination_TextInputLayout = (TextInputLayout) findViewById(R.id.destination_TextInputLayout);

        amount_AutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.amount_AutoCompleteTextView);
        amount_TextInputLayout = (TextInputLayout) findViewById(R.id.amount_TextInputLayout);

        mpin_AutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.mpin_AutoCompleteTextView);
        mpin_TextInputLayout = (TextInputLayout)findViewById(R.id.mpin_TextInputLayout);


        //mpinEditText.setOnEditorActionListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_BalanceCheck:
                validateDetails();
                break;

        }
    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());

            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(CashInRnd.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(CashInRnd.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread agent.thread = new DataParserThread(CashInRnd.this, mComponentInfo, CashInRnd.this, message.arg1, message.obj.toString());

                agent.thread.execute();
            }
        }

    };

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CashInRnd.this);
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
    public void validateDetails() {

        if (validition()) {

            proceedCashIn();
        }
    }

    public boolean validition()
    {
        boolean ret=false;
        if (destination_AutoCompleteTextView != null && amount_AutoCompleteTextView != null && mpin_AutoCompleteTextView != null) {

            destinationString = destination_AutoCompleteTextView.getText().toString().trim();
            amountString = amount_AutoCompleteTextView.getText().toString();
            mpinString = mpin_AutoCompleteTextView.getText().toString();

          if (destinationString.length()>=8) {
                 destination_TextInputLayout.setError(null);
                 if (amountString.length() >0) {
                    amount_TextInputLayout.setError(null);

                    if (mpinString.length() == 4) {
                        mpin_TextInputLayout.setError(null);
                        ret = true;
                    } else {
                         mpin_TextInputLayout.setError(getString(R.string.prompt_mPin));
                    }
                } else {
                    amount_TextInputLayout.setError(getString(R.string.pleaseEnterAmount));
                }

            } else {
              amount_TextInputLayout.setError(getString(R.string.pleaseEnterDestination));
            }
        } else {
          Toast.makeText(CashInRnd.this, getString(R.string.restartapp), Toast.LENGTH_LONG).show();
        }

        return ret;
    }
    private void proceedCashIn() {

        if (new InternetCheck().isConnected(CashInRnd.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateCashInData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, CashInRnd.this, mHandler, requestData, "getCashInTransaction", 119).start();

        } else {
            Toast.makeText(CashInRnd.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 299:
                if (resultCode == RESULT_OK) {
                    if (new InternetCheck().isConnected(CashInRnd.this)) {
                        showProgressDialog(getString(R.string.pleasewait));
                        String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");
                        new ServerTask(mComponentInfo, CashInRnd.this, mHandler, requestData, "getCashInTransaction", 119).start();
                    } else {
                        Toast.makeText(CashInRnd.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                }
                break;
        }
    }

   */
/* {"agentCode":"237100001012","pin":"C09AF4A983FE8B815C9D130B6B04262E","source":"237000215002","co
        mments":"sms","pintype":"MPIN","vendorcode":"MICR","clienttype":"GPRS","accountType":"MA"}
    *//*


    private String generateCashInData() {
        String jsonString = "";

       */
/* {"agentCode":"237000271010","pin":"D3F8D3568D92CA73E4D5A1671529F075","source":"237000271010","c
            omments":"sms","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS","amount":"2000","destination":"
            237000271015","accountType":"MA","requestcts":"25/05/2016 18:01:51","udv2":"NOSMS"}
          *//*



        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            // countryObj.put("source", data[0].trim());
            countryObj.put("source",agentCode);
            countryObj.put("comments", "comments");
            //countryObj.put("requestcts", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("amount",amountString);
            countryObj.put("destination",destinationString);
            countryObj.put("accountType", "MA");
            countryObj.put("requestcts", "requestcts");
            countryObj.put("udv2", "NOSMS");

            jsonString = countryObj.toString();

        } catch (Exception e) {
            Log.e("blc Exception", e.toString());
        }


        return jsonString;
    }

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashInRnd.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.balancecheck));
        // payerAccountString = payerAccountString.replace("_", "-");
        //  payerAccountString = payerAccountString.toUpperCase();

        builder.setMessage(String.format(getString(R.string.balanceSuccessResponse), data));
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                CashInRnd.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            CashInRnd.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();
            if (requestNo == 112) {
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "CASHIN").commit();
                    Intent i = new Intent(CashInRnd.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString(), 1);
                }
            }
        } else {
            hideProgressDialog();
            Toast.makeText(CashInRnd.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }

}
*/

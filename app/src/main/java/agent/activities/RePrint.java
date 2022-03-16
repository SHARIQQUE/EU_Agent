package agent.activities;


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
import android.widget.LinearLayout;
import android.widget.ListView;
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

import callback.ServerResponseParseCompletedNotifier;
import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.ReprintTutionFeesSh;
import sucess_receipt.SucessReceiptReprintBillPay;
import sucess_receipt.SucessReceiptReprintCashIn;
import sucess_receipt.SucessReceiptReprintCashOut;
import sucess_receipt.SucessReceiptReprintCashToCashReceiveMoney;
import sucess_receipt.SucessReceiptReprintCashToCashSameCountry;
import sucess_receipt.SucessReceiptReprintCashToMarchant;
import sucess_receipt.SucessReceiptReprintCreateAgent;
import sucess_receipt.SucessReceiptReprintReceiveMoneyToMobile;
import sucess_receipt.SucessReceiptReprintSendMoneyToMobile;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by AdityaBugalia on 31/08/16.
 */
public class RePrint extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

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
                validateDetails_NextButton();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public AutoCompleteTextView serialNumberEditText, mpinEditText;
    Spinner operationTypeSpinner, billerSpinner, accountNameSpinner;
    Button nextButton;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    boolean isBillPay = false;
    LinearLayout input_LL, review_LL;
    ListView statementLsitView;
    ProgressDialog mDialog;

    Dialog successDialog;
    String agentName, agentCode, operationCodeString, operationNameString, SerialIdString, mpinString, accountTypeCodeString, billerCodeString, destinationString = "";
    private String[] operationCodeArray, billerArray, accountCodeArray, accountNameArray;

    int iLevel = 99;
    boolean isMiniStmtMode = false;

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


        setContentView(R.layout.re_prints);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_prints);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.rePrints));
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
        serialNumberEditText = (AutoCompleteTextView) findViewById(R.id.serialId_EditText_Tariff);

        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_Tariff);
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


        // Looper.prepare();

        operationTypeSpinner = (Spinner) findViewById(R.id.spinner_OperationType_Tariff);
        billerSpinner = (Spinner) findViewById(R.id.spinner_Biller_Tariff);
        accountNameSpinner = (Spinner) findViewById(R.id.spinner_AccountType_Tariff);

        nextButton = (Button) findViewById(R.id.nextButton_Tariff);
        nextButton.setOnClickListener(this);

        input_LL = (LinearLayout) findViewById(R.id.input_Recipient_LL_Tariff);
        review_LL = (LinearLayout) findViewById(R.id.review_LL_Tariff);
        billerArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billerSpinner.setAdapter(spinnerArrayAdapter);

        operationCodeArray = getResources().getStringArray(R.array.TxnType);

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 5) {
            // Looper.prepare();
            String[] data = bankAccounts.split("\\;");

            accountNameArray = new String[data.length + 1];
            accountNameArray[0] = getString(R.string.PleaseSelectAccountType);

            accountCodeArray = new String[data.length + 1];
            accountCodeArray[0] = getString(R.string.PleaseSelectAccountType);

            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    accountNameArray[i + 1] = tData[1];
                    accountCodeArray[i + 1] = tData[4];
                }
            }
        } else {
            accountNameArray = new String[1];
            accountNameArray[0] = getString(R.string.payeraccount);
            accountCodeArray = new String[1];
            accountCodeArray[0] = getString(R.string.payeraccount);
        }
     /*   spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountNameArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountNameSpinner.setAdapter(spinnerArrayAdapter);*/

        billerSpinner.setVisibility(View.GONE);
        operationTypeSpinner.setOnItemSelectedListener(this);
    }

    private boolean validateDetails() {
        boolean ret = false;

        //     if (accountNameSpinner.getSelectedItemPosition() > 0) {

        SerialIdString = serialNumberEditText.getText().toString().trim();

        if (SerialIdString.length() >= 8) {
            mpinString = mpinEditText.getText().toString().trim();
            if (mpinString.length() == 4) {

                operationCodeString = operationCodeArray[operationTypeSpinner.getSelectedItemPosition()];
                operationNameString = operationTypeSpinner.getSelectedItem().toString();
                billerCodeString = billerSpinner.getSelectedItem().toString();
                //  accountTypeCodeString = accountCodeArray[accountNameSpinner.getSelectedItemPosition()];

                ret = true;

            } else {
                mpinEditText.requestFocus();
                Toast.makeText(RePrint.this, getString(R.string.pleaseEnterMpin), Toast.LENGTH_SHORT).show();
            }
        } else {
            serialNumberEditText.requestFocus();
            Toast.makeText(RePrint.this, getString(R.string.pleaseEnterSerialId), Toast.LENGTH_SHORT).show();
        }
       /* } else {
            accountNameSpinner.requestFocus();
            Toast.makeText(RePrint.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
        }*/
        return ret;
    }

    public void validateDetails_NextButton() {
        if (isMiniStmtMode) {
            isMiniStmtMode = false;
            mpinEditText.setText("");
            nextButton.setText("Next");
            input_LL.setVisibility(View.VISIBLE);
            review_LL.setVisibility(View.GONE);
        }

        if (validateDetails()) {
            rePrintprocessing();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_Tariff:
                validateDetails_NextButton();
                break;
        }
    }

  //  response reprint Send Money ----showconfcode----Tag Remove
  //  response reprint Receive  Money ----showconfcode----Tag Remove
   // SENDCASH Set Cash To Cash



    void rePrintprocessing() {
        if (new InternetCheck().isConnected(RePrint.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

        //    callApi("getRePrintTransactionInJSON",requestData,126);

            new ServerTask(mComponentInfo, RePrint.this, mHandler, requestData, "getRePrintTransactionInJSON", 126).start();

        } else {
            Toast.makeText(RePrint.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(RePrint.this,mComponentInfo,RePrint.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(RePrint.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(RePrint.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    private String generateTariffData() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

          /*  {"agentCode":"237000271010","pin":"D3F8D3568D92CA73E4D5A1671529F075","comments":"OK","pintype":
                "IPIN","vendorcode":"MICR","clienttype":"GPRS","transrefno":"6554534"}
             */
            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            //countryObj.put("amount", amountString);
            //countryObj.put("source", agentCode);
            countryObj.put("comments", "NOSMS");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            //countryObj.put("transtype", operationCodeString);

            countryObj.put("transrefno", SerialIdString);

            //countryObj.put("fromcity", "YDE");
            // countryObj.put("tocity", "YDE");

            //countryObj.put("udv1", "SAMEBRANCH");
            //countryObj.put("accountType", accountTypeCodeString);
            //  countryObj.put("billerCode", "");
            //  countryObj.put("destination", billerCodeString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(RePrint.this);
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

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());
            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(RePrint.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(RePrint.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(RePrint.this, mComponentInfo, RePrint.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RePrint.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.rePrints));
        //    String[] temp= data.split("\\|");
        //   builder.setMessage(String.format(getString(R.string.tariffResponseSuccess),operationNameString,temp[0], temp[1]) );

        builder.setMessage(getString(R.string.rePrintsSucessReceipt) + " \n " + " \n " + data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                RePrint.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {
                    String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");

                    if (new InternetCheck().isConnected(RePrint.this)) {
                        showProgressDialog(getString(R.string.pleasewait));

                      //  callApi("getRePrintTransactionInJSON",requestData,126);

                        new ServerTask(mComponentInfo, RePrint.this, mHandler, requestData, "getRePrintTransactionInJSON", 126).start();
                    } else {
                        Toast.makeText(RePrint.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }

                } else {
                }
                break;
        }
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 126) {
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "TARIFF").commit();
                    Intent i = new Intent(RePrint.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            }
        } else {
            hideProgressDialog();
            Toast.makeText(RePrint.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessReceipt(String data) {

        String[] recieptData = null;
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        //    bundle.putString("spinnerCountryString", spinnerCountryString);
        //    bundle.putString("tariffAmountFee", tariffAmountFee);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        //   mComponentInfo.getmSharedPreferences().edit().putString("spinnerCountryString", spinnerCountryString).commit();
        //   mComponentInfo.getmSharedPreferences().edit().putString("tariffAmountFee", tariffAmountFee).commit();
        //  mComponentInfo.getmSharedPreferences().edit().putString("transferno", ).commit();
        if (data.trim().length() > 0) {
            recieptData = data.split("\\|");
            //  System.out.print(recieptData);
        } else {

        }
        String txnType = CommonUtilities.txnType;

        switch (txnType) {
            case "CASHIN":
                startActivity(new Intent(this, SucessReceiptReprintCashIn.class));
                break;

            case "CREATEAGENT":
                startActivity(new Intent(this, SucessReceiptReprintCreateAgent.class));
                break;

            case "REMTSEND":
                startActivity(new Intent(this, SucessReceiptReprintSendMoneyToMobile.class));
                break;

            case "REMTRECV":
                startActivity(new Intent(this, SucessReceiptReprintReceiveMoneyToMobile.class));
                break;

            case "CASHTOM":
                startActivity(new Intent(this, SucessReceiptReprintCashToMarchant.class));
                break;

            case "CASHOUT":
                startActivity(new Intent(this, SucessReceiptReprintCashOut.class));
                break;

            case "BILLPAY":   // BILLPAY
                startActivity(new Intent(this, SucessReceiptReprintBillPay.class));
                break;

            case "RECVCASH":
                startActivity(new Intent(this, SucessReceiptReprintCashToCashReceiveMoney.class));
                break;

             case "SENDCASH":

                startActivity(new Intent(this, SucessReceiptReprintCashToCashSameCountry.class));
                break;

            case "REPRINT":   // REPRINT again not valid
                      Toast.makeText(RePrint.this,getString(R.string.transactionNotAllowed),Toast.LENGTH_LONG).show();

                Intent intent3 = new Intent(RePrint.this, RePrint.class);
                startActivity(intent3);
                break;


            case "FEEPAYMENT":

                Intent intent6 = new Intent(RePrint.this, ReprintTutionFeesSh.class);
                intent6.putExtra("data_sh", data);
                startActivity(intent6);

                break;

        }

        //   Intent intent = new Intent(RePrint.this, SucessReceiptCashIn.class);
        //   intent.putExtra("data", data);
        // intent.putExtra("spinnerCountryString", spinnerCountryString);
        //  intent.putExtra("tariffAmountFee", tariffAmountFee);


        //  startActivity(intent);
        RePrint.this.finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            RePrint.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_OperationType_Tariff:

                if (i == 3) {
                    isBillPay = true;
                    billerSpinner.setVisibility(View.VISIBLE);
                } else {
                    isBillPay = false;
                    billerSpinner.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        switch (adapterView.getId()) {

            case R.id.spinner_OperationType_Tariff:

                operationTypeSpinner.setSelection(0);
                billerSpinner.setVisibility(View.GONE);
                break;
        }

    }
}



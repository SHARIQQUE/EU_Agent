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
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptPrintReceipt;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by AdityaBugalia on 31/08/16.
 */
public class PrintReceipt extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

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

    public AutoCompleteTextView transactionNo_EditText, mpinEditText,transactionCode_EditText;
    Spinner spinner_OperationType, spinner_AccountType;
    Button nextButton;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    boolean isBillPay = false;
    LinearLayout input_LL, review_LL;
    ListView statementLsitView;
    ProgressDialog mDialog;

    Dialog successDialog;
    String agentName, transactionCodeString,agentCode, operationCodeString, operationNameString, transactionNoString, mpinString, accountTypeCodeString, billerCodeString, destinationString = "";
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



        setContentView(R.layout.prints_receipts);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_prints);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.printsReceipt));
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
        transactionNo_EditText = (AutoCompleteTextView) findViewById(R.id.transactionNo_EditText);
        transactionCode_EditText= (AutoCompleteTextView) findViewById(R.id.transactionCode_EditText);

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

        spinner_OperationType = (Spinner) findViewById(R.id.spinner_OperationType);
        spinner_AccountType = (Spinner) findViewById(R.id.spinner_AccountType);

        nextButton = (Button) findViewById(R.id.nextButton_Tariff);
        nextButton.setOnClickListener(this);

        input_LL = (LinearLayout) findViewById(R.id.input_Recipient_LL_Tariff);
        review_LL = (LinearLayout) findViewById(R.id.review_LL_Tariff);
        billerArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");


        operationCodeArray = getResources().getStringArray(R.array.TxnTypeTransactionApproval);

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
       /* ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountNameArray); //selected item will look like a spinner set from XML
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_AccountType.setAdapter(arrayAdapter);*/

     //   spinner_OperationType.setOnItemSelectedListener(this);
    }

    private boolean validateDetails() {
        boolean ret = false;

      //  if (spinner_OperationType.getSelectedItemPosition() > 0) {

           // if (spinner_AccountType.getSelectedItemPosition() > 0) {

                transactionNoString = transactionNo_EditText.getText().toString().trim();
                if (transactionNoString.length() >= 8) {

                    transactionCodeString=transactionCode_EditText.getText().toString().trim();
                    if(transactionCodeString.length()>=4)
                    {

                    mpinString = mpinEditText.getText().toString().trim();
                    if (mpinString.length() == 4) {

                      //  operationCodeString = operationCodeArray[spinner_OperationType.getSelectedItemPosition()];
                       // operationNameString = spinner_OperationType.getSelectedItem().toString();
                      //  accountTypeCodeString = accountCodeArray[spinner_AccountType.getSelectedItemPosition()];

                        ret = true;

                    } else {
                        mpinEditText.requestFocus();
                        Toast.makeText(PrintReceipt.this, getString(R.string.pleaseEnterMpin), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mpinEditText.requestFocus();
                    Toast.makeText(PrintReceipt.this, getString(R.string.pleaseEntertransactionCode), Toast.LENGTH_SHORT).show();
                }
                } else {
                    transactionNo_EditText.requestFocus();
                    Toast.makeText(PrintReceipt.this, getString(R.string.enterTransactionNumber), Toast.LENGTH_SHORT).show();
                }
            /*} else {
                spinner_AccountType.requestFocus();
                Toast.makeText(PrintReceipt.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
            }*/
       /* } else {
            Toast.makeText(PrintReceipt.this, getString(R.string.PleaseSelectOperationType), Toast.LENGTH_SHORT).show();
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
            printsRequest();
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

    void printsRequest() {
        if (new InternetCheck().isConnected(PrintReceipt.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generatePrintReceipt();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

        //    callApi("printJSON",requestData,147);


            new ServerTask(mComponentInfo, PrintReceipt.this, mHandler, requestData, "printJSON", 147).start();
        } else {
            Toast.makeText(PrintReceipt.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(PrintReceipt.this,mComponentInfo,PrintReceipt.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(PrintReceipt.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(PrintReceipt.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    private String generatePrintReceipt() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {



            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("source", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("comments", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("requestcts", "");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transrefno",transactionNoString);
            countryObj.put("transcode",transactionCodeString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(PrintReceipt.this);
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
                Toast.makeText(PrintReceipt.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(PrintReceipt.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(PrintReceipt.this, mComponentInfo, PrintReceipt.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };


    private void showSuccessReceipt(String data) {

        String RECEIPTPRINTDATA;

        RECEIPTPRINTDATA=data+"|RECEIPTPRINT";

        mComponentInfo.getmSharedPreferences().edit().putString("data", RECEIPTPRINTDATA).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("transactionCodeString",transactionCodeString).commit();


        startActivity(new Intent(this, SucessReceiptPrintReceipt.class));
        PrintReceipt.this.finish();
    }


    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PrintReceipt.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.prints));
        //    String[] temp= data.split("\\|");
        //   builder.setMessage(String.format(getString(R.string.tariffResponseSuccess),operationNameString,temp[0], temp[1]) );

        builder.setMessage(getString(R.string.printsSucessReceipt));// + " \n " + " \n " + data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // proceedApprove();
            }
        });


       /* builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                TransactionApproval.this.finish();
            }

        });*/       // Cancel Button


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

                    if (new InternetCheck().isConnected(PrintReceipt.this)) {
                        showProgressDialog(getString(R.string.pleasewait));
                    //    callApi("printJSON",requestData,147);
                       new ServerTask(mComponentInfo, PrintReceipt.this, mHandler, requestData, "printJSON", 147).start();
                    } else {
                        Toast.makeText(PrintReceipt.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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

            if (requestNo == 147) {
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "TARIFF").commit();
                    Intent i = new Intent(PrintReceipt.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);

                }
                else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            }
        } else {
            hideProgressDialog();
          //  showSuccess(generalResponseModel.getUserDefinedString(), 1);   // test
            Toast.makeText(PrintReceipt.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            PrintReceipt.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        switch (adapterView.getId()) {
        }

    }
}



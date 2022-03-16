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
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

public class AccountBalanceSpinner extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier,AdapterView.OnItemSelectedListener{
    private void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
                validateDetails();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    String balanceCodeSpinnerValue;
    Spinner spinner_accountBalanceType;
    AutoCompleteTextView mpinEditText;
    ComponentMd5SharedPre mComponentInfo;
    Toolbar mToolbar;
    String agentName, agentCode, mpinString, payerAccountString, accountCodeString;
    Button nextButton;
    String[] payerBankAccountsArray, payerAccountCodeArray;
    ProgressDialog mDialog;
    Dialog successDialog;
    String[] balanceCheckType;
    String[] balanceCheckCode;
    String balanceType,balanceCode;

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


        setContentView(R.layout.account_balance_spinner);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_BalanceCheck);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.accountBalanceNew));
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



        // Spinner element
        spinner_accountBalanceType= (Spinner)findViewById(R.id.spinner_accountBalanceType);

        // Spinner click listener
        spinner_accountBalanceType.setOnItemSelectedListener(this);

        // Spinner Drop down elements

        balanceCheckType= getResources().getStringArray(R.array.balanceCheckType);
        balanceCheckCode=  getResources().getStringArray(R.array.balanceCheckCode);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, balanceCheckType);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_accountBalanceType.setAdapter(dataAdapter);
        spinner_accountBalanceType.setSelection(1);

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


    }

    public void validateDetails() {

        if (validateDetails_PartI()) {
            proceedBalanceCheck();
        }
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
                Toast.makeText(AccountBalanceSpinner.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(AccountBalanceSpinner.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(AccountBalanceSpinner.this, mComponentInfo, AccountBalanceSpinner.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }
    };

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(AccountBalanceSpinner.this);
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

    private void proceedBalanceCheck() {

        if (new InternetCheck().isConnected(AccountBalanceSpinner.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateBalanceCheckData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, AccountBalanceSpinner.this, mHandler, requestData, "getBalanceInJSON", 112).start();

        } else {
            Toast.makeText(AccountBalanceSpinner.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {

                    if (new InternetCheck().isConnected(AccountBalanceSpinner.this)) {
                        showProgressDialog(getString(R.string.pleasewait));

                        String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");
                        new ServerTask(mComponentInfo, AccountBalanceSpinner.this, mHandler, requestData, "getBalanceInJSON", 112).start();
                    } else {
                        Toast.makeText(AccountBalanceSpinner.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }


                } else {


                }


                break;


        }
    }

    private String generateBalanceCheckData() {
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
            countryObj.put("accountType", "MA");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);



            if(balanceCodeSpinnerValue.equalsIgnoreCase("BAL"))
           {

           }

           else
           {
               countryObj.put("wallettype", "COMM");  //  Secondary Balance
           }




            jsonString = countryObj.toString();

        } catch (Exception e) {
            Log.e("blc Exception", e.toString());
        }

        return jsonString;
    }




    private boolean validateDetails_PartI() {
        boolean ret = false;

        if (spinner_accountBalanceType.getSelectedItemPosition() > 0) {

        mpinString = mpinEditText.getText().toString();
        if (mpinString.trim().length() == 4) {

            ret = true;

        } else {
            Toast.makeText(AccountBalanceSpinner.this, getString(R.string.mpinAccountBalance), Toast.LENGTH_SHORT).show();
        }
       } else {
            Toast.makeText(AccountBalanceSpinner.this, getString(R.string.pleaseselectbankaccount), Toast.LENGTH_SHORT).show();
        }

        return ret;

    }

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountBalanceSpinner.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.balancecheck));


        /*payerAccountString = payerAccountString.replace("_", "-");
        payerAccountString = payerAccountString.toUpperCase();*/

        builder.setMessage(String.format(getString(R.string.balanceSuccessResponse), data));


        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                AccountBalanceSpinner.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            AccountBalanceSpinner.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        String selectedValueSPinner = parent.getItemAtPosition(position).toString();
      //  Toast.makeText(parent.getContext(), "selected Value = " + selectedValueSPinner, Toast.LENGTH_LONG).show();

         balanceType = balanceCheckType[position];
         balanceCode = balanceCheckCode[position];

         balanceCodeSpinnerValue=balanceCode;

        if(position==1)
        {
            balanceCodeSpinnerValue=balanceCode;
            System.out.println(balanceCodeSpinnerValue);
        }
        else {
            balanceCodeSpinnerValue=balanceCode;
            System.out.println(balanceCodeSpinnerValue);
        }


    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();
            if (requestNo == 112) {

                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "BALANCE").commit();
                    Intent i = new Intent(AccountBalanceSpinner.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString(), 1);
                }
            }
        } else {
            hideProgressDialog();
            Toast.makeText(AccountBalanceSpinner.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }
    }

}

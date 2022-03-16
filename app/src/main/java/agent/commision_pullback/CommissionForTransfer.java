package agent.commision_pullback;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import agent.activities.OTPVerificationActivity;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

public class CommissionForTransfer extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier {

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


    Spinner payerAccountsSpinner;
    AutoCompleteTextView mpinEditText;
    ComponentMd5SharedPre mComponentInfo;
    Toolbar mToolbar;
    String agentName, agentCode, mpinString, payerAccountString, accountCodeString;
    Button nextButton;
    String[] payerBankAccountsArray, payerAccountCodeArray;
    ProgressDialog mDialog;
    Dialog successDialog;


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


        setContentView(R.layout.commision_for_transfer);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_walletForTransfer);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.commisionForTransfer_capital));
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
        payerAccountsSpinner = (Spinner) findViewById(R.id.spinner_AccountType_BalanceCheck);

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 5) {
            String[] data = bankAccounts.split("\\;");
            payerBankAccountsArray = new String[data.length + 1];
            payerBankAccountsArray[0] = getString(R.string.pleaseselectaccount);
            payerAccountCodeArray = new String[data.length + 1];
            payerAccountCodeArray[0] = getString(R.string.pleaseselectaccount);
            try {
                for (int i = 0; i < data.length; i++) {
                    String[] tData = data[i].split("\\|");
                    if (tData.length > 0) {
                        payerBankAccountsArray[i + 1] = tData[0] + " - " + tData[1];
                        payerAccountCodeArray[i + 1] = tData[4];
                    }
                }
            } catch (Exception e) {


            }
        } else {
            payerBankAccountsArray = new String[1];
            payerBankAccountsArray[0] = getString(R.string.payeraccount);

            payerAccountCodeArray = new String[1];
            payerAccountCodeArray[0] = getString(R.string.payeraccount);
        }

        payerAccountsSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_BalanceCheck);
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
                Toast.makeText(CommissionForTransfer.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(CommissionForTransfer.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(CommissionForTransfer.this, mComponentInfo, CommissionForTransfer.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }
    };

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CommissionForTransfer.this);
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

        if (new InternetCheck().isConnected(CommissionForTransfer.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateCommissionTransfer();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, CommissionForTransfer.this, mHandler, requestData, "pullBackCommission", 185).start();

        } else {
            Toast.makeText(CommissionForTransfer.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {

                    if (new InternetCheck().isConnected(CommissionForTransfer.this)) {
                        showProgressDialog(getString(R.string.pleasewait));

                        String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");
                        new ServerTask(mComponentInfo, CommissionForTransfer.this, mHandler, requestData, "pullBackCommission", 185).start();
                    } else {
                        Toast.makeText(CommissionForTransfer.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }


                } else {


                }


                break;


        }
    }

    private String generateCommissionTransfer() {
        String jsonString = "";


        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {
            JSONObject countryObj = new JSONObject();

            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", agentCode);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return jsonString;
    }


    private boolean validateDetails_PartI() {
        boolean ret = false;

      //  if (payerAccountsSpinner.getSelectedItemPosition() > 0) {

            mpinString = mpinEditText.getText().toString();
            if (mpinString.trim().length() == 4) {

                payerAccountString = payerAccountsSpinner.getSelectedItem().toString();
                accountCodeString = payerAccountCodeArray[payerAccountsSpinner.getSelectedItemPosition()];
                ret = true;

            } else {
                Toast.makeText(CommissionForTransfer.this, getString(R.string.mpinAccountBalance), Toast.LENGTH_SHORT).show();
            }
    /*    } else {
            Toast.makeText(AccountBalance.this, getString(R.string.pleaseselectbankaccount), Toast.LENGTH_SHORT).show();
        }*/

        return ret;

    }

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommissionForTransfer.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.commisionForTransfer_capital));
        payerAccountString = payerAccountString.replace("_", "-");
        payerAccountString = payerAccountString.toUpperCase();

        builder.setMessage(String.format(getString(R.string.successCommissionForTransfer), data));


        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                CommissionForTransfer.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            CommissionForTransfer.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();

            if (requestNo == 185) {

                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "BALANCE").commit();
                    Intent i = new Intent(CommissionForTransfer.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString(), 1);
                }
            }
        } else {
            hideProgressDialog();
            Toast.makeText(CommissionForTransfer.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }
    }

}

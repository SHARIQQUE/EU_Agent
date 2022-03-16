package agent.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static agent.thread.ServerTask.baseUrl;


/**
 * Created by AdityaBugalia on 09/08/16.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ServerResponseParseCompletedNotifier,
        AutoCompleteTextView.OnEditorActionListener {

    boolean isRequestQuestion = false;
    Dialog  securityQuestionDialog;
    Dialog successDialog;
    TextView textview_app_version;

    private static final int WRITE_EXTERNAL_STORAGE = 1;
    private static final int READ_PHONE_STATE = 5;
    private static final int REQUEST_SMS_PHONE = 2;
    private static final int REQUEST_READ_PHONE = 0;
    private static final int CAMERA_READ = 3;
    private Spinner countrySpinner;
    AppCompatEditText answerEditText;

    private EditText mobileNoEditTextLogin, mpinEditText, termilaidEditText;
    private TextInputLayout mobileNoTIL, mpinTIL, terminalIdTIL;
    private Button proccedButton;
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray;
    private ComponentMd5SharedPre mComponentInfo;
    private TextView loginWithAnotherAccount_textView_Login;

    private Snackbar mSnackBar;
    private ProgressDialog mDialog;
    String otpString, languageToUse,agentcode, process, agentName, imeiNo, wifiMacAddress, requestString, mpin;
    private boolean canExitApp;
    Intent displayActivityIntent;
    SharedPreferences.Editor editor;
    String selectedQuestion = "", selectedAnswer = "", selectedQuestionCode = "";


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());

            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                showErrorSnackBar(getString(R.string.pleaseCheckInternet));
                canExitApp = true;
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                showErrorSnackBar(getString(R.string.pleaseTryAgainLater));
                canExitApp = true;
            } else {

                System.out.println("requestString====================" + requestString);
                DataParserThread thread = new DataParserThread(LoginActivity.this, mComponentInfo, LoginActivity.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String countryCode, mobileNo, termilaid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

         languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "en";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.login_activity);

        textview_app_version = (TextView)findViewById(R.id.textview_app_version);


        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");


        } catch (Exception e) {
            e.printStackTrace();
        }

        countrySpinner = (Spinner) findViewById(R.id.spinnerCountryLogin);
        countrySpinner.setOnItemSelectedListener(this);

        mobileNoEditTextLogin = (EditText) findViewById(R.id.mobileNoEditTextLogin);
        mpinEditText = (EditText) findViewById(R.id.mpinEditTextLogin);
        mpinEditText.setOnEditorActionListener(this);

        loginWithAnotherAccount_textView_Login = (TextView) findViewById(R.id.loginWithAnotherAccount_textView_Login);
        loginWithAnotherAccount_textView_Login.setOnClickListener(this);


        mobileNoTIL = (TextInputLayout) findViewById(R.id.mobileNoEditTextLoginTIL);
        mpinTIL = (TextInputLayout) findViewById(R.id.mpinEditTextLoginTIL);

        terminalIdTIL = (TextInputLayout) findViewById(R.id.terminalIdEditTextLoginTIL);
        termilaidEditText = (EditText) findViewById(R.id.terminalIdNoEditTextLogin);

        proccedButton = (Button) findViewById(R.id.proceedButtonLogin);
        proccedButton.setOnClickListener(this);

        countrySpinner.setOnItemSelectedListener(this);

        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        countrySpinner.setAdapter(adapter);

        //  countrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        countrySpinner.requestFocus();
        displayActivityIntent = new Intent(LoginActivity.this, CustomerService.class);
        editor = mComponentInfo.getmSharedPreferences().edit();

        if (mComponentInfo.getmSharedPreferences().getBoolean("isPreviousLogin", false)) {

            loginWithAnotherAccount_textView_Login.setVisibility(View.VISIBLE);
            countrySpinner.setEnabled(false);
            mobileNoEditTextLogin.setEnabled(false);
            termilaidEditText.setEnabled(false);

            mobileNoEditTextLogin.setText(mComponentInfo.getmSharedPreferences().getString("loggedInMobileNo", ""));
            termilaidEditText.setText(mComponentInfo.getmSharedPreferences().getString("loggedInTerminalID", ""));
            countrySpinner.setSelection(mComponentInfo.getmSharedPreferences().getInt("loggedInCountryPosition", 0));
        }


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


        updateVersionRequestCheck_request();

    }

    private void showErrorSnackBar(String message) {
        mSnackBar = Snackbar.make(findViewById(R.id.container_Login), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(LoginActivity.this);
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

    private boolean validateDetails() {

        boolean ret = false;
        if (countrySpinner.getSelectedItemPosition() != 0) {
            if (mobileNoEditTextLogin != null && mpinEditText != null && termilaidEditText != null) {
                mobileNoEditTextLogin.requestFocus();

                countryCode = countryCodeArray[countrySpinner.getSelectedItemPosition()];
                mobileNo = mobileNoEditTextLogin.getText().toString();
                termilaid = termilaidEditText.getText().toString();
                mpin = mpinEditText.getText().toString();

                if (mobileNo.trim().length() == Integer.parseInt(countryMobileNoLengthArray[countrySpinner.getSelectedItemPosition()])) {
                    mobileNoTIL.setError(null);
                    mpinEditText.requestFocus();

                    mobileNo = countryPrefixArray[countrySpinner.getSelectedItemPosition()] + mobileNo;

                    if (termilaid.trim().length() >= 8) {
                        if (mpin.trim().length() == 4) {
                            mpinTIL.setError(null);

                            ret = true;

                        } else {
                            mpinEditText.requestFocus();
                            showErrorSnackBar(getString(R.string.pleaseEnterMpinLogin));
                        }
                    } else {

                        if (mComponentInfo.getmSharedPreferences().getBoolean("isPreviousLogin", false)) {
                            countrySpinner.setEnabled(true);
                            mobileNoEditTextLogin.setEnabled(true);
                            termilaidEditText.setEnabled(true);
                        }
                        // termilaidEditText.requestFocus();
                        // showErrorSnackBar(getString(R.string.pleaseEnterTermilaId));
                        Toast.makeText(LoginActivity.this, getString(R.string.pleaseEnterTermilaId), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    if (mComponentInfo.getmSharedPreferences().getBoolean("isPreviousLogin", false)) {


                        countrySpinner.setEnabled(true);
                        mobileNoEditTextLogin.setEnabled(true);
                        termilaidEditText.setEnabled(true);


                    }
                    mobileNoEditTextLogin.requestFocus();
                    Toast.makeText(LoginActivity.this, getString(R.string.incorrectmobilenumber) + " " + countryMobileNoLengthArray[countrySpinner.getSelectedItemPosition()] + " Digits Number", Toast.LENGTH_SHORT).show();
                }


            } else {
                if (mComponentInfo.getmSharedPreferences().getBoolean("isPreviousLogin", false)) {


                    countrySpinner.setEnabled(true);
                    mobileNoEditTextLogin.setEnabled(true);
                    termilaidEditText.setEnabled(true);


                }
                Toast.makeText(LoginActivity.this, getString(R.string.restartapp), Toast.LENGTH_SHORT).show();
                showErrorSnackBar(getString(R.string.restartapp));
            }
        } else {
            if (mComponentInfo.getmSharedPreferences().getBoolean("isPreviousLogin", false)) {

                countrySpinner.setEnabled(true);
                mobileNoEditTextLogin.setEnabled(true);
                termilaidEditText.setEnabled(true);

            }

            Toast.makeText(LoginActivity.this, getString(R.string.pleaseSelectCountry), Toast.LENGTH_SHORT).show();
            //showErrorSnackBar("Please Select Country");
        }
        return ret;
    }

    private void masterData() {

        if (new InternetCheck().isConnected(LoginActivity.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateMasterData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, LoginActivity.this, mHandler, requestData, "masterdata", 192).start();

        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    public void startServerInteraction(String data, String apiName, int requestNo) {


        if (new InternetCheck().isConnected(LoginActivity.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            new ServerTask(mComponentInfo, LoginActivity.this, mHandler, data, apiName, requestNo).start();
        } else {

            Toast.makeText(LoginActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

        }
    }



    private void startServerInteraction() {
        hideKeyboard();
        try {
            if (new InternetCheck().isConnected(LoginActivity.this)) {
                String requestData = "";
                if (mayRequestPhoneState()) {

                    requestData = generateLoginData();
                } else {
                    hideProgressDialog();
                    return;
                }
                showProgressDialog(getString(R.string.loggingin));

                mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

                //      callApi("getLoginInJSON",requestData,102);
                new ServerTask(mComponentInfo, LoginActivity.this, mHandler, requestData, "getLoginInJSON", 102).start();

            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

                canExitApp = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceedButtonLogin:

                if (validateDetails()) {

                    if (mComponentInfo.getmSharedPreferences().getBoolean("isPreviousLogin", false))
                    {
                         startServerInteraction();
                    }
                    else {
                        perform_SecurityQuestion_Request();
                    }
                }


                break;

            case R.id.loginWithAnotherAccount_textView_Login:
                mComponentInfo.getmSharedPreferences().edit().putBoolean("isPreviousLogin", false).commit();

                loginWithAnotherAccount_textView_Login.setVisibility(View.GONE);


                countrySpinner.setEnabled(true);
                mobileNoEditTextLogin.setEnabled(true);
                termilaidEditText.setEnabled(true);

                mobileNoEditTextLogin.setText("");
                termilaidEditText.setText("");


                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        switch (adapterView.getId()) {

            case R.id.spinnerCountryLogin:
                InputFilter[] filterArray = new InputFilter[1];
                if (i > 0) {
                    filterArray[0] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[i]));
                } else {
                    filterArray[0] = new InputFilter.LengthFilter(0);
                }


                String countrySelectionString = countrySpinner.getSelectedItem().toString();
                SharedPreferences.Editor Eeditor = getSharedPreferences("countrySelectionString", MODE_PRIVATE).edit();
                Eeditor.putString("countrySelectionString", countrySelectionString);
                Eeditor.commit();


                String countrySelectionCode = countryCodeArray[i];
                SharedPreferences.Editor Eeditor2 = getSharedPreferences("countrySelectionCode", MODE_PRIVATE).edit();
                Eeditor2.putString("countrySelectionCode", countrySelectionCode);
                Eeditor2.commit();




                mobileNoEditTextLogin.setFilters(filterArray);
                mobileNoEditTextLogin.setInputType(InputType.TYPE_CLASS_NUMBER);
                // mobileNoEditTextLogin.setText("");

                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void generateSystemInfo() {

       /* WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        wifiMacAddress = wInfo.getMacAddress();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imeiNo = telephonyManager.getDeviceId();*/


        if (imeiNo == null || imeiNo.trim().length() == 0) {

            imeiNo = randomImei(16);
        }

        if (wifiMacAddress == null || wifiMacAddress.trim().length() == 0) {
            wifiMacAddress = randomMac(12);

        }

        System.out.println(imeiNo);
        System.out.println(wifiMacAddress);

    }

    private String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String randomImei(int count) {

        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
       // return builder.toString() + DateFormat.getDateTimeInstance().format(new Date());
        return builder.toString();
    }

    public String randomMac(int count) {

        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));

        }

      //  return builder.toString() + DateFormat.getDateTimeInstance().format(new Date());
        return builder.toString();

    }

    private void copyActivationData() {

    }

    private String generateActivationData() {
        agentcode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        mpin = mpinEditText.getText().toString().trim();
        int shrtCode1 = generateRandomNo();
        int shrtCode2 = generateRandomNo();

        generateSystemInfo();
        //  imeiNo="25825810";

        SharedPreferences.Editor editor = mComponentInfo.getmSharedPreferences().edit();
        editor.putInt("shrtCode1", shrtCode1);
        editor.putInt("shrtCode2", shrtCode2);
        editor.putString("imeiNo", imeiNo);  // not imei number :- generate Random number
        editor.putString("wifiMacAddress", wifiMacAddress);
        editor.commit();

        String jsonString = "";


        mpin = mComponentInfo.getMD5(agentcode + mpin).toUpperCase();


        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentcode);
            countryObj.put("pin", mpin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("imei", imeiNo);  // not imei number :- generate Random number
            countryObj.put("systemId", wifiMacAddress);
            countryObj.put("s1", shrtCode1);
            countryObj.put("s2", shrtCode2);
            countryObj.put("mobileno", agentcode);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }

        return jsonString;


    }


    private int generateRandomNo() {
        Random ran = new Random();
        int x = ran.nextInt((999 - 100) + 1) + 100;
        return x;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {

                    if (new InternetCheck().isConnected(LoginActivity.this)) {
                        showProgressDialog(getString(R.string.activating));
                        if (mayRequestPhoneState()) {

                            requestString = generateActivationData();
                        } else {
                            hideProgressDialog();
                            return;
                        }
                        System.out.println("requestString====================" + requestString);

                        //            callApi("getActivationInJSON",requestString,104);
                        new ServerTask(mComponentInfo, LoginActivity.this, mHandler, requestString, "getActivationInJSON", 104).start();
                    } else {
                        hideProgressDialog();
                        Toast.makeText(LoginActivity.this, getString(R.string.errorlogininternet), Toast.LENGTH_SHORT).show();
                        //canExitApp = true;
                    }
                } else {


                }


                break;


        }


    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        if (generalResponseModel.getResponseCode() == 0) {


            if (requestNo == 2021) {

                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");
                    String updateVersionFromApi=responseArray[0];

                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

                    int currentVersionApp = pInfo.versionCode;

                    String currentVersionAppString=String.valueOf(currentVersionApp);//Now it will return "10"
                    textview_app_version.setText(getString(R.string.app_version)+" : "+currentVersionAppString);
                    int updateVersionCodeFromApi = Integer.parseInt(updateVersionFromApi);


                    //  currentVersionApp=11;
                    //  updateVersionCodeFromApi=10;

                    // app version and backend version (updateVersionFromApi) is same it  == ok
                    // app version  greater than backend version (updateVersionFromApi)  == ok
                    // app version less than than backend version (updateVersionFromApi) Then Block  == then Update app


                    if (currentVersionApp == updateVersionCodeFromApi) // 11 == 11 = ok
                    {
                        hideProgressDialog();
                    } else if (currentVersionApp > updateVersionCodeFromApi) //  12 > 11 = ok
                    {
                        hideProgressDialog();
                    } else {
                        hideProgressDialog();

                        updateDialogueBox();
                    }


                } catch (Exception e) {

                    Toast.makeText(this, e.toString() + "Error code 2021", Toast.LENGTH_LONG).show();
                    finish();

                }



            }


          else if (requestNo == 102) {
                //    int Case =(getBoo==true) ?2:3;                                      //agent   2    = frenchise 3
                if (generalResponseModel.getResponseList().get("accounttype").toString().equalsIgnoreCase("2")) {

                    if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("OTP")) {
                        SharedPreferences.Editor editor = mComponentInfo.getmSharedPreferences().edit();
                        editor.putString("agentCode", generalResponseModel.getResponseList().get("agentcode").toString());
                        editor.putString("agentName", generalResponseModel.getResponseList().get("agentname").toString());
                        editor.putString("currency", generalResponseModel.getResponseList().get("currency").toString());
                        editor.putString("country", generalResponseModel.getResponseList().get("country").toString());
                        editor.putString("accountType", generalResponseModel.getResponseList().get("accounttype").toString());
                        editor.putString("otp", generalResponseModel.getResponseList().get("otp").toString());
                        editor.putString("currency", generalResponseModel.getResponseList().get("currency").toString());

                        try {


                            editor.putString("tla", generalResponseModel.getResponseList().get("tla").toString());
                            editor.putString("tlacode", generalResponseModel.getResponseList().get("tlacode").toString());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        String data = generalResponseModel.getResponseList().get("state").toString();
                        editor.putString("state", data);


                        // data for OTP Verification
                        editor.putString("process", "LOGIN");
                         /*editor.putString("","");
                        editor.putString("","");
                        editor.putString("","");*/
                        editor.commit();
                        hideProgressDialog();
                        mComponentInfo.set_MPIN_OTP_STRING(mpin);
                        mComponentInfo.set_IS_HIDE_MPIN_OTP(true);


                        Intent i = new Intent(LoginActivity.this, OTPVerificationActivity.class);
                        startActivityForResult(i, 299);


                    } else {
                        hideProgressDialog();
                        SharedPreferences.Editor editor = mComponentInfo.getmSharedPreferences().edit();
                        String agentName = generalResponseModel.getResponseList().get("agentname").toString();
                        editor.putString("agentCode", generalResponseModel.getResponseList().get("agentcode").toString());
                        editor.putString("agentName", generalResponseModel.getResponseList().get("agentname").toString());
                        editor.putString("currency", generalResponseModel.getResponseList().get("currency").toString());
                        editor.putString("country", generalResponseModel.getResponseList().get("country").toString());
                        editor.putString("accountType", generalResponseModel.getResponseList().get("accounttype").toString());


                        editor.commit();
                        if (new InternetCheck().isConnected(LoginActivity.this)) {
                            showProgressDialog(getString(R.string.gettingdetails) + "  " + agentName);
                            new ServerTask(mComponentInfo, LoginActivity.this, mHandler, "", "agentInfo?agentcode=" + generalResponseModel.getResponseList().get("agentcode").toString(), 105).start();
                        } else {
                            showErrorSnackBar(getString(R.string.pleaseCheckInternet));
                            canExitApp = true;
                        }
                    }
              /*  } else {
                    hideProgressDialog();
                    Toast.makeText(LoginActivity.this, getString(R.string.login_error_response), Toast.LENGTH_SHORT).show();
                }*/


                } else {
                    hideProgressDialog();
                    Toast.makeText(LoginActivity.this, getString(R.string.pleaseLoginAgent), Toast.LENGTH_SHORT).show();
                }


            }

            else if (requestNo == 104) {
                String TempData = generalResponseModel.getUserDefinedString();

                String[] arrayString = TempData.split("\\|");

                String stringTemp = arrayString[1];

                if (stringTemp.equalsIgnoreCase("Transaction Successful"))   // 7 december Transaction Successful 2017 Response
                {

                    mComponentInfo.getmSharedPreferences().edit().putBoolean("isPreviousLogin", true).commit();


                    loginWithAnotherAccount_textView_Login.setVisibility(View.VISIBLE);


                    mComponentInfo.getmSharedPreferences().edit().putString("loggedInMobileNo", mobileNoEditTextLogin.getText().toString()).commit();
                    mComponentInfo.getmSharedPreferences().edit().putString("loggedInTerminalID", termilaidEditText.getText().toString()).commit();
                    mComponentInfo.getmSharedPreferences().edit().putInt("loggedInCountryPosition", countrySpinner.getSelectedItemPosition()).commit();
                    mComponentInfo.getmSharedPreferences().edit().putString("loggedInIMEI", imeiNo).commit();
                    mComponentInfo.getmSharedPreferences().edit().putString("loggedInMACAddress", wifiMacAddress).commit();


                    masterData();

                } else {
                    mpinEditText.setText("");
                    hideProgressDialog();
                    Toast.makeText(LoginActivity.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                }


            }

            else if (requestNo == 214) {
                // mpinEditText.setText
                hideProgressDialog();

                if (isRequestQuestion) {

                    String[] data = generalResponseModel.getUserDefinedString().split("\\#");

                    show_SecurityQuestionDialog(generalResponseModel.getUserDefinedString());

                } else {

                    startServerInteraction();
                }

                //perform_SecurityQuestion_Request();


            }

            else if (requestNo == 192) {

                String tempdata4 = generalResponseModel.getUserDefinedString4();
                String tempdata5 = generalResponseModel.getUserDefinedString5();
                String tempdata6 = generalResponseModel.getUserDefinedString6();

                SharedPreferences.Editor editor = getSharedPreferences("PROFILEDETAILS", MODE_PRIVATE).edit();
                editor.putString("profiles", tempdata4);
                editor.putString("branches", tempdata5);
                editor.putString("idproofs", tempdata6);
                editor.commit();


                Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();

            } else if (requestNo == 105) {

               /* hideProgressDialog();
                editor.putString("bankAccounts", generalResponseModel.getUserDefinedString());
                editor.commit();*/
                // Toast.makeText(LoginActivity.this, "ho gya", Toast.LENGTH_SHORT).show();

                //  showProgressDialog(getString(R.string.pleasewait));

                editor.putString("bankAccounts", generalResponseModel.getUserDefinedString());
                editor.putString("cityLogin", generalResponseModel.getUserDefinedString1());
                editor.commit();

                if (new InternetCheck().isConnected(LoginActivity.this)) {
                    startServerInteraction(generateCityData(countryCode), "getStateListInJSON", 154);


                    //    LoginActivity.this.finish();
                } else {
                    showErrorSnackBar(getString(R.string.pleaseCheckInternet));
                    canExitApp = true;
                }

            } else if (requestNo == 154) {


                mComponentInfo.getmSharedPreferences().edit().putString("cityList", generalResponseModel.getUserDefinedString()).commit();


                masterData();

               /* Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();*/

            } else if (requestNo == 106) {
                hideProgressDialog();
                editor.putString("billerDetails", getString(R.string.pleaseselectbiller) + generalResponseModel.getUserDefinedString());
                editor.commit();

            } else {
                mpinEditText.setText("");
                hideProgressDialog();
                showErrorSnackBar(generalResponseModel.getUserDefinedString());
            }
        } else {
            mpinEditText.setText("");
            hideProgressDialog();
            showErrorSnackBar(generalResponseModel.getUserDefinedString());
        }

    }

    private String generateCityData(String CountryCode) {

        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", mComponentInfo.getmSharedPreferences().getString("agentCode", ""));

            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("countrycode", CountryCode);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }

        return jsonString;
    }


    //////////////////////// Security Question ////////////////////////////////////////////////
    void perform_SecurityQuestion_Request() {

        isRequestQuestion = true;
        if (validateDetails()) {
            if (mayRequestPhoneState()) {

                startServerInteraction("secretQuestion", generateSecretQuestionData("", "", ""), 214, getString(R.string.loggingin));
            }
            hideKeyboard();
        }

    }

    private String generateSecretQuestionData(String question, String answer, String questionCode) {

        String jsonString = "";

        String pinTemp  = mComponentInfo.getMD5(mobileNo + mpin).toUpperCase();


        //  imeiNo="25825810";

        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", mobileNo);
            countryObj.put("pin", pinTemp);
            countryObj.put("pintype", "MPIN");

            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("question", question);
            countryObj.put("answer", answer);
            countryObj.put("questioncode", questionCode);
            countryObj.put("language", languageToUse);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        requestString = jsonString;
        return jsonString;

    }



    void perform_SecurityQuestion_Verify(String question, String answer, String questionCode) {
        isRequestQuestion = false;
        if (validateDetails()) {
            if (mayRequestPhoneState()) {

                startServerInteraction("secretQuestion", generateSecretQuestionData(question, answer, questionCode), 214, getString(R.string.loggingin));
            }
            hideKeyboard();
        }

    }

    void show_SecurityQuestionDialog(final String questionArr) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View v = getLayoutInflater().inflate(R.layout.security_question_layout, null);

        final String[] questions, answers, questionCode;


        String[] data = questionArr.split("\\#");

        if (data[0].equalsIgnoreCase("")) {

            Toast.makeText(LoginActivity.this, getString(R.string.plzTryAgainLater), Toast.LENGTH_LONG).show();
        }

        else {

            ArrayList<String> questionsList = new ArrayList<String>();
            ArrayList<String> answersList = new ArrayList<String>();
            ArrayList<String> questionCodeList = new ArrayList<String>();
            questionsList.add("Please Select Question");
            answersList.add("Please Select Answer");
            questionCodeList.add("Please Select Question Code");


            for (int i = 0; i < data.length; i++) {

                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    // if(tData[4].equalsIgnoreCase("MA")) {
                    questionsList.add(tData[0]);

                    answersList.add(tData[1]);
                    questionCodeList.add(tData[2]);
                    // }
                }


            }
            questions = questionsList.toArray(new String[questionsList.size()]);

            answers = answersList.toArray(new String[answersList.size()]);
            questionCode = questionCodeList.toArray(new String[questionCodeList.size()]);


            //String message= String.format(getString(R.string.pendingaccountupdateapprove),temp[0],temp[1]);
//
            Spinner spinner = (Spinner) v.findViewById(R.id.question_Spinner_security_question);

            spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, questions));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedQuestion = questions[position];
                    selectedAnswer = answers[position];
                    selectedQuestionCode = questionCode[position];

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            answerEditText = (AppCompatEditText) v.findViewById(R.id.answer_EditText_security_question);
            answerEditText.setEnabled(true);
//
//
//
//        startServerInteractionApproveUpdateAccount(temp[1]);
//        approveRejectDialog.cancel();
//
//
//    }
////});
            builder.setView(v);
            builder.setCancelable(false);
//        v.findViewById(R.id.approvUpdateAccountButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startServerInteractionApproveUpdateAccount(temp[1]);
//                approveRejectDialog.cancel();
//
//
//            }
//        });
//        builder.setView(v);


            builder.setTitle("Answer Question");
            //builder.setMessage(question);
            builder.setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if (selectedAnswer == null || selectedAnswer.length() == 0 || selectedAnswer.equalsIgnoreCase("****")) {
                        if (answerEditText.getText().toString().trim().length() > 2) {
                            perform_SecurityQuestion_Verify(selectedQuestion, mComponentInfo.getMD5(answerEditText.getText().toString()).toUpperCase(), selectedQuestionCode);
                            securityQuestionDialog.cancel();
                        } else {

                            Toast.makeText(LoginActivity.this, "Answer must be atleast 3 character long", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (answerEditText.getText().toString().trim().length() > 2) {
                            if (mComponentInfo.getMD5(answerEditText.getText().toString()).toUpperCase().equalsIgnoreCase(selectedAnswer)) {
                                startServerInteraction();
                                securityQuestionDialog.cancel();

                            } else {

                                Toast.makeText(LoginActivity.this, "Wrong Answer", Toast.LENGTH_LONG).show();
                            }
                        } else {

                            Toast.makeText(LoginActivity.this, "Answer must be atleast 3 character long", Toast.LENGTH_LONG).show();
                        }
                    }


                }
            });

            builder.setNegativeButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    securityQuestionDialog.cancel();

                }
            });

            securityQuestionDialog = builder.create();
            securityQuestionDialog.show();
        }
    }

    private void startServerInteraction(String apiName, String requestData, int requestNo, String displayMessage) {
        hideKeyboard();
        try {
            if (new InternetCheck().isConnected(LoginActivity.this)) {

                if (mayRequestPhoneState()) {

                    //   requestData = generateLoginData();
                    showProgressDialog(displayMessage);

                    mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

                    vollyRequest(apiName, requestData, requestNo);
                    //   new ServerTask(mComponentInfo, GenerateOTPActivity.this, mHandler, requestData, "getLoginInJSON", 102).start();

                } else {
                    hideProgressDialog();
                    return;
                }
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

                canExitApp = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();

        }
    }


    void vollyRequest(String apiName, final String body, final int requestCode)
    {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,baseUrl+apiName,new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(LoginActivity.this,mComponentInfo,LoginActivity.this,requestCode,response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(LoginActivity.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(LoginActivity.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

        }

    }


    //////////////////////// Security Question ////////////////////////////////////////////////

    String generateBillerData() {

        /*"agentCode":"237100001012","pin":"C09AF4A983FE8B815C9D130B6B04262E",
         "pintype":"MPIN","requestcts":"25/05/2016 18:01:51",
         "vendorcode":"MICR","clienttype":"GPRS","country":"TCH"*/

        String jsonString = "";
        mpin = mComponentInfo.getMD5(mobileNo + mpin).toUpperCase();
        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", mobileNo);
            countryObj.put("pin", mpin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "");
            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }




    private boolean mayRequestPhoneState()

    {

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return true;
            }
            if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED)



            {
                return true;
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    //  requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_SMS_PHONE);
                } else {

                }
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_READ);

            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    //  requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_SMS_PHONE);
                } else {

                }
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);

            }

            else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //  requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_WIFI_PHONE);
                } else {
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
            }





        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    private String generateMasterData() {

        String vpin = "";
        vpin = mComponentInfo.getMD5(mobileNo.substring(3,5) + "MASTERDATA").toUpperCase();

        String jsonString = "";


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", mobileNo);
            countryObj.put("vpin", vpin);
            countryObj.put("data", "ALL");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        requestString = jsonString;
        return jsonString;

    }


    private String generateLoginData() {

        String jsonString = "";

        String mpinNew = "";
        mpinNew = mComponentInfo.getMD5(mobileNo + mpin).toUpperCase();
        SharedPreferences.Editor Eeditor = getSharedPreferences("EU_MPIN", MODE_PRIVATE).edit();
        Eeditor.putString("MPIN", mpinNew);  // 52D66E4FFF4025519DA0AD46993B92D5
        Eeditor.commit();

        //  imeiNo="25825810";
        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", mobileNo);
            countryObj.put("pin", mpinNew);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            if (mComponentInfo.getmSharedPreferences().getBoolean("isPreviousLogin", false)) {

                imeiNo = mComponentInfo.getmSharedPreferences().getString("loggedInIMEI", "");
                wifiMacAddress = mComponentInfo.getmSharedPreferences().getString("loggedInMACAddress", "");

            } else {
                countryObj.put("resetactivation", "true");
                generateSystemInfo();
            }


            countryObj.put("imei", imeiNo);

            String imeiNumberDeviceId="";

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imeiNumberDeviceId = Settings.Secure.getString(
                        LoginActivity.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                countryObj.put("imeino", imeiNumberDeviceId);

            }
            else {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                imeiNumberDeviceId = telephonyManager.getDeviceId();
                countryObj.put("imeino", imeiNumberDeviceId);
            }


            countryObj.put("serialno", wifiMacAddress);
            countryObj.put("terminalid", termilaid);


            jsonString = countryObj.toString();

        }
        catch (Exception e) {

            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();


        }
        requestString = jsonString;
        return jsonString;

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {


            if (validateDetails()) {

                if (mComponentInfo.getmSharedPreferences().getBoolean("isPreviousLogin", false))
                {
                    startServerInteraction();
                    hideKeyboard();
                }
                else {
                    perform_SecurityQuestion_Request();
                }
            }


            return true;
        }
        return false;
    }

    // ############################# Update Apk ####################################

    private void updateVersionRequestCheck_request() {

        if (new InternetCheck().isConnected(LoginActivity.this)) {

            String  requestString = generate_updateVersionRequest();

            //  showProgressDialog(getString(R.string.pleasewait));

            vollyRequest_updateApk("getApkversion", requestString, 2021);

           //    new ServerTask(mComponentInfo, LoginActivity.this, mHandler, requestString, "getApkversion", 2021).start();

        }

        else {
            hideProgressDialog();
            Toast.makeText(LoginActivity.this, getString(R.string.errorlogininternet), Toast.LENGTH_SHORT).show();

        }
    }

    private String generate_updateVersionRequest() {

        String jsonString = "";
        try {

            JSONObject countryObj = new JSONObject();

            //  http://localhost:8080/json/estel/getApkversion


            // Banking  Frenchinse Subscriber Merchant

            countryObj.put("hryversion", "Banking");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    void vollyRequest_updateApk(String apiName, final String body, final int requestCode)
    {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,baseUrl+apiName,new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(LoginActivity.this,mComponentInfo,LoginActivity.this,requestCode,response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();

                            Toast.makeText(LoginActivity.this,getString(R.string.pleaseTryAgainLater)+"\nError code 2021", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(LoginActivity.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

            Toast.makeText(LoginActivity.this,getString(R.string.pleaseTryAgainLater)+"\nError code 2021", Toast.LENGTH_LONG).show();
            finish();
        }

    }
    // ############################################################################################


    private void updateDialogueBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setCancelable(false);
        builder.setTitle(R.string.app_name);


        builder.setMessage(getString(R.string.versionNotAllowed));


        builder.setPositiveButton(getString(R.string.ok2), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                try {
                    //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.expressunion.agent")));

                    hideProgressDialog();


                  //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.expressunion.agent")));

                } catch (android.content.ActivityNotFoundException anfe) {
                 //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.expressunion.agent")));
                }
                LoginActivity.this.finish();
            }
        });

        // hide Cancel Button
     /*   builder.setNegativeButton(getString(R.string.splash_update_installLater), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                successDialog.cancel();


              //  intent = new Intent(LoginActivity.this, LoginActivity.class);
              //  startActivity(intent);
             //   LoginActivity.this.finish();


            }
        });

      */


        successDialog = builder.create();
        successDialog.show();


    }
}

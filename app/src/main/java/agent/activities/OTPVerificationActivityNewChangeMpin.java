package agent.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by AdityaBugalia on 23/06/16.
 */

public class OTPVerificationActivityNewChangeMpin extends AppCompatActivity implements ServerResponseParseCompletedNotifier, AutoCompleteTextView.OnEditorActionListener, View.OnClickListener {


    TextView purposeTxtView, descriptionTextView, resendOTPTextView;
    TextInputLayout otpEntryEditText_TIL, mpinEntryTIL;
    AutoCompleteTextView otpEntryEditText, mpinEntryEditText;
    Button proceedButton;
    String otpString, mpinString, agentcode, process, agentName, imeiNo, wifiMacAddress, accountTypeCodeString;
    String TempOtp = "";  // must be deleted , only for test purpose.
    ComponentMd5SharedPre mComponentInfo;
    ProgressDialog mDialog;
    Toolbar mToolbar;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            SmsMessage smsMessage;
            String msg_from, messageContent;
            if (bundle != null) {

                try {

                    if (Build.VERSION.SDK_INT >= 19) {
                        msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                        smsMessage = msgs[0];
                        msg_from = smsMessage.getDisplayOriginatingAddress();
                        if (msg_from.equalsIgnoreCase("EU TCHAD") ||
                                msg_from.equalsIgnoreCase("EU MOBILE") ||
                                msg_from.equalsIgnoreCase("EU GABON") ||
                                msg_from.equalsIgnoreCase("EU CONGO") ||
                                msg_from.equalsIgnoreCase("EU MOBILE MONEY") ||
                                msg_from.equalsIgnoreCase("EU RCA") ||
                                msg_from.equalsIgnoreCase("EU RDC") ||
                                msg_from.equalsIgnoreCase("8081") ||
                                msg_from.equalsIgnoreCase("8007") ||
                                msg_from.equalsIgnoreCase("8041") ||

                                msg_from.equalsIgnoreCase("+237677117148") ||
                                msg_from.equalsIgnoreCase("+237675291003") ||
                                msg_from.equalsIgnoreCase("+237696630812") ||
                                msg_from.equalsIgnoreCase("+237699426792") ||

                                msg_from.equalsIgnoreCase("237677117148") ||
                                msg_from.equalsIgnoreCase("237675291003") ||
                                msg_from.equalsIgnoreCase("237696630812") ||
                                msg_from.equalsIgnoreCase("237699426792") ||
                                msg_from.equalsIgnoreCase("+918860812472") || // Estel Test Number_ INDIA
                                msg_from.equalsIgnoreCase("+918920527258") || // Sharique  mobile  number
                                msg_from.equalsIgnoreCase("+919718196849") || //Estel Test Number_ INDIA
                                msg_from.equalsIgnoreCase("+919999644234")) { //Estel Test Number_ INDIA

                            try {
                                messageContent = smsMessage.getMessageBody();
                                //   messageContent = "Your One-Time Password:123456 to:45454534, 345445. Please do not share this password with anyone.";
                                //   messageContent = "Votre One-Time Password: 668541 a : 237694140439,9272068.Garder cette information secrete et ne la communiquer a personne";
                                String[] data = messageContent.split("\\:");
                                String aa = data[1].split("\\ ")[0];
                                otpEntryEditText.setText(aa);

                                mpinEntryEditText.requestFocus();
                            } catch (Exception e) {

                            }

                        }


                        Log.d("Exception caught", msg_from);
                    } else {
                        Object pdus[] = (Object[]) bundle.get("pdus");
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                        msg_from = smsMessage.getDisplayOriginatingAddress();
                        messageContent = smsMessage.getMessageBody();
                        if (msg_from.equalsIgnoreCase("EU TCHAD") ||
                                msg_from.equalsIgnoreCase("EU MOBILE") ||
                                msg_from.equalsIgnoreCase("EU GABON") ||
                                msg_from.equalsIgnoreCase("EU CONGO") ||
                                msg_from.equalsIgnoreCase("EU MOBILE MONEY") ||
                                msg_from.equalsIgnoreCase("EU RCA") ||
                                msg_from.equalsIgnoreCase("EU RDC") ||
                                msg_from.equalsIgnoreCase("8081") ||
                                msg_from.equalsIgnoreCase("8007") ||
                                msg_from.equalsIgnoreCase("8041") ||

                                msg_from.equalsIgnoreCase("+237677117148") ||
                                msg_from.equalsIgnoreCase("+237675291003") ||
                                msg_from.equalsIgnoreCase("+237696630812") ||
                                msg_from.equalsIgnoreCase("+237699426792") ||

                                msg_from.equalsIgnoreCase("237677117148") ||
                                msg_from.equalsIgnoreCase("237675291003") ||
                                msg_from.equalsIgnoreCase("237696630812") ||
                                msg_from.equalsIgnoreCase("237699426792") ||
                                msg_from.equalsIgnoreCase("+918860812472") ||
                                msg_from.equalsIgnoreCase("+918920527258") || //Sharique mobile  number
                                msg_from.equalsIgnoreCase("+919718196849") || //Sharique mobile  number
                                msg_from.equalsIgnoreCase("+919999644234")) {

                            try {
                                messageContent = smsMessage.getMessageBody();
                                //  messageContent = "Your One-Time Password:123456 to:45454534, 345445. Please do not share this password with anyone.";
                                String[] data = messageContent.split("\\:");
                                otpEntryEditText.setText(data[1].split("\\ ")[0]);
                                mpinEntryEditText.requestFocus();
                            } catch (Exception e) {

                            }

                        }
                    }


                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }


        }
    };
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {


            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                // showErrorSnackBar("Please check Internet Connection");
                // canExitApp = true;
                Toast.makeText(OTPVerificationActivityNewChangeMpin.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(OTPVerificationActivityNewChangeMpin.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();

            } else {


                DataParserThread thread = new DataParserThread(OTPVerificationActivityNewChangeMpin.this, mComponentInfo, OTPVerificationActivityNewChangeMpin.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resendotpTextView:
                startServerInteractionForResendOTP();
                break;
        }
    }

    private BroadcastReceiver otpRecieverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (mComponentInfo.get_OTP_STRING().trim().length() > 0) {
                otpEntryEditText.setText(mComponentInfo.get_OTP_STRING());

                mComponentInfo.set_OTP_STRING("");

                if (checkDetails()) {
                    startServerInteraction();

                }
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();


//        LocalBroadcastManager.getInstance(this).registerReceiver(otpRecieverLocal,
//                new IntentFilter("RECIEVE_OTP"));
//
//        if(mComponentInfo.get_OTP_STRING().trim().length()>0) {
//            otpEntryEditText.setText(mComponentInfo.get_OTP_STRING());
//
//            mComponentInfo.set_OTP_STRING("");
//
//            if (checkDetails()) {
//                startServerInteraction();
//
//            }
//
//        }


        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);

// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task. If in a background agent.thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...

                Log.e("","Successfully started retriever, expect broadcast intent");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Log.e("","Failed to start retriever, inspect Exception for more details");
            }
        });


        LocalBroadcastManager.getInstance(this).registerReceiver(otpRecieverLocal,
                new IntentFilter("RECIEVE_OTP"));

        if (mComponentInfo.get_OTP_STRING().trim().length() > 0) {
            otpEntryEditText.setText(mComponentInfo.get_OTP_STRING());

            mComponentInfo.set_OTP_STRING("");

            if (checkDetails()) {
                startServerInteraction();
            }
        }
    }

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


        setContentView(R.layout.otp_verification_activity_new_changempin);

        //  registerReceiver(broadcastReceiver, new IntentFilter("SMS_RECIEVED"));

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        purposeTxtView = (TextView) findViewById(R.id.purposeTextViewOtpVerification);
        otpEntryEditText_TIL = (TextInputLayout) findViewById(R.id.otpEntryEditTextOtpVerification_TIL);
        mpinEntryTIL = (TextInputLayout) findViewById(R.id.mpinEntryEditTextOtpVerification_TIL);
        otpEntryEditText = (AutoCompleteTextView) findViewById(R.id.otpEntryEditTextOtpVerification);
        otpEntryEditText.setOnEditorActionListener(this);
        mpinEntryEditText = (AutoCompleteTextView) findViewById(R.id.mpinEntryEditTextOtpVerification);
        mpinEntryEditText.setOnEditorActionListener(this);

        mpinEntryEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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


        proceedButton = (Button) findViewById(R.id.proceedButtonOtpVerification);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextViewOtpVerification);
        resendOTPTextView = (TextView) findViewById(R.id.resendotpTextView);
        resendOTPTextView.setOnClickListener(this);
        resendOTPTextView.setVisibility(View.GONE);
        resendOTPTextView.postDelayed(new Runnable() {
            public void run() {
                resendOTPTextView.setVisibility(View.VISIBLE);
            }
        }, 30000);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (checkDetails()) {
                    startServerInteraction();

                }

            }
        });
        setData();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_Otp_verification);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);

        mToolbar.setTitle(getString(R.string.OTPVerificationNewHeding));
        mToolbar.setSubtitle(agentName);
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
      /*  getSupportActionBar().setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.name, null));
        getSupportActionBar()..setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
            }
        });*/
     //   isPermissionsProvided();
        if (!mComponentInfo.is_IS_HIDE_MPIN_OTP()) {

            mpinEntryTIL.setVisibility(View.VISIBLE);

        } else {

            mpinEntryEditText.setText(mComponentInfo.get_MPIN_OTP_STRING());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  unregisterReceiver(broadcastReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(otpRecieverLocal);
    }


    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(OTPVerificationActivityNewChangeMpin.this);
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

    private void startServerInteractionForResendOTP() {
        if (new InternetCheck().isConnected(OTPVerificationActivityNewChangeMpin.this)) {
            showProgressDialog(getString(R.string.sendingotp));
            new ServerTask(mComponentInfo, OTPVerificationActivityNewChangeMpin.this, mHandler, generateResendOtpData(), "resendOTPInJSON", 117).start();
        } else {
            Toast.makeText(OTPVerificationActivityNewChangeMpin.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
            //canExitApp = true;
        }
    }

    private void startServerInteraction() {
        if (new InternetCheck().isConnected(OTPVerificationActivityNewChangeMpin.this)) {
            showProgressDialog(getString(R.string.PleaseWaitVerifyingOTP));
            new ServerTask(mComponentInfo, OTPVerificationActivityNewChangeMpin.this, mHandler, generateOtpData(), "getOTPInJSON", 103).start();
        } else {
            Toast.makeText(OTPVerificationActivityNewChangeMpin.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
            //canExitApp = true;
        }
    }

    private String generateResendOtpData() {
        String jsonString = "";
        String pin = mComponentInfo.getMD5(agentcode + mpinString).toUpperCase();
        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentcode);
            //  countryObj.put("pin", pin);
            //  countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            countryObj.put("transtype", "CHANGEPIN");
            countryObj.put("accounttype", accountTypeCodeString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            //  countryObj.put("otpCode", otpString);
            jsonString = countryObj.toString();
        } catch (Exception e) {

        }
        return jsonString;
    }

    private String generateOtpData() {
        String jsonString = "";
        String pin = mComponentInfo.getMD5(agentcode + mpinString).toUpperCase();
        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentcode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "CHANGEPIN");
            countryObj.put("accounttype", accountTypeCodeString);
            countryObj.put("otpCode", otpString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();
        } catch (Exception e) {

        }
        return jsonString;
    }

    private void setData() {
        //mComponentInfo.getmSharedPreferences();
        process = mComponentInfo.getmSharedPreferences().getString("process", "");
        accountTypeCodeString = mComponentInfo.getmSharedPreferences().getString("accountTypeCodeString", "MA");
        purposeTxtView.setText(process);
        agentcode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        TempOtp = mComponentInfo.getmSharedPreferences().getString("otp", "");
        //descriptionTextView.setText("This Otp verification is for Subscriber: " + agentcode+"+"+TempOtp);
        descriptionTextView.setText(getString(R.string.PleaseEnterDetailstoProceedFurther));

    }


    private boolean checkDetails() {
        boolean ret = false;
        if (otpEntryEditText != null && mpinEntryEditText != null) {

         //   if (isPermissionsProvided()) {
                //generateSystemInfo();
                otpString = otpEntryEditText.getText().toString().trim();
                mpinString = mpinEntryEditText.getText().toString();
                if (otpString.length() == 6) {
                    otpEntryEditText_TIL.setError(null);

                    if (mpinString.length() == 4) {
                        mpinEntryTIL.setError(null);
                        ret = true;
                    } else {
                        mpinEntryTIL.setError(getString(R.string.prompt_mPin));
                    }


                } else {
                    otpEntryEditText_TIL.setError(getString(R.string.PleaseEnter6DigitOTPCode));
                }

          /*  } else {
                Toast.makeText(OTPVerificationActivityNewChangeMpin.this, getString(R.string.PleaseProvidePermissions), Toast.LENGTH_LONG).show();

            }*/
        } else {


            Toast.makeText(OTPVerificationActivityNewChangeMpin.this, getString(R.string.restartapp), Toast.LENGTH_LONG).show();

        }


        return ret;
    }

    private boolean isPermissionsProvided() {

        boolean ret = false;

        int currentapiVersion = Build.VERSION.SDK_INT;


        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(OTPVerificationActivityNewChangeMpin.this,
                    Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(OTPVerificationActivityNewChangeMpin.this,
                        Manifest.permission.ACCESS_WIFI_STATE)
                        == PackageManager.PERMISSION_GRANTED) {


                    ret = true;

                } else {


                    ActivityCompat.requestPermissions(OTPVerificationActivityNewChangeMpin.this,
                            new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                            156);


                }

            } else {


                ActivityCompat.requestPermissions(OTPVerificationActivityNewChangeMpin.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        155);
            }
        } else {
            ret = true;

        }
        return ret;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;

        switch (item.getItemId()) {
            case android.R.id.home:
                switch (process) {
                    case "LOGIN":
                        i = new Intent(OTPVerificationActivityNewChangeMpin.this, LoginActivity.class);
                        hideProgressDialog();
                        startActivity(i);
                        OTPVerificationActivityNewChangeMpin.this.finish();


                        break;


                }


                break;


        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        if (generalResponseModel.getResponseCode() == 0) {
            switch (requestNo) {
                case 103:

                    setResult(RESULT_OK);
                    OTPVerificationActivityNewChangeMpin.this.finish();


                  /*  switch (process) {
                        case "LOGIN":

                            //updateProgressDialogMessage("Please Wait, Activating user account");

                            break;
                    }*/
                    break;

                case 104:
                    hideProgressDialog();
                    Toast.makeText(OTPVerificationActivityNewChangeMpin.this, getString(R.string.YouHavebeenSuccessfullyActivatedPleaseLogin), Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }

                    Intent i = new Intent(OTPVerificationActivityNewChangeMpin.this, LoginActivity.class);
                    startActivity(i);
                    OTPVerificationActivityNewChangeMpin.this.finish();

                    break;
                case 117:
                    hideProgressDialog();
                    Toast.makeText(this, getString(R.string.otpSentSuccessfully), Toast.LENGTH_LONG).show();
                    break;
            }

        } else {
            hideProgressDialog();

            String resultDescriptionString = mComponentInfo.getResultDescription(generalResponseModel.getResponseCode() + "", mComponentInfo.getmSharedPreferences().getString("languageToUse", "fr")).trim().length() > 0 ?
                    mComponentInfo.getResultDescription(generalResponseModel.getResponseCode() + "", mComponentInfo.getmSharedPreferences().getString("languageToUse", "fr")) + "" : generalResponseModel.getUserDefinedString();

            Toast.makeText(OTPVerificationActivityNewChangeMpin.this, "" + resultDescriptionString, Toast.LENGTH_SHORT).show();


            if (requestNo != 117) {

                String resultDescriptionString2 = mComponentInfo.getResultDescription(generalResponseModel.getResponseCode() + "", mComponentInfo.getmSharedPreferences().getString("languageToUse", "fr")).trim().length() > 0 ?
                        mComponentInfo.getResultDescription(generalResponseModel.getResponseCode() + "", mComponentInfo.getmSharedPreferences().getString("languageToUse", "fr")) + "" : generalResponseModel.getUserDefinedString();
                Toast.makeText(OTPVerificationActivityNewChangeMpin.this, "" + resultDescriptionString2, Toast.LENGTH_SHORT).show();

                setResult(RESULT_CANCELED);
                //   OTPVerificationActivityNew.this.finish();
            }
        }
    }

    /*private void generateSystemInfo() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo wInfo = wifiManager.getConnectionInfo();
        wifiMacAddress = wInfo.getMacAddress();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imeiNo = telephonyManager.getDeviceId();

    }*/

    private String generateActivationData() {
        int shrtCode1 = generateRandomNo();
        int shrtCode2 = generateRandomNo();
        SharedPreferences.Editor editor = mComponentInfo.getmSharedPreferences().edit();
        editor.putInt("shrtCode1", shrtCode1);
        editor.putInt("shrtCode2", shrtCode2);
        editor.putString("imeiNo", imeiNo);
        editor.putString("wifiMacAddress", wifiMacAddress);
        editor.commit();

        String jsonString = "";


        String pin = mComponentInfo.getMD5(agentcode + mpinString).toUpperCase();
        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentcode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("imei", imeiNo);
            countryObj.put("systemId", wifiMacAddress);
            countryObj.put("s1", shrtCode1);
            countryObj.put("s2", shrtCode2);
            countryObj.put("mobileno", agentcode);
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

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {

            OTPVerificationActivityNewChangeMpin.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mDialog != null) {
                        mDialog.setMessage(message);
                    }
                }
            });
        }


    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (checkDetails()) {
                startServerInteraction();
                hideKeyboard();

            }
            return true;
        }
        return false;
    }

  /*  {"agentcode":"237100001012","pin":"C09AF4A983FE8B815C9D130B6B04262E","pintype"
        :"MPIN","requestcts":"25/05/2016
        18:01:51","vendorcode":"MICR","clienttype":"GPRS","transtype":"LOGIN","otpCode
        ":"5665"}*/
}

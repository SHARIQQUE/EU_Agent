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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class ChangeMpin extends AppCompatActivity implements View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {
    Intent i;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, agentCode;
    Toolbar mToolbar;
    private EditText currentMpinEditText, newMpinEditText, confirmMpinEditText;
    private Button proceed_ChangeMpin_Button;
    private String currentMpin = "", newMpin = "", confirmMpin = "";
    private ProgressDialog mDialog;
private Dialog successDialog;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());

            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(ChangeMpin.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(ChangeMpin.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(ChangeMpin.this, mComponentInfo, ChangeMpin.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };
    private boolean isServerOperationInProcess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


        setContentView(R.layout.change_mpin);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();


        mToolbar = (Toolbar) findViewById(R.id.tool_bar_Changempin);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.changempin_new));
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
        currentMpinEditText = (EditText) findViewById(R.id.currentMpin_EditText_ChangeMpin);
        newMpinEditText = (EditText) findViewById(R.id.newMpin_EditText_ChangeMpin);
        confirmMpinEditText = (EditText) findViewById(R.id.confirmmpin_EditText_ChangeMpin);
        confirmMpinEditText.setOnEditorActionListener(this);

        proceed_ChangeMpin_Button = (Button) findViewById(R.id.proceed_changempin_button);
        proceed_ChangeMpin_Button.setOnClickListener(this);


        currentMpinEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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

        newMpinEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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

        confirmMpinEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
                validateDetails();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            ChangeMpin.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceed_changempin_button:
                validateDetails();
                break;
        }
    }

    public void validateDetails() {

        if (!valiateCurrentMpin()) {
            return;
        }
        if (!validateNewMpin()) {
            return;
        }
        if (!validateConfirmMpin()) {
            return;
        }
        if (!validateConfirmPinMatch()) {
            return;
        }
        if (!validatebothPinMatch()) {
            return;
        }
        proceedChangeMpin();
        // startServerTask("CHANGEPIN", 116);
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(ChangeMpin.this);
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

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(ChangeMpin.this,mComponentInfo,ChangeMpin.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(ChangeMpin.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(ChangeMpin.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    private void proceedChangeMpin() {

        if (new InternetCheck().isConnected(ChangeMpin.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generatePaymentrequestData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;


          //  callApi("changePinJson",requestData,118);

            new ServerTask(mComponentInfo, ChangeMpin.this, mHandler, requestData, "changePinJson", 118).start();


        } else {
            Toast.makeText(ChangeMpin.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    private String generatePaymentrequestData() {
        String jsonString = "";


        String pin = mComponentInfo.getMD5(agentCode + currentMpin).toUpperCase();
        String newPin = mComponentInfo.getMD5(agentCode + confirmMpin).toUpperCase();
        try {

           /* {"agentcode":"237699325872","pin":"012C9A8E1E89FA2AAFFF18E54CF0CCC8","pintype":"I
                PIN","requestcts":"25/05/2016
                18:01:51","vendorcode":"MICR","clienttype":"GPRS","newpin":"012C9A8E1E89FA2AAFFF18
                E54CF0CCC8","source":"237699325872"}*/



            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("newpin", newPin);
            countryObj.put("source", agentCode);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please Try again later", Toast.LENGTH_SHORT).show();
        }


        return jsonString;
    }


    public boolean valiateCurrentMpin() {

        if (currentMpinEditText.getText().toString().trim().length() == 4) {
            currentMpin = currentMpinEditText.getText().toString().trim();
            return true;
        } else {
            currentMpinEditText.setError(getString(R.string.prompt_mPin));
            requestFocus(currentMpinEditText);
            return false;
        }


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public boolean validateNewMpin() {


        if (newMpinEditText.getText().toString().trim().length() == 4) {
            newMpin = newMpinEditText.getText().toString().trim();
            return true;
        } else {
            newMpinEditText.setError(getString(R.string.pleaseEnterNewmpin));
            requestFocus(newMpinEditText);
            return false;
        }

    }

    public boolean validateConfirmMpin() {

        if (confirmMpinEditText.getText().toString().trim().length() == 4) {
            confirmMpin = confirmMpinEditText.getText().toString().trim();
            return true;
        } else {
            confirmMpinEditText.setError(getString(R.string.pleaseEnterConfirmPpin));
            requestFocus(confirmMpinEditText);
            return false;
        }

    }

    public boolean validateConfirmPinMatch() {

        if (newMpin.toString().equals(confirmMpin)) {
            return true;
        } else {
            Toast.makeText(ChangeMpin.this, getString(R.string.pinmismatch), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public boolean validatebothPinMatch() {

        if (confirmMpin.toString().equals(currentMpin)) {
            Toast.makeText(ChangeMpin.this, getString(R.string.newoldsameerror), Toast.LENGTH_LONG).show();
            return false;
        } else {
            requestFocus(newMpinEditText);
            return true;
        }


    }
    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMpin.this);

        builder.setCancelable(false);
        builder.setTitle(R.string.changempin);

        builder.setMessage(getString(R.string.ChangeMpinSucessReceipt));//+"\n"+"\n"+data);

        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                Intent intent = new Intent(ChangeMpin.this, LoginActivity.class);
                startActivity(intent);
                ChangeMpin.this.finish();
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

            if (requestNo == 118) {


                String[] resultDec = generalResponseModel.getUserDefinedString().split("\\|");
                // resultDec[1]="";

                if (resultDec[0].equalsIgnoreCase("Transaction Successful")) {

                    if (resultDec[1].equalsIgnoreCase("true")) {  //  if   "otpflag": "true",

                        Intent i = new Intent(ChangeMpin.this, OTPVerificationActivityNewChangeMpin.class);
                        startActivityForResult(i, 299);

                    } else {
                        showSuccess(generalResponseModel.getUserDefinedString(), 1);
                    }
                }

            }
        } else {
            hideProgressDialog();
            Toast.makeText(ChangeMpin.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }


    }
}

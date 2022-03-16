package agent.transaction_cancel;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import agent.activities.OTPVerificationActivity;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptTransactionCancelReceipt;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by AdityaBugalia on 31/08/16.
 */
public class TransactionCancel extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

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

               /* if (validateDetails_Part_1()) {
                    validateDetails_Part_2();
                }*/

                if (CommonUtilities.isTxnApprovalPhase1) {
                    //   validateDetails_Part_1();
                    if (validateDetails_Part_1()) {
                        pictureSignatureServerRequest();
                    }
                } else {
                    if (validateDetails_Part_2()) {
                        proceedTransactionCancel();
                    }
                }

                //  validateDetails_Part_1();

                   /* Discuss */


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public AutoCompleteTextView cancelNumberEditText, mpinEditText;
    Spinner spinner_OperationType, spinner_AccountType;
    Button nextButton_approval, submit_button;
    Toolbar mToolbar;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;

    ComponentMd5SharedPre mComponentInfo;
    boolean isBillPay = false;
    ListView statementLsitView;
    ProgressDialog mDialog;
    ImageView imageViewPicture, imageViewSign;
    Dialog successDialog;
    String agentName, agentCode, operationCodeString, operationNameString, approvalNoString, mpinString, accountTypeCodeString, billerCodeString, destinationString = "";
    private String[] operationCodeArray, billerArray, accountCodeArray, accountNameArray;
    TextView operationTypeTextView, approvalNumberTextView, accountTypeTextView;
    int iLevel = 99;
    boolean isReview;
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

        setContentView(R.layout.transaction_cancel);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_approval);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.transactionCancel));
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
        cancelNumberEditText = (AutoCompleteTextView) findViewById(R.id.approvalNo_EditText_Tariff);

        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_Tariff);
        mpinEditText.setOnEditorActionListener(this);
        // Looper.prepare();

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



        input_SV_AccToCash = (ScrollView) findViewById(R.id.input_SV_AccToCash);
        review_SV_AccToCash = (ScrollView) findViewById(R.id.review_SV_AccToCash);
        cancelNumberEditText.setOnEditorActionListener(this);

        operationTypeTextView = (TextView) findViewById(R.id.operationTypeTextView);
        approvalNumberTextView = (TextView) findViewById(R.id.approvalNumberTextView);
        accountTypeTextView = (TextView) findViewById(R.id.accountTypeTextView);

        spinner_OperationType = (Spinner) findViewById(R.id.spinner_OperationType);
        spinner_AccountType = (Spinner) findViewById(R.id.spinner_AccountType);

        nextButton_approval = (Button) findViewById(R.id.nextButton_approval);
        nextButton_approval.setOnClickListener(this);

        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(this);

        cancelNumberEditText.setOnEditorActionListener(this);
        //  input_LL = (LinearLayout) findViewById(R.id.input_Recipient_LL_Tariff);
        //  review_LL = (LinearLayout) findViewById(R.id.review_LL_Tariff);
        billerArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        imageViewSign = (ImageView) findViewById(R.id.imageViewSign);
        // operationCodeArray = getResources().getStringArray(R.array.TxnTypeTransactionCancel);

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

        spinner_OperationType.setOnItemSelectedListener(this);
    }

    private boolean validateDetails_Part_1() {
        boolean ret = false;

        //  if (spinner_OperationType.getSelectedItemPosition() > 0) {

        // if (spinner_AccountType.getSelectedItemPosition() > 0) {

        approvalNoString = cancelNumberEditText.getText().toString().trim();
        if (approvalNoString.length() >= 8) {
            mpinString = mpinEditText.getText().toString().trim();
            // if (mpinString.length() == 4) {

            //      operationCodeString = operationCodeArray[spinner_OperationType.getSelectedItemPosition()];
            //       operationNameString = spinner_OperationType.getSelectedItem().toString();
            //   accountTypeCodeString = accountCodeArray[spinner_AccountType.getSelectedItemPosition()];
            CommonUtilities.isTxnApprovalPhase1 = false;
            ret = true;

                   /* } else {
                        mpinEditText.requestFocus();
                        Toast.makeText(TransactionCancel.this, getString(R.string.pleaseEnterMpin), Toast.LENGTH_SHORT).show();
                    }*/
        } else {
            cancelNumberEditText.requestFocus();
            Toast.makeText(TransactionCancel.this, getString(R.string.pleaseEnterCancelNumber), Toast.LENGTH_SHORT).show();
        }
      /*  } else {
            spinner_AccountType.requestFocus();
            Toast.makeText(TransactionCancel.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
        }*/
       /* } else {
            Toast.makeText(TransactionCancel.this, getString(R.string.PleaseSelectOperationType), Toast.LENGTH_SHORT).show();
        }*/
        return ret;
    }


    private void showAccToCashReview() {

        TransactionCancel.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();


                //  titleTextView.setText(getString(R.string.pleasereviewdetail));
                input_SV_AccToCash.setVisibility(View.GONE);
                review_SV_AccToCash.setVisibility(View.VISIBLE);

                operationTypeTextView.setText(operationNameString);
                accountTypeTextView.setText(accountTypeCodeString);
                approvalNumberTextView.setText(approvalNoString);

                // payerAccountTypeTxtView_Review.setText(spinnerAccountToDebitString);
                //  nextButton_MoneyTransfer.setText(getString(R.string.submit));
                isReview = true;

                mpinEditText.requestFocus();
                submit_button.setVisibility(View.VISIBLE);
                nextButton_approval.setVisibility(View.GONE);

                //proceedTransactionCancel();


            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_approval:
                //   CommonUtilities.isTxnApprovalPhase1 = true;
                if (validateDetails_Part_1()) {
                    pictureSignatureServerRequest();
                }

                break;
            case R.id.submit_button:
                //  CommonUtilities.isTxnApprovalPhase1 = false;
                if (validateDetails_Part_2()) {
                    proceedTransactionCancel();
                }
                break;
        }
    }

    private boolean validateDetails_Part_2() {
        boolean ret = false;

        //  if (payerAccountsSpinner.getSelectedItemPosition() > 0) {

        mpinString = mpinEditText.getText().toString();
        if (mpinString.trim().length() == 4) {

            CommonUtilities.isTxnApprovalPhase1 = true;
            ret = true;

        } else {
            Toast.makeText(TransactionCancel.this, getString(R.string.prompt_mPin), Toast.LENGTH_SHORT).show();
        }
    /*    } else {
            Toast.makeText(AccountBalance.this, getString(R.string.pleaseselectbankaccount), Toast.LENGTH_SHORT).show();
        }*/

        return ret;

    }


    public void pictureSignatureServerRequest() {
        showProgressDialog(getString(R.string.pleasewait));
        String requestData = generatePictureSign();

      //  callApi("getViewProfileInJSON",requestData,148);

        new ServerTask(mComponentInfo, TransactionCancel.this, mHandler, requestData, "getViewProfileInJSON", 148).start();
    }

    private String generatePictureSign() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

           /* {"agentCode":"237000271016","pin":"46A75E89BC40FDD2F70895AB710D5A22",
            "source":"237000271016","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS"}
              */
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", approvalNoString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    void proceedTransactionCancel() {
        if (new InternetCheck().isConnected(TransactionCancel.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateCancelData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

          //  callApi("getReversalTransactionInJSON",requestData,127);

            new ServerTask(mComponentInfo, TransactionCancel.this, mHandler, requestData, "getReversalTransactionInJSON", 127).start();
        } else {
            Toast.makeText(TransactionCancel.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateCancelData() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

         /*   {"agentCode":"237000271010","pin":"D3F8D3568D92CA73E4D5A1671529F075","comments":"OK","pintype":
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
            //  countryObj.put("transtype", operationCodeString);

            countryObj.put("transrefno", approvalNoString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(TransactionCancel.this);
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
                Toast.makeText(TransactionCancel.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(TransactionCancel.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(TransactionCancel.this, mComponentInfo, TransactionCancel.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };

    private void showSuccessReceipt(String data) {


        System.out.print(data);


        String[] recieptData = null;
        Bundle bundle = new Bundle();
        bundle.putString("data", data);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("transType", operationCodeString).commit();

        if (data.trim().length() > 0) {
            recieptData = data.split("\\|");
             System.out.print(recieptData);
        } else {

        }
        String transtype=recieptData[13];
        //String txnType = CommonUtilities.txnType;

        switch (transtype) {
            case "CASHTOM":
                startActivity(new Intent(this, SucessReceiptTransactionCancelReceipt.class));
                break;

            case "BILLPAY":
                startActivity(new Intent(this, SucessReceiptTransactionCancelReceipt.class));
                break;

        }
              TransactionCancel.this.finish();
    }
    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionCancel.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.transactionCancel));
        //    String[] temp= data.split("\\|");
        //   builder.setMessage(String.format(getString(R.string.tariffResponseSuccess),operationNameString,temp[0], temp[1]) );


        builder.setMessage(getString(R.string.TransactionCancelSucessReceipt));// + " \n " + " \n " + data);
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
                TransactionCancel.this.finish();
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

                    if (new InternetCheck().isConnected(TransactionCancel.this)) {
                        showProgressDialog(getString(R.string.pleasewait));

                    //    callApi("getReversalTransactionInJSON",requestData,127);

                        new ServerTask(mComponentInfo, TransactionCancel.this, mHandler, requestData, "getReversalTransactionInJSON", 127).start();
                    } else {
                        Toast.makeText(TransactionCancel.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }

                } else {
                }
                break;
        }
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        if (generalResponseModel.getResponseCode() == 0 || requestNo == 148) {

            if (requestNo == 127) {
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "TARIFF").commit();
                    Intent i = new Intent(TransactionCancel.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccessReceipt(generalResponseModel.getUserDefinedString());
                }
            } else if (requestNo == 148) {


                String responseData = generalResponseModel.getUserDefinedString();

                String[] responseArray = responseData.split("\\|");

                if (responseArray[0].matches("pictureSign")) {


                    byte[] imageBytes1 = Base64.decode(responseArray[1], Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                    imageViewSign.setImageBitmap(decodedImage);

                    byte[] imageBytes2 = Base64.decode(responseArray[2], Base64.DEFAULT);
                    Bitmap decodedImage2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);
                    imageViewPicture.setImageBitmap(decodedImage2);

                  /*  if (getStreamFromResponse(responseArray[1], responseArray[2]) != null) {
                        getByteByString(text);
                        setImage();
                        //  showAccToCashReview();
                    } else {
                        //Toast.makeText(this, "Image Data is not in proper format", Toast.LENGTH_SHORT).show();
                    }

                    if (getByteByStringSign(text[1]) != null) {
                        setImageSign();
                        //   showAccToCashReview();
                    } else {
                        //Toast.makeText(this, "Sign Data is not in proper format", Toast.LENGTH_SHORT).show();
                    }*/


                    showAccToCashReview();
                } else {
                    showAccToCashReview();
                }
            }

        } else {
            hideProgressDialog();
            //  showSuccess(generalResponseModel.getUserDefinedString(), 1);   // test
            Toast.makeText(TransactionCancel.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }

    String[] text = new String[2];
    public static byte[] resultByteArrayImage;
    public static byte[] resultByteArraySign;
    public static byte[][] bytesArray = new byte[2][2];
    InputStream inputImage, inputSign;

    public String[] getStreamFromResponse(String imageBinary, String signBinary) {
        try {
            inputImage = new ByteArrayInputStream(imageBinary.getBytes());
            inputSign = new ByteArrayInputStream(signBinary.getBytes());

            int sizeImageInput = inputImage.available();
            int sizeSignInput = inputSign.available();

            byte[] bufferImage = new byte[sizeImageInput];
            byte[] bufferSign = new byte[sizeSignInput];

            inputImage.read(bufferImage);
            inputImage.close();

            inputSign.read(bufferSign);
            inputSign.close();

            // byte buffer into a string
            text[0] = new String(bufferImage);
            text[1] = new String(bufferSign);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }

    public static byte[] getByteByStringSign(String binaryStringArrayImageSign) {
        int splitSize = 8;
        int index = 0;
        int position = 0;
        int i = 0;
        if (binaryStringArrayImageSign.length() % splitSize == 0) {
            // resultByteArraySign = new byte[binaryStringArrayImageSign[1].length() / splitSize];
            resultByteArraySign = new byte[binaryStringArrayImageSign.length() / splitSize];
            StringBuilder text = new StringBuilder(binaryStringArrayImageSign);
            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArraySign[position] = byteAsInt.byteValue();
                index += splitSize;
                position++;
            }
            return resultByteArraySign;
        } else {
            System.out.println("Cannot convert binary string to byte[], because of the input length. '" + binaryStringArrayImageSign + "' % 8 != 0");
            return null;
        }

    }
    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(TransactionCancel.this,mComponentInfo, TransactionCancel.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(TransactionCancel.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(TransactionCancel.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }

    public static byte[] getByteByString(String[] binaryStringArrayImageSign) {
        int splitSize = 8;
        int index = 0;
        int position = 0;
        int i = 0;

        if (binaryStringArrayImageSign[0].length() % splitSize == 0) {
            // resultByteArrayImage = new byte[binaryStringArrayImageSign[0].length() / splitSize];
            resultByteArrayImage = new byte[binaryStringArrayImageSign[0].length() / splitSize];
            StringBuilder text = new StringBuilder(binaryStringArrayImageSign[0]);

            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArrayImage[position] = byteAsInt.byteValue();
                index += splitSize;
                position++;
            }
            return resultByteArrayImage;
        } else {
            System.out.println("Cannot convert binary string to byte[], because of the input length. '" + binaryStringArrayImageSign[0] + "' % 8 != 0");
            return null;
        }


    }

    Bitmap bmpImage, bmpSign;

    public void setImage() {
        try {
            bmpImage = BitmapFactory.decodeByteArray(resultByteArrayImage, 0, resultByteArrayImage.length);
            imageViewPicture.setImageBitmap(bmpImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageSign() {
        try {
            bmpSign = BitmapFactory.decodeByteArray(resultByteArraySign, 0, resultByteArraySign.length);
            imageViewSign.setImageBitmap(bmpSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            TransactionCancel.this.finish();
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



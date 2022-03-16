package agent.transaction_approval;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
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

import adapter.CountryFlagAdapter;
import adapter.OrderTransferApprovalrBaseAdapter;
import agent.activities.R;
import callback.CallbackFromAdapterOrderApproval;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptTransferOrderApproval;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by Sahrique on 14/03/17.
 */


public class OrderTransferApproval extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener, CallbackFromAdapterOrderApproval, AdapterView.OnItemClickListener {
    ListView listView;
    String OrderForm_listviewItem,OrderFormName_listviewItem,OrderTo_listviewItem,OrderToNeme_listviewItem,amount_listviewItem,tramnsactionid_listviewItem;
    ArrayList accountsArrayDetailsList;
    String mpinString;

    TableRow tableRow;
    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    Toolbar mToolbar;
    LinearLayout linearLayoutListview;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, agentCode, spinnerCountryString, transferBasisString, mobileNumberString, amountString, spinnerAccountToDebitString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    Button confirmButton, cancelButton;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner spinnerCountry, spinnerAccountToDebit, transferBasisSpinner;
    private ScrollView input_SV_AccToCash, scrollview_secondpage;
    private AutoCompleteTextView mpinEditText,
            MobileNo_EditText;
    private TextView agentCodeTextView, transactionReferenceNoProofReviewPage, transferBasisTxtView_Review, titleTextView, payerAccountTypeTxtView_Review;
    private ProgressDialog mDialog;
    TextView orderFrom_textview_secondpage, orderFromName_textview_secondpage, orderto_textview_secondpage, ordertoName_textview_secondpage, amount_textView_secondpage, transId_textview_secondpage;


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());

            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();

            } else {
                DataParserThread thread = new DataParserThread(OrderTransferApproval.this, mComponentInfo, OrderTransferApproval.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;

    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    Context context;

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


        setContentView(R.layout.ordertransfer_approval);

        // OtherAccount.this.getSystemService(Context.CONNECTIVITY_SERVICE);


        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        transferTagArray = getResources().getStringArray(R.array.TransferTag);

        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_otherAccount);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

       // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.orderTransferApproval_capital));
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
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {

            OrderTransferApproval.this.finish();
        }


        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);


        scrollview_secondpage = (ScrollView) findViewById(R.id.scrollview_secondpage);

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




        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);

        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(OrderTransferApproval.this);


        orderFrom_textview_secondpage = (TextView) findViewById(R.id.orderFrom_textview_secondpage);
        orderFromName_textview_secondpage = (TextView) findViewById(R.id.orderFromName_textview_secondpage);
        orderto_textview_secondpage = (TextView) findViewById(R.id.orderto_textview_secondpage);
        ordertoName_textview_secondpage = (TextView) findViewById(R.id.ordertoName_textview_secondpage);
        amount_textView_secondpage = (TextView) findViewById(R.id.amount_textView_secondpage);
        transId_textview_secondpage = (TextView) findViewById(R.id.transId_textview_secondpage);


        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);
        MobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.MobileNo_EditText);
        MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 0) {
            String[] data = bankAccounts.split("\\;");

            ArrayList<String> accountList = new ArrayList<String>();
            ArrayList<String> accountCodeList = new ArrayList<String>();
            accountList.add(getString(R.string.accounttodebit));
            accountCodeList.add(getString(R.string.accounttodebit));

            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    if (tData[4].equalsIgnoreCase("MA")) {
                        accountList.add(tData[0] + " - " + tData[1]);
                        accountCodeList.add(tData[4]);
                    }
                }
            }

            payerBankAccountsArray = accountList.toArray(new String[accountList.size()]);

            payerAccountCodeArray = accountCodeList.toArray(new String[accountCodeList.size()]);

        } else {
            payerBankAccountsArray = new String[1];
            payerBankAccountsArray[0] = getString(R.string.accounttodebit);
            payerAccountCodeArray = new String[1];
            payerAccountCodeArray[0] = getString(R.string.accounttodebit);
        }

        spinnerAccountToDebit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));

        titleTextView = (TextView) findViewById(R.id.titleTextView_AccToCash);
        agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);


        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);


        linearLayoutListview = (LinearLayout) findViewById(R.id.linearLayoutListview);
        transactionReferenceNoProofReviewPage = (TextView) findViewById(R.id.transactionReferenceNoProofReviewPage);


        agentCodeTextView.setText(getString(R.string.agentCode) + agentCode);
        MobileNo_EditText.setOnEditorActionListener(this);

        getOrderTransferApprovalDetails();

    }


    private String generatePictureSign() {
        String jsonString = "";


        try {

           /* {"agentCode":"237000271016","pin":"46A75E89BC40FDD2F70895AB710D5A22",
            "source":"237000271016","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS"}
              */

            SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
            mpinString = prefs.getString("MPIN", null);
            System.out.print(mpinString);


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", mpinString);
            countryObj.put("source", mobileNumberString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                MobileNo_EditText.setText("");
                MobileNo_EditText.setHint(getString(R.string.PleasEenterMobileNumber));
                // MobileNo_EditText.setFilters(null);
                MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] digitsfilters = new InputFilter[2];
                digitsfilters[0] = new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {
                        if (end > start) {

                            char[] acceptedChars = new char[]{'0', '1', '2', '3', '4', '5',
                                    '6', '7', '8', '9'};

                            for (int index = start; index < end; index++) {
                                if (new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                                    return source;
                                } else {
                                    return "";
                                }
                            }
                        }
                        return null;
                    }


                };
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]));
                MobileNo_EditText.setHint(String.format(getString(R.string.hintMobileNo), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                MobileNo_EditText.setFilters(digitsfilters);
                MobileNo_EditText.setText("");


            } else if (i == 2) {
                MobileNo_EditText.setText("");
                InputFilter[] digitsfilters = new InputFilter[2];
                digitsfilters[0] = new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {
                        if (end > start) {

                            char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f',
                                    'g', 'h', 'i', 'j', 'k', 'l',
                                    'm', 'n', 'o', 'p', 'q', 'r',
                                    's', 't', 'u', 'v', 'w', 'x',
                                    'y', 'z', 'A', 'B', 'C', 'D',
                                    'E', 'F', 'G', 'H', 'I', 'J',
                                    'K', 'L', 'M', 'N', 'O', 'P',
                                    'Q', 'R', 'S', 'T', 'U', 'V',
                                    'W', 'X', 'Y', 'Z', '.'};

                            for (int index = start; index < end; index++) {
                                if (new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                                    return source;
                                } else {
                                    return "";
                                }
                            }
                        }
                        return null;
                    }


                };


                digitsfilters[1] = new InputFilter.LengthFilter(16);
                MobileNo_EditText.setHint(getString(R.string.pleaseentername));
                MobileNo_EditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                MobileNo_EditText.setFilters(digitsfilters);
                MobileNo_EditText.setText("");
            }
        } else {
            Toast.makeText(OrderTransferApproval.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:
                setInputType(i);
                break;
            case R.id.spinner_PayerAccountTypeSelection_AccToAcc:

                break;
            case R.id.spinnerCountry:
                setInputType(transferBasisSpinner.getSelectedItemPosition());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void OtherAccountReview(final String strData) {
        OrderTransferApproval.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();

                //  pictureSignatureServerRequest();

                titleTextView.setText(getString(R.string.pleasereviewdetail));
                input_SV_AccToCash.setVisibility(View.GONE);
                scrollview_secondpage.setVisibility(View.VISIBLE);
                transferBasisTxtView_Review.setText(transferBasisString);


                String[] data = strData.split("\\|");


                //  confirmButton.setText(getString(R.string.transfernow));
                isReview = true;
                //confirmButton.setVisibility(View.GONE);

                //  listView.setVisibility(View.VISIBLE);


            }
        });

    }

    private boolean ValidationDetails() {
        boolean ret = false;



                mpinString = mpinEditText.getText().toString();
                if (mpinString.trim().length() == 4) {


                    ret = true;


                } else {
                    Toast.makeText(OrderTransferApproval.this, getString(R.string.mpinAccountBalance), Toast.LENGTH_SHORT).show();

                }
        return ret;
    }

    public void validation() {
        if (ValidationDetails()) {

                 OrderApprovalFinal();
        }
    }

    void OrderApprovalFinal() {

        if (new InternetCheck().isConnected(OrderTransferApproval.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateApproverdOrder();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

         //   callApi("approveorderwallettrfrProcess",requestData,184);

           new ServerTask(mComponentInfo, OrderTransferApproval.this, mHandler, requestData, "approveorderwallettrfrProcess", 184).start();

        } else {
            Toast.makeText(OrderTransferApproval.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(OrderTransferApproval.this,mComponentInfo, OrderTransferApproval.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(OrderTransferApproval.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(OrderTransferApproval.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.confirmButton:
                validation();
                break;

            case R.id.cancelButton:

                scrollview_secondpage.setVisibility(View.GONE);
                linearLayoutListview.setVisibility(View.VISIBLE);

                break;

        }
    }


    private void getOrderTransferApprovalDetails() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(OrderTransferApproval.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = getOrderTransferApprovalDetailsGenerate();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

         //   callApi("getOrderwalletTfrDetailsInJSON",requestData,183);


              new ServerTask(mComponentInfo, OrderTransferApproval.this, mHandler, requestData, "getOrderwalletTfrDetailsInJSON", 183).start();


        } else {
            Toast.makeText(OrderTransferApproval.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private String generateAccountDetails() {
        String strAccountData = "";

        try {
            String[] data = spinnerAccountToDebitString.split("\\-");

            http:
            //192.168.0.224:9090/RESTfulWebServiceEU/json/estel/
            // accountsDetails?agentcode=237000271011&mobileno=237000271015

            strAccountData = "accountsDetails?agentcode=" + agentCode + "&mobileno=" + mobileNumberString;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAccountData;
    }


    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderTransferApproval.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.otherAccount));

        String[] temp = data.split("\\|");

        builder.setMessage(getString(R.string.ActiverSucessReceipt) + getString(R.string.transactionId) + temp[0]);

        // builder.setMessage(getString(R.string.OtherAccountSucessReceipt) + " \n " +" \n "+ data);
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                OrderTransferApproval.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }


    private String getOrderTransferApprovalDetailsGenerate() {
        String jsonString = "";

        try {


            SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
            mpinString = prefs.getString("MPIN", null);
            System.out.print(mpinString);


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", mpinString);
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

    private String generateApproverdOrder() {
        String jsonString = "";

        try {

           /*
             {
                  "agentcode": "237000270001",
                  "pin": "457DF80ECFCD61103D4AF2D800961B57",
                  "source": "237000270001",
                  "vendorcode": "MICR",
                  "amount": "950",
                  "rtransid": "6429441",
                  "destination": "237000215002",
                  "pintype": "MPIN",
                  "clienttype": "SELFCARE"
                }
            */

          /*  String OrderForm_listviewItem=data[0];
            String OrderFormName_listviewItem=data[1];
            String OrderTo_listviewItem=data[2];
            String OrderToNeme_listviewItem=data[3];
            String amount_listviewItem=data[4];
            String tramnsactionid_listviewItem=data[5];

         */
            String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", OrderTo_listviewItem);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("amount", amount_listviewItem);
            countryObj.put("rtransid", tramnsactionid_listviewItem);
            countryObj.put("destination", OrderForm_listviewItem);
            countryObj.put("pintype", "MPIN");
            countryObj.put("clienttype", "GPRS");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 184) {
                hideProgressDialog();
                showSuccessReceipt(generalResponseModel.getUserDefinedString());
            }


            else if (requestNo == 183) {


                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");

                accountsArrayDetailsList = generalResponseModel.getCustomResponseList();
                System.out.print(accountsArrayDetailsList);
                String strData = generalResponseModel.getUserDefinedString();

                System.out.print(strData);

                listView = (ListView) findViewById(R.id.listview);


                   /* accountsArrayDetailsList.add("237000271501|shipra Ag1|Mobile Account1|MA1|Y1|Active");
                    accountsArrayDetailsList.add("237000271502|shipra Ag2|Mobile Account2|MA2|Y2|NoActive");
                    accountsArrayDetailsList.add("237000271503|shipra Ag3|Mobile Account3|MA3|Y3|Active3");
                    accountsArrayDetailsList.add("237000271501|shipra Ag1|Mobile Account1|MA1|Y1|Active");
                    accountsArrayDetailsList.add("237000271502|shipra Ag2|Mobile Account2|MA2|Y2|NoActive");
                    accountsArrayDetailsList.add("237000271503|shipra Ag3|Mobile Account3|MA3|Y3|Active3");
*/

                if (accountsArrayDetailsList != null) {
                    if (accountsArrayDetailsList.size() > 0) {

                        OrderTransferApprovalrBaseAdapter adapter = new OrderTransferApprovalrBaseAdapter(OrderTransferApproval.this, accountsArrayDetailsList);
                        listView.setOnItemClickListener(this);
                        listView.setAdapter(adapter);

                    } else {

                        Toast.makeText(OrderTransferApproval.this, getString(R.string.norecordfound), Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(OrderTransferApproval.this, getString(R.string.norecordfound), Toast.LENGTH_SHORT).show();


                }


                // OtherAccountReview(strData);
            }


        } else {
            hideProgressDialog();
            Toast.makeText(OrderTransferApproval.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }

    private void showSuccessReceipt(String data) {


        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        //  bundle.putString("labelNameString", labelNameString);
        bundle.putString("OrderForm_listviewItem", OrderForm_listviewItem);
        bundle.putString("OrderTo_listviewItem", OrderTo_listviewItem);
        bundle.putString("amount_listviewItem", amount_listviewItem);
        bundle.putString("countrySelectionString", countrySelectionString);


        Intent intent = new Intent(OrderTransferApproval.this, SucessReceiptTransferOrderApproval.class);

        intent.putExtra("data", data);
        intent.putExtra("OrderForm_listviewItem", OrderForm_listviewItem);
        intent.putExtra("OrderTo_listviewItem", OrderTo_listviewItem);
        intent.putExtra("amount_listviewItem", amount_listviewItem);
        intent.putExtra("countrySelectionString", countrySelectionString);


        startActivity(intent);
        OrderTransferApproval.this.finish();
    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            OrderTransferApproval.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(OrderTransferApproval.this);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            OrderTransferApproval.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private int getCountrySelection() {
        int ret = 0;
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                ret = i;
            }
        }
        return ret;
    }

    private boolean validateAmount(String input) {
        boolean ret = false;

        try {
            int amt = Integer.parseInt(input);
            if (amt > 0) {
                ret = true;
                amountString = "" + amt;

            }

        } catch (Exception e)

        {

        }

        return ret;


    }

    @Override
    public void callbaAdapterOrderApproval(String data0, String data1, String data2, String data3, String data4, String data5) {


        linearLayoutListview.setVisibility(View.GONE);
        scrollview_secondpage.setVisibility(View.VISIBLE);


        OrderForm_listviewItem=data0;
        OrderFormName_listviewItem=data1;
        OrderTo_listviewItem=data2;
        OrderToNeme_listviewItem=data3;
        amount_listviewItem=data4;
        tramnsactionid_listviewItem=data5;

        orderFrom_textview_secondpage.setText(OrderForm_listviewItem);
        orderFromName_textview_secondpage.setText(OrderFormName_listviewItem);
        orderto_textview_secondpage.setText(OrderTo_listviewItem);
        ordertoName_textview_secondpage.setText(OrderToNeme_listviewItem);
        amount_textView_secondpage.setText(amount_listviewItem);
        transId_textview_secondpage.setText(tramnsactionid_listviewItem);

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] data;

        data = accountsArrayDetailsList.get(position).toString().split("\\|");

        showPreConfirmationPopup(data);


    }


    private void showPreConfirmationPopup(final String[] data) {

        TextView orderForm_textview, transactionId_textview, amount_textview, ordertoName_textview, orderFromName_textview, actionTxtView, orderto_textview;


        AlertDialog.Builder builder = new AlertDialog.Builder(OrderTransferApproval.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.pendingOrder));
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.listview_item_ordertrasfer_approval, null);


        orderForm_textview = (TextView) convertView.findViewById(R.id.orderForm_textview);
        ordertoName_textview = (TextView) convertView.findViewById(R.id.ordertoName_textview);
        orderto_textview = (TextView) convertView.findViewById(R.id.orderto_textview);
        orderFromName_textview = (TextView) convertView.findViewById(R.id.orderFromName_textview);
        amount_textview = (TextView) convertView.findViewById(R.id.amount_textview);
        transactionId_textview = (TextView) convertView.findViewById(R.id.transactionId_textview);



         OrderForm_listviewItem=data[0];
         OrderFormName_listviewItem=data[1];
         OrderTo_listviewItem=data[2];
         OrderToNeme_listviewItem=data[3];
         amount_listviewItem=data[4];
         tramnsactionid_listviewItem=data[5];

        orderForm_textview.setText(OrderForm_listviewItem);
        orderFromName_textview.setText(OrderFormName_listviewItem);
        orderto_textview.setText(OrderTo_listviewItem);
        ordertoName_textview.setText(OrderToNeme_listviewItem);
        amount_textview.setText(amount_listviewItem);
        transactionId_textview.setText(tramnsactionid_listviewItem);



        builder.setView(convertView);
        builder.setPositiveButton(getString(R.string.next), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                OrderTransferApproval.this.callbaAdapterOrderApproval(data[0], data[1], data[2], data[3], data[4], data[5]);
            }
        });

        builder.setNegativeButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();

            }
        });
        successDialog = builder.create();
        successDialog.show();
    }
}

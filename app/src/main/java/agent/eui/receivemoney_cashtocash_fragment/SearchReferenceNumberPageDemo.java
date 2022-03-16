package agent.eui.receivemoney_cashtocash_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static android.content.Context.MODE_PRIVATE;
import static agent.thread.ServerTask.baseUrl;

public class SearchReferenceNumberPageDemo extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    ModalReceiveMoney modalReceiveMoney = new ModalReceiveMoney();


    View view;
    Button search_button_new;
    String[] threshHolderAmount_array, transactionType_name_array, transactionType_code_array, countryCodeArray;
    Toolbar mToolbar;
    EditText editext_transactionReferenceNumber_imoney, editext_transactionReferenceNumber_receiveCash, editext_recipientName_imoney, edittext_mobileNumber_imoney;
    Spinner spinner_transactionType;
    ComponentMd5SharedPre mComponentInfo;
    String idDocumentTypeSpinnerString, transactionreferenceNoTypeString, transactionReferenceNumberString_receiveCash, receipientMobileNumberString_imoney, recipientNameString_imoney, agentName, agentCode, transactionReferenceNumberString_imoney, senderMobileNoString, countrySelectionString = "";
    boolean isServerOperationInProcess;
    private ScrollView scrollview_first_page;
    private String[] countryArray, currencyArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, payerAccountCodeArray;
    private ProgressDialog mDialog;

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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SearchReferenceNumberPageDemo.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mComponentInfo = (ComponentMd5SharedPre) getActivity().getApplicationContext();

        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;


        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());


        view = inflater.inflate(R.layout.receivemoneycashtocash_transactiontype_fragment, container, false); // Inflate the layout for this fragment


        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // currencySenderSelectionString = mComponentInfo.ge223tmSharedPreferences().getString("currency", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

     /*   mToolbar = (Toolbar) view.findViewById(R.id.tool_bar_remmetanceSendMoneyToMobile);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.sendMoneyNewCashtoCash));
        mToolbar.setSubtitle("" + agentName);
        //  mToolbar.setSubtitle(agentcode);
        // mToolbar.setSubtitle("Please provide details to proceed further");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        try {


            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //  setSupportActionBar(mToolbar);
            //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        } catch (Exception e) {

            Log.e("", "" + e.toString());
        }*/


        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList_EUI", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList_EUI", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList_EUI", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength_EUI", "").split("\\|");
            currencyArray = mComponentInfo.getmSharedPreferences().getString("currencyList_EUI", "").split("\\|");
            threshHolderAmount_array = mComponentInfo.getmSharedPreferences().getString("thresholderamount_EUI", "").split("\\|");

        } catch (Exception e) {

            getActivity().finish();
        }

        search_button_new = (Button) view.findViewById(R.id.search_button_new);
        search_button_new.setOnClickListener(this);


        scrollview_first_page = (ScrollView) view.findViewById(R.id.scrollview_first_page);


        editext_transactionReferenceNumber_imoney = (EditText) view.findViewById(R.id.editext_transactionReferenceNumber_imoney);
        editext_transactionReferenceNumber_imoney.setOnEditorActionListener(this);

        editext_transactionReferenceNumber_receiveCash = (EditText) view.findViewById(R.id.editext_transactionReferenceNumber_receiveCash);
        editext_transactionReferenceNumber_receiveCash.setOnEditorActionListener(this);


        editext_recipientName_imoney = (EditText) view.findViewById(R.id.editext_recipientName_imoney);
        editext_recipientName_imoney.setOnEditorActionListener(this);


        edittext_mobileNumber_imoney = (EditText) view.findViewById(R.id.edittext_mobileNumber_imoney);
        edittext_mobileNumber_imoney.setOnEditorActionListener(this);


        spinner_transactionType = (Spinner) view.findViewById(R.id.spinner_transactionType);

        transactionType_name_array = getResources().getStringArray(R.array.partnerTransactionType);
        transactionType_code_array = getResources().getStringArray(R.array.partnerTransactionType_code);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, transactionType_name_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_transactionType.setAdapter(adapter);
        spinner_transactionType.setOnItemSelectedListener(this);


        mComponentInfo.setArrowBackButton_receiveCash(1);



        return view;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

                validationDetails();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void request_get_searchInt() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_searchInt();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


            //   vollyRequest_get_searchInt("searchInt", requestData, 222);

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "searchInt", 222).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    void vollyRequest_get_searchInt(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                          //  Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                         //   Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SearchReferenceNumberPageDemo.this, requestCode, response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
                            getActivity().finish();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

        }

    }

    private String generateJson_searchInt() {

        String jsonString = "";

        SharedPreferences prefs = getActivity().getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "25/05/2016 18:01:51");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            countryObj.put("referencenumber", transactionReferenceNumberString_receiveCash);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


          /*  if(!transactionReferenceNumberString_imoney.equalsIgnoreCase(""))
            {
                countryObj.put("referencenumber", transactionReferenceNumberString_imoney);
            }
            else if(!receipientMobileNumberString_imoney.equalsIgnoreCase(""))
            {
                countryObj.put("referencenumber", receipientMobileNumberString_imoney);
            }
            else
            {
                countryObj.put("referencenumber", recipientNameString_imoney);
            }*/

            countryObj.put("comments", "TEST");


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button_new:
                validationDetails();
                break;
        }
    }


    void validationDetails() {
        if (validateSendMoneyToMobile_PartI()) {


            if (!transactionReferenceNumberString_receiveCash.equalsIgnoreCase("")) {
                request_get_searchInt();
            } else {

                Intent intent = new Intent(getActivity(), ImoneySeacrhReceiveCash.class);   // Imoney receive Call
                startActivity(intent);
            }

           /* if (!transactionReferenceNumberString_receiveCash.equalsIgnoreCase(""))
            {
                request_get_searchInt();
            }

            else if (!receipientMobileNumberString_imoney.equalsIgnoreCase(""))
            {

                Intent intent=new Intent(getActivity(),CashToCashReceiveMoneySameCountryNew.class);
                startActivity(intent);
            }


            else if (!receipientMobileNumberString_imoney.equalsIgnoreCase(""))
            {

                Intent intent=new Intent(getActivity(),CashToCashReceiveMoneySameCountryNew.class);
                startActivity(intent);
            }

            else if (!recipientNameString_imoney.equalsIgnoreCase(""))
            {

                Intent intent=new Intent(getActivity(),CashToCashReceiveMoneySameCountryNew.class);
                startActivity(intent);
            }*/


        }
    }

    private boolean validateSendMoneyToMobile_PartI() {
        boolean ret = false;

        transactionReferenceNumberString_receiveCash = editext_transactionReferenceNumber_receiveCash.getText().toString().trim();

        transactionReferenceNumberString_imoney = editext_transactionReferenceNumber_imoney.getText().toString().trim();
        receipientMobileNumberString_imoney = edittext_mobileNumber_imoney.getText().toString().trim();
        recipientNameString_imoney = editext_recipientName_imoney.getText().toString().trim();


        if (spinner_transactionType.getSelectedItemPosition() != 0 || spinner_transactionType.getSelectedItemPosition() != 1) {


            if (transactionReferenceNumberString_receiveCash.length() > 3 || transactionReferenceNumberString_imoney.length() > 3 || receipientMobileNumberString_imoney.length() > 3 || recipientNameString_imoney.length() > 3) {


                if (!transactionReferenceNumberString_receiveCash.equalsIgnoreCase("")) {
                    modalReceiveMoney.setReferenceNumber(transactionReferenceNumberString_receiveCash);
                } else if (!transactionReferenceNumberString_imoney.equalsIgnoreCase("")) {
                    modalReceiveMoney.setSearchBy_referenceNo_imoney(transactionReferenceNumberString_imoney);
                } else if (!receipientMobileNumberString_imoney.equalsIgnoreCase("")) {
                    modalReceiveMoney.setSearchBy_mobile_imoney(receipientMobileNumberString_imoney);
                } else if (!recipientNameString_imoney.equalsIgnoreCase("")) {
                    modalReceiveMoney.setSearchBy_name_imoney(recipientNameString_imoney);
                }


                ret = true;


            } else {
                Toast.makeText(getActivity(), getString(R.string.reference_number_cashtocash), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.PleaseSelectTransactionType), Toast.LENGTH_LONG).show();
        }


        return ret;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {


            case R.id.spinner_transactionType:

                String str = spinner_transactionType.getSelectedItem().toString();
                String test = transactionType_code_array[i];

                //  if (i > 0)

                if (i == 0) {

                    editext_transactionReferenceNumber_imoney.setVisibility(View.VISIBLE);
                    editext_recipientName_imoney.setVisibility(View.VISIBLE);
                    edittext_mobileNumber_imoney.setVisibility(View.VISIBLE);

                    editext_transactionReferenceNumber_receiveCash.setVisibility(View.GONE);


                    transactionReferenceNumberString_imoney = "";   // not remove validation
                    receipientMobileNumberString_imoney = "";        // not remove validation
                    recipientNameString_imoney = "";                // not remove validation


                } else if (i == 1) {
                    editext_transactionReferenceNumber_receiveCash.setVisibility(View.VISIBLE);

                    editext_transactionReferenceNumber_imoney.setVisibility(View.GONE);
                    editext_recipientName_imoney.setVisibility(View.GONE);
                    edittext_mobileNumber_imoney.setVisibility(View.GONE);

                    transactionReferenceNumberString_receiveCash = "";  // not remove validation


                }

                break;


        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(getActivity());
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


    private String getThresholdAmount(String countryCode) {

        String thresholdAmt = "0.0";


        for (int i = 0; i < countryCodeArray.length; i++) {

            if (countryCodeArray[i].equalsIgnoreCase(countryCode)) {

                thresholdAmt = threshHolderAmount_array[i];
            }
        }

        modalReceiveMoney.setThresHolderAmount(thresholdAmt);

        return thresholdAmt;
    }

    String prefixNo = "";




    private String getSenderCountryName(String countryCode) {

        String countryname = "", countryPrefix = "", countryMobileLength = "";


        for (int i = 0; i < countryCodeArray.length; i++) {

            if (countryCodeArray[i].equalsIgnoreCase(countryCode)) {

                countryname = countryArray[i];
                countryMobileLength = countryMobileNoLengthArray[i];
                countryPrefix = countryPrefixArray[i];


            }
        }

        modalReceiveMoney.setSenderCountryName(countryname);

        return countryname;
    }
    private String getCountryDestinationName(String countryCode) {

        String countryname = "", countryPrefix = "", countryMobileLength = "";


        for (int i = 0; i < countryCodeArray.length; i++) {

            if (countryCodeArray[i].equalsIgnoreCase(countryCode)) {

                countryname = countryArray[i];
                countryMobileLength = countryMobileNoLengthArray[i];
                countryPrefix = countryPrefixArray[i];


            }
        }

        modalReceiveMoney.setDestinationCountry_name(countryname);

        return countryname;
    }


    private int getCountryDestinationLength(String countryCode,String destinationMobileNumber) {

        String countryname = "", countryPrefix = "", countryMobileLength = "";

        int length = 0;
        for (int i = 0; i < countryCodeArray.length; i++) {

            if (countryCodeArray[i].equalsIgnoreCase(countryCode)) {

                countryCode = countryArray[i];
                countryMobileLength = countryMobileNoLengthArray[i];
                countryPrefix = countryPrefixArray[i];

                length = countryPrefix.length() + Integer.parseInt(countryMobileLength);

            }
        }

      modalReceiveMoney.setCountrylenght(length);


           return length;
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {


        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();

            if (requestNo == 222) {

                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");

                    modalReceiveMoney.setChangeRate(responseArray[1]);
                    modalReceiveMoney.setEmailIdSender(responseArray[2]);
                    modalReceiveMoney.setEmailIdReceiver(responseArray[28]);
                    modalReceiveMoney.setExcahangeRate(responseArray[7]);

                    modalReceiveMoney.setDestinationCountry_code(responseArray[9]);

                    getCountryDestinationName(responseArray[9]);


                    getCountryDestinationLength(responseArray[9],responseArray[14]);


                    getThresholdAmount(responseArray[9]);

                    modalReceiveMoney.setDateOfBirthSender(responseArray[6]);
                    modalReceiveMoney.setIdProofNumber(responseArray[5]);
                    modalReceiveMoney.setQuestion(responseArray[13]);
                    modalReceiveMoney.setAnswer(responseArray[21]);
                    modalReceiveMoney.setAmountSent(responseArray[17]);
                    modalReceiveMoney.setAmountSentNew(responseArray[18]);
                    modalReceiveMoney.setAmountToPay(responseArray[18]);

                    modalReceiveMoney.setCountrySender(responseArray[23]);
                    getSenderCountryName(responseArray[23]);


                    // modalReceiveMoney.setCountryOfDestination(responseArray[29]);
                    modalReceiveMoney.setReceiverAddress(responseArray[24]);
                    modalReceiveMoney.setSenderAddress(responseArray[25]);
                    modalReceiveMoney.setSenderFirstName(responseArray[26]);
                    modalReceiveMoney.setSenderLastName(responseArray[27]);
                    modalReceiveMoney.setDestinationFirstName(responseArray[10]);
                    modalReceiveMoney.setDestinationLastName(responseArray[3]);
                    modalReceiveMoney.setDateOfBirthDestination(responseArray[11]);
                    modalReceiveMoney.setReceiverAddress(responseArray[20]);
                    modalReceiveMoney.setIdProofType(responseArray[12]);



                    if (responseArray[31].equalsIgnoreCase(null) || responseArray[31].equalsIgnoreCase("null") || responseArray[31].equalsIgnoreCase("")) {
                        modalReceiveMoney.setFees("0");
                    } else {
                        modalReceiveMoney.setFees(responseArray[31]);
                    }

                    modalReceiveMoney.setVat("0");  // untill hardcode   tag is not api


                    NumberFormat df = DecimalFormat.getInstance(Locale.ENGLISH);
                    df.setMinimumFractionDigits(2);
                    df.setGroupingUsed(false);
                    df.setMaximumFractionDigits(2);
                    df.setRoundingMode(RoundingMode.DOWN);
//                    Double fees_Frm_server=Double.parseDouble(responseArray[31]);
//                    Double vat_Frm_server=Double.parseDouble(responseArray[35]);
//                    vat_Frm_server=vat_Frm_server/100*fees_Frm_server;
//                    fees_Frm_server=fees_Frm_server-vat_Frm_server;
//
//                    modalReceiveMoney.setDisplayVAT(df.format(vat_Frm_server)+"");
//                    modalReceiveMoney.setDisplayFees(df.format(fees_Frm_server)+"");
//


                    modalReceiveMoney.setSenderMobileNumber(responseArray[30]);       //      Sender  Mobile Number      //   senderphone tag
                    modalReceiveMoney.setDestinationMobileNumber(responseArray[14]);  //     Destination Mobile Number  //  beneficiaryphone   tag

                    modalReceiveMoney.setCurrencySender(responseArray[32]);    //  responseArray[32]   32 set currency Destination  ???? cehck server tag
                    modalReceiveMoney.setCurrencyDestination(responseArray[33]);  // responseArray[33]   33 set currency Destination  ???? cehck server tag
                    modalReceiveMoney.setReasonOfTheTransfer(responseArray[34]);  // responseArray[33]   33 reason of trasfer cehck server tag


                    if (responseArray[13] == null || responseArray[13].equalsIgnoreCase("") || responseArray[13].equalsIgnoreCase("null"))   // question is null then not display question page as documnet
                    {
                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_sender_Receipt_transfer").replace(R.id.frameLayout_cashtocash_receivemoney, new ReceiveMoneySenderDetailsRecipientDetailTransferDetailsFragment()).commit();
                    } else {
                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_question").replace(R.id.frameLayout_cashtocash_receivemoney, new QuestionAnswerReceiveMoneyFragment()).commit();
                    }


                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
                }
            }


        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

            transactionReferenceNumberString_receiveCash = "";   // not remove validation
            transactionReferenceNumberString_imoney = "";   // not remove validation
            receipientMobileNumberString_imoney = "";        // not remove validation
            recipientNameString_imoney = "";                // not remove validation

        }


    }

}


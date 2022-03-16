package agent.eui.sendmoney_cashtocash_fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import adapter.CountryFlagAdapterIDDocumnetType;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static android.content.Context.MODE_PRIVATE;

public class SenderDetailPartOneFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    AutoCompleteTextView edittext_iddocument;
    View view;
    Button nextButton, dateOfBirth_calender_button;
    String[] idDocumnetType_code_array, idDocumnetType_name_array, countryCodeArray;
    Toolbar mToolbar;
    String  languageToUse="",idProof_validation_string = "", idProof_code, year_temp, month_temp, day_temp, senderMobileNumberRegisterCheck = "";

    EditText idDocumentPlaceOfissue_textview, firstName_textview, name_textview, id_documentNumber_textview;
    static EditText id_document_dateofissue_textview;

    AutoCompleteTextView senderMobileNumber_autoCompletetextview;
    Spinner spinner_id_document_type, spinner_id_document_countryOfIssue;
    ComponentMd5SharedPre mComponentInfo;
    String profession_string, residence_string, gender_Title_string, address1_String, address2_String, nationality_string, fix_homePhoneNumber_string, city_string, emailString_sender, idProof_name, idDocumentCountryIssueSpinnerString, senderMobileNumberString, firstNameString, nameString, idDocumnetNoString, agentName, agentCode, idDocumentPlaceOfissue_String, countrySelectionString;
    boolean isServerOperationInProcess;
    ArrayList<String> list_idDocumnettype_fromServer;
    ModalSendMoney modalSendMoney = new ModalSendMoney();
    private ScrollView scrollview_first_page;
    private String[] countryArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, payerAccountCodeArray;
    private ProgressDialog mDialog;
    static String idDocumnetDateOfissueString = "";
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SenderDetailPartOneFragment.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mComponentInfo = (ComponentMd5SharedPre) getActivity().getApplicationContext();

        languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;


        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());


        view = inflater.inflate(R.layout.senderdetail_part_one_fragment, container, false); // Inflate the layout for this fragment


        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // currencySenderSelectionString = mComponentInfo.getmSharedPreferences().getString("currency", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        mComponentInfo.setArrowBackButton_sendCash(3);

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

        } catch (Exception e) {

            getActivity().finish();
        }

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        dateOfBirth_calender_button = (Button) view.findViewById(R.id.dateOfBirth_calender_button);
        dateOfBirth_calender_button.setOnClickListener(this);

        scrollview_first_page = (ScrollView) view.findViewById(R.id.scrollview_first_page);


        idDocumentPlaceOfissue_textview = (EditText) view.findViewById(R.id.idDocumentPlaceOfissue_textview);
        idDocumentPlaceOfissue_textview.setOnEditorActionListener(this);

        senderMobileNumber_autoCompletetextview = (AutoCompleteTextView) view.findViewById(R.id.senderMobileNumber_autoCompletetextview);
        firstName_textview = (EditText) view.findViewById(R.id.firstName_textview);
        name_textview = (EditText) view.findViewById(R.id.name_textview);
        id_documentNumber_textview = (EditText) view.findViewById(R.id.id_documentNumber_textview);
        id_document_dateofissue_textview = (EditText) view.findViewById(R.id.id_document_dateofissue_textview);

        edittext_iddocument = (AutoCompleteTextView) view.findViewById(R.id.edittext_iddocument);
        edittext_iddocument.setOnClickListener(this);


        // firstName_textview.setText("Kokam");
        // name_textview.setText("Ghislain");
        // id_documentNumber_textview.setText("UEAF023458895");
        // idDocumentPlaceOfissue_textview.setText("Gabon");


        spinner_id_document_type = (Spinner) view.findViewById(R.id.spinner_id_document_type);
        spinner_id_document_type.setOnItemSelectedListener(this);


        spinner_id_document_countryOfIssue = (Spinner) view.findViewById(R.id.spinner_id_document_countryOfIssue);
        CountryFlagAdapterIDDocumnetType adapter = new CountryFlagAdapterIDDocumnetType("Id proof date of issue", countryArray, getResources(), getLayoutInflater());
        spinner_id_document_countryOfIssue.setAdapter(adapter);

        // spinner_id_document_countryOfIssue.setSelection(getCountrySelection());
        spinner_id_document_countryOfIssue.requestFocus();
        spinner_id_document_countryOfIssue.setOnItemSelectedListener(this);
        spinner_id_document_countryOfIssue.setSelection(getCountrySelection());



        idproofRequest();


        return view;
    }


    private void idproofRequest() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generate_idproof();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            if(languageToUse.equalsIgnoreCase("en"))
            {
                new IdprooftypeRequestUrlEnglish(mComponentInfo, getActivity(), mHandler, requestData, "typePieces", 229).start();
            }
            else {
                new IdprooftypeRequestUrlFrench(mComponentInfo, getActivity(), mHandler, requestData, "typePieces", 229).start();
            }



        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private void AgentIdentity() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentity();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;


            //  new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getAgentIdentity", 220).start();

            vollyRequestApi_serverTask("getAgentIdentity", requestData, 220);


        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void vollyRequestApi_serverTask(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServerTask.baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SenderDetailPartOneFragment.this, requestCode, response.toString());
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
            getActivity().finish();

        }

    }

    private String generate_idproof() {
        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }

    private String generateDataAgentIdentity() {
        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();

            //  countryCodePrefixString = getCountryPrefixCode();
            //   accountNumber = getCountryPrefixCode() + accountNumber;

            countryObj.put("agentCode", modalSendMoney.getSenderMobileNumber());            // verify check account number
            countryObj.put("transtype", "SENDCASHINT");
            countryObj.put("isotp", "Y");

            String vpin = mComponentInfo.getMD5(agentCode.substring(3, 5) + "GETAGENTIDENTITY").toUpperCase();

            countryObj.put("vpin", vpin);
            countryObj.put("initiatorAgent", agentCode);   // agent Code Login
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
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

            case R.id.nextButton:


                validationDetails();

                break;

            case R.id.dateOfBirth_calender_button:


                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

                break;

            case R.id.edittext_iddocument:


                break;
        }
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {

            // Toast.makeText(getActivity(), +month+"/"+day+"/"+year), Toast.LENGTH_LONG).show();
            idDocumnetDateOfissueString = "" + month + "/" + day + "/" + year;
            id_document_dateofissue_textview.setText(" " + day + "/" + month + "/" + year);

        }

    }


    void validationDetails() {

        if (validateSendMoneyToMobile_PartI()) {


            ModalFragmentManage modalFragmentManage = new ModalFragmentManage();
            modalFragmentManage.setFragment_for_sender("forthFragment");

            modalSendMoney.setIdDocumnetType_code(idProof_code);

            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_four").replace(R.id.frameLayout_cashtocash, new SenderDetailPartSecondFragment()).commit();
        }

    }

    boolean validationFirstName()
    {
        boolean ret=false;

        if(senderMobileNumberRegisterCheck.equalsIgnoreCase("register"))
        {
            ret=true;
        }
        else {

            if (firstNameString.length() > 2) {

                ret = true;
            }

            else {
                ret = false;
            }

        }


        return ret;
    }


    private boolean validateSendMoneyToMobile_PartI() {
        boolean ret = false;


        firstNameString = firstName_textview.getText().toString();

        // senderMobileNumber_autoCompletetextview.setText(modalSendMoney.getSenderMobileNumber());
        senderMobileNumberString = senderMobileNumber_autoCompletetextview.getText().toString();


        if (validationFirstName()) {

            nameString = name_textview.getText().toString();
            if (nameString.length() > 2) {

                if (idDocumentValidationDetails(idProof_validation_string)) {

                    idDocumnetNoString = id_documentNumber_textview.getText().toString();
                    if (idDocumnetNoString.length() > 3) {

                        idDocumnetDateOfissueString = id_document_dateofissue_textview.getText().toString();

                        if (idDocumnetDateOfissueString.length() > 3) {

                            if (spinner_id_document_countryOfIssue.getSelectedItemPosition() != 0) {

                                idDocumentPlaceOfissue_String = idDocumentPlaceOfissue_textview.getText().toString().trim();
                                if (idDocumentPlaceOfissue_String.length() > 3) {

                                    modalSendMoney.setIdDocumentNumber(idDocumnetNoString);
                                    modalSendMoney.setIdProofPlaceOfIssue(idDocumentPlaceOfissue_String);

                                   /* if (issueDateFromServer.equalsIgnoreCase("")) {
                                       modalSendMoney.setIdDocumnetDateOfIssue(idDocumnetDateOfissueString);
                                    }
                                    else {
                                        modalSendMoney.setIdDocumnetDateOfIssue(issueDateFromServer);
                                    }*/

                                    modalSendMoney.setIdDocumnetDateOfIssue(idDocumnetDateOfissueString);

                                    modalSendMoney.setIdDocumentCountryOfIssue(idDocumentCountryIssueSpinnerString);
                                    modalSendMoney.setCity(city_string);
                                    modalSendMoney.setAddress1(address1_String);
                                    modalSendMoney.setAddress2(address2_String);
                                    modalSendMoney.setFirstNameSender(firstNameString);
                                    modalSendMoney.setNameSender(nameString);
                                    modalSendMoney.setEmailSender(emailString_sender);


                                  /* if(senderMobileNumberString.equalsIgnoreCase(""))
                                    {
                                        modalSendMoney.setSenderMobileNumber(modalSendMoney.getSenderMobileNumber());
                                    }
                                    else {
                                        modalSendMoney.setSenderMobileNumber(modalSendMoney.getSenderMobileNoCodePrefix()+senderMobileNumberString);
                                    }*/


                                    ret = true;

                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.id_document_placeofissue_cashtocash), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.id_documentCountryOfissue_cashtocash), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.id_documentDateofissue_cashtocash), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), getString(R.string.id_documnetnumber_cashtocash), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.type_de_document_id), Toast.LENGTH_LONG).show();
                }


            } else {
                Toast.makeText(getActivity(), getString(R.string.name_cashtocash), Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.first_name), Toast.LENGTH_LONG).show();

        }


        return ret;
    }

    public boolean idDocumentValidationDetails(String idProof_validation_string) {

        if (idProof_validation_string.equalsIgnoreCase("")) {

            return true;
        }

        else if (idProof_validation_string.equalsIgnoreCase("idProof_not_found")) {

            Toast.makeText(getActivity(), getString(R.string.type_de_document_id), Toast.LENGTH_LONG).show();

            return false;
        }

        else if (spinner_id_document_type.getSelectedItemPosition() == 0) {

            Toast.makeText(getActivity(), getString(R.string.type_de_document_id), Toast.LENGTH_LONG).show();

            return false;
        }


        return true;
    }

    /*



   if (emailAddress.equalsIgnoreCase("")) {
            return true;
        }
        else if (!matcher.find()) {
            return false;
        }

        return true;

   */


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_id_document_type:

                idProof_name = spinner_id_document_type.getSelectedItem().toString();
                modalSendMoney.setIdDocumnetType(idProof_name);

                idProof_code = idDocumnetType_code_array[i];


                // validation Condition   // ID document type     // Type de document id

                if(idProof_name.equalsIgnoreCase("ID document type")||idProof_name.equalsIgnoreCase("Type de document id"))
                {
                    idProof_validation_string = "idProof_not_found";  // fixed not change for validation
                }

                else {
                    idProof_validation_string = "";   // fixed not change for validation
                }





                /*idDocumnetType_name_array = getResources().getStringArray(R.array.idDocumnetType_name_array);
                idDocumnetType_code_array = getResources().getStringArray(R.array.idDocumnetType_code_array);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, idDocumnetType_name_array);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_id_document_type.setAdapter(adapter);*/


                break;

            case R.id.spinner_id_document_countryOfIssue:

                idDocumentCountryIssueSpinnerString = spinner_id_document_countryOfIssue.getSelectedItem().toString();

                String idDocumentCountryOfIssue_code = countryCodeArray[i];
                modalSendMoney.setIdDocumentCountryOfIssue_code(idDocumentCountryOfIssue_code);



               /* setInputType(transferBasisSpinner.getSelectedItemPosition());


                if (i == 0) {
                    System.out.println("0 index");
                } else {
                    request_getcurrency();

                }
               */
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
        try {

            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(message);
            mDialog.setCancelable(false);
            mDialog.setIndeterminate(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressDialog() {
        try {

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();

            if (requestNo == 220) {

                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");


                    senderMobileNumber_autoCompletetextview.setText(modalSendMoney.getSenderMobileNumber());

                    // senderMobileNumber_autoCompletetextview.setText(".");
                    // senderMobileNumber_autoCompletetextview.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });

                    firstName_textview.setText(".");       //  firstNameString firtname tag  is not add agent identity
                    nameString = responseArray[7];

                    /*if (firstNameString_sender != null && firstNameString_sender.length() > 1) {
                        if (!(firstNameString_sender.charAt(0) + "").equalsIgnoreCase(" ")) {
                            if (!firstNameString_sender.contains(" ")) {
                                name_textview.setText(firstNameString_sender);
                            }
                        }
                    }*/

                    name_textview.setText(nameString);

                    modalSendMoney.setFirstName_sender_agentidentity(firstNameString);

                    idDocumnetNoString = responseArray[9];
                    id_documentNumber_textview.setText(idDocumnetNoString);

                    try {

                        String idDocumentIssueFromServer = responseArray[10];           // 2017-05-04 00:00:00.0  // Server response 17 October 2019


                        if(idDocumentIssueFromServer.equalsIgnoreCase(""))
                        {
                            id_document_dateofissue_textview.setText(idDocumentIssueFromServer);
                        }

                        else {

                            String[] tempIdproof_server = idDocumentIssueFromServer.split("\\ ");
                            String[] tempIdproof_temp = tempIdproof_server[0].split("\\-");

                            day_temp = tempIdproof_temp[2];
                            month_temp = tempIdproof_temp[1];
                            year_temp = tempIdproof_temp[0];

                            idDocumnetDateOfissueString = day_temp + "/" + month_temp + "/" + year_temp;
                            id_document_dateofissue_textview.setText(idDocumnetDateOfissueString);
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "" + "id Issue Date format is change From Backend  Plz try again later", Toast.LENGTH_SHORT).show();
                    }

                    idDocumentPlaceOfissue_String = responseArray[18];
                    idDocumentPlaceOfissue_textview.setText(idDocumentPlaceOfissue_String);

                    city_string = responseArray[23];



                    fix_homePhoneNumber_string = responseArray[11];    // 237111111111 coming from server 21 October 2019

                    if(fix_homePhoneNumber_string.equalsIgnoreCase(""))
                    {
                        modalSendMoney.setFixHomePhoneNumber(fix_homePhoneNumber_string);
                    }
                    else {
                        fix_homePhoneNumber_string=fix_homePhoneNumber_string.substring(3);   //  111111111
                        modalSendMoney.setFixHomePhoneNumber(fix_homePhoneNumber_string);
                    }

                    address1_String = responseArray[15];
                    address2_String = responseArray[24];

                    senderMobileNumberRegisterCheck = "register";

                    mComponentInfo.setSenderMobileNumberRegisterCheck(senderMobileNumberRegisterCheck);


                    mComponentInfo.setProfessionAgentIdentity(responseArray[1]);
                    mComponentInfo.setNationalityAgentIdentity(responseArray[13]);
                    mComponentInfo.setCityAgentIdentity(city_string);


                    idProof_code = responseArray[12];
                    // idProof_code = "DRVLIC";
                    idProof_validation_string = "";  // fixed not change for validation


                    if(idProof_code.equalsIgnoreCase(""))
                    {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, idDocumnetType_name_array);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_id_document_type.setAdapter(adapter);
                    }

                    else {

                        for (int i = 0; i < idDocumnetType_code_array.length; i++) {
                            if (idDocumnetType_code_array[i].equalsIgnoreCase(idProof_code)) {
                                idProof_code = idDocumnetType_code_array[i];
                                idProof_name = idDocumnetType_name_array[i];
                            }

                            else {

                                System.out.println("idProffNameFromServer");   // if  not match from Current List Id DOCUMENT
                                /// // id_documentNumber_textview.setText("");       //
                                /// // id_document_dateofissue_textview.setText("");
                                //  idDocumentPlaceOfissue_textview.setText("");
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, idDocumnetType_name_array);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_id_document_type.setAdapter(adapter);


                        if (idProof_name != null) {
                            int spinnerPosition = adapter.getPosition(idProof_name);
                            spinner_id_document_type.setSelection(spinnerPosition);
                        }
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            } else if (requestNo == 229) {

                try {

                    System.out.println("Success iD Proof");

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\;");

                    idDocumnetType_name_array = responseArray[0].split("\\|");
                    idDocumnetType_code_array = responseArray[1].split("\\|");

                    String idDocumnetType_name_array_temp = getString(R.string.type_de_document_id);
                    idDocumnetType_name_array[0] = idDocumnetType_name_array_temp;


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
                }


                //  spinner_id_document_type.setSelection(1);

                AgentIdentity();

            }


        } else {

            hideProgressDialog();
            senderMobileNumber_autoCompletetextview.setText(modalSendMoney.getSenderMobileNumber());
            // firstName_textview.setText(".");
            senderMobileNumberRegisterCheck = "notRegister";
            mComponentInfo.setSenderMobileNumberRegisterCheck(senderMobileNumberRegisterCheck);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, idDocumnetType_name_array);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_id_document_type.setAdapter(adapter);
            idProof_validation_string = "idProof_not_found";  // fixed not change for validation

            //    {"agentcode":"","resultdescription":"Subscriber/Agent detail Not Found","responsects":"15/10/2019 09:32:50 AM","resultcode":"122"}
            //    Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
    }
}


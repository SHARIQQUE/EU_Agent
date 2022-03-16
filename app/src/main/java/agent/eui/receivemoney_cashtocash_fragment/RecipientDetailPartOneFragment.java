package agent.eui.receivemoney_cashtocash_fragment;

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
import agent.eui.sendmoney_cashtocash_fragment.IdprooftypeRequestUrlEnglish;
import agent.eui.sendmoney_cashtocash_fragment.IdprooftypeRequestUrlFrench;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;

import static android.content.Context.MODE_PRIVATE;
import static agent.thread.ServerTask.baseUrl;

public class RecipientDetailPartOneFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {


    View view;
    String[] idDocumnetType_name_array;

    Button nextButton, dateOfBirth_calender_button;
    String[] profession_Type_array,genderTitle_code_array, genderTitle_name_array, idDocumnetType_code_array, countryCodeArray;
    Toolbar mToolbar;
    ModalReceiveMoney modalReceiveMoney = new ModalReceiveMoney();
    EditText other_edittext;

    AutoCompleteTextView idDocumentPlaceOfissue_autoComplteTextview, id_documentNumber_autoCompleteTextView;
    static AutoCompleteTextView id_document_dateofissue_autocompletetextview;
    Spinner spinner_id_document_type, spinner_id_document_countryOfIssue, spinner_gender_type, spinner_ProfessionType;
    ComponentMd5SharedPre mComponentInfo;
    String idDocumentType_code,genderType_code, languageToUse, otherSelectString, professionString, genderTypeString, idDocumentTypeSpinnerString, idDocumentCountryIssueSpinnerString, professionOtherSelectString, idDocumnetNoString, agentName, agentCode, idDocumentPlaceOfissue_String, senderMobileNoString, countrySelectionString = "";
    boolean isServerOperationInProcess;
    static String idDocumnetDateOfissueString = "", year_temp = "", month_temp = "", day_temp = "";
    ;
    private ScrollView scrollview_first_page;
    private String[] countryArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, payerAccountCodeArray;
    private ProgressDialog mDialog;
    TextView senderMobileNumber_textview, senderFirstName_textView, name_sender_textview;
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, RecipientDetailPartOneFragment.this, message.arg1, message.obj.toString());
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


        view = inflater.inflate(R.layout.recipientdetail_part_one_fragment, container, false); // Inflate the layout for this fragment


        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // currencySenderSelectionString = mComponentInfo.getmSharedPreferences().getString("currency", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


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


        idDocumentPlaceOfissue_autoComplteTextview = (AutoCompleteTextView) view.findViewById(R.id.idDocumentPlaceOfissue_autoComplteTextview);
        idDocumentPlaceOfissue_autoComplteTextview.setOnEditorActionListener(this);

        id_documentNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.id_documentNumber_autoCompleteTextView);
        id_document_dateofissue_autocompletetextview = (AutoCompleteTextView) view.findViewById(R.id.id_document_dateofissue_autocompletetextview);


        spinner_id_document_type = (Spinner) view.findViewById(R.id.spinner_id_document_type);
        spinner_id_document_type.setOnItemSelectedListener(this);


        spinner_id_document_countryOfIssue = (Spinner) view.findViewById(R.id.spinner_id_document_countryOfIssue);
        CountryFlagAdapterIDDocumnetType adapter5 = new CountryFlagAdapterIDDocumnetType("Id proof date of issue", countryArray, getResources(), getLayoutInflater());
        spinner_id_document_countryOfIssue.setAdapter(adapter5);
        spinner_id_document_countryOfIssue.setSelection(getCountrySelection());
        spinner_id_document_countryOfIssue.requestFocus();
        spinner_id_document_countryOfIssue.setOnItemSelectedListener(this);


        spinner_gender_type = (Spinner) view.findViewById(R.id.spinner_gender_type);
        genderTitle_name_array = getResources().getStringArray(R.array.genderTitle_name_sendCash);
        genderTitle_code_array = getResources().getStringArray(R.array.genderTittle_code_sendCash);

        other_edittext = (EditText) view.findViewById(R.id.other_edittext);


      /*  ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender_type.setAdapter(adapter3);*/
        spinner_gender_type.setOnItemSelectedListener(this);

        spinner_ProfessionType = (Spinner) view.findViewById(R.id.spinner_ProfessionType);
        spinner_ProfessionType.setOnItemSelectedListener(this);



         profession_Type_array = getResources().getStringArray(R.array.professionArray_cashtocash_sendmoney);

        //  Arrays.sort(profession_Type_array);
        //   profession_Type_array[0]=getString(R.string.profession);
        otherSelectString = profession_Type_array[32];





        senderMobileNumber_textview = (TextView) view.findViewById(R.id.senderMobileNumber_textview);
        senderFirstName_textView = (TextView) view.findViewById(R.id.senderFirstName_textView);
        name_sender_textview = (TextView) view.findViewById(R.id.name_sender_textview);


        senderMobileNoString = modalReceiveMoney.getDestinationMobileNumber();
        senderMobileNumber_textview.setText(senderMobileNoString);


        senderFirstName_textView.setText(modalReceiveMoney.getDestinationFirstName());
        name_sender_textview.setText(modalReceiveMoney.getDestinationLastName());


        mComponentInfo.setArrowBackButton_receiveCash(4);

        idproofRequest();


        return view;
    }

    private void idproofRequest() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generate_idproof();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

            if (languageToUse.equalsIgnoreCase("en")) {
                new IdprooftypeRequestUrlEnglish(mComponentInfo, getActivity(), mHandler, requestData, "typePieces", 229).start();
            } else {
                new IdprooftypeRequestUrlFrench(mComponentInfo, getActivity(), mHandler, requestData, "typePieces", 229).start();
            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
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


    private void AgentIdentity() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentity();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;


            // new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getAgentIdentity", 221).start();

            vollyRequest_getAgentidentity("getAgentIdentity", requestData, 221);


        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void vollyRequest_getAgentidentity(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                         //   Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                         //   Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, RecipientDetailPartOneFragment.this, requestCode, response.toString());
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


    private String generateDataAgentIdentity() {
        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();

            //  countryCodePrefixString = getCountryPrefixCode();
            //   accountNumber = getCountryPrefixCode() + accountNumber;

            countryObj.put("agentCode", modalReceiveMoney.getDestinationMobileNumber());            // verify check account number

            countryObj.put("transtype", "RECVCASHINT");
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
            id_document_dateofissue_autocompletetextview.setText(" " + day + "/" + month + "/" + year);

        }

    }


    void validationDetails() {

        if (validateSendMoneyToMobile_PartI()) {

           /* SenderDetailPartSecondFragment senderDetailPartSecondFragment = new SenderDetailPartSecondFragment();

            Bundle bundle = new Bundle();
            String recipientNumberString = getArguments().getString("recipientNumberString");
            bundle.putString("recipientNumberString", recipientNumberString);

            senderDetailPartSecondFragment.setArguments(bundle); //data being send to SecondFragment*/


            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("part_second").replace(R.id.frameLayout_cashtocash_receivemoney, new RecipientDetailpartSecondFragment()).commit();
        }

    }

    private boolean validateSendMoneyToMobile_PartI() {
        boolean ret = false;


        if (spinner_gender_type.getSelectedItemPosition() != 0) {


            if (spinner_id_document_type.getSelectedItemPosition() != 0) {

                idDocumnetNoString = id_documentNumber_autoCompleteTextView.getText().toString();
                if (idDocumnetNoString.length() > 3) {

                    idDocumnetDateOfissueString = id_document_dateofissue_autocompletetextview.getText().toString();

                    if (idDocumnetDateOfissueString.length() > 3) {
                        if (spinner_id_document_countryOfIssue.getSelectedItemPosition() != 0) {

                            idDocumentPlaceOfissue_String = idDocumentPlaceOfissue_autoComplteTextview.getText().toString().trim();
                            if (idDocumentPlaceOfissue_String.length() > 3) {

                                // if (spinner_ProfessionType.getSelectedItemPosition() != 0) {


                                professionOtherSelectString = other_edittext.getText().toString();

                                if (professionString.equalsIgnoreCase(otherSelectString)) {
                                    modalReceiveMoney.setProfession(professionOtherSelectString);
                                    //  Toast.makeText(getActivity(),"profession Other "+professionOtherSelectString, Toast.LENGTH_LONG).show();

                                } else {
                                    modalReceiveMoney.setProfession(professionString);

                                }


                                modalReceiveMoney.setIdProofIssueDate_agentIdentity(idDocumnetDateOfissueString);
                                modalReceiveMoney.setIdProofIssueOfPlace_agentIdentity(idDocumentPlaceOfissue_String);
                                modalReceiveMoney.setIdProofNumber(idDocumnetNoString);


                                ret = true;

                                        /*} else {
                                            Toast.makeText(getActivity(), getString(R.string.profession), Toast.LENGTH_LONG).show();
                                        }*/

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
                Toast.makeText(getActivity(), getString(R.string.id_document_tye_cashtocash), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.gender_title_new), Toast.LENGTH_LONG).show();
        }


        return ret;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_gender_type:

                genderTypeString = spinner_gender_type.getSelectedItem().toString();
                modalReceiveMoney.setGenderType_agentIdentity(genderTypeString);

                genderType_code = genderTitle_code_array[i];
                modalReceiveMoney.setGenderType_code_agentIdentity(genderType_code);


                //  Toast.makeText(getActivity(), genderTypeString, Toast.LENGTH_SHORT).show();

                break;

            case R.id.spinner_id_document_type:

                idDocumentTypeSpinnerString = spinner_id_document_type.getSelectedItem().toString();
                modalReceiveMoney.setIdProofType(idDocumentTypeSpinnerString);


                idDocumentType_code = idDocumnetType_code_array[i];
                modalReceiveMoney.setIdProofType_code(idDocumentType_code);


                break;

            case R.id.spinner_id_document_countryOfIssue:

                idDocumentCountryIssueSpinnerString = spinner_id_document_countryOfIssue.getSelectedItem().toString();

                idDocumentCountryIssueSpinnerString = countryArray[i];
                modalReceiveMoney.setIdproofCountryOfIssue_name(idDocumentCountryIssueSpinnerString);

                String idDocumentCountryIssueSpinnerString_code = countryCodeArray[i];
                modalReceiveMoney.setIdproofCountryOfIssue_code(idDocumentCountryIssueSpinnerString_code);


                // Toast.makeText(getActivity(), idDocumentCountryIssueSpinnerString, Toast.LENGTH_SHORT).show();

                break;

            case R.id.spinner_ProfessionType:

                professionString = spinner_ProfessionType.getSelectedItem().toString();

                if (professionString.equalsIgnoreCase(otherSelectString)) {
                    other_edittext.setVisibility(View.VISIBLE);
                } else {
                    other_edittext.setVisibility(View.GONE);
                }

                modalReceiveMoney.setProfession(professionString);


                // Toast.makeText(getActivity(), professionTypeString, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();

            if (requestNo == 221) {


                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");

                    idDocumentPlaceOfissue_String = responseArray[18];
                    idDocumentPlaceOfissue_autoComplteTextview.setText(idDocumentPlaceOfissue_String);

                    idDocumnetNoString = responseArray[9];
                    id_documentNumber_autoCompleteTextView.setText(idDocumnetNoString);


                    ///////////////////////////////////////////  gender Type   /////////////////////////////////////////

                    genderType_code = responseArray[26];

                    if(genderType_code.equalsIgnoreCase(""))
                    {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_gender_type.setAdapter(adapter);
                    }

                    else {

                        for (int i = 0; i < genderTitle_code_array.length; i++) {
                            if (genderTitle_code_array[i].equalsIgnoreCase(genderType_code)) {
                                genderType_code = genderTitle_code_array[i];
                                genderType_code = genderTitle_name_array[i];

                            } else {

                                System.out.println("genderType_code");   // if  not match from Current List Id DOCUMENT
                                /// // id_documentNumber_textview.setText("");       //
                                /// // id_document_dateofissue_textview.setText("");
                                //  idDocumentPlaceOfissue_textview.setText("");
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_gender_type.setAdapter(adapter);


                        if (genderType_code != null) {
                            int spinnerPosition = adapter.getPosition(genderType_code);
                            spinner_gender_type.setSelection(spinnerPosition);
                        }
                    }


                    ///////////////////////////////////////////  id Document Date Of issue String  /////////////////////////////////////////

                    idDocumnetDateOfissueString = responseArray[10];
                    if (idDocumnetDateOfissueString.equalsIgnoreCase("")) {
                        id_document_dateofissue_autocompletetextview.setText(idDocumnetDateOfissueString);
                    } else {
                        String[] tempIdproof_server = idDocumnetDateOfissueString.split("\\ ");
                        String[] tempIdproof_temp = tempIdproof_server[0].split("\\-");

                        day_temp = tempIdproof_temp[2];
                        month_temp = tempIdproof_temp[1];
                        year_temp = tempIdproof_temp[0];

                        idDocumnetDateOfissueString = day_temp + "/" + month_temp + "/" + year_temp;
                        id_document_dateofissue_autocompletetextview.setText(idDocumnetDateOfissueString);
                    }

                    ///////////////////////////////////////////  id Document Type   /////////////////////////////////////////

                    idDocumentType_code=responseArray[12];
                    if(idDocumentType_code.equalsIgnoreCase(""))
                    {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, idDocumnetType_name_array);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_id_document_type.setAdapter(adapter);
                    }

                    else {

                        for (int i = 0; i < idDocumnetType_code_array.length; i++) {
                            if (idDocumnetType_code_array[i].equalsIgnoreCase(idDocumentType_code)) {
                                idDocumentType_code = idDocumnetType_code_array[i];
                                idDocumentType_code = idDocumnetType_name_array[i];
                            } else {

                                System.out.println("idProffNameFromServer");   // if  not match from Current List Id DOCUMENT
                                /// // id_documentNumber_textview.setText("");       //
                                /// // id_document_dateofissue_textview.setText("");
                                //  idDocumentPlaceOfissue_textview.setText("");
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, idDocumnetType_name_array);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_id_document_type.setAdapter(adapter);


                        if (idDocumentType_code != null) {
                            int spinnerPosition = adapter.getPosition(idDocumentType_code);
                            spinner_id_document_type.setSelection(spinnerPosition);
                        }
                    }



                    ///////////////////////////////////////////  Profession  only Name In String.xml   () /////////////////////////////////////////

                    professionString=responseArray[1];
                    if (professionString.equalsIgnoreCase("")) {

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, profession_Type_array);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ProfessionType.setAdapter(adapter2);

                    } else {


                        for (int i = 0; i < profession_Type_array.length; i++) {
                            if (profession_Type_array[i].equalsIgnoreCase(professionString)) {
                                professionString = profession_Type_array[i];
                            } else {
                                System.out.println("professionString");   // not match
                            }
                        }

                        ArrayAdapter<String> adapter20 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, profession_Type_array);
                        adapter20.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ProfessionType.setAdapter(adapter20);


                        if (professionString != null) {
                            int spinnerPosition = adapter20.getPosition(professionString);
                            spinner_ProfessionType.setSelection(spinnerPosition);
                        }

                    }


                    //  Gender                         //   26 = "F"
                    //  id Document Type               //   12 = "DRVLIC"
                    //  id Document Number             //   9
                    //  id Document Date Off Issue     //   10
                    //  Profession                     //   1


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
                    // getActivity().finish();
                }
            } else if (requestNo == 229) {


                String responseData = generalResponseModel.getUserDefinedString();
                String[] responseArray = responseData.split("\\;");

                idDocumnetType_name_array = responseArray[0].split("\\|");
                idDocumnetType_code_array = responseArray[1].split("\\|");


                String idDocumnetType_name_array_temp = getString(R.string.type_de_document_id_aaaa);
                idDocumnetType_name_array[0] = idDocumnetType_name_array_temp;


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, idDocumnetType_name_array);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_id_document_type.setAdapter(adapter);


                if (modalReceiveMoney.getDestinationMobileNumber() == null
                        || modalReceiveMoney.getDestinationMobileNumber().equalsIgnoreCase("null")
                        || modalReceiveMoney.getDestinationMobileNumber().equalsIgnoreCase("")) {

                    System.out.println(" Agent Identity");  // if mobile number  is null api not call  not call
                } else {
                    AgentIdentity();
                }


            }


        } else {
            hideProgressDialog();

            if (requestNo != 221) {
                Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_gender_type.setAdapter(adapter);


            ArrayAdapter<String> adapter20 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, profession_Type_array);
            adapter20.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_ProfessionType.setAdapter(adapter20);



        }
    }
}


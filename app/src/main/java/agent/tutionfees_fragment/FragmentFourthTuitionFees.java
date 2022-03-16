package agent.tutionfees_fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.CountryFlagAdapterIDDocumnetType;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static android.content.Context.MODE_PRIVATE;



public class FragmentFourthTuitionFees extends Fragment implements View.OnClickListener, TextWatcher, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    String str_first, str_second, str_third;

    String level_code, countryCodeSelection, schoolCode, feesTutionString, feesExamString, feesCompitionString, optionTypeString, classNameString, emailString, levelTypeString, agentCode, agentName, payerNameString, countrySelectionString, mobileNumberString;
    private ProgressDialog mDialog;
    String[] schollDate_array, schollDate_code_array, level_name_array, level_code_array, option_name_array, option_code_array;
    Spinner spinner_level_type, spinner_option_type, spinner_schollYear_type, spinnerCountry;
    LinearLayout linearlayout_dynamically, linearLayout_feesDetail;
    Double editableAmount_double = 0.0;
    String partialAmountString;
    Double partialAmount_double = 0.0;
    String countryMobileNoLenghtString = "";


    Button submitButton;
    ComponentMd5SharedPre mComponentInfo;
    int i;


    String[] feesIdArray = {"5", "4", "3"};
    //String[] feeNameArray = {"FRAIS EXAMEN BEPC", "FRAIS EXIGIBLES", "Compitation fees"};
    // String[] feeAmountArray = {"3500", "7500", "100"};

    String[] feeNameArray;// = {"FRAIS EXAMEN BEPC", "FRAIS EXIGIBLES", "Compitation fees"};
    String[] feeAmountEnteredArr;// = {"FRAIS EXAMEN BEPC", "FRAIS EXIGIBLES", "Compitation fees"};
    String[] feeNameEnteredArr;// = {"FRAIS EXAMEN BEPC", "FRAIS EXIGIBLES", "Compitation fees"};
    String[] feeAmountArray;// = {"3500", "7500", "100"};
    String[] comptitionFeesArray;// = {"3500", "7500", "100"};


    // EditText amountEditText;
    String amountTuitionTemp_demo, amountExamTemp_demo, amountCompitionTemp_demo;
    Button button_agenttIdentity;
    AutoCompleteTextView mobileNumber_autoCompleteTextView;
    EditText payerEmail_edittext, payerName_edittext;
    EditText className_edittext;
    TextView totalAmount_textview;
    String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray;

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    View view;
    String mpinString;

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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, FragmentFourthTuitionFees.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fourth_tuitionfees, container, false); // Inflate the layout for this fragment


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
        try {


            agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
            agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
            //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
            // currencySenderSelectionString = mComponentInfo.ge223tmSharedPreferences().getString("currency", "");

            // SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
            // countrySelectionString = prefs.getString("countrySelectionString", null);


            countrySelectionString = mComponentInfo.getCountry_name();
            //  countrySelectionString="Central African Republic";


            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");


            spinnerCountry = (Spinner) view.findViewById(R.id.spinnerCountry);
            CountryFlagAdapterIDDocumnetType adapter = new CountryFlagAdapterIDDocumnetType("SendingCountry", countryArray, getResources(), getLayoutInflater());
            spinnerCountry.setAdapter(adapter);
            spinnerCountry.setEnabled(false);
            spinnerCountry.setSelection(getCountrySelection());
            spinnerCountry.requestFocus();
            spinnerCountry.setOnItemSelectedListener(this);


            submitButton = (Button) view.findViewById(R.id.submitButton);
            submitButton.setOnClickListener(this);


            spinner_option_type = (Spinner) view.findViewById(R.id.spinner_option_type);


            mobileNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.mobileNumber_autoCompleteTextView);
            totalAmount_textview = (TextView) view.findViewById(R.id.totalAmount_textview);


            className_edittext = (EditText) view.findViewById(R.id.className_edittext);
            payerName_edittext = (EditText) view.findViewById(R.id.payerName_edittext);
            payerEmail_edittext = (EditText) view.findViewById(R.id.payerEmail_edittext);


            linearlayout_dynamically = (LinearLayout) view.findViewById(R.id.linearlayout_dynamically);
            linearLayout_feesDetail = (LinearLayout) view.findViewById(R.id.linearLayout_feesDetail);


            countryCodeSelection = mComponentInfo.getCountry_code();
            schoolCode = mComponentInfo.getSchool_code();

            spinner_level_type = (Spinner) view.findViewById(R.id.spinner_level_type);

            button_agenttIdentity = (Button) view.findViewById(R.id.button_agenttIdentity);
            button_agenttIdentity.setOnClickListener(this);

            spinner_schollYear_type = (Spinner) view.findViewById(R.id.spinner_schollYear_type);

          //  schollDate_array = getResources().getStringArray(R.array.school_date_array);
          //  schollDate_code_array = getResources().getStringArray(R.array.school_date_arrayCode);



            // ######################################################################

            // ############## 2019 To 2020  ##############


         /*   int year = Calendar.getInstance().get(Calendar.YEAR);

            if(year==2019)
            {
                schollDate_array = getResources().getStringArray(R.array.school_dateArrayName_2019_to_2020);
                schollDate_code_array = getResources().getStringArray(R.array.school_dateArrayCode_2019_to_2020);
            }
            else if(year==2020)
            {
                schollDate_array = getResources().getStringArray(R.array.school_dateArrayName_2020_to_2021);
                schollDate_code_array = getResources().getStringArray(R.array.school_dateArrayCode_2020_to_2021);
            }
            else if(year==2021)
            {
                schollDate_array = getResources().getStringArray(R.array.school_dateArrayName_2021_to_2022);
                schollDate_code_array = getResources().getStringArray(R.array.school_dateArrayCode_2021_to_2022);
            }

            else if(year==2022)
            {
                schollDate_array = getResources().getStringArray(R.array.school_dateArrayName_2022_to_2023);
                schollDate_code_array = getResources().getStringArray(R.array.school_dateArrayCode_2022_to_2023);
            }
            else if(year==2023){
                schollDate_array = getResources().getStringArray(R.array.school_dateArrayName_2023_to_2024);
                schollDate_code_array = getResources().getStringArray(R.array.school_dateArrayCode_2023_to_2024);
            }

            else if(year==2024){
                schollDate_array = getResources().getStringArray(R.array.school_dateArrayName_2024_to_2025);
                schollDate_code_array = getResources().getStringArray(R.array.school_dateArrayCode_2024_to_2025);
            }

          */

            schollDate_array = getResources().getStringArray(R.array.school_dateArrayName_temp);
            schollDate_code_array = getResources().getStringArray(R.array.school_dateArrayCode_temp);

            // ######################################################################

            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, schollDate_array);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_schollYear_type.setAdapter(adapter3);
            spinner_schollYear_type.setOnItemSelectedListener(this);


            mComponentInfo.setArrowBackButtonTuitionFees("frag_fourth");

            setInputType_payerMobileNumber(1);

            request_Level();


        } catch (Exception e) {

            getActivity().finish();
        }


        return view;
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_level_type:

                levelTypeString = spinner_level_type.getSelectedItem().toString();
                mComponentInfo.setLevelTypeName(levelTypeString);
                classNameString = levelTypeString;
                className_edittext.setText(classNameString);

                level_code = level_code_array[i];
                mComponentInfo.setLevelTypeCode(level_code);


                if (i > 0) {

                    //  feeNameArray = new String[0];
                    //  feeAmountArray = new String[0];
                    //  comptitionFeesArray = new String[0];

                    request_option();
                }

                break;


            case R.id.spinner_option_type:

                optionTypeString = spinner_option_type.getSelectedItem().toString();
                mComponentInfo.setOptionTypeName(optionTypeString);
                String optionTypeCode = option_code_array[i];
                mComponentInfo.setOptionTypeCode(optionTypeCode);


                break;


            case R.id.spinner_schollYear_type:

                String schoolDate_type = spinner_schollYear_type.getSelectedItem().toString();


                break;


        }
    }


    private void request_Level() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_request_level();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "findClasses" + "/" + countryCodeSelection + "/" + schoolCode, 233).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    private void request_option() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_request_option();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "findClassOptions" + "/" + countryCodeSelection + "/" + schoolCode + "/" + level_code, 234).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    private void request_Fees() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_request_fees();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "findFees" + "/" + countryCodeSelection + "/" + schoolCode + "/" + level_code, 235).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }


    private String generateJson_request_fees() {
        String str = "";


        try {


        } catch (Exception e) {
        }
        return str;
    }

    private String generateJson_request_option() {
        String str = "";


        try {


        } catch (Exception e) {
        }
        return str;
    }

    private String generateJson_request_level() {
        String str = "";


        try {


        } catch (Exception e) {
        }
        return str;
    }

    private String generateJson_request_studentDetails() {
        String str = "";


        try {


        } catch (Exception e) {
        }
        return str;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.submitButton:

                validationDetails();  // temporary comment

                //  validateFeeAmounts();

                break;

            case R.id.button_agenttIdentity:

                if(validation_agentIdentity())
                {
                    agentIdentity_receiverMobileNo();
                }

                break;


        }
    }


    public void validationDetails() {

        if (validateDetails_Part_II()) {

            proceedTariffAmount();

        }
    }

    String aaaa = "";

    private String getTotalAmount() {

        String totalAmount = "0";
        double totalAmountD = 0.0;


        View viewGroup = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        for (int i = 0; i < feeNameArray.length; i++) {

            CheckBox checkBox = viewGroup.findViewWithTag(feeNameArray[i]);


            if (checkBox.isChecked()) {

                EditText editText = viewGroup.findViewWithTag(feeNameArray[i] + feeNameArray[i]);

                feeAmountEnteredArr[i] = editText.getText().toString().trim();

                feeNameEnteredArr[i] = feeNameArray[i];


                try {

                    totalAmountD = totalAmountD + Double.parseDouble(feeAmountEnteredArr[i]);


                } catch (Exception e) {


                }


            } else {
                feeAmountEnteredArr[i] = "0";
                feeNameEnteredArr[i] = "0";

            }


        }

        totalAmount = totalAmountD + "";

        mComponentInfo.setTransactionAmount(totalAmount);


        return totalAmount;

    }


    private boolean validateDetails_Part_II() {
        boolean ret = false;

        mobileNumberString = mobileNumber_autoCompleteTextView.getText().toString();
        payerNameString = payerName_edittext.getText().toString();
        emailString = payerEmail_edittext.getText().toString();
        classNameString = className_edittext.getText().toString();


        if (spinner_level_type.getSelectedItemPosition() != 0) {


            if (classNameString.trim().length() >= 3) {


                if (spinner_schollYear_type.getSelectedItemPosition() != 0) {

                    if (!getTotalAmount().equalsIgnoreCase("0.0")) {

                        // 0.0 < 1000.0 || 0.0 == 10000


                        if (editableAmount_double <= partialAmount_double) {


                            if (payerMobileNumber_validation()) {

                                if (payerNameString.trim().length() >= 3) {

                                    if (emailValidation(emailString)) {


                                        mComponentInfo.setClassName(classNameString);
                                        mComponentInfo.setPayerName(payerNameString);
                                        mComponentInfo.setPayerMobileNumber(countryCodeSelection + mobileNumberString);
                                        mComponentInfo.setPayerEmail(emailString);


                                        ret = true;

                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.enterValidEmailId), Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.payerName), Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.PayerMobileNumber), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), getString(R.string.amountCanotbeGreater), Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getActivity(), getString(R.string.selectFeesType), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.scholl_year), Toast.LENGTH_LONG).show();
                }


            } else {
                Toast.makeText(getActivity(), getString(R.string.className), Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.levelName_2), Toast.LENGTH_LONG).show();
        }

        return ret;

    }

    boolean emailValidation(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (emailAddress.equalsIgnoreCase("")) {
            return true;
        } else if (!matcher.find()) {
            return false;
        }

        return true;
    }



    boolean validation_agentIdentity() {   // setPayerMobileNumber

        mobileNumberString = mobileNumber_autoCompleteTextView.getText().toString();

        boolean ret = false;

        try {

            for (int i = 0; i < countryMobileNoLengthArray.length; i++) {
                if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                    countryMobileNoLenghtString = countryMobileNoLengthArray[i];
                } else {

                }
            }


            int payerMobileNo_int = mobileNumberString.length();
            int countryMobileNoLenght_int = Integer.parseInt(countryMobileNoLenghtString);

            if (countryMobileNoLenght_int == payerMobileNo_int) {
                ret = true;
            }

            else {
                Toast.makeText(getActivity(), getString(R.string.PayerMobileNumber), Toast.LENGTH_LONG).show();

                ret = false;
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        return ret;
    }

    boolean payerMobileNumber_validation() {   // setPayerMobileNumber

        boolean ret = false;

        try {

            for (int i = 0; i < countryMobileNoLengthArray.length; i++) {
                if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                    countryMobileNoLenghtString = countryMobileNoLengthArray[i];
                } else {

                }
            }

            int payerMobileNo_int = mobileNumberString.length();
            int countryMobileNoLenght_int = Integer.parseInt(countryMobileNoLenghtString);

           if (countryMobileNoLenght_int == payerMobileNo_int) {

                ret = true;
            }

           else {
                ret = false;
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        return ret;
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

    Double fees_tution_double, exam_tution_double, compitition_tution_double, transactionAmount_double;

    private String generateTariffAmmount() {
        String jsonString = "";

        SharedPreferences prefs = getActivity().getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", mComponentInfo.getPayerMobileNumber());


           /* if (mComponentInfo.getFeesTution().equalsIgnoreCase("")) {
                fees_tution_double = Double.parseDouble("0");
            } else {
                fees_tution_double = Double.parseDouble(mComponentInfo.getFeesTution());
            }

            if (mComponentInfo.getFeesExam().equalsIgnoreCase("")) {
                exam_tution_double = Double.parseDouble("0");
            } else {
                exam_tution_double = Double.parseDouble(mComponentInfo.getFeesExam());
            }

            if (mComponentInfo.getFeesCompetition().equalsIgnoreCase("")) {
                compitition_tution_double = Double.parseDouble("0");
            } else {
                compitition_tution_double = Double.parseDouble(mComponentInfo.getFeesCompetition());
            }
*/

            //  transactionAmount_double = fees_tution_double + exam_tution_double + compitition_tution_double;

            //    String transactionAmountString = Double.toString(transactionAmount_double);


            countryObj.put("amount", getTotalAmount());


            countryObj.put("transtype", "FEEPAYMENT");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            String fromCity = mComponentInfo.getmSharedPreferences().getString("state", "");

            countryObj.put("fromcity", fromCity);   //  change from server
            countryObj.put("tocity", mComponentInfo.getCity());     //  Change from Server
            countryObj.put("comments", "");
            countryObj.put("udv1", "");
            countryObj.put("accountType", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            //  countryObj.put("destination", nameNumberString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    void callApi(String apiName, String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServerTask.baseUrl + apiName, new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, FragmentFourthTuitionFees.this, requestCode, response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }

    void proceedTariffAmount() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            callApi("getTarifInJSON", requestData, 236);

            //   new ServerTaskDemoTutionFees(mComponentInfo, getActivity(), mHandler, requestData, "getTarifInJSON", 236).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();


            if (requestNo == 233) {

                try {


                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\;");

                    level_name_array = responseArray[0].split("\\|");
                    level_code_array = responseArray[1].split("\\|");

                    level_name_array[0] = getString(R.string.levelName_2);

                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, level_name_array);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_level_type.setAdapter(adapter3);
                    spinner_level_type.setOnItemSelectedListener(this);




                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            } else if (requestNo == 234) {

                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\;");

                    option_name_array = responseArray[0].split("\\|");
                    option_name_array[0] = getString(R.string.option);
                    option_code_array = responseArray[1].split("\\|");

                    ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, option_name_array);
                    adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_option_type.setAdapter(adapter6);
                    spinner_option_type.setOnItemSelectedListener(this);

                    request_Fees();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            }

            else if (requestNo == 243) {

                try {

                     String responseData = generalResponseModel.getUserDefinedString();
                     String[] responseArray = responseData.split("\\|");

                     payerNameString=responseArray[7];             // payer name from server
                     payerName_edittext.setText(payerNameString);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            }

            else if (requestNo == 235) {


                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\;");


                    feeNameArray = responseArray[1].split("\\|");
                    feeAmountArray = responseArray[2].split("\\|");

                    feeAmountEnteredArr = new String[feeAmountArray.length];
                    feeNameEnteredArr = new String[feeNameArray.length];


                    mComponentInfo.setFeeNameArray_fromServer(feeNameArray);     // demo
                    mComponentInfo.setFeeAmountArray_fromServer(feeAmountArray);

                    mComponentInfo.setFeeAmountEnteredArr_fromServer(feeAmountEnteredArr);
                    mComponentInfo.setFeeNameEnteredArr_fromServer(feeNameEnteredArr);

                    totalAmount_textview.setVisibility(View.VISIBLE);
                    linearLayout_feesDetail.setVisibility(View.VISIBLE);


                    for (int i = 0; i < feeAmountArray.length; i++) {

                        feeAmountEnteredArr[i] = "0";

                    }

                    for (int i = 0; i < feeNameArray.length; i++) {

                        feeNameEnteredArr[i] = "0";

                    }


                    comptitionFeesArray = responseArray[3].split("\\|");

                    linearlayout_dynamically.removeAllViews();


                    LayoutInflater inflater = getLayoutInflater();


                    for (i = 0; i < feeNameArray.length; i++) {

                        View view = inflater.inflate(R.layout.fees_item_structure, null);

                        CheckBox feeTypeCheckBox = (CheckBox) view.findViewById(R.id.checkbox_fee_paymenttype_item_str);
                        feeTypeCheckBox.setText(feeNameArray[i]);

                        feeTypeCheckBox.setOnCheckedChangeListener(this);


                        EditText amountEditText = (EditText) view.findViewById(R.id.fee_amount_edittext_item_str);
                        amountEditText.setText(feeAmountArray[i]);
                        amountEditText.setEnabled(false);
                        amountEditText.addTextChangedListener(FragmentFourthTuitionFees.this);
                        amountEditText.setVisibility(View.GONE);


                        amountEditText.setTag(feeNameArray[i] + feeNameArray[i]);
                        feeTypeCheckBox.setOnCheckedChangeListener(this);
                        feeTypeCheckBox.setTag(feeNameArray[i]);


                        linearlayout_dynamically.addView(view);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            }

            if (requestNo == 236) {   // tariff
                try {
                    String[] temp = generalResponseModel.getUserDefinedString().split("\\|");


                    String tariffAmountFee = temp[0];   // fees
                    String vatFeesAmount = temp[2];   // vat


                    Double fees_Frm_server = Double.parseDouble(tariffAmountFee);
                    Double vat_Frm_server = Double.parseDouble(vatFeesAmount);

                    vat_Frm_server = (vat_Frm_server / (100 + vat_Frm_server)) * fees_Frm_server;
                    fees_Frm_server = fees_Frm_server - vat_Frm_server;

                    NumberFormat df_fees_down = DecimalFormat.getInstance(Locale.ENGLISH);
                    df_fees_down.setMinimumFractionDigits(2);
                    df_fees_down.setGroupingUsed(false);
                    df_fees_down.setMaximumFractionDigits(2);
                    df_fees_down.setRoundingMode(RoundingMode.DOWN);



                   /* DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.UP);*/


                    mComponentInfo.setFees_tariff(df_fees_down.format(fees_Frm_server));


                    if (vatFeesAmount.equalsIgnoreCase("") || vatFeesAmount.equalsIgnoreCase("null")) {
                        mComponentInfo.setVat_tariff("0");
                    } else {

                        NumberFormat df_vat_up = DecimalFormat.getInstance(Locale.ENGLISH);
                        df_vat_up.setMinimumFractionDigits(2);
                        df_vat_up.setGroupingUsed(false);
                        df_vat_up.setMaximumFractionDigits(2);
                        df_vat_up.setRoundingMode(RoundingMode.UP);

                        mComponentInfo.setVat_tariff(df_vat_up.format(vat_Frm_server));
                    }

                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("FragmentFourthTuitionFees").replace(R.id.frameLayout_tutionfees, new MpinPageFragmentTuionFeesTemp()).commit();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            }

        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

            System.out.println(requestNo);   // 234   // 235 fees



            if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("no record found")) {      // not change Server resposne {"status":102,"message":"no record found"}



                if (requestNo == 234) {
                    linearlayout_dynamically.removeAllViews();
                    totalAmount_textview.setVisibility(View.GONE);
                    linearLayout_feesDetail.setVisibility(View.GONE);
                    // spinner_option_type.clear();
                    spinner_option_type.setAdapter(null);

                    request_Fees();


                } else if (requestNo == 235) {
                    linearlayout_dynamically.removeAllViews();
                    totalAmount_textview.setVisibility(View.GONE);
                    linearLayout_feesDetail.setVisibility(View.GONE);
                    // spinner_option_type.clear();
                    spinner_option_type.setAdapter(null);

                    getActivity().finish();

                }


            } else {
                linearlayout_dynamically.removeAllViews();
                totalAmount_textview.setVisibility(View.GONE);
                linearLayout_feesDetail.setVisibility(View.GONE);
                // spinner_option_type.clear();
                spinner_option_type.setAdapter(null);
            }


        }

    }

    /////////////////// Custome Check Box //////////////

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        View viewGroup = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        switch (buttonView.getId()) {

            case R.id.checkbox_fee_paymenttype_item_str:


                for (int a = 0; a < feeNameArray.length; a++) {

                    if (buttonView.getText().toString().equalsIgnoreCase(feeNameArray[a])) {
                        EditText edittextAmount = ((EditText) viewGroup.findViewWithTag(feeNameArray[a] + feeNameArray[a]));


                        if (buttonView.isChecked()) {


                            edittextAmount.setVisibility(View.VISIBLE);


                            if (comptitionFeesArray[a].equalsIgnoreCase("0")) {
                                edittextAmount.setEnabled(false);

                                // editableAmount = 0.0; // demo for validation on next button
                                // partialAmount = 0.0;  // demo for validation on next button


                            } else {

                                try {


                                    edittextAmount.setEnabled(true);

                                    partialAmountString = feeAmountEnteredArr[a];
                                    partialAmount_double = Double.parseDouble(partialAmountString);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }


                        } else {

                            edittextAmount.setVisibility(View.GONE);
                            edittextAmount.setEnabled(false);
                            feeAmountEnteredArr[a] = "0";
                            feeNameEnteredArr[a] = "0";

                            editableAmount_double = 0.0;

                            // editableAmount = 0.0;   // demo for validation on next button
                            //  partialAmount = 0.0;   // demo for validation on next button

                        }


                    }

                    totalAmount_textview.setText(getTotalAmount());


                }


                break;


        }


    }
    private String generateDataAgentIdentity() {
        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();

            //  countryCodePrefixString = getCountryPrefixCode();
            //   accountNumber = getCountryPrefixCode() + accountNumber;

            countryObj.put("agentCode", countryCodeSelection + mobileNumberString);            // verify check account number
            countryObj.put("transtype", "FEEPAYMENT");  // ankit 7 november 2019


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


    private void agentIdentity_receiverMobileNo() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentity();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

         //  new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getAgentIdentity", 243).start();

            vollyRequestApi_serverTask("getAgentIdentity", requestData, 243);


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


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, FragmentFourthTuitionFees.this, requestCode, response.toString());
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

    private void setInputType_payerMobileNumber(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                mobileNumber_autoCompleteTextView.setText("");
                mobileNumber_autoCompleteTextView.setHint(getString(R.string.PayerMobileNumber));
                // sourceMobileNumberEditText.setFilters(null);
                mobileNumber_autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[getCountrySelection()]));

                // sourceMobileNumberEditText.setHint(String.format(getString(R.string.senderMobileNumber_cashtocash), countryMobileNoLengthArray[getCountrySelection()] + " "));

                mobileNumber_autoCompleteTextView.setHint(getString(R.string.PayerMobileNumber));
                mobileNumber_autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
                mobileNumber_autoCompleteTextView.setFilters(digitsfilters);
                mobileNumber_autoCompleteTextView.setText("");


            } else if (i == 2) {
                mobileNumber_autoCompleteTextView.setText("");
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
                mobileNumber_autoCompleteTextView.setHint(getString(R.string.PayerMobileNumber));
                mobileNumber_autoCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                mobileNumber_autoCompleteTextView.setFilters(digitsfilters);
                mobileNumber_autoCompleteTextView.setText("");
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }


    private boolean validateFeeAmounts() {
        boolean ret = true;


        View viewGroup = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        if (((CheckBox) viewGroup.findViewWithTag(getString(R.string.FRAIS_EXAMEN_BEPC))).isChecked()) {

            if (validateAmount(((EditText) viewGroup.findViewWithTag(getString(R.string.edittext_FRAIS_EXAMEN_BEPC))).getText().toString())) {


            } else {
                ret = false;
            }

        }
        if (((CheckBox) viewGroup.findViewWithTag(getString(R.string.FRAIS_EXIGIBLES))).isChecked()) {

            if (validateAmount(((EditText) viewGroup.findViewWithTag(getString(R.string.edittext_FRAIS_EXIGIBLES))).getText().toString())) {


            } else {
                ret = false;
            }

        }

        if (((CheckBox) viewGroup.findViewWithTag(getString(R.string.Compitation_fees))).isChecked()) {


            if (validateAmount(((EditText) viewGroup.findViewWithTag(getString(R.string.edittext_Compitation_fees))).getText().toString())) {


            } else {
                ret = false;
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

            }
        } catch (Exception e) {
        }
        return ret;
    }


    private void updateTotalAmountUI(String data) {

        totalAmount_textview.setText(getTotalAmount());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {


        if (s.toString().equalsIgnoreCase("")) {

            totalAmount_textview.setText("");
        } else {

            editableAmount_double = Double.parseDouble(s + "");

            if (partialAmount_double >= editableAmount_double) {
                updateTotalAmountUI(s + "");
            } else {
                Toast.makeText(getActivity(), getString(R.string.amountCanotbeGreater), Toast.LENGTH_LONG).show();
            }
        }
     /*
        if(editableAmount>partialAmount)
        {
            Toast.makeText(getActivity(), "Plz update amount proper  ", Toast.LENGTH_LONG).show();
        }
        else {
            updateTotalAmountUI(s + "");
        }*/


    }
}

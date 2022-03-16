package agent.tutionfees_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;

import adapter.CountryFlagAdapterIDDocumnetType;
import agent.activities.R;
import agent.eui.sendmoney_cashtocash_fragment.ModalSendMoney;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Sahrique on 14/03/17.
 */

public class FragmentFirstTuitionFees extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {
    String schoolname_array_new, schoolCode_array_new, regionName_array_new, regionCode_array_new, division_array_new, subdivision_array_new, city_array_new;
    String[] studentregistrationNo_array,studentFirstName_array,studentBirtDate_array,student_gender_array,studentPhoneNumber_array,student_email_array,schoolname_array, schoolCode_array, regionName_array, regionCode_array, division_array, subdivision_array, city_array;

    String currencydestinationSelectionString, destinationCountrySelected_code;
    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    String[] currencyArray_zero_index;
    ModalSendMoney modalSendMoney = new ModalSendMoney();

    Toolbar mToolbar;
    View view;
    ComponentMd5SharedPre mComponentInfo;
    String schoolCodeString, agentName, agentCode, destinationCountrySelectedString, transferBasisString, countrySelectionString = "", accountCodeString;
    boolean isServerOperationInProcess;
    private Spinner spinnerDestinationCountry, transferBasisSpinner;
    private ScrollView scrollview;
    private AutoCompleteTextView schoolcode_editText;
    private ProgressDialog mDialog;
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;
    Button nextButton;
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, FragmentFirstTuitionFees.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };

    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

                validationAllDetails();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

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

        view = inflater.inflate(R.layout.fragment_first_tutionfees, container, false); // Inflate the layout for this fragment


        transferTagArray = getResources().getStringArray(R.array.TransferTag);

        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        // mToolbar.setNavigationIcon(R.drawable.franceflag);

        /*mToolbar = (Toolbar)view.findViewById(R.id.tool_bar_MoneyTransfer);
        mToolbar.setTitle(getString(R.string.cashInNewDisplaypage));
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
        }*/


        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {

            getActivity().finish();
        }


        scrollview = (ScrollView) view.findViewById(R.id.scrollview);
        spinnerDestinationCountry = (Spinner) view.findViewById(R.id.spinnerDestinationCountry);
        CountryFlagAdapterIDDocumnetType adapter = new CountryFlagAdapterIDDocumnetType("Country", countryArray, getResources(), getLayoutInflater());
        spinnerDestinationCountry.setAdapter(adapter);

        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerDestinationCountry.setSelection(getCountrySelection());
        spinnerDestinationCountry.requestFocus();
        spinnerDestinationCountry.setOnItemSelectedListener(this);

        transferBasisSpinner = (Spinner) view.findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(this);

        schoolcode_editText = (AutoCompleteTextView) view.findViewById(R.id.schoolcode_editText);
        schoolcode_editText.setOnEditorActionListener(this);


        if (mComponentInfo.getFirstChooseClick().equalsIgnoreCase("findSchool")) {
            schoolcode_editText.setHint(getString(R.string.schoolname_2));

        } else if (mComponentInfo.getFirstChooseClick().equalsIgnoreCase("findSchoolCode")) {
            schoolcode_editText.setHint(getString(R.string.schoolCode));

        } else {
            schoolcode_editText.setHint(getString(R.string.studentRegistrationNumber));
        }


        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        mComponentInfo.setArrowBackButtonTuitionFees("frag_first");


        return view;

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:


                break;


            case R.id.spinnerDestinationCountry:

                destinationCountrySelectedString = spinnerDestinationCountry.getSelectedItem().toString();
                destinationCountrySelected_code = countryPrefixArray[i];

                mComponentInfo.setCountry_name(destinationCountrySelectedString);
                mComponentInfo.setCountry_code(destinationCountrySelected_code);


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


    void validationAllDetails() {

        if (mComponentInfo.getFirstChooseClick().equalsIgnoreCase("findSchool")) {
            validateDetails_schollName();

        } else if (mComponentInfo.getFirstChooseClick().equalsIgnoreCase("findSchoolCode")) {
            validateDetails_schollCode();

        } else {
            validateDetails_registrationNumber();
        }
    }


    private boolean validate_schollName() {
        boolean ret = false;

        if (spinnerDestinationCountry.getSelectedItemPosition() != 0) {


            schoolCodeString = schoolcode_editText.getText().toString();
            if (schoolCodeString.trim().length() >=3) {


                ret = true;


            } else {
                Toast.makeText(getActivity(), getString(R.string.school_name_first_page), Toast.LENGTH_LONG).show();

            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.country), Toast.LENGTH_LONG).show();

        }

        return ret;
    }

    private boolean validate_schollCode() {
        boolean ret = false;

        if (spinnerDestinationCountry.getSelectedItemPosition() != 0) {


            schoolCodeString = schoolcode_editText.getText().toString();
            if (schoolCodeString.trim().length() >=3) {


                ret = true;


            } else {
                Toast.makeText(getActivity(), getString(R.string.schoolCode), Toast.LENGTH_LONG).show();

            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.country), Toast.LENGTH_LONG).show();

        }

        return ret;
    }

    private boolean validate_registrationMobileNumber() {
        boolean ret = false;

        if (spinnerDestinationCountry.getSelectedItemPosition() != 0) {


            schoolCodeString = schoolcode_editText.getText().toString();
            if (schoolCodeString.trim().length() >=3) {


                ret = true;


            } else {
                Toast.makeText(getActivity(), getString(R.string.studentRegistrationNumber), Toast.LENGTH_LONG).show();

            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.country), Toast.LENGTH_LONG).show();

        }

        return ret;
    }


    public void validateDetails_schollName() {

        if (validate_schollName()) {

            request_schoolName();
        }
    }

    public void validateDetails_schollCode() {

        if (validate_schollCode()) {

            request_schoolCode();
        }
    }

    public void validateDetails_registrationNumber() {

        if (validate_registrationMobileNumber()) {

            request_registrationNumber();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.nextButton:


                validationAllDetails();

                //  validateDetails();   // comment

                break;
        }
    }

    private void request_schoolName() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_findSchool();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            // http://192.168.0.42:9090/RESTfulWebServiceEU/json/estel/findSchool/237/KUMBA

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "findSchool" + "/" + destinationCountrySelected_code + "/" + schoolCodeString, 230).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    private void request_schoolCode() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_findSchool();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            // http://192.168.0.42:9090/RESTfulWebServiceEU/json/estel/schoolDetails/237/LT58100H23

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "schoolDetails" + "/" + destinationCountrySelected_code + "/" + schoolCodeString, 230).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    private void request_registrationNumber() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_findSchool();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            // http://192.168.0.42:9090/RESTfulWebServiceEU/json/estel/studentDetails/237/102188510549

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "studentDetails" + "/" + destinationCountrySelected_code + "/" + schoolCodeString, 242).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }


    private String generateJson_findSchool() {
        String str = "";


        try {


        } catch (Exception e) {
        }
        return str;
    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }


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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            if (!isServerOperationInProcess) {
                getActivity().finish();
            }
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


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();


            if (requestNo == 230) {

                try {


                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\;");


                    if (mComponentInfo.getFirstChooseClick().equalsIgnoreCase("findSchool")) {


                        schoolname_array = responseArray[0].split("\\|");

                        schoolname_array[0]=getString(R.string.school_name_first_page);

                        schoolCode_array = responseArray[1].split("\\|");
                        regionName_array = responseArray[2].split("\\|");
                        regionCode_array = responseArray[3].split("\\|");
                        division_array = responseArray[4].split("\\|");
                        subdivision_array = responseArray[5].split("\\|");
                        city_array = responseArray[6].split("\\|");


                        mComponentInfo.setSchoolname_array(schoolname_array);
                        mComponentInfo.setSchoolCode_array(schoolCode_array);
                        mComponentInfo.setRegionName_array(regionName_array);
                        mComponentInfo.setRegionCode_array(regionCode_array);
                        mComponentInfo.setDivision_array(division_array);
                        mComponentInfo.setSubdivision_array(subdivision_array);
                        mComponentInfo.setCity_array(city_array);




                        mComponentInfo.setSelectSearchBy("schoolName");

                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("FragmentFirstTuitionFees").replace(R.id.frameLayout_tutionfees, new FragmentSecondTuitionFees()).commit();

                    }


                    else if (mComponentInfo.getFirstChooseClick().equalsIgnoreCase("findSchoolCode")) {



                        schoolname_array = responseArray[0].split("\\|");

                        schoolname_array[0]=getString(R.string.school_name_first_page);

                        schoolCode_array = responseArray[1].split("\\|");
                        regionName_array = responseArray[2].split("\\|");
                        regionCode_array = responseArray[3].split("\\|");
                        division_array = responseArray[4].split("\\|");
                        subdivision_array = responseArray[5].split("\\|");
                        city_array = responseArray[6].split("\\|");


                        schoolname_array_new = schoolname_array[1];
                        schoolCode_array_new = schoolCode_array[1];
                        regionName_array_new = regionName_array[1];
                        regionCode_array_new = regionCode_array[1];
                        division_array_new = division_array[1];
                        subdivision_array_new = subdivision_array[1];
                        city_array_new = city_array[1];


                        mComponentInfo.setSchool_name(schoolname_array_new);
                        mComponentInfo.setSchool_code(schoolCode_array_new);
                        mComponentInfo.setRegion_name(regionName_array_new);
                        mComponentInfo.setRegion_code(regionCode_array_new);
                        mComponentInfo.setDivision(division_array_new);
                        mComponentInfo.setSubdivision(subdivision_array_new);
                        mComponentInfo.setCity(city_array_new);


                        mComponentInfo.setSelectSearchBy("searchSchoolCode");


                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("FragmentSecondTuitionFees").replace(R.id.frameLayout_tutionfees, new FragmentThirdTutionFees()).commit();

                    }




                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            }



            else   if (requestNo == 242) {      // demo repsonse Registartion Mobile number


                try {


                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\;");


                    schoolname_array = responseArray[0].split("\\|");

                    schoolname_array[0] = getString(R.string.school_name_first_page);

                    schoolCode_array = responseArray[1].split("\\|");
                    regionName_array = responseArray[2].split("\\|");
                    regionCode_array = responseArray[3].split("\\|");
                    division_array = responseArray[4].split("\\|");
                    subdivision_array = responseArray[5].split("\\|");
                    city_array = responseArray[6].split("\\|");

                    studentregistrationNo_array = responseArray[7].split("\\|");
                    studentFirstName_array = responseArray[8].split("\\|");
                    studentBirtDate_array = responseArray[9].split("\\|");
                    student_gender_array = responseArray[10].split("\\|");
                    studentPhoneNumber_array = responseArray[11].split("\\|");
                    student_email_array = responseArray[12].split("\\|");


                    schoolname_array_new = schoolname_array[1];
                    schoolCode_array_new = schoolCode_array[1];
                    regionName_array_new = regionName_array[1];
                    regionCode_array_new = regionCode_array[1];
                    division_array_new = division_array[1];
                    subdivision_array_new = subdivision_array[1];
                    city_array_new = city_array[1];


                    mComponentInfo.setSchool_name(schoolname_array_new);
                    mComponentInfo.setSchool_code(schoolCode_array_new);
                    mComponentInfo.setRegion_name(regionName_array_new);
                    mComponentInfo.setRegion_code(regionCode_array_new);
                    mComponentInfo.setDivision(division_array_new);
                    mComponentInfo.setSubdivision(subdivision_array_new);
                    mComponentInfo.setCity(city_array_new);


                    String studentRegistrationNo, studentName, studentBirthDate, student_gender, studentPhoneNumber, student_email;


                    studentRegistrationNo = studentregistrationNo_array[1];
                    studentName = studentFirstName_array[1];

                    studentBirthDate = studentBirtDate_array[1];


                    try {

                        if(studentBirthDate.equalsIgnoreCase(""))
                        {
                            mComponentInfo.setStudent_dateOfbirthdate(studentBirthDate);
                        }
                        else {
                            String[] studentBirthDate_temp = studentBirthDate.split("\\-");

                            String day_temp = studentBirthDate_temp[2];
                            String month_temp = studentBirthDate_temp[1];
                            String year_temp = studentBirthDate_temp[0];

                            studentBirthDate = day_temp + "/" + month_temp + "/" + year_temp;

                            mComponentInfo.setStudent_dateOfbirthdate(studentBirthDate);

                        }


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "" + "Date of Birth invalid from Backend Plz try again later", Toast.LENGTH_SHORT).show();
                    }

                    student_gender = student_gender_array[1];
                    studentPhoneNumber = studentPhoneNumber_array[1];
                    student_email = student_email_array[1];


                    mComponentInfo.setStudentRegistrationNumberString(studentRegistrationNo);
                    mComponentInfo.setStudentFirstNameString(studentName);
                    mComponentInfo.setGenderName(student_gender);
                    mComponentInfo.setStudentMobileNumberString(studentPhoneNumber);
                    mComponentInfo.setStudent_email(student_email);

                    mComponentInfo.setSelectSearchBy("registrationNumber");



                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("FragmentSecondTuitionFees").replace(R.id.frameLayout_tutionfees, new FragmentThirdTutionFees()).commit();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        } else {

            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();


        }

    }


}

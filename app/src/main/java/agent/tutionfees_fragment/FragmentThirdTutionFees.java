package agent.tutionfees_fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.CountryFlagAdapterIDDocumnetType;
import agent.activities.R;
import agent.eui.receivemoney_cashtocash_fragment.ModalReceiveMoney;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class FragmentThirdTutionFees extends Fragment implements View.OnClickListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

    String searchdataString = "", findNameString, findMobileNumberString, countryCodeSelection, studentMobileNumberString, findRegistrationNumber, studentFirstNameString, studentNameString, studentRegistrationNumberString, emailString, genderTypeString, agentCode, agentName, countrySelectionString;
    private ProgressDialog mDialog;
    String[] search_resultname_array, search_resultCode_array, countryArray, countryPrefixArray, countryMobileNoLengthArray, countryCodeArray, genderTitle_name_array, genderTitle_code_array, registrationType_name_array, registrationType_code;
    Spinner spinner_gender_type, spinner_registrationType, spinnerCountry, transferBasisSpinner, spinner_search_result;

    Button submitButton, dateOfBirth_calender_button, button_find;
    Button submitButton_secondPage;
    ComponentMd5SharedPre mComponentInfo;
    static AutoCompleteTextView dateOfBirtDate_autocompletetextview;
    static String dateOfBirtDateString;
    String countryMobileNoLenghtString = "";

    AutoCompleteTextView find_name_autoCompleteTextView, findMobileNumber_autoCompleteTextview, findRegistrationNumber_autoCompleteTextView, studentRegistrationNumber_autoCompleteTextView, studentMobileNumber_autoCompleteTextView;
    EditText email_edittext, studentName_edittext, studentName_first_edittext;

    String clickMobileNumber = "", clickRegistrationNumber = "", clickName = "";

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    ModalReceiveMoney modalReceiveMoney = new ModalReceiveMoney();

    View view;
    String mpinString, schoolCode;

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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, FragmentThirdTutionFees.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_third_tutionfees, container, false); // Inflate the layout for this fragment


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


            countrySelectionString = mComponentInfo.getCountry_name();
            //  countrySelectionString="Central African Republic";


            try {
                countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
                countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
                countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
                countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

            } catch (Exception e) {

                getActivity().finish();
            }


            TextView schoolname_textview, city_textview, sub_division_textview, division_textview, countryName_textview, schoolCode_textview, region_name_textview;

            countryName_textview = (TextView) view.findViewById(R.id.countryName_textview);
            schoolCode_textview = (TextView) view.findViewById(R.id.schoolCode_textview);
            region_name_textview = (TextView) view.findViewById(R.id.region_name_textview);
            division_textview = (TextView) view.findViewById(R.id.division_textview);
            sub_division_textview = (TextView) view.findViewById(R.id.sub_division_textview);
            city_textview = (TextView) view.findViewById(R.id.city_textview);
            schoolname_textview = (TextView) view.findViewById(R.id.schoolname_textview);


            submitButton = (Button) view.findViewById(R.id.submitButton);
            submitButton.setOnClickListener(this);

            submitButton_secondPage = (Button) view.findViewById(R.id.submitButton_secondPage);
            submitButton_secondPage.setOnClickListener(this);


            button_find = (Button) view.findViewById(R.id.button_find);
            button_find.setOnClickListener(this);


            countryName_textview.setText(mComponentInfo.getCountry_name());
            schoolname_textview.setText(mComponentInfo.getSchool_name());
            schoolCode_textview.setText(mComponentInfo.getSchool_code());
            region_name_textview.setText(mComponentInfo.getRegion_name());
            division_textview.setText(mComponentInfo.getDivision());
            sub_division_textview.setText(mComponentInfo.getSubdivision());
            city_textview.setText(mComponentInfo.getCity());


            countryCodeSelection = mComponentInfo.getCountry_code();


            spinner_gender_type = (Spinner) view.findViewById(R.id.spinner_gender_type);

            genderTitle_name_array = getResources().getStringArray(R.array.genderDetailsType_tuitionfees);
            genderTitle_code_array = getResources().getStringArray(R.array.genderDetailsCode_tuitionfees);


            spinner_search_result = (Spinner) view.findViewById(R.id.spinner_search_result);


            dateOfBirtDate_autocompletetextview = (AutoCompleteTextView) view.findViewById(R.id.dateOfBirtDate_autocompletetextview);

            dateOfBirth_calender_button = (Button) view.findViewById(R.id.dateOfBirth_calender_button);
            dateOfBirth_calender_button.setOnClickListener(this);

            findMobileNumber_autoCompleteTextview = (AutoCompleteTextView) view.findViewById(R.id.findMobileNumber_autoCompleteTextview);
            findRegistrationNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.findRegistrationNumber_autoCompleteTextView);
            find_name_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.find_name_autoCompleteTextView);


            studentRegistrationNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.studentRegistrationNumber_autoCompleteTextView);
            studentMobileNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.studentMobileNumber_autoCompleteTextView);

            studentName_edittext = (EditText) view.findViewById(R.id.studentName_edittext);
            studentName_first_edittext = (EditText) view.findViewById(R.id.studentName_first_edittext);
            email_edittext = (EditText) view.findViewById(R.id.email_edittext);


            spinner_registrationType = (Spinner) view.findViewById(R.id.spinner_registrationType);
            registrationType_name_array = getResources().getStringArray(R.array.registrationType_name);
            registrationType_code = getResources().getStringArray(R.array.registrationType_code);


            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, registrationType_name_array);
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_registrationType.setAdapter(adapter4);
            spinner_registrationType.setOnItemSelectedListener(this);

            schoolCode = mComponentInfo.getSchool_code();


            spinnerCountry = (Spinner) view.findViewById(R.id.spinnerCountry);
            CountryFlagAdapterIDDocumnetType adapter = new CountryFlagAdapterIDDocumnetType("SendingCountry", countryArray, getResources(), getLayoutInflater());
            spinnerCountry.setAdapter(adapter);
            spinnerCountry.setEnabled(false);
            spinnerCountry.setSelection(getCountrySelection());
            spinnerCountry.requestFocus();
            spinnerCountry.setOnItemSelectedListener(this);


            transferBasisSpinner = (Spinner) view.findViewById(R.id.spinnerSendMode_AccToCash);

            String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
            transferBasisSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
            transferBasisSpinner.setSelection(1);
            transferBasisSpinner.setOnItemSelectedListener(this);


            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_gender_type.setAdapter(adapter3);
            spinner_gender_type.setOnItemSelectedListener(this);


            if (mComponentInfo.getSelectSearchBy().equalsIgnoreCase("schoolName"))  // if select first by KUMBA
            {
                System.out.println(" all data Clear ");
                mComponentInfo.setArrowBackButtonTuitionFees("frag_third");

            } else if (mComponentInfo.getSelectSearchBy().equalsIgnoreCase("searchSchoolCode"))  // if seelct first by Kumba
            {
                System.out.println(" all data Clear ");
                mComponentInfo.setArrowBackButtonTuitionFees("frag_registrationNo_schollCode");

            } else {

                studentRegistrationNumber_autoCompleteTextView.setText(mComponentInfo.getStudentRegistrationNumberString());
                studentFirstNameString = mComponentInfo.getStudentFirstNameString();
                studentName_first_edittext.setText(studentFirstNameString);
                studentName_edittext.setText("");
                dateOfBirtDate_autocompletetextview.setText(mComponentInfo.getStudent_dateOfbirthdate());
                studentMobileNumber_autoCompleteTextView.setText(mComponentInfo.getStudentMobileNumberString());
                email_edittext.setText(mComponentInfo.getStudent_email());


                genderTypeString = mComponentInfo.getGenderName();


                if (genderTypeString.equalsIgnoreCase("M") || genderTypeString.equalsIgnoreCase("Male") || genderTypeString.equalsIgnoreCase("male") || genderTypeString.equalsIgnoreCase("Homme")) {
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_gender_type.setAdapter(adapter2);
                    spinner_gender_type.setOnItemSelectedListener(this);
                    spinner_gender_type.setSelection(1);
                } else if (genderTypeString.equalsIgnoreCase("f") || genderTypeString.equalsIgnoreCase("Female") || genderTypeString.equalsIgnoreCase("female") || genderTypeString.equalsIgnoreCase("Femme")) {
                    ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_gender_type.setAdapter(adapter5);
                    spinner_gender_type.setOnItemSelectedListener(this);
                    spinner_gender_type.setSelection(2);
                } else {

                    ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_gender_type.setAdapter(adapter5);
                    spinner_gender_type.setOnItemSelectedListener(this);
                    spinner_gender_type.setSelection(0);
                }


                mComponentInfo.setArrowBackButtonTuitionFees("frag_registrationNo_schollCode");
            }


            setInputType(1);

            setInputType_studentMobileNumber(1);   // for student  Mobile Number

        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
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

            dateOfBirtDateString = "" + month + "/" + day + "/" + year;
            dateOfBirtDate_autocompletetextview.setText(" " + day + "/" + month + "/" + year);

        }

    }


    private void listSelectionRequest() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_request_studentDetails();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "studentDetails" + "/" + countryCodeSelection + "/" + findRegistrationNumber, 240).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_gender_type:

                genderTypeString = spinner_gender_type.getSelectedItem().toString();
                mComponentInfo.setGenderName(genderTypeString);
                String genderType_code = genderTitle_code_array[i];
                mComponentInfo.setGenderCode(genderType_code);

                break;

            case R.id.spinnerCountry:

                setInputType(1);

                setInputType_studentMobileNumber(1);


                break;


            case R.id.spinner_search_result:


                String genderTypeString = spinner_search_result.getSelectedItem().toString();

                findRegistrationNumber = search_resultCode_array[i];


                if (i > 0) {

                    listSelectionRequest();
                }


                break;


            case R.id.spinner_registrationType:


                String registrationTypeString = spinner_registrationType.getSelectedItem().toString();

                if (i == 0) {

                    findMobileNumber_autoCompleteTextview.setVisibility(View.VISIBLE);

                    findRegistrationNumber_autoCompleteTextView.setVisibility(View.GONE);
                    find_name_autoCompleteTextView.setVisibility(View.GONE);
                    spinner_search_result.setVisibility(View.GONE);

                    clickMobileNumber = "mobileNo";
                    clickRegistrationNumber = "";
                    clickName = "";
                } else if (i == 1) {

                    findRegistrationNumber_autoCompleteTextView.setVisibility(View.VISIBLE);

                    findMobileNumber_autoCompleteTextview.setVisibility(View.GONE);
                    find_name_autoCompleteTextView.setVisibility(View.GONE);
                    spinner_search_result.setVisibility(View.GONE);

                    clickRegistrationNumber = "regNo";
                    clickMobileNumber = "";
                    clickName = "";
                } else if (i == 2) {

                    find_name_autoCompleteTextView.setVisibility(View.VISIBLE);

                    if (searchdataString.equalsIgnoreCase("searchdata")) {
                        spinner_search_result.setVisibility(View.VISIBLE);

                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, search_resultname_array);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_search_result.setAdapter(adapter3);
                        spinner_search_result.setOnItemSelectedListener(this);

                        submitButton.setVisibility(View.VISIBLE);
                        submitButton_secondPage.setVisibility(View.GONE);

                    } else {
                        spinner_search_result.setVisibility(View.GONE);
                    }


                    findMobileNumber_autoCompleteTextview.setVisibility(View.GONE);
                    findRegistrationNumber_autoCompleteTextView.setVisibility(View.GONE);

                    clickName = "name";
                    clickMobileNumber = "";
                    clickRegistrationNumber = "";
                }

                break;


        }
    }
    private void setInputType_studentMobileNumber(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                studentMobileNumber_autoCompleteTextView.setText("");
                studentMobileNumber_autoCompleteTextView.setHint(getString(R.string.mobileNumber));
                // sourceMobileNumberEditText.setFilters(null);
                studentMobileNumber_autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
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

                studentMobileNumber_autoCompleteTextView.setHint(getString(R.string.studentMobileNumber));
                studentMobileNumber_autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
                studentMobileNumber_autoCompleteTextView.setFilters(digitsfilters);
                studentMobileNumber_autoCompleteTextView.setText("");


            } else if (i == 2) {
                studentMobileNumber_autoCompleteTextView.setText("");
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
                studentMobileNumber_autoCompleteTextView.setHint(getString(R.string.studentMobileNumber));
                studentMobileNumber_autoCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                studentMobileNumber_autoCompleteTextView.setFilters(digitsfilters);
                studentMobileNumber_autoCompleteTextView.setText("");
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

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

                validationDetailsI();

                break;


            case R.id.submitButton_secondPage:

                validationDetailsII();

                break;

            case R.id.button_find:

                validation_find_by_mobileNo_registrationNo_name_AllDetails();

                break;


            case R.id.dateOfBirth_calender_button:

                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

                break;


        }
    }


    /////////////////////////////////////////////////////////////////////////

    void validation_find_by_mobileNo_registrationNo_name_AllDetails() {

        findMobileNumberString = findMobileNumber_autoCompleteTextview.getText().toString();
        findRegistrationNumber = findRegistrationNumber_autoCompleteTextView.getText().toString();
        findNameString = find_name_autoCompleteTextView.getText().toString();


        if (clickMobileNumber.equalsIgnoreCase("mobileNo")) {

            validationDetails_mobileNumber();

        } else if (clickRegistrationNumber.equalsIgnoreCase("regNo")) {

            validationDetails_registrationNumber();


        } else {
            validationDetails_name();
        }
    }


    public void validationDetails_mobileNumber() {


        if (validation_byMobileNumber()) {

            request_find_mobileNumber();
        }
    }

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                findMobileNumber_autoCompleteTextview.setText("");
                findMobileNumber_autoCompleteTextview.setHint(getString(R.string.mobileNumber));
                // sourceMobileNumberEditText.setFilters(null);
                findMobileNumber_autoCompleteTextview.setInputType(InputType.TYPE_CLASS_NUMBER);
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

                findMobileNumber_autoCompleteTextview.setHint(getString(R.string.mobileNumber));
                findMobileNumber_autoCompleteTextview.setInputType(InputType.TYPE_CLASS_NUMBER);
                findMobileNumber_autoCompleteTextview.setFilters(digitsfilters);
                findMobileNumber_autoCompleteTextview.setText("");


            } else if (i == 2) {
                findMobileNumber_autoCompleteTextview.setText("");
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
                findMobileNumber_autoCompleteTextview.setHint(getString(R.string.pleaseentername));
                findMobileNumber_autoCompleteTextview.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                findMobileNumber_autoCompleteTextview.setFilters(digitsfilters);
                findMobileNumber_autoCompleteTextview.setText("");
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }


    private boolean validation_byMobileNumber() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            String spinnerCountryString = spinnerCountry.getSelectedItem().toString();
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                // errorMsgToDisplay = String.format(getString(R.string.hintMobileCashIn), lengthToCheck + 1 + "");

            }
            if (findMobileNumberString.length() > lengthToCheck) {

                findMobileNumberString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + findMobileNumberString;
                mComponentInfo.setStudentMobileNumberString(findMobileNumberString);

                ret = true;

            } else {
                //     Toast.makeText(getActivity(), errorMsgToDisplay, Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), getString(R.string.mobileNumber), Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

        return ret;
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

    public void validationDetails_registrationNumber() {

        if (validation_byRegistationNumber()) {

            request_find_registrationNumber();
        }
    }

    public void validationDetails_name() {

        if (validation_byName()) {

            request_find_name();
        }
    }


    private void request_find_mobileNumber() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_request_studentDetails();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "findStudent" + "/" + countryCodeSelection + "/" + schoolCode + "/" + findMobileNumberString, 232).start();


        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    private void request_find_registrationNumber() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_request_studentDetails();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "studentDetails" + "/" + countryCodeSelection + "/" + findRegistrationNumber, 232).start();


        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    private void request_find_name() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_request_studentDetails();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "findStudent" + "/" + countryCodeSelection + "/" + schoolCode + "/" + findNameString, 241).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }


//////////////////////////////////////////////////////////////////////////


    public void validationDetailsI() {

        if (validateDetails_Part_I()) {


            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("FragmentThirdTutionFees").replace(R.id.frameLayout_tutionfees, new FragmentFourthTuitionFees()).commit();

        }
    }

    public void validationDetailsII() {

        if (validateDetails_Part_II()) {

            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("FragmentThirdTutionFees").replace(R.id.frameLayout_tutionfees, new FragmentFourthTuitionFees()).commit();

        }
    }


    private boolean validateDetails_Part_I() {
        boolean ret = false;

        studentRegistrationNumberString = studentRegistrationNumber_autoCompleteTextView.getText().toString();
        studentNameString = studentName_edittext.getText().toString();
        studentFirstNameString = studentName_first_edittext.getText().toString();
        studentMobileNumberString = studentMobileNumber_autoCompleteTextView.getText().toString();
        emailString = email_edittext.getText().toString();

        if (studentRegistrationNumberString.trim().length() >= 3) {


            if (studentFirstNameString.trim().length() >= 3) {

            if (spinner_gender_type.getSelectedItemPosition() != 0) {

                dateOfBirtDateString = dateOfBirtDate_autocompletetextview.getText().toString().trim();

                if (dateOfBirtDateString.length() > 3) {

                    mComponentInfo.setStudentRegistrationNumberString(studentRegistrationNumberString);

                    mComponentInfo.setStudentNameString(studentNameString);
                    mComponentInfo.setStudentFirstNameString(studentFirstNameString);

                    if (studentMobileNumber_validation()) {

                        if (emailValidation(emailString)) {

                            mComponentInfo.setStudent_email(emailString);
                            mComponentInfo.setStudent_dateOfbirthdate(dateOfBirtDateString);

                            ret = true;

                        } else {
                            Toast.makeText(getActivity(), getString(R.string.enterValidEmailId), Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getActivity(), getString(R.string.studentMobileNumber), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getActivity(), getString(R.string.birth_date), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getActivity(), getString(R.string.gender_new), Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.studentFirstName_reviewpage_new), Toast.LENGTH_LONG).show();
        }

        } else {
            Toast.makeText(getActivity(), getString(R.string.registrationNumber), Toast.LENGTH_LONG).show();
        }

        return ret;

    }


    boolean studentMobileNumber_validation() {

        boolean ret = false;

        try {

            for (int i = 0; i < countryMobileNoLengthArray.length; i++) {
                if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                    countryMobileNoLenghtString = countryMobileNoLengthArray[i];
                } else {

                }
            }

            int studentMobileNumber_int = studentMobileNumberString.length();
            int countryMobileNoLenght_int = Integer.parseInt(countryMobileNoLenghtString);

            if (studentMobileNumberString.equalsIgnoreCase("")) {
                mComponentInfo.setStudentMobileNumberString(studentMobileNumberString);
                ret = true;
            } else if (countryMobileNoLenght_int == studentMobileNumber_int) {
                mComponentInfo.setStudentMobileNumberString(countryCodeSelection + studentMobileNumberString);
                ret = true;
            } else {
                ret = false;
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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


    private boolean validateDetails_Part_II() {
        boolean ret = false;

        studentRegistrationNumberString = studentRegistrationNumber_autoCompleteTextView.getText().toString();
        studentNameString = studentName_edittext.getText().toString();
        studentFirstNameString = studentName_first_edittext.getText().toString();
        studentMobileNumberString = studentMobileNumber_autoCompleteTextView.getText().toString();
        emailString = email_edittext.getText().toString();

        if (studentRegistrationNumberString.trim().length() >= 3) {

        if (studentFirstNameString.trim().length() >= 3) {

            if (spinner_gender_type.getSelectedItemPosition() != 0) {

                dateOfBirtDateString = dateOfBirtDate_autocompletetextview.getText().toString().trim();

                if (dateOfBirtDateString.length() > 3) {

                    mComponentInfo.setStudentRegistrationNumberString(studentRegistrationNumberString);


                    mComponentInfo.setStudentNameString(studentNameString);
                    mComponentInfo.setStudentFirstNameString(studentFirstNameString);

                    if (studentMobileNumberString.equalsIgnoreCase("")) {

                    } else {
                        mComponentInfo.setStudentMobileNumberString(countryCodeSelection + studentMobileNumberString);
                    }

                    mComponentInfo.setStudent_email(emailString);
                    mComponentInfo.setStudent_dateOfbirthdate(dateOfBirtDateString);

                    ret = true;


                } else {
                    Toast.makeText(getActivity(), getString(R.string.birth_date), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getActivity(), getString(R.string.gender_new), Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.studentFirstName_reviewpage_new), Toast.LENGTH_LONG).show();
        }

    } else {
        Toast.makeText(getActivity(), getString(R.string.registrationNumber), Toast.LENGTH_LONG).show();
    }

        return ret;

    }


    public boolean validation_byRegistationNumber() {
        boolean ret = false;


        if (findRegistrationNumber.trim().length() >= 4) {
            mComponentInfo.setRegisterByNumberByName(findRegistrationNumber);

            ret = true;

        } else {
            Toast.makeText(getActivity(), getString(R.string.registrationNumber), Toast.LENGTH_SHORT).show();
        }

        return ret;
    }

    public boolean validation_byName() {

        boolean ret = false;


        if (findNameString.trim().length() >= 3) {
            mComponentInfo.setRegisterByNumberByName(findNameString);

            ret = true;

        } else {
            Toast.makeText(getActivity(), getString(R.string.name_tutionfees_new), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();


            if (requestNo == 232) {

                try {


                    if (clickMobileNumber.equalsIgnoreCase("mobileNo")) {

                        String responseData = generalResponseModel.getUserDefinedString();
                        String[] responseArray = responseData.split("\\|");

                        spinner_search_result.setVisibility(View.GONE);

                        studentNameString = responseArray[0];
                        genderTypeString = responseArray[2];

                        dateOfBirtDateString = responseArray[1];   // 2019-01-29  From Server  23 October 2019

                        try {

                            if (dateOfBirtDateString.equalsIgnoreCase("")) {
                                mComponentInfo.setStudent_dateOfbirthdate(dateOfBirtDateString);
                                dateOfBirtDate_autocompletetextview.setText(dateOfBirtDateString);
                            } else {

                                String[] tempIdproof_temp = dateOfBirtDateString.split("\\-");

                                String day_temp = tempIdproof_temp[2];
                                String month_temp = tempIdproof_temp[1];
                                String year_temp = tempIdproof_temp[0];

                                dateOfBirtDateString = day_temp + "/" + month_temp + "/" + year_temp;

                                mComponentInfo.setStudent_dateOfbirthdate(dateOfBirtDateString);
                                dateOfBirtDate_autocompletetextview.setText(dateOfBirtDateString);
                            }

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "" + "Date of birth invalid from backend Plz try again later", Toast.LENGTH_SHORT).show();
                        }

                        emailString = responseArray[6];
                        findMobileNumberString = responseArray[5];

                        String student_city = responseArray[15];

                        studentName_first_edittext.setText(studentNameString);
                        mComponentInfo.setStudentNameString(studentNameString);

                        mComponentInfo.setGenderName(genderTypeString);
                        mComponentInfo.setStudentMobileNumberString(findMobileNumberString);
                        studentMobileNumber_autoCompleteTextView.setText(findMobileNumberString);

                        mComponentInfo.setStudent_city(student_city);
                        mComponentInfo.setStudent_email(emailString);


                        studentRegistrationNumber_autoCompleteTextView.setText(findRegistrationNumber);
                        mComponentInfo.setRegisterByNumberByName(findRegistrationNumber);
                        studentRegistrationNumber_autoCompleteTextView.setText(findRegistrationNumber);
                        // studentName_first_edittext.setText(studentFirstNameString);
                        email_edittext.setText(emailString);


                        if (genderTypeString.equalsIgnoreCase("M") || genderTypeString.equalsIgnoreCase("Male") || genderTypeString.equalsIgnoreCase("male") || genderTypeString.equalsIgnoreCase("Homme")) {
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter2);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(1);
                        } else if (genderTypeString.equalsIgnoreCase("f") || genderTypeString.equalsIgnoreCase("Female") || genderTypeString.equalsIgnoreCase("female") || genderTypeString.equalsIgnoreCase("Femme")) {
                            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter5);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(2);
                        } else {

                            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter5);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(0);
                        }
                        mComponentInfo.setGenderName(genderTypeString);


                        submitButton.setVisibility(View.VISIBLE);
                        submitButton_secondPage.setVisibility(View.GONE);

                    } else if (clickRegistrationNumber.equalsIgnoreCase("regNo")) {
                        spinner_search_result.setVisibility(View.GONE);

                        String responseData = generalResponseModel.getUserDefinedString();
                        String[] responseArray = responseData.split("\\|");

                        studentNameString = responseArray[0];
                        genderTypeString = responseArray[2];
                        dateOfBirtDateString = responseArray[1];    // 2019-01-29  From Server  23 October 2019

                        try {

                            if (dateOfBirtDateString.equalsIgnoreCase("")) {
                                mComponentInfo.setStudent_dateOfbirthdate(dateOfBirtDateString);
                                dateOfBirtDate_autocompletetextview.setText(dateOfBirtDateString);
                            } else {


                                String[] tempIdproof_temp = dateOfBirtDateString.split("\\-");

                                String day_temp = tempIdproof_temp[2];
                                String month_temp = tempIdproof_temp[1];
                                String year_temp = tempIdproof_temp[0];

                                dateOfBirtDateString = day_temp + "/" + month_temp + "/" + year_temp;

                                mComponentInfo.setStudent_dateOfbirthdate(dateOfBirtDateString);
                                dateOfBirtDate_autocompletetextview.setText(dateOfBirtDateString);
                            }

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "" + "Date of birth From backend Plz try again later", Toast.LENGTH_SHORT).show();
                        }


                        emailString = responseArray[6];
                        findMobileNumberString = responseArray[5];

                        String student_city = responseArray[15];

                        studentName_first_edittext.setText(studentNameString);
                        mComponentInfo.setStudentNameString(studentFirstNameString);


                        if (genderTypeString.equalsIgnoreCase("M") || genderTypeString.equalsIgnoreCase("Male") || genderTypeString.equalsIgnoreCase("male") || genderTypeString.equalsIgnoreCase("Homme")) {
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter2);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(1);
                        } else if (genderTypeString.equalsIgnoreCase("f") || genderTypeString.equalsIgnoreCase("Female") || genderTypeString.equalsIgnoreCase("female") || genderTypeString.equalsIgnoreCase("Femme")) {
                            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter5);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(2);
                        } else {

                            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter5);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(0);
                        }


                        mComponentInfo.setGenderName(genderTypeString);
                        mComponentInfo.setStudentMobileNumberString(findMobileNumberString);
                        studentMobileNumber_autoCompleteTextView.setText(findMobileNumberString);

                        mComponentInfo.setStudent_city(student_city);
                        mComponentInfo.setStudent_email(emailString);


                        studentRegistrationNumber_autoCompleteTextView.setText(findRegistrationNumber);
                        mComponentInfo.setRegisterByNumberByName(findRegistrationNumber);
                        studentRegistrationNumber_autoCompleteTextView.setText(findRegistrationNumber);
                        //  studentName_first_edittext.setText(studentFirstNameString);
                        email_edittext.setText(emailString);

                        submitButton.setVisibility(View.VISIBLE);
                        submitButton_secondPage.setVisibility(View.GONE);


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            } else if (requestNo == 241) {   // if select Name


                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\;");

                    search_resultname_array = responseArray[0].split("\\|");

                    search_resultname_array[0] = getString(R.string.seach_result);
                    search_resultCode_array = responseArray[1].split("\\|");


                    spinner_search_result.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, search_resultname_array);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_search_result.setAdapter(adapter3);
                    spinner_search_result.setOnItemSelectedListener(this);

                    submitButton.setVisibility(View.VISIBLE);
                    submitButton_secondPage.setVisibility(View.GONE);

                    searchdataString = "searchdata";


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestNo == 240) {
                {
                    try {

                        String responseData = generalResponseModel.getUserDefinedString();
                        String[] responseArray = responseData.split("\\|");

                        spinner_search_result.setVisibility(View.VISIBLE);

                        studentNameString = responseArray[0];
                        genderTypeString = responseArray[2];
                        dateOfBirtDateString = responseArray[1];    // 2019-01-29  From Server  23 October 2019
                        emailString = responseArray[6];
                        findMobileNumberString = responseArray[5];

                        String student_city = responseArray[15];


                        submitButton.setVisibility(View.GONE);
                        submitButton_secondPage.setVisibility(View.VISIBLE);
                        studentName_edittext.setText("");                                    //  only Display List response in Student name blank
                        studentName_first_edittext.setText(studentNameString);               //  only Display List response in Student  First Name
                        mComponentInfo.setStudentFirstNameString(studentNameString);         //  only Display List response in Student  First Name

                        try {

                            if (dateOfBirtDateString.equalsIgnoreCase("")) {
                                mComponentInfo.setStudent_dateOfbirthdate(dateOfBirtDateString);
                                dateOfBirtDate_autocompletetextview.setText(dateOfBirtDateString);
                            } else {

                                String[] tempIdproof_temp = dateOfBirtDateString.split("\\-");

                                String day_temp = tempIdproof_temp[2];
                                String month_temp = tempIdproof_temp[1];
                                String year_temp = tempIdproof_temp[0];

                                dateOfBirtDateString = day_temp + "/" + month_temp + "/" + year_temp;

                                mComponentInfo.setStudent_dateOfbirthdate(dateOfBirtDateString);
                                dateOfBirtDate_autocompletetextview.setText(dateOfBirtDateString);
                            }

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "" + "Date of Birth From Backend Plz try again later", Toast.LENGTH_SHORT).show();
                        }


                        if (genderTypeString.equalsIgnoreCase("M") || genderTypeString.equalsIgnoreCase("Male") || genderTypeString.equalsIgnoreCase("male") || genderTypeString.equalsIgnoreCase("Homme")) {
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter2);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(1);
                        } else if (genderTypeString.equalsIgnoreCase("f") || genderTypeString.equalsIgnoreCase("Female") || genderTypeString.equalsIgnoreCase("female") || genderTypeString.equalsIgnoreCase("Femme")) {
                            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter5);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(2);
                        } else {

                            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
                            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gender_type.setAdapter(adapter5);
                            spinner_gender_type.setOnItemSelectedListener(this);
                            spinner_gender_type.setSelection(0);
                        }


                        mComponentInfo.setGenderName(genderTypeString);
                        mComponentInfo.setStudentMobileNumberString(findMobileNumberString);

                        studentMobileNumber_autoCompleteTextView.setText(findMobileNumberString);

                        mComponentInfo.setStudent_city(student_city);
                        mComponentInfo.setStudent_email(emailString);


                        studentRegistrationNumber_autoCompleteTextView.setText(findRegistrationNumber);
                        mComponentInfo.setRegisterByNumberByName(findRegistrationNumber);
                        studentRegistrationNumber_autoCompleteTextView.setText(findRegistrationNumber);
                        // studentName_first_edittext.setText(studentFirstNameString);
                        email_edittext.setText(emailString);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();


            studentMobileNumber_autoCompleteTextView.setText("");
            studentRegistrationNumber_autoCompleteTextView.setText("");
            mComponentInfo.setRegisterByNumberByName("");
            studentRegistrationNumber_autoCompleteTextView.setText("");
            studentName_edittext.setText("");
            studentName_first_edittext.setText("");
            dateOfBirtDate_autocompletetextview.setText("");
            email_edittext.setText("");


            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_gender_type.setAdapter(adapter2);
            spinner_gender_type.setOnItemSelectedListener(this);

            spinner_search_result.setVisibility(View.GONE);

        }

    }
}

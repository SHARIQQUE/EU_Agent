package agent.eui.sendmoney_cashtocash_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.CountryFlagAdapterIDDocumnetType;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;

import static android.content.Context.MODE_PRIVATE;

public class SenderDetailPartSecondFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {


    View view;
    EditText other_edittext;
    Button nextButton;
    String[] residentType_code_array, countryCodeArray, genderTitle_code_array;
    ModalSendMoney modalSendMoney;
    Toolbar mToolbar;
    AutoCompleteTextView address_1_autoCompleteTextView, address_2_autoCompleteTextView, city_autoCompleteTextView, email_autoCompleteTextView, homePhonenumber_autoCompleteTextView;
    Spinner spinner_nationallity, transferBasisSpinner, spinner_country, spinner_gender_type, spinner_ProfessionType, spinner_residentType;
    ComponentMd5SharedPre mComponentInfo;
    String city_string, address1_String, address2_String, fix_homePhoneNumber_string, otherSelectString, genderTypeString, professionString, professionOtherSelectString, countryName, residentTypeString, nationallityString, emailString, nameString, agentName, agentCode, senderMobileNoString, countrySelectionString = "";

    boolean isServerOperationInProcess;
    private ScrollView scrollview_first_page;
    private String[] countryArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, payerAccountCodeArray;
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SenderDetailPartSecondFragment.this, message.arg1, message.obj.toString());
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

        view = inflater.inflate(R.layout.senderdetail_part_second_fragment, container, false); // Inflate the layout for this fragment

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

        try {
            scrollview_first_page = (ScrollView) view.findViewById(R.id.scrollview_first_page);


            city_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.city_autoCompleteTextView);
            email_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.email_autoCompleteTextView);
            homePhonenumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.homePhonenumber_autoCompleteTextView);
            address_1_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.address_1_autoCompleteTextView);
            address_2_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.address_2_autoCompleteTextView);
            address_2_autoCompleteTextView.setOnEditorActionListener(this);

            other_edittext = (EditText) view.findViewById(R.id.other_edittext);


            spinner_gender_type = (Spinner) view.findViewById(R.id.spinner_gender_type);
            String[] genderTitle_name_array = getResources().getStringArray(R.array.genderTitle_name_sendCash);
            genderTitle_code_array = getResources().getStringArray(R.array.genderTittle_code_sendCash);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderTitle_name_array);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_gender_type.setAdapter(adapter);
            spinner_gender_type.setOnItemSelectedListener(this);


            spinner_ProfessionType = (Spinner) view.findViewById(R.id.spinner_ProfessionType);


            String[] profession_Type_array = getResources().getStringArray(R.array.professionArray_cashtocash_sendmoney);

            //  Arrays.sort(profession_Type_array);


            otherSelectString = profession_Type_array[32];  // Autres


            spinner_residentType = (Spinner) view.findViewById(R.id.spinner_residentType);

            String[] residentType_Type_array = getResources().getStringArray(R.array.residentType_Type_array);
            residentType_code_array = getResources().getStringArray(R.array.residentType_code_array);


            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, residentType_Type_array);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_residentType.setAdapter(adapter3);
            spinner_residentType.setOnItemSelectedListener(this);


            spinner_country = (Spinner) view.findViewById(R.id.spinner_country);
            CountryFlagAdapterIDDocumnetType adapter5 = new CountryFlagAdapterIDDocumnetType("Country", countryArray, getResources(), getLayoutInflater());
            spinner_country.setAdapter(adapter5);

            spinner_country.setSelection(getCountrySelection());
            spinner_country.requestFocus();
            spinner_country.setOnItemSelectedListener(this);

            transferBasisSpinner = (Spinner) view.findViewById(R.id.spinnerSendMode_AccToCash);
            String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
            transferBasisSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
            transferBasisSpinner.setSelection(1);
            transferBasisSpinner.setOnItemSelectedListener(this);


            String[] nationalityArray = getResources().getStringArray(R.array.nationality_array_sendMoney_EUI);


            spinner_nationallity = (Spinner) view.findViewById(R.id.spinner_nationallity);


            professionString = mComponentInfo.getProfessionAgentIdentity();
            nationallityString = mComponentInfo.getNationalityAgentIdentity();


            ///////////////////////////////////////////  Profession  only Name In String.xml   () /////////////////////////////////////////

            if (professionString.equalsIgnoreCase("")) {

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, profession_Type_array);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_ProfessionType.setAdapter(adapter2);
                spinner_ProfessionType.setOnItemSelectedListener(this);

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
                spinner_ProfessionType.setOnItemSelectedListener(this);


                if (professionString != null) {
                    int spinnerPosition = adapter20.getPosition(professionString);
                    spinner_ProfessionType.setSelection(spinnerPosition);
                }

            }

            //////////////////////////////////// Nationality  (only Name In String.xml) /////////////////////////////////////////


            if (nationallityString.equalsIgnoreCase("")||nationallityString.equalsIgnoreCase("Nationality")||nationallityString.equalsIgnoreCase("Nationalité"))

            {
                ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nationalityArray);
                adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_nationallity.setAdapter(adapter9);
                spinner_nationallity.setOnItemSelectedListener(this);
            }

            else {

                for (int i = 0; i < nationalityArray.length; i++) {
                    if (nationalityArray[i].equalsIgnoreCase(nationallityString)) {
                      //  idProof_code = nationalityArray[i];
                         nationallityString = nationalityArray[i];
                    } else {
                        System.out.println("idProffNameFromServer");   // not match
                    }
                }

                ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nationalityArray);
                adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_nationallity.setAdapter(adapter7);
                spinner_nationallity.setOnItemSelectedListener(this);

                if (nationallityString != null) {
                    int spinnerPosition = adapter7.getPosition(nationallityString);
                    spinner_nationallity.setSelection(spinnerPosition);
                }
                System.out.println(nationallityString);
                System.out.println(nationallityString);

            }

            /////////////////////////////////////////////////////////////////////////////////////


            mComponentInfo.setArrowBackButton_sendCash(4);


            address1_String = modalSendMoney.getAddress1();
            address1_String = modalSendMoney.getAddress2();

            fix_homePhoneNumber_string = modalSendMoney.getFixHomePhoneNumber();   // server resposne


            city_string = mComponentInfo.getCityAgentIdentity();
            city_autoCompleteTextView.setText(city_string);
            address_1_autoCompleteTextView.setText(address1_String);
            address_2_autoCompleteTextView.setText(address2_String);
            homePhonenumber_autoCompleteTextView.setText(fix_homePhoneNumber_string);

            //  setInputType(1);


        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getActivity(), getString(R.string.plzTryAgainLater), Toast.LENGTH_LONG).show();
            getActivity().finish();

        }


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


        }
    }


    void validationDetails() {

        if (validateSendMoneyToMobile_PartI()) {

            QuestionAnswerFragment questionAnswerFragment = new QuestionAnswerFragment();

            ModalFragmentManage modalFragmentManage = new ModalFragmentManage();
            modalFragmentManage.setFragment_for_sender("fifthFragment");


            modalSendMoney.setNationality(nationallityString);
            modalSendMoney.setProfession(professionString);


            if (fix_homePhoneNumber_string.equalsIgnoreCase("")) {
                modalSendMoney.setFixHomePhoneNumber(fix_homePhoneNumber_string);
            } else {
                String senderCountryCodePrefix = countryPrefixArray[spinner_country.getSelectedItemPosition()];
                modalSendMoney.setPreFixCountryHomePhoneNumber(senderCountryCodePrefix);
                modalSendMoney.setFixHomePhoneNumber(fix_homePhoneNumber_string);
            }


            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_five").replace(R.id.frameLayout_cashtocash, questionAnswerFragment).commit();
        }

    }


    private boolean validateSendMoneyToMobile_PartI() {
        boolean ret = false;


            /*emailString = email_autoCompleteTextView.getText().toString();
            if (emailString.length() > 3) {*/

        //   if (spinner_gender_type.getSelectedItemPosition() != 0) {

        if (spinner_ProfessionType.getSelectedItemPosition() != 0) {

            //   if (spinner_residentType.getSelectedItemPosition() != 0) {
            if (spinner_country.getSelectedItemPosition() != 0) {

                city_string = city_autoCompleteTextView.getText().toString();
                if (city_string.length() > 3) {

                    if (spinner_nationallity.getSelectedItemPosition() != 0) {
                        fix_homePhoneNumber_string = homePhonenumber_autoCompleteTextView.getText().toString();


                        professionOtherSelectString = other_edittext.getText().toString();

                        if (professionString.equalsIgnoreCase(otherSelectString)) {
                            modalSendMoney.setProfession(professionOtherSelectString);
                        } else {
                            modalSendMoney.setProfession(professionString);
                        }


                        emailString = email_autoCompleteTextView.getText().toString();

                        if (emailString.length() >= 0) {

                            if (emailValidation(emailString)) {

                                if (homePhoneNumberValidation(fix_homePhoneNumber_string)) {

                                    address1_String = address_1_autoCompleteTextView.getText().toString();
                                    address2_String = address_2_autoCompleteTextView.getText().toString();

                                    modalSendMoney.setEmailSender(emailString);
                                    modalSendMoney.setCity(city_string);
                                    modalSendMoney.setAddress1(address1_String);
                                    modalSendMoney.setAddress2(address2_String);


                                    ret = true;

                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.homePhonenumber_cashtocash), Toast.LENGTH_LONG).show();
                                }


                            } else {
                                Toast.makeText(getActivity(), getString(R.string.enterValidEmailId), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.email), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(getActivity(), getString(R.string.nationality), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getActivity(), getString(R.string.city_cashtocash), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.country), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.profession), Toast.LENGTH_LONG).show();
        }

        return ret;
    }

    public boolean homePhoneNumberValidation(String fix_homePhoneNumber_string) {

        int lengthToCheck = 3;
        String errorMsgToDisplay = "";
        int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

        if (transferBasisposition == 1) {
            lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinner_country.getSelectedItemPosition()]);
            errorMsgToDisplay = String.format(getString(R.string.homePhonenumber_cashtocash));

        } else {
            lengthToCheck = 2;
            errorMsgToDisplay = getString(R.string.homePhonenumber_cashtocash);
        }

        if (fix_homePhoneNumber_string.equalsIgnoreCase("")) {

            return true;
        } else if (fix_homePhoneNumber_string.length() == lengthToCheck) {
            // Toast.makeText(getActivity(), getString(R.string.homePhonenumber_cashtocash), Toast.LENGTH_LONG).show();
            return true;
        } else {

            Toast.makeText(getActivity(), getString(R.string.homePhonenumber_cashtocash), Toast.LENGTH_LONG).show();
            return false;

        }
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinner_gender_type:

                genderTypeString = spinner_gender_type.getSelectedItem().toString();
                modalSendMoney.setGenderType(genderTypeString);

                String genderType_code = genderTitle_code_array[i];
                modalSendMoney.setGenderType_code(genderType_code);


                //  Toast.makeText(getActivity(), genderTypeString, Toast.LENGTH_SHORT).show();

                break;

            case R.id.spinner_ProfessionType:

                professionString = spinner_ProfessionType.getSelectedItem().toString();


                if (professionString.equalsIgnoreCase(otherSelectString)) {
                    other_edittext.setVisibility(View.VISIBLE);
                } else {
                    other_edittext.setVisibility(View.GONE);
                }

                // Toast.makeText(getActivity(), professionTypeString, Toast.LENGTH_SHORT).show();

                break;

            case R.id.spinner_residentType:

                residentTypeString = spinner_residentType.getSelectedItem().toString();
                modalSendMoney.setResident(residentTypeString);


                String residentCode = residentType_code_array[i];
                modalSendMoney.setResidentCode(residentCode);


                // Toast.makeText(getActivity(), professionTypeString, Toast.LENGTH_SHORT).show();

                break;


            case R.id.spinner_country:


                countryName = spinner_country.getSelectedItem().toString();

                modalSendMoney.setCountryName(countryName);


                String countryCode = countryCodeArray[i];
                modalSendMoney.setCountryCode(countryCode);

                // Toast.makeText(getActivity(), professionTypeString, Toast.LENGTH_SHORT).show();

                setInputType(1);

                break;

            case R.id.spinner_nationallity:
                nationallityString = spinner_nationallity.getSelectedItem().toString();

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

    private void setInputType(int i) {

        if (spinner_country.getSelectedItemPosition() > 0) {
            if (i == 1) {
                homePhonenumber_autoCompleteTextView.setText(fix_homePhoneNumber_string);
                homePhonenumber_autoCompleteTextView.setHint(getString(R.string.homePhonenumber_cashtocash));
                // subscriber_MobileNo_EditText.setFilters(null);
                homePhonenumber_autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinner_country.getSelectedItemPosition()]));
                //  homePhonenumber_autoCompleteTextView.setHint(String.format(getString(R.string.hintMobileCashIn), countryMobileNoLengthArray[spinner_country.getSelectedItemPosition()] + " "));
                homePhonenumber_autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
                homePhonenumber_autoCompleteTextView.setFilters(digitsfilters);
                homePhonenumber_autoCompleteTextView.setText(fix_homePhoneNumber_string);


            } else if (i == 2) {
                homePhonenumber_autoCompleteTextView.setText(fix_homePhoneNumber_string);
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
                homePhonenumber_autoCompleteTextView.setHint(getString(R.string.homePhonenumber_cashtocash));
                homePhonenumber_autoCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                homePhonenumber_autoCompleteTextView.setFilters(digitsfilters);
                homePhonenumber_autoCompleteTextView.setText(fix_homePhoneNumber_string);
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            Toast.makeText(getActivity(), "Server Response " + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        } else {
            hideProgressDialog();
            // Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            //  Subscriber/Agent Not Found
        }
    }


}
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static android.content.Context.MODE_PRIVATE;

public class QuestionAnswerFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {


    ModalSendMoney modalSendMoney = new ModalSendMoney();

    String receiverMobileNumberRegisterCheck = "";

    View view;
    Button nextButton;
    String[] questionName_array, questionCode_array, answar_array, countryCodeArray;
    Toolbar mToolbar;
    TextView manullyQuestion_TextView;
    AutoCompleteTextView answar_autoComplteTextview, recipientNo_autoCompletetextview;
    EditText manullyQuestion_edittext, email_autoCompleteTextView, firstName_autoCompleteTextView, name_autoCompleteTextView;
    Spinner spinner_testQuestion;
    ComponentMd5SharedPre mComponentInfo;
    String languageToUse, mannuallyQuestionString, emailString_receipient, idDocumentTypeSpinnerString, question_code, firstNameString_receipient, nameString_receipient, agentName, questionString, agentCode, answerString, countrySelectionString = "";
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, QuestionAnswerFragment.this, message.arg1, message.obj.toString());
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


        view = inflater.inflate(R.layout.recipient_detail_fragment, container, false); // Inflate the layout for this fragment


        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // currencySenderSelectionString = mComponentInfo.getmSharedPreferences().getString("currency", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {

            getActivity().finish();
        }

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);


        scrollview_first_page = (ScrollView) view.findViewById(R.id.scrollview_first_page);


        answar_autoComplteTextview = (AutoCompleteTextView) view.findViewById(R.id.answar_autoComplteTextview);
        answar_autoComplteTextview.setOnEditorActionListener(this);

        recipientNo_autoCompletetextview = (AutoCompleteTextView) view.findViewById(R.id.recipientNo_autoCompletetextview);
        firstName_autoCompleteTextView = (EditText) view.findViewById(R.id.firstName_autoCompleteTextView);

        mComponentInfo.setArrowBackButton_sendCash(5);

       /* if(modalSendMoney.getFirstNameSender().equalsIgnoreCase(null)|| modalSendMoney.getFirstNameSender().equalsIgnoreCase("")||modalSendMoney.getFirstNameSender().equalsIgnoreCase("null"))
        {
            firstName_autoCompleteTextView.setText(modalSendMoney.getFirstName_agentidentity());
        }
        else {
            firstName_autoCompleteTextView.setText(modalSendMoney.getFirstName_agentidentity());
        }*/

        name_autoCompleteTextView = (EditText) view.findViewById(R.id.name_autoCompleteTextView);


        recipientNo_autoCompletetextview.setText(modalSendMoney.getDestinationMobileNumber());

        email_autoCompleteTextView = (EditText) view.findViewById(R.id.email_autoCompleteTextView);
        manullyQuestion_edittext = (EditText) view.findViewById(R.id.manullyQuestion_edittext);
        manullyQuestion_TextView = (TextView) view.findViewById(R.id.manullyQuestion_TextView);


        spinner_testQuestion = (Spinner) view.findViewById(R.id.spinner_testQuestion);
        spinner_testQuestion.setOnItemSelectedListener(this);

        questionRequest();


        return view;
    }


    private void questionRequest() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateSecretQuestionData(questionString, answerString, question_code);

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "secretQuestion", 225).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private void answerResponse() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateSecretQuestionData("", "", "");

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "secretQuestion", 225).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private String generateSecretQuestionData(String question, String answer, String questionCode) {

        String jsonString = "";


        SharedPreferences prefs = getActivity().getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String mpin = prefs.getString("MPIN", null);
        //  String pinTemp = mComponentInfo.getMD5(agentCode + mpin).toUpperCase();


        //  imeiNo="25825810";

        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", mpin);
            countryObj.put("pintype", "MPIN");

            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("udv1", "EUI");
            countryObj.put("question", question);
            countryObj.put("answer", answer);
            countryObj.put("questioncode", questionCode);
            countryObj.put("language", languageToUse);


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
        }
    }


    void validationDetails() {

        if (validateSendMoneyToMobile_PartI()) {


            MpinPageSendMoneyFrgament mpinPageSendMoneyFrgament = new MpinPageSendMoneyFrgament();


            modalSendMoney.setFirstNameReceiver(firstNameString_receipient);
            modalSendMoney.setNameReceiver(nameString_receipient);
            modalSendMoney.setEmailReceiver(emailString_receipient);

            ModalFragmentManage modalFragmentManage = new ModalFragmentManage();
            modalFragmentManage.setFragment_for_sender("sixFragment");


            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_six").replace(R.id.frameLayout_cashtocash, mpinPageSendMoneyFrgament).commit();
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

    boolean validationFirstName()
    {
        boolean ret=false;

        if(receiverMobileNumberRegisterCheck.equalsIgnoreCase("register"))
        {
            ret=true;
        }
        else {

            if (firstNameString_receipient.length() > 2) {

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

        firstNameString_receipient = firstName_autoCompleteTextView.getText().toString();

        if (validationFirstName()) {

            nameString_receipient = name_autoCompleteTextView.getText().toString();
            if (nameString_receipient.length() > 3) {


                emailString_receipient = email_autoCompleteTextView.getText().toString();
                if (emailString_receipient.length() >= 0) {

                    if (emailValidation(emailString_receipient)) {

                        if (spinner_testQuestion.getSelectedItemPosition() != 0) {

                            answerString = answar_autoComplteTextview.getText().toString().trim();
                            if (answerString.length() > 3) {


                                modalSendMoney.setAnswer_name(answerString);


                                mannuallyQuestionString = manullyQuestion_edittext.getText().toString();

                                if (questionString.equalsIgnoreCase(getString(R.string.manually_question)))  // fixed not change in string and fr
                                {
                                    modalSendMoney.setQuestion_name(mannuallyQuestionString);
                                    modalSendMoney.setQuestion_code(mannuallyQuestionString);

                                    //  Toast.makeText(getActivity(),"mannuallyQuestionString  + "+ mannuallyQuestionString, Toast.LENGTH_LONG).show();
                                } else {

                                    modalSendMoney.setQuestion_name(questionString);
                                    //  Toast.makeText(getActivity(),"questionString  + "+ questionString, Toast.LENGTH_LONG).show();
                                }


                                ret = true;

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.answer), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            mannuallyQuestionString = "";
                            //Toast.makeText(getActivity(), getString(R.string.test_question), Toast.LENGTH_LONG).show();
                            ret = true;
                        }


                    } else {
                        Toast.makeText(getActivity(), getString(R.string.enterValidEmailId), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getActivity(), getString(R.string.email), Toast.LENGTH_LONG).show();

                }


            } else {
                Toast.makeText(getActivity(), getString(R.string.name_cashtocash), Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getActivity(), getString(R.string.first_name), Toast.LENGTH_LONG).show();

        }

        return ret;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {


            case R.id.spinner_testQuestion:

                questionString = spinner_testQuestion.getSelectedItem().toString();
                modalSendMoney.setQuestion_name(questionString);
                String questionCode = questionCode_array[i];
                modalSendMoney.setQuestion_code(questionCode);
                if (i > 0) {
                    if (questionString.equalsIgnoreCase(getString(R.string.manually_question)))  // fixed not change in string and fr
                    {
                        manullyQuestion_edittext.setVisibility(View.VISIBLE);
                        manullyQuestion_TextView.setVisibility(View.VISIBLE);
                    } else {
                        manullyQuestion_edittext.setVisibility(View.GONE);
                        manullyQuestion_TextView.setVisibility(View.GONE);
                    }
                } else {
                    questionString = "";
                    modalSendMoney.setQuestion_name(questionString);
                    modalSendMoney.setQuestion_code("");
                }


                answar_autoComplteTextview.setText("");

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

    private void AgentIdentity() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentity();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;


            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getAgentIdentity", 221).start();

            //  vollyRequest_getAgentidentity("getAgentIdentity", requestData, 221);


        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateDataAgentIdentity() {
        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();

            //  countryCodePrefixString = getCountryPrefixCode();
            //   accountNumber = getCountryPrefixCode() + accountNumber;

            countryObj.put("agentCode", modalSendMoney.getDestinationMobileNumber());            // verify check account number
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
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 225) {

                try {


                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\;");

                    //  responseModel.setUserDefinedString(question + ";" +questionCode+ ";" + answer);


                    questionName_array = (responseArray[0] + "|" + getString(R.string.manually_question)).split("\\|");
                    questionCode_array = (responseArray[1] + "|" + getString(R.string.manually_question)).split("\\|");
                    answar_array = responseArray[2].split("\\|");

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, questionName_array);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_testQuestion.setAdapter(adapter2);

                    AgentIdentity();


                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().finish();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                }
            } else if (requestNo == 221) {


                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");

                    firstName_autoCompleteTextView.setText(".");    // firstNameString_receipient   first name tag  add  in agent Identity //  5 Nov 2019

                    nameString_receipient = responseArray[7];


                   /* if(firstNameString_receipient!=null && firstNameString_receipient.length()>1){
                        if(!(firstNameString_receipient.charAt(0)+"").equalsIgnoreCase(" ") ){
                            if( !firstNameString_receipient.contains(" ") ) {
                                firstName_autoCompleteTextView.setText(firstNameString_receipient);
                            }
                        }
                    }*/


                    name_autoCompleteTextView.setText(nameString_receipient);

                //    modalSendMoney.setFirstNameReceiver(firstNameString_receipient);

                    //  firstNameString_receipient = "";                            // new tag add agent identy

                    receiverMobileNumberRegisterCheck = "register";


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }


        } else {    // else Condition


            hideProgressDialog();


            if (requestNo == 225) {

                Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

            else if (requestNo == 221) {
                recipientNo_autoCompletetextview.setText(modalSendMoney.getDestinationMobileNumber());
              //  firstName_autoCompleteTextView.setText(".");
                receiverMobileNumberRegisterCheck = "notRegister";
                mComponentInfo.setSenderMobileNumberRegisterCheck(receiverMobileNumberRegisterCheck);
            }
        }

    }
}



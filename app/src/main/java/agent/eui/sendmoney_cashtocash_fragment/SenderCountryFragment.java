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
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
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

import com.android.volley.DefaultRetryPolicy;
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
import adapter.CountryFlagAdapterIDDocumnetType;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;

import static android.content.Context.MODE_PRIVATE;
import static agent.thread.ServerTask.baseUrl;

public class SenderCountryFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    ModalSendMoney modalSendMoney = new ModalSendMoney();

    View view;
    Button nextButton;
    String[] currencyArray, reason_name_english_array, reason_name_french_array, reason_code_array, bankSelectionArray, transferTagArray, accountTypeArray, countryCodeArray;
    Toolbar mToolbar;
    ModalFragmentManage modalFragmentManage = new ModalFragmentManage();

    ComponentMd5SharedPre mComponentInfo;
    String languageToUse, countrySelectionCode, agentName, spinnerCurrenceyString, spinnerCountryDestinationString, agentCode, reasonForTransfer_string, spinnerCountryString, senderMobileNoString, currencySenderSelectionString, amountString, countrySelectionString = "";
    boolean isServerOperationInProcess;
    private Spinner spinner_reasonForTransfer, transferBasisSpinner, spinnerCountry, spinnerCurrency, spinnerCountryDestination;
    private ScrollView scrollview_first_page;
    private AutoCompleteTextView amountEditText, sourceMobileNumberEditText;
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SenderCountryFragment.this, message.arg1, message.obj.toString());
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


        view = inflater.inflate(R.layout.sendercountry_fragment, container, false); // Inflate the layout for this fragment

        mComponentInfo = (ComponentMd5SharedPre) getActivity().getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);

        try {

            agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
            agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

            SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
            countrySelectionString = prefs.getString("countrySelectionString", null);

           /* // for DRC login
            if(countrySelectionString.equalsIgnoreCase("Democratic Republic of Congo"))
            {
               countrySelectionString="REPUBLIQUE DEMOCRATIQUE DU CONGO"; //diffrent country not coming small
            }*/


            SharedPreferences prefs2 = getActivity().getSharedPreferences("countrySelectionCode", MODE_PRIVATE);
            countrySelectionCode = prefs2.getString("countrySelectionCode", null);


            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList_EUI", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList_EUI", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList_EUI", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength_EUI", "").split("\\|");
            currencyArray = mComponentInfo.getmSharedPreferences().getString("currencyList_EUI", "").split("\\|");

        } catch (Exception e) {

            getActivity().finish();
        }

        generateCurrencyArr();

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        scrollview_first_page = (ScrollView) view.findViewById(R.id.scrollview_first_page);


        // Destination Mobile number is Benifeciry //


        spinnerCountry = (Spinner) view.findViewById(R.id.spinnerCountry);
        CountryFlagAdapterIDDocumnetType adapter = new CountryFlagAdapterIDDocumnetType("SendingCountry", countryArray, getResources(), getLayoutInflater());

        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        spinnerCountryDestination = (Spinner) view.findViewById(R.id.spinnerCountryDestination);

        spinnerCountry = (Spinner) view.findViewById(R.id.spinnerCountry);

        CountryFlagAdapter adapter2 = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountryDestination.setAdapter(adapter2);
        spinnerCountryDestination.setEnabled(false);
        spinnerCountryDestination.setSelection(getCountrySelection());
        spinnerCountryDestination.requestFocus();
        spinnerCountryDestination.setOnItemSelectedListener(this);


        transferBasisSpinner = (Spinner) view.findViewById(R.id.spinnerSendMode_AccToCash);

        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(this);


        sourceMobileNumberEditText = (AutoCompleteTextView) view.findViewById(R.id.sourceMobileNumberEditText);
        sourceMobileNumberEditText.setHint(getString(R.string.senderMobileNo_cashtocash));


        amountEditText = (AutoCompleteTextView) view.findViewById(R.id.amountEditText_AccToCash);


        spinner_reasonForTransfer = (Spinner) view.findViewById(R.id.spinner_reasonForTransfer);
        spinner_reasonForTransfer.setOnItemSelectedListener(this);


        spinnerCurrency = (Spinner) view.findViewById(R.id.spinnerCurrency);
        spinnerCurrency.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, currencyArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(arrayAdapter);


        mComponentInfo.setArrowBackButton_sendCash(0);

        request_getReason();

       // request_getcurrency();


        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(getActivity(), getString(R.string.shoudNotAllowZero), Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        amountEditText.setText(enteredString.substring(1));
                    } else {
                        amountEditText.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        amountEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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


        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountryDestination.setSelection(getCountrySelection());
        //  spinnerCurrency.setSelection(getCurrencySelection());

        setInputType(1);

        modalSendMoney.setQuestion_name("");
        modalSendMoney.setQuestion_code("");
        modalSendMoney.setAnswer_name("");
        modalSendMoney.setAnswer_name("");



        return view;
    }



    private void generateCurrencyArr(){

        String tempCurrencyString="";

        for(int i=0;i<countryCodeArray.length;i++){

            if((countrySelectionCode.equalsIgnoreCase("CAM")&&countryCodeArray[i].equals("CM"))||
                    (countrySelectionCode.equalsIgnoreCase("GBN")&&countryCodeArray[i].equals("GA"))||
                    (countrySelectionCode.equalsIgnoreCase("CNG")&&countryCodeArray[i].equals("CG"))||
                    (countrySelectionCode.equalsIgnoreCase("TCH")&&countryCodeArray[i].equals("TD"))||
                    (countrySelectionCode.equalsIgnoreCase("DRC")&&countryCodeArray[i].equals("CD"))||
                    (countrySelectionCode.equalsIgnoreCase("RCA")&&countryCodeArray[i].equals("CF"))) {


                tempCurrencyString = currencyArray[i];


            }

        }
//        tempCurrencyString=getActivity().getString(R.string.sendingCurrency_cashtocash_new)+":"+tempCurrencyString;
        tempCurrencyString=tempCurrencyString;
        currencyArray=tempCurrencyString.split("\\:");


    }
    private void request_getReason() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_getReason();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


            vollyRequest_getReason("getReasonList", requestData, 218);

            //  new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getReasonList", 218).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    void vollyRequest_getReason(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SenderCountryFragment.this, requestCode, response.toString());
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

    private String generateJson_getReason() {

        String jsonString = "";

        try {
            JSONObject countryObj = new JSONObject();


            // no json any objct pass // acording to abadesh


            jsonString = countryObj.toString();

        } catch (Exception e) {

            Log.e("blc Exception", e.toString());
        }

        return jsonString;
    }


    private void request_getcurrency() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_getcurrency();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


            vollyRequest_getCurrency("getCurrency", requestData, 217);

            //   new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getCurrency", 217).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    void vollyRequest_getCurrency(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SenderCountryFragment.this, requestCode, response.toString());
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

    private String generateJson_getcurrency() {

        String jsonString = "";

        try {
            JSONObject countryObj = new JSONObject();

            countryObj.put("countrycode", countrySelectionCode);

            jsonString = countryObj.toString();

        } catch (Exception e) {

            Log.e("blc Exception", e.toString());
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


    private boolean validateAmount(String input) {
        boolean ret = false;

        try {
            int amt = Integer.parseInt(input);
            if (amt > 0) {
                ret = true;
                amountString = "" + amt;
            }
        } catch (Exception e) {
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


           /* Bundle bundle=new Bundle();
            bundle.putString("senderMobileNoString",senderMobileNoString);
            bundle.putString("amountString", amountString);
            bundle.putString("currencySenderSelectionString", spinnerCurrenceyString);
            bundle.putString("reasonForTransfer_string", reasonForTransfer_string);
            bundle.putString("countrySelectionCode", countrySelectionCode);
*/
           tweakCountryEUI();

           modalSendMoney.setSenderMobileNumber(senderMobileNoString);
            modalSendMoney.setAmountString(amountString);
            modalSendMoney.setCurrencySenderCode(spinnerCurrenceyString);
            modalSendMoney.setReasonOfTransfer(reasonForTransfer_string);
            modalSendMoney.setCountrySenderName(countrySelectionString);
            modalSendMoney.setCountrySenderCode(countrySelectionCode);


            //     destinationCountryFragment.setArguments(bundle);    // data being send to SecondFragment

            modalFragmentManage.setFragment_for_sender("firstFragment");


            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_one").replace(R.id.frameLayout_cashtocash, new DestinationCountryFragment()).commit();
        }

    }



    private void tweakCountryEUI(){


        for(int i=0;i<countryCodeArray.length;i++){

            if((countrySelectionCode.equalsIgnoreCase("CAM")&&countryCodeArray[i].equals("CM"))||
                    (countrySelectionCode.equalsIgnoreCase("GBN")&&countryCodeArray[i].equals("GA"))||
                    (countrySelectionCode.equalsIgnoreCase("CNG")&&countryCodeArray[i].equals("CG"))||
                    (countrySelectionCode.equalsIgnoreCase("TCH")&&countryCodeArray[i].equals("TD"))||
                    (countrySelectionCode.equalsIgnoreCase("DRC")&&countryCodeArray[i].equals("CD"))||
                    (countrySelectionCode.equalsIgnoreCase("RCA")&&countryCodeArray[i].equals("CF"))) {


                countrySelectionString = countryArray[i];
                countrySelectionCode = countryCodeArray[i];

            }




        }



    }

    private boolean validateSendMoneyToMobile_PartI() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountryDestination.getSelectedItemPosition()]);
            String errorMsgToDisplay = "";


            transferBasisposition = 1;

            senderMobileNoString = sourceMobileNumberEditText.getText().toString().trim();
            errorMsgToDisplay = getString(R.string.hintPleaseEntreMobileNo) + " " + lengthToCheck + " " + getString(R.string.digits);
            if (senderMobileNoString.length() == lengthToCheck) {


                // if (spinnerCountryDestination.getSelectedItemPosition() != 0) {
                spinnerCountryDestinationString = spinnerCountryDestination.getSelectedItem().toString();
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountryDestination.getSelectedItemPosition()]);
                errorMsgToDisplay = getString(R.string.hintReceipntNumberRecipient) + " " + lengthToCheck + " " + getString(R.string.digits);


//                if (spinnerCurrency.getSelectedItemPosition() != 0) {
                if (!spinnerCurrency.getSelectedItem().toString().equalsIgnoreCase("Select Currency")) {
                    // spinnerCurrenceyString = spinnerCurrency.getSelectedItem().toString();

                    //  if (spinnerAccountToDebit.getSelectedItemPosition() != 0) {
                    amountString = amountEditText.getText().toString().trim();
                    if (amountString.length() > 0 && validateAmount(amountString)) {


                        if (spinner_reasonForTransfer.getSelectedItemPosition() != 0) {


                            String senderCountryCodePrefix=countryPrefixArray[spinnerCountry.getSelectedItemPosition()];
                            modalSendMoney.setSenderMobileNoCodePrefix(senderCountryCodePrefix);

                            senderMobileNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + senderMobileNoString;


                            ret = true;


                        } else {
                            Toast.makeText(getActivity(), getString(R.string.resonfor_transfer), Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getActivity(), getString(R.string.amountNew), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.sendingCurrency_cashtocash_new), Toast.LENGTH_LONG).show();
                }


            } else {

                //  Toast.makeText(getActivity(), errorMsgToDisplay, Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), getString(R.string.senderMobileNo_cashtocash), Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                sourceMobileNumberEditText.setText("");
                sourceMobileNumberEditText.setHint(getString(R.string.senderMobileNo_cashtocash));
                // sourceMobileNumberEditText.setFilters(null);
                sourceMobileNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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

                sourceMobileNumberEditText.setHint(getString(R.string.senderMobileNo_cashtocash));
                sourceMobileNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                sourceMobileNumberEditText.setFilters(digitsfilters);
                sourceMobileNumberEditText.setText("");


            } else if (i == 2) {
                sourceMobileNumberEditText.setText("");
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
                sourceMobileNumberEditText.setHint(getString(R.string.pleaseentername));
                sourceMobileNumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                sourceMobileNumberEditText.setFilters(digitsfilters);
                sourceMobileNumberEditText.setText("");
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:


                break;

            case R.id.spinner_PayerAccountTypeSelection_AccToAcc:

                if (i > 0) {
                    if (amountEditText != null) {
                        amountEditText.requestFocus();
                    }
                }
                break;

            case R.id.spinnerCountry:

                setInputType(1);
                //setInputTypeDestination(1);
                break;

            case R.id.spinnerCurrency:


                spinnerCurrenceyString = spinnerCurrency.getSelectedItem().toString();
                spinnerCurrenceyString = currencyArray[i];

                break;

            case R.id.spinner_reasonForTransfer:

                reasonForTransfer_string = spinner_reasonForTransfer.getSelectedItem().toString();


                String reasonForTransfer_Code = reason_code_array[i];
                modalSendMoney.setReasonOfTransferCode(reasonForTransfer_Code);


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

            if (requestNo == 217) {

                try {


                    String responseData = generalResponseModel.getUserDefinedCurrency();
                    String[] responseArray = responseData.split("\\;");
                    String countryCode = responseArray[3];

                  //  modalSendMoney.setCountryCode_sender_getCurrency(countryCode);

                    currencyArray = responseArray[0].split("\\|");

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, currencyArray);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCurrency.setAdapter(arrayAdapter);




                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
                    getActivity().finish();

                }


            } else if (requestNo == 218) {

                try {



                    String responseData = generalResponseModel.getReasonTranfer();
                    String[] responseArray = responseData.split("\\;");

                    reason_code_array = responseArray[0].split("\\|");
                    reason_name_english_array = responseArray[1].split("\\|");
                    reason_name_french_array = responseArray[2].split("\\|");


                    if (languageToUse.equalsIgnoreCase("fr")) {
                        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, reason_name_french_array);
                        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_reasonForTransfer.setAdapter(adapter5);

                    } else {
                        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, reason_name_english_array);
                        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_reasonForTransfer.setAdapter(adapter5);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }


        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }
    }
}
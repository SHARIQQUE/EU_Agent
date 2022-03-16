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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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
import static agent.thread.ServerTask.baseUrl;


/**
 * Created by Sahrique on 14/03/17.
 */

public class DestinationCountryFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String currencydestinationSelectionString, destinationCountrySelected_code;
    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    String[] currencyArray_zero_index;
    ModalSendMoney modalSendMoney = new ModalSendMoney();

    Toolbar mToolbar;
    View view;
    TaxExchangeRateVatPageFragment taxExchangeRateVatPageFragment;
    ComponentMd5SharedPre mComponentInfo;
    String agentName, agentCode, destinationCountrySelectedString, transferBasisString, recipientNumberString, countrySelectionString = "", accountCodeString;
    boolean isServerOperationInProcess;
    private Spinner spinnerCurrency_destination, spinnerDestinationCountry, transferBasisSpinner;
    private ScrollView scrollview;
    private AutoCompleteTextView receipient_MobileNo_EditText;
    private ProgressDialog mDialog;
    private String[] currencyArray, countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, DestinationCountryFragment.this, message.arg1, message.obj.toString());

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
                validateDetails();
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

        view = inflater.inflate(R.layout.destinationcountry_fragment, container, false); // Inflate the layout for this fragment


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
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList_EUI", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList_EUI", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList_EUI", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength_EUI", "").split("\\|");
            currencyArray = mComponentInfo.getmSharedPreferences().getString("currencyList_EUI", "").split("\\|");

        } catch (Exception e) {

            getActivity().finish();
        }


        spinnerCurrency_destination = (Spinner) view.findViewById(R.id.spinnerCurrency_destination);
        spinnerCurrency_destination.setOnItemSelectedListener(this);

        String currencyArraytemp=getString(R.string.sendingCurrency_cashtocash_destination_new);
        currencyArray[0]=currencyArraytemp;

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, currencyArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency_destination.setAdapter(arrayAdapter);



        /*System.out.println(countryArray);
        System.out.println(countryCodeArray);
        System.out.println(countryPrefixArray);
        System.out.println(countryMobileNoLengthArray);
        System.out.println(currencyArray);


        Arrays.sort(countryArray);
        Arrays.sort(countryCodeArray);
        Arrays.sort(countryPrefixArray);
        Arrays.sort(countryMobileNoLengthArray);
        Arrays.sort(currencyArray);

        System.out.println(countryArray);
        System.out.println(countryCodeArray);
        System.out.println(countryPrefixArray);
        System.out.println(countryMobileNoLengthArray);
        System.out.println(currencyArray);*/




        scrollview = (ScrollView) view.findViewById(R.id.scrollview);
        spinnerDestinationCountry = (Spinner) view.findViewById(R.id.spinnerDestinationCountry);
        CountryFlagAdapterIDDocumnetType adapter = new CountryFlagAdapterIDDocumnetType("destinationCountry", countryArray, getResources(), getLayoutInflater());
        spinnerDestinationCountry.setAdapter(adapter);

        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        // spinnerDestinationCountry.setSelection(getCountrySelection());
        spinnerDestinationCountry.requestFocus();
        spinnerDestinationCountry.setOnItemSelectedListener(this);

        transferBasisSpinner = (Spinner) view.findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(this);

        receipient_MobileNo_EditText = (AutoCompleteTextView) view.findViewById(R.id.receipient_MobileNo_EditText);
        receipient_MobileNo_EditText.setHint(getString(R.string.rercipient_mobileno_cashtocash));
        receipient_MobileNo_EditText.setOnEditorActionListener(this);

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);



        return view;

    }



    private void generateCurrencyArr(int position) {

        String tempCurrencyString = "";
        String[] currencyArrTemp;


        tempCurrencyString = currencyArray[position];


//        tempCurrencyString = getActivity().getString(R.string.sendingCurrency_cashtocash_destination_new) + ":" + tempCurrencyString;
        tempCurrencyString = tempCurrencyString;
        currencyArrTemp = tempCurrencyString.split("\\:");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, currencyArrTemp);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency_destination.setAdapter(arrayAdapter);

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


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, DestinationCountryFragment.this, requestCode, response.toString());
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

            countryObj.put("countrycode", destinationCountrySelected_code);

            jsonString = countryObj.toString();

        } catch (Exception e) {

            Log.e("blc Exception", e.toString());
        }

        return jsonString;
    }


    private void setInputType(int i) {

        if (spinnerDestinationCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                receipient_MobileNo_EditText.setText("");
                receipient_MobileNo_EditText.setHint(getString(R.string.rercipient_mobileno_cashtocash));
                // receipient_MobileNo_EditText.setFilters(null);
                receipient_MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinnerDestinationCountry.getSelectedItemPosition()]));
                //  receipient_MobileNo_EditText.setHint(String.format(getString(R.string.recipint_mobilenumber_cashtocash_), countryMobileNoLengthArray[spinnerDestinationCountry.getSelectedItemPosition()] + " "));
                receipient_MobileNo_EditText.setHint(String.format(getString(R.string.rercipient_mobileno_cashtocash)));//, countryMobileNoLengthArray[spinnerDestinationCountry.getSelectedItemPosition()] + " "));
                receipient_MobileNo_EditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                receipient_MobileNo_EditText.setFilters(digitsfilters);
                receipient_MobileNo_EditText.setText("");


            } else if (i == 2) {
                receipient_MobileNo_EditText.setText("");
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
                receipient_MobileNo_EditText.setHint(getString(R.string.pleaseentername));
                receipient_MobileNo_EditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                receipient_MobileNo_EditText.setFilters(digitsfilters);
                receipient_MobileNo_EditText.setText("");
            }
        } else {
//            Toast.makeText(getActivity(), getString(R.string.destinationCountry_cashtocash), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:

                setInputType(i);

                break;


            case R.id.spinnerDestinationCountry:

                destinationCountrySelectedString = spinnerDestinationCountry.getSelectedItem().toString();

                destinationCountrySelected_code = countryCodeArray[i];
                setInputType(transferBasisSpinner.getSelectedItemPosition());


               /* if (i == 0) {

                    String[] arrTempCurrency = {getString(R.string.destination_currency)};

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrTempCurrency);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCurrency_destination.setAdapter(arrayAdapter);

                } else {
                    request_getcurrency();
                }*/

                if (i > 0) {

                    generateCurrencyArr(i);
                }
                break;

            case R.id.spinnerCurrency_destination:

                currencydestinationSelectionString = spinnerCurrency_destination.getSelectedItem().toString();
                //  currencydestinationSelectionString = currencyArray[i];



               /* try {

                    currencydestinationSelectionString = spinnerCurrency_destination.getSelectedItem().toString();

                    if (currencyArray == null) {

                        Toast.makeText(getActivity(), "position  =" + "Zero ", Toast.LENGTH_LONG).show();


                        String[] arrTempCurrency = {getString(R.string.destination_currency)};

                        ArrayAdapter arrayAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrTempCurrency);
                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCurrency_destination.setAdapter(arrayAdapter2);



                    } else {

                        currencydestinationSelectionString = currencyArray[i];

                        Toast.makeText(getActivity(), "position  =" + currencydestinationSelectionString, Toast.LENGTH_LONG).show();

                        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, currencyArray);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCurrency_destination.setAdapter(arrayAdapter);

                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
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


    private boolean validateAccToCash_PartI() {
        boolean ret = false;

        if (spinnerDestinationCountry.getSelectedItemPosition() != 0) {
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerDestinationCountry.getSelectedItemPosition()]) - 1;
                // errorMsgToDisplay = String.format(getString(R.string.recipint_mobilenumber_cashtocash_), lengthToCheck + 1 + "");
                errorMsgToDisplay = String.format(getString(R.string.rercipient_mobileno_cashtocash));

            } else {
                transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);
            }

//            if (spinnerCurrency_destination.getSelectedItemPosition() != 0) {
            if (!spinnerCurrency_destination.getSelectedItem().toString().equalsIgnoreCase("Select Currency")) {

                recipientNumberString = receipient_MobileNo_EditText.getText().toString().trim();
                if (recipientNumberString.length() > lengthToCheck) {
                    if (transferBasisposition == 1) {
                        if (recipientNumberString.length() == ++lengthToCheck) {
                            recipientNumberString = countryPrefixArray[spinnerDestinationCountry.getSelectedItemPosition()] + recipientNumberString;
                        } else {
                            Toast.makeText(getActivity(), errorMsgToDisplay, Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }


                    ret = true;


                } else {
                    Toast.makeText(getActivity(), errorMsgToDisplay, Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(getActivity(), getString(R.string.sendingCurrency_cashtocash_destination_new), Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.destinationCountry_cashtocash), Toast.LENGTH_LONG).show();

        }

        return ret;
    }

    public void validateDetails() {
        if (validateAccToCash_PartI()) {


            ModalSendMoney modalSendMoney = new ModalSendMoney();
            modalSendMoney.setDestinationMobileNumber(recipientNumberString);
            modalSendMoney.setCurrencyDestinationCode(currencydestinationSelectionString);
            modalSendMoney.setCountryDestinationName(destinationCountrySelectedString);
            modalSendMoney.setCountryDestinationCode(destinationCountrySelected_code);


            //  taxExchangeRateVatPageFragment.setArguments(bundle); //data being send to SecondFragment

            ModalFragmentManage modalFragmentManage = new ModalFragmentManage();
            modalFragmentManage.setFragment_for_sender("secondFragment");


            //  Tax Api Call Here then go next page

            request_TaxCommit();


        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton:



                validateDetails();
                break;
        }
    }

    private void request_TaxCommit() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_taxCommit();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


            // vollyRequest_tax("taxandcommInt", requestData, 216);

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "taxandcommInt", 216).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    void vollyRequest_tax(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, DestinationCountryFragment.this, requestCode, response.toString());
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


    private String generateJson_taxCommit() {
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
            countryObj.put("comments", "comments");
            countryObj.put("amount", modalSendMoney.getAmountString());
            countryObj.put("sendercountry", modalSendMoney.getCountrySenderCode());// sender country code
            countryObj.put("sendertown", "ghjh");
            countryObj.put("sendercurrency", modalSendMoney.getCurrencySenderCode());
            countryObj.put("destcountry", modalSendMoney.getCountryDestinationCode()); // destination  country code
            countryObj.put("desttown", "jhgjh");
            countryObj.put("destcurrency", modalSendMoney.getCurrencyDestinationCode());
            countryObj.put("exchangerate", "2");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
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

    private String generateTariffAmmount() {
        String jsonString = "";

        SharedPreferences prefs = getActivity().getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", modalSendMoney.getSenderMobileNumber());
            countryObj.put("amount", modalSendMoney.getAmountString());
            countryObj.put("transtype", "SENDCASH");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            // String fromCity = mComponentInfo.getmSharedPreferences().getString("state", "");

            countryObj.put("fromcity", modalSendMoney.getCountrySenderCode());   //  change from server
            countryObj.put("tocity", modalSendMoney.getCountryDestinationCode());     //  Change from Server
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


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();


            if (requestNo == 219) {

                try {


                    String responseData = generalResponseModel.getUserDefinedCurrency();
                    String[] responseArray = responseData.split("\\;");

                    String countryCode = responseArray[2];
                    //   modalSendMoney.setCountryCode_receiver_getCurrency(countryCode);


                    currencyArray = responseArray[0].split("\\|");

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, currencyArray);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCurrency_destination.setAdapter(arrayAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            }

            else if (requestNo == 216) {


                try {

                     String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");

                   /*
                    0 = "180.0"     fees
                    1 = "0.0"       taxcharge
                    2 = "6000"
                    3 = "6000.00"
                    4 = "1"
                    5 = "Transaction Successful"
                    6 = "19.25"     vat*/


                    modalSendMoney.setVat_fromserver(responseArray[6]);   // vat

                    modalSendMoney.setTax_fromServer(responseArray[1]);   // tax
                    modalSendMoney.setAmount_fromServer(responseArray[2]);
                    NumberFormat df = DecimalFormat.getInstance(Locale.ENGLISH);
                    df.setMinimumFractionDigits(2);
                    df.setGroupingUsed(false);
                    df.setMaximumFractionDigits(2);
                    df.setRoundingMode(RoundingMode.DOWN);


                    modalSendMoney.setAmountToPay_fromserver(df.format(Double.parseDouble(responseArray[3])));



                    if(responseArray[4].equalsIgnoreCase("1")) // if 1 then Fees Display ///
                    {
                        modalSendMoney.setFees_fromServer(responseArray[0]);  // fees customercharge tag  both are same
                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_two").replace(R.id.frameLayout_cashtocash, new TaxExchangeRateVatPageFragment()).commit();
                    }
                    else {
                        proceedTariffAmount(); // if 0 then Tariff Api Call
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();

                }
            }

            else if (requestNo == 114) {

                String[] temp = generalResponseModel.getUserDefinedString().split("\\|");
                String tariffAmountFee = temp[0];

                modalSendMoney.setFees_fromServer(tariffAmountFee);

                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_two").replace(R.id.frameLayout_cashtocash, new TaxExchangeRateVatPageFragment()).commit();


            }


        } else {

            hideProgressDialog();

            if(requestNo==216)
            {
                if (generalResponseModel.getUserDefinedString().equalsIgnoreCase(""))
                {
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    void proceedTariffAmount() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  callApi("getTarifInJSON",requestData,114);

          //  new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getTarifInJSON", 114).start();

            vollyRequestApi_serverTask("getTarifInJSON",requestData,114);



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


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, DestinationCountryFragment.this, requestCode, response.toString());
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



}

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

import agent.activities.R;
import agent.eui.sendmoney_cashtocash_fragment.ModalSendMoney;
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

public class FragmentSecondTuitionFees extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String destinationCountrySelected_code, schoolCodeSelectedString;
    String[] bankSelectionArray, transferTagArray, accountTypeArray;
    String[] schoolcode_array, schoolname_array,currencyArray_zero_index;
    ModalSendMoney modalSendMoney = new ModalSendMoney();

    Toolbar mToolbar;
    View view;

    ComponentMd5SharedPre mComponentInfo;
    String schoolCodeString, agentName, agentCode, schoolNameSelectedString, transferBasisString, countrySelectionString = "", accountCodeString;
    boolean isServerOperationInProcess;
    private Spinner spinner_schoolName, transferBasisSpinner;
    private ScrollView scrollview;
    private AutoCompleteTextView schoolcode_editText;
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, FragmentSecondTuitionFees.this, message.arg1, message.obj.toString());

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

        view = inflater.inflate(R.layout.fragment_second_tutionfees, container, false); // Inflate the layout for this fragment


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




        //   String currencyArraytemp = getString(R.string.sendingCurrency_cashtocash_destination_new);
        //  currencyArray[0] = currencyArraytemp;


        scrollview = (ScrollView) view.findViewById(R.id.scrollview);

        spinner_schoolName = (Spinner) view.findViewById(R.id.spinner_schoolName);



        schoolname_array= mComponentInfo.getSchoolname_array();
        schoolcode_array= mComponentInfo.getSchoolCode_array();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, schoolname_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_schoolName.setAdapter(adapter);
        spinner_schoolName.setOnItemSelectedListener(this);


        transferBasisSpinner = (Spinner) view.findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(this);

        schoolcode_editText = (AutoCompleteTextView) view.findViewById(R.id.schoolcode_editText);
        schoolcode_editText.setOnEditorActionListener(this);

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);


            mComponentInfo.setArrowBackButtonTuitionFees("frag_second");


        } catch (Exception e) {

            getActivity().finish();
        }




        return view;

    }


    void vollyRequest_getCurrency(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                         //   Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                         //   Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, FragmentSecondTuitionFees.this, requestCode, response.toString());
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

            countryObj.put("countrycode", schoolCodeSelectedString);

            jsonString = countryObj.toString();

        } catch (Exception e) {

            Log.e("blc Exception", e.toString());
        }

        return jsonString;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:


                break;


            case R.id.spinner_schoolName:

                try {


                    schoolNameSelectedString = spinner_schoolName.getSelectedItem().toString();
                    mComponentInfo.setSchool_name(schoolNameSelectedString);

                    schoolCodeSelectedString = schoolcode_array[i];
                    destinationCountrySelected_code = countryPrefixArray[i];

                }
                catch (Exception e)
                {
                    e.printStackTrace();
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


    private boolean validateAccToCash_PartI() {
        boolean ret = false;

        if (spinner_schoolName.getSelectedItemPosition() != 0) {


          /*  schoolCodeString = schoolcode_editText.getText().toString();
            if (schoolCodeString.trim().length() > 4) {
*/

            ret = true;


            /*} else {
                Toast.makeText(getActivity(), getString(R.string.schoolCode), Toast.LENGTH_LONG).show();

            }*/


        } else {
            Toast.makeText(getActivity(), getString(R.string.school_name_first_page), Toast.LENGTH_LONG).show();

        }

        return ret;
    }

    public void validateDetails() {
        if (validateAccToCash_PartI()) {

            request_schoolDetails();

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

    private void request_schoolDetails() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_request_schoolDetails();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "schoolDetails" + "/" + "237" + "/" + schoolCodeSelectedString, 231).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    private String generateJson_request_schoolDetails() {
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


            if (requestNo == 231) {

                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");

                    String schoolname=responseArray[0];
                    String schoolCode=responseArray[1];
                    String regionName=responseArray[2];
                    String regionCode=responseArray[3];
                    String division=responseArray[4];
                    String subdivision=responseArray[5];
                    String city=responseArray[6];

                    mComponentInfo.setSchool_name(schoolname);
                    mComponentInfo.setSchool_code(schoolCode);
                    mComponentInfo.setRegion_name(regionName);
                    mComponentInfo.setRegion_code(regionCode);
                    mComponentInfo.setDivision(division);
                    mComponentInfo.setSubdivision(subdivision);
                    mComponentInfo.setCity(city);

                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("FragmentSecondTuitionFees").replace(R.id.frameLayout_tutionfees, new FragmentThirdTutionFees()).commit();



                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            }

        } else {

            hideProgressDialog();
             Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();


        }

    }
}

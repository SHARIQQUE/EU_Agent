/*
package shariq.eu_agent_new;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


*/
/**
 * Created by AdityaBugalia on 31/08/16.
 *//*

public class Tariff extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
                validateDetails();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public AutoCompleteTextView amountEditText, mpinEditText, destinationEditText;
    TextInputLayout destinationTextInputLayout;
    Spinner SpinnerOperationType, spinnerCountry, SpinnerAccountName;
    Button nextButton;
    Toolbar mToolbar;
    ComponentMd5SharedPre mComponentInfo;
    LinearLayout input_LL, review_LL;
    ProgressDialog mDialog;
    boolean isCashInCashOutClick = false;

    Dialog successDialog;
    String agentName, countrySelectionString, agentCode, spinnerCountryString, spinnerOprationTypeString, operationNameString, amountString, mpinString, spinnerAccountTypeString, destinationString;
    private String[] countryMobileNoLengthArray, countryArray, operationCodeArray, billerArray, accountCodeArray, accountNameCodeArray, countryPrefixArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tariff);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_Tariff);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.tariff));
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
        }
        amountEditText = (AutoCompleteTextView) findViewById(R.id.amount_EditText_Tariff);

        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_Tariff);
        mpinEditText.setOnEditorActionListener(this);
        // Looper.prepare();

        destinationEditText = (AutoCompleteTextView) findViewById(R.id.destinationEditText);
        destinationTextInputLayout = (TextInputLayout) findViewById(R.id.destinationEditText_til);
        destinationEditText.setOnEditorActionListener(this);

        SpinnerOperationType = (Spinner) findViewById(R.id.SpinnerOperationType);
        SpinnerAccountName = (Spinner) findViewById(R.id.spinner_AccountName);

        nextButton = (Button) findViewById(R.id.submitButton);
        nextButton.setOnClickListener(this);

        input_LL = (LinearLayout) findViewById(R.id.input_Recipient_LL_Tariff);
        review_LL = (LinearLayout) findViewById(R.id.review_LL_Tariff);
        billerArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");


        countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
        countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
        countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);

        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);


        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 5) {
            // Looper.prepare();
            String[] data = bankAccounts.split("\\;");

            accountNameCodeArray = new String[data.length + 1];
            accountNameCodeArray[0] = getString(R.string.PleaseSelectAccountType);

            accountCodeArray = new String[data.length + 1];
            accountCodeArray[0] = getString(R.string.PleaseSelectAccountType);

            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    accountNameCodeArray[i + 1] = tData[1];
                    accountCodeArray[i + 1] = tData[4];
                }
            }
        } else {
            accountNameCodeArray = new String[1];
            accountNameCodeArray[0] = getString(R.string.payeraccount);
            accountCodeArray = new String[1];
            accountCodeArray[0] = getString(R.string.payeraccount);
        }

        operationCodeArray = getResources().getStringArray(R.array.TxnType);  // Opration Type Static > Cash in,CashOut,balance,Cashhom,Remtsend,,CreateAgent

        ArrayAdapter<String> arrayAdapterAccountName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountNameCodeArray); //selected item will look like a spinner set from XML
        arrayAdapterAccountName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerAccountName.setAdapter(arrayAdapterAccountName);       //  Account Name Dynamically Data Save

        SpinnerOperationType.setOnItemSelectedListener(this);       //     OnItemSelectedListener
        SpinnerAccountName.setOnItemSelectedListener(this);        //     OnItemSelectedListener
    }

    private boolean validateDetailsBalanceSendReceivedCreateAgent() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            if (SpinnerOperationType.getSelectedItemPosition() > 0) {
                spinnerOprationTypeString = operationCodeArray[SpinnerOperationType.getSelectedItemPosition()];

                if (SpinnerAccountName.getSelectedItemPosition() > 0) {
                    spinnerAccountTypeString = accountCodeArray[SpinnerAccountName.getSelectedItemPosition()];

                    amountString = amountEditText.getText().toString().trim();
                    if (amountString.length() > 0 && validateAmount(amountString)) {
                        mpinString = mpinEditText.getText().toString().trim();
                        if (mpinString.length() == 4) {

                            ret = true;

                        } else {
                            mpinEditText.requestFocus();
                            Toast.makeText(Tariff.this, getString(R.string.pleaseEnterMpin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        amountEditText.requestFocus();
                        Toast.makeText(Tariff.this, getString(R.string.pleaseenteramount), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SpinnerAccountName.requestFocus();
                    Toast.makeText(Tariff.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Tariff.this, getString(R.string.PleaseSelectOperationType), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Tariff.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }

        return ret;
    }

    public void validateDetails() {
        if (isCashInCashOutClick) {     // if  click tariff  Cash In Cash Out Only
            if (validateDetailsCashInCashOut()) {
                proceedTariffCashInCashOut();
            }
        } else {
            if (validateDetailsBalanceSendReceivedCreateAgent()) {
                proceedTariffDetailsBalanceSendReceivedCreateAgent();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                validateDetails();
                break;
        }
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

    void proceedTariffDetailsBalanceSendReceivedCreateAgent() {
        if (new InternetCheck().isConnected(Tariff.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffDataBalanceSendReceivedCreateAgent();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            new ServerTask(mComponentInfo, Tariff.this, mHandler, requestData, "getTarifInJSON", 114).start();
        } else {
            Toast.makeText(Tariff.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateTariffDataBalanceSendReceivedCreateAgent() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

           */
/* {"agentCode":"237675488392","pin":"E399FD9F652DA0E4248A1AF0F3A9EDC2",
            "amount":"544534","transtype":"CASHIN","pintype":"MPIN",
            "vendorcode":"MICR","clienttype":"GPRS","fromcity":"YDE","tocity":"YDE","comments":
             "NOSMS","udv1":"SAMEBRANCH","accountType":"MA"}
        *//*


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("amount", amountString);
            countryObj.put("transtype", spinnerOprationTypeString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("fromcity", "YDE");
            countryObj.put("tocity", "YDE");
            countryObj.put("comments", "NOSMS");
            countryObj.put("udv1", "SAMEBRANCH");
            countryObj.put("accountType", spinnerAccountTypeString);
            //  countryObj.put("billerCode", "");
            //  countryObj.put("destination", billerCodeString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private String generateTariffDataCashInCashOut() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

           */
/* {"agentCode":"237675488392","pin":"E399FD9F652DA0E4248A1AF0F3A9EDC2",
            "amount":"544534","transtype":"CASHIN","pintype":"MPIN",
            "vendorcode":"MICR","clienttype":"GPRS","fromcity":"YDE","tocity":"YDE","comments":
             "NOSMS","udv1":"SAMEBRANCH","accountType":"MA"}
        *//*


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("amount", amountString);
            countryObj.put("transtype", spinnerOprationTypeString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            // countryObj.put("fromcity", "YDE");   //  change from server
            //  countryObj.put("tocity", "YDE");     //  Change from Server
            countryObj.put("comments", "NOSMS");
            countryObj.put("udv1", "SAMEBRANCH");
            countryObj.put("accountType", spinnerAccountTypeString);

            countryObj.put("destination", destinationString);


            //  countryObj.put("billerCode", "");
            //  countryObj.put("destination", billerCodeString);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    void proceedTariffCashInCashOut() {
        if (new InternetCheck().isConnected(Tariff.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffDataCashInCashOut();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            new ServerTask(mComponentInfo, Tariff.this, mHandler, requestData, "getTarifInJSON", 114).start();
        } else {
            Toast.makeText(Tariff.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateDetailsCashInCashOut() {
        boolean ret = false;

        if (SpinnerOperationType.getSelectedItemPosition() > 0) {
            spinnerOprationTypeString = operationCodeArray[SpinnerOperationType.getSelectedItemPosition()];

            if (SpinnerAccountName.getSelectedItemPosition() > 0) {
                spinnerAccountTypeString = accountCodeArray[SpinnerAccountName.getSelectedItemPosition()];

                destinationString = destinationEditText.getText().toString().trim();
                if (destinationString.length() > 0) {

                    amountString = amountEditText.getText().toString().trim();
                    if (amountString.length() > 0 && validateAmount(amountString)) {
                        mpinString = mpinEditText.getText().toString().trim();
                        if (mpinString.length() == 4) {

                            ret = true;


                            destinationString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + destinationString;


                        } else {
                            mpinEditText.requestFocus();
                            Toast.makeText(Tariff.this, getString(R.string.pleaseEnterMpin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        amountEditText.requestFocus();
                        Toast.makeText(Tariff.this, getString(R.string.pleaseenteramount), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    amountEditText.requestFocus();
                    Toast.makeText(Tariff.this, getString(R.string.pleaseEnterDestination), Toast.LENGTH_SHORT).show();
                }
            } else {
                SpinnerAccountName.requestFocus();
                Toast.makeText(Tariff.this, getString(R.string.PleaseSelectAccountType), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Tariff.this, getString(R.string.PleaseSelectOperationType), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(Tariff.this);
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

    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                destinationEditText.setText("");
                destinationEditText.setHint(getString(R.string.hintDestinationMobileNo));
                // destinationEditText.setFilters(null);
                destinationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]));
                destinationEditText.setHint(String.format(getString(R.string.hintDestinationMobileNo), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                destinationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                destinationEditText.setFilters(digitsfilters);
                destinationEditText.setText("");


            } else if (i == 2) {
                destinationEditText.setText("");
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
                                    'W', 'X', 'Y', 'Z', '.',
                                    '0', '1', '2', '3', '4', '5',
                                    '6', '7', '8', '9'

                            };

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
                destinationEditText.setHint(getString(R.string.enterMerchantMobileNo));
                destinationEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                destinationEditText.setFilters(digitsfilters);
                destinationEditText.setText("");
            }
        } else {
            Toast.makeText(Tariff.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());
            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(Tariff.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(Tariff.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread agent.thread = new DataParserThread(Tariff.this, mComponentInfo, Tariff.this, message.arg1, message.obj.toString());
                agent.thread.execute();
            }
        }
    };

    private void showSuccess(String data, int txnCase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Tariff.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.Tariff));
        String[] temp = data.split("\\|");


        builder.setMessage(String.format(getString(R.string.tariffSucess) + " " + temp[0] + "XAF"));
        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                Tariff.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 299:
                if (resultCode == RESULT_OK) {
                    String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");

                    if (new InternetCheck().isConnected(Tariff.this)) {
                        showProgressDialog(getString(R.string.pleasewait));
                        new ServerTask(mComponentInfo, Tariff.this, mHandler, requestData, "getTarifInJSON", 114).start();
                    } else {
                        Toast.makeText(Tariff.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }
                } else {
                }
                break;
        }
    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        if (generalResponseModel.getResponseCode() == 0) {
            if (requestNo == 114) {
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "TARIFF").commit();
                    Intent i = new Intent(Tariff.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString(), 1);
                }
            }
        } else {
            hideProgressDialog();
            Toast.makeText(Tariff.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }
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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Tariff.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)    //
    {
        String item = adapterView.getItemAtPosition(position).toString();    // On Selecting spinner item

        //  Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();

        try {
            switch (adapterView.getId()) {
                case R.id.SpinnerOperationType:   // Tariff Type

                    if (SpinnerOperationType.getSelectedItemPosition() == 0) {
                        destinationEditText.setVisibility(view.GONE);
                        destinationTextInputLayout.setVisibility(view.GONE);
                        isCashInCashOutClick = false;
                    } else if (item.equalsIgnoreCase("CASHIN")) {
                        destinationEditText.setVisibility(view.VISIBLE);
                        setInputType(1);
                        destinationTextInputLayout.setVisibility(view.VISIBLE);
                        isCashInCashOutClick = true;
                    } else if (item.equalsIgnoreCase("CASHOUT")) {
                        destinationEditText.setVisibility(view.VISIBLE);
                        setInputType(1);
                        destinationTextInputLayout.setVisibility(view.VISIBLE);
                        isCashInCashOutClick = true;
                    } else if (item.equalsIgnoreCase("BALANCE")) {
                        destinationEditText.setVisibility(view.GONE);
                        destinationTextInputLayout.setVisibility(view.GONE);
                        isCashInCashOutClick = false;
                    } else if (item.equalsIgnoreCase("CASHTOM")) {
                        destinationEditText.setVisibility(view.GONE);
                        destinationTextInputLayout.setVisibility(view.GONE);
                        isCashInCashOutClick = false;
                    } else if (item.equalsIgnoreCase("REMTSEND")) {
                        destinationEditText.setVisibility(view.GONE);
                        destinationTextInputLayout.setVisibility(view.GONE);
                        isCashInCashOutClick = false;
                    } else if (item.equalsIgnoreCase("CREATEAGENT")) {
                        destinationEditText.setVisibility(view.GONE);
                        destinationTextInputLayout.setVisibility(view.GONE);
                        isCashInCashOutClick = false;
                    } else if (item.equalsIgnoreCase("REMTRECV")) {
                        destinationEditText.setVisibility(view.GONE);
                        destinationTextInputLayout.setVisibility(view.GONE);
                        isCashInCashOutClick = false;
                    }

                    break;

                case R.id.spinner_AccountName:   // Account Name Type

                    if (SpinnerAccountName.getSelectedItemPosition() == 0) {
                        //  Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
*/

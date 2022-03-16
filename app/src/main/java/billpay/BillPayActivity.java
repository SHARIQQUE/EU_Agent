package billpay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.activities.OTPVerificationActivity;
import sucess_receipt.SucessReceiptBillPay;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by AdityaBugalia on 11/10/16.
 */

public class BillPayActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, ServerResponseParseCompletedNotifier, AdapterView.OnItemSelectedListener {

    Spinner billerCountrySpinner, billerNameSpinner, payerAccountsSpinner, billPreferenceSpinner, paymentModeSpinner, invoiceDetailSpinner;
    AutoCompleteTextView sourceMobileNumberEditText,invoiceNoEditText, customerIdEditText, payementCodeEditText, mpinEditText;
    String sourceMobileNumber,amountString,subscriberNameString, toCity, tariffAmountFee, billerCountryString = "", billerNameString = "", invoicenoString = "",
            customerIdString = "", paymentCodeString = "", payerAccountString = "", payerAccountCodeString = "",
            agentName = "", agentCode = "", mpinString = "", destinationString = "",
            accountNoString = "", countryCodeSelectionString = "", countrySelectionString = "", paymentModeString = "", billerCodeString = "";
    TextInputLayout sourceMobileNumber_TIL,customerIdEditText_TIL, invoiceNoEditText_TIL, paymentReferenceCodeEditText_TIL;
    String[] tempString,countryArray, countryCodeArray, countryPrefixArray,countryMobileNoLengthArray, billerNameArray, billerCodeArray, payerBankAccountsArray, payerBankAccountCodeArray, invoiceDetailArray;
    TextView payerDetailsIndicationTxtView,amountTextView;
    String errorMsgToDisplay = "";

    ComponentMd5SharedPre mComponentInfo;
    Toolbar mToolbar;
    Button nextButton;
    TextView sourceNumber_textview_reviewPage,tariffTextViewBillpay;

    boolean isReview = false,
            isServerOperationInProcess = false,

    isMultiMode = false;
    SharedPreferences.Editor editor;
    ArrayAdapter<String> spinnerArrayAdapter;
    Dialog successDialog;
    LinearLayout billerName_Review_LL, customerId_Review_LL, paymentMode_Review_LL, invoiceNo_Review_LL, destination_Review_LL;
    TextView billerCountry_TxtView_Review, billerName_TxtView_Review,
            billerPreference_TxtView_Review, customerId_TxtView_Review,
            paymentMode_TxtView_Review, invoiceNo_TxtView_Review,
            destination_TxtView_Review, payerAccount_TxtView_Review;
    LinearLayout input_LL, review_LL;
    int layoutLevel = 0;
    private ProgressDialog mDialog;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());

            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(BillPayActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(BillPayActivity.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(BillPayActivity.this, mComponentInfo, BillPayActivity.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
                // validateDetails();
                //validateMpin();
                validateDetails_NextButton();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        editor = mComponentInfo.getmSharedPreferences().edit();

        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "en";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.billpay_activity);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_BillPayment);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
       // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        mToolbar.setTitle(getString(R.string.billpayment_title));
        mToolbar.setSubtitle("" + agentName);

        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        try {

            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {

            Log.e("", "" + e.toString());
        }
        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            billerNameArray = mComponentInfo.getmSharedPreferences().getString("billerDetails", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");


        } catch (Exception e) {

            BillPayActivity.this.finish();
        }


        nextButton = (Button) findViewById(R.id.nextButton_Billpayment);

        nextButton.setOnClickListener(this);

        nextButton.setVisibility(View.VISIBLE);
        billerCountrySpinner = (Spinner) findViewById(R.id.spinner_BillerCountry_BillPayment);
        billerNameSpinner = (Spinner) findViewById(R.id.spinner_BillerName_BillPayment);
        payerAccountsSpinner = (Spinner) findViewById(R.id.spinner_PayerAccount_BillPayment);
        payerAccountsSpinner.setOnItemSelectedListener(this);
        billPreferenceSpinner = (Spinner) findViewById(R.id.spinner_BillPreference_BillPayment);
        billPreferenceSpinner.setOnItemSelectedListener(this);
        paymentModeSpinner = (Spinner) findViewById(R.id.spinner_PaymentMode_BillPayment);
        paymentModeSpinner.setOnItemSelectedListener(this);
        invoiceDetailSpinner = (Spinner) findViewById(R.id.spinner_InvoiceDetails_BillPayment);
        invoiceDetailSpinner.setOnItemSelectedListener(this);
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        billerCountrySpinner.setAdapter(adapter);

        /*spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billerNameArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billerNameSpinner.setAdapter(spinnerArrayAdapter);
        billerNameSpinner.setOnItemSelectedListener(this);*/

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        tariffTextViewBillpay = (TextView) findViewById(R.id.tariffTextViewBillpay);
        sourceNumber_textview_reviewPage= (TextView) findViewById(R.id.sourceNumber_textview_reviewPage);
        amountTextView=(TextView)findViewById(R.id.amountTextView);


        if (bankAccounts.trim().length() > 5) {
            // Looper.prepare();
            String[] data = bankAccounts.split("\\;");

            ArrayList<String> accountList = new ArrayList<String>();
            ArrayList<String> accountCodeList = new ArrayList<String>();
            accountList.add(getString(R.string.payeraccount));
            accountCodeList.add(getString(R.string.payeraccount));


            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    // if(tData[4].equalsIgnoreCase("MA")) {
                    String temMobileAccount=tData[1];
                    temMobileAccount=getString(R.string.mobileAccount);
                    accountList.add(tData[0] + " - " + temMobileAccount);

                    accountCodeList.add(tData[4]);
                    //    }
                }


            }
            payerBankAccountCodeArray = accountCodeList.toArray(new String[accountCodeList.size()]);
            payerBankAccountsArray = accountList.toArray(new String[accountList.size()]);
        } else {
            payerBankAccountsArray = new String[1];
            payerBankAccountsArray[0] = getString(R.string.payeraccount);

            payerBankAccountCodeArray = new String[1];
            payerBankAccountCodeArray[0] = getString(R.string.payeraccount);

        }


        customerIdEditText = (AutoCompleteTextView) findViewById(R.id.customerId_EditText_BillPayment);
        customerIdEditText.setOnEditorActionListener(this);
        invoiceNoEditText = (AutoCompleteTextView) findViewById(R.id.invoiceNo_EditText_BillPayment);
        invoiceNoEditText.setOnEditorActionListener(this);
        payementCodeEditText = (AutoCompleteTextView) findViewById(R.id.payementCode_EditText_BillPayment);


        customerIdEditText_TIL = (TextInputLayout) findViewById(R.id.customerId_EditText_BillPayment_TIL);
        sourceMobileNumber_TIL= (TextInputLayout) findViewById(R.id.sourceMobileNumber_TIL);
        invoiceNoEditText_TIL = (TextInputLayout) findViewById(R.id.invoiceNo_EditText_BillPayment_TIL);
        paymentReferenceCodeEditText_TIL = (TextInputLayout) findViewById(R.id.payementCode_EditText_BillPayment_TIL);

        sourceMobileNumberEditText = (AutoCompleteTextView) findViewById(R.id.sourceMobileNumberEditText);

       // payerAccountsSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));
        input_LL = (LinearLayout) findViewById(R.id.input_Recipient_LL_BillPayment);
        review_LL = (LinearLayout) findViewById(R.id.review_LL_BillPayment);


        payerDetailsIndicationTxtView = (TextView) findViewById(R.id.payerDetailsIndicationTxtView);



        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText__Review_BillPayment);
        mpinEditText.setOnEditorActionListener(this);

        mpinEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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




        billerName_Review_LL = (LinearLayout) findViewById(R.id.billerNameReview_LL);
        customerId_Review_LL = (LinearLayout) findViewById(R.id.customerIdReview_LL);
        paymentMode_Review_LL = (LinearLayout) findViewById(R.id.paymentModeReview_LL);
        invoiceNo_Review_LL = (LinearLayout) findViewById(R.id.invoiceNoReview_LL);
        destination_Review_LL = (LinearLayout) findViewById(R.id.destinationNoReview_LL);
        billerCountry_TxtView_Review = (TextView) findViewById(R.id.billerCountry_TxtView_Review_BillPayment);
        billerName_TxtView_Review = (TextView) findViewById(R.id.billerName_TxtView_Review_BillPayment);
        billerPreference_TxtView_Review = (TextView) findViewById(R.id.billPreference_TxtView_Review_BillPayment);
        customerId_TxtView_Review = (TextView) findViewById(R.id.customerId_TxtView_Review_BillPayment);
        paymentMode_TxtView_Review = (TextView) findViewById(R.id.paymentMode_TxtView_Review_BillPayment);
        invoiceNo_TxtView_Review = (TextView) findViewById(R.id.invoiceNo_TxtView_Review_BillPayment);

        destination_TxtView_Review = (TextView) findViewById(R.id.destinationNo_TxtView_Review_BillPayment);
        payerAccount_TxtView_Review = (TextView) findViewById(R.id.payerAccount_TxtView_Review_BillPayment);

        setLayout(layoutLevel);
        billerCountrySpinner.setSelection(getCountrySelection());
        billerCountrySpinner.setEnabled(true);
        billerNameSpinner.setVisibility(View.GONE);
        billPreferenceSpinner.setVisibility(View.GONE);
        billerCountrySpinner.setOnItemSelectedListener(this);
        //     startServerInteraction();


    }
    private void setInputType(int i) {

        if (billerCountrySpinner.getSelectedItemPosition() > 0) {
            if (i == 1) {
                sourceMobileNumberEditText.setText("");
                sourceMobileNumberEditText.setHint(getString(R.string.PleasEenterSourceMobileNumber));
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[billerCountrySpinner.getSelectedItemPosition()]));
                sourceMobileNumberEditText.setHint(String.format(getString(R.string.hinSourceNo),countryMobileNoLengthArray[billerCountrySpinner.getSelectedItemPosition()] + " "));
                sourceMobileNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                sourceMobileNumberEditText.setFilters(digitsfilters);
                sourceMobileNumberEditText.setText("");


            } else
            {
                sourceMobileNumberEditText.setText("");
                sourceMobileNumberEditText.setHint(getString(R.string.PleasEenterSourceMobileNumber));
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
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[billerCountrySpinner.getSelectedItemPosition()]));

                 sourceMobileNumberEditText.setHint(String.format(getString(R.string.hinSourceNo),countryMobileNoLengthArray[billerCountrySpinner.getSelectedItemPosition()] + " "));
                sourceMobileNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                sourceMobileNumberEditText.setFilters(digitsfilters);
                sourceMobileNumberEditText.setText("");

            }
        } else {
            Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    private boolean validateDetails() {
        boolean ret = false;

        switch (layoutLevel) {
            case 1:

                if (billerCountrySpinner.getSelectedItemPosition() > 0) {

                    if (billerNameSpinner.getSelectedItemPosition() > 0) {

                        if (validationSourceMobileNumber()) {

                            invoicenoString = invoiceNoEditText.getText().toString().trim();
                       if (invoicenoString.length() >= 2) {


                            paymentCodeString = payementCodeEditText.getText().toString().trim();


                                billerCountryString = billerCountrySpinner.getSelectedItem().toString();
                                billerNameString = billerNameSpinner.getSelectedItem().toString();
                             //   payerAccountString = payerAccountsSpinner.getSelectedItem().toString().split("\\-")[0];
                                payerAccountCodeString = payerBankAccountCodeArray[payerAccountsSpinner.getSelectedItemPosition()];


                                layoutLevel = 4;
                                setLayout(layoutLevel);
                           /* } else {
                                Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectaccounttobedebited), Toast.LENGTH_SHORT).show();
                            }*/



                        } else {
                            Toast.makeText(BillPayActivity.this, getString(R.string.pleaseenterbillno), Toast.LENGTH_SHORT).show();
                        }
                        } else {
                            Toast.makeText(BillPayActivity.this, errorMsgToDisplay, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectnameofbiller), Toast.LENGTH_SHORT).show();
                    }




                } else {
                    Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectcountryofbiller), Toast.LENGTH_SHORT).show();
                }


                break;


            case 2:

                if (billerCountrySpinner.getSelectedItemPosition() > 0) {
                    if (billerNameSpinner.getSelectedItemPosition() > 0) {
                        customerIdString = customerIdEditText.getText().toString().trim();
                        if (customerIdString.length() >= 2) {
                            int paymentModeSelection = paymentModeSpinner.getSelectedItemPosition();
                            if (paymentModeSelection > 0) {

                                if (paymentModeSelection == 1) {
                                    paymentModeString = "Pay All Bills";

                                 //   if (payerAccountsSpinner.getSelectedItemPosition() > 0) {

                                        billerCountryString = billerCountrySpinner.getSelectedItem().toString();
                                        billerNameString = billerNameSpinner.getSelectedItem().toString();
                                        payerAccountString = payerAccountsSpinner.getSelectedItem().toString().split("\\-")[0];


                                        payerAccountCodeString = payerBankAccountCodeArray[payerAccountsSpinner.getSelectedItemPosition()];

                                        layoutLevel = 5;
                                        setLayout(layoutLevel);
                                  /*  } else {
                                        Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectaccounttobedebited), Toast.LENGTH_SHORT).show();
                                    }
                          */

                                } else {
                                    paymentModeString = getString(R.string.PaySingleBill);
                                    if (invoiceDetailSpinner.getSelectedItemPosition() > 0) {
                                        paymentCodeString = payementCodeEditText.getText().toString().trim();

                                      //  if (payerAccountsSpinner.getSelectedItemPosition() > 0) {

                                            billerCountryString = billerCountrySpinner.getSelectedItem().toString();
                                            billerNameString = billerNameSpinner.getSelectedItem().toString();
                                            invoicenoString = invoiceDetailSpinner.getSelectedItem().toString();
                                            payerAccountString = payerAccountsSpinner.getSelectedItem().toString().split("\\-")[0];
                                            payerAccountCodeString = payerBankAccountCodeArray[payerAccountsSpinner.getSelectedItemPosition()];

                                            layoutLevel = 6;
                                            setLayout(layoutLevel);
                                        /*} else {
                                            Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectaccounttobedebited), Toast.LENGTH_SHORT).show();
                                        }*/


                                    } else {
                                        Toast.makeText(BillPayActivity.this, getString(R.string.PleaseSelectInvoice), Toast.LENGTH_SHORT).show();
                                    }


                                }


                            } else {
                                Toast.makeText(BillPayActivity.this, getString(R.string.PleaseSelectPaymentMode), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(BillPayActivity.this, getString(R.string.PleaseEnterCustomerId), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectnameofbiller), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectcountryofbiller), Toast.LENGTH_SHORT).show();
                }


                break;

            case 3:

                if (billerCountrySpinner.getSelectedItemPosition() > 0) {
                    if (billerNameSpinner.getSelectedItemPosition() > 0) {
                        customerIdString = customerIdEditText.getText().toString().trim();
                        if (customerIdString.length() > 5) {
                            int paymentModeSelection = paymentModeSpinner.getSelectedItemPosition();
                            if (paymentModeSelection > 0) {

                                if (paymentModeSelection == 1) {
                                    paymentCodeString = payementCodeEditText.getText().toString().trim();

                                  //  if (payerAccountsSpinner.getSelectedItemPosition() > 0) {
                                        billerCountryString = billerCountrySpinner.getSelectedItem().toString();
                                        billerNameString = billerNameSpinner.getSelectedItem().toString();
                                        payerAccountString = payerAccountsSpinner.getSelectedItem().toString().split("\\-")[0];
                                        payerAccountCodeString = payerBankAccountCodeArray[payerAccountsSpinner.getSelectedItemPosition()];

                                        layoutLevel = 5;
                                        setLayout(layoutLevel);
                                   /* } else {
                                        Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectaccounttobedebited), Toast.LENGTH_SHORT).show();
                                    }*/


                                } else {
                                    if (invoiceDetailSpinner.getSelectedItemPosition() > 0) {
                                        paymentCodeString = payementCodeEditText.getText().toString().trim();

                                      //  if (payerAccountsSpinner.getSelectedItemPosition() > 0) {

                                            billerCountryString = billerCountrySpinner.getSelectedItem().toString();
                                            billerNameString = billerNameSpinner.getSelectedItem().toString();
                                            invoicenoString = invoiceDetailSpinner.getSelectedItem().toString();
                                            payerAccountString = payerAccountsSpinner.getSelectedItem().toString().split("\\-")[0];
                                            payerAccountCodeString = payerBankAccountCodeArray[payerAccountsSpinner.getSelectedItemPosition()];

                                            layoutLevel = 6;
                                            setLayout(layoutLevel);
                                      /*  } else {
                                            Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectaccounttobedebited), Toast.LENGTH_SHORT).show();
                                        }*/


                                    } else {
                                        Toast.makeText(BillPayActivity.this, getString(R.string.PleaseSelectInvoice), Toast.LENGTH_SHORT).show();
                                    }


                                }


                            } else {
                                Toast.makeText(BillPayActivity.this, getString(R.string.PleaseSelectPaymentMode), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(BillPayActivity.this, "Please Enter Customer Id", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BillPayActivity.this, getString(R.string.PleaseEnterCustomerId), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectcountryofbiller), Toast.LENGTH_SHORT).show();
                }


                break;


            case 4:
              /*  if (mpinEditText.getText().toString().trim().length() == 4) {

                    mpinString = mpinEditText.getText().toString().trim();
                    ret = true;
                } else {
                    Toast.makeText(BillPayActivity.this, "Please Enter mPin", Toast.LENGTH_SHORT).show();
                }*/
                if (validateMpin()) {
                    ret = true;
                } else {
                    ret = false;
                }

                break;
            case 5:
              /*  if (mpinEditText.getText().toString().trim().length() == 4) {

                    mpinString = mpinEditText.getText().toString().trim();
                    ret = true;
                } else {
                    Toast.makeText(BillPayActivity.this, "Please Enter mPin", Toast.LENGTH_SHORT).show();
                }
*/
                if (validateMpin()) {
                    ret = true;
                } else {
                    ret = false;
                }

                break;
            case 6:
               /* if (mpinEditText.getText().toString().trim().length() == 4) {

                    mpinString = mpinEditText.getText().toString().trim();
                    ret = true;
                } else {
                    Toast.makeText(BillPayActivity.this, "Please Enter mPin", Toast.LENGTH_SHORT).show();
                }
*/
                if (validateMpin()) {
                    ret = true;
                } else {
                    ret = false;
                }

                break;

        }


        return ret;
    }

    public boolean validateMpin() {
        boolean ret = false;
        if (mpinEditText.getText().toString().trim().length() == 4) {

            mpinString = mpinEditText.getText().toString().trim();
            ret = true;
        } else {
            Toast.makeText(BillPayActivity.this, getString(R.string.mpinAccountBalance), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetails_NextButton() {
        if (isMultiMode) {
            proceedMultiMode();

        } else {
            if (validateDetails()) {

                proceedBillPay();

            }
        }
    }

  //  proceedBillerInfo  getBillerInfoJSON  call

    private void proceedBillerInfo(){

        if (new InternetCheck().isConnected(BillPayActivity.this)) {
            showProgressDialog("Please Wait");
            String requestData = generateBillerInfoData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            isServerOperationInProcess = true;

        //    callApi("getBillerInfoJSON",requestData,158);

            new ServerTask(mComponentInfo, BillPayActivity.this, mHandler, requestData, "getBillerInfoJSON", 158).start();

        } else {
            Toast.makeText(BillPayActivity.this, "Please check Internet", Toast.LENGTH_LONG).show();
        }
    }
    private String generateBillerInfoData() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);
        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("agentname", agentName);
            countryObj.put("pin", pin);
            //countryObj.put("source", sourceMobileNumber);  // tag add 3 november
            countryObj.put("comments", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("invoiceno", invoicenoString);
            countryObj.put("enelid", "");
            countryObj.put("billertype", "");
            countryObj.put("amount", "");
            // countryObj.put("billercustid", "");
            countryObj.put("destination",billerCodeString);   // remove hardcode  // replace billerCodeString   237100004001
            countryObj.put("acountType","MA");
           // countryObj.put("transtype", "BILLPAY");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }


        return jsonString;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_Billpayment:

                validateDetails_NextButton();
               /* if (!isReview) {
                    if (validateDetails_PartI()) {
                        showReview();
                    }

                } else {
                    if (validateDetails_PartII()) {
                        proceedBillPay();
                    }

                }*/
                break;

        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(BillPayActivity.this);
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

    private void proceedBillPay() {

        if (new InternetCheck().isConnected(BillPayActivity.this)) {
            showProgressDialog("Please Wait");
            String requestData = generateBillPayData();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

         //   callApi("getBillPayTransactionInJSON",requestData,109);

            new ServerTask(mComponentInfo, BillPayActivity.this, mHandler, requestData, "getBillPayTransactionInJSON", 109).start();
        } else {
            Toast.makeText(BillPayActivity.this, "Please check Internet", Toast.LENGTH_LONG).show();
        }

    }

    private String generateTariffAmmount() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("destination", "subscriberNumberString");
            countryObj.put("amount", "amountString");
            countryObj.put("transtype", "CASHIN");
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");

            String fromCity = mComponentInfo.getmSharedPreferences().getString("state", "");

            countryObj.put("fromcity", fromCity);   //  change from server
            countryObj.put("tocity", billerCountryString);     //  Change from Server
            countryObj.put("comments", "commnet");
            countryObj.put("udv1", "");
            countryObj.put("accountType", "");
            //  countryObj.put("destination", nameNumberString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

  /*  void proceedTariffAmount() {
        if (new InternetCheck().isConnected(BillPayActivity.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateTariffAmmount();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            new ServerTask(mComponentInfo, BillPayActivity.this, mHandler, requestData, "getTarifInJSON", 114).start();
        } else {
            Toast.makeText(BillPayActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {
                    if (new InternetCheck().isConnected(BillPayActivity.this)) {
                        showProgressDialog("Please Wait");
                        String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");
                        isServerOperationInProcess = true;

                    //    callApi("getBillPayTransactionInJSON",requestData,109);

                        new ServerTask(mComponentInfo, BillPayActivity.this, mHandler, requestData, "getBillPayTransactionInJSON", 109).start();
                    } else {
                        Toast.makeText(BillPayActivity.this, "Please check Internet", Toast.LENGTH_LONG).show();
                    }

                } else {


                }


                break;


        }
    }


    private void setLayout(int level) {
        switch (level) {

            case 0:
                input_LL.setVisibility(View.VISIBLE);
                review_LL.setVisibility(View.GONE);

                billPreferenceSpinner.setSelection(0);

                billerNameSpinner.setVisibility(View.VISIBLE);
                // billerNameSpinner.setSelection(0);

                customerIdEditText_TIL.setVisibility(View.GONE);
                customerIdEditText.setText(null);

                paymentModeSpinner.setVisibility(View.GONE);
                paymentModeSpinner.setSelection(0);

                invoiceDetailSpinner.setVisibility(View.GONE);
                invoiceDetailSpinner.setSelection(0);

                invoiceNoEditText_TIL.setVisibility(View.GONE);
                invoiceNoEditText.setText(null);
                paymentReferenceCodeEditText_TIL.setVisibility(View.GONE);
                payementCodeEditText.setText(null);

                payerAccountsSpinner.setVisibility(View.GONE);
                payerAccountsSpinner.setSelection(0);
                payerDetailsIndicationTxtView.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);


                billerName_Review_LL.setVisibility(View.GONE);
                customerId_Review_LL.setVisibility(View.GONE);
                paymentMode_Review_LL.setVisibility(View.VISIBLE);
                invoiceNo_Review_LL.setVisibility(View.GONE);


                billerCountry_TxtView_Review.setText("");
                billerName_TxtView_Review.setText("");
                billerPreference_TxtView_Review.setText("");
                customerId_TxtView_Review.setText("");

                paymentMode_TxtView_Review.setText("");
                invoiceNo_TxtView_Review.setText("");
                sourceMobileNumberEditText.setText("");
               // sourceMobileNumber_TIL.setVisibility(View.GONE);  // demo

              //  destination_TxtView_Review.setText("");
               // payerAccount_TxtView_Review.setText("");


                mpinEditText.setText("");
                nextButton.setText(getString(R.string.proceedNew));

                break;

            case 1:          // enter invoice

                billerNameSpinner.setVisibility(View.VISIBLE);
                customerIdEditText_TIL.setVisibility(View.GONE);
                paymentModeSpinner.setVisibility(View.GONE);
                invoiceDetailSpinner.setVisibility(View.GONE);
                invoiceNoEditText_TIL.setVisibility(View.VISIBLE);
                paymentReferenceCodeEditText_TIL.setVisibility(View.GONE);


              //  payerAccountsSpinner.setVisibility(View.VISIBLE);
             //   payerDetailsIndicationTxtView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setText(getString(R.string.proceedBillPay));

                break;

            case 2:   // enter customer id

                billerNameSpinner.setVisibility(View.VISIBLE);
                customerIdEditText_TIL.setVisibility(View.VISIBLE);
                paymentModeSpinner.setVisibility(View.GONE);
                invoiceDetailSpinner.setVisibility(View.GONE);
                invoiceDetailSpinner.setSelection(0);
                invoiceNoEditText_TIL.setVisibility(View.GONE);
                paymentReferenceCodeEditText_TIL.setVisibility(View.GONE);

                payerAccountsSpinner.setVisibility(View.GONE);
                payerDetailsIndicationTxtView.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setText(getString(R.string.getUnpaidBills));

                customerIdEditText.requestFocus();
                break;

            case 3:

                billerNameSpinner.setVisibility(View.VISIBLE);
                customerIdEditText_TIL.setVisibility(View.VISIBLE);
                paymentModeSpinner.setVisibility(View.VISIBLE);
                invoiceDetailSpinner.setVisibility(View.VISIBLE);
                invoiceNoEditText_TIL.setVisibility(View.GONE);
                paymentReferenceCodeEditText_TIL.setVisibility(View.GONE);

            //    payerAccountsSpinner.setVisibility(View.VISIBLE);
           //     payerDetailsIndicationTxtView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setText(getString(R.string.getUnpaidBills));
                customerIdEditText.requestFocus();
                break;


            case 4:   // confirmation layout , fees , amount

                proceedBillerInfo();


                input_LL.setVisibility(View.GONE);
                review_LL.setVisibility(View.VISIBLE);

                billerName_Review_LL.setVisibility(View.VISIBLE);
                customerId_Review_LL.setVisibility(View.GONE);
                paymentMode_Review_LL.setVisibility(View.GONE);
                invoiceNo_Review_LL.setVisibility(View.VISIBLE);
                destination_Review_LL.setVisibility(View.GONE);


                billerCountry_TxtView_Review.setText(countrySelectionString);
                billerName_TxtView_Review.setText(billerNameString);
                billerPreference_TxtView_Review.setText(getString(R.string.prompt_InvoiceNo_code));
                customerId_TxtView_Review.setText("");
                paymentMode_TxtView_Review.setText("");
                invoiceNo_TxtView_Review.setText(invoicenoString);
                nextButton.setText(getString(R.string.proceedInvoiceConformationpage));
                destination_TxtView_Review.setText("");
              //  payerAccount_TxtView_Review.setText(payerAccountString);

                break;
            case 5:
                input_LL.setVisibility(View.GONE);
                review_LL.setVisibility(View.VISIBLE);

                billerName_Review_LL.setVisibility(View.VISIBLE);
                customerId_Review_LL.setVisibility(View.VISIBLE);
                paymentMode_Review_LL.setVisibility(View.VISIBLE);
                invoiceNo_Review_LL.setVisibility(View.GONE);
                destination_Review_LL.setVisibility(View.VISIBLE);


                billerCountry_TxtView_Review.setText(countrySelectionString);
                billerName_TxtView_Review.setText(billerNameString);
                billerPreference_TxtView_Review.setText(getString(R.string.prompt_CustomerId));
                customerId_TxtView_Review.setText(customerIdString);

                paymentMode_TxtView_Review.setText(paymentModeString);
                invoiceNo_TxtView_Review.setText("");


                destination_TxtView_Review.setText(destinationString);
              //  payerAccount_TxtView_Review.setText(payerAccountString);

                break;


            case 6:
                input_LL.setVisibility(View.GONE);
                review_LL.setVisibility(View.VISIBLE);

                billerName_Review_LL.setVisibility(View.VISIBLE);
                customerId_Review_LL.setVisibility(View.VISIBLE);
                paymentMode_Review_LL.setVisibility(View.VISIBLE);
                invoiceNo_Review_LL.setVisibility(View.VISIBLE);
                destination_Review_LL.setVisibility(View.VISIBLE);


                billerCountry_TxtView_Review.setText(countrySelectionString);
                billerName_TxtView_Review.setText(billerNameString);
                billerPreference_TxtView_Review.setText(getString(R.string.prompt_CustomerId));
                customerId_TxtView_Review.setText(customerIdString);

                paymentMode_TxtView_Review.setText(paymentModeString);
                invoiceNo_TxtView_Review.setText(invoicenoString);


                destination_TxtView_Review.setText(destinationString);
              //  payerAccount_TxtView_Review.setText(payerAccountString);

                break;


        }


    }

    private String generateUpaidBillsData() {
        String jsonString = "";

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("billercustId", customerIdString);
            countryObj.put("destination", billerCodeString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);




          //  Toast.makeText(BillPayActivity.this, customerIdString, Toast.LENGTH_SHORT).show();



            jsonString = countryObj.toString();
            System.out.println("sharique--------"+jsonString);


        } catch (Exception e) {

        }


        return jsonString;
    }

    private String generateBillPayData() {
        String jsonString = "";


        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", sourceMobileNumber);
            countryObj.put("comments", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("invoiceno", invoicenoString);
            countryObj.put("billercustid", "");
            countryObj.put("destination", billerCodeString);   // remove hardcode  // replace billerCodeString   237100004001


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            countryObj.put("accountType", "MA");
            jsonString = countryObj.toString();

        } catch (Exception e) {

        }


        return jsonString;
    }


    private void showSuccess(String data, int txnCase) {


        String[] temp = data.split("\\|");

        System.out.print(temp);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();

        mComponentInfo.getmSharedPreferences().edit().putString("feesBillPay", tempString[1]).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("amountBillPay", tempString[2]).commit();

        Intent intent = new Intent(BillPayActivity.this, SucessReceiptBillPay.class);
        startActivity(intent);
        BillPayActivity.this.finish();



    }

    public void startServerInteraction(String data, String apiName, int requestNo) {


        showProgressDialog(getString(R.string.pleasewait));
        if (new InternetCheck().isConnected(BillPayActivity.this)) {
            new ServerTask(mComponentInfo, BillPayActivity.this, mHandler, data, apiName, requestNo).start();
        } else {

            Toast.makeText(BillPayActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

        }
    }

    String generateBillerData(String transType) {
        String jsonString = "";

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", mComponentInfo.getmSharedPreferences().getString("agentCode", ""));
            countryObj.put("pin", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            //  countryObj.put("transtype", "PREPAIDELECTRICITY");
            countryObj.put("transtype", transType);
            countryObj.put("udv2", "BILLERCODEREQUIRED");
            countryObj.put("country", countryCodeSelectionString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();



        } catch (Exception e) {

        }

        return jsonString;

    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(BillPayActivity.this,mComponentInfo,BillPayActivity.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(BillPayActivity.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(BillPayActivity.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        isServerOperationInProcess = false;
        hideProgressDialog();
        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 109) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("accountTypeCodeString", payerAccountCodeString).commit();

                    mComponentInfo.getmSharedPreferences().edit().putString("process", "BILLPAY").commit();
                    Intent i = new Intent(BillPayActivity.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString(), 1);
                }


            } else if (requestNo == 119) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("accountTypeCodeString", payerAccountCodeString).commit();

                    mComponentInfo.getmSharedPreferences().edit().putString("process", "BILLPAY").commit();
                    Intent i = new Intent(BillPayActivity.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    billerCodeString = generalResponseModel.getUserDefinedString();
                    mComponentInfo.getmSharedPreferences().edit().putString("billerCode", billerCodeString).commit();
                    billPreferenceSpinner.setVisibility(View.VISIBLE);
                }


            }


            else if (requestNo == 106) {

                if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Fee Not Configured"))
                {
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if (generalResponseModel.getUserDefinedString().matches("Technical Failure"))   // from server
                {
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if (generalResponseModel.getUserDefinedString().matches("Adapter Not Available"))   // from server
                {
                    hideProgressDialog();
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }

                else if(generalResponseModel.getUserDefinedString().equalsIgnoreCase("Entity General Error"))
                {
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }

                else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Please try Again later"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }

                else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Invalid PIN"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }

                else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Vendor Not Found"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if (generalResponseModel.getUserDefinedString().equalsIgnoreCase("Template Not Found"))  // from server
                {
                    hideProgressDialog();
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else if (generalResponseModel.getUserDefinedString().matches("Subscriber/Agent Not Found"))   // from server
                {
                    hideProgressDialog();
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
                else {

                    hideProgressDialog();
                    editor.putString("BILLPAY_billerDetails" + countryCodeSelectionString, getString(R.string.pleaseselectbiller) + generalResponseModel.getUserDefinedString().split("\\;")[0].trim() + "|" + getString(R.string.Others));
                    editor.putString("BILLPAY_billerCodeDetails" + countryCodeSelectionString, getString(R.string.pleaseselectbiller) + generalResponseModel.getUserDefinedString().split("\\;")[1].trim() + "|Others");
                    editor.commit();

                    try {

                        billerNameArray = mComponentInfo.getmSharedPreferences().getString("BILLPAY_billerDetails" + countryCodeSelectionString, "").split("\\|");
                        billerCodeArray = mComponentInfo.getmSharedPreferences().getString("BILLPAY_billerCodeDetails" + countryCodeSelectionString, "").split("\\|");

                        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billerNameArray); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        billerNameSpinner.setAdapter(spinnerArrayAdapter);
                        billerNameSpinner.setOnItemSelectedListener(this);
                        billerNameSpinner.setVisibility(View.VISIBLE);

                    } catch (Exception e) {

                        BillPayActivity.this.finish();
                    }

                }
            }

            else if (requestNo == 158) {
                tempString = generalResponseModel.getUserDefinedString().split("\\|");
                tariffTextViewBillpay.setText(tempString[1]);
                amountTextView.setText(tempString[2]);
                sourceNumber_textview_reviewPage.setText(sourceMobileNumber);

                //   showAccToCashReview(tariffAmountFee);
            }

            else if (requestNo == 116) {
                if (generalResponseModel.getUserDefinedString().trim().length() != 0) {

                    hideProgressDialog();
                    mComponentInfo.getmSharedPreferences().edit().putString("unPaidBills", generalResponseModel.getUserDefinedString()).commit();

                    Intent i = new Intent(BillPayActivity.this, BillSelectionCustomerId.class);
                    startActivity(i);

//                String[] tempone=   generalResponseModel.getUserDefinedString().trim().split("\\;");
//                destinationString=tempone[1];
//                String[] temp=tempone[0].split("\\|");
//                invoiceDetailArray=new String[temp.length+1];
//                invoiceDetailArray[0]="Please select Invoice No";
//                for(int i=0;i<temp.length;i++){
//                    invoiceDetailArray[i+1]=temp[i];
//                }
//
//                spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, invoiceDetailArray); //selected item will look like a spinner set from XML
//                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                hideProgressDialog();
//
//                BillPayActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        invoiceDetailSpinner.setAdapter(spinnerArrayAdapter);
//                        setLayout(3);
//                    }
//                });


                } else {

                    hideProgressDialog();
                    billerNameSpinner.setSelection(0);
                    billPreferenceSpinner.setSelection(0);
                    billPreferenceSpinner.setVisibility(View.GONE);
                    Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

                }
            }


        } else {
            setLayout(layoutLevel = 0);
            hideProgressDialog();
            Toast.makeText(BillPayActivity.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            if (!isServerOperationInProcess) {
                BillPayActivity.this.finish();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    String[] text = new String[2];
    public static byte[] resultByteArrayImage;
    public static byte[] resultByteArraySign;
    public static byte[][] bytesArray = new byte[2][2];
    InputStream inputImage, inputSign;

    public String[] getStreamFromResponse(String imageBinary, String signBinary) {
        try {
            inputImage = new ByteArrayInputStream(imageBinary.getBytes());
            inputSign = new ByteArrayInputStream(signBinary.getBytes());

            int sizeImageInput = inputImage.available();
            int sizeSignInput = inputSign.available();

            byte[] bufferImage = new byte[sizeImageInput];
            byte[] bufferSign = new byte[sizeSignInput];

            inputImage.read(bufferImage);
            inputImage.close();

            inputSign.read(bufferSign);
            inputSign.close();

            // byte buffer into a string
            text[0] = new String(bufferImage);
            text[1] = new String(bufferSign);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }

    public static byte[] getByteByStringSign(String binaryStringArrayImageSign) {
        int splitSize = 8;
        int index = 0;
        int position = 0;
        int i = 0;
        if (binaryStringArrayImageSign.length() % splitSize == 0) {
            // resultByteArraySign = new byte[binaryStringArrayImageSign[1].length() / splitSize];
            resultByteArraySign = new byte[binaryStringArrayImageSign.length() / splitSize];
            StringBuilder text = new StringBuilder(binaryStringArrayImageSign);
            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArraySign[position] = byteAsInt.byteValue();
                index += splitSize;
                position++;
            }
            return resultByteArraySign;
        } else {
            System.out.println("Cannot convert binary string to byte[], because of the input length. '" + binaryStringArrayImageSign + "' % 8 != 0");
            return null;
        }

    }


    private void proceedMultiMode() {
        customerIdString = customerIdEditText.getText().toString();

        if (billerNameSpinner.getSelectedItemPosition() > 0) {

            if (validationSourceMobileNumber()) {

            if (customerIdString.trim().length() >= 2) {


                    if (new InternetCheck().isConnected(BillPayActivity.this)) {
                        showProgressDialog(getString(R.string.PleaseWaitGettingtheUnpaidInvoices));

                        String requestData = generateUpaidBillsData();

                        billerNameString = billerNameSpinner.getSelectedItem().toString();
                        countrySelectionString = billerCountrySpinner.getSelectedItem().toString();
                        String billSelectiondData = countrySelectionString + "|" + getString(R.string.prompt_CustomerId) + "|" + billerNameString + "|" + customerIdString;
                        mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
                        mComponentInfo.getmSharedPreferences().edit().putString("billSelectionData", billSelectiondData).commit();

                        isServerOperationInProcess = true;


                      //  callApi("getUnPaidBillerJSON",requestData,116);

                        new ServerTask(mComponentInfo, BillPayActivity.this, mHandler, requestData, "getUnPaidBillerJSON", 116).start();


                    } else {
                        Toast.makeText(BillPayActivity.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }
                }
          else {
            Toast.makeText(BillPayActivity.this, getString(R.string.PleaseEnterCustomerId), Toast.LENGTH_LONG).show();
        }

        }  else {
                Toast.makeText(BillPayActivity.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(BillPayActivity.this, getString(R.string.pleaseselectbiller), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validationSourceMobileNumber() {
        boolean ret = false;

           // int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            int lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[billerCountrySpinner.getSelectedItemPosition()]);

           int transferBasisposition = 1;

           sourceMobileNumber = sourceMobileNumberEditText.getText().toString().trim();
            errorMsgToDisplay = getString(R.string.sourceNumberSelectCountry) + " "+lengthToCheck +" "+ getString(R.string.digits);

            if (sourceMobileNumber.length() == lengthToCheck) {

                sourceMobileNumber = countryPrefixArray[billerCountrySpinner.getSelectedItemPosition()] + sourceMobileNumber;

                mComponentInfo.getmSharedPreferences().edit().putString("sourceMobileNumberBillPay", sourceMobileNumber).commit();


                errorMsgToDisplay = getString(R.string.hintReceipntNumberRecipient) + " "+lengthToCheck  +" "+ getString(R.string.digits);

                 ret = true;


            } else {
                Toast.makeText(BillPayActivity.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }


        return ret;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {

            case R.id.spinner_BillPreference_BillPayment:
                layoutLevel = i;


                if (i == 2) {
                    isMultiMode = true;
                } else {
                    isMultiMode = false;
                }


                setLayout(layoutLevel);
                break;


//            case R.id.spinner_PaymentMode_BillPayment:
//
//                if(i!=0) {
//                    proceedMultiMode();
//                }else {
//
//                        Toast.makeText(BillPayActivity.this, "Please Enter Customer Id", Toast.LENGTH_SHORT).show();
//
//
//                }
//
//                break;
//
//            case R.id.spinner_InvoiceDetails_BillPayment:
//
//                break;


            case R.id.spinner_BillerName_BillPayment:
                if (i == 0) {
                    billPreferenceSpinner.setSelection(0);

                    setLayout(layoutLevel = 0);

                }
                if (i != 0) {

                    billerNameString = billerNameSpinner.getSelectedItem().toString();
                    billerCodeString = billerCodeArray[i];

                    if (billerCodeString.equalsIgnoreCase("Others")) {

                        startServerInteraction(generateBillerData("BILLPAY"), "getBillerJSON", 106);
                    } else {
                        billPreferenceSpinner.setVisibility(View.VISIBLE);
                    }
                  //  startServerInteraction(generateBillerData("BILLPAY"), "getBillerJSON", 106);

                    //startServerInteraction(generateBillercodeData(),"billerCode",119);
                }

                break;
            case R.id.spinner_PayerAccount_BillPayment:
              /*  if(payerAccountsSpinner.getSelectedItem().toString() !="Mobile Account"){
                    Toast.makeText(this, "Please Select Mobile Account as Payer Account", Toast.LENGTH_LONG).show();
                    payerAccountsSpinner.setSelection(0);
                }*//*else{
                    if (amountEditText != null) {
                        amountEditText.requestFocus();
                    }
                }*/
                break;

            case R.id.spinner_BillerCountry_BillPayment:

                if (i != 0) {

                    setInputType(i);

                    countryCodeSelectionString = countryCodeArray[billerCountrySpinner.getSelectedItemPosition()];

                    if (mComponentInfo.getmSharedPreferences().getString("BILLPAY_billerDetails" + countryCodeSelectionString, "").trim().length() == 0) {

                        startServerInteraction(generateBillerData("BILLPAY"), "getBillerJSON", 106);

                    } else {
                        setBillers();
                    }
                }

                break;

        }

    }


    private void setBillers() {
        try {

            billerNameArray = mComponentInfo.getmSharedPreferences().getString("BILLPAY_billerDetails" + countryCodeSelectionString, "").split("\\|");
            billerCodeArray = mComponentInfo.getmSharedPreferences().getString("BILLPAY_billerCodeDetails" + countryCodeSelectionString, "").split("\\|");

            billerNameArray[0]=getString(R.string.SelectBillerName);

            spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, billerNameArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            billerNameSpinner.setAdapter(spinnerArrayAdapter);
            billerNameSpinner.setOnItemSelectedListener(this);
            billerNameSpinner.setVisibility(View.VISIBLE);

        } catch (Exception e) {

            BillPayActivity.this.finish();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


        switch (adapterView.getId()) {
            case R.id.spinner_BillPreference_BillPayment:
                billPreferenceSpinner.setSelection(0);
                setLayout(layoutLevel = 0);
                isMultiMode = false;
                break;


            case R.id.spinner_BillerName_BillPayment:
                billPreferenceSpinner.setSelection(0);
                setLayout(layoutLevel = 0);

                break;


        }
    }


    String generateBillercodeData() {

        String jsonString = "";
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);

            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("billername", billerNameString);
            jsonString = countryObj.toString();

        } catch (Exception e) {

        }

        return jsonString;


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


}

package agent.internationalremittance.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.CountryFlagAdapter;
import agent.activities.R;
import agent.internationalremittance.model.InternationalRemittanceModel;
import agent.internationalremittance.presenter.InternationPresenterCallback;
import agent.internationalremittance.presenter.InternationalRemittancePresenter;
import agent.internationalremittance.view.InternationalRemittanceViewCallaback;
import callback.TaskCompleteToPrint;
import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;
import printer_utilities.PrintUtils;
import printer_utilities.PrinterListShowActivity;
import printer_utilities.PrinterManager;


public class InternationalRemittanceActivity extends AppCompatActivity
        implements View.OnClickListener,
        InternationalRemittanceViewCallaback,
        AdapterView.OnItemSelectedListener, TaskCompleteToPrint {
    private Button proceedButton_IR;
    private TextView pagetitle_ir;
    Toolbar mToolbar;
    String printerAddress;
    private ProgressDialog dialog;
    private AutoCompleteTextView senderMobileNo_EditText,
            senderName_EditText, reciverMobileNo_EditText, reciverName_EditText, amount_EditText,
            referenceNo_EditText,
            mpin_EditText;
    AlertDialog.Builder successDialogBuilder;
    private Spinner countryNameSpinner, iroNameSpinner, currencySpinner;

    private TextView amountTextView_IR, txnType_Reciept, senderName_Reciept, senderNo_Reciept, reciverName_Reciept, reciverNo_Reciept, destinationCountry_Reciept, tokenNo_Reciept, currency_Reciept, txnId_Reciept, agentCode_Reciept, amount_Reciept;

    final ThreadLocal<InternationPresenterCallback> presenterCallback = new ThreadLocal<InternationPresenterCallback>();
    BottomNavigationView navigation;
    InternationalRemittancePresenter mPresenter;
    ComponentMd5SharedPre mComponentInfo;
    HashMap<String, String> map = new HashMap<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.remitsend:
                    mPresenter.functionalitySelected(true);

                    return true;
                case R.id.remitrecieve:
                    mPresenter.functionalitySelected(false);

                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        navigation.setSelectedItemId(R.id.remitsend);
        mPresenter.functionalitySelected(true);
        createSuccessLayout(false, null);

        updateAmountConverted("");
        amount_EditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mPresenter.onTextChanged(s, start, before, count);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        mPresenter = new InternationalRemittancePresenter(InternationalRemittanceActivity.this, mComponentInfo);
        pagetitle_ir = (TextView) findViewById(R.id.pageTitle_IR);


        senderMobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.senderMobileNoEditText_IR);
        senderName_EditText = (AutoCompleteTextView) findViewById(R.id.senderNameEditText_IR);
        reciverMobileNo_EditText = (AutoCompleteTextView) findViewById(R.id.reciverMobileNoEditText_IR);
        reciverName_EditText = (AutoCompleteTextView) findViewById(R.id.recieverNameEditText_IR);
        amount_EditText = (AutoCompleteTextView) findViewById(R.id.amountEditText_IR);
        referenceNo_EditText = (AutoCompleteTextView) findViewById(R.id.referenceNoEditText_IR);
        mpin_EditText = (AutoCompleteTextView) findViewById(R.id.mpinEditText_IR);
        amountTextView_IR = (TextView) findViewById(R.id.amountTextView_IR);
        proceedButton_IR = (Button) findViewById(R.id.proceedButton_IR);
        proceedButton_IR.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MoneyTransfer);


        countryNameSpinner = (Spinner) findViewById(R.id.spinner_countryName);
        iroNameSpinner = (Spinner) findViewById(R.id.spinner_IRO);
        currencySpinner = (Spinner) findViewById(R.id.spinner_Currency);

        currencySpinner.setOnItemSelectedListener(this);
        iroNameSpinner.setOnItemSelectedListener(this);
        countryNameSpinner.setOnItemSelectedListener(this);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }


    @Override
    public void setValuesLayout(HashMap<String, String> valuesLayout, String[] iroArr, String[] countryNamesArr, String[] currencyArr, boolean isRemitSend) {

        pagetitle_ir.setText(valuesLayout.get("pageTitle"));

        senderMobileNo_EditText.setText("");
        senderName_EditText.setText("");
        reciverMobileNo_EditText.setText("");
        reciverName_EditText.setText("");
        referenceNo_EditText.setText("");
        amount_EditText.setText("");
        mpin_EditText.setText("");

        if (isRemitSend) {

            referenceNo_EditText.setVisibility(View.INVISIBLE);

        } else {

            referenceNo_EditText.setVisibility(View.VISIBLE);
        }


        CountryFlagAdapter adapter = new CountryFlagAdapter(countryNamesArr, getResources(), getLayoutInflater());
        countryNameSpinner.setAdapter(adapter);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(InternationalRemittanceActivity.this, android.R.layout.simple_spinner_item, iroArr); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iroNameSpinner.setAdapter(spinnerArrayAdapter);

        spinnerArrayAdapter = new ArrayAdapter<String>(InternationalRemittanceActivity.this, android.R.layout.simple_spinner_item, currencyArr); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(spinnerArrayAdapter);

        mToolbar.setTitle("International Remittance");
        mToolbar.setSubtitle("" + valuesLayout.get("agentName"));
        //  mToolbar.setSubtitle(agentcode);
        // mToolbar.setSubtitle("Please provide details to proceed further");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
    }

    @Override
    public void updateAmountConverted(String message) {

        amountTextView_IR.setText(message);

    }

    @Override
    public void updateAmountHint(String message) {
        amount_EditText.setHint(message);
    }


    @Override
    public void onValidationFailed(String message) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(InternationalRemittanceActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void createSuccessLayout(boolean setValues, final HashMap<String, String> successValues) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (!setValues) {
            successDialogBuilder = new AlertDialog.Builder(InternationalRemittanceActivity.this);

            successDialogBuilder.setCancelable(true);

            View view = getLayoutInflater().inflate(R.layout.transaction_receipt_test, null);

            //successDialogBuilder.setTitle("International Remittance");


            txnType_Reciept = view.findViewById(R.id.txnType_Reciept);
            senderNo_Reciept = view.findViewById(R.id.senderNo_Reciept);
            senderName_Reciept = view.findViewById(R.id.senderName_Reciept);
            reciverNo_Reciept = view.findViewById(R.id.reciverNo_Reciept);
            reciverName_Reciept = view.findViewById(R.id.reciverName_Reciept);
            destinationCountry_Reciept = view.findViewById(R.id.destinationCountry_Reciept);
            currency_Reciept = view.findViewById(R.id.currency_Reciept);
            txnId_Reciept = view.findViewById(R.id.txnId_Reciept);
            amount_Reciept = view.findViewById(R.id.amount_Reciept);
            agentCode_Reciept = view.findViewById(R.id.agentCode_Reciept);
            tokenNo_Reciept = view.findViewById(R.id.tokenNo_Reciept);


            successDialogBuilder.setView(view);


            successDialogBuilder.setNegativeButton(
                    "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            senderMobileNo_EditText.setText("");
                            senderName_EditText.setText("");
                            reciverMobileNo_EditText.setText("");
                            reciverName_EditText.setText("");
                            referenceNo_EditText.setText("");
                            amount_EditText.setText("");
                            mpin_EditText.setText("");
                            dialog.cancel();
                        }
                    });

           /* successDialogBuilder.setPositiveButton(
                    "Print",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            senderMobileNo_EditText.setText("");
                            senderName_EditText.setText("");
                            reciverMobileNo_EditText.setText("");
                            reciverName_EditText.setText("");
                            referenceNo_EditText.setText("");
                            amount_EditText.setText("");
                            mpin_EditText.("");


                            if (askForPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH_PHONE)) {
                                if (askForPermission(Manifest.permission.BLUETOOTH_ADMIN, REQUEST_BLUETOOTH_PHONE_ADMIN)) {
                                    connect_to_Printer = new InternationalRemittanceActivity.ConnectToPrinter();
                                    connect_to_Printer.execute();
                                }
                            }

                            dialog.cancel();
                        }
                    });
*/

        } else {


            txnType_Reciept.setText(successValues.get("txnType_Reciept"));
            senderNo_Reciept.setText(successValues.get("senderNo_Reciept"));
            senderName_Reciept.setText(successValues.get("senderName_Reciept"));
            reciverNo_Reciept.setText(successValues.get("reciverNo_Reciept"));
            reciverName_Reciept.setText(successValues.get("reciverName_Reciept"));
            destinationCountry_Reciept.setText(successValues.get("destinationCountry_Reciept"));
            currency_Reciept.setText(successValues.get("currency_Reciept"));
            txnId_Reciept.setText(successValues.get("txnId_Reciept"));
            amount_Reciept.setText(successValues.get("amount_Reciept"));
            agentCode_Reciept.setText(successValues.get("agentCode_Reciept"));
            tokenNo_Reciept.setText(successValues.get("tokenNo_Reciept"));


        }

    }


    @Override
    public void onValidationSuccess(HashMap<String, String> successValues) {

        System.out.println();


        // message=String.format(message, senderName_EditText.getText().toString()+"( "+senderMobileNo_EditText.getText().toString()+")",reciverName_EditText.getText().toString()+"( "+reciverMobileNo_EditText.getText().toString()+")");
        createSuccessLayout(true, successValues);
        AlertDialog alert11 = successDialogBuilder.create();
        alert11.show();
    }

    @Override
    public void showProgress(String message) {
        dialog = new ProgressDialog(InternationalRemittanceActivity.this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mPresenter.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        map = new HashMap<>();


        map.put("senderMobileNo", senderMobileNo_EditText.getText().toString());
        map.put("senderName", senderName_EditText.getText().toString());
        map.put("reciverMobileNo", reciverMobileNo_EditText.getText().toString());
        map.put("reciverName", reciverName_EditText.getText().toString());
        map.put("amount", amount_EditText.getText().toString());
        map.put("mpin", mpin_EditText.getText().toString());
        map.put("referenceno", referenceNo_EditText.getText().toString());


        mPresenter.onClick(v, map);
    }


    // ########################################## BlueTooth  Printer ##########################################

    private Printer mPrinter;
    int a = 0;
    private PrinterManager mPrinterManager;
    private boolean doPrint = false;
    private BluetoothAdapter btAdapter;
    ConnectToPrinter connect_to_Printer;
    private ProgressDialog mDialog;
    private ProtocolAdapter mProtocolAdapter;
    ArrayList<String> responseListForPrinter_Header, responseListForPrinter_Body;
    private static final int REQUEST_BLUETOOTH_PHONE = 0;
    private static final int REQUEST_BLUETOOTH_PHONE_ADMIN = 1;
    private static final int REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED = 2;
    Bitmap bitMap;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CommonUtilities.REQUEST_ENABLE_BT:

                if (resultCode == Activity.RESULT_OK) {
                    if (doPrint == true) {
                        doPrint = true;
                        connect_to_Printer = new InternationalRemittanceActivity.ConnectToPrinter();
                        connect_to_Printer.execute();
                    } else {
                        // cp = new connectToPinpad();
                        // cp.execute();
                    }
                } else {
                    if (doPrint == true) {
                    } else {
                        finish();
                    }
                }
                break;

            case CommonUtilities.REQUEST_DEVICE:

                if (resultCode == Activity.RESULT_OK) {
                    // proceedAfterAmount();
                } else {
                    // ((Activity) ctx).finish();
                    // setInitialLayout();
                }
                break;

            case 200:
                if (resultCode == Activity.RESULT_OK) {
                    // md.setSignatureData(data.getStringExtra("signature"));
                    // rl1.setVisibility(View.GONE);
                    // rl2.setVisibility(View.GONE);
                    // rl3.setVisibility(View.GONE);
                    // rl4.setVisibility(View.GONE);
                    // rl5.setVisibility(View.GONE);
                    // rl6.setVisibility(View.VISIBLE);
                } else {
                    // rl5.setVisibility(View.VISIBLE);
                }
                break;

            case 300:
                if (resultCode == Activity.RESULT_OK) {
                    // doPrinting();
                    connect_to_Printer = new InternationalRemittanceActivity.ConnectToPrinter();
                    connect_to_Printer.execute();
                    // printerOptions.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    @Override
    public void onTaskCompleteToPrinter(ArrayList<String> result) {

    }

    private class ConnectToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            if (btAdapter != null) {
                if (btAdapter.isEnabled()) {
                    // commonDisplay.settaskCompleteNotifier(QuickSale.this);
                    // commonDisplay.showProgressDialog(getActivity(),
                    // "Connecting to Reader");
                    try {

                        if (printerAddress != null) {

                            if (a < 2) {

                                CommonUtilities.mDevice = btAdapter.getRemoteDevice(printerAddress);
                                // }

                                mPrinterManager = PrinterManager.getInstance(InternationalRemittanceActivity.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, InternationalRemittanceActivity.this);
                                mPrinter = mPrinterManager.initPrinter(mProtocolAdapter, mChannelListener, mPrinter);
                                status = 2;

                            } else {
                                status = 3;
                            }
                        } else {
                            status = 3;
                        }

                    } catch (Exception e) {
                        a++;
                        status = 3;
                        InternationalRemittanceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // commonDisplay.dismissProgressDialog();
                            }
                        });
                    }
                } else {
                    status = 1;
                }
            } else {
                status = 0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // commonDisplay.dismissProgressDialog();

            switch (status) {
                case 0:
                    hideProgressDialog();
                    Toast.makeText(InternationalRemittanceActivity.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    InternationalRemittanceActivity.PrintToPrinter printToPrinter = new InternationalRemittanceActivity.PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(InternationalRemittanceActivity.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
           // showProgress(getString(R.string.pleasewait));
            showProgressDialog(getString(R.string.pleasewait));


            SharedPreferences sp = InternationalRemittanceActivity.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
        }

    }

    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {


            PrintUtils mPrintUtils = new PrintUtils(InternationalRemittanceActivity.this, mPrinter, bitMap, InternationalRemittanceActivity.this);


            mPrintUtils.print_Demo_Map(map);

            mPrintUtils.print_Footer();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgressDialog(getString(R.string.pleasewait));
        }

    }

    private final ProtocolAdapter.ChannelListener mChannelListener = new ProtocolAdapter.ChannelListener() {
        @Override
        public void onLowBattery(boolean state) {
            // if (state) {
            // toast(getString(R.string.msg_low_battery));
            // }
        }

        @Override
        public void onOverHeated(boolean state) {
            // if (state) {
            // toast(getString(R.string.msg_overheated));
            // }
        }

        @Override
        public void onPaperReady(boolean state) {
            // if (state) {
            // toast(getString(R.string.msg_paper_ready));
            // } else {
            // toast(getString(R.string.msg_no_paper));
            // }
        }

        @Override
        public void onReadBarcode() {
            // readBarcode(0);
        }

        @Override
        public void onReadCard() {
            // readMagstripe();
        }

        @Override
        public void onReadEncryptedCard() {
            // toast(getString(R.string.msg_read_encrypted_card));
        }
    };

    private void showProgressDialog(String message) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new ProgressDialog(InternationalRemittanceActivity.this);
        try {
            mDialog.setMessage(message);
            mDialog.setCancelable(false);
            mDialog.setIndeterminate(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private boolean askForPermission(String permission, Integer requestCode) {
        boolean ret = false;
        if (ContextCompat.checkSelfPermission(InternationalRemittanceActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(InternationalRemittanceActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(InternationalRemittanceActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(InternationalRemittanceActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            ret = true;
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }

        return ret;
    }

    private boolean mayRequestPhoneState() {

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return true;
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_PRIVILEGED)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                    == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH)) {
                    //  requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE);
                } else {
                    //requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE);
                }
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_BLUETOOTH_PHONE);

            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                    == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_ADMIN)) {
                    //  requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_WIFI_PHONE);
                } else {
                }
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH_PHONE_ADMIN);
            }
            //            }else if(ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_PRIVILEGED)
            //                    == PackageManager.PERMISSION_DENIED){
            //                if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_PRIVILEGED)) {
            //                    //  requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_SMS_PHONE);
            //                }else {
            //
            //                }
            //                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED}, REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED);
            //
            //            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(InternationalRemittanceActivity.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_BLUETOOTH_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // if(mayRequestPhoneState()){
                    //   connect_to_Printer= new ConnectToPrinter();
                    //  connect_to_Printer.execute();
                    //    }
                    askForPermission(Manifest.permission.BLUETOOTH_ADMIN, REQUEST_BLUETOOTH_PHONE_ADMIN);

                } else {
                }
                break;
            case REQUEST_BLUETOOTH_PHONE_ADMIN:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //                    if(mayRequestPhoneState()){
                    //                        connect_to_Printer= new ConnectToPrinter();
                    //                        connect_to_Printer.execute();
                    //
                    //                    }

                } else {
                }
                break;

            case REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (mayRequestPhoneState()) {
                        connect_to_Printer = new InternationalRemittanceActivity.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }

    // ########################################## BlueTooth  Printer End ##########################################

}


package agent.report;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import adapter.ReportsAgentBankAdapter;
import agent.activities.OTPVerificationActivity;
import agent.activities.R;
import callback.DateSetNotifier;
import callback.ServerResponseParseCompletedNotifier;
import callback.TaskCompleteToPrint;
import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.DatePickerFragment;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import printer_utilities.PrintUtils;
import printer_utilities.PrinterListShowActivity;
import printer_utilities.PrinterManager;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


public class AgentBankReport extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, View.OnTouchListener, ServerResponseParseCompletedNotifier, DateSetNotifier, TaskCompleteToPrint {

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
                validateDetails_NextButton();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private ProtocolAdapter mProtocolAdapter;
    String readerAddress, printerAddress;
    private Printer mPrinter;
    Double totalFeesAdd;

    int hourSet1, minuteSet1,hourSet2,minuteSet2;
    String fromTimeSetString, toTimeSetString, amPmSelect1,amPmSelect2;
    Double feesAddTotal=0.0,tempDataFees;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button buttonTimeSetTo, buttonTimeSetFrom;
    int a = 0;
    private PrinterManager mPrinterManager;
    private Handler mPrinterConnectionHandler;
    private String printMessage;
    private boolean doPrint = false;
    private BluetoothAdapter btAdapter;
    AgentBankReport.ConnectToPrinter connect_to_Printer;
    String printdata;
    ArrayList<String> responseListForPrinter_Header, responseListForPrinter_Body;
    AlertDialog alertDialog;
    String printValues = null;
    private static final int REQUEST_BLUETOOTH_PHONE = 0;
    private static final int REQUEST_BLUETOOTH_PHONE_ADMIN = 1;
    private static final int REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED = 2;
    Bitmap bitMap;

    DatePickerDialog.OnDateSetListener ondate;
    DialogFragment newFragment;
    boolean bolleanSelectTime = false;
    int ifzerohour;
    public AutoCompleteTextView toDateEditText, fromDateEditText, mpinEditText;
    Spinner operationTypeSpinner, accountTypeSpinner;
    Button nextButton_MiniStatement;
    Toolbar mToolbar;
    TextView dateTextView, timeTextView, reportTypeTextView;
    LinearLayout linearlayout_printButton;
    ComponentMd5SharedPre mComponentInfo;

    ScrollView input_LL;
    LinearLayout review_LL;
    ListView listview_agentbankreport;
    ProgressDialog mDialog;
    Button printButton;
    TextView textView_toTimeSet, textView_totalamountRecepit,textView_fromtimeSet;

    private String[] payerBankAccountsArray, payerAccountCodeArray, operationCodeArray;
    String agentName, agentCode, operationTypeCodeString, operationTypeString, toDateString, fromDateString, mpinString, accountCodeString;

    int iLevel = 99;
    boolean isMiniStmtMode = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.agent_bank_report);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();


        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MiniReport);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.miniReport));

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


        reportTypeTextView = (TextView) findViewById(R.id.reportTypeTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);

        fromDateEditText = (AutoCompleteTextView) findViewById(R.id.fromDate_EditText_MiniReport);
        fromDateEditText.setInputType(InputType.TYPE_NULL);
        fromDateEditText.setOnTouchListener(this);

        printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(this);


        toDateEditText = (AutoCompleteTextView) findViewById(R.id.toDate_EditText_MiniReport);
        toDateEditText.setInputType(InputType.TYPE_NULL);
        toDateEditText.setOnTouchListener(this);

        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText_MiniReport);
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



        linearlayout_printButton = (LinearLayout) findViewById(R.id.linearlayout_printButton);


        operationTypeSpinner = (Spinner) findViewById(R.id.spinner_OperationType_MiniReport);
        accountTypeSpinner = (Spinner) findViewById(R.id.spinner_AccountType_MiniReport);

        nextButton_MiniStatement = (Button) findViewById(R.id.nextButton_MiniStatement);
        nextButton_MiniStatement.setOnClickListener(this);

        buttonTimeSetFrom = (Button) findViewById(R.id.buttonTimeSetFrom);
        buttonTimeSetFrom.setOnClickListener(this);

        buttonTimeSetTo = (Button) findViewById(R.id.buttonTimeSetTo);
        buttonTimeSetTo.setOnClickListener(this);

        textView_fromtimeSet = (TextView) findViewById(R.id.textView_fromtimeSet);
        textView_toTimeSet = (TextView) findViewById(R.id.textView_toTimeSet);
        textView_totalamountRecepit = (TextView) findViewById(R.id.textView_totalamountRecepit);


        input_LL = (ScrollView) findViewById(R.id.input_Recipient_SV_MiniReport);
        review_LL = (LinearLayout) findViewById(R.id.review_LL_MiniReport);

        listview_agentbankreport = (ListView) findViewById(R.id.listview_agentbankreport);

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 0) {
            String[] data = bankAccounts.split("\\;");
            payerBankAccountsArray = new String[data.length + 1];
            payerAccountCodeArray = new String[data.length + 1];
            payerBankAccountsArray[0] = getString(R.string.pleaseselectaccount);
            payerAccountCodeArray[0] = getString(R.string.pleaseselectaccount);

            for (int i = 0; i < data.length; i++) {

                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    payerBankAccountsArray[i + 1] = tData[0] + " - " + tData[1];
                    payerAccountCodeArray[i + 1] = tData[4];
                }


            }
        } else {
            payerBankAccountsArray = new String[1];
            payerAccountCodeArray = new String[1];
            payerBankAccountsArray[0] = getString(R.string.pleaseselectaccount);
            payerAccountCodeArray[0] = getString(R.string.pleaseselectaccount);
        }
        operationCodeArray = getResources().getStringArray(R.array.TxnTypeCodeMiniReports);
        // accountTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));

        try {
            // icon = BitmapFactory.decodeResource(getResources(), R.drawable.receipt_pic_printer);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.reciept_pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean validateDetails() {
        boolean ret = false;
        // if (operationTypeSpinner.getSelectedItemPosition() > 0) {

        fromDateString = fromDateEditText.getText().toString().trim();
        if (fromDateString.length() > 9) {

            bolleanSelectTime = false;

            System.out.println(hourSet1);
            System.out.println(minuteSet1);

            if (hourSet1>0 || minuteSet1>0) {


                toDateString = toDateEditText.getText().toString().trim();
                if (toDateString.length() > 9) {


                    if (hourSet2>0 || minuteSet2>0) {

                        if (isDateInOrder(fromDateString, toDateString)) {
                            mpinString = mpinEditText.getText().toString().trim();
                            if (mpinString.length() == 4) {

                                operationTypeCodeString = operationCodeArray[operationTypeSpinner.getSelectedItemPosition()];
                                operationTypeString = operationTypeSpinner.getSelectedItem().toString();

                            /*
                            fromDateString = fromDateString + " 00:00:00";
                            toDateString = toDateString + " 23:59:59";
                            */

                                bolleanSelectTime = true;

                                ret = true;

                            } else {
                                Toast.makeText(AgentBankReport.this, getString(R.string.prompt_mPin), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AgentBankReport.this, getString(R.string.pleaseentercorrectdates), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AgentBankReport.this, getString(R.string.selectToTime), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AgentBankReport.this, getString(R.string.pleaseentertodate), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AgentBankReport.this, getString(R.string.selectFromTime), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AgentBankReport.this, getString(R.string.pleaseenterfromdate), Toast.LENGTH_SHORT).show();
        }


        return ret;


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.fromDate_EditText_MiniReport:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 1;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");

                }
                break;

            case R.id.toDate_EditText_MiniReport:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 0;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

        }

        return false;
    }

    public void validateDetails_NextButton() {
        if (isMiniStmtMode) {
            isMiniStmtMode = false;
            mpinEditText.setText("");
            nextButton_MiniStatement.setText(getString(R.string.next));
            input_LL.setVisibility(View.VISIBLE);
            review_LL.setVisibility(View.GONE);
            linearlayout_printButton.setVisibility(View.GONE);

        } else {


            if (validateDetails()) {
                proceedMiniStmt();

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton_MiniStatement:

                validateDetails_NextButton();

                break;

            case R.id.printButton:

                printRequestProcessing();

                break;


            case R.id.buttonTimeSetFrom:

                timeFromSet();

                //  bolleanSelectTime=true;

                break;


            case R.id.buttonTimeSetTo:

                timeToSet();

                //  bolleanSelectTime=true;


                break;

        }


    }


    boolean timeFromSet() {

        bolleanSelectTime = false;

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR);
        mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if (hourOfDay > 12) {
                            amPmSelect1 = "PM";
                            hourSet1 = hourOfDay;

                        } else {
                            amPmSelect1 = "AM";
                            hourSet1 = hourOfDay;
                        }
                        bolleanSelectTime = true;

                        minuteSet1 = minute;

                        textView_fromtimeSet.setText(hourSet1 + ":" + minute+" "+amPmSelect1);

                        //  txtTime.setText(hourOfDay + ":" + minute);
                    }

                }, mHour, mMinute, false);

        timePickerDialog.show();

        return bolleanSelectTime;

    }

    boolean timeToSet() {

        bolleanSelectTime = false;

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if (hourOfDay > 12) {
                            amPmSelect2 = "PM";
                            hourSet2 = hourOfDay;

                        } else {
                            amPmSelect2 = "AM";
                            hourSet2 = hourOfDay;
                        }



                        minuteSet2 = minute;
                        textView_toTimeSet.setText(hourSet2 + ":" + minute+" "+amPmSelect2);

                        //  bolleanSelectTime = false;


                        //  txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();

        return bolleanSelectTime;
    }

    void printRequestProcessing() {
        if (askForPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH_PHONE)) {
            if (askForPermission(Manifest.permission.BLUETOOTH_ADMIN, REQUEST_BLUETOOTH_PHONE_ADMIN)) {
                connect_to_Printer = new AgentBankReport.ConnectToPrinter();
                connect_to_Printer.execute();
            }
        }
    }

    private int txnType = 0;

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

                                mPrinterManager = PrinterManager.getInstance(AgentBankReport.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, AgentBankReport.this);
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
                        AgentBankReport.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(AgentBankReport.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    AgentBankReport.PrintToPrinter printToPrinter = new AgentBankReport.PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(AgentBankReport.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = AgentBankReport.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
        }

    }


    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {


            PrintUtils mPrintUtils = new PrintUtils(AgentBankReport.this, mPrinter, bitMap, AgentBankReport.this);

            mPrintUtils.print_Header_Reports_agentBank(responseListForPrinter_Header);
            mPrintUtils.print_reportHistory_agentBankReportReport(responseListForPrinter_Body);
            //  mPrintUtils.print_reportHistory_feesAddTotal_list(responseListForPrinter_TotalFees);


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
        mDialog = new ProgressDialog(AgentBankReport.this);
        try {
            mDialog.setMessage(message);
            mDialog.setCancelable(false);
            mDialog.setIndeterminate(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CommonUtilities.REQUEST_ENABLE_BT:

                if (resultCode == Activity.RESULT_OK) {
                    if (doPrint == true) {
                        doPrint = true;
                        connect_to_Printer = new AgentBankReport.ConnectToPrinter();
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
                    connect_to_Printer = new AgentBankReport.ConnectToPrinter();
                    connect_to_Printer.execute();
                    // printerOptions.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    private void generateAndSetData() {

        // dataList values
        //1 header top line
        //2 date
        //3 time
        //4 txn id
        //5 txn type


        // dataList values
        //1 subscriber Number
        //2 subscriber name
        //3 Attach Branch name
        //4 city
        //5 country
        //6 Amount
        //7 Fees
        //8 Agent Name
        //9 Agent Country

        //  int a = recieptData.length;

        try {


        } catch (Exception r) {
            r.printStackTrace();
        }

    }

    private boolean askForPermission(String permission, Integer requestCode) {
        boolean ret = false;
        if (ContextCompat.checkSelfPermission(AgentBankReport.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AgentBankReport.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(AgentBankReport.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(AgentBankReport.this, new String[]{permission}, requestCode);
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
            Toast.makeText(AgentBankReport.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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
                        connect_to_Printer = new AgentBankReport.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }


    void proceedMiniStmt() {
        if (new InternetCheck().isConnected(AgentBankReport.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateAgentBankReport();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            // mComponentInfo.getmSharedPreferences().edit().putBoolean("isOtherAccount", accountCodeString.equalsIgnoreCase("MA") ? false : true).commit();

        //    callApi("getAgentBankMobileReport",requestData,171);
           new ServerTask(mComponentInfo, AgentBankReport.this, mHandler, requestData, "getAgentBankMobileReport", 171).start();
        } else {
            Toast.makeText(AgentBankReport.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(AgentBankReport.this,mComponentInfo,AgentBankReport.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(AgentBankReport.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(AgentBankReport.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }
    private String generateAgentBankReport() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        // String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {

            /*

              {
               "agentCode":"237000271099",
               "pin":"92E3C998FEE3FAF92F1D811E7E3F4BD5",
               "clienttype":"GPRS",
               "vendorcode":"MICR",
               "accountType":"MA",
               "pintype":"MPIN",
               "criteria":[
                  {
                     "datefrom":"26/12/2017 00:00:00 PM",
                     "dateto":"09/01/2018 23:59:59 PM",
                     "count":"1"
                  }
               ]
            }


            */



            JSONObject jObject1 = new JSONObject();

            jObject1.put("agentCode", agentCode);
            jObject1.put("pin", pin);
            jObject1.put("clienttype", "GPRS");
            jObject1.put("vendorcode", "MICR");
            jObject1.put("accountType", "MA");
            jObject1.put("pintype", "MPIN");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            jObject1.put(APK_NAME_VERSION, APP_VERSION_API);

            // 11/11/2010 2:45:37 PM

            String tempFromdatetime = fromDateString + " " + hourSet1 + ":" + minuteSet1 + ":37 " + amPmSelect1;
            String tempTodatetime = toDateString + " " + hourSet2 + ":" + minuteSet2 + ":10 " + amPmSelect2;

            JSONObject jObject2 = new JSONObject();

            //   jObject2.put("transType", "REMTSEND");
            jObject2.put("datefrom", tempFromdatetime);   //   fromDateString
            jObject2.put("dateto", tempTodatetime);   //     toDateString
            jObject2.put("count", "200");

            JSONArray jArray = new JSONArray();
            jArray.put(jObject2);
            jObject1.put("criteria", jArray);

            jsonString = jObject1.toString();

        } catch (Exception e) {
            System.out.println(jsonString);
        }


        return jsonString;
    }


    private void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

    }



    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {


            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(AgentBankReport.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(AgentBankReport.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(AgentBankReport.this, mComponentInfo, AgentBankReport.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {


                    String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");

                    if (new InternetCheck().isConnected(AgentBankReport.this)) {
                        showProgressDialog(getString(R.string.pleasewait));
                        new ServerTask(mComponentInfo, AgentBankReport.this, mHandler, requestData, "getTransactionHistory", 113).start();
                    } else {
                        Toast.makeText(AgentBankReport.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }

                } else {

                }

                break;


        }
    }*/


    @Override
    public void onParsingCompleted(final GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        try {
            if (generalResponseModel.getResponseCode() == 0) {

                if (requestNo == 171) {



                    if (generalResponseModel.isOTP()) {
                        mComponentInfo.getmSharedPreferences().edit().putString("process", "TRANSHISTORY").commit();
                        Intent i = new Intent(AgentBankReport.this, OTPVerificationActivity.class);
                        startActivityForResult(i, 299);
                    } else {

                        AgentBankReport.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (generalResponseModel.getCustomResponseList().size() > 0) {


                                    mComponentInfo.setTransHistoryData(generalResponseModel.getCustomResponseList());



                                    ReportsAgentBankAdapter adapter = new ReportsAgentBankAdapter(AgentBankReport.this, generalResponseModel.getCustomResponseList(), mComponentInfo.getmSharedPreferences().getBoolean("isOtherAccount", false));
                                    listview_agentbankreport.setAdapter(adapter);

                                    input_LL.setVisibility(View.GONE);
                                    review_LL.setVisibility(View.VISIBLE);

                                    // dateTextView
                                    // timeTextView

                                    Date date = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy hh:mm a");
                                    String dateTime = formatter.format(date);
                                    System.out.println(dateTime);

                                    String[] temDateTime = dateTime.split("\\ ");
                                    dateTextView.setText(temDateTime[0]);
                                    timeTextView.setText(temDateTime[1] + " " + temDateTime[2]);


                                    /////////////////////////////// RECEIPT HEADER - SHARIQUE/////////////

                                    responseListForPrinter_Header = new ArrayList<String>();
                                    responseListForPrinter_Header.add(0, getString(R.string.expressUnionFinance).toUpperCase());
                                    responseListForPrinter_Header.add(1, temDateTime[0]);
                                    responseListForPrinter_Header.add(2, temDateTime[1] + " " + temDateTime[2]);
                                    responseListForPrinter_Header.add(3, getString(R.string.reports));

                                    ///////////////////////////////////

                                    ///////////////////////////   RECEIPT BODY - SHARIQUE ////////////

                                    responseListForPrinter_Body = new ArrayList<String>();
                                    for (int k = 0; k < generalResponseModel.getCustomResponseList().size(); k++) {
                                        responseListForPrinter_Body.add(k, generalResponseModel.getCustomResponseList().get(k).toString());

                                    }
                                    //////////////////


                                    reportTypeTextView.setText(operationTypeString);
                                    linearlayout_printButton.setVisibility(View.VISIBLE);


                                    nextButton_MiniStatement.setText(getString(R.string.searchanother));

                                    isMiniStmtMode = true;
                                } else {

                                    Toast.makeText(AgentBankReport.this, getString(R.string.norecordfound), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }

                }
            } else {
                hideProgressDialog();
                Toast.makeText(AgentBankReport.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isDateInOrder(String startDate, String endDate) {
        boolean ret = false;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        Date date1;
        Date date2;
        try {

            date1 = simpleDateFormat.parse(startDate + " 11:59:59");
            date2 = simpleDateFormat.parse(endDate + " 00:00:01");


        } catch (ParseException e) {


            e.printStackTrace();
            return ret = false;
        }
        //milliseconds
        long different = date1.getTime() - date2.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        String daysTemp = (elapsedDays + "");
        if (daysTemp.contains("-")) {
            daysTemp = daysTemp.substring(1);
        }
        int days = Integer.parseInt(daysTemp);
        days++;
        if (7 > days) {

            ret = true;
        }

        return ret;
    }

    @Override
    public void onDateSet(final DatePicker var1, final String year, final String month, final String day) {
        AgentBankReport.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (iLevel) {

                    case 0:
                        toDateEditText.setText("" + day + "/" + month + "/" + year);
                        break;

                    case 1:
                        fromDateEditText.setText("" + day + "/" + month + "/" + year);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            AgentBankReport.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}

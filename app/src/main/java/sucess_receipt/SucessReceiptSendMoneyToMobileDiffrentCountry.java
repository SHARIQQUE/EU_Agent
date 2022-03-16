package sucess_receipt;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;

import java.util.ArrayList;
import java.util.Locale;

import agent.activities.R;
import callback.TaskCompleteToPrint;
import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;
import printer_utilities.PrintUtils;
import printer_utilities.PrinterListShowActivity;
import printer_utilities.PrinterManager;

/**
 * Created by Shariq on 15-06-2017.
 */


public class SucessReceiptSendMoneyToMobileDiffrentCountry extends AppCompatActivity implements TaskCompleteToPrint {
    TextView dateRecepit, timeReceipt,commentsTitleReview, totalamountRecepit, currencyTypeReceipt, destinationNameReceipt, destinationNumberReceipt, transactionCodeRecepit, transactionIdReceipt, senderNameReceipt, senderNumberReceipt, attachBranceNameReceipt, cityReceipt, destinationCountryRecepit, amountRecepit, feesRecepit, agentNameRecepit, agentCountryRecepit;
    Button homeButton;
    String[] recieptData;
    TextView oldTxnidTitleTextViewReceipt, oldTxnidTextViewReceipt, titleTextView;
    LinearLayout oldTxnIdLL;
    private int txnType = 0;
    Double feesNull,feesAdd,totalAmount,amountAdd;
    LinearLayout linearlayout_confcode;
    String showconfcode;
    int amountPrintbuttonhide;

    String countrySelection,SenderNameAgentIdentity,destinationNameAgentIdentity;
    ComponentMd5SharedPre mComponentInfo;
    String agentCode,commentString, destinationNoString, agentName, spinnerCurrenceyString;

    Bundle bundle = new Bundle();
    String subscriberNoDestinationNo;
    private ProgressDialog mDialog;
    private ProtocolAdapter mProtocolAdapter;
    String readerAddress, printerAddress;
    private Printer mPrinter;
    int a = 0;
    String totalAmountString;
    private PrinterManager mPrinterManager;
    private Handler mPrinterConnectionHandler;
    private String printMessage;
    private boolean doPrint = false;
    private BluetoothAdapter btAdapter;
    SucessReceiptSendMoneyToMobileDiffrentCountry.ConnectToPrinter connect_to_Printer;
    String printdata;
    ArrayList<String> responseListForPrinter_Header, responseListForPrinter_Body;
    AlertDialog alertDialog;
    String printValues = null;
    private static final int REQUEST_BLUETOOTH_PHONE = 0;
    private static final int REQUEST_BLUETOOTH_PHONE_ADMIN = 1;
    private static final int REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED = 2;
    Bitmap bitMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse=mComponentInfo.getmSharedPreferences().getString("languageToUse","");
        if(languageToUse.trim().length()==0){
            languageToUse="fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.sucess_receipt_sendmoney_different_country_tomobile);
        Toolbar mToolbar;
        Button printButton;
        dateRecepit = (TextView) findViewById(R.id.dateRecepit);
        timeReceipt = (TextView) findViewById(R.id.timeReceipt);
        transactionIdReceipt = (TextView) findViewById(R.id.transactionIdReceipt);

        senderNameReceipt = (TextView) findViewById(R.id.senderNameReceipt);
        senderNumberReceipt = (TextView) findViewById(R.id.senderNumberReceipt);
        attachBranceNameReceipt = (TextView) findViewById(R.id.attachBranceNameReceipt);
        cityReceipt = (TextView) findViewById(R.id.cityReceipt);
        destinationCountryRecepit = (TextView) findViewById(R.id.destinationCountryRecepit);
        amountRecepit = (TextView) findViewById(R.id.amountRecepit);
        feesRecepit = (TextView) findViewById(R.id.feesRecepit);
        agentNameRecepit = (TextView) findViewById(R.id.agentNameRecepit);
        agentCountryRecepit = (TextView) findViewById(R.id.agentCountryRecepit);
        destinationNameReceipt = (TextView) findViewById(R.id.destinationNameReceipt);
        destinationNumberReceipt = (TextView) findViewById(R.id.destinationNumberReceipt);
        currencyTypeReceipt = (TextView) findViewById(R.id.currencyTypeReceipt);
        totalamountRecepit = (TextView) findViewById(R.id.totalamountRecepit);
        transactionCodeRecepit = (TextView) findViewById(R.id.transactionCodeRecepit);
        commentsTitleReview = (TextView) findViewById(R.id.commentsTitleReview);
        linearlayout_confcode=(LinearLayout)findViewById(R.id.linearlayout_confcode);



        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_transactionReceiptCashiin);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.transactionReceipt));
        //mToolbar.setSubtitle("" + agentName);
        //  mToolbar.setSubtitle(agentcode);
        // mToolbar.setSubtitle("Please provide details to proceed further");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        try {

            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } catch (Exception e) {

            Log.e("", "" + e.toString());
        }

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        oldTxnidTitleTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTitleTextViewReceipt);
        oldTxnidTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTextViewReceipt);
        oldTxnIdLL = (LinearLayout) findViewById(R.id.oldTxnIdLL);

        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");

//        String aa = getIntent().getExtras().getString("data", "");
        String aa = mComponentInfo.getmSharedPreferences().getString("data", "");

        spinnerCurrenceyString = mComponentInfo.getmSharedPreferences().getString("spinnerCurrenceyString", "");
        countrySelection = mComponentInfo.getmSharedPreferences().getString("countrySelection", "");
        destinationNoString = mComponentInfo.getmSharedPreferences().getString("destinationNoString", "");


        SenderNameAgentIdentity = mComponentInfo.getmSharedPreferences().getString("senderNameERA", "");
        destinationNameAgentIdentity = mComponentInfo.getmSharedPreferences().getString("SenderDestinationERA", "");

        commentString = mComponentInfo.getmSharedPreferences().getString("commentString", "");
        amountPrintbuttonhide = mComponentInfo.getmSharedPreferences().getInt("amountPrintbuttonhide", amountPrintbuttonhide);



        if (aa.trim().length() > 0) {
            recieptData = aa.split("\\|");
            System.out.print(recieptData);
        } else {

        }

       /* try {
            if (recieptData.length > 16) {
                if (!recieptData[16].isEmpty() || !recieptData[16].equalsIgnoreCase("")) {
                    oldTxnIdLL.setVisibility(View.VISIBLE);
                    oldTxnidTextViewReceipt.setText(recieptData[16]);
                    titleTextView.setText("RE PRINT SEND MONEY");
                    txnType = 1;
                }

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
*/


        transactionIdReceipt.setText(recieptData[0]);


        String dateTimeSaperate = recieptData[3];
        String[] dateTime = dateTimeSaperate.split("\\ ");

        String date = dateTime[0];
        String time = dateTime[1];

        dateRecepit.setText(date);
        timeReceipt.setText(time);


        //  6574247|Transaction Successful|comments|19/06/2017 15:17:28|Cameroon|237971819684  //0 to 5
        //  |400.0|1500|AbangMinkoo|anwar|237000271011|sharique|EU YDE WARDA||LAN4   // 14

        destinationCountryRecepit.setText(countrySelection);



        if(recieptData[6].equalsIgnoreCase("null")) {
            feesNull = 0.0;

            feesRecepit.setText("0.0");
            amountRecepit.setText(recieptData[7]);
            totalamountRecepit.setText(recieptData[7]);
        }
        else
        {
            feesRecepit.setText(recieptData[6]);
            amountAdd = Double.parseDouble(recieptData[7]);
            feesAdd = Double.parseDouble(recieptData[6]);
            totalAmount = (amountAdd - feesAdd);


            totalAmountString = Double.toString(totalAmount);
            totalamountRecepit.setText(recieptData[7]);
            amountRecepit.setText(totalAmountString);
        }




        currencyTypeReceipt.setText(spinnerCurrenceyString);
        senderNameReceipt.setText(SenderNameAgentIdentity);
        senderNumberReceipt.setText(recieptData[15]);
        destinationNumberReceipt.setText(destinationNoString);
        destinationNameReceipt.setText(destinationNameAgentIdentity);
        transactionCodeRecepit.setText(recieptData[14]);
        attachBranceNameReceipt.setText(recieptData[12]);
        agentNameRecepit.setText(agentName);
        agentCountryRecepit.setText(recieptData[4]);
        commentsTitleReview.setText(commentString);

        showconfcode=recieptData[16];


        if(showconfcode.equalsIgnoreCase("1"))
        {
            generateAndSetData();
        }

        else {

            linearlayout_confcode.setVisibility(View.GONE);
            generateAndSetDataHide();
        }


        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SucessReceiptSendMoneyToMobileDiffrentCountry.this.finish();
            }
        });

        printButton = (Button) findViewById(R.id.printButton);

        if(amountPrintbuttonhide>1005000)    // change if greter than 100000 then print button hide
        {
            printButton.setVisibility(View.GONE);
        }

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mayRequestPhoneState();
                if (askForPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH_PHONE)) {
                    if (askForPermission(Manifest.permission.BLUETOOTH_ADMIN, REQUEST_BLUETOOTH_PHONE_ADMIN)) {
                        connect_to_Printer = new SucessReceiptSendMoneyToMobileDiffrentCountry.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                }
            }
        });

        try {
            // icon = BitmapFactory.decodeResource(getResources(), R.drawable.receipt_pic_printer);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.reciept_pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {

            PrintUtils mPrintUtils = new PrintUtils(SucessReceiptSendMoneyToMobileDiffrentCountry.this, mPrinter,bitMap,SucessReceiptSendMoneyToMobileDiffrentCountry.this);
            mPrintUtils.print_Header(responseListForPrinter_Header);

            if(showconfcode.equalsIgnoreCase("1"))
            {
                mPrintUtils.print_SendMoneyToMobile_diffrentCountryDisplayConfcode(responseListForPrinter_Body);
            }

            else {
                mPrintUtils.print_SendMoneyToMobile_diffrentCountryHide(responseListForPrinter_Body);
            }

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

                                mPrinterManager = PrinterManager.getInstance(SucessReceiptSendMoneyToMobileDiffrentCountry.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, SucessReceiptSendMoneyToMobileDiffrentCountry.this);
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
                        SucessReceiptSendMoneyToMobileDiffrentCountry.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(SucessReceiptSendMoneyToMobileDiffrentCountry.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    SucessReceiptSendMoneyToMobileDiffrentCountry.PrintToPrinter printToPrinter = new SucessReceiptSendMoneyToMobileDiffrentCountry.PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(SucessReceiptSendMoneyToMobileDiffrentCountry.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = SucessReceiptSendMoneyToMobileDiffrentCountry.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
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
        mDialog = new ProgressDialog(SucessReceiptSendMoneyToMobileDiffrentCountry.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CommonUtilities.REQUEST_ENABLE_BT:

                if (resultCode == Activity.RESULT_OK) {
                    if (doPrint == true) {
                        doPrint = true;
                        connect_to_Printer = new SucessReceiptSendMoneyToMobileDiffrentCountry.ConnectToPrinter();
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
                    connect_to_Printer = new SucessReceiptSendMoneyToMobileDiffrentCountry.ConnectToPrinter();
                    connect_to_Printer.execute();
                    // printerOptions.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    private void generateAndSetDataHide() {

        int a = recieptData.length;

        responseListForPrinter_Body = new ArrayList<String>();
        responseListForPrinter_Header = new ArrayList<String>();

        // Header

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, recieptData[3].split("\\ ")[0]);
        responseListForPrinter_Header.add(2, recieptData[3].split("\\ ")[1]);
        responseListForPrinter_Header.add(3, recieptData[0]);
        responseListForPrinter_Header.add(4, getString(R.string.sendMoneytomobileReceipt));


        responseListForPrinter_Body.add(0, SenderNameAgentIdentity);  // Sender name
        responseListForPrinter_Body.add(1, recieptData[15]);  // Sender Number
        responseListForPrinter_Body.add(2, countrySelection); //  Destination Country
        responseListForPrinter_Body.add(3, destinationNameAgentIdentity);   //  Destination name
        responseListForPrinter_Body.add(4, destinationNoString);  //  Destination number
        responseListForPrinter_Body.add(5, spinnerCurrenceyString);  //  currency Type Type


        if(recieptData[6].equalsIgnoreCase("null")) {
            recieptData[6] = "0.0";

            responseListForPrinter_Body.add(6, recieptData[7]);  // Amount
            responseListForPrinter_Body.add(7, recieptData[6]);  // fees
            responseListForPrinter_Body.add(8, recieptData[7]);  // Total Amount

        }
        else
        {

            responseListForPrinter_Body.add(6, totalAmountString);  // Amoount
            responseListForPrinter_Body.add(7, recieptData[6]);  // fees
            responseListForPrinter_Body.add(8, recieptData[7]);  // Total Amount

        }


        //  responseListForPrinter_Body.add(9, recieptData[14]); // Transaction Code
        responseListForPrinter_Body.add(9, agentName);   //  agent Name
        responseListForPrinter_Body.add(10, recieptData[12]);   // Agnet branch Name
        responseListForPrinter_Body.add(11, recieptData[4]);  // Agent Country
        responseListForPrinter_Body.add(12, commentString);  // comment




    }

    private void generateAndSetData() {

        int a = recieptData.length;

        responseListForPrinter_Body = new ArrayList<String>();
        responseListForPrinter_Header = new ArrayList<String>();

        // Header

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, recieptData[3].split("\\ ")[0]);
        responseListForPrinter_Header.add(2, recieptData[3].split("\\ ")[1]);
        responseListForPrinter_Header.add(3, recieptData[0]);
        responseListForPrinter_Header.add(4, getString(R.string.sendMoneytomobileReceipt));


        responseListForPrinter_Body.add(0, SenderNameAgentIdentity);  // Sender name
        responseListForPrinter_Body.add(1, recieptData[15]);  // Sender Number
        responseListForPrinter_Body.add(2, countrySelection); //  Destination Country
        responseListForPrinter_Body.add(3, destinationNameAgentIdentity);   //  Destination name
        responseListForPrinter_Body.add(4, destinationNoString);  //  Destination number
        responseListForPrinter_Body.add(5, spinnerCurrenceyString);  //  currency Type Type


        if(recieptData[6].equalsIgnoreCase("null")) {
            recieptData[6] = "0.0";

            responseListForPrinter_Body.add(6, recieptData[7]);  // Amount
            responseListForPrinter_Body.add(7, recieptData[6]);  // fees
            responseListForPrinter_Body.add(8, recieptData[7]);  // Total Amount

        }
        else
        {

            responseListForPrinter_Body.add(6, totalAmountString);  // Amoount
            responseListForPrinter_Body.add(7, recieptData[6]);  // fees
            responseListForPrinter_Body.add(8, recieptData[7]);  // Total Amount

        }


        responseListForPrinter_Body.add(9, recieptData[14]); // Transaction Code
        responseListForPrinter_Body.add(10, agentName);   //  agent Name
        responseListForPrinter_Body.add(11, recieptData[12]);   // Agnet branch Name
        responseListForPrinter_Body.add(12, recieptData[4]);  // Agent Country
        responseListForPrinter_Body.add(13, commentString);  // comment




    }

    private boolean askForPermission(String permission, Integer requestCode) {
        boolean ret = false;
        if (ContextCompat.checkSelfPermission(SucessReceiptSendMoneyToMobileDiffrentCountry.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SucessReceiptSendMoneyToMobileDiffrentCountry.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SucessReceiptSendMoneyToMobileDiffrentCountry.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SucessReceiptSendMoneyToMobileDiffrentCountry.this, new String[]{permission}, requestCode);
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
            Toast.makeText(SucessReceiptSendMoneyToMobileDiffrentCountry.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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
                        connect_to_Printer = new SucessReceiptSendMoneyToMobileDiffrentCountry.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }


}



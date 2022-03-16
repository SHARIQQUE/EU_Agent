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


public class SucessReceiptPrintReceipt extends AppCompatActivity implements TaskCompleteToPrint {
    TextView destinaytionCountryTextView, dateRecepit, timeReceipt, transactionIdReceipt, subscriberNumberReceipt, subscribeNameReceipt,
            attachBranceNameReceipt, cityReceipt, countryRecepit, amountRecepit, feesRecepit, agentNameRecepit,
            agentCountryRecepit, oldTxnidTitleTextViewReceipt, oldTxnidTextViewReceipt, transTypeTextView;
    Button homeButton, printButton;
    String[] recieptData;
    String dateTimeSaperate;
    String[] dateTime;
    Double feesNull,feesAdd,totalAmount,amountAdd;

    TextView senderNameCashToMerchantTextView,destinationNameTextView,destinationNumberTextView, agentBrancehNameTextView,comment_textView, senderNumberTextView, senderNameTextView, marchantNameTextView, billNumberReceiptTextView, labelNameTextView;
    LinearLayout linearlayout_country,linearLayout_agentBrancehName, linearLayout_currency, linearLayoutMarchnatName, linearLayoutSenderNameCashToMarchant,linearlayout_subscriberName, linearLayoutBillNumber, LinearLayoutLabelName, linearLayoutDestinationName;
    LinearLayout linearLayout_comments,linearLayout_totalAmount, linearLayout_transactionCode, linearLayoutDestinationNumber, linearLayoutDestinationCountry, linearlayout_subscriberNumber, linearLayoutSenderNumber, linearLayoutSenderName, linearLayout_city, linearLayout_attachedBranch_name;
    TextView totalAmount_textView, transactionCode_textview, currency_TextView;
    ComponentMd5SharedPre mComponentInfo;
    String agentCode, agentName;

    Bundle bundle = new Bundle();
    String subscriberNoDestinationNo;
    String spinnerCountryString, totalAmountString, tariffAmountFee;
    LinearLayout oldTxnIdLL;
    //========================================================
    private ProgressDialog mDialog;
    private ProtocolAdapter mProtocolAdapter;
    String readerAddress, printerAddress;
    private Printer mPrinter;
    int a = 0;
    private PrinterManager mPrinterManager;
    private Handler mPrinterConnectionHandler;
    private String printMessage;
    private boolean doPrint = false;
    private BluetoothAdapter btAdapter;
    ConnectToPrinter connect_to_Printer;
    String printdata,transactionCodeString;
    ArrayList<String> responseListForPrinter_Header, responseListForPrinter_Body_printReceipt,responseListForPrinter_Body_ReceiveMoney_transactionApproval, responseListForPrinter_Body_SendMoney_transactionApproval, responseListForPrinter_Body_CashIn_CashOut_transactionApproval;
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

        setContentView(R.layout.sucess_receipt_print);
        Toolbar mToolbar;

        dateRecepit = (TextView) findViewById(R.id.dateRecepit);
        timeReceipt = (TextView) findViewById(R.id.timeReceipt);
        transactionIdReceipt = (TextView) findViewById(R.id.transactionIdReceipt);

        subscriberNumberReceipt = (TextView) findViewById(R.id.subscriberNumberReceipt);
        subscribeNameReceipt = (TextView) findViewById(R.id.subscribeNameReceipt);
        attachBranceNameReceipt = (TextView) findViewById(R.id.attachBranceNameReceipt);
        cityReceipt = (TextView) findViewById(R.id.cityReceipt);
        countryRecepit = (TextView) findViewById(R.id.countryRecepit);
        amountRecepit = (TextView) findViewById(R.id.amountRecepit);
        feesRecepit = (TextView) findViewById(R.id.feesRecepit);
        agentNameRecepit = (TextView) findViewById(R.id.agentNameRecepit);
        agentCountryRecepit = (TextView) findViewById(R.id.agentCountryRecepit);
        printButton = (Button) findViewById(R.id.printButton);

        /////////////  REPRINT CASH IN////////////////////////////
        transTypeTextView = (TextView) findViewById(R.id.transTypeTextView);
        oldTxnidTitleTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTitleTextViewReceipt);
        oldTxnidTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTextViewReceipt);
        oldTxnIdLL = (LinearLayout) findViewById(R.id.oldTxnIdLL);

        //////////////

        linearLayoutSenderName = (LinearLayout) findViewById(R.id.linearLayoutSenderName);
        linearLayoutSenderNumber = (LinearLayout) findViewById(R.id.linearLayoutSenderNumber);
        linearlayout_subscriberNumber = (LinearLayout) findViewById(R.id.linearlayout_subscriberNumber);
        linearlayout_subscriberName = (LinearLayout) findViewById(R.id.linearlayout_subscriberName);
        linearLayoutMarchnatName = (LinearLayout) findViewById(R.id.linearLayoutMarchnatName);
        linearLayoutDestinationName = (LinearLayout) findViewById(R.id.linearLayoutDestinationName);
        LinearLayoutLabelName = (LinearLayout) findViewById(R.id.LinearLayoutLabelName);
        linearLayoutBillNumber = (LinearLayout) findViewById(R.id.linearLayoutBillNumber);
        billNumberReceiptTextView = (TextView) findViewById(R.id.billNumberReceiptTextView);
        linearLayoutDestinationNumber = (LinearLayout) findViewById(R.id.linearLayoutDestinationNumber);
        linearLayoutDestinationCountry = (LinearLayout) findViewById(R.id.linearLayoutDestinationCountry);
        linearLayout_attachedBranch_name = (LinearLayout) findViewById(R.id.linearLayout_attachedBranch_name);
        linearLayout_totalAmount = (LinearLayout) findViewById(R.id.linearLayout_totalAmount);
        linearLayout_transactionCode = (LinearLayout) findViewById(R.id.linearLayout_transactionCode);
        linearLayout_currency = (LinearLayout) findViewById(R.id.linearLayout_currency);
        linearLayout_city = (LinearLayout) findViewById(R.id.linearLayout_city);
        linearLayout_agentBrancehName = (LinearLayout) findViewById(R.id.linearLayout_agentBrancehName);
        linearlayout_country = (LinearLayout) findViewById(R.id.linearlayout_country);
        linearLayout_comments = (LinearLayout) findViewById(R.id.linearLayout_comments);
        linearLayoutSenderNameCashToMarchant = (LinearLayout) findViewById(R.id.linearLayoutSenderNameCashToMarchant);


        marchantNameTextView = (TextView) findViewById(R.id.marchantNameTextView);
        billNumberReceiptTextView = (TextView) findViewById(R.id.billNumberReceiptTextView);
        labelNameTextView = (TextView) findViewById(R.id.labelNameTextView);
        destinaytionCountryTextView = (TextView) findViewById(R.id.destinaytionCountryTextView);
        destinationNameTextView = (TextView) findViewById(R.id.destinationNameTextView);
        destinationNumberTextView = (TextView) findViewById(R.id.destinationNumberTextView);
        senderNameCashToMerchantTextView=(TextView) findViewById(R.id.senderNameCashToMerchantTextView);

        senderNameTextView = (TextView) findViewById(R.id.senderNameTextView);
        comment_textView = (TextView) findViewById(R.id.comment_textView);

        transactionCode_textview = (TextView) findViewById(R.id.transactionCode_textview);
        totalAmount_textView = (TextView) findViewById(R.id.totalAmount_textView);
        currency_TextView = (TextView) findViewById(R.id.currency_TextView);

        senderNumberTextView = (TextView) findViewById(R.id.senderNumberTextView);
        agentBrancehNameTextView = (TextView) findViewById(R.id.agentBrancehNameTextView);


        ///////////////////////////////


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

        //transTypeTextView.setText(transType);
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        transactionCodeString= mComponentInfo.getmSharedPreferences().getString("transactionCodeString", "");



        //   String aa = getIntent().getExtras().getString("data", "");
        String aa = mComponentInfo.getmSharedPreferences().getString("data", "");
        //     spinnerCountryString = mComponentInfo.getmSharedPreferences().getString("spinnerCountryString", "");
        tariffAmountFee = mComponentInfo.getmSharedPreferences().getString("tariffAmountFee", "");

        if (aa.trim().length() > 0) {
            recieptData = aa.split("\\|");
            System.out.print(recieptData);
        } else {

        }

        transactionIdReceipt.setText(recieptData[0]);
        dateTimeSaperate = recieptData[3];
        dateTime = dateTimeSaperate.split("\\ ");

        String date = dateTime[0];
        String time = dateTime[1];

        dateRecepit.setText(date);
        timeReceipt.setText(time);



            linearlayout_subscriberNumber.setVisibility(View.GONE);
            linearlayout_subscriberName.setVisibility(View.GONE);

            linearLayoutMarchnatName.setVisibility(View.VISIBLE);
            marchantNameTextView.setText(recieptData[14]);   // ENEO 6

            linearLayoutBillNumber.setVisibility(View.VISIBLE);
            billNumberReceiptTextView.setText(recieptData[10]);  // only set Server Data

            LinearLayoutLabelName.setVisibility(View.VISIBLE);
            labelNameTextView.setText(recieptData[13]);    // only set Server Data

            linearLayoutDestinationName.setVisibility(View.VISIBLE);
            destinationNameTextView.setText(recieptData[6]);   // ENEO  6
            linearLayoutSenderNameCashToMarchant.setVisibility(View.VISIBLE);
            senderNameCashToMerchantTextView.setText(recieptData[8]);    // only set Server Data
            linearLayout_attachedBranch_name.setVisibility(View.GONE);
            linearLayout_transactionCode.setVisibility(View.VISIBLE);
            linearLayout_totalAmount.setVisibility(View.VISIBLE);


            if(recieptData[4].equalsIgnoreCase("null"))
            {
                feesNull = 0.0;
                feesRecepit.setText("0.0");
                amountRecepit.setText(recieptData[5]);
                totalAmount_textView.setText(recieptData[5]);
            }

            else {

                amountAdd = Double.parseDouble(recieptData[5]);
                feesAdd = Double.parseDouble(recieptData[4]);
                totalAmount = (amountAdd + feesAdd);

                totalAmountString = Double.toString(totalAmount);
                totalAmount_textView.setText(totalAmountString);
                feesRecepit.setText(recieptData[4]);
                amountRecepit.setText(recieptData[5]);

            }

            linearLayout_city.setVisibility(View.GONE);
            linearlayout_country.setVisibility(View.GONE);

           oldTxnIdLL.setVisibility(View.VISIBLE);
           oldTxnidTextViewReceipt.setText(recieptData[11]);


            //countryRecepit.setText(recieptData[4]);



            agentNameRecepit.setText(agentName);
            linearLayout_agentBrancehName.setVisibility(View.VISIBLE);


            try {
                agentBrancehNameTextView.setText(recieptData[9]);    //  null Value From Server
                agentCountryRecepit.setText(recieptData[12]);   //   only set Data From  Server Data
                linearLayout_comments.setVisibility(View.VISIBLE);
                comment_textView.setText(recieptData[2]);
                transactionCode_textview.setText(transactionCodeString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            generateAndSetDataPrintReceipt();

            printButton.setVisibility(View.VISIBLE);



        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SucessReceiptPrintReceipt.this.finish();
            }
        });


        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mayRequestPhoneState();
                if (askForPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH_PHONE)) {
                    if (askForPermission(Manifest.permission.BLUETOOTH_ADMIN, REQUEST_BLUETOOTH_PHONE_ADMIN)) {
                        connect_to_Printer = new ConnectToPrinter();
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

                                mPrinterManager = PrinterManager.getInstance(SucessReceiptPrintReceipt.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, SucessReceiptPrintReceipt.this);
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
                        SucessReceiptPrintReceipt.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(SucessReceiptPrintReceipt.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    PrintToPrinter printToPrinter = new PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(SucessReceiptPrintReceipt.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = SucessReceiptPrintReceipt.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
        }

    }

    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {


            PrintUtils mPrintUtils = new PrintUtils(SucessReceiptPrintReceipt.this, mPrinter, bitMap, SucessReceiptPrintReceipt.this);
            mPrintUtils.print_Header(responseListForPrinter_Header);

         mPrintUtils.print_Receipt(responseListForPrinter_Body_printReceipt);


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
        mDialog = new ProgressDialog(SucessReceiptPrintReceipt.this);
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
                        connect_to_Printer = new ConnectToPrinter();
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
                    connect_to_Printer = new ConnectToPrinter();
                    connect_to_Printer.execute();
                    // printerOptions.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    private void generateAndSetDataPrintReceipt() {

        responseListForPrinter_Header = new ArrayList<String>();

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, recieptData[3].split("\\ ")[0]);
        responseListForPrinter_Header.add(2, recieptData[3].split("\\ ")[1]);
        responseListForPrinter_Header.add(3, recieptData[0]);
        responseListForPrinter_Header.add(4, getString(R.string.printReceiptPage));

        responseListForPrinter_Body_printReceipt = new ArrayList<String>();

        responseListForPrinter_Body_printReceipt.add(0, recieptData[11]);    // Old transaction
        responseListForPrinter_Body_printReceipt.add(1, recieptData[14]);    // Merchant Name
        responseListForPrinter_Body_printReceipt.add(2, recieptData[10]);   // Bill NUmber
        responseListForPrinter_Body_printReceipt.add(3, recieptData[13]);    // Label name
        responseListForPrinter_Body_printReceipt.add(4, recieptData[6]);    // Destination name
        responseListForPrinter_Body_printReceipt.add(5, recieptData[8]);   //  sender name


        if(recieptData[4].equalsIgnoreCase("null"))
        {
            recieptData[6] = "0.0";

            responseListForPrinter_Body_printReceipt.add(6, recieptData[5]);  // Amount
            responseListForPrinter_Body_printReceipt.add(7, recieptData[6]);  // fees
            responseListForPrinter_Body_printReceipt.add(8, recieptData[5]);  // Total Amount
        }

        else
        {

            responseListForPrinter_Body_printReceipt.add(6, recieptData[5]);   // amount
            responseListForPrinter_Body_printReceipt.add(7, recieptData[4]);   // fees
            responseListForPrinter_Body_printReceipt.add(8, totalAmountString);   // total amount
        }

        responseListForPrinter_Body_printReceipt.add(9, transactionCodeString);   // transaction Code
        responseListForPrinter_Body_printReceipt.add(10, agentName);                   // agent name
        responseListForPrinter_Body_printReceipt.add(11, recieptData[9]);   // agent Brance Name
        responseListForPrinter_Body_printReceipt.add(12, recieptData[12]);   // agent Country
        responseListForPrinter_Body_printReceipt.add(13, recieptData[2]);   // Comments
   }



    private boolean askForPermission(String permission, Integer requestCode) {
        boolean ret = false;
        if (ContextCompat.checkSelfPermission(SucessReceiptPrintReceipt.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SucessReceiptPrintReceipt.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SucessReceiptPrintReceipt.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SucessReceiptPrintReceipt.this, new String[]{permission}, requestCode);
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
            Toast.makeText(SucessReceiptPrintReceipt.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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
                        connect_to_Printer = new ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }
}



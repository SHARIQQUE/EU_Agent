package agent.eui.sendmoney_cashtocash_fragment;

import android.Manifest;
import android.app.Activity;
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
import agent.activities.RePrint;
import callback.TaskCompleteToPrint;
import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;
import printer_utilities.PrintUtils;
import printer_utilities.PrinterListShowActivity;
import printer_utilities.PrinterManager;

/**
 * Created by Shariq on 15-06-2017.
 */


public class ReprintSendCash extends AppCompatActivity implements TaskCompleteToPrint {
    TextView dateRecepit, commentsTitleReview, attachBranceNameReceipt, currencyType_sender_textview, destinationCountryRecepit, referencenumberCashtocash_textview, timeReceipt, transactionIdReceipt, destinationNumberReceipt, destinationNameReceipt, amountReceipt, cityReceipt, countryRecepit, amountRecepit, feesRecepit, agentNameRecepit, agentCountryRecepit;
    Button homeButton;
    ComponentMd5SharedPre mComponentInfo;
    String agentCode, agentName;
    Button printButton;
    String date, time;
    TextView destinationFirstName_textview,senderNumberReceipt, senderCountryRecepit, idProofType_textview, senderIdproofIssueDate_textview, tax_textview,
            otherTax_recepit, senderfirstNameReceipt, totalamountsend_textview, destination_currency_type_textview, amountTopay_textview, test_question_textview, test_answar_textview;


    TextView senderNameReceipt, oldTxnidTitleTextViewReceipt, oldTxnidTextViewReceipt, titleTextView;
    Bitmap bitMap;
    private int txnType = 0;
    LinearLayout oldTxnIdLL;
    Bundle bundle = new Bundle();



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

        setContentView(R.layout.reprint_send_cash);
        Toolbar mToolbar;

        dateRecepit = (TextView) findViewById(R.id.dateRecepit);
        timeReceipt = (TextView) findViewById(R.id.timeReceipt);
        transactionIdReceipt = (TextView) findViewById(R.id.transactionIdReceipt);

        referencenumberCashtocash_textview = (TextView) findViewById(R.id.referencenumberCashtocash_textview);
        destinationCountryRecepit = (TextView) findViewById(R.id.destinationCountryRecepit);
        destinationNumberReceipt = (TextView) findViewById(R.id.destinationNumberReceipt);
        destinationNameReceipt = (TextView) findViewById(R.id.destinationNameReceipt);
        amountReceipt = (TextView) findViewById(R.id.amountReceipt);
        feesRecepit = (TextView) findViewById(R.id.feesRecepit);
        cityReceipt = (TextView) findViewById(R.id.cityReceipt);
        countryRecepit = (TextView) findViewById(R.id.countryRecepit);
        amountRecepit = (TextView) findViewById(R.id.amountRecepit);
        agentNameRecepit = (TextView) findViewById(R.id.agentNameRecepit);
        senderNameReceipt = (TextView) findViewById(R.id.senderNameReceipt);
        attachBranceNameReceipt = (TextView) findViewById(R.id.attachBranceNameReceipt);
        commentsTitleReview = (TextView) findViewById(R.id.commentsTitleReview);
        currencyType_sender_textview = (TextView) findViewById(R.id.currencyType_sender_textview);
        senderfirstNameReceipt = (TextView) findViewById(R.id.senderfirstNameReceipt);
        destinationFirstName_textview = (TextView) findViewById(R.id.destinationFirstName_textview);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        oldTxnidTitleTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTitleTextViewReceipt);
        oldTxnidTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTextViewReceipt);
        oldTxnIdLL = (LinearLayout) findViewById(R.id.oldTxnIdLL);

        senderNumberReceipt = (TextView) findViewById(R.id.senderNumberReceipt);
        senderCountryRecepit = (TextView) findViewById(R.id.senderCountryRecepit);
        idProofType_textview = (TextView) findViewById(R.id.idProofType_textview);
        senderIdproofIssueDate_textview = (TextView) findViewById(R.id.senderIdproofIssueDate_textview);
        tax_textview = (TextView) findViewById(R.id.tax_textview);
        otherTax_recepit = (TextView) findViewById(R.id.otherTax_recepit);
        totalamountsend_textview = (TextView) findViewById(R.id.totalamountsend_textview);
        destination_currency_type_textview = (TextView) findViewById(R.id.destination_currency_type_textview);
        amountTopay_textview = (TextView) findViewById(R.id.amountTopay_textview);
        test_question_textview = (TextView) findViewById(R.id.test_question_textview);
        test_answar_textview = (TextView) findViewById(R.id.test_answar_textview);


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

        try {


            agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
            agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");


            String dateTimeSaperate = mComponentInfo.getDateTime_reprint_sendcash();
            String[] dateTime = dateTimeSaperate.split("\\ ");

            date = dateTime[0];
            time = dateTime[1];

            if (date != null && date.contains("-")) {

                date = date.replace("-", "/");
            }

//            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
//            date=sdf.format(date);
            dateRecepit.setText(date);
            timeReceipt.setText(time);



            referencenumberCashtocash_textview.setText(mComponentInfo.getReferenceNumber_reprint_sendcash());
            senderNameReceipt.setText(mComponentInfo.getSenderName_reprint_sendCash());
            senderfirstNameReceipt.setText(mComponentInfo.getSenderFirstName_reprint_sendCash());
            destinationCountryRecepit.setText(mComponentInfo.getDestinationCountryName_reprint_sendCash());
            destinationNameReceipt.setText(mComponentInfo.getDestinationName_reprint_sendCash());
            destinationNumberReceipt.setText(mComponentInfo.getDestinationMobileNumber_reprint_sendCash());
            currencyType_sender_textview.setText(mComponentInfo.getSenderCurrencyCode_reprint_sendCash());
            amountReceipt.setText(mComponentInfo.getAmount_reprint_sendCash() + " " +mComponentInfo.getSenderCurrencyCode_reprint_sendCash());
            feesRecepit.setText(mComponentInfo.getFees_reprint_sendCash());
            agentNameRecepit.setText(agentName);
            attachBranceNameReceipt.setText(mComponentInfo.getAttachedBranchName_reprint_sendCash());
            countryRecepit.setText(mComponentInfo.getSenderCountryName_reprint_sendCash());
            commentsTitleReview.setText(mComponentInfo.getComment_reprint_sendCash());
            transactionIdReceipt.setText(mComponentInfo.getTransactionId_reprint_sendCash());



            senderNumberReceipt.setText(mComponentInfo.getSenderMobileNumber_reprint_sendCash());
            senderCountryRecepit.setText(mComponentInfo.getIdDocumentCountryOffissue_reprint_sendCash());
            idProofType_textview.setText(mComponentInfo.getIdDocumnetType_reprint_sendCash());

            senderIdproofIssueDate_textview.setText(mComponentInfo.getIdDocumentCountryOffissue_reprint_sendCash());


            if (mComponentInfo.getTax_reprint_sendCash().equalsIgnoreCase("0.0")) {
                tax_textview.setText("0");
            } else {
                tax_textview.setText(mComponentInfo.getTax_reprint_sendCash());
            }

            if (mComponentInfo.getOtherTax_reprint_sendCash().equalsIgnoreCase("0.0")) {
                otherTax_recepit.setText("0");
            } else {
                otherTax_recepit.setText(mComponentInfo.getOtherTax_reprint_sendCash());
            }


            totalamountsend_textview.setText(mComponentInfo.getTotalAmount());
            destination_currency_type_textview.setText(mComponentInfo.getDestinationCurrencyCode_reprint_sendCash());
            amountTopay_textview.setText(mComponentInfo.getAmountToPay_reprint_sendCash());
            test_question_textview.setText(mComponentInfo.getQuestionName_reprint_sendCash());
            test_answar_textview.setText(mComponentInfo.getAnswerName_reprint_sendCash());
            destinationFirstName_textview.setText(mComponentInfo.getDestinationFirstName_reprint_sendCash());



            generateAndSetData();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ReprintSendCash.this, getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

        }


        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent5= new Intent(ReprintSendCash.this, RePrint.class);

                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent5);
                finish();

            }
        });


        printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mayRequestPhoneState();
                if (askForPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH_PHONE)) {
                    if (askForPermission(Manifest.permission.BLUETOOTH_ADMIN, REQUEST_BLUETOOTH_PHONE_ADMIN)) {
                        connect_to_Printer = new ReprintSendCash.ConnectToPrinter();
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
    public void onBackPressed() {
        super.onBackPressed();

   /*  //   getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_cashtocash, new SenderCountryFragment()).commit();
        Toast.makeText(SucessReceiptCashToCashSendMoney.this, "Receipt page back button  ", Toast.LENGTH_LONG).show();

       *//* FragmentManager fm =SucessReceiptCashToCashSendMoney.this.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }*//*

        FragmentManager fragmentManager = getSupportFragmentManager();
        //this will clear the back stack and displays no animation on the screen
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

*/
        Intent intent5= new Intent(ReprintSendCash.this, RePrint.class);

        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent5);
        finish();


    }

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
    String printdata;
    ArrayList<String> responseListForPrinter_Header, responseListForPrinter_Body;

    String printValues = null;
    private static final int REQUEST_BLUETOOTH_PHONE = 0;
    private static final int REQUEST_BLUETOOTH_PHONE_ADMIN = 1;
    private static final int REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED = 2;


    private void generateAndSetData() {


        responseListForPrinter_Body = new ArrayList<String>();
        responseListForPrinter_Header = new ArrayList<String>();

        //    HEADER

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, date);
        responseListForPrinter_Header.add(2, time);
        responseListForPrinter_Header.add(3, mComponentInfo.getTransactionId_reprint_sendCash());


        responseListForPrinter_Body.add(0, mComponentInfo.getReferenceNumber_reprint_sendcash());

        // sender Details


        responseListForPrinter_Body.add(1, mComponentInfo.getSenderFirstName_reprint_sendCash());
        responseListForPrinter_Body.add(2, mComponentInfo.getSenderName_reprint_sendCash());
        responseListForPrinter_Body.add(3, mComponentInfo.getSenderMobileNumber_reprint_sendCash());

        responseListForPrinter_Body.add(4, mComponentInfo.getIdDocumentCountryOffissue_reprint_sendCash());
        responseListForPrinter_Body.add(5, mComponentInfo.getIdDocumnetType_reprint_sendCash());
        responseListForPrinter_Body.add(6, mComponentInfo.getIdDocumentCountryOffissue_reprint_sendCash());


        // Destination Details


        responseListForPrinter_Body.add(7, mComponentInfo.getDestinationCountryName_reprint_sendCash());
        responseListForPrinter_Body.add(8, mComponentInfo.getDestinationFirstName_reprint_sendCash());
        responseListForPrinter_Body.add(9, mComponentInfo.getDestinationName_reprint_sendCash());
        responseListForPrinter_Body.add(10, mComponentInfo.getDestinationMobileNumber_reprint_sendCash());

        // amount and taxex Details


        responseListForPrinter_Body.add(11, mComponentInfo.getSenderCurrencyCode_reprint_sendCash());
        responseListForPrinter_Body.add(12, mComponentInfo.getAmount_reprint_sendCash());
        responseListForPrinter_Body.add(13, mComponentInfo.getFees_reprint_sendCash());


        if (mComponentInfo.getTax_reprint_sendCash().equalsIgnoreCase("0.0")) {
            responseListForPrinter_Body.add(14, "0");
        } else {
            responseListForPrinter_Body.add(14, mComponentInfo.getTax_reprint_sendCash());
        }

        if (mComponentInfo.getOtherTax_reprint_sendCash().equalsIgnoreCase("0.0")) {
            responseListForPrinter_Body.add(15, "0");
        } else {
            responseListForPrinter_Body.add(15, mComponentInfo.getOtherTax_reprint_sendCash());

        }


        responseListForPrinter_Body.add(16, mComponentInfo.getTotalAmount());
        responseListForPrinter_Body.add(17, mComponentInfo.getDestinationCurrencyCode_reprint_sendCash());
        responseListForPrinter_Body.add(18, mComponentInfo.getAmountToPay_reprint_sendCash());

        // Other Details

        responseListForPrinter_Body.add(19, mComponentInfo.getQuestionName_reprint_sendCash());
        responseListForPrinter_Body.add(20, mComponentInfo.getAnswerName_reprint_sendCash());
        responseListForPrinter_Body.add(21, agentName);   // Parent Agent name
        responseListForPrinter_Body.add(22, mComponentInfo.getAttachedBranchName_reprint_sendCash());
        responseListForPrinter_Body.add(23, mComponentInfo.getSenderCountryName_reprint_sendCash());


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

                                mPrinterManager = PrinterManager.getInstance(ReprintSendCash.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, ReprintSendCash.this);
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
                        ReprintSendCash.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(ReprintSendCash.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    ReprintSendCash.PrintToPrinter printToPrinter = new ReprintSendCash.PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(ReprintSendCash.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = ReprintSendCash.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
        }

    }

    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {

            PrintUtils mPrintUtils = new PrintUtils(ReprintSendCash.this, mPrinter, bitMap, ReprintSendCash.this);

            mPrintUtils.print_Header_CashToCashSendMoneyReceiveMoney(responseListForPrinter_Header);

            mPrintUtils.print_sendCash(responseListForPrinter_Body);

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
        mDialog = new ProgressDialog(ReprintSendCash.this);
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
                        connect_to_Printer = new ReprintSendCash.ConnectToPrinter();
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
                    connect_to_Printer = new ReprintSendCash.ConnectToPrinter();
                    connect_to_Printer.execute();
                    // printerOptions.setVisibility(View.VISIBLE);
                }
                break;
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


    private boolean askForPermission(String permission, Integer requestCode) {
        boolean ret = false;
        if (ContextCompat.checkSelfPermission(ReprintSendCash.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ReprintSendCash.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ReprintSendCash.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ReprintSendCash.this, new String[]{permission}, requestCode);
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
            Toast.makeText(ReprintSendCash.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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
                        connect_to_Printer = new ReprintSendCash.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }
}



   
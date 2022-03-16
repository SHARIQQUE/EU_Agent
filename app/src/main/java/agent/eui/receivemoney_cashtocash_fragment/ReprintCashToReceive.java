package agent.eui.receivemoney_cashtocash_fragment;

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


public class ReprintCashToReceive extends AppCompatActivity implements TaskCompleteToPrint {
    TextView dateRecepit, timeReceipt,referencenumberCashtocash_textview,senderCountryRecepit_textview,destinationNumberReceipt,amountReceipt,senderNameReceipt,destinationFirstName_textview,commentsTitleReview,destination_currency_type_textview, transactionIdReceipt, referenceNumberReceipt, destinationNameReceipt, attachBranceNameReceipt, cityReceipt, countryRecepit, amountRecepit, feesRecepit, agentNameRecepit, agentCountryRecepit;
    Button homeButton;
    ComponentMd5SharedPre mComponentInfo;
    String agentCode, agentName,countrySelectionString;
    Button printButton;
    TextView  senderNumberReceipt,senderfirstNameReceipt_recieve,idDocumnetCountryOffIssue_textview,idProofType_textview,senderIdproofIssueDate_textview,tax_textview,
            otherTax_recepit,totalamountsend_textview,test_question_textview,test_answar_textview;


    String date, time,totalAmountCalculation;

    TextView oldTxnidTitleTextViewReceipt, oldTxnidTextViewReceipt, titleTextView;
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

        setContentView(R.layout.reprint_cashtocash_receive);
        Toolbar mToolbar;


        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        dateRecepit = (TextView) findViewById(R.id.dateRecepit);
        timeReceipt = (TextView) findViewById(R.id.timeReceipt);
        transactionIdReceipt = (TextView) findViewById(R.id.transactionIdReceipt);

        referencenumberCashtocash_textview = (TextView) findViewById(R.id.referencenumberCashtocash_textview);
        senderCountryRecepit_textview = (TextView) findViewById(R.id.senderCountryRecepit_textview);
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
        destination_currency_type_textview = (TextView) findViewById(R.id.destination_currency_type_textview);
        senderfirstNameReceipt_recieve = (TextView) findViewById(R.id.senderfirstNameReceipt_recieve);
        destinationFirstName_textview = (TextView) findViewById(R.id.destinationFirstName_textview);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        oldTxnidTitleTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTitleTextViewReceipt);
        oldTxnidTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTextViewReceipt);
        oldTxnIdLL = (LinearLayout) findViewById(R.id.oldTxnIdLL);

        senderNumberReceipt = (TextView) findViewById(R.id.senderNumberReceipt);
        idDocumnetCountryOffIssue_textview = (TextView) findViewById(R.id.idDocumnetCountryOffIssue_textview);
        idProofType_textview = (TextView) findViewById(R.id.idProofType_textview);
        senderIdproofIssueDate_textview = (TextView) findViewById(R.id.senderIdproofIssueDate_textview);
        tax_textview = (TextView) findViewById(R.id.tax_textview);
        otherTax_recepit = (TextView) findViewById(R.id.otherTax_recepit);
        totalamountsend_textview = (TextView) findViewById(R.id.totalamountsend_textview);
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


            String dateTimeSaperate = mComponentInfo.getDateTime_reprint_receiveCash();
            String[] dateTime = dateTimeSaperate.split("\\ ");

            date = dateTime[0];
            time = dateTime[1];


//            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
//            date=sdf.format(date);
            if(date!=null&&date.contains("-")){

                date=date.replace("-","/");
            }

            dateRecepit.setText(date);
            timeReceipt.setText(time);


            referencenumberCashtocash_textview.setText(mComponentInfo.getReferenceNumber_reprint_receiveCash());
            senderNameReceipt.setText(mComponentInfo.getSenderLatName_reprint_receiveCash());
            senderCountryRecepit_textview.setText(mComponentInfo.getDestinationCountryName_reprint_receiveCash());
            destinationFirstName_textview.setText(mComponentInfo.getDestinationFirstName_reprint_receiveCash());
            destinationNameReceipt.setText(mComponentInfo.getDestinationLastName_reprint_receiveCash());
            destinationNumberReceipt.setText(mComponentInfo.getDestinationMobileNumber_reprint_receiveCash());
            destination_currency_type_textview.setText(mComponentInfo.getDestinationCurrencyCode_reprint_receiveCash());
            amountReceipt.setText(mComponentInfo.getAmount_reprint_receiveCash());
            feesRecepit.setText(mComponentInfo.getFees_reprint_receiveCash());
            agentNameRecepit.setText(agentName);
            attachBranceNameReceipt.setText(mComponentInfo.getAgentBranch_reprint_receiveCash());
            countryRecepit.setText(countrySelectionString);
            commentsTitleReview.setText(mComponentInfo.getComment_reprint_receiveCash());
            transactionIdReceipt.setText(mComponentInfo.getTransactionId_reprint_receiveCash());
            senderNumberReceipt.setText(mComponentInfo.getSenderMobileNumber_reprint_receiveCash());
            idDocumnetCountryOffIssue_textview.setText(mComponentInfo.getSenderCountryName_reprint_receiveCash());  //  getSenderCountryName koma Change  25 july 2019
            idProofType_textview.setText(mComponentInfo.getIdProofType_reprint_receiveCash());
            senderIdproofIssueDate_textview.setText(mComponentInfo.getIdProofIssueDate_reprint_receiveCash());
            tax_textview.setText(mComponentInfo.getVat_reprint_receiveCash());


            if(mComponentInfo.getOtherTax_reprint_receiveCash().equalsIgnoreCase("0.0"))
            {
                otherTax_recepit.setText("0");
            }
            else {
                otherTax_recepit.setText(mComponentInfo.getOtherTax_reprint_receiveCash());
            }


            try {


                String amountString = mComponentInfo.getAmount_reprint_receiveCash();
                String feesString = mComponentInfo.getFees_reprint_receiveCash();
                String vatString = mComponentInfo.getVat_reprint_receiveCash();
                String otherTax = mComponentInfo.getOtherTax_reprint_receiveCash();


                Double amount_double = Double.parseDouble(amountString);
                Double fees_double = Double.parseDouble(feesString);
                Double vat_double = Double.parseDouble(vatString);
                Double otherTax_double = Double.parseDouble(otherTax);

                Double totalAmountCalculation_double = amount_double - fees_double - vat_double - otherTax_double;

                totalAmountCalculation = Double.toString(totalAmountCalculation_double);
                totalamountsend_textview.setText(totalAmountCalculation);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }




            if(!mComponentInfo.getQuestionName_reprint_receiveCash().equalsIgnoreCase("null")){

                test_question_textview.setText(mComponentInfo.getQuestionName_reprint_receiveCash());
            }else {

                test_question_textview.setText("");
            }


            if(!mComponentInfo.getAnswerName_reprint_receiveCash().equalsIgnoreCase("null")){

                test_answar_textview.setText(mComponentInfo.getAnswerName_reprint_receiveCash());
            }else {

                test_answar_textview.setText("");
            }
            test_answar_textview.setText(mComponentInfo.getAnswerName_reprint_receiveCash());
            senderfirstNameReceipt_recieve.setText(mComponentInfo.getSenderFirstName_reprint_receiveCash());




            generateAndSetData();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ReprintCashToReceive.this, getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

        }


        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ReprintCashToReceive.this, RePrint.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
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
                        connect_to_Printer = new ReprintCashToReceive.ConnectToPrinter();
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


        Intent intent5= new Intent(ReprintCashToReceive.this, RePrint.class);

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
        responseListForPrinter_Header.add(3, mComponentInfo.getTransactionId_reprint_receiveCash());


        responseListForPrinter_Body.add(0, mComponentInfo.getReferenceNumber_reprint_receiveCash());

        responseListForPrinter_Body.add(1, mComponentInfo.getSenderFirstName_reprint_receiveCash());
        responseListForPrinter_Body.add(2, mComponentInfo.getSenderLatName_reprint_receiveCash());
        responseListForPrinter_Body.add(3, mComponentInfo.getSenderMobileNumber_reprint_receiveCash());
        responseListForPrinter_Body.add(4, mComponentInfo.getSenderCountryName_reprint_receiveCash());
        responseListForPrinter_Body.add(5, mComponentInfo.getDestinationCountryName_reprint_receiveCash());
        responseListForPrinter_Body.add(6, mComponentInfo.getDestinationFirstName_reprint_receiveCash());
        responseListForPrinter_Body.add(7, mComponentInfo.getDestinationLastName_reprint_receiveCash());
        responseListForPrinter_Body.add(8, mComponentInfo.getDestinationMobileNumber_reprint_receiveCash());
        responseListForPrinter_Body.add(9, mComponentInfo.getIdProofType_reprint_receiveCash());
        responseListForPrinter_Body.add(10, mComponentInfo.getIdProofIssueDate_reprint_receiveCash());
        responseListForPrinter_Body.add(11, mComponentInfo.getDestinationCurrencyCode_reprint_receiveCash());
        responseListForPrinter_Body.add(12, mComponentInfo.getAmount_reprint_receiveCash());
        responseListForPrinter_Body.add(13, mComponentInfo.getFees_reprint_receiveCash());
        responseListForPrinter_Body.add(14, mComponentInfo.getVat_reprint_receiveCash());

        if(mComponentInfo.getOtherTax_reprint_receiveCash().equalsIgnoreCase("0.0"))
        {
            responseListForPrinter_Body.add(15, "0");
        }
        else {
            responseListForPrinter_Body.add(15, mComponentInfo.getOtherTax_reprint_receiveCash());

        }


        responseListForPrinter_Body.add(16, totalAmountCalculation);


      //  responseListForPrinter_Body.add(16, mComponentInfo.getAmountSent());
        responseListForPrinter_Body.add(17, mComponentInfo.getQuestionName_reprint_receiveCash());
        responseListForPrinter_Body.add(18, mComponentInfo.getAnswerName_reprint_receiveCash());
        responseListForPrinter_Body.add(19, agentName);   // Parent Agent name
        responseListForPrinter_Body.add(20, mComponentInfo.getAgentBranch_reprint_receiveCash());   // Parent Agent name
        responseListForPrinter_Body.add(21, countrySelectionString);   // Parent Agent name




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

                                mPrinterManager = PrinterManager.getInstance(ReprintCashToReceive.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, ReprintCashToReceive.this);
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
                        ReprintCashToReceive.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(ReprintCashToReceive.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    ReprintCashToReceive.PrintToPrinter printToPrinter = new ReprintCashToReceive.PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(ReprintCashToReceive.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = ReprintCashToReceive.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
        }

    }



    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {

            PrintUtils mPrintUtils = new PrintUtils(ReprintCashToReceive.this, mPrinter, bitMap, ReprintCashToReceive.this);

            mPrintUtils.print_Header_receiveMoney(responseListForPrinter_Header);

            mPrintUtils.print_receiveCash(responseListForPrinter_Body);

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
        mDialog = new ProgressDialog(ReprintCashToReceive.this);
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
                        connect_to_Printer = new ReprintCashToReceive.ConnectToPrinter();
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
                    connect_to_Printer = new ReprintCashToReceive.ConnectToPrinter();
                    connect_to_Printer.execute();
                    // printerOptions.setVisibility(View.VISIBLE);
                }
                break;
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
        if (ContextCompat.checkSelfPermission(ReprintCashToReceive.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ReprintCashToReceive.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ReprintCashToReceive.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ReprintCashToReceive.this, new String[]{permission}, requestCode);
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
            Toast.makeText(ReprintCashToReceive.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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
                        connect_to_Printer = new ReprintCashToReceive.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }
}



   
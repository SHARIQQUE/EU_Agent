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


public class SucessReceiptTransactionCancelReceipt extends AppCompatActivity implements TaskCompleteToPrint {
    TextView transTypeTextView, dateRecepit, timeReceipt, totalamountRecepit, senderNameReceipt, commentsTitleReview, destinationNameReceipt, destinationNumberReceipt, transactionCodeRecepit, transactionIdReceipt, marchantNameReceipt, billNumberReceipt, attachBranceNameReceipt, cityReceipt, labelNameReceipt, amountRecepit, feesRecepit, agentNameRecepit, agentCountryRecepit;
    Button homeButton;
    String totalAmountString;
    Double amountAdd,feesAdd,totalAmount;
    String[] recieptData;
    ComponentMd5SharedPre mComponentInfo;
    String approvalType,agentCode, labelNameString, senderNameString, spinnerCountryString, spinnerBillerNameString, agentName;
    TextView oldTxnidTitleTextViewReceipt, oldTxnidTextViewReceipt, titleTextView;
    LinearLayout oldTxnIdLL;
    private int txnType = 0;
    Bundle bundle = new Bundle();
    String subscriberNoDestinationNo;
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
    SucessReceiptTransactionCancelReceipt.ConnectToPrinter connect_to_Printer;
    String printdata;
    ArrayList<String> responseListForPrinter_Header, responseListForPrinter_Body_cancel_cashToMarchant,responseListForPrinter_Body_cancel_billpay;
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


        setContentView(R.layout.sucess_receipt_transaction_cancel);
        Toolbar mToolbar;

        dateRecepit = (TextView) findViewById(R.id.dateRecepit);
        timeReceipt = (TextView) findViewById(R.id.timeReceipt);
        transTypeTextView = (TextView) findViewById(R.id.transTypeTextView);


        transactionIdReceipt = (TextView) findViewById(R.id.transactionIdReceipt);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        oldTxnidTitleTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTitleTextViewReceipt);
        oldTxnidTextViewReceipt = (TextView) findViewById(R.id.oldTxnidTextViewReceipt);
        oldTxnIdLL = (LinearLayout) findViewById(R.id.oldTxnIdLL);

        marchantNameReceipt = (TextView) findViewById(R.id.marchantNameReceipt);
        billNumberReceipt = (TextView) findViewById(R.id.billNumberReceipt);
        attachBranceNameReceipt = (TextView) findViewById(R.id.attachBranceNameReceipt);
        cityReceipt = (TextView) findViewById(R.id.cityReceipt);
        labelNameReceipt = (TextView) findViewById(R.id.labelNameReceipt);
        amountRecepit = (TextView) findViewById(R.id.amountRecepit);
        feesRecepit = (TextView) findViewById(R.id.feesRecepit);
        agentNameRecepit = (TextView) findViewById(R.id.agentNameRecepit);
        agentCountryRecepit = (TextView) findViewById(R.id.agentCountryRecepit);
        destinationNameReceipt = (TextView) findViewById(R.id.destinationNameReceipt);
        destinationNumberReceipt = (TextView) findViewById(R.id.destinationNumberReceipt);
        senderNameReceipt = (TextView) findViewById(R.id.senderNameReceipt);
        totalamountRecepit = (TextView) findViewById(R.id.totalamountRecepit);
        transactionCodeRecepit = (TextView) findViewById(R.id.transactionCodeRecepit);
        commentsTitleReview = (TextView) findViewById(R.id.commentsTitleReview);

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


        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");

        String aa = mComponentInfo.getmSharedPreferences().getString("data", "");

        //  labelNameString =  mComponentInfo.getmSharedPreferences().getString("labelNameString", "");
        //  senderNameString =  mComponentInfo.getmSharedPreferences().getString("senderNameString", "");
        //  spinnerCountryString =  mComponentInfo.getmSharedPreferences().getString("spinnerCountryString", "");
        //  spinnerBillerNameString =  mComponentInfo.getmSharedPreferences().getString("spinnerBillerNameString", "");


        if (aa.trim().length() > 0) {
            recieptData = aa.split("\\|");
            System.out.print(recieptData);
        } else {

        }

        transactionIdReceipt.setText(recieptData[0]);
        String dateTimeSaperate = recieptData[3];
        String[] dateTime = dateTimeSaperate.split("\\ ");

        String date = dateTime[0];
        String time = dateTime[1];

        dateRecepit.setText(date);
        timeReceipt.setText(time);
        String transType = "";

        switch (recieptData[13]) {

           case "CASHTOM":
                    if (recieptData[13].equalsIgnoreCase("CASHTOM"))
                    {
                        oldTxnIdLL.setVisibility(View.VISIBLE);
                        oldTxnidTextViewReceipt.setText(recieptData[11]);
                        transTypeTextView.setText(getString(R.string.cashToMReversal));

                        approvalType ="CASHTOM";

                    }

                     /* generalResponseModel.setUserDefinedString(
                       obj.getString("transid") +     // Transaid New
                               "|" + obj.getString("resultdescription") +
                               "|" + obj.getString("comments") +    // 2
                               "|" + obj.getString("responsects") +
                               "|" + obj.getString("fee") +      // eversal Fees :
                               "|" + obj.getString("amount") +    // 5
                               "|" + obj.getString("destinationname") +    // 6
                               "|" + obj.getString("source") +
                               "|" + obj.getString("sourcename") + // 8
                               "|" + obj.getString("agentbranch") +    // 9
                               "|" + obj.getString("invoiceno") +     // 10
                               "|" + obj.getString("transferno") +     // Trans id Old
                               "|" + obj.getString("country") +     // 12
                               "|" + obj.getString("transtype") +   // 13
                               "|" + obj.getString("confcode") +    // 14
                               "|" + obj.getString("labelname")+   // 15
                               "|" + obj.getString("billername")+    // 16
                               "|" + obj.getString("resultdescription")

               );
*/


                    commentsTitleReview.setText(recieptData[2]);
                    destinationNameReceipt.setText(recieptData[6]);
                    amountRecepit.setText(recieptData[5]);
                    feesRecepit.setText(recieptData[4]);
                     amountAdd = Double.parseDouble(recieptData[5]);
                     feesAdd = Double.parseDouble(recieptData[4]);
                     totalAmount = (amountAdd + feesAdd);
                    totalAmountString = Double.toString(totalAmount);
                    totalamountRecepit.setText(totalAmountString);
                    billNumberReceipt.setText(recieptData[10]);
                    transactionCodeRecepit.setText(recieptData[14]);
                    labelNameReceipt.setText(recieptData[15]);
                    marchantNameReceipt.setText(recieptData[16]);
                    senderNameReceipt.setText(recieptData[8]);
                    //destinationNumberReceipt.setText(recieptData[5]);

                    attachBranceNameReceipt.setText(recieptData[9]);
                    agentNameRecepit.setText(agentName);
                    agentCountryRecepit.setText(recieptData[12]);

                    generateAndSetDataCashToMarchant();

                break;

            case "BILLPAY": {

                if (recieptData[13].equalsIgnoreCase("BILLPAY"))
                {
                    oldTxnIdLL.setVisibility(View.VISIBLE);
                    oldTxnidTitleTextViewReceipt.setText(recieptData[11]);
                    transTypeTextView.setText(getString(R.string.billpayReversal));
                    approvalType ="BILLPAY";
                }

                commentsTitleReview.setText(recieptData[2]);
                destinationNameReceipt.setText(recieptData[6]);
                amountRecepit.setText(recieptData[5]);
                feesRecepit.setText(recieptData[4]);
                 amountAdd = Double.parseDouble(recieptData[5]);
                 feesAdd = Double.parseDouble(recieptData[4]);
                 totalAmount = (amountAdd + feesAdd);
                totalAmountString = Double.toString(totalAmount);
                totalamountRecepit.setText(totalAmountString);
                billNumberReceipt.setText(recieptData[10]);
                transactionCodeRecepit.setText(recieptData[14]);
                labelNameReceipt.setText(recieptData[15]);
                marchantNameReceipt.setText(recieptData[16]);
                senderNameReceipt.setText(recieptData[8]);
                //destinationNumberReceipt.setText(recieptData[5]);

                attachBranceNameReceipt.setText(recieptData[9]);
                agentNameRecepit.setText(agentName);
                agentCountryRecepit.setText(recieptData[12]);


                generateAndSetDataBillPay();



                break;
            }
        }


        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SucessReceiptTransactionCancelReceipt.this.finish();
            }
        });

        Button printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mayRequestPhoneState();
                if (askForPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH_PHONE)) {
                    if (askForPermission(Manifest.permission.BLUETOOTH_ADMIN, REQUEST_BLUETOOTH_PHONE_ADMIN)) {
                        connect_to_Printer = new SucessReceiptTransactionCancelReceipt.ConnectToPrinter();
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

                                mPrinterManager = PrinterManager.getInstance(SucessReceiptTransactionCancelReceipt.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, SucessReceiptTransactionCancelReceipt.this);
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
                        SucessReceiptTransactionCancelReceipt.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(SucessReceiptTransactionCancelReceipt.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    SucessReceiptTransactionCancelReceipt.PrintToPrinter printToPrinter = new SucessReceiptTransactionCancelReceipt.PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(SucessReceiptTransactionCancelReceipt.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = SucessReceiptTransactionCancelReceipt.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
        }

    }

    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {

            PrintUtils mPrintUtils = new PrintUtils(SucessReceiptTransactionCancelReceipt.this, mPrinter, bitMap,SucessReceiptTransactionCancelReceipt.this);
            mPrintUtils.print_Header_TransactionCancel(responseListForPrinter_Header);

            switch (approvalType) {

                case "CASHTOM":   //  Success
                    mPrintUtils.print_cancelReceipt_cashToMerchant(responseListForPrinter_Body_cancel_cashToMarchant);
                    break;



                case "BILLPAY":
                    mPrintUtils.print_cancelReceipt_BillPay(responseListForPrinter_Body_cancel_billpay);
                    break;

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
        mDialog = new ProgressDialog(SucessReceiptTransactionCancelReceipt.this);
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
                        connect_to_Printer = new SucessReceiptTransactionCancelReceipt.ConnectToPrinter();
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
                    connect_to_Printer = new SucessReceiptTransactionCancelReceipt.ConnectToPrinter();
                    connect_to_Printer.execute();
                    // printerOptions.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    private void generateAndSetDataBillPay() {

        int a = recieptData.length;
        responseListForPrinter_Body_cancel_billpay = new ArrayList<String>();
        responseListForPrinter_Header = new ArrayList<String>();

        // HEAD

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, recieptData[3].split("\\ ")[0]);
        responseListForPrinter_Header.add(2, recieptData[3].split("\\ ")[1]);
        responseListForPrinter_Header.add(3, recieptData[0]);
        responseListForPrinter_Header.add(4, getString(R.string.cashToMReversal));
        responseListForPrinter_Header.add(5, recieptData[11]);



        responseListForPrinter_Body_cancel_billpay.add(0, recieptData[16]); // Marchant Name
        responseListForPrinter_Body_cancel_billpay.add(1, recieptData[10]);      // Bill Number
        responseListForPrinter_Body_cancel_billpay.add(2, recieptData[15]);      // Label Name
        responseListForPrinter_Body_cancel_billpay.add(3, recieptData[6]);     // Destination Name
        responseListForPrinter_Body_cancel_billpay.add(4, recieptData[8]);   // Sender Name
        responseListForPrinter_Body_cancel_billpay.add(5, recieptData[5]);  // Amount
        responseListForPrinter_Body_cancel_billpay.add(6, recieptData[4]); // Fees
        responseListForPrinter_Body_cancel_billpay.add(7, totalAmountString);      // Total Amount
        responseListForPrinter_Body_cancel_billpay.add(8, recieptData[14]); // Transaction Code
        responseListForPrinter_Body_cancel_billpay.add(9, agentName); // Agent name
        responseListForPrinter_Body_cancel_billpay.add(10, recieptData[9]); // Attach Branch Name
        responseListForPrinter_Body_cancel_billpay.add(11, recieptData[12]); // Agent Country
        responseListForPrinter_Body_cancel_billpay.add(12, recieptData[2]); // Comments

    }

    private void generateAndSetDataCashToMarchant() {

        int a = recieptData.length;
        responseListForPrinter_Body_cancel_cashToMarchant = new ArrayList<String>();
        responseListForPrinter_Header = new ArrayList<String>();

        // HEAD

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, recieptData[3].split("\\ ")[0]);
        responseListForPrinter_Header.add(2, recieptData[3].split("\\ ")[1]);
        responseListForPrinter_Header.add(3, recieptData[0]);
        responseListForPrinter_Header.add(4, getString(R.string.cashToMReversal));
        responseListForPrinter_Header.add(5, recieptData[11]);



        responseListForPrinter_Body_cancel_cashToMarchant.add(0, recieptData[16]); // Marchant Name
        responseListForPrinter_Body_cancel_cashToMarchant.add(1, recieptData[10]);      // Bill Number
        responseListForPrinter_Body_cancel_cashToMarchant.add(2, recieptData[15]);      // Label Name
        responseListForPrinter_Body_cancel_cashToMarchant.add(3, recieptData[6]);     // Destination Name
        responseListForPrinter_Body_cancel_cashToMarchant.add(4, recieptData[8]);   // Sender Name
        responseListForPrinter_Body_cancel_cashToMarchant.add(5, recieptData[5]);  // Amount
        responseListForPrinter_Body_cancel_cashToMarchant.add(6, recieptData[4]); // Fees
        responseListForPrinter_Body_cancel_cashToMarchant.add(7, totalAmountString);      // Total Amount
        responseListForPrinter_Body_cancel_cashToMarchant.add(8, recieptData[14]); // Transaction Code
        responseListForPrinter_Body_cancel_cashToMarchant.add(9, agentName); // Agent name
        responseListForPrinter_Body_cancel_cashToMarchant.add(10, recieptData[9]); // Attach Branch Name
        responseListForPrinter_Body_cancel_cashToMarchant.add(11, recieptData[12]); // Agent Country
        responseListForPrinter_Body_cancel_cashToMarchant.add(12, recieptData[2]); // Comments

    }

    private boolean askForPermission(String permission, Integer requestCode) {
        boolean ret = false;
        if (ContextCompat.checkSelfPermission(SucessReceiptTransactionCancelReceipt.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SucessReceiptTransactionCancelReceipt.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SucessReceiptTransactionCancelReceipt.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SucessReceiptTransactionCancelReceipt.this, new String[]{permission}, requestCode);
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
            Toast.makeText(SucessReceiptTransactionCancelReceipt.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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
                        connect_to_Printer = new SucessReceiptTransactionCancelReceipt.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }
}



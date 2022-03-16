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


public class SucessReceiptReprintCashToCashReceiveMoney extends AppCompatActivity implements TaskCompleteToPrint {
    TextView amountLetterTextview, dateRecepit, timeReceipt, commentsTitleReview, totalamountRecepit, currencyTypeReceipt, destinationNameReceipt, destinationNumberReceipt, transactionCodeRecepit, transactionIdReceipt, referenceNumber_textView, senderNameReceipt, senderNumberReceipt, attachBranceNameReceipt, cityReceipt, destinationCountryRecepit, amountRecepit, feesRecepit, agentNameRecepit, agentCountryRecepit;
    Button homeButton;
    String[] recieptData;
    TextView oldTxnidTitleTextViewReceipt, oldTxnidTextViewReceipt, titleTextView;
    LinearLayout oldTxnIdLL;
    private int txnType = 0;
    Double feesNull, feesAdd, totalAmount, amountAdd;
    String stringNumberWord;
    String[] amountStingArray;
    int numberToWordInteger, numberToWordFloat;

    ComponentMd5SharedPre mComponentInfo;

    Bundle bundle = new Bundle();
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
    SucessReceiptReprintCashToCashReceiveMoney.ConnectToPrinter connect_to_Printer;
    String printdata;
    ArrayList<String> responseListForPrinter_Header, responseListForPrinter_Body;
    AlertDialog alertDialog;
    String printValues = null;
    private static final int REQUEST_BLUETOOTH_PHONE = 0;
    private static final int REQUEST_BLUETOOTH_PHONE_ADMIN = 1;
    private static final int REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED = 2;
    Bitmap bitMap;
    String[] oneToNineArray, hundredToCroreArray, tenToNinteenArray, twentyToNinty;


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

        setContentView(R.layout.success_receipt_cashtocash_receivemoney_samecountry);
        Toolbar mToolbar;
        Button printButton;
        dateRecepit = (TextView) findViewById(R.id.dateRecepit);
        timeReceipt = (TextView) findViewById(R.id.timeReceipt);
        transactionIdReceipt = (TextView) findViewById(R.id.transactionIdReceipt);

        senderNameReceipt = (TextView) findViewById(R.id.senderNameReceipt);
        senderNumberReceipt = (TextView) findViewById(R.id.senderNumberReceipt);
        referenceNumber_textView = (TextView) findViewById(R.id.referenceNumber_textView);
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


        oneToNineArray = getResources().getStringArray(R.array.oneToNineArray);
        hundredToCroreArray = getResources().getStringArray(R.array.hundredToCroreArray);
        tenToNinteenArray = getResources().getStringArray(R.array.tenToNinteenArray);
        twentyToNinty = getResources().getStringArray(R.array.twentyToNinty);


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
        amountLetterTextview = (TextView) findViewById(R.id.amountLetterTextview);
        oldTxnIdLL = (LinearLayout) findViewById(R.id.oldTxnIdLL);

        //agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        // agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");

        // String aa = getIntent().getExtras().getString("data", "");
        String aa = mComponentInfo.getmSharedPreferences().getString("data", "");

        // currencySearchString = mComponentInfo.getmSharedPreferences().getString("currencySearchString", "");
        // senderNumberString = mComponentInfo.getmSharedPreferences().getString("senderNumberString", "");
        // senderNameString = mComponentInfo.getmSharedPreferences().getString("senderNameString", "");


        //amountSearch = mComponentInfo.getmSharedPreferences().getString("amountSearch", "");
        // amountSearchNoToWord = mComponentInfo.getmSharedPreferences().getString("amountSearchNoToWord", "");
        // commentString = mComponentInfo.getmSharedPreferences().getString("commentString", "");


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

        /*if(recieptData[6].equalsIgnoreCase("null")) {
            feesNull = 0.0;
            feesRecepit.setText("0.0");
            amountRecepit.setText(recieptData[7]);
            totalamountRecepit.setText(recieptData[7]);
        }
        else
        {
            feesRecepit.setText(recieptData[6]);
            amountRecepit.setText(recieptData[7]);

            amountAdd = Double.parseDouble(recieptData[7]);
            feesAdd = Double.parseDouble(recieptData[6]);
            totalAmount = (amountAdd - feesAdd);

            totalAmountString = Double.toString(totalAmount);
            totalamountRecepit.setText(recieptData[7]);
            amountRecepit.setText(totalAmountString);
        }*/


        amountRecepit.setText(recieptData[7]);
        currencyTypeReceipt.setText(recieptData[14]);
        senderNameReceipt.setText(recieptData[11]);
        senderNumberReceipt.setText(recieptData[10]);
        destinationNumberReceipt.setText(recieptData[5]);
        destinationNameReceipt.setText(recieptData[9]);
        attachBranceNameReceipt.setText(recieptData[8]);
        agentNameRecepit.setText(recieptData[15]);
        agentCountryRecepit.setText(recieptData[4]);
        destinationCountryRecepit.setText(recieptData[4]);
        commentsTitleReview.setText(recieptData[2]);
        referenceNumber_textView.setText(recieptData[17]);


       // str=xpath.replaceAll("\\.", "/*/");

        String aaaaa=recieptData[7];
        String  str=aaaaa.replaceAll("\\.", "-");
        String[] arrrString=str.split("\\-");
        int result = Integer.parseInt(arrrString[0]);

        convertNoToWord(result);


        oldTxnIdLL.setVisibility(View.VISIBLE);
        oldTxnidTextViewReceipt.setText(recieptData[16]);


        generateAndSetData();   // print

        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SucessReceiptReprintCashToCashReceiveMoney.this.finish();
            }
        });


        printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mayRequestPhoneState();
                if (askForPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH_PHONE)) {
                    if (askForPermission(Manifest.permission.BLUETOOTH_ADMIN, REQUEST_BLUETOOTH_PHONE_ADMIN)) {
                        connect_to_Printer = new SucessReceiptReprintCashToCashReceiveMoney.ConnectToPrinter();
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

            PrintUtils mPrintUtils = new PrintUtils(SucessReceiptReprintCashToCashReceiveMoney.this, mPrinter, bitMap, SucessReceiptReprintCashToCashReceiveMoney.this);
            mPrintUtils.print_Header(responseListForPrinter_Header);
            mPrintUtils.print_reprint_cashToCashReceiveMoneySameCountry(responseListForPrinter_Body);
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

                                mPrinterManager = PrinterManager.getInstance(SucessReceiptReprintCashToCashReceiveMoney.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, SucessReceiptReprintCashToCashReceiveMoney.this);
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
                        SucessReceiptReprintCashToCashReceiveMoney.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(SucessReceiptReprintCashToCashReceiveMoney.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    SucessReceiptReprintCashToCashReceiveMoney.PrintToPrinter printToPrinter = new SucessReceiptReprintCashToCashReceiveMoney.PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(SucessReceiptReprintCashToCashReceiveMoney.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = SucessReceiptReprintCashToCashReceiveMoney.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
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
        mDialog = new ProgressDialog(SucessReceiptReprintCashToCashReceiveMoney.this);
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
                        connect_to_Printer = new SucessReceiptReprintCashToCashReceiveMoney.ConnectToPrinter();
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
                    connect_to_Printer = new SucessReceiptReprintCashToCashReceiveMoney.ConnectToPrinter();
                    connect_to_Printer.execute();
                    // printerOptions.setVisibility(View.VISIBLE);
                }
                break;
        }

    }


    public String convertNoToWord(int number) {
        int n = 1;
        int word;
        stringNumberWord = "";
        while (number != 0) {
            switch (n) {
                case 1:
                    word = number % 100;
                    pass(word);
                    if (number > 100 && number % 100 != 0) {
                        show(" ");
                    }
                    number /= 100;
                    break;

                case 2:
                    word = number % 10;
                    if (word != 0) {
                        show(" ");
                        show(hundredToCroreArray[0]);
                        show(" ");
                        pass(word);
                    }
                    number /= 10;
                    break;

                case 3:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(hundredToCroreArray[1]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;

                case 4:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(hundredToCroreArray[2]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;

                case 5:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(hundredToCroreArray[3]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;

            }
            n++;
        }
        System.out.println(stringNumberWord);


        if(stringNumberWord.equalsIgnoreCase(""))
        {
            amountLetterTextview.setText(getString(R.string.zero));
        }
        else
        {
            amountLetterTextview.setText(stringNumberWord);
        }

       //  Toast.makeText(SucessReceiptReprintCashToCashReceiveMoney.this, stringNumberWord, Toast.LENGTH_LONG).show();

        return stringNumberWord;
    }

    public void pass(int number) {
        int word, q;
        if (number < 10) {
            show(oneToNineArray[number]);
        }
        if (number > 9 && number < 20) {
            show(tenToNinteenArray[number - 10]);
        }
        if (number > 19) {
            word = number % 10;
            if (word == 0) {
                q = number / 10;
                show(twentyToNinty[q - 2]);
            } else {
                q = number / 10;
                show(oneToNineArray[word]);
                show(" ");
                show(twentyToNinty[q - 2]);
            }
        }
    }

    public void show(String s) {
        String st;
        st = stringNumberWord;
        stringNumberWord = s;
        stringNumberWord += st;
        System.out.println(stringNumberWord);

    }

    private void generateAndSetData() {

        int a = recieptData.length;

        responseListForPrinter_Body = new ArrayList<String>();
        responseListForPrinter_Header = new ArrayList<String>();

        // Header

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, recieptData[3].split("\\ ")[0]);
        responseListForPrinter_Header.add(2, recieptData[3].split("\\ ")[1]);
        responseListForPrinter_Header.add(3, recieptData[0]);         // transaction ID
        responseListForPrinter_Header.add(4, getString(R.string.cashToCashReceiveReprint_new));

        /*if(recieptData[6].equalsIgnoreCase("null")) {
            recieptData[6] = "0.0";

            responseListForPrinter_Body.add(5, recieptData[7]);  // Amount
            responseListForPrinter_Body.add(6, recieptData[6]);  // fees
            responseListForPrinter_Body.add(7, recieptData[7]);  // Total Amount

        }
        else
        {
            responseListForPrinter_Body.add(5, totalAmountString);  // Amount
            responseListForPrinter_Body.add(6, recieptData[6]);  // fees
            responseListForPrinter_Body.add(7, recieptData[7]);  // Total Amount

        }*/


        responseListForPrinter_Body.add(0, recieptData[16]);  // OLD Transaction
        responseListForPrinter_Body.add(1, recieptData[17]);  // Reference
        responseListForPrinter_Body.add(2, recieptData[11]);  // sender Name
        responseListForPrinter_Body.add(3, recieptData[4]);             // Destination Country
        responseListForPrinter_Body.add(4, recieptData[9]);      // destination name
        responseListForPrinter_Body.add(5, recieptData[5]);  // destination number
        responseListForPrinter_Body.add(6, recieptData[14]); // Curencey Type
        responseListForPrinter_Body.add(7, recieptData[7]); // Amount

        if(stringNumberWord.equalsIgnoreCase(""))  // if amount 0.00 then print zero
        {
            stringNumberWord=getString(R.string.zero);
            responseListForPrinter_Body.add(8, stringNumberWord);   // amount number To Word Convert
        }
        else {
             responseListForPrinter_Body.add(8, stringNumberWord);   // amount number To Word Convert
          }

        responseListForPrinter_Body.add(9, recieptData[15]);   //  agent Name
        responseListForPrinter_Body.add(10, recieptData[8]);   // Agnet branch Name
        responseListForPrinter_Body.add(11, recieptData[4]);  // Agent Country
        responseListForPrinter_Body.add(12, recieptData[2]);  // comment
    }

    private boolean askForPermission(String permission, Integer requestCode) {
        boolean ret = false;
        if (ContextCompat.checkSelfPermission(SucessReceiptReprintCashToCashReceiveMoney.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SucessReceiptReprintCashToCashReceiveMoney.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SucessReceiptReprintCashToCashReceiveMoney.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SucessReceiptReprintCashToCashReceiveMoney.this, new String[]{permission}, requestCode);
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
            Toast.makeText(SucessReceiptReprintCashToCashReceiveMoney.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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
                        connect_to_Printer = new SucessReceiptReprintCashToCashReceiveMoney.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }


}



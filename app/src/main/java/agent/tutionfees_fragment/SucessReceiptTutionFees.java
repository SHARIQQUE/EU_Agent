package agent.tutionfees_fragment;

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
import callback.TaskCompleteToPrint;
import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;
import printer_utilities.PrintUtils;
import printer_utilities.PrinterListShowActivity;
import printer_utilities.PrinterManager;

/**
 * Created by Shariq on 15-06-2017.
 */


public class SucessReceiptTutionFees extends AppCompatActivity implements TaskCompleteToPrint {
    Button homeButton;
    ComponentMd5SharedPre mComponentInfo;
    String agentCode, agentName,countrySelectionString;
    Button printButton;
    TextView   dateRecepit,timeReceipt,transactionIdReceipt;
    String[] oneToNineArray, hundredToCroreArray, tenToNinteenArray, twentyToNinty;

int amountInLetter;
    String date, time;
    TextView  agentNameRecepit_textView,agentBranceName_textview,totalAmount_textView,vat_textView,fees_textView, transactionAmount_textview,className_textView,option_textView,payerMobileNo_textView,payerName_textView,payerEmail_textView,level_textview, email_student_textView, birthDate_student_textView,genderStudent_textView,firstName_student_textview,   name_student_textview,schoolCode_textView,region_textView,division_textview,subdivision_textview,city_textview,schoolName_textview,studentRegistrationNumber_textview;
    TextView   amount_letter_textview,paymentOption_textview;
    String[] feeNameArray_fromServer,feeAmountArray_fromServer,feeAmountEnteredArr_fromServer,feeNameEnteredArr_fromServer;
    LinearLayout fees_master_linearLayout;


    Bitmap bitMap;

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

        setContentView(R.layout.sucess_receipt_tution_fees);
        Toolbar mToolbar;


        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        dateRecepit = (TextView) findViewById(R.id.dateRecepit);
        timeReceipt = (TextView) findViewById(R.id.timeReceipt);
        transactionIdReceipt = (TextView) findViewById(R.id.transactionIdReceipt);


        oneToNineArray = getResources().getStringArray(R.array.oneToNineArray);
        hundredToCroreArray = getResources().getStringArray(R.array.hundredToCroreArray);
        tenToNinteenArray = getResources().getStringArray(R.array.tenToNinteenArray);
        twentyToNinty = getResources().getStringArray(R.array.twentyToNinty);

        schoolCode_textView = (TextView) findViewById(R.id.schoolCode_textView);
        region_textView = (TextView) findViewById(R.id.region_textView);
        division_textview = (TextView) findViewById(R.id.division_textview);
        subdivision_textview = (TextView) findViewById(R.id.subdivision_textview);
        city_textview = (TextView) findViewById(R.id.city_textview);
        schoolName_textview = (TextView) findViewById(R.id.schoolName_textview);
        studentRegistrationNumber_textview = (TextView) findViewById(R.id.studentRegistrationNumber_textview);
        name_student_textview = (TextView) findViewById(R.id.name_student_textview);
        firstName_student_textview = (TextView) findViewById(R.id.firstName_student_textview);
        genderStudent_textView = (TextView) findViewById(R.id.genderStudent_textView);
        birthDate_student_textView = (TextView) findViewById(R.id.birthDate_student_textView);
        email_student_textView = (TextView) findViewById(R.id.email_student_textView);

        payerMobileNo_textView = (TextView) findViewById(R.id.payerMobileNo_textView);
        payerName_textView = (TextView) findViewById(R.id.payerName_textView);
        payerEmail_textView = (TextView) findViewById(R.id.payerEmail_textView);


        level_textview = (TextView) findViewById(R.id.level_textview);
        option_textView = (TextView) findViewById(R.id.option_textView);
        className_textView = (TextView) findViewById(R.id.className_textView);

        transactionAmount_textview = (TextView) findViewById(R.id.transactionAmount_textview);
        fees_textView = (TextView) findViewById(R.id.fees_textView);
        vat_textView = (TextView) findViewById(R.id.vat_textView);
        totalAmount_textView = (TextView) findViewById(R.id.totalAmount_textView);
        amount_letter_textview = (TextView) findViewById(R.id.amount_letter_textview);

        agentNameRecepit_textView = (TextView) findViewById(R.id.agentNameRecepit_textView);
        agentBranceName_textview = (TextView) findViewById(R.id.agentBranceName_textview);

        paymentOption_textview = (TextView) findViewById(R.id.paymentOption_textview);




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


            String dateTimeSaperate = mComponentInfo.getDateTime_reprintTuitionFees();
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



            feeNameArray_fromServer= mComponentInfo.getFeeNameArray_fromServer();
            feeAmountArray_fromServer= mComponentInfo.getFeeAmountArray_fromServer();
            feeAmountEnteredArr_fromServer=mComponentInfo.getFeeAmountEnteredArr_fromServer();
            feeNameEnteredArr_fromServer = mComponentInfo.getFeeNameEnteredArr_fromServer();



            transactionIdReceipt.setText(mComponentInfo.getTransactionId_reprint());
            schoolCode_textView.setText(mComponentInfo.getSchool_code());
            region_textView.setText(mComponentInfo.getRegion_name());
            division_textview.setText(mComponentInfo.getDivision());
            subdivision_textview.setText(mComponentInfo.getSubdivision());
            city_textview.setText(mComponentInfo.getCity());

            schoolName_textview.setText(mComponentInfo.getSchool_name());
            studentRegistrationNumber_textview.setText(mComponentInfo.getStudentRegistrationNumberString());
            name_student_textview.setText(mComponentInfo.getStudentNameString());
            firstName_student_textview.setText(mComponentInfo.getStudentFirstNameString());
            genderStudent_textView.setText(mComponentInfo.getGenderName());
            birthDate_student_textView.setText(mComponentInfo.getStudent_dateOfbirthdate());
            email_student_textView.setText(mComponentInfo.getStudent_email());

            payerMobileNo_textView.setText(mComponentInfo.getPayerMobileNumber());
            payerName_textView.setText(mComponentInfo.getPayerName());
            payerEmail_textView.setText(mComponentInfo.getPayerEmail());


            level_textview.setText(mComponentInfo.getLevelTypeName());
            option_textView.setText(mComponentInfo.getOptionTypeName());
            className_textView.setText(mComponentInfo.getClassName());

            transactionAmount_textview.setText(mComponentInfo.getTransactionAmount());
            fees_textView.setText(mComponentInfo.getFees_tariff());
            vat_textView.setText(mComponentInfo.getVat_tariff());

            totalAmount_textView.setText(mComponentInfo.getTotalAmount());


            Double data = new Double(mComponentInfo.getTotalAmount());

            int value = data.intValue();
            convertNoToWord(value);


            agentNameRecepit_textView.setText(agentName);
           // agentBranceName_textview.setText(mComponentInfo.getAgentBranch_reprint());
            agentBranceName_textview.setText(getString(R.string.express_union_capital)); // goning chnage on skype 21 aug 2019
            // YEs instead of EU BUEA I, we want to put there the fix value "EXPRESS UNION" No matter the agent or franchisee parent branch





            paymentOption_textview.setText(getString(R.string.full_payment));





            fees_master_linearLayout=(LinearLayout)findViewById(R.id.fees_master_linearLayout);

            for (int i = 0; i < feeNameArray_fromServer.length; i++) {

                View view = getLayoutInflater().inflate(R.layout.fees_item_receiptpage, null);


                if (feeAmountEnteredArr_fromServer[i].equalsIgnoreCase("0")) {
                    System.out.println("test");
                } else {


                    TextView feeNameTitle = (TextView) view.findViewById(R.id.examFees_title_textview);
                    TextView feeNameValue = (TextView) view.findViewById(R.id.examFees_textview);


                    feeNameTitle.setText(feeNameEnteredArr_fromServer[i]);
                    feeNameValue.setText(feeAmountEnteredArr_fromServer[i]);


                    fees_master_linearLayout.addView(view);
                }
            }



            generateAndSetData();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SucessReceiptTutionFees.this, getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

        }


        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent5= new Intent(SucessReceiptTutionFees.this, TutionFeesMenu.class);

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
                        connect_to_Printer = new SucessReceiptTutionFees.ConnectToPrinter();
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


        Intent intent5= new Intent(SucessReceiptTutionFees.this, TutionFeesMenu.class);

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
    ArrayList<String> responseListForPrinter_Header, responseListForPrinter_Body,responseListForPrinter_Body_Part_2,responseListForPrinter_Body_feesType;

    String printValues = null;
    private static final int REQUEST_BLUETOOTH_PHONE = 0;
    private static final int REQUEST_BLUETOOTH_PHONE_ADMIN = 1;
    private static final int REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED = 2;



    private void generateAndSetData() {


        responseListForPrinter_Body = new ArrayList<String>();
        responseListForPrinter_Header = new ArrayList<String>();
        responseListForPrinter_Body_feesType = new ArrayList<String>();
        responseListForPrinter_Body_Part_2 = new ArrayList<String>();

        //    HEADER

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, date);
        responseListForPrinter_Header.add(2, time);
        responseListForPrinter_Header.add(3, mComponentInfo.getTransactionId_reprint());

          // School Detail


        responseListForPrinter_Body.add(0, mComponentInfo.getSchool_code());  // scholl code

       /* responseListForPrinter_Body.add(1, mComponentInfo.getRegion_name());  // region
        responseListForPrinter_Body.add(2, mComponentInfo.getDivision());     // division
        responseListForPrinter_Body.add(3, mComponentInfo.getSubdivision());   // sub devision
        responseListForPrinter_Body.add(4, mComponentInfo.getCity());          // city
        */
        responseListForPrinter_Body.add(1, mComponentInfo.getSchool_name()); // school name

        // Student Details

        responseListForPrinter_Body.add(2, mComponentInfo.getStudentRegistrationNumberString());   // registration number
        responseListForPrinter_Body.add(3, mComponentInfo.getStudentNameString());       // name
        responseListForPrinter_Body.add(4, mComponentInfo.getStudentFirstNameString());    // Firt name
        responseListForPrinter_Body.add(5, mComponentInfo.getGenderName());         // gender
        responseListForPrinter_Body.add(6, mComponentInfo.getStudent_dateOfbirthdate());         // Birt date
        responseListForPrinter_Body.add(7, mComponentInfo.getStudent_email());         // Email


        // PAyer  Details


        responseListForPrinter_Body.add(8, mComponentInfo.getPayerMobileNumber());   // payer Mobile number
        responseListForPrinter_Body.add(9, mComponentInfo.getPayerName());       // payer name
        responseListForPrinter_Body.add(10, mComponentInfo.getPayerEmail());    // payer email

        // Class  Details

        responseListForPrinter_Body.add(11, mComponentInfo.getLevelTypeName());   // level
        responseListForPrinter_Body.add(12, mComponentInfo.getOptionTypeName());       // Option
        responseListForPrinter_Body.add(13, mComponentInfo.getClassName());    // Name






        for (int k = 0; k < feeNameArray_fromServer.length; k++)
            {

                if(feeAmountEnteredArr_fromServer[k].equalsIgnoreCase("0"))
                {
                    System.out.println("test");
                }
                else {
                    responseListForPrinter_Body_feesType.add(0, feeNameArray_fromServer[k]+"|"+feeAmountEnteredArr_fromServer[k]);
                }

            }

        responseListForPrinter_Body_Part_2.add(0, mComponentInfo.getTransactionAmount());   // Transaction amount
        responseListForPrinter_Body_Part_2.add(1, mComponentInfo.getFees_tariff());       // Fees
        responseListForPrinter_Body_Part_2.add(2, mComponentInfo.getVat_tariff());    // vat
        responseListForPrinter_Body_Part_2.add(3, mComponentInfo.getTotalAmount());    // Total amount
        responseListForPrinter_Body_Part_2.add(4, stringNumberWord);    // Total amount
        responseListForPrinter_Body_Part_2.add(5, agentName);   // Parent Agent name
       //  responseListForPrinter_Body_Part_2.add(6, mComponentInfo.getAgentBranch_reprint());
        responseListForPrinter_Body_Part_2.add(6, getString(R.string.express_union_capital));     // goning chnage on skype 21 aug 2019
        // YEs instead of EU BUEA I, we want to put there the fix value "EXPRESS UNION" No matter the agent or franchisee parent branch



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

                                mPrinterManager = PrinterManager.getInstance(SucessReceiptTutionFees.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, SucessReceiptTutionFees.this);
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
                        SucessReceiptTutionFees.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(SucessReceiptTutionFees.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), CommonUtilities.REQUEST_ENABLE_BT);
                    break;

                case 2:

                    //      doPrinting(responseListForPrinter);
                    // printTransactionreciept("Customer Copy");

                    SucessReceiptTutionFees.PrintToPrinter printToPrinter = new SucessReceiptTutionFees.PrintToPrinter();
                    printToPrinter.execute();
                    break;

                case 3:
                    hideProgressDialog();
                    // commonDisplay.dismissProgressDialog();
                    startActivityForResult(new Intent(SucessReceiptTutionFees.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = SucessReceiptTutionFees.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
        }

    }

    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {

            PrintUtils mPrintUtils = new PrintUtils(SucessReceiptTutionFees.this, mPrinter, bitMap, SucessReceiptTutionFees.this);

            mPrintUtils.print_Header_tutionfees(responseListForPrinter_Header);

            mPrintUtils.print_tutionFees_part_1(responseListForPrinter_Body);
            mPrintUtils.print_feesType_tutionFees(responseListForPrinter_Body_feesType);
            mPrintUtils.print_tutionFees_part_2(responseListForPrinter_Body_Part_2);


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
        mDialog = new ProgressDialog(SucessReceiptTutionFees.this);
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
                        connect_to_Printer = new SucessReceiptTutionFees.ConnectToPrinter();
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
                    connect_to_Printer = new SucessReceiptTutionFees.ConnectToPrinter();
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
        if (ContextCompat.checkSelfPermission(SucessReceiptTutionFees.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SucessReceiptTutionFees.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SucessReceiptTutionFees.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SucessReceiptTutionFees.this, new String[]{permission}, requestCode);
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
            Toast.makeText(SucessReceiptTutionFees.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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
                        connect_to_Printer = new SucessReceiptTutionFees.ConnectToPrinter();
                        connect_to_Printer.execute();
                    }
                } else {
                }
                break;
        }
    }


    //////////////////// Amount To Word ////////////
    String stringNumberWord;


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

        if (stringNumberWord.equalsIgnoreCase("")) {
            amount_letter_textview.setText(getString(R.string.zero));
        } else {
            amount_letter_textview.setText(stringNumberWord);
        }
        //   Toast.makeText(CashToCashReceiveMoneySameCountryNew.this,stringNumberWord,Toast.LENGTH_LONG).show();
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

    //////////////////////////////////////////////
}



   
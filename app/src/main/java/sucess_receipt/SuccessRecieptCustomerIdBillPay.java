package sucess_receipt;

        import android.app.AlertDialog;
        import android.content.SharedPreferences;
        import android.content.res.Configuration;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;

        import adapter.PaidBillAdapter;
        import agent.activities.R;
        import callback.TaskCompleteToPrint;
        import commonutilities.ComponentMd5SharedPre;


        import com.datecs.api.printer.Printer;
        import com.datecs.api.printer.ProtocolAdapter;

        import java.util.Locale;

        import commonutilities.CommonUtilities;
        import printer_utilities.PrintUtils;
        import printer_utilities.PrinterListShowActivity;
        import printer_utilities.PrinterManager;

        import android.bluetooth.BluetoothAdapter;
        import android.app.ProgressDialog;
        import android.os.Handler;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.content.Intent;
        import android.widget.Toast;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.Manifest;
        import android.os.Build;
        import android.app.Activity;
        import android.content.pm.PackageManager;

/**
 * Created by Shariq on 15-06-2017.
 */


public class SuccessRecieptCustomerIdBillPay extends AppCompatActivity implements TaskCompleteToPrint {
    TextView dateRecepit, timeReceipt, CommentRecepitTextView,
             CustomerIdReceipt,  destinationNumberReceipt,
            transactionCodeRecepit, transactionIdReceipt, marchantNameReceipt,
            agentBranceNameReceipt,
            agentNameRecepit, agentCountryRecepit;
    Button homeButton;
    String[] recieptData;
    ComponentMd5SharedPre mComponentInfo;
    String agentCode, agentName;
ListView paidBillsListView;
    Bundle bundle = new Bundle();
    String totalAmountString, subscriberNoDestinationNo, receiptTypeCustomerid;
    String spinnerCountryString, tariffAmountFee;
    LinearLayout oldTxnIdLL;
    //========================================================
    private ProgressDialog mDialog;
    private ProtocolAdapter mProtocolAdapter;
    String feesBillPay, amountBillPay, readerAddress, printerAddress;
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
    AlertDialog alertDialog;
    String printValues = null;
    private static final int REQUEST_BLUETOOTH_PHONE = 0;
    private static final int REQUEST_BLUETOOTH_PHONE_ADMIN = 1;
    private static final int REQUEST_BLUETOOTH_PHONE_BLUETOOTH_PRIVILEGED = 2;
    Bitmap bitMap;
    Button printButton;

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

        setContentView(R.layout.success_reciept_customer_id_billpay);
        Toolbar mToolbar;

        dateRecepit = (TextView) findViewById(R.id.dateRecepit);
        timeReceipt = (TextView) findViewById(R.id.timeReceipt);
        transactionIdReceipt = (TextView) findViewById(R.id.transactionIdReceipt);

        marchantNameReceipt = (TextView) findViewById(R.id.marchantNameReceipt);

        agentBranceNameReceipt = (TextView) findViewById(R.id.agentBranceNameReceipt);



        agentNameRecepit = (TextView) findViewById(R.id.agentNameRecepit);
        agentCountryRecepit = (TextView) findViewById(R.id.agentCountryRecepit);

        destinationNumberReceipt = (TextView) findViewById(R.id.destinationNumberReceipt);

        paidBillsListView=(ListView)findViewById(R.id.paidBillsListViewBillPay) ;
        transactionCodeRecepit = (TextView) findViewById(R.id.transactionCodeRecepit);
        CustomerIdReceipt = (TextView) findViewById(R.id.CustomerIdReceipt);
        CommentRecepitTextView = (TextView) findViewById(R.id.CommentRecepitTextView);


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
        feesBillPay = mComponentInfo.getmSharedPreferences().getString("feesBillPay", "");
        amountBillPay = mComponentInfo.getmSharedPreferences().getString("amountBillPay", "");

      //  receiptTypeCustomerid = mComponentInfo.getmSharedPreferences().getString("receiptTypeCustomerid", "");   //  // if select  Customer Id  Then Receipt
        String aa = mComponentInfo.getmSharedPreferences().getString("data", "");

        // String labelNameString = getIntent().getExtras().getString("labelNameString", "");
        // String senderNameString = getIntent().getExtras().getString("senderNameString", "");
        // String spinnerCountryString = getIntent().getExtras().getString("spinnerCountryString", "");
        //  String spinnerBillerNameString = getIntent().getExtras().getString("spinnerBillerNameString", "");




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

            CustomerIdReceipt.setText(recieptData[10]);
            CommentRecepitTextView.setText(recieptData[2]);
            amountBillPay=recieptData[7];
            feesBillPay=recieptData[6];
            Double amountAdd = Double.parseDouble(amountBillPay);
            Double feesAdd = Double.parseDouble(feesBillPay);
            Double tempdata = (amountAdd + feesAdd);
            totalAmountString = Double.toString(tempdata);

            // transactionCodeRecepit.setText("");

            marchantNameReceipt.setText(recieptData[12]);

            // senderNameReceipt.setText("");
            //destinationNumberReceipt.setText(recieptData[5]);

            agentBranceNameReceipt.setText(recieptData[14]);
            agentNameRecepit.setText(agentName);
            agentCountryRecepit.setText(recieptData[4]);

            generateAndSetData();


        homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuccessRecieptCustomerIdBillPay.this.finish();
            }
        });


        printButton = (Button) findViewById(R.id.printButton);
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

    //0 = CASHIB , 1 = Reprint CashIN
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

                                mPrinterManager = PrinterManager.getInstance(SuccessRecieptCustomerIdBillPay.this);
                                mPrinterManager.disconnect();
                                Thread.sleep(1500);
                                mPrinterManager.connect(printerAddress, SuccessRecieptCustomerIdBillPay.this);
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
                        SuccessRecieptCustomerIdBillPay.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(SuccessRecieptCustomerIdBillPay.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
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
                    startActivityForResult(new Intent(SuccessRecieptCustomerIdBillPay.this, PrinterListShowActivity.class), 300);
                    break;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // commonDisplay.showProgressDialog(QuickSale.this.getActivity(),
            // "Connecting To Printer");
            showProgressDialog(getString(R.string.pleasewait));

            SharedPreferences sp = SuccessRecieptCustomerIdBillPay.this.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            printerAddress = sp.getString("printerAddress", "");
            Log.e("printerAddress", printerAddress);
            // printerAddress="00:1B:35:03:A9:1C";
        }

    }

    private class PrintToPrinter extends AsyncTask<String, Void, String> {
        int status = 0;

        @Override
        protected String doInBackground(String... params) {


            PrintUtils mPrintUtils = new PrintUtils(SuccessRecieptCustomerIdBillPay.this, mPrinter, bitMap,SuccessRecieptCustomerIdBillPay.this);
            mPrintUtils.print_Header_billpayCustomerId(responseListForPrinter_Header);
            mPrintUtils.printPaidInvoices_CustomerID_BillPay(mComponentInfo.getPaidBillsList());
            mPrintUtils.print_billPay_footer(responseListForPrinter_Body);

            mPrintUtils.print_Footer();  // image
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
        mDialog = new ProgressDialog(SuccessRecieptCustomerIdBillPay.this);
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

    private void generateAndSetData() {


        int a = recieptData.length;
        responseListForPrinter_Body = new ArrayList<String>();
        responseListForPrinter_Header = new ArrayList<String>();

        responseListForPrinter_Header.add(0, "Express Union Finance SA".toUpperCase());
        responseListForPrinter_Header.add(1, recieptData[3].split("\\ ")[0]);
        responseListForPrinter_Header.add(2, recieptData[3].split("\\ ")[1]);
        responseListForPrinter_Header.add(3, recieptData[0]);
        responseListForPrinter_Header.add(4, getString(R.string.billPay));
        responseListForPrinter_Header.add(5, recieptData[12]);


        responseListForPrinter_Body.add(0, agentName);    // Agent name
        responseListForPrinter_Body.add(1, recieptData[14]);   //  Agent Branch name
        responseListForPrinter_Body.add(2, recieptData[4]);   // Agent Country
        responseListForPrinter_Body.add(3, recieptData[2]);   // comment


        PaidBillAdapter adapter=new PaidBillAdapter(this,mComponentInfo.getPaidBillsList());
        paidBillsListView.setAdapter(adapter);

    }

    private boolean askForPermission(String permission, Integer requestCode) {
        boolean ret = false;
        if (ContextCompat.checkSelfPermission(SuccessRecieptCustomerIdBillPay.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SuccessRecieptCustomerIdBillPay.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SuccessRecieptCustomerIdBillPay.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SuccessRecieptCustomerIdBillPay.this, new String[]{permission}, requestCode);
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
            Toast.makeText(SuccessRecieptCustomerIdBillPay.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
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



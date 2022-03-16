package billpay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import adapter.BillSelectionAdapter;
import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.BillModel;
import model.GeneralResponseModel;
import agent.activities.DisplayActivity;
import agent.activities.OTPVerificationActivity;
import sucess_receipt.SuccessRecieptCustomerIdBillPay;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;


/**
 * Created by AdityaBugalia on 14/10/16.
 */

public class BillSelectionCustomerFinal extends AppCompatActivity implements View.OnClickListener, ServerResponseParseCompletedNotifier, AutoCompleteTextView.OnEditorActionListener {

    ComponentMd5SharedPre mComponentInfo;
    LinearLayout progressDialog_LL;
    Button stopBillPayButton;
    TextView titleTextView;
    ListView listView;
    private ProgressDialog mDialog;
String sourceMobileNumberBillPay;

    int nextIndex = 0;
    boolean isStopRequested = false, isBillPaymentComplete = false;
    String billSelectionDataString, agentCode, accountTypeCodeString;
    String[] billSelectionDataArray, selectedInvoiceArray;
    ArrayList<BillModel> selectedBillList;

    String tempData="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "en";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.bill_selection_customer_final);
        billSelectionDataString = mComponentInfo.getmSharedPreferences()
                .getString("billSelectionData", "");
        agentCode = mComponentInfo.getmSharedPreferences()
                .getString("agentCode", "");
        billSelectionDataArray = billSelectionDataString.split("\\|");
        selectedInvoiceArray = billSelectionDataArray[4].split("\\,");
        listView = (ListView) findViewById(R.id.listView_BillSelectionFinal);

        progressDialog_LL = (LinearLayout) findViewById(R.id.progressDialog_LL_BillPayFinal);
        titleTextView = (TextView) findViewById(R.id.titleTextVie_BillPayFinal);
        stopBillPayButton = (Button) findViewById(R.id.stop_BillPayFinalScreen);
        stopBillPayButton.setOnClickListener(this);
        accountTypeCodeString = mComponentInfo.getmSharedPreferences().getString("payerAccountCodeString", "");
        sourceMobileNumberBillPay= mComponentInfo.getmSharedPreferences().getString("sourceMobileNumberBillPay", "");
        ViewGeneration vg = new ViewGeneration(0,"","","");

        vg.execute();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.stop_BillPayFinalScreen:
                if (isBillPaymentComplete) {

                    Intent i = new Intent(BillSelectionCustomerFinal.this, DisplayActivity.class);
                    startActivity(i);
                    BillSelectionCustomerFinal.this.finish();
                } else {
                    isStopRequested = true;
                    stopBillPayButton.setEnabled(false);
                }

                break;

        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }


    class ViewGeneration extends AsyncTask<Void, Void, Void> {
        int viewGenerationCase;
        String name="",customerName="",feeSupportedBy="";

        ViewGeneration(int viewGenerationCase,String name, String customerName,String feeSupportedBy) {
            this.viewGenerationCase = viewGenerationCase;
            this.name=name;
            this.customerName=customerName;
            this.feeSupportedBy=feeSupportedBy;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideProgressDialog();
            showProgressDialog(getString(R.string.pleasewait));


        }

        @Override
        protected Void doInBackground(Void... voids) {

            switch (viewGenerationCase) {
                case 0:
                    selectedBillList = new ArrayList<BillModel>();
                    BillModel billModel;
                    for (int i = 0; i < selectedInvoiceArray.length; i++) {
                        billModel = new BillModel();

                        String billDetailString = selectedInvoiceArray[i];
                        String[] tempBillDetailData = billDetailString.split("\\;");
                        billModel.setSelected(false);
                        billModel.setBillName(tempBillDetailData[0]);
                        billModel.setDueDate(tempBillDetailData[1]);
                        billModel.setAmount(tempBillDetailData[2]);
                        billModel.setFeeAmount(tempBillDetailData[3]);
                        selectedBillList.add(billModel);

                    }

                    break;

                case 1:
                    selectedBillList.get(nextIndex - 1).setSelected(true);
                    selectedBillList.get(nextIndex - 1).setName(name);
                    selectedBillList.get(nextIndex - 1).setCustomerName(customerName);

                    String fee=selectedBillList.get(nextIndex - 1).getFeeAmount();
                    String amount=selectedBillList.get(nextIndex - 1).getAmount();
                    String totalAmount="";
                    if(feeSupportedBy.equalsIgnoreCase("MER")){

                        totalAmount=Double.parseDouble(amount)-Double.parseDouble(fee)+"";
                    }else {
                        totalAmount=Double.parseDouble(amount)+Double.parseDouble(fee)+"";

                    }
                    selectedBillList.get(nextIndex - 1).setTotalAmount(amount);
                    selectedBillList.get(nextIndex - 1).setAmount(totalAmount);



                    break;
                case 2:
                    selectedBillList.get(nextIndex - 1).setSelected(true);
                    selectedBillList.get(nextIndex - 1).setName(name);
                    selectedBillList.get(nextIndex - 1).setCustomerName(customerName);

                     fee=selectedBillList.get(nextIndex - 1).getFeeAmount();
                     amount=selectedBillList.get(nextIndex - 1).getAmount();
                     totalAmount="";
                    if(feeSupportedBy.equalsIgnoreCase("MER")){

                        totalAmount=Double.parseDouble(amount)-Double.parseDouble(fee)+"";
                    }else {
                        totalAmount=Double.parseDouble(amount)+Double.parseDouble(fee)+"";

                    }
                    selectedBillList.get(nextIndex - 1).setTotalAmount(amount);
                    selectedBillList.get(nextIndex - 1).setAmount(totalAmount);

                    break;
                case 3:


                    break;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
            BillSelectionAdapter adapter;
            switch (viewGenerationCase) {
                case 0:

                    adapter = new BillSelectionAdapter(BillSelectionCustomerFinal.this, selectedBillList);
                    listView.setAdapter(adapter);

                    proceedBillPay();
                    break;

                case 1:
                    adapter = new BillSelectionAdapter(BillSelectionCustomerFinal.this, selectedBillList);
                    listView.setAdapter(adapter);

                  //  if (tempData.equalsIgnoreCase())

                    showSuccess(tempData);


                    break;
                case 2:
                    adapter = new BillSelectionAdapter(BillSelectionCustomerFinal.this, selectedBillList);
                    listView.setAdapter(adapter);
                    proceedBillPay();

                    break;
                case 3:

                    break;


            }
        }

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());

            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                Toast.makeText(BillSelectionCustomerFinal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();


                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                Toast.makeText(BillSelectionCustomerFinal.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_SHORT).show();
            } else {
                DataParserThread thread = new DataParserThread(BillSelectionCustomerFinal.this, mComponentInfo, BillSelectionCustomerFinal.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };

    private String generateBillPayData(String invoicenoString) {
        String jsonString = "";


        String pin = mComponentInfo.getMD5(agentCode + billSelectionDataArray[6]).toUpperCase();
        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);

            countryObj.put("source", sourceMobileNumberBillPay);
            countryObj.put("comments", "");
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("invoiceno", invoicenoString);
            countryObj.put("billercustid", "");
            countryObj.put("accountType", accountTypeCodeString);
            countryObj.put("destination", mComponentInfo.getmSharedPreferences().getString("billerCode", ""));
            // countryObj.put("billercode",   mComponentInfo.getmSharedPreferences().getString("billerCode",""));



            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }


        return jsonString;
    }

    private void proceedBillPay() {

        if (new InternetCheck().isConnected(BillSelectionCustomerFinal.this)) {
            // showProgressDialog("Please Wait");


            String requestData = generateBillPayData(selectedInvoiceArray[nextIndex].split("\\;")[0]);
            nextIndex++;
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

        //    callApi("getBillPayTransactionInJSON",requestData,109);


           new ServerTask(mComponentInfo, BillSelectionCustomerFinal.this, mHandler, requestData, "getBillPayTransactionInJSON", 109).start();
        } else {
            showCompletionLayout();
            Toast.makeText(BillSelectionCustomerFinal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(BillSelectionCustomerFinal.this,mComponentInfo,BillSelectionCustomerFinal.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(BillSelectionCustomerFinal.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(BillSelectionCustomerFinal.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(BillSelectionCustomerFinal.this);
        mDialog.setMessage(message);
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(false);
        mDialog.show();
    }

    private void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 109) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("accountTypeCodeString", accountTypeCodeString).commit();

                    mComponentInfo.getmSharedPreferences().edit().putString("process", "BILLPAY").commit();
                    Intent i = new Intent(BillSelectionCustomerFinal.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {

                    if (!isStopRequested) {
                        if (nextIndex < selectedInvoiceArray.length) {
                            ViewGeneration vg = new ViewGeneration(2,generalResponseModel.getResponseList().get("billercode"),generalResponseModel.getResponseList().get("sourcename"),generalResponseModel.getResponseList().get("feesupportedby"));
                            vg.execute();
                            //proceedBillPay();
                        } else {
                            ViewGeneration vg = new ViewGeneration(1,generalResponseModel.getResponseList().get("billercode"),generalResponseModel.getResponseList().get("sourcename"),generalResponseModel.getResponseList().get("feesupportedby"));

                            vg.execute();
                            tempData=generalResponseModel.getUserDefinedString();
                            //   showCompletionLayout();

                        }
                    } else {
                        // showCompletionLayout();

                    }

                }


            }


        } else {

            hideProgressDialog();
             showCompletionLayout();

            //showSuccess(generalResponseModel.getUserDefinedString());

            Toast.makeText(BillSelectionCustomerFinal.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }
    }


    private void showSuccess(String data) {

        String[] temp = data.split("\\|");

        System.out.print(temp);

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();

      //  String receiptTypeCustomerid="CustomeridReceiptType";   // if select  Customer Id  Then Receipt

    //    mComponentInfo.getmSharedPreferences().edit().putString("receiptTypeCustomerid",receiptTypeCustomerid).commit();
        mComponentInfo.setPaidBillsList(selectedBillList);
        Intent intent = new Intent(BillSelectionCustomerFinal.this, SuccessRecieptCustomerIdBillPay.class);
        startActivity(intent);
        BillSelectionCustomerFinal.this.finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 299:
                if (resultCode == RESULT_OK) {
                    if (new InternetCheck().isConnected(BillSelectionCustomerFinal.this)) {
                        //showProgressDialog("Please Wait");
                        String requestData = mComponentInfo.getmSharedPreferences().getString("requestData", "");

                   //     callApi("getBillPayTransactionInJSON",requestData,109);

                       new ServerTask(mComponentInfo, BillSelectionCustomerFinal.this, mHandler, requestData, "getBillPayTransactionInJSON", 109).start();
                    } else {
                        showCompletionLayout();
                        Toast.makeText(BillSelectionCustomerFinal.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
                    }

                } else {
                    showCompletionLayout();

                }
                break;
        }
    }


    private void showCompletionLayout() {
        isBillPaymentComplete = true;
        titleTextView.setText(getString(R.string.billhavebeenpaid));
        progressDialog_LL.setVisibility(View.GONE);
        stopBillPayButton.setText(getString(R.string.home_button));

    }

}

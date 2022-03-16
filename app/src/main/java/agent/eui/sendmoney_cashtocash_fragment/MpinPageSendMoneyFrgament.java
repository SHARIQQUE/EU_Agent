package agent.eui.sendmoney_cashtocash_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static android.content.Context.MODE_PRIVATE;
import static agent.thread.ServerTask.baseUrl;


public class MpinPageSendMoneyFrgament extends Fragment implements View.OnClickListener , ServerResponseParseCompletedNotifier {

    ModalSendMoney modalSendMoney=new ModalSendMoney();
    ComponentMd5SharedPre mComponentInfo;

    View view;
    EditText mpin_AutoCompleteTextView;
    String countrySelectionString,mpinString,agentName, agentCode;
    Button submitButton;

    private ProgressDialog mDialog;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());
            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
            } else {
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, MpinPageSendMoneyFrgament.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mComponentInfo = (ComponentMd5SharedPre) getActivity().getApplicationContext();

        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;


        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());

        view = inflater.inflate(R.layout.receiptpage_cashtocash_sendmoney, container, false); // Inflate the layout for this fragment


        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // currencySenderSelectionString = mComponentInfo.ge223tmSharedPreferences().getString("currency", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        TextView totalAmount_textview,destinationCurrency_textview, rate_textview, amountTopay_textview, destinationCountry_textview, vat_textview, reasonForTransfer_textview, sending_currency_textview, amount_textview, fees_textview, other_taxe_textview, answer_textview, question_textview, recipient_mobileNo_textview, recipient_firstname_textview, recipient_name_textview, recipient_email_textview, senderCity_textview, nationality_textview, email_textview_sender, homePhoneno_sender_textview, address_1_textview, address_2_textview, senderMobileNumber_textview, id_documentPlaceOfIssue_textview, senderFirstName_textView, id_documentCountryOfissue_textview, name_sender_textview, id_document_tye_textview, id_documentDateofissue_textview, idDocumentNumber_textview, gender_textview, profession_textview, resident_textview, senderCountry_textview;
        Button nextButton_reviewpage;

        senderMobileNumber_textview = (TextView) view.findViewById(R.id.senderMobileNumber_textview);
        senderFirstName_textView = (TextView) view.findViewById(R.id.senderFirstName_textView);
        name_sender_textview = (TextView) view.findViewById(R.id.name_sender_textview);
        id_document_tye_textview = (TextView) view.findViewById(R.id.id_document_tye_textview);
        idDocumentNumber_textview = (TextView) view.findViewById(R.id.idDocumentNumber_textview);
        id_documentDateofissue_textview = (TextView) view.findViewById(R.id.id_documentDateofissue_textview);
        id_documentCountryOfissue_textview = (TextView) view.findViewById(R.id.id_documentCountryOfissue_textview);
        id_documentPlaceOfIssue_textview = (TextView) view.findViewById(R.id.id_documentPlaceOfIssue_textview);
        gender_textview = (TextView) view.findViewById(R.id.gender_textview);
        profession_textview = (TextView) view.findViewById(R.id.profession_textview);
        resident_textview = (TextView) view.findViewById(R.id.resident_textview);
        senderCountry_textview = (TextView) view.findViewById(R.id.senderCountry_textview);
        senderCity_textview = (TextView) view.findViewById(R.id.senderCity_textview);
        nationality_textview = (TextView) view.findViewById(R.id.nationality_textview);
        email_textview_sender = (TextView) view.findViewById(R.id.email_textview);
        homePhoneno_sender_textview = (TextView) view.findViewById(R.id.homePhoneno_sender_textview);
        address_1_textview = (TextView) view.findViewById(R.id.address_1_textview);
        address_2_textview = (TextView) view.findViewById(R.id.address_2_textview);
        recipient_mobileNo_textview = (TextView) view.findViewById(R.id.recipient_mobileNo_textview);
        recipient_firstname_textview = (TextView) view.findViewById(R.id.recipient_firstname_textview);
        recipient_name_textview = (TextView) view.findViewById(R.id.recipient_name_textview);
        recipient_email_textview = (TextView) view.findViewById(R.id.recipient_email_textview);
        question_textview = (TextView) view.findViewById(R.id.question_textview);
        answer_textview = (TextView) view.findViewById(R.id.answer_textview);
        reasonForTransfer_textview = (TextView) view.findViewById(R.id.reasonForTransfer_textview);
        sending_currency_textview = (TextView) view.findViewById(R.id.sending_currency_textview);
        amount_textview = (TextView) view.findViewById(R.id.amount_textview);
        fees_textview = (TextView) view.findViewById(R.id.fees_textview);
        vat_textview = (TextView) view.findViewById(R.id.vat_textview);
        other_taxe_textview = (TextView) view.findViewById(R.id.other_taxe_textview);
        destinationCurrency_textview = (TextView) view.findViewById(R.id.destinationCurrency_textview);
        rate_textview = (TextView) view.findViewById(R.id.rate_textview);
        amountTopay_textview = (TextView) view.findViewById(R.id.amountTopay_textview);
        destinationCountry_textview = (TextView) view.findViewById(R.id.destinationCountry_textview);
        totalAmount_textview = (TextView) view.findViewById(R.id.totalAmount_textview);

        submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        mpin_AutoCompleteTextView = (EditText) view.findViewById(R.id.mpin_AutoCompleteTextView);


        modalSendMoney.getSenderMobileNumber();

        senderMobileNumber_textview.setText(modalSendMoney.getSenderMobileNumber()+" ");


        senderFirstName_textView.setText(modalSendMoney.getFirstNameSender()+" ");
        name_sender_textview.setText(modalSendMoney.getNameSender()+" ");
        email_textview_sender.setText(modalSendMoney.getEmailSender()+" ");

        id_document_tye_textview.setText(modalSendMoney.getIdDocumnetType()+" ");
        idDocumentNumber_textview.setText(modalSendMoney.getIdDocumentNumber()+" ");
        senderCity_textview.setText(modalSendMoney.getCity()+" ");

        id_documentDateofissue_textview.setText(modalSendMoney.getIdDocumnetDateOfIssue()+" ");
        id_documentCountryOfissue_textview.setText(modalSendMoney.getIdDocumentCountryOfIssue()+" ");
        id_documentPlaceOfIssue_textview.setText(modalSendMoney.getIdProofPlaceOfIssue()+" ");
        homePhoneno_sender_textview.setText(modalSendMoney.getPreFixCountryHomePhoneNumber()+modalSendMoney.getFixHomePhoneNumber()+" ");



        try {

            address_2_textview.setText(modalSendMoney.getAddress2()+" ");

        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        address_1_textview.setText(modalSendMoney.getAddress1()+" ");
        gender_textview.setText(modalSendMoney.getGenderType()+" ");
        profession_textview.setText(modalSendMoney.getProfession()+" ");
        resident_textview.setText(modalSendMoney.getResident()+" ");
        senderCountry_textview.setText(modalSendMoney.getCountryName()+" ");
        nationality_textview.setText(modalSendMoney.getNationality()+" ");
        recipient_mobileNo_textview.setText(modalSendMoney.getDestinationMobileNumber()+" ");
        recipient_firstname_textview.setText(modalSendMoney.getFirstNameReceiver()+" ");
        recipient_name_textview.setText(modalSendMoney.getNameReceiver()+" ");
        recipient_email_textview.setText(modalSendMoney.getEmailReceiver()+" ");


        question_textview.setText(modalSendMoney.getQuestion_name()+" ");



        answer_textview.setText(modalSendMoney.getAnswer_name());
        reasonForTransfer_textview.setText(modalSendMoney.getReasonOfTransfer()+" ");
        sending_currency_textview.setText(modalSendMoney.getCurrencySenderCode()+" ");

        amount_textview.setText(modalSendMoney.getAmountString()+" ");
        fees_textview.setText(modalSendMoney.getDisplayFees()+" ");
        vat_textview.setText(modalSendMoney.getDisplayVAT()+" ");
        other_taxe_textview.setText(modalSendMoney.getOtherTax()+" ");
        totalAmount_textview.setText(modalSendMoney.getTotalAmount()+" ");
        destinationCurrency_textview.setText(modalSendMoney.getCurrencyDestinationCode()+" ");
        rate_textview.setText(modalSendMoney.getRate_calculation()+" ");
        amountTopay_textview.setText(modalSendMoney.getAmountToPay_fromserver()+" ");
        destinationCountry_textview.setText(modalSendMoney.getCountryDestinationName()+" ");

        mComponentInfo.setArrowBackButton_sendCash(7);


        //  senderMobileNumber_textview.setText(String.valueOf(bundle.getString("senderMobileNoString")));
        //  senderFirstName_textView.setText(" " + String.valueOf(bundle.getString("countrySelectionString")));
        //  name_sender_textview.setText(String.valueOf(bundle.getString("receipientNumberString")));


        return view;
    }



    private void showProgressDialog(String message) {
        try {


            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(message);
            mDialog.setCancelable(false);
            mDialog.setIndeterminate(false);
            mDialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void hideProgressDialog() {
        try {


            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.submitButton:


                validationDetails();

                break;


        }
    }

    void vollyRequest_cashToCash(String apiName, final String body, final int requestCode)
    {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,baseUrl+apiName,new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                         //   Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                          //  Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(),mComponentInfo,MpinPageSendMoneyFrgament.this,requestCode,response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
                            getActivity().finish();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

        }

    }



    private void request_get_cashToCash() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_cashToCash();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            //  vollyRequest_cashToCash("sendCashInt", requestData, 223);

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "sendCashInt", 223).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }
    private String generateJson_cashToCash() {

        String jsonString = "";

      //  SharedPreferences prefs = getActivity().getSharedPreferences("EU_MPIN", MODE_PRIVATE);
      //  String pin = prefs.getString("MPIN", null);


        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentcode", agentCode);

            String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("localamount", modalSendMoney.getAmountString());
            countryObj.put("sendercountry", modalSendMoney.getCountrySenderCode());
            countryObj.put("destination", modalSendMoney.getDestinationMobileNumber());
            countryObj.put("source", modalSendMoney.getSenderMobileNumber());
            countryObj.put("receivermobilenumber", modalSendMoney.getDestinationMobileNumber());
            countryObj.put("sendermobilenumber", modalSendMoney.getSenderMobileNumber());
            countryObj.put("reasonforpayment", modalSendMoney.getReasonOfTransfer());
            countryObj.put("typetransaction", "0");

            //countryObj.put("exchangerate", modalSendMoney.getFees_fromServer());
            countryObj.put("exchangerate", modalSendMoney.getFees_fromServer());

            countryObj.put("senderfirstname", modalSendMoney.getFirstNameSender());
            countryObj.put("senderlastname", modalSendMoney.getNameSender());
            countryObj.put("receiverfirstname", modalSendMoney.getFirstNameReceiver());
            countryObj.put("receiverlastname", modalSendMoney.getNameReceiver());
            countryObj.put("originatingcountry", modalSendMoney.getCountrySenderCode());
            countryObj.put("originatingcurrency", modalSendMoney.getCurrencySenderCode());
            countryObj.put("destinationcountry", modalSendMoney.getCountryDestinationCode());
            countryObj.put("destinationcurrency", modalSendMoney.getCurrencyDestinationCode());
            countryObj.put("senderphonenumber", modalSendMoney.getPreFixCountryHomePhoneNumber()+modalSendMoney.getFixHomePhoneNumber());
            countryObj.put("receiveremail", modalSendMoney.getEmailReceiver());

            Calendar calander = Calendar.getInstance();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateTimeString = simpledateformat.format(calander.getTime());



            countryObj.put("transactiondate",currentDateTimeString);
            countryObj.put("sendercodetypepiece",modalSendMoney.getIdDocumnetType_code());
            countryObj.put("senderid1number",modalSendMoney.getIdDocumentNumber());

            String senderdateofissue_temp=modalSendMoney.getIdDocumnetDateOfIssue();

            String senderdateofissue_final=senderdateofissue_temp.replace("/","-");

            countryObj.put("senderdateofissue",senderdateofissue_final);
            countryObj.put("sendercountryofissue",modalSendMoney.getIdDocumentCountryOfIssue_code());
            countryObj.put("senderstateofissue",modalSendMoney.getIdProofPlaceOfIssue());
            countryObj.put("sendergender",modalSendMoney.getGenderType());
            countryObj.put("senderoccupation",modalSendMoney.getProfession());
            countryObj.put("senderresident",modalSendMoney.getResidentCode());
            countryObj.put("sendercity",modalSendMoney.getCity());
            countryObj.put("sendernationality",modalSendMoney.getNationality());
            countryObj.put("senderemail",modalSendMoney.getEmailSender());
            countryObj.put("senderaddress1",modalSendMoney.getAddress1());
            countryObj.put("senderaddress2",modalSendMoney.getAddress2());
            countryObj.put("testquestion",modalSendMoney.getQuestion_name());  // replace Question Code to question name // 28 jan 2020
            countryObj.put("testanswer",modalSendMoney.getAnswer_name());
            countryObj.put("payoutamount",modalSendMoney.getAmountToPay_fromserver());  // add 9 July 2019

            countryObj.put("taxcharged",modalSendMoney.getTax_fromServer());


            countryObj.put("additionaltax",modalSendMoney.getOtherTax());  // temporary comment acording to shyam sir


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

/*
                    "agentcode": "237000271501",
                    "pin": "C9534300C5181EEFA7271B14D3B74DD6",
                    "pintype": "IPIN",
                    "vendorcode": "MICROEU",
                    "clienttype": "SELFCARE",
                    "localamount": "200",
                    "sendercountry": "CM",
                    "destination": "237000271510",
                    "source": "237799898000",
                    "receivermobilenumber": "173314144",
                    "sendermobilenumber": "8230573344",
                    "reasonforpayment": "Had too much money",
                    "typetransaction": "2",
                    "exchangerate": "10.0",
                    "senderfirstname": "bhawesh",
                    "senderlastname": "kumar1",
                    "receiverfirstname": "prabhat",
                    "receiverlastname": "mishra1",
                    "originatingcountry": "cm",
                    "originatingcurrency": "xaf",
                    "destinationcountry": "bgd",
                    "destinationcurrency": "taka",
                    "senderphonenumber": "8230573344",
                    "receiveremail": "blabla@yuhi.com",
                    "transactiondate": "2019-05-08 12:10:03",
                    "sendercodetypepiece": "1",
                    "senderid1number": "1111",
                    "senderdateofissue": "2019-05-08",
                    "sendercountryofissue": "1",
                    "senderstateofissue": "dlh",
                    "sendergender": "male",
                    "senderoccupation": "shopkeeper",
                    "senderresident": "abc",
                    "sendercity": "city1",
                    "sendernationality": "indian",
                    "senderemail": "pkc@abc.com",
                    "senderaddress1": "Test?",
                    "senderaddress2": "add2",
                    "testquestion": "Q1",
                    "testanswer": "A1"*/






            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }


    public void validationDetails() {

        if (validateDetails_PartI()) {


            request_get_cashToCash();


           // Toast.makeText(getActivity(), "Print page", Toast.LENGTH_SHORT).show();

        }
    }


    private boolean validateDetails_PartI() {
        boolean ret = false;


        mpinString = mpin_AutoCompleteTextView.getText().toString();
        if (mpinString.trim().length() == 4) {

            ret = true;

        } else {
            Toast.makeText(getActivity(), getString(R.string.mpinAccountBalance), Toast.LENGTH_SHORT).show();
        }

        return ret;

    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {


        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();

            if (requestNo == 223) {


                try {


                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");

                //   Toast.makeText(getActivity(), "" + responseArray[0], Toast.LENGTH_SHORT).show();


                    modalSendMoney.setDateTime_reprint(responseArray[1]);
                    modalSendMoney.setTransactionid_reprint(responseArray[2]);
                    modalSendMoney.setReferenceNumber_print(responseArray[3]);


                    ModalFragmentManage modalFragmentManage = new ModalFragmentManage();
                    modalFragmentManage.setFragment_for_sender("sixFragment");


                    Intent intent = new Intent(getActivity(), SucessReceiptCashToCashSendMoney.class);
                    startActivity(intent);
                    getActivity().finish();   // must be finish other wise not clear fragmnet in Receipt page


                  //  getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_mpin").replace(R.id.frameLayout_cashtocash,new DemoNew()).commit();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
        }



    }

}


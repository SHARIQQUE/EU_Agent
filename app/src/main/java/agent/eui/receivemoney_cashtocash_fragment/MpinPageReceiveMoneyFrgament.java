package agent.eui.receivemoney_cashtocash_fragment;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import java.util.ArrayList;
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


public class MpinPageReceiveMoneyFrgament extends Fragment implements View.OnClickListener,ServerResponseParseCompletedNotifier {

    String agentCode, agentName,countrySelectionString;
    private ProgressDialog mDialog;
    TextView vat_textview, referenceNumber_textView,amountTopay_textview,destinationCountry_textview, reasonForTransfer_textview, destination_currency_textview, amount_textview, fees_textview, other_taxe_textview, answer_textview, question_textview, recipient_mobileNo_textview, recipient_firstname_textview, recipient_name_textview, senderMobileNumber_textview, id_documentPlaceOfIssue_textview, senderFirstName_textView, id_documentCountryOfissue_textview, name_sender_textview, birthDate_textview, id_document_tye_textview, id_documentDateofissue_textview, idDocumentNumber_textview, gender_textview, profession_textview;
    Button submitButton;
    ComponentMd5SharedPre mComponentInfo;
    ModalReceiveMoney modalReceiveMoney= new ModalReceiveMoney();

    View view;
    AutoCompleteTextView mpin_AutoCompleteTextView;
    String mpinString;

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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, MpinPageReceiveMoneyFrgament.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.receiptpage_cashtocash_receivemoney, container, false); // Inflate the layout for this fragment


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


        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // currencySenderSelectionString = mComponentInfo.ge223tmSharedPreferences().getString("currency", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


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
        birthDate_textview = (TextView) view.findViewById(R.id.birthDate_textview);
        recipient_mobileNo_textview = (TextView) view.findViewById(R.id.recipient_mobileNo_textview);
        recipient_firstname_textview = (TextView) view.findViewById(R.id.recipient_firstname_textview);
        recipient_name_textview = (TextView) view.findViewById(R.id.recipient_name_textview);
        question_textview = (TextView) view.findViewById(R.id.question_textview);
        answer_textview = (TextView) view.findViewById(R.id.answer_textview);
        reasonForTransfer_textview = (TextView) view.findViewById(R.id.reasonForTransfer_textview);
        destinationCountry_textview = (TextView) view.findViewById(R.id.destinationCountry_textview);
        destination_currency_textview = (TextView) view.findViewById(R.id.destination_currency_textview);
        amount_textview = (TextView) view.findViewById(R.id.amount_textview);
        fees_textview = (TextView) view.findViewById(R.id.fees_textview);
        vat_textview = (TextView) view.findViewById(R.id.vat_textview);
        other_taxe_textview = (TextView) view.findViewById(R.id.other_taxe_textview);
        amountTopay_textview = (TextView) view.findViewById(R.id.amountTopay_textview);
        referenceNumber_textView = (TextView) view.findViewById(R.id.referenceNumber_textView);

        mpin_AutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.mpin_AutoCompleteTextView);

        submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        senderMobileNumber_textview.setText(modalReceiveMoney.getSenderMobileNumber());
        senderFirstName_textView.setText(modalReceiveMoney.getSenderFirstName());
        name_sender_textview.setText(modalReceiveMoney.getSenderLastName());
        id_document_tye_textview.setText(modalReceiveMoney.getIdProofType());
        idDocumentNumber_textview.setText(modalReceiveMoney.getIdProofNumber());
        id_documentDateofissue_textview.setText(modalReceiveMoney.getIdProofIssueDate_agentIdentity());
        id_documentCountryOfissue_textview.setText(modalReceiveMoney.getIdproofCountryOfIssue_name());
        id_documentPlaceOfIssue_textview.setText(modalReceiveMoney.getIdProofIssueOfPlace_agentIdentity());
        gender_textview.setText(modalReceiveMoney.genderType_agentIdentity);
        profession_textview.setText(modalReceiveMoney.getProfession());

        recipient_mobileNo_textview.setText(modalReceiveMoney.getDestinationMobileNumber());
        recipient_firstname_textview.setText(modalReceiveMoney.getDestinationFirstName());
        recipient_name_textview.setText(modalReceiveMoney.getDestinationLastName());
        question_textview.setText(modalReceiveMoney.getQuestion());
        answer_textview.setText(modalReceiveMoney.getAnswer());
        reasonForTransfer_textview.setText(modalReceiveMoney.getReasonOfTheTransfer());
        destination_currency_textview.setText(modalReceiveMoney.getCurrencyDestination());
        amount_textview.setText(modalReceiveMoney.getAmountSentNew());
        fees_textview.setText(modalReceiveMoney.getFees());
        vat_textview.setText(modalReceiveMoney.getVat());
        other_taxe_textview.setText(modalReceiveMoney.getOtherTax());

      //  birthDate_textview.setText(modalReceiveMoney.getDateOfBirthDestination());

        amountTopay_textview.setText(modalReceiveMoney.getAmountToPay());
        referenceNumber_textView.setText(modalReceiveMoney.getReferenceNumber());
        destinationCountry_textview.setText(modalReceiveMoney.getDestinationCountry_name());



        mComponentInfo.setArrowBackButton_receiveCash(7);
        return view;
    }


    private void request_CashToReceive() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_cashToReceive();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


          //    vollyRequest_getCashToCash("receiveCashInt", requestData, 224);

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "receiveCashInt", 224).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }


    void vollyRequest_getCashToCash(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                         //   Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                         //   Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, MpinPageReceiveMoneyFrgament.this, requestCode, response.toString());
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


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(getActivity(), getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

        }

    }

    private String generateJson_cashToReceive() {

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
            countryObj.put("destination", modalReceiveMoney.getDestinationMobileNumber());
            countryObj.put("source", modalReceiveMoney.getSenderMobileNumber());
            countryObj.put("destinationcountry", modalReceiveMoney.getDestinationCountry_code());
            countryObj.put("destinationcurrency", modalReceiveMoney.getCurrencyDestination());  // temporay
            countryObj.put("amount", modalReceiveMoney.getAmountToPay());  // change  14 jan 2020
            countryObj.put("referencenumber", modalReceiveMoney.getReferenceNumber());
            countryObj.put("requestcts", "2019-12-08 05:23:44");
            countryObj.put("cnitype", modalReceiveMoney.getIdProofType_code());
            countryObj.put("cninumber", modalReceiveMoney.getIdProofNumber());  // id type check
            countryObj.put("expirydate", "");      // non mandatory
            countryObj.put("sourcename", modalReceiveMoney.getSenderFirstName());
            countryObj.put("destinationname", modalReceiveMoney.getDestinationFirstName());

            // countryObj.put("additionaltax", modalReceiveMoney.getOtherTax());  // temporary comment acording to shyam sir

            countryObj.put("dateofissue", modalReceiveMoney.getIdProofIssueDate_agentIdentity());
            countryObj.put("countryofissue", modalReceiveMoney.getIdproofCountryOfIssue_code());
            countryObj.put("placeofissue", modalReceiveMoney.getIdProofIssueOfPlace_agentIdentity());
            countryObj.put("gender", modalReceiveMoney.getGenderType_code_agentIdentity());

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            // bhawesh Request

            /*    "agentcode": "237000271501",
                   "pin": "C9534300C5181EEFA7271B14D3B74DD6",
                   "pintype": "IPIN",
                    "vendorcode": "MICROEU",
                    "clienttype": "SELFCARE",
                    "destination": "237000271510",
                    "source": "237799898000",
                    "destinationcountry": "BGD",
                    "destinationcurrency": "Taka",
                    "amount": "2000",
                    "referencenumber": "P161902180003",
                    "requestcts": "017-12-08 05:23:44",
                    "cnitype": "CI",
                    "cninumber": "rtytry",
                    "expirydate": "2017-12-12",
                    "sourcename": "rajesh1234",
                    "destinationname": "Rajesh"


                    */






            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.submitButton:

                validationDetails();

                break;


        }
    }

    public void validationDetails() {

        if (validateDetails_PartI()) {

            request_CashToReceive();


            //  Toast.makeText(getActivity(), "Print page", Toast.LENGTH_SHORT).show();

        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(getActivity());
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

    private boolean validateDetails_PartI() {
        boolean ret = false;


        mpinString = mpin_AutoCompleteTextView.getText().toString();
        if (mpinString.trim().length() == 4) {

            ret = true;

        } else {
            Toast.makeText(getActivity(), getString(R.string.secerateCode), Toast.LENGTH_SHORT).show();
        }

        return ret;

    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 224) {

                // change 148

                String responseData = generalResponseModel.getUserDefinedString();
                String[] responseArray = responseData.split("\\|");


                modalReceiveMoney.setDateTime_reprint(responseArray[1]);
                modalReceiveMoney.setTransactionId_reprint(responseArray[2]);
                modalReceiveMoney.setCountryDestintion_reprint(responseArray[3]);
                modalReceiveMoney.setCountry_reprint(responseArray[4]);
                modalReceiveMoney.setAgentBranch_reprint(responseArray[5]);
                modalReceiveMoney.setDestinatioName_reprint(responseArray[6]);
                modalReceiveMoney.setReferenceNumber_reprint(responseArray[7]);



                Intent intent = new Intent(getActivity(), SucessReceiptCashToReceive.class);
                startActivity(intent);
                getActivity().finish();   // must be finish other wise not clear fragmnet in Receipt page



               //  getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameLayout_cashtocash_receivemoney, new ReprintReceiveMoneyDemo()).commit();

            }


        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }
}

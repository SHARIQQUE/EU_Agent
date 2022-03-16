package agent.eui.sendmoney_cashtocash_fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;

import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;

import static android.content.Context.MODE_PRIVATE;
import static agent.thread.ServerTask.baseUrl;


public class SenderRecipintReviewDetailFrgament extends Fragment implements View.OnClickListener, ServerResponseParseCompletedNotifier {


    Double exchange_rate_double, taxcharged_fees_double, tempAmount_double, amount_topay_double;


    ProgressDialog mDialog;
    TextView vat_textview_cashtocash, senderMobileNumber_textview, recipientCountry_textView, recipintMobileNumber_textview, sendingcurrency_textview, fees_textview_cashtocash, amount_textview_cashtocash, totalAmount_textview_cashtocash, destination_currency_textview_cashtocash, rate_textview_cashtocash, amountopay_textview_cashtocash;
    Button nextButton_reviewpage;
    EditText othertax_textview_cashtocash;
    String otherTaxString, exchange_rate_string, taxcharged_fees_string;
    View view;
    String destinationCountrySelected_code, countrySelectionCode, agentName, agentCode, senderMobileNoString, destinationCountrySelectedString, currencyDestinationSelectionString, currencySenderSelectionString, recipientNumberString, amountString;
    ComponentMd5SharedPre mComponentInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.sender_recipint_review_detail_frgament, container, false); // Inflate the layout for this fragment

        mComponentInfo = (ComponentMd5SharedPre) getActivity().getApplicationContext();


        senderMobileNumber_textview = (TextView) view.findViewById(R.id.senderMobileNumber_textview);
        recipientCountry_textView = (TextView) view.findViewById(R.id.recipientCountry_textView);
        recipintMobileNumber_textview = (TextView) view.findViewById(R.id.recipintMobileNumber_textview);
        sendingcurrency_textview = (TextView) view.findViewById(R.id.sendingcurrency_textview);
        fees_textview_cashtocash = (TextView) view.findViewById(R.id.fees_textview_cashtocash);
        amount_textview_cashtocash = (TextView) view.findViewById(R.id.amount_textview_cashtocash);
        othertax_textview_cashtocash = (EditText) view.findViewById(R.id.othertax_textview_cashtocash);
        totalAmount_textview_cashtocash = (TextView) view.findViewById(R.id.totalAmount_textview_cashtocash);
        destination_currency_textview_cashtocash = (TextView) view.findViewById(R.id.destination_currency_textview_cashtocash);
        rate_textview_cashtocash = (TextView) view.findViewById(R.id.rate_textview_cashtocash);
        amountopay_textview_cashtocash = (TextView) view.findViewById(R.id.amountopay_textview_cashtocash);
        vat_textview_cashtocash = (TextView) view.findViewById(R.id.vat_textview_cashtocash);

        nextButton_reviewpage = (Button) view.findViewById(R.id.nextButton_reviewpage);
        nextButton_reviewpage.setOnClickListener(this);

        try {

            agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
            agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

            Bundle bundle = getArguments();


            currencySenderSelectionString = String.valueOf(bundle.getString("currencySenderSelectionString"));
            currencyDestinationSelectionString = String.valueOf(bundle.getString("currencydestinationSelectionString"));
            senderMobileNoString = String.valueOf(bundle.getString("senderMobileNoString"));
            destinationCountrySelectedString = String.valueOf(bundle.getString("destinationCountrySelectedString"));
            recipientNumberString = String.valueOf(bundle.getString("recipientNumberString"));
            amountString = String.valueOf(bundle.getString("amountString"));
            countrySelectionCode = String.valueOf(bundle.getString("countrySelectionCode"));
            destinationCountrySelected_code = String.valueOf(bundle.getString("destinationCountrySelected_code"));


            senderMobileNumber_textview.setText(senderMobileNoString);
            recipientCountry_textView.setText(destinationCountrySelectedString);
            recipintMobileNumber_textview.setText(recipientNumberString);
            sendingcurrency_textview.setText(currencySenderSelectionString);
            amount_textview_cashtocash.setText(amountString + " " + currencySenderSelectionString);
            destination_currency_textview_cashtocash.setText(currencyDestinationSelectionString);


            request_TaxCommit();


            //   apiCall_taxAndCommint();


        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }


    private void request_TaxCommit() {

        if (new InternetCheck().isConnected(getActivity())) {

            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateJson_taxCommit();

            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();


            vollyRequest_getCurrency("taxandcommInt", requestData, 216);

            //  new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getCurrency", 216).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }

    }

    void vollyRequest_getCurrency(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                          //  Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                          //  Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, SenderRecipintReviewDetailFrgament.this, requestCode, response.toString());
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


    private String generateJson_taxCommit() {
        String jsonString = "";

        SharedPreferences prefs = getActivity().getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "25/05/2016 18:01:51");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("comments", "comments");
            countryObj.put("amount", amountString);
            countryObj.put("sendercountry", countrySelectionCode);// sender country code
            countryObj.put("sendertown", "ghjh");
            countryObj.put("sendercurrency", currencySenderSelectionString);
            countryObj.put("destcountry", destinationCountrySelected_code); // destination  country code
            countryObj.put("desttown", "jhgjh");
            countryObj.put("destcurrency", currencyDestinationSelectionString);
            countryObj.put("exchangerate", "2");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(message);
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(false);
        mDialog.show();
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.nextButton_reviewpage:


                SenderDetailPartOneFragment senderDetailPartOneFragment = new SenderDetailPartOneFragment();


                Bundle bundle = new Bundle();

                senderMobileNoString = getArguments().getString("senderMobileNoString");
                recipientNumberString = getArguments().getString("recipientNumberString");   // first Fragment

                bundle.putString("senderMobileNoString", senderMobileNoString);
                bundle.putString("recipientNumberString", recipientNumberString);

                senderDetailPartOneFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameLayout_cashtocash, senderDetailPartOneFragment).commit();

                break;
        }

    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();

            if (requestNo == 216) {


                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");


                    String exchange_rate_string = responseArray[0];     // rate
                    String taxcharged_fees_string = responseArray[1];   // fees

                    fees_textview_cashtocash.setText(taxcharged_fees_string + " " + currencySenderSelectionString);
                    rate_textview_cashtocash.setText(exchange_rate_string);
                    othertax_textview_cashtocash.setText("0.0");
                    otherTaxString = othertax_textview_cashtocash.getText().toString();
                    othertax_textview_cashtocash.setText(otherTaxString);


                    exchange_rate_double = Double.parseDouble(exchange_rate_string);
                    taxcharged_fees_double = Double.parseDouble(taxcharged_fees_string);
                    tempAmount_double = Double.parseDouble(amountString);


                    tempAmount_double = tempAmount_double + taxcharged_fees_double;
                    totalAmount_textview_cashtocash.setText(tempAmount_double.toString() + " " + currencySenderSelectionString);


                    vat_textview_cashtocash.setText("0.0" + " " + currencySenderSelectionString); // temporary

                    amount_topay_double = tempAmount_double * exchange_rate_double;
                    amountopay_textview_cashtocash.setText(amount_topay_double.toString() + " " + currencyDestinationSelectionString);


                    // Toast.makeText(getActivity(),exchangerate_string,Toast.LENGTH_LONG).show();
                    // Toast.makeText(getActivity(),taxcharged_string,Toast.LENGTH_LONG).show();


                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().finish();
                }
            }


        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }

    }
}


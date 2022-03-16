package agent.eui.sendmoney_cashtocash_fragment;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;


public class TaxExchangeRateVatPageFragment extends Fragment implements View.OnClickListener, ServerResponseParseCompletedNotifier {


    ModalSendMoney modalSendMoney = new ModalSendMoney();
    Double otherTax, totalAmount_double, amount, amountToPay, rateCalculation_double, amount_temp, fees_temp, vat_temp;
    ProgressDialog mDialog;
    TextView vat_textview_cashtocash, senderMobileNumber_textview, recipientCountry_textView, recipintMobileNumber_textview, sendingcurrency_textview, fees_textview_cashtocash, amount_textview_cashtocash, totalAmount_textview_cashtocash, destination_currency_textview_cashtocash, rate_textview_cashtocash, amountopay_textview_cashtocash;
    Button nextButton_reviewpage;
    EditText othertax_textview_cashtocash;
    String otherTaxString;

    View view;
    String agentName, agentCode, totalAmount_double_temp;
    ComponentMd5SharedPre mComponentInfo;


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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, TaxExchangeRateVatPageFragment.this, message.arg1, message.obj.toString());

                thread.execute();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.sender_recipint_review_detail_frgament, container, false); // Inflate the layout for this fragment


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

        mComponentInfo.setArrowBackButton_sendCash(2);


        try {

            agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
            agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

            senderMobileNumber_textview.setText(modalSendMoney.getSenderMobileNumber());
            recipientCountry_textView.setText(modalSendMoney.getCountryDestinationName());
            recipintMobileNumber_textview.setText(modalSendMoney.getDestinationMobileNumber());
            sendingcurrency_textview.setText(modalSendMoney.getCurrencySenderCode());

            amount_textview_cashtocash.setText(modalSendMoney.getAmountString() + " " + modalSendMoney.getCurrencySenderCode());

            destination_currency_textview_cashtocash.setText(modalSendMoney.getCurrencyDestinationCode() + " ");


            // otherTaxString=modalSendMoney.getTax_fromServer(); //commnet 25 july 2019
            otherTaxString = "0";
            modalSendMoney.setOtherTax(otherTaxString);


            if (otherTaxString.equalsIgnoreCase("0.0")) {

                otherTaxString = "0";
            }


            othertax_textview_cashtocash.setText(otherTaxString);
            otherTaxString = othertax_textview_cashtocash.getText().toString();

            othertax_textview_cashtocash.setText(otherTaxString);
            modalSendMoney.setOtherTax(otherTaxString);

            othertax_textview_cashtocash.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if (s.length() != 0 && !(s + "").equalsIgnoreCase("."))

                        otherTax = Double.parseDouble(othertax_textview_cashtocash.getText().toString());
                    totalAmount_double = amount_temp + fees_temp + otherTax;
                    totalAmount_textview_cashtocash.setText(totalAmount_double + " " + modalSendMoney.getCurrencySenderCode() + " ");
                    modalSendMoney.setTotalAmount(totalAmount_double + "");
                    otherTaxString = otherTax + "";
                    if (otherTax == 0.0) {
                        modalSendMoney.setOtherTax("0");
                    } else {

                        modalSendMoney.setOtherTax(otherTaxString);
                    }


                    if (s.length() == 0 && !(s + "").equalsIgnoreCase("."))

                        otherTax = Double.parseDouble("0.0");
                    totalAmount_double = amount_temp + fees_temp + otherTax;
                    totalAmount_textview_cashtocash.setText(totalAmount_double + " " + modalSendMoney.getCurrencySenderCode() + " ");
                    otherTaxString = otherTax + "";
                    modalSendMoney.setOtherTax(otherTaxString);
                    modalSendMoney.setTotalAmount(totalAmount_double + "");
                }
            });

            amount = Double.parseDouble(modalSendMoney.getAmountString());

            Log.e("Amount From Server", modalSendMoney.getAmountToPay_fromserver());

            amountToPay = Double.parseDouble(modalSendMoney.getAmountToPay_fromserver());


//            Double amountToPay = Double.parseDouble(modalSendMoney.getAmountToPay_fromserver().contains(",")
//                    ?modalSendMoney.getAmountToPay_fromserver().replace(",","."):modalSendMoney.getAmountToPay_fromserver());

         //   rateCalculation_double = amount / amountToPay;  // commnet 31 july reverse
            rateCalculation_double = amountToPay / amount;


            NumberFormat df = DecimalFormat.getInstance(Locale.ENGLISH);
            df.setMinimumFractionDigits(2);
            df.setGroupingUsed(false);
            df.setMaximumFractionDigits(2);
            df.setRoundingMode(RoundingMode.DOWN);


            Double fees_Frm_server = Double.parseDouble(modalSendMoney.getFees_fromServer());
            //  Double tax_Frm_server = Double.parseDouble(modalSendMoney.getTax_fromServer());

            // Double fees_plus_taxCharge = fees_Frm_server + tax_Frm_server;   // add new tax  + tax Charge 25 july 2019


            Double vat_Frm_server = Double.parseDouble(modalSendMoney.getVat_fromserver());
            // vat_Frm_server = (vat_Frm_server / (100 + vat_Frm_server)) * fees_plus_taxCharge;       // tax_Frm_server replace add new tax  + tax Charge 25 july 2019
            //  Double fees_Frm_server_double = fees_plus_taxCharge - vat_Frm_server;  // Fee=19.25/(19.25+100)*2200


            vat_Frm_server=(vat_Frm_server/(100+vat_Frm_server))*fees_Frm_server;
            fees_Frm_server=fees_Frm_server-vat_Frm_server;


            modalSendMoney.setDisplayVAT(df.format(vat_Frm_server) + "");
            modalSendMoney.setDisplayFees(df.format(fees_Frm_server) + "");


            fees_textview_cashtocash.setText(modalSendMoney.getDisplayFees() + " " + modalSendMoney.getCurrencySenderCode());


            rate_textview_cashtocash.setText(df.format(rateCalculation_double));    // java.lang.IllegalArgumentException: Bad class: class java.lang.String
            modalSendMoney.setRate_calculation(df.format(rateCalculation_double));

            amount_temp = Double.parseDouble(modalSendMoney.getAmountString());
            fees_temp = Double.parseDouble(modalSendMoney.getFees_fromServer());



            if (otherTaxString == null || otherTaxString.trim().length() == 0) {
                otherTaxString = "0";

            }


            otherTax = Double.parseDouble(otherTaxString);
            totalAmount_double = amount_temp + fees_temp + otherTax;

            totalAmount_double_temp = String.valueOf(df.format(totalAmount_double));

            totalAmount_textview_cashtocash.setText(totalAmount_double_temp + " " + modalSendMoney.getCurrencySenderCode() + " ");
            modalSendMoney.setTotalAmount(totalAmount_double_temp + "");

            vat_textview_cashtocash.setText(modalSendMoney.getDisplayVAT() + " " + modalSendMoney.getCurrencySenderCode() + " ");
            //  amountopay_textview_cashtocash.setText(modalSendMoney.getAmountToPay_fromserver() + " " + modalSendMoney.getCurrencyDestinationCode() + " ");

            amountopay_textview_cashtocash.setText(modalSendMoney.getAmountToPay_fromserver() + " " + modalSendMoney.getCurrencyDestinationCode() + " ");

            //   apiCall_taxAndCommint();


        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
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


    private void showProgressDialog(String message) {
        try {

            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage(message);
            mDialog.setCancelable(false);
            mDialog.setIndeterminate(false);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.nextButton_reviewpage:


                SenderDetailPartOneFragment senderDetailPartOneFragment = new SenderDetailPartOneFragment();


               /* Bundle bundle = new Bundle();

                senderMobileNoString = getArguments().getString("senderMobileNoString");
                recipientNumberString = getArguments().getString("recipientNumberString");   // first Fragment
                reasonForTransfer_string = getArguments().getString("reasonForTransfer_string");   // first Fragment

                bundle.putString("senderMobileNoString", senderMobileNoString);
                bundle.putString("recipientNumberString", recipientNumberString);

                bundle.putString("reasonForTransfer_string", reasonForTransfer_string);
                bundle.putString("taxcharged_fees_string", taxcharged_fees_string);
                bundle.putString("vat_string", vat_string);
                bundle.putString("otherTaxString", otherTaxString);
                bundle.putString("totalAmountString",totalAmountString);
                bundle.putString("amounToPayString",amounToPayString);

                senderDetailPartOneFragment.setArguments(bundle);*/

                ModalFragmentManage modalFragmentManage = new ModalFragmentManage();
                modalFragmentManage.setFragment_for_sender("thirdFragment");


                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_three").replace(R.id.frameLayout_cashtocash, senderDetailPartOneFragment).commit();

                break;
        }

    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

    }


}


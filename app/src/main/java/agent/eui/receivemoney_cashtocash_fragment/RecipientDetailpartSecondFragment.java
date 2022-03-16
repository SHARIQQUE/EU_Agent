package agent.eui.receivemoney_cashtocash_fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import agent.activities.R;
import commonutilities.ComponentMd5SharedPre;


public class RecipientDetailpartSecondFragment extends Fragment implements View.OnClickListener {

    ComponentMd5SharedPre mComponentInfo;
    Double otherTax, totalAmount_double;
    View view;
    TextView vat_textview, amountTopay_textview, destination_currency, destination_country, reasonForTransfer_textview, amount_textview, fees_textview, referenceNumber_textview;
    Double amountToPay_double;
    String otherTaxString = "0";
    EditText othertax_edittext;
    String  numberAsString="";
    Double amount_temp;
    ModalReceiveMoney modalReceiveMoney = new ModalReceiveMoney();
    private String[] threshHolderAmount_array, countryCodeArray, countryArray, currencyArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, payerAccountCodeArray;

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

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.recipientdetail_partsecond_fragment, container, false); // Inflate the layout for this fragment
        Button nextButton;


        referenceNumber_textview = (TextView) view.findViewById(R.id.referenceNumber_textview);
        reasonForTransfer_textview = (TextView) view.findViewById(R.id.reasonForTransfer_textview);
        amount_textview = (TextView) view.findViewById(R.id.amount_textview);
        fees_textview = (TextView) view.findViewById(R.id.fees_textview);
        vat_textview = (TextView) view.findViewById(R.id.vat_textview);
        destination_country = (TextView) view.findViewById(R.id.destination_country);

        destination_currency = (TextView) view.findViewById(R.id.destination_currency);
        amountTopay_textview = (TextView) view.findViewById(R.id.amountTopay_textview);
        othertax_edittext = (EditText) view.findViewById(R.id.othertax_edittext);

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);


        referenceNumber_textview.setText(modalReceiveMoney.getReferenceNumber());
        destination_country.setText(modalReceiveMoney.getDestinationCountry_name());
        reasonForTransfer_textview.setText(modalReceiveMoney.getReasonOfTheTransfer());
        amount_textview.setText(modalReceiveMoney.getAmountSentNew());
        fees_textview.setText(modalReceiveMoney.getFees());
        vat_textview.setText(modalReceiveMoney.getVat());
        destination_currency.setText(modalReceiveMoney.getCurrencyDestination());
        amountTopay_textview.setText(modalReceiveMoney.getAmountToPay());

        othertax_edittext.setText(otherTaxString);
     //   otherTaxString = othertax_edittext.getText().toString().trim();

        othertax_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(s.length() != 0 && !(s+"").equalsIgnoreCase(".")) {

                    try {


                        otherTax = Double.parseDouble(othertax_edittext.getText().toString());
                        amount_temp = Double.parseDouble(ModalReceiveMoney.getAmountToPay());


                        totalAmount_double = amount_temp - otherTax;
                         numberAsString = Double.toString(totalAmount_double);

                        amountTopay_textview.setText(numberAsString);
                        otherTaxString = otherTax + "";

                        if (otherTax == 0.0) {
                            modalReceiveMoney.setOtherTax("0");
                        } else {

                            modalReceiveMoney.setOtherTax(otherTaxString);
                        }


                        if (s.length() == 0 && !(s + "").equalsIgnoreCase("."))

                            otherTax = Double.parseDouble("0.0");
                        totalAmount_double = amount_temp - otherTax;
                        numberAsString = Double.toString(totalAmount_double);

                        amountTopay_textview.setText(numberAsString);
                        otherTaxString = otherTax + "";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                  Double  fffffff = Double.parseDouble(ModalReceiveMoney.getAmountToPay());
                  String  aaaa = Double.toString(fffffff);
                   amountTopay_textview.setText(aaaa);
                    modalReceiveMoney.setAmountToPay(aaaa);

                }




            }
        });


        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList_EUI", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList_EUI", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList_EUI", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength_EUI", "").split("\\|");
            currencyArray = mComponentInfo.getmSharedPreferences().getString("currencyList_EUI", "").split("\\|");
            threshHolderAmount_array = mComponentInfo.getmSharedPreferences().getString("thresholderamount_EUI", "").split("\\|");

        } catch (Exception e) {

            getActivity().finish();
        }


        //  senderMobileNumber_textview.setText(String.valueOf(bundle.getString("senderMobileNoString")));
        //  senderFirstName_textView.setText(" " + String.valueOf(bundle.getString("countrySelectionString")));
        //  name_sender_textview.setText(String.valueOf(bundle.getString("receipientNumberString")));

        mComponentInfo.setArrowBackButton_receiveCash(5);


        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nextButton: {


                otherTaxString = othertax_edittext.getText().toString().trim();

                modalReceiveMoney.setOtherTax(otherTaxString);

                if(otherTaxString.equalsIgnoreCase("0"))
                {
                    modalReceiveMoney.setAmountToPay(ModalReceiveMoney.getAmountToPay());
                }
                else {
                    modalReceiveMoney.setAmountToPay(numberAsString);
                }






                if (modalReceiveMoney.getDestinationMobileNumber() == null
                        || modalReceiveMoney.getDestinationMobileNumber().equalsIgnoreCase("null")
                        || modalReceiveMoney.getDestinationMobileNumber().equalsIgnoreCase("")) {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_mpin").replace(R.id.frameLayout_cashtocash_receivemoney, new MpinPageReceiveMoneyFrgament()).commit();

                } else if (modalReceiveMoney.getDestinationMobileNumber().length() >= modalReceiveMoney.getCountrylenght())  // only Success Mobile number Otp generate
                {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_otp").replace(R.id.frameLayout_cashtocash_receivemoney, new OtpPageReceiveMoney()).commit();
                } else if (modalReceiveMoney.getDestinationMobileNumber().length() < 5) {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_mpin").replace(R.id.frameLayout_cashtocash_receivemoney, new MpinPageReceiveMoneyFrgament()).commit();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_mpin").replace(R.id.frameLayout_cashtocash_receivemoney, new MpinPageReceiveMoneyFrgament()).commit();
                }


                break;
            }

        }
    }

}

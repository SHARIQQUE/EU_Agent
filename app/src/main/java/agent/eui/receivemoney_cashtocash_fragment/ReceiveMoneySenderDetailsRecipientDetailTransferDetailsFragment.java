package agent.eui.receivemoney_cashtocash_fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import agent.activities.R;
import commonutilities.ComponentMd5SharedPre;


public class ReceiveMoneySenderDetailsRecipientDetailTransferDetailsFragment extends Fragment implements View.OnClickListener {


    View view;
    ModalReceiveMoney modalReceiveMoney= new ModalReceiveMoney();

    ComponentMd5SharedPre mComponentInfo;
    String[]  thresholderamount,transactionType_name_array,countryPrefixArray,countryMobileNoLengthArray,currencyArray,countryCodeArray;

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
        view = inflater.inflate(R.layout.receivemoney_senderdetail_recipientdetail_transferdetail, container, false); // Inflate the layout for this fragment
        Button nextButton;

        TextView destination_country, vat_textview, recipientMobileNumber_textview, recipientFirstName_textView, destination_currency, reasonForTransfer_textview, amount_textview, fees_textview, recipient_name_textview, email_textview, address_2_textview, senderMobileNumber_textview, senderFirstName_textView, name_sender_textview, birthDate_textview, referenceNumber_textview;


        try {
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList_EUI", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList_EUI", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength_EUI", "").split("\\|");
            currencyArray = mComponentInfo.getmSharedPreferences().getString("currencyList_EUI", "").split("\\|");
            thresholderamount = mComponentInfo.getmSharedPreferences().getString("Thresholderamount_EUI", "").split("\\|");

        } catch (Exception e) {

            getActivity().finish();
        }



        senderMobileNumber_textview = (TextView) view.findViewById(R.id.senderMobileNumber_textview);
        senderFirstName_textView = (TextView) view.findViewById(R.id.senderFirstName_textView);
        name_sender_textview = (TextView) view.findViewById(R.id.name_sender_textview);
        birthDate_textview = (TextView) view.findViewById(R.id.birthDate_textview);
        referenceNumber_textview = (TextView) view.findViewById(R.id.referenceNumber_textview);
        email_textview = (TextView) view.findViewById(R.id.email_textview);
        address_2_textview = (TextView) view.findViewById(R.id.address_2_textview);
        recipient_name_textview = (TextView) view.findViewById(R.id.recipient_name_textview);
        reasonForTransfer_textview = (TextView) view.findViewById(R.id.reasonForTransfer_textview);
        amount_textview = (TextView) view.findViewById(R.id.amount_textview);
        destination_country = (TextView) view.findViewById(R.id.destination_country);
        fees_textview = (TextView) view.findViewById(R.id.fees_textview);
        vat_textview = (TextView) view.findViewById(R.id.vat_textview);

        recipientMobileNumber_textview = (TextView) view.findViewById(R.id.recipientMobileNumber_textview);
        recipientFirstName_textView = (TextView) view.findViewById(R.id.recipientFirstName_textView);
        destination_currency = (TextView) view.findViewById(R.id.destination_currency);

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);



        senderMobileNumber_textview.setText(modalReceiveMoney.getSenderMobileNumber());
        senderFirstName_textView.setText(modalReceiveMoney.getSenderFirstName());
        name_sender_textview.setText(modalReceiveMoney.getSenderLastName());
        if (modalReceiveMoney.getDateOfBirthDestination().equalsIgnoreCase("null")){
            birthDate_textview.setText("");
        }else {
            birthDate_textview.setText(modalReceiveMoney.getDateOfBirthDestination());
        }

        referenceNumber_textview.setText(modalReceiveMoney.getReferenceNumber());

        if (modalReceiveMoney.getEmailIdReceiver().equalsIgnoreCase("null")){
            email_textview.setText("");
        }else {
            email_textview.setText(modalReceiveMoney.getEmailIdReceiver());
        }

        address_2_textview.setText(modalReceiveMoney.getSenderAddress());
        reasonForTransfer_textview.setText(modalReceiveMoney.getReasonOfTheTransfer());
        amount_textview.setText(modalReceiveMoney.getAmountSentNew());
        fees_textview.setText(modalReceiveMoney.getFees());
        vat_textview.setText(modalReceiveMoney.getVat());

        recipientMobileNumber_textview.setText(modalReceiveMoney.getDestinationMobileNumber());
        recipientFirstName_textView.setText(modalReceiveMoney.getDestinationFirstName());
        recipient_name_textview.setText(modalReceiveMoney.getDestinationLastName());

        destination_currency.setText(modalReceiveMoney.getCurrencyDestination());
        destination_country.setText(modalReceiveMoney.getDestinationCountry_name());

        //  senderMobileNumber_textview.setText(String.valueOf(bundle.getString("senderMobileNoString")));
        //  senderFirstName_textView.setText(" " + String.valueOf(bundle.getString("countrySelectionString")));
        //  name_sender_textview.setText(String.valueOf(bundle.getString("receipientNumberString")));

        mComponentInfo.setArrowBackButton_receiveCash(3);

        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nextButton: {


                String amount_fromServer=modalReceiveMoney.getAmountSent();
                String thresholderAmount=modalReceiveMoney.getThresHolderAmount();

              //  amount_fromServer="1000";
              //  thresholderAmount="200";


                double amount_fromServer_double=Double.parseDouble(amount_fromServer);
                double thresholderAmount_temp=Double.parseDouble(thresholderAmount);


                if(amount_fromServer_double >= thresholderAmount_temp && modalReceiveMoney.getDestinationMobileNumber()==null
                        || modalReceiveMoney.getDestinationMobileNumber().equalsIgnoreCase("null")
                        || modalReceiveMoney.getDestinationMobileNumber().equalsIgnoreCase(""))

                {
                    Toast.makeText(getActivity(),getString(R.string.thresHolderAmount), Toast.LENGTH_SHORT).show();

                }


                else {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_part_one").replace(R.id.frameLayout_cashtocash_receivemoney, new RecipientDetailPartOneFragment()).commit();
                 }



                break;
            }

        }
    }

}
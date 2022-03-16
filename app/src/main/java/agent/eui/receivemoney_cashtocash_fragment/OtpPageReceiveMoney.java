package agent.eui.receivemoney_cashtocash_fragment;

import android.app.ProgressDialog;
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
import android.widget.Toast;

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


public class OtpPageReceiveMoney extends Fragment implements View.OnClickListener , ServerResponseParseCompletedNotifier {

    ModalReceiveMoney modalReceiveMoney;

    View view;
    String otpString;
    AutoCompleteTextView otp_receiveMoney_autoCompletTextview;
    ProgressDialog mDialog;
    ComponentMd5SharedPre mComponentInfo;
    String agentName,agentcode;
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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, OtpPageReceiveMoney.this, message.arg1, message.obj.toString());
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


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.receivemoney_cashtocash_otp_fragment, container, false); // Inflate the layout for this fragment
        Button resend_otp_button,nextButton;


        otp_receiveMoney_autoCompletTextview = (AutoCompleteTextView) view.findViewById(R.id.otp_receiveMoney_autoCompletTextview);

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        resend_otp_button = (Button) view.findViewById(R.id.resend_otp_button);
        resend_otp_button.setOnClickListener(this);

        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentcode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");


        //  senderMobileNumber_textview.setText(String.valueOf(bundle.getString("senderMobileNoString")));
        //  senderFirstName_textView.setText(" " + String.valueOf(bundle.getString("countrySelectionString")));
        //  name_sender_textview.setText(String.valueOf(bundle.getString("receipientNumberString")));

        resendOtp_new();

        mComponentInfo.setArrowBackButton_receiveCash(6);

        return view;
    }


    private String generateResendOtpData() {
        String jsonString = "";

        SharedPreferences prefs = getActivity().getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String mpinString = prefs.getString("MPIN", null);

        String pin = mComponentInfo.getMD5(agentcode + mpinString).toUpperCase();

        try {
            JSONObject countryObj = new JSONObject();

           // countryObj.put("agentcode", agentcode); // comment
            countryObj.put("agentcode", modalReceiveMoney.getDestinationMobileNumber());
           // countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
             countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "RECVCASHINT");     // ??????????????????
            countryObj.put("accounttype", "MA");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            //  countryObj.put("otpCode", otpString);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }



    private void resendOtp_new() {
        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.sendingotp));
            new ServerTask(mComponentInfo, getActivity(), mHandler, generateResendOtpData(), "getEUIOTPInJSON", 238).start();
        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
            //canExitApp = true;
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nextButton: {

                   validationDetails();

                break;

                // login page Api  for otp generate

            }
            case R.id.resend_otp_button: {

                resendOtp_new();

                break;

            }


        }
    }


    void validationDetails() {

        if (validateSendMoneyToMobile_PartI()) {

            otpVerify();

        }

    }

    private boolean validateSendMoneyToMobile_PartI() {
        boolean ret = false;


        otpString = otp_receiveMoney_autoCompletTextview.getText().toString().trim();
        if (otpString.length() > 5) {

            ret = true;

        } else {
            Toast.makeText(getActivity(), getString(R.string.enter_otp_receiveMoney), Toast.LENGTH_LONG).show();
        }


        return ret;
    }


    private String otpverify() {
        String jsonString = "";

        SharedPreferences prefs = getActivity().getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String mpinString = prefs.getString("MPIN", null);

        String pin = mComponentInfo.getMD5(agentcode + mpinString).toUpperCase();

        try {
            JSONObject countryObj = new JSONObject();

          //  countryObj.put("agentcode", agentcode); // comment
            countryObj.put("agentcode", modalReceiveMoney.getDestinationMobileNumber());
           // countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");

            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "RECVCASHINT");     // ??????????????????
            countryObj.put("accounttype", "MA");
            countryObj.put("otpCode", otpString);


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }

    private void otpVerify() {
        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.PleaseWaitVerifyingOTP));

            new ServerTask(mComponentInfo, getActivity(), mHandler, otpverify(), "getEUIOTPInJSON", 239).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
            //canExitApp = true;
        }
    }
    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();

            if (requestNo == 239) {


                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                  //  String[] responseArray = responseData.split("\\|");


                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_mpin").replace(R.id.frameLayout_cashtocash_receivemoney, new MpinPageReceiveMoneyFrgament()).commit();



                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

                }
            }
            else if (requestNo == 238) {

                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");
                  //  Toast.makeText(getActivity(), responseArray[0], Toast.LENGTH_SHORT).show();




                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                }
            }

            else if (requestNo == 239) {

                try {

                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\|");
                 //   Toast.makeText(getActivity(), responseArray[0], Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                }
            }


        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }

    }
}


package agent.internationalremittance.presenter;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.HashMap;

import agent.activities.R;
import agent.internationalremittance.model.InternationalRemittanceModel;
import agent.internationalremittance.view.InternationalRemittanceViewCallaback;
import commonutilities.ComponentMd5SharedPre;


public class InternationalRemittancePresenter implements InternationPresenterCallback {

    InternationalRemittanceViewCallaback viewCallaback;
    View activityView;
    ComponentMd5SharedPre mComponentInfo;
    boolean isRemitSendCheck = false;
    String countryName, iroName, referenceno, currency, currencyConverted, amountEntered, mpin, senderName, senderMobileNo, reciverName, reciverMobileNo;
    InternationalRemittanceModel model;
    int countrySelectedPosition, iroselectedposition, currencySelectedPosition;
    Handler authRequestDelayHandler = new Handler();

    public InternationalRemittancePresenter(InternationalRemittanceViewCallaback viewCallaback, ComponentMd5SharedPre mComponentInfo) {
        this.viewCallaback = viewCallaback;
        this.mComponentInfo = mComponentInfo;
        model = new InternationalRemittanceModel(mComponentInfo, null);
    }

    @Override
    public void onViewCreated(View v) {
        this.activityView = v;
    }

    private Runnable sendData_AuthRequestHandler_Runnable = new Runnable() {
        public void run() {
            try {
                authRequestDelayHandler.removeCallbacks(sendData_AuthRequestHandler_Runnable);

                HashMap<String, String> successValues = new HashMap<>();
                if (isRemitSendCheck) {
                    successValues.put("txnType_Reciept", "Send Remittance");
                    successValues.put("tokenNo_Reciept", model.getTokenNo());
                    successValues.put("amount_Reciept", currencyConverted);
                } else {
                    successValues.put("txnType_Reciept", "Recieve Remittance");
                    successValues.put("tokenNo_Reciept",referenceno);
                    successValues.put("amount_Reciept", amountEntered);
                }
                successValues.put("senderNo_Reciept", senderMobileNo);
                successValues.put("senderName_Reciept", senderName);
                successValues.put("reciverNo_Reciept", reciverMobileNo);
                successValues.put("reciverName_Reciept", reciverName);
                successValues.put("destinationCountry_Reciept", countryName);
                successValues.put("currency_Reciept", currency);
                successValues.put("txnId_Reciept", model.getTokenNo());

                successValues.put("agentCode_Reciept", model.getAgentCode());



                viewCallaback.onValidationSuccess(successValues);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onClick(View v, HashMap<String, String> enteredValues) {
        switch (v.getId()) {

            case R.id.proceedButton_IR:
                senderMobileNo = enteredValues.get("senderMobileNo");
                senderName = enteredValues.get("senderName");
                reciverMobileNo = enteredValues.get("reciverMobileNo");
                reciverName = enteredValues.get("reciverName");
                amountEntered = enteredValues.get("amount");
                mpin = enteredValues.get("mpin");
                referenceno = enteredValues.get("referenceno");


                if (validateDetails()) {
                    viewCallaback.showProgress("Please wait");
                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, 10000);
                }

                break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spinner_countryName:

                countrySelectedPosition = position;

                countryName = "";

                if (position > 0) {
                    countryName = model.getCountryNameArr()[position];
                }
                break;


            case R.id.spinner_IRO:
                iroselectedposition = position;

                iroName = "";

                if (position > 0) {
                    iroName = model.getIroArr()[position];
                }
                break;
            case R.id.spinner_Currency:
                currencySelectedPosition = position;

                currency = "";

                if (position > 0) {
                    currency = model.getCurrencyArr()[position];

                    if(!isRemitSendCheck){
                        viewCallaback.updateAmountHint("Amount in "+model.getCurrencyArr()[position]);
                    }
                }
                break;

        }

    }

    @Override
    public void functionalitySelected(boolean isRemitSend) {
        isRemitSendCheck = isRemitSend;
        HashMap<String, String> map = new HashMap<>();

        map.put("agentCode", model.getAgentCode());
        map.put("agentName", model.getAgentName());

        if (isRemitSend) {
            viewCallaback.updateAmountHint("Amount in XAF");
            map.put("pageTitle", "Please Enter Details For Sending Remittance");
            viewCallaback.setValuesLayout(map, model.getIroArr(), model.getCountryNameArr(), model.getCurrencyArr(), true);
        } else {
            map.put("pageTitle", "Please Enter Details For Receiving Remittance");
            viewCallaback.updateAmountHint("Amount");
            viewCallaback.setValuesLayout(map, model.getIroArr(), model.getCountryNameArr(), model.getCurrencyArr(), false);
        }
    }


    private void convertAmount(String amount){
        String temp;
        double amountT=Double.valueOf(amount);
double finalAmount=0.0;


        switch (currencySelectedPosition){

            case 1:

                amountT=amountT*.0017;
                finalAmount= amountT-model.getFees()[currencySelectedPosition];

                break;


            case 2:
                amountT=amountT*.12;
                finalAmount= amountT-model.getFees()[currencySelectedPosition];
                break;

            case 3:
                amountT=amountT*1.0;
                finalAmount= amountT-model.getFees()[currencySelectedPosition];
                break;




        }
        currencyConverted=finalAmount+"";
        viewCallaback.updateAmountConverted(amountT+" "+model.getCurrencyArr()[currencySelectedPosition]+"." +
                " Fees charged is "+model.getFees()[currencySelectedPosition]+" . Final Amount Transfered is "+finalAmount +" "+model.getCurrencyArr()[currencySelectedPosition]+".");

    }

    private boolean validateAmount(String input) {
        boolean ret = false;

        try {
            int amt = Integer.parseInt(input);
            if (amt > 4999) {
                ret = true;

            }
        } catch (Exception e) {
        }
        return ret;
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(isRemitSendCheck) {

            if (count > 0) {
                if (currencySelectedPosition > 0) {
                    if (validateAmount(s.toString())) {
                        viewCallaback.updateAmountConverted("");
                        convertAmount(s.toString());

                    } else {
                        viewCallaback.updateAmountConverted("");
                        viewCallaback.onValidationFailed("Please enter valid amount. Amount Should not be less than 5000 XAF");
                    }


                } else {
                    viewCallaback.updateAmountConverted("");
                    viewCallaback.onValidationFailed("Please Select Currency before entering amount");
                }

            }
        }

    }


    private boolean validateDetails() {
        boolean ret = false;
        String message = "";

        if (!isRemitSendCheck) {
            if (referenceno == null || referenceno.length() == 6) {

                ret = false;
                message = "Please Enter Reference No";
            }

        }
        if (countrySelectedPosition > 0) {
            if (iroselectedposition > 0) {
                if (currencySelectedPosition > 0) {
                    if (senderMobileNo != null && senderMobileNo.length() > 8) {
                        if (senderName != null && senderName.length() > 3) {
                            if (reciverMobileNo != null && reciverMobileNo.length() > 8) {
                                if (reciverName != null && reciverName.length() > 3) {
                                    if (amountEntered != null && amountEntered.length() > 1) {
                                        if (mpin != null && mpin.length() == 4) {


                                            ret = true;
                                        } else {
                                            message = "Please Enter 4 digit mPIN";

                                        }

                                    } else {
                                        message = "Please Enter Amount";

                                    }

                                } else {
                                    message = "Please Enter Reciver Name";

                                }

                            } else {
                                message = "Please Enter Reciver Mobile No";

                            }

                        } else {
                            message = "Please Enter Sender Name";

                        }

                    } else {
                        message = "Please Enter Sender Mobile No";

                    }
                } else {
                    message = "Please Select Currency";

                }
            } else {
                message = "Please Select Remittance Operator";

            }

        } else {
            message = "Please Select Country";

        }


        if (!ret) {

            viewCallaback.onValidationFailed(message);
        }

        return ret;
    }
}

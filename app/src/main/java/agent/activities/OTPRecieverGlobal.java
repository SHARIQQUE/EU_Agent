package agent.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import commonutilities.ComponentMd5SharedPre;

/**
 * Created by AdityaBugalia on 22/03/18.
 */

public class OTPRecieverGlobal extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ComponentMd5SharedPre mComponentInfo;
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        SmsMessage smsMessage;
        String msg_from = "", messageContent;

        String aa = intent.getStringExtra("sms_body");
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
        }
        if (bundle != null) {

            try {
                if (Build.VERSION.SDK_INT >= 19) {

                    Bundle extras = intent.getExtras();
                    Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);


                    switch (status.getStatusCode()) {
                        case CommonStatusCodes.SUCCESS:

                            messageContent = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);


                            if (msg_from.equalsIgnoreCase("EU TCHAD") ||
                                    msg_from.equalsIgnoreCase("") ||
                                    msg_from.equalsIgnoreCase("EU MOBILE") ||
                                    msg_from.equalsIgnoreCase("EU GABON") ||
                                    msg_from.equalsIgnoreCase("EU CONGO") ||
                                    msg_from.equalsIgnoreCase("EU MOBILE MONEY") ||
                                    msg_from.equalsIgnoreCase("EU RCA") ||
                                    msg_from.equalsIgnoreCase("EU RDC") ||
                                    msg_from.equalsIgnoreCase("8081") ||
                                    msg_from.equalsIgnoreCase("8007") ||
                                    msg_from.equalsIgnoreCase("8041") ||


                                    msg_from.equalsIgnoreCase("+237677117148") ||
                                    msg_from.equalsIgnoreCase("+237675291003") ||
                                    msg_from.equalsIgnoreCase("+237696630812") ||
                                    msg_from.equalsIgnoreCase("+237699426792") ||

                                    msg_from.equalsIgnoreCase("237677117148") ||
                                    msg_from.equalsIgnoreCase("237675291003") ||
                                    msg_from.equalsIgnoreCase("237696630812") ||
                                    msg_from.equalsIgnoreCase("237699426792") ||
                                    msg_from.equalsIgnoreCase("+918860812472") || //Estel Test Number_ INDIA
                                    msg_from.equalsIgnoreCase("+919999644234")) { //Estel Test Number_ INDIA

                                try {

                                    //   messageContent = "Your One-Time Password:123456 to:45454534, 345445. Please do not share this password with anyone.";
                                    //   messageContent = "Votre One-Time Password: 668541 a : 237694140439,9272068.Garder cette information secrete et ne la communiquer a personne";
                                    String[] data = messageContent.split("\\ ");


                                    for (int i = 0; i < data.length; i++) {

                                        data[i] = data[i].replaceAll("[^\\d]", "");

                                        if (data[i].trim().length() == 6) {

                                            mComponentInfo = (ComponentMd5SharedPre) context.getApplicationContext();
                                            mComponentInfo.set_OTP_STRING(data[i]);
                                            Intent newIntent = new Intent("RECIEVE_OTP");
                                            LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
                                            break;
                                        }


                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();

                                }

                            } else {

                                Object pdus[] = (Object[]) bundle.get("pdus");
                                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                                msg_from = smsMessage.getDisplayOriginatingAddress();
                                messageContent = smsMessage.getMessageBody();
                                if (msg_from.equalsIgnoreCase("EU TCHAD") ||
                                        msg_from.equalsIgnoreCase("EU MOBILE") ||
                                        msg_from.equalsIgnoreCase("EU GABON") ||
                                        msg_from.equalsIgnoreCase("EU CONGO") ||
                                        msg_from.equalsIgnoreCase("EU MOBILE MONEY") ||
                                        msg_from.equalsIgnoreCase("EU RCA") ||
                                        msg_from.equalsIgnoreCase("EU RDC") ||
                                        msg_from.equalsIgnoreCase("8081") ||
                                        msg_from.equalsIgnoreCase("8007") ||
                                        msg_from.equalsIgnoreCase("8041") ||

                                        msg_from.equalsIgnoreCase("+237677117148") ||
                                        msg_from.equalsIgnoreCase("+237675291003") ||
                                        msg_from.equalsIgnoreCase("+237696630812") ||
                                        msg_from.equalsIgnoreCase("+237699426792") ||

                                        msg_from.equalsIgnoreCase("237677117148") ||
                                        msg_from.equalsIgnoreCase("237675291003") ||
                                        msg_from.equalsIgnoreCase("237696630812") ||
                                        msg_from.equalsIgnoreCase("237699426792") ||
                                        msg_from.equalsIgnoreCase("+918860812472") ||
                                        msg_from.equalsIgnoreCase("+919999644234")) {

                                    try {

                                        messageContent = smsMessage.getMessageBody();
                                        //  messageContent = "Your One-Time Password:123456 to:45454534, 345445. Please do not share this password with anyone.";
                                        String[] data = messageContent.split("\\ ");


                                        for (int i = 0; i < data.length; i++) {

                                            data[i] = data[i].replaceAll("[^\\d]", "");

                                            if (data[i].trim().length() == 6) {

                                                mComponentInfo = (ComponentMd5SharedPre) context.getApplicationContext();
                                                mComponentInfo.set_OTP_STRING(data[i]);
                                                Intent newIntent = new Intent("RECIEVE_OTP");
                                                LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);

                                                break;
                                            }


                                        }


                                    } catch (Exception e) {


                                    }
                                }
                            }
                            break;
                        case CommonStatusCodes.TIMEOUT:

                            break;

                    }
//                    msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
//                    smsMessage = msgs[0];
//                    msg_from = smsMessage.getDisplayOriginatingAddress();

                }

            } catch (Exception e) {

                e.printStackTrace();


            }


        }
    }
}
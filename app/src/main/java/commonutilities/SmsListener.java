package commonutilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by AdityaBugalia on 21/10/16.
 */

public class SmsListener extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {

            Intent i=new Intent("SMS_RECIEVED");
            //i.setAction("android.provider.Telephony.SMS_RECEIVED");
            i.putExtras(intent.getExtras());
            context.sendBroadcast(i);

        }
    }
}
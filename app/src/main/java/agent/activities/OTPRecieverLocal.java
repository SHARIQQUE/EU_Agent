package agent.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;


import commonutilities.ComponentMd5SharedPre;

/**
 * Created by AdityaBugalia on 22/03/18.
 */

public class OTPRecieverLocal extends BroadcastReceiver {

ComponentMd5SharedPre mComponentInfo;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent("RECIEVE_OTP");
        mComponentInfo=(ComponentMd5SharedPre)context.getApplicationContext() ;
        if(mComponentInfo.get_OTP_STRING().trim().length()>0) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
        }

    }
}

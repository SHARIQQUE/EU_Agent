package commonutilities;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetCheck {
    public boolean isConnected(Context ctx) {
        boolean status = false;

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }
}

package commonutilities;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class CommonUtilities {

    public static final String PREFFS_NAME = "eu_agentprefs";
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int REQUEST_DEVICE = 2;
    public static int activationLevel;
    public static int transactionType;
    public static boolean redirection;
    public static BluetoothDevice mDevice;
    public static boolean pinpadDisconnect;
    public static boolean print = false;
    // public static PrintNotifier printNotifier;
    public static Context act;
    public static String txnType = "";
    public static boolean isTxnApprovalPhase1 = true;

    public static boolean cashToCashReceive = true;


}
package printer_utilities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import commonutilities.CommonUtilities;


public class PrinterManager {
    // A listener to notify when connection is closed.
    public interface OnConnectionClosedListener {
        public void OnConnectionClosed();
    }

    // A listener to notify when connection is established.
    public interface OnConnectionEstablishedListener {
        public void OnConnectionEstablished();
    }

    private static final boolean PRINTER_DEBUG = false;

    private static final UUID SPP_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static PrinterManager sInstance = null;
    ;

    public static PrinterManager getInstance(Context context) {


        if (sInstance == null) {
            sInstance = new PrinterManager(context);
        }

        return sInstance;
    }

    // Local parameters.
    private BluetoothAdapter mBtAdapter;
    private BluetoothSocket mBtSocket;
    private boolean connectMethodResult;
    BluetoothSocket socket;
    BluetoothDevice device;
    private OnConnectionEstablishedListener mOnConnectEstablishedListener;
    private int connectionAttemptType;
    private OnConnectionClosedListener mOnConnectionClosedListener;
    int btMode = 99;
    InputStream in;
    OutputStream out;
    Context ctx;
    private IOException tempException;

    // Class constructor.
    private PrinterManager(Context context) {
        this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public synchronized BluetoothSocket connect(final String btAddress,Context ctx) throws IOException {
        this.ctx = ctx;
        // socket = getBtSocket(btAddress);
        SharedPreferences settings = ctx.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
        btMode = settings.getInt("btMode", 99);

        switch (btMode) {

            case 99:
                socketVerify(btAddress);

                break;
            case 1:
                tempException = null;
                tempException = getSocketInsecure(btAddress);
                if (tempException != null) {
                    socketVerify(btAddress);
                }
                break;

            case 2:
                tempException = null;
                tempException = getSocketSecure(btAddress);
                if (tempException != null) {
                    socketVerify(btAddress);
                }
                break;

            default:
                socketVerify(btAddress);
                break;

        }

        mBtAdapter.cancelDiscovery();

        System.out.println("Bluetooth Socket discovery cancelled done.");

        try {
            validateConnection(socket);
        } catch (IOException e) {
            socket.close();
            System.out.println("Bluetooth Socket Exception Returned:"
                    + e.toString());
            throw e;
        }

        return socket;

    }

//	private void closeBtConnection() {
//		// Release printer instance.
//
//		// Release current socket.
//		if (mBtSocket != null) {
//			try {
//
//				mBtSocket.close();
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				mBtSocket.close();
//				mBtSocket = null;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

    private IOException createRfcommSocketToServiceRecord(UUID SPP_UUID,
                                                          String btAddress) {

        connectMethodResult = false;
        IOException result = null;

        device = mBtAdapter.getRemoteDevice(btAddress);

        try {
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            socket.connect();
            connectMethodResult = true;
            SharedPreferences settings = ctx.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putInt("btMode", 3);
            editor.commit();

        } catch (IOException e) {
            result = e;
        }

        return result;

    }

    public synchronized boolean disconnect() {
        // closeBtConnection();
        boolean ret = false;

        if (mBtSocket != null) {
            try {

                mBtSocket.close();
//			
//				Thread.sleep(0);

                mBtSocket.close();
                mBtSocket = null;
                ret = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setConnected(false);
        return ret;
    }

    public String getBluetoothAddress() {
        return mBtSocket.getRemoteDevice().getAddress();
    }

    public String getBluetoothName() {
        return mBtSocket.getRemoteDevice().getName();
    }

    @SuppressLint("NewApi")
    private BluetoothSocket getBtSocket(String btAddress) throws IOException {
        BluetoothSocket socket = null;

        BluetoothDevice device = mBtAdapter.getRemoteDevice(btAddress);

        if (VERSION.SDK_INT < VERSION_CODES.GINGERBREAD_MR1) {
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            // mdobj.setsrvstatus(false);
            System.out.println("Bluetooth Version check If");
        } else {
            try {
                // compatibility with pre SDK 10 devices
                /*
				 * Method method = device.getClass().getMethod(
				 * "createInsecureRfcommSocketToServiceRecord", UUID.class);
				 * socket = (BluetoothSocket) method.invoke(device, SPP_UUID);
				 */
                System.out.println("Bluetooth Version check Else");
                if (Build.MANUFACTURER.equalsIgnoreCase("Samsung")
                        || Build.MANUFACTURER.contains("Nexus")
                        || Build.MANUFACTURER.contains("HTC")
                        || Build.MANUFACTURER.contains("SONY")
                        || Build.MANUFACTURER.contains("MOTO")
                        || Build.MANUFACTURER.contains("LENOVO")) {
                    System.out
                            .println("Bluetooth Version check Else Manufacturer");
                    socket = device
                            .createInsecureRfcommSocketToServiceRecord(SPP_UUID);
                    // mdobj.setsrvstatus(false);
                } else {
                    System.out
                            .println("Bluetooth Version check Else Manufacturer 11111111111111");
                    Method m = device.getClass().getMethod(
                            "createRfcommSocket", new Class[]{int.class});
                    socket = (BluetoothSocket) m.invoke(device, 1);
                    // mdobj.setsrvstatus(true);
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw new IOException(e);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new IOException(e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new IOException(e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
        return socket;
    }

    @SuppressLint("NewApi")
    public IOException getSocketInsecure(String btAddress) {
        connectMethodResult = false;
        IOException result = null;
        device = mBtAdapter.getRemoteDevice(btAddress);
        try {
            if (VERSION.SDK_INT >= 2.2) {
                socket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        try {
            socket.connect();

            connectMethodResult = true;

            SharedPreferences settings = ctx.getSharedPreferences(CommonUtilities.PREFFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putInt("btMode", 1);
            editor.commit();
        } catch (IOException e) {
            result = e;
        }

        return result;

    }

    public IOException getSocketSecure(String btAddress) {
        connectMethodResult = false;
        IOException result = null;
        Method m = null;

        device = mBtAdapter.getRemoteDevice(btAddress);

        try {
            m = device.getClass().getMethod("createRfcommSocket",
                    new Class[]{int.class});
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            socket = (BluetoothSocket) m.invoke(device, 1);

        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {

            e.printStackTrace();
        }
        try {
            socket.connect();
            connectMethodResult = true;
            SharedPreferences settings = ctx.getSharedPreferences(
                    CommonUtilities.PREFFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putInt("btMode", 2);
            editor.commit();

        } catch (IOException e) {
            result = e;
        }

        return result;
    }

    public Printer initPrinter(ProtocolAdapter mProtocolAdapter,
                               ProtocolAdapter.ChannelListener mChannelListener, Printer mPrinter1)
            throws IOException {

        // 200 ==> All set.
        //


        mProtocolAdapter = new ProtocolAdapter(socket.getInputStream(),
                socket.getOutputStream());

        if (mProtocolAdapter.isProtocolEnabled()) {
            final ProtocolAdapter.Channel channel = mProtocolAdapter
                    .getChannel(ProtocolAdapter.CHANNEL_PRINTER);
            channel.setListener(mChannelListener);
            // Create new event pulling agent.thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            channel.pullEvent();
                        } catch (IOException e) {
                            e.printStackTrace();
                            // error(e.getMessage(), mRestart);
                            break;
                        }
                    }
                }
            }).start();

            mPrinter1 = new Printer(channel.getInputStream(), channel.getOutputStream());
        } else {
            mPrinter1 = new Printer(mProtocolAdapter.getRawInputStream(),
                    mProtocolAdapter.getRawOutputStream());

        }

        return mPrinter1;

    }

    private synchronized void setConnected(boolean state) {

        if (state) {
            if (mOnConnectEstablishedListener != null) {
                mOnConnectEstablishedListener.OnConnectionEstablished();
            }
        } else {
            if (mOnConnectionClosedListener != null) {
                mOnConnectionClosedListener.OnConnectionClosed();
            }
        }
    }

    public void setOnConnectionClosedListener(
            OnConnectionClosedListener listener) {
        mOnConnectionClosedListener = listener;
    }

    public void setOnConnectionEstablishedListener(
            OnConnectionEstablishedListener listener) {
        mOnConnectEstablishedListener = listener;
    }

    @SuppressLint("NewApi")
    private void socketVerify(String btAddress) throws IOException {

        Exception e = null;
        if (VERSION.SDK_INT < VERSION_CODES.GINGERBREAD_MR1) {

            e = createRfcommSocketToServiceRecord(SPP_UUID, btAddress);
            if (connectMethodResult == false) {
                e = createRfcommSocketToServiceRecord(SPP_UUID, btAddress);
                if (connectMethodResult == false) {
                    if (VERSION.SDK_INT >= 3.0) {
                        throw new IOException(e);
                    } else {

                    }
                }
            }

        } else {
            if (connectionAttemptType == 00) {
                e = getSocketSecure(btAddress);
                if (connectMethodResult == false) {
                    connectionAttemptType = 01;
                    e = getSocketSecure(btAddress);
                    if (connectMethodResult == false) {
                        connectionAttemptType = 02;
                    }
                }
            }

            if (connectionAttemptType == 02) {
                e = getSocketInsecure(btAddress);
                if (connectMethodResult == false) {
                    connectionAttemptType = 03;
                    e = getSocketInsecure(btAddress);
                    if (connectMethodResult == false) {
                        connectionAttemptType = 04;
                    }
                }
            }

            if (connectionAttemptType == 04) {
                e = getSocketInsecure(btAddress);
                if (connectMethodResult == false) {
                    connectionAttemptType = 05;
                    e = getSocketInsecure(btAddress);
                    if (connectMethodResult == false) {
                        connectionAttemptType = 06;
                    }
                }
            }

            if (connectionAttemptType == 06) {
                connectionAttemptType = 00;
                throw new IOException(e);
            }
        }
    }

    private synchronized void validateConnection(BluetoothSocket socket)
            throws IOException {

        in = socket.getInputStream();

        out = socket.getOutputStream();
        // Pinpad pinpad = new Pinpad(in, out);
        // pinpad.sysBeep(4096, 256, 50);
        // pinpad.setPinpadListener(new PinpadListener() {
        // @Override
        // public void onPinpadRelease() {
        // closeBtConnection();
        // setConnected(false);
        // }
        // });
        //
        // mPinpad = pinpad;
        mBtSocket = socket;
        setConnected(true);
    }


}
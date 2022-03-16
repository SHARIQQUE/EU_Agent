package printer_utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import agent.activities.R;
import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;


public class PrinterListShowActivity extends Activity {
    private class BluetootDevicePair {
        String name;
        String addr;
    }

    ComponentMd5SharedPre mComponentInfo;

    private class BluetoothDeviceAdapter extends ArrayAdapter<BluetootDevicePair> {
        public BluetoothDeviceAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // View
            // v=BluetoothDeviceListShowActivity.this.getLayoutInflater().inflate(R.layout.bluetooth_listview_item,
            // null);
            if (convertView == null) {
                convertView = (((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.bluetooth_listview_item, null));
            }

            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView address = (TextView) convertView.findViewById(R.id.address);

            BluetootDevicePair device = getItem(position);
            name.setText(device.name);
            address.setText(device.addr);

            return convertView;
        }
    }

    private ProgressDialog dialog;
    private Intent intent;

    private BluetoothDeviceAdapter mListAdapter;
    private ListView mListView;

    // Connect to specific bluetooth device.
    public void connectToDevice(final String btAddress) {
        // Construct a progress dialog to prevent user from actions until
        // connection is finished.
        dialog = new ProgressDialog(PrinterListShowActivity.this);
        dialog.setMessage("Connecting To Printer");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                return true;
            }
        });
        dialog.show();
        // Force connection to be execute in separate agent.thread.
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    BluetoothAdapter btAdapter = BluetoothAdapter
                            .getDefaultAdapter();

                    CommonUtilities.mDevice = btAdapter.getRemoteDevice(btAddress);

                    if (CommonUtilities.mDevice.getBondState() == BluetoothDevice.BOND_NONE) {

                    } else if (CommonUtilities.mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {

                        Log.i("","device.getBondState() is BluetoothDevice.BOND_NONE");
                        SharedPreferences.Editor editor = mComponentInfo.getmSharedPreferences().edit();
                        editor.putString("printerAddress", btAddress);
                        editor.putString("printerUUID",BluetoothDevice.EXTRA_UUID);
                        editor.commit();
                        intent = new Intent();
                        intent.putExtra("printerAddress", btAddress);
                        setResult(RESULT_OK, intent);

                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    showToast("Failed To Connect to Printer" + ": "+ e.getMessage());
                } finally {
                    dialog.dismiss();
                }
            }
        });
        t.start();
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bluetooth_device_list);
        setResult(RESULT_CANCELED);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        mListAdapter = new BluetoothDeviceAdapter(this);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                String btAddress = mListAdapter.getItem(position).addr;
                connectToDevice(btAddress);
            }
        });

        mListView.setAdapter(mListAdapter);

        findViewById(R.id.cancelBluetoothDeviceListLayout).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        findViewById(R.id.refreshBluetoothDeviceListLayout).setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDeviceList();
                    }
                });

        updateDeviceList();
    }

    /**
     * Called when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Show toast notification, running in UI agent.thread.
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Populate list with paired bluetooth devices.
    private void updateDeviceList() {
        BluetoothAdapter bthAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mListAdapter != null) {
            mListAdapter.clear();
        }
        if (bthAdapter != null) {
            for (BluetoothDevice device : bthAdapter.getBondedDevices()) {
                BluetootDevicePair pair = new BluetootDevicePair();
                pair.name = device.getName();
                pair.addr = device.getAddress();
                mListAdapter.add(pair);
            }
        }
        mListAdapter.notifyDataSetChanged();
    }

}

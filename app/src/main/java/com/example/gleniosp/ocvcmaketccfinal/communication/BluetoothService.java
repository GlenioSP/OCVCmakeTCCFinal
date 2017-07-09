package com.example.gleniosp.ocvcmaketccfinal.communication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothService {

    private Context mContext = null;
    private BluetoothAdapter bluetoothAdapter = null;
    private boolean bluetoothEnabled;

    private BroadcastReceiver bluetoothReceiverService = null;

    public BluetoothService(Context mContext) {

        this.mContext = mContext;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothEnabled = true;
            }
            else {
                bluetoothEnabled = false;
            }

            bluetoothReceiverService = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                        if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                                == BluetoothAdapter.STATE_ON) {
                            // Bluetooth is connected
                            bluetoothEnabled = true;
                            ((Activity ) context).invalidateOptionsMenu();
                        }
                        else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                                == BluetoothAdapter.STATE_OFF) {
                            // Bluetooth is disconnected
                            bluetoothEnabled = false;
                            ((Activity ) context).invalidateOptionsMenu();
                        }
                    }
                }
            };

            // Register for broadcasts when a device is discovered
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            mContext.registerReceiver(bluetoothReceiverService, filter);
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public boolean getBluetoothEnabled() {
        return bluetoothEnabled;
    }

    public BroadcastReceiver getBluetoothReceiverService() {
        return bluetoothReceiverService;
    }

    public Intent enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            return enableIntent;
        }
        else {
            // bluetooth already enabled
            return null;
        }
    }

    public Intent ensureDiscoverable() {
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(
                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            return discoverableIntent;
        }
        else {
            // bluetooth already discoverable
            return null;
        }
    }

//    public Intent cancelDiscoverable() {
//        // make discoverable lasts just for 1 second more
//        Intent undiscoverableIntent = new
//                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        undiscoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1);
//        return undiscoverableIntent;
//    }

}

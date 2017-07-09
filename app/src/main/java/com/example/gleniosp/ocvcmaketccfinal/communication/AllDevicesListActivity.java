package com.example.gleniosp.ocvcmaketccfinal.communication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gleniosp.ocvcmaketccfinal.R;

import java.util.Set;

public class AllDevicesListActivity extends Activity {

    private TextView tvBLDialogDeviceListPairedDeviceTitle, tvBLDialogDeviceListNewDeviceTitle;
    private ListView lvBLDialogDeviceListPairedDevice, lvBLDialogDeviceListNewDevice;
    private Button btnBLDialogDeviceListScan;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> pairedDevicesArrayAdapter;
    private ArrayAdapter<String> newDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_all_devices_list);

        getWidgetReferences();
        bindEventHandler();
        initializeValues();
    }

    private void getWidgetReferences() {
        tvBLDialogDeviceListPairedDeviceTitle = (TextView) findViewById(R.id.tvBLDialogDeviceListPairedDeviceTitle);
        tvBLDialogDeviceListNewDeviceTitle = (TextView) findViewById(R.id.tvBLDialogDeviceListNewDeviceTitle);

        lvBLDialogDeviceListPairedDevice = (ListView) findViewById(R.id.lvBLDialogDeviceListPairedDevice);
        lvBLDialogDeviceListNewDevice = (ListView) findViewById(R.id.lvBLDialogDeviceListNewDevice);

        btnBLDialogDeviceListScan = (Button) findViewById(R.id.btnBLDialogDeviceListScan);

    }

    private void bindEventHandler() {

        btnBLDialogDeviceListScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDiscovery();
                btnBLDialogDeviceListScan.setVisibility(View.GONE);
            }
        });
    }

    private void initializeValues() {
        pairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        newDevicesArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        lvBLDialogDeviceListPairedDevice.setAdapter(pairedDevicesArrayAdapter);
        lvBLDialogDeviceListNewDevice.setAdapter(newDevicesArrayAdapter);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryFinishReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryFinishReceiver, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter
                .getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            tvBLDialogDeviceListPairedDeviceTitle.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n"
                        + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.activity_bl_dialog_no_paired_device)
                    .toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private void startDiscovery() {
        setTitle(R.string.activity_bl_dialog_scanning_device);

        tvBLDialogDeviceListNewDeviceTitle.setVisibility(View.VISIBLE);

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    newDevicesArrayAdapter.add(device.getName() + "\n"
                            + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle(R.string.activity_bl_dialog_all_devices);
                if (newDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(
                            R.string.activity_bl_dialog_no_device_found).toString();
                    newDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(discoveryFinishReceiver);
    }

}


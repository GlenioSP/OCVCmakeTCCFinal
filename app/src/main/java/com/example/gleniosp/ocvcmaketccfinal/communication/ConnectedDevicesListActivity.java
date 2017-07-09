package com.example.gleniosp.ocvcmaketccfinal.communication;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gleniosp.ocvcmaketccfinal.MainActivity;
import com.example.gleniosp.ocvcmaketccfinal.R;


public class ConnectedDevicesListActivity extends Activity {

    private TextView tvBLDialogConnectedDevicesListSubtitle;
    private ListView lvBLDialogConnectedDevicesList;

    private ArrayAdapter<String> connectedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connected_devices_list);

        setResult(Activity.RESULT_CANCELED);

        getWidgetReferences();
        bindEventHandler();
        initializeValues();
    }

    private void getWidgetReferences() {
        tvBLDialogConnectedDevicesListSubtitle = (TextView) findViewById(R.id.tvBLDialogConnectedDevicesListSubtitle);
        lvBLDialogConnectedDevicesList = (ListView) findViewById(R.id.lvBLDialogConnectedDevicesList);
    }

    private void bindEventHandler() {
        lvBLDialogConnectedDevicesList.setOnItemClickListener(mDeviceClickListener);
    }

    private void initializeValues() {
        connectedDevicesArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        lvBLDialogConnectedDevicesList.setAdapter(connectedDevicesArrayAdapter);

        if (MainActivity.chatService.getNumberConnectedDevices() > 0) {
            BluetoothDevice bluetoothDevice;
            for (int i = 0; i < MainActivity.chatService.getListDevicesSocket().size(); i++) {
                if (MainActivity.chatService.getListDevicesSocket().get(i) != null) {
                    bluetoothDevice = MainActivity.chatService.getListDevicesSocket().get(i).getRemoteDevice();
                    connectedDevicesArrayAdapter.add(bluetoothDevice.getName() + "\n"
                            + bluetoothDevice.getAddress());
                }
            }
        } else {
            String noDevices = getResources().getText(R.string.activity_bl_dialog_no_connected_device)
                    .toString();
            connectedDevicesArrayAdapter.add(noDevices);
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            String info = ((TextView) v).getText().toString();
            String deviceAddress = info.substring(info.length() - 17);
            String deviceName = info.substring(0, info.length() - 18);

            showDialog(deviceName, deviceAddress);
        }
    };

    void showDialog(final String deviceName, final String deviceAddress) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Warning!");

        final TextView input = new TextView(this);
        input.setText("The device " + deviceName + " will be disconnected from server. " +
                "\nDo you want to disconnect it?");

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_tv_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_tv_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_tv_margin);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_tv_margin);
        input.setLayoutParams(params);
        container.addView(input);

        b.setView(container);

        b.setPositiveButton("DISCONNECT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                Intent intent = new Intent();
                intent.putExtra("CONNECTED_DEVICE_ADDRESS", deviceAddress);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        b.setNegativeButton("CANCEL", null);
        b.show();
    }
}

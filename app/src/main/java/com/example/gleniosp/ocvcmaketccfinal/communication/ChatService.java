package com.example.gleniosp.ocvcmaketccfinal.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.gleniosp.ocvcmaketccfinal.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

public class ChatService {

    private Context mContext = null;

    private static final String NAME = "OCVMapServer";

    // source: https://stackoverflow.com/questions/13964342/android-how-do-bluetooth-uuids-work
    private static final UUID SERVER_UUID = UUID.
            fromString("00001101-0000-1000-8000-00805f9b34fb");

    private final int MAX_CONNECTED_DEVICES = 7;
    private int number_connected_devices = 0;
    private HashMap<Integer, BluetoothSocket> listDevicesSocket =
            new HashMap<>(MAX_CONNECTED_DEVICES);

    private final BluetoothAdapter bluetoothAdapter;
    private final Handler handler;
    private AcceptThread acceptThread;
    private HashMap<Integer, ConnectedThread> listConnectedThreads =
            new HashMap<Integer, ConnectedThread>(MAX_CONNECTED_DEVICES);
    private int state;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTED = 2;

    public ChatService(Context mContext, Handler handler) {

        this.mContext = mContext;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        state = STATE_NONE;

        this.handler = handler;
    }

    // Set the current state of the chat connection
    private synchronized void setState(int state) {
        this.state = state;
    }

    // get current connection state
    public synchronized int getState() {
        return state;
    }

    public synchronized int getNumberConnectedDevices() {
        return number_connected_devices;
    }

    public synchronized HashMap<Integer, BluetoothSocket> getListDevicesSocket() {
        return listDevicesSocket;
    }

    // start service
    public synchronized void startServer() {

        setState(STATE_LISTEN);

        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    // manage Bluetooth connection
    private synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {

        int index = getDeviceIndexByAddress(device.getAddress());

        // Start the thread to manage the connection and perform transmissions
        listConnectedThreads.put(index, new ConnectedThread(socket, index));
        listConnectedThreads.get(index).start();

        // Send the name of the connected device back to the UI Activity
        Message msg = handler.obtainMessage(MainActivity.BLUETOOTH_MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.BLUETOOTH_DEVICE_NAME, device.getName() + " (device:" +
                index + ")");
        msg.setData(bundle);
        handler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    // before writing, make sure to check if state is STATE_CONNECTED
    public synchronized void writeToAll(byte[] out) {
        for (int i = 0; i < listConnectedThreads.size(); i++) {
            if (listConnectedThreads.get(i) != null) {
                listConnectedThreads.get(i).write(out);
            }
        }
    }

    private synchronized void connectionLost(int index, String deviceName) {

        Message msg = handler.obtainMessage(MainActivity.BLUETOOTH_MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.BLUETOOTH_TOAST_RECEIVED, "Connection with device " +
                deviceName + " (device:" + index + ") was lost.");
        msg.setData(bundle);
        handler.sendMessage(msg);

        // remove thread from list
        if (listDevicesSocket.get(index) != null) {
            listConnectedThreads.get(index).cancel();
        }
        listConnectedThreads.remove(index);
        // remove device from list
        listDevicesSocket.remove(index);

        number_connected_devices--;

        if (number_connected_devices == 0) {
            setState(STATE_LISTEN);
        }

    }

    public synchronized void disconnectDevice(String deviceAddress) {

        int index = getDeviceIndexByAddress(deviceAddress);

        if (index != -1) {
            if (listDevicesSocket.get(index) != null) {
                listConnectedThreads.get(index).cancel();
            }
        }
    }

    // stop all threads
    public synchronized void stopServer() {

        for (int i = 0; i < listConnectedThreads.size(); i++) {
            if (listDevicesSocket.get(i) != null) {
                listConnectedThreads.get(i).cancel();
            }
            listConnectedThreads.remove(i);
        }

        for (int i = 0; i < listDevicesSocket.size(); i++) {
            listDevicesSocket.remove(i);
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        number_connected_devices = 0;

        setState(STATE_NONE);
    }

    private synchronized int getDeviceIndexByAddress(String deviceAddress) {

        int index = -1;

        for (int i = 0; i < listDevicesSocket.size(); i++) {
            if (listDevicesSocket.get(i) != null) {
                if (deviceAddress.equals(listDevicesSocket.get(i).getRemoteDevice().getAddress())) {
                    index = i;
                }
            }
        }

        return index;
    }

    /*
     * THREADS
     * *******************************/

    // runs while listening for incoming connections
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                // standard secure listen
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                        NAME, SERVER_UUID);
            } catch (IOException e) {
                Toast.makeText(mContext, "Server Socket could not be opened. Please, try again.",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            serverSocket = tmp;
        }

        public void run() {
            setName("AcceptThread");

            BluetoothSocket socket = null;

            while (number_connected_devices <= MAX_CONNECTED_DEVICES) {
                switch(state) {
                    case STATE_LISTEN:
                    case STATE_CONNECTED:
                        try {
                            socket = serverSocket.accept();
                        } catch (IOException e) {
                            break;
                        }

                        // If a connection was accepted
                        if (socket != null) {

                            synchronized (ChatService.this) {
                                // start the connected thread.
                                listDevicesSocket.put(number_connected_devices, socket);
                                int index = getDeviceIndexByAddress(
                                        socket.getRemoteDevice().getAddress());
                                connected(listDevicesSocket.get(index),
                                        listDevicesSocket.get(index).getRemoteDevice());
                                number_connected_devices++;
                            }
                        }
                        break;
                    case STATE_NONE:
                        if (number_connected_devices == 0) {
                            ChatService.this.stopServer();
                        }
                        return;
                }
            }
            if (number_connected_devices == MAX_CONNECTED_DEVICES) {
                Toast.makeText(mContext, "Maximum number of connected devices to server was " +
                                "reached.", Toast.LENGTH_LONG).show();
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

    // runs during a connection with a remote device
    private class ConnectedThread extends Thread {

        private final BluetoothSocket bluetoothSocket;
        private final String deviceName;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        private final int index;

        public ConnectedThread(BluetoothSocket socket, int index) {
            bluetoothSocket = socket;
            deviceName = bluetoothSocket.getRemoteDevice().getName();
            this.index = index;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                disconnectDevice(bluetoothSocket.getRemoteDevice().getAddress());
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            setName("ConnectedThread" + index);

            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = inputStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    handler.obtainMessage(MainActivity.BLUETOOTH_MESSAGE_READ, bytes, -1,
                            buffer).sendToTarget();
                } catch (IOException e) {
                    connectionLost(index, deviceName);
                    break;
                }
            }
        }

        // write to OutputStream
        public void write(byte[] buffer) {
            try {
                outputStream.write(buffer);
            } catch (IOException e) {
                Toast.makeText(mContext, "Server could not write to device socket with name: " +
                                listDevicesSocket.get(index).getRemoteDevice().getName(),
                        Toast.LENGTH_LONG).show();
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Toast.makeText(mContext, "Server could not close connection between itself " +
                                "and device socket with name: " +
                                listDevicesSocket.get(index).getRemoteDevice().getName(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}

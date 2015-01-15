package com.codefest.rearsensordroid.bluetooth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import java.util.UUID;

/**
 * Created by Carlos Monzón on 11/15/2014.
 */
public class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {

    final static Logger logger = LoggerFactory.getLogger(ConnectAsyncTask.class);

    private BluetoothSocket bluetoothSocket;

    private ConnectedThread connectedThread;

    private DisplaySensor displaySensor;

    public ConnectAsyncTask(DisplaySensor displaySensor) {
        this.displaySensor = displaySensor;
    }

    @Override
    protected BluetoothSocket doInBackground(BluetoothDevice... device) {
        BluetoothSocket mmSocket = null;
        BluetoothDevice mmDevice = device[0];

        try {
            String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
            mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
            logger.info("Created insecure socket to BlueTooth");
            mmSocket.connect();
            logger.info("Connected to BlueTooth");
        } catch (Exception e) {
            logger.debug("Exception when creating insecure RF socket: " + e.getMessage());
        }

        return mmSocket;
    }

    @Override
    protected void onPostExecute(BluetoothSocket result) {
        bluetoothSocket = result;
        if (bluetoothSocket != null) {
            logger.info("BlueTooth socket is not null");
            connectedThread = new ConnectedThread(bluetoothSocket, displaySensor);
            connectedThread.start();
        } else {
            logger.info("BlueTooth socket is null");
        }
    }
}

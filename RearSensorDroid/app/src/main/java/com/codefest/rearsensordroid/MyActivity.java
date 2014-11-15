package com.codefest.rearsensordroid;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MyActivity extends ListActivity {

    private Button btToggle;
    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket btSocket;
    private ArrayList<BluetoothDevice> btDeviceArray = new ArrayList<BluetoothDevice>();
    private ConnectAsyncTask connectAsyncTask;
    private ConnectedThread contecThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        setListAdapter(mArrayAdapter);

        // Instance AsyncTask
        connectAsyncTask = new ConnectAsyncTask();

        //Get Bluettoth Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check smartphone support Bluetooth
        if (mBluetoothAdapter == null) {
            //Device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "Not support bluetooth", Toast.LENGTH_LONG).show();
            finish();
        }

        // Check Bluetooth enabled
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        // Queryng paried devices
        Set<BluetoothDevice> pariedDevices = mBluetoothAdapter.getBondedDevices();
        if (pariedDevices.size() > 0) {
            for (BluetoothDevice device : pariedDevices) {
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                btDeviceArray.add(device);
            }
        }

        btToggle = (Button) findViewById(R.id.btToggle);
        btToggle.setOnClickListener(btToggleOnClickListener);

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        BluetoothDevice device = btDeviceArray.get(position);
        connectAsyncTask.execute(device);

    }

    // Click event on Button
    private OnClickListener btToggleOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

//            OutputStream mmOutStream = null;
//            InputStream inputStream = null;
//
//            try {
//
//                if (btSocket.isConnected()) {
//                    mmOutStream = btSocket.getOutputStream();
//                    inputStream = btSocket.getInputStream();
//                    mmOutStream.write(new String("L").getBytes());
//                    inputStream.read();
//
//                }
//
//            } catch (IOException e) {
//            }
            contecThread.write(new String("L").getBytes());

        }
    };


    private class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {

        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... device) {

            mmDevice = device[0];

            try {

                String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
                mmSocket.connect();

            } catch (Exception e) {
            }

            return mmSocket;
        }

        @Override
        protected void onPostExecute(BluetoothSocket result) {

            btSocket = result;
            //Enable Button
            btToggle.setEnabled(true);
            contecThread = new ConnectedThread(btSocket);
            contecThread.start();

        }


    }


    /**
     * Created by Carlos on 11/15/2014.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                try {
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                    for (int i = begin; i < bytes; i++) {
                        if (buffer[i] == "#".getBytes()[0]) {
                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if (i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.d("WRITE", e.getLocalizedMessage());
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int) msg.arg1;
            int end = (int) msg.arg2;

            switch (msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
//                    Toast.makeText(getApplicationContext(),writeMessage,Toast.LENGTH_LONG).show();
//
                    TextView distancia = (TextView) findViewById(R.id.distancia);
                    distancia.setText(writeMessage);
                    break;
            }
        }
    };

}

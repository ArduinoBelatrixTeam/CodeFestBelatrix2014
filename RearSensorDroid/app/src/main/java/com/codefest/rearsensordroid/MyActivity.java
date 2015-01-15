package com.codefest.rearsensordroid;

import com.codefest.rearsensordroid.bluetooth.ConnectAsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MyActivity extends ListActivity {

    //TODO: inactive the logger, save the file appender when the application is down.


    private ArrayAdapter<String> mArrayAdapter;

    private BluetoothAdapter mBluetoothAdapter;

    private ArrayList<BluetoothDevice> btDeviceArray = new ArrayList<BluetoothDevice>();

    private ConnectAsyncTask connectAsyncTask;

    private TextViewDisplaySensor textViewDisplaySensor;

    final static Logger logger = LoggerFactory.getLogger(MyActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logger.info("starting onCreate method");

        //application keeps the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        textViewDisplaySensor = new TextViewDisplaySensor((TextView) findViewById(R.id.distancia));

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        setListAdapter(mArrayAdapter);

        //Get Bluetooth Adapter
        logger.info("Getting Bluetooth Adapter");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check smartphone support Bluetooth
        logger.info("Check smartphone support Bluetooth");
        if (mBluetoothAdapter == null) {
            //Device does not support Bluetooth
            logger.info("Device does not support Bluetooth");
            Toast.makeText(getApplicationContext(), "Not support bluetooth", Toast.LENGTH_LONG)
                    .show();
            finish();
        }

        // Check Bluetooth enabled
        logger.info("Check Bluetooth enabled");
        if (!mBluetoothAdapter.isEnabled()) {
            logger.info("Bluetooth is not enabled");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        logger.info("Bluetooth is enabled");

        // Querying paired devices
        logger.info("Querying paired devices");
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            logger.info("There are paired devices");
            for (BluetoothDevice device : pairedDevices) {
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                btDeviceArray.add(device);
                logger.info("Added device: " + device.getName());
            }
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        BluetoothDevice device = btDeviceArray.get(position);
        logger.info("Selected device: " + device.getName());

        connectAsyncTask = new ConnectAsyncTask(textViewDisplaySensor);
        connectAsyncTask.execute(device);

    }

}

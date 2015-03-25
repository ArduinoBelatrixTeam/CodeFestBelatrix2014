package com.codefest.rearsensordroid.bluetooth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Carlos Monzón on 11/15/2014.
 * Modified by Luis Silva on 12/20/2014 - Display decoupled to facilitate testing an extensibility
 */
public class ConnectedThread extends Thread {

    private final InputStream mmInStream;
    private final static int BUFFER_SIZE = 1024;
    private final static String CYCLE_SEPARATOR = "#";
    private final static String SENSOR_SEPARATOR = ",";

    private DisplaySensor displaySensor;

    final static Logger logger = LoggerFactory.getLogger(ConnectAsyncTask.class);

    public ConnectedThread(BluetoothSocket socket, DisplaySensor displaySensor) {
        InputStream tmpIn = null;
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            logger.debug("IOException when getting InputStream from socket: " + e.getMessage());
        }
        mmInStream = tmpIn;
        this.displaySensor = displaySensor;
    }

    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        int begin = 0;
        int bytes = 0;
        while (true) {
            try {
                logger.debug("buffer before read: " + (new String(buffer)) + " / bytes: " + bytes);
                bytes += mmInStream.read(buffer, bytes, buffer.length - bytes); //Adds bytes to the buffer array
                logger.debug("buffer after read: " + (new String(buffer)) + " / bytes: " + bytes);
                for (int i = begin; i < bytes; i++) { //Through the new added bytes
                    if (buffer[i] == CYCLE_SEPARATOR.getBytes()[0]) { //A complete read of sensors was found (cycle)
                        mHandler.obtainMessage(1, begin, i, buffer).sendToTarget(); //The cycle is sent to the Handler
                        //Check if there are no elements for a new cycle
                        if (i == bytes - 1) { //If there aren't more elements for a new cycle then restart buffer array
                            bytes = 0;
                            begin = 0;
                        } else{
                            begin = i + 1; //Set starting point for the next cycle
                        }
                    }
                }
                //If we reached the max buffer size value and is not a cycle separator the restart buffer array
                if((bytes == BUFFER_SIZE) && (buffer[BUFFER_SIZE-1] !=
                        CYCLE_SEPARATOR.getBytes()[0])){
                    bytes = 0;
                    begin = 0;
                }
            } catch (IOException e) {
                logger.debug("IOException when reading from socket: " + e.getMessage());
                break;
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
                    updateText(writeMessage);
                    break;
            }
        }
    };

    void updateText(String text) {
        text = text.replace("#", "").trim();
        if (text != "") {
            displaySensor.display(text);
        }
    }
}

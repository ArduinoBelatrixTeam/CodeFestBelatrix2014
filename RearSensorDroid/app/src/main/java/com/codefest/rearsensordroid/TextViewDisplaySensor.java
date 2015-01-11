package com.codefest.rearsensordroid;

import android.widget.TextView;

import com.codefest.rearsensordroid.bluetooth.DisplaySensor;

/**
 * Created by Luis Silva on 20/12/2014.
 */
public class TextViewDisplaySensor implements DisplaySensor {

    private TextView distance;
    private static final String DEFAULT_DISPLAY_UNIT = " cm";

    TextViewDisplaySensor(TextView distance){
        this.distance = distance;
    }

    @Override
    public void display(String value) {
        distance.setText(value + DEFAULT_DISPLAY_UNIT);
        if (Integer.valueOf(value) < 40) {
            distance.setTextSize(250);
        } else {
            distance.setTextSize(180);
        }
    }


}

package com.codefest.rearsensordroid;

import com.codefest.rearsensordroid.bluetooth.DisplaySensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.widget.TextView;

/**
 * Created by Luis Silva on 20/12/2014.
 */
public class TextViewDisplaySensor implements DisplaySensor {

    private TextView distance;

    final static Logger logger = LoggerFactory.getLogger(TextViewDisplaySensor.class);

    private static final String DEFAULT_DISPLAY_UNIT = " cm";

    TextViewDisplaySensor(TextView distance) {
        this.distance = distance;
    }

    @Override
    public void display(String value) {

        //to avoid NumberException when setting display size
        if (value == null || "".equals(value)) {
            distance.setText("-");
        } else {
            distance.setText(value + DEFAULT_DISPLAY_UNIT);
            logger.debug("Text displayed: " + value);
        }
    }

}

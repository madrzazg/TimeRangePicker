package com.dev.matt.timerangepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity implements com.dev.matt.rangetimepicker.TimeRangePicker.OnTimeRangeChangedListener {

    com.dev.matt.rangetimepicker.TimeRangePicker timeRangePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeRangePicker = (com.dev.matt.rangetimepicker.TimeRangePicker) findViewById(R.id.timeRangePicker);
        timeRangePicker.setOnTimeRangeChangedListener(this);
        timeRangePicker.setFromHour(13);
        timeRangePicker.setFromMinute(33);
        timeRangePicker.set24HourView(true);
        Log.d("TAG", "onCreate: ");
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, String range) {
        Log.d("TAG", "onTimeChanged: " + range + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
    }
}

package com.dev.matt.rangetimepicker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;


public class TimePickerFragment extends Fragment {
    private TimePicker timePicker;
    private TimePicker.OnTimeChangedListener onTimeChangedListener;
    private boolean is24HourView;
    private int hour;
    private int minute;

    public TimePickerFragment() {
        Log.d("TAG", "TimePickerFragment: ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_picker, container, false);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(onTimeChangedListener);
        // This is handy when this fragment is recreated and should update its view.
        // When orientation changes this wouldn't be necessary as everything is recreated.
        // But, by changing tabs on FragmentTabHost, only View is being recreated and initTimePicker
        // is not being called to update properties.
        // When that happens I needed to manually set hour, minute and is24HourView,
        // before onCreateView is called.
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setIs24HourView(is24HourView);
        Log.d("TAG", "onCreateView: TimePickerFragment");
        return view;
    }

    public TimePicker getTimePicker() {
        return timePicker;
    }

    public void setOnTimeChangedListener(TimePicker.OnTimeChangedListener onTimeChangedListener) {
        this.onTimeChangedListener = onTimeChangedListener;
    }

    public void setIs24HourView(boolean is24HourView) {
        this.is24HourView = is24HourView;
    }

    public boolean isIs24HourView() {
        return is24HourView;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }
}

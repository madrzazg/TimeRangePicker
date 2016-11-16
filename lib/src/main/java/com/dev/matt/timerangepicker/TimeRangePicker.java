package com.dev.matt.rangetimepicker;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by matt on 09.11.16.
 */

public class TimeRangePicker extends FrameLayout implements TabHost.OnTabChangeListener {
    private Context context;
    private FragmentTabHost fragmentTabHost;
    private OnTimeRangeChangedListener onTimeRangeChangedListener;

    private TimePickerFragment fromTimePickerFragment;
    private TimePickerFragment toTimePickerFragment;

    // Hour on 'FROM' tab
    private int fromHour;
    // Minute on 'FROM' tab
    private int fromMinute;
    // Hour on 'TO' tab
    private int toHour;
    // Minute on 'TO' tab
    private int toMinute;
    // If set to true, use current time on 'FROM' tab
    private boolean defaultFrom = true;
    // If set to true, use current time on 'TO' tab
    private boolean defaultTo = true;
    // Use 24 hour clock format
    private boolean is24HourView = false;




    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable("superState"));
            this.fromHour = bundle.getInt("fromHour");
            this.fromMinute = bundle.getInt("fromMinute");
            this.toHour = bundle.getInt("toHour");
            this.toMinute = bundle.getInt("toMinute");
            this.defaultFrom = bundle.getBoolean("defaultFrom");
            this.defaultTo = bundle.getBoolean("defaultTo");
            this.is24HourView = bundle.getBoolean("is24HourView");
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("fromHour", fromHour);
        bundle.putInt("fromMinute", fromMinute);
        bundle.putInt("toHour", toHour);
        bundle.putInt("toMinute", toMinute);
        bundle.putBoolean("defaultFrom", defaultFrom);
        bundle.putBoolean("defaultTo", defaultTo);
        bundle.putBoolean("is24HourView", is24HourView);

        return bundle;
    }

    private TimePicker.OnTimeChangedListener onTimeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker timePicker, int i, int i1) {
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();

            // Change time for 'FROM' clock
            if (fragmentTabHost.getCurrentTabTag() == "from") {
                fromHour = hour;
                fromMinute = minute;
            }
            // Change time for 'TO' clock
            else if (fragmentTabHost.getCurrentTabTag() == "to") {
                Log.d("TAG", "onTimeChanged: saving to");
                toHour = hour;
                toMinute = minute;
            }

            // Call onTimeChanged with timePicker and current tab tag
            if (onTimeRangeChangedListener != null) {
                onTimeRangeChangedListener.onTimeChanged(timePicker, fragmentTabHost.getCurrentTabTag());
            }
        }
    };


    public TimeRangePicker(Context context) {
        super(context);
        this.context = context;
        Log.d("TAG", "TimeRangePicker: ");
        initialize(context);
    }

    public TimeRangePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context);
    }

    private void initialize(final Context context) {
        inflate(context, R.layout.time_range_picker, this);
        fragmentTabHost = (FragmentTabHost) findViewById(R.id.fragmentTabHost);
        Log.d("TAG", "initialize: " + fragmentTabHost);
        fragmentTabHost.setup(context, ((AppCompatActivity) context).getSupportFragmentManager(), android.R.id.tabcontent);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("from").setIndicator("FROM"), TimePickerFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("to").setIndicator("TO"), TimePickerFragment.class, null);

        fragmentTabHost.setOnTabChangedListener(this);

        // View doesn't exist at this moment so wait 50ms and execute.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fromTimePickerFragment  = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag(fragmentTabHost.getCurrentTabTag());
                TimePicker timePicker = fromTimePickerFragment.getTimePicker();
                // Initialize timePicker
                initTimePicker(timePicker, fragmentTabHost.getCurrentTabTag());
                // Set listener for time changes
                timePicker.setOnTimeChangedListener(onTimeChangedListener);

                Log.d("TAG", "findFragmentByTag: " + fromTimePickerFragment);
            }
        }, 50);

    }

    public void setOnTimeRangeChangedListener(OnTimeRangeChangedListener onTimeRangeChangedListener) {
        this.onTimeRangeChangedListener = onTimeRangeChangedListener;
    }

    @Override
    public void onTabChanged(final String s) {
        // Find fragment by tag.
        final TimePickerFragment timePickerFragment = (TimePickerFragment) (((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag(s));
        Log.d("TAG", "onTabChanged immediate: " + s + " " + timePickerFragment);

        // If fragment was not found, means it hasn't created its view yet.
        // So we wait 50ms and initialize timePicker.
        if (timePickerFragment == null ) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TimePickerFragment timePickerFragment = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag(s);
                    Log.d("TAG", "onTabChanged delayed: " + s + " " + timePickerFragment);
                    timePickerFragment.getTimePicker().setOnTimeChangedListener(onTimeChangedListener);
                    initTimePicker(timePickerFragment.getTimePicker(), s);
                }
            }, 50);
        }
        else {
            // Fragment was found, but it must be recreated.
            // Fragment has not initialized its view yet. So I can't use getTimePicker().setOnTimeChangedListener().
            // Instead I pass listener via simple setter.
            // onTimeChangedListener will be later assigned to TimePicker's onTimeChangedListener in onCreateView().
            timePickerFragment.setOnTimeChangedListener(onTimeChangedListener);
            timePickerFragment.setIs24HourView(is24HourView);

            // Set clock time for 'FROM' tab
            if (fragmentTabHost.getCurrentTabTag() == "from") {
                timePickerFragment.setHour(fromHour);
                timePickerFragment.setMinute(fromMinute);
            }
            // Set clock time for 'TO' tab.
            else if (fragmentTabHost.getCurrentTabTag() == "to") {
                timePickerFragment.setHour(toHour);
                timePickerFragment.setMinute(toMinute);
            }
        }
    }

    // Initializes given timePicker. Tag is name of the tab, can be either 'FROM' or 'TO'
    public void initTimePicker(TimePicker timePicker, String tag) {
        timePicker.setIs24HourView(is24HourView);
        Log.d("TAG", "initTimePicker: ");

        if (tag.equals("from")) {
            // If user didn't set time in activity use current time.
            if (defaultFrom) {
                fromHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                fromMinute = Calendar.getInstance().get(Calendar.MINUTE);
            }
            timePicker.setCurrentHour(fromHour);
            timePicker.setCurrentMinute(fromMinute);
        }
        else {
            // If user didn't set time in activity use current time.
            if (defaultTo) {
                toHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                toMinute = Calendar.getInstance().get(Calendar.MINUTE);
                defaultTo = false;
            }

            timePicker.setCurrentHour(toHour);
            timePicker.setCurrentMinute(toMinute);
        }
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
        this.defaultFrom = false;

        // Update from time picker
        fromTimePickerFragment  = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("from");
        if (fromTimePickerFragment != null && fromTimePickerFragment.getTimePicker() != null) {
            fromTimePickerFragment.getTimePicker().setCurrentHour(fromHour);
        }
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromMinute(int fromMinute) {
        this.fromMinute = fromMinute;
        this.defaultFrom = false;

        // Update from time picker
        fromTimePickerFragment  = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("from");
        if (fromTimePickerFragment != null && fromTimePickerFragment.getTimePicker() != null) {
            fromTimePickerFragment.getTimePicker().setCurrentMinute(fromMinute);
        }
    }

    public int getFromMinute() {
        return fromMinute;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
        this.defaultTo = false;

        // Update to time picker
        toTimePickerFragment = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("to");
        if (toTimePickerFragment != null && toTimePickerFragment.getTimePicker() != null) {
            toTimePickerFragment.getTimePicker().setCurrentHour(toHour);
        }
    }

    public int getToMinute() {
        return toMinute;
    }

    public void setToMinute(int toMinute) {
        this.toMinute = toMinute;
        this.defaultTo = false;

        // Update to time picker
        toTimePickerFragment = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("to");
        if (toTimePickerFragment != null && toTimePickerFragment.getTimePicker() != null) {
            toTimePickerFragment.getTimePicker().setCurrentMinute(toMinute);
        }
    }

    public boolean is24HourView() {
        return is24HourView;
    }

    public void set24HourView(boolean is24HourView) {
        this.is24HourView = is24HourView;

        // Update from time picker
        fromTimePickerFragment = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("from");
        if (fromTimePickerFragment != null && fromTimePickerFragment.getTimePicker() != null) {
            fromTimePickerFragment.getTimePicker().setIs24HourView(is24HourView);
        }

        // Update to time picker
        toTimePickerFragment = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("to");
        if (toTimePickerFragment != null && toTimePickerFragment.getTimePicker() != null) {
            fromTimePickerFragment.getTimePicker().setIs24HourView(is24HourView);
        }
    }


    // Interface that must be implemented by an Activity or a Fragment using TimeRangePicker.
    // Without that you won't be notified when time changes.
    public interface OnTimeRangeChangedListener {
        void onTimeChanged(TimePicker timePicker, String range);
    }
}

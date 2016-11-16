[![](https://jitpack.io/v/madrzazg/RangeTimePicker.svg)](https://jitpack.io/#madrzazg/RangeTimePicker)

# RangeTimePicker
Non dialog Android Time Range Picker.

#### Features
* choose 24 or 12 hour format.
* set initial time.

![](https://raw.githubusercontent.com/madrzazg/RangeTimePicker/master/screenshots/Screenshot_2016-11-10-18-10-52.png)

![](https://raw.githubusercontent.com/madrzazg/RangeTimePicker/master/screenshots/Screenshot_2016-11-10-18-10-59.png)

![](https://raw.githubusercontent.com/madrzazg/RangeTimePicker/master/screenshots/Screenshot_2016-11-10-18-11-22.png)

![](https://raw.githubusercontent.com/madrzazg/RangeTimePicker/master/screenshots/Screenshot_2016-11-10-18-11-16.png)


### Usage

### XML
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.dev.matt.rangetimepicker.MainActivity">


    <com.dev.matt.rangetimepicker.TimeRangePicker
        android:id="@+id/timeRangePicker"
        android:layout_width="wrap_content"
        android:layout_centerInParent="true"
        android:layout_height="wrap_content">

    </com.dev.matt.rangetimepicker.TimeRangePicker>


</RelativeLayout>


```
#### Java

```java
public class MainActivity extends AppCompatActivity implements TimeRangePicker.OnTimeRangeChangedListener {

    TimeRangePicker timeRangePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeRangePicker = (TimeRangePicker) findViewById(R.id.timeRangePicker);
        timeRangePicker.setOnTimeRangeChangedListener(this);
        timeRangePicker.setFromHour(13);
        timeRangePicker.setFromMinute(33);
        timeRangePicker.set24HourView(true);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, String range) {
        Log.d("TAG", "onTimeChanged: " + range + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
    }
}

```

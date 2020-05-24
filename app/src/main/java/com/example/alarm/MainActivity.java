package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SensorEventListener, StepListener{
    //to make alaram manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    TextView update_text;
    PendingIntent pending_intent;
    int choose_whale_sound;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = " Steps";
    private int numSteps;
    TextView TvSteps;
    Button BtnStart;
    Button BtnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        //initialize alarm manager
        alarm_manager = (AlarmManager)  getSystemService(ALARM_SERVICE);

        //initialize timepicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        //initialize text update box
        update_text = (TextView) findViewById(R.id.update_text);

        //create an instance of a calrndar
        final Calendar calendar=Calendar.getInstance();


        //create an intent to the alarm receiver class
        final Intent my_intent = new Intent(this.context, AlarmReceiver.class);

        Spinner spinner= (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.whale_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //set an onclicklistener to the onItemSelected method
        spinner.setOnItemSelectedListener(this);

        //initialize on button
        Button alarm_on=(Button) findViewById(R.id.alarm_on);


        //create onclick listener to set alarm
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //setting calendar instance with the hour and minute that we have picked on the timepicker
                calendar.set(Calendar.HOUR_OF_DAY,alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE,alarm_timepicker.getMinute());

                //get int values of hour and minute
                int hour=alarm_timepicker.getHour();
                int minute=alarm_timepicker.getMinute();



                //convert int to string
                String hour_string=String.valueOf(hour);
                String minute_string=String.valueOf(minute);

                if(hour>12)
                {
                    hour_string= String.valueOf(hour-12);

                }
                if(minute<10)
                {
                    minute_string= "0"+String.valueOf(minute);
                }

                //method that changes update text textbox
                set_alarm_text("Alarm set to "+hour_string+" : "+minute_string);

                //put in extra string into my_intent
                //tells the clock you pressed alarm_on button
                my_intent.putExtra("extra","alarm on");

                //put in an extra long into my_intent
                //this tells the clock that you wants a certain value from dropdown value
                my_intent.putExtra("whale_choice",choose_whale_sound);

                //create a pending intent that delays the intent
                //until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(MainActivity.this,0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set the alarm manager
                alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);
            }
        });
        //initialize count button
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.count);
        BtnStop = (Button) findViewById(R.id.alarm_off);

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                TvSteps.setText("Start Walking!");
                        numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

            }
        });

        //initialize off button
        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        //create onclick listener to stop alarm
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numSteps>=10)
                {//unregister listener
                sensorManager.unregisterListener(MainActivity.this);
                //method that changes update text textbox
                set_alarm_text("Alarm off");

                TvSteps.setText("Good Morning!");
                //cancel the alarm
                alarm_manager.cancel(pending_intent);

                //put extra string and tells the clock
                // you pressed the alaram_off button
                my_intent.putExtra("extra","alarm off");

                //also put an extra long into the alarm off section
                //to prevent crashes in a Null Pointer Exceptions
                my_intent.putExtra("whale_choice",choose_whale_sound);

                //stop the ringtone
                sendBroadcast(my_intent);

            }}
        });


    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(numSteps + TEXT_NUM_STEPS );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        //outputting whatever id has selected
        //Toast
        //Toast.makeText(parent.getContext(),"the spinner item is: "+id, Toast.LENGTH_SHORT).show();;
        choose_whale_sound = (int) id;





    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }
}
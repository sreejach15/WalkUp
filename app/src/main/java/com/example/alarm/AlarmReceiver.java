package com.example.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    /* public Alarm_Receiver() {
          super();
      }
  */
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver","Yay!");

        //fetch the extra strings from the intent

        //tells the app whwther from the pressed the alarm_on or alarm_off
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("What is the key? ",get_your_string);

        //fetch the extra long from the intent
        //tells the app which value the user picked from the drop down spinner
        Integer get_your_whale_choice = intent.getExtras().getInt("whale_choice");

        Log.e("THe whale choice is :",get_your_whale_choice.toString());

        //create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //pass the extra string from the main activity to the rintoneplayingservics
        service_intent.putExtra("extra",get_your_string);

        //pass the extra integer from receiver to the ringtone playing service
        service_intent.putExtra("whale choice",get_your_whale_choice);
        //start the ringtone service
        context.startService(service_intent);
    }
}

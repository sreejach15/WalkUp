package com.example.alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.security.Provider;
import java.util.Random;

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        //fetch the extra string values
        String state = intent.getExtras().getString("extra");

        //fetch the extra string from the alarm om/aarm off values
        Integer whale_sound_choice = intent.getExtras().getInt("whale_choice");


        Log.e("Ringtone state : extra is ",state);
        Log.e("whale choice is" ,whale_sound_choice.toString());



        //this converts the extra strings from the intent to startId, value 0 or 1
        assert state != null;
        switch (state){
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }
        //if else statements

        //if there is no music playing, and the user pressed alarm_on
        //music should start playing
        if(!this.isRunning && startId == 1) {

            Log.e("there is no music, ", "and you start");


            this.isRunning = true;
            this.startId = 0;

            //put the notification here

            //play the whale sound depending on the whale choice id

            if (whale_sound_choice == 0) {
                //play random song
                int minimum_number = 1;
                int maximum_number = 3;
                Random random_number = new Random();
                int whale_number = random_number.nextInt(maximum_number + minimum_number);

                if (whale_number == 1) {
                    media_song = MediaPlayer.create(this, R.raw.sound1);
                    media_song.setLooping(true);
                    media_song.start();
                    Log.e("Playing :","sound 1");
                }
                else if (whale_number == 2) {
                    media_song = MediaPlayer.create(this, R.raw.sound2);
                    media_song.setLooping(true);
                    media_song.start();
                    Log.e("Playing :","sound 2");
                }
                else {
                    media_song = MediaPlayer.create(this, R.raw.sound3);
                    media_song.setLooping(true);
                    media_song.start();
                    Log.e("Playing :","sound 3");
                }

            }
            else if (whale_sound_choice == 1) {
                media_song = MediaPlayer.create(this, R.raw.sound1);
                media_song.setLooping(true);
                //start the ringtone
                media_song.start();

                Log.e("Playing :","sound 1");
            }
            else if (whale_sound_choice == 2) {
                media_song = MediaPlayer.create(this, R.raw.sound2);
                media_song.setLooping(true);
                //start the ringtone
                media_song.start();
                Log.e("Playing :","sound 2");
            }
            else {
                media_song = MediaPlayer.create(this, R.raw.sound3);
                media_song.setLooping(true);
                //start the ringtone
                media_song.start();
                Log.e("Playing :","sound 3");
            }
        }

        //if there is no music playing, and the user pressed alarm_off
        //music should stop playing
        else if(this.isRunning && startId == 0) {

            Log.e("there is no music, ","and you end");

            //stop the ringtone
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;

        }
        //these are it user presses random button
        //just to bug-proof the app
        //if there us no music playing, and the user pressed alarm_off , do  nothing
        else if(!this.isRunning && startId == 0) {
            Log.e("there is no music","and you want end");

            this.isRunning = false;
            this.startId = 0;
        }

        //if there is music playing and the user pressed alarm on ,do nothing
        else if(this.isRunning && startId == 1) {

            Log.e("there is no music, ","and you start");

            this.isRunning = true;
            this.startId = 1;

        }

        //cant think of anything else,just to catch the odd event
        else {
            Log.e("else ","somehow you reached this");

        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {


        // Tell the user we stopped.
        Log.e("on Destroy called","ta da");

        super.onDestroy();
        this.isRunning = false;
        Toast.makeText(this,"on Destroy called", Toast.LENGTH_SHORT).show();
    }

}
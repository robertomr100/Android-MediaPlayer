package edu.fsu.cs.mobile.hw4;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.io.IOException;

public class MyMediaService extends IntentService {
    public MyMediaService() {
        super("MyMediaService");
    }

    private MediaPlayer mp;
    public static final int NOTIFY_ID = 1;



    @Override protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        String msg=intent.getStringExtra("myaction");
        intent.setAction(MediaPlayerActivity.FILTER_ACTION_KEY);

        if(extras != null)
        {
            if(msg.equals("first"))
            {

               // Notify("FIRST");
                String test="http://searchgurbani.com/audio/sggs/1.mp3";
                String song = intent.getStringExtra("song");
                try {
                    mp = new MediaPlayer();
                    mp.setDataSource( test);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.prepare();
                    mp.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("message", "first"));

            }


            if(extras.getString("myaction").equals("start"))
            {
                //mp.start();
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("message", "start"));

            }


            if(extras.getString("myaction").equals("stop"))
            {
                // mp.stop();
            }



        }

    }




}

package edu.fsu.cs.mobile.hw4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import java.io.IOException;
/*
I only got the service to intialize the media player and start the song so for the submission I decided
not to use it and implement the mediaplayer on the activity, as it doesnt crashed when I try to stop the song.
I commented out the Service implementation that I had on the activity.
Additionally clicking on the notification causes an error but it does stop and end with the music.
The mp3 file that I used for testing is:
http://searchgurbani.com/audio/sggs/1.mp3


Roberto Mora

 */



public class MediaPlayerActivity extends AppCompatActivity implements MyDialogFragment.MyListener {
    private Button Play;
    private Button Pause;
    private Button Stop;
    private TextView textView;
    private MediaPlayer mp;
    MyReceiver myReceiver;
    private ImageView imageView;
    public static final String FILTER_ACTION_KEY = "any_key";
    public static int NOTIFY_ID = 1;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_media_player);

            textView=(TextView) findViewById(R.id.streamtext);
            Play=(Button) findViewById(R.id.PlayButton);
            Pause=(Button) findViewById(R.id.PauseButton);
            Stop=(Button) findViewById(R.id.StopButton);
            imageView=(ImageView) findViewById(R.id.imageView);
            Play.setVisibility(View.INVISIBLE);
            Pause.setVisibility(View.INVISIBLE);
            Stop.setVisibility(View.INVISIBLE);


        NotificationManager manager = this.getSystemService(NotificationManager.class);
        NotificationChannel mChannel = new NotificationChannel("my_channel1", "my_channell", NotificationManager.IMPORTANCE_HIGH);
        mChannel.setDescription("test channel");
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mChannel.setShowBadge(false);
        manager.createNotificationChannel(mChannel);

        final NotificationManager nm = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        final Notification.Builder builder = new Notification.Builder(getApplicationContext(),"my_channel1");
        builder.setContentTitle("Music Playing");
        builder.setContentText("Great Song--Uknown artist");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(contentIntent);




        Play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mp.start();
                    //Intent MyIntent = new Intent(getApplicationContext(),
                     //       MyMediaService.class);
                    //MyIntent.putExtra("myaction", "start");
                    //startService(MyIntent);
                    Play.setEnabled(false);
                    Stop.setEnabled(true);
                    Pause.setEnabled(true);

                    nm.notify(NOTIFY_ID, builder.build());

                }
            });

        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent MyIntent = new Intent(getApplicationContext(),
               //         MyMediaService.class);
               // MyIntent.putExtra("myaction", "stop");
               // startService(MyIntent);
                mp.pause();
                mp.seekTo(0);
                Stop.setEnabled(false);
                Play.setEnabled(true);
                Pause.setEnabled(false);
                nm.cancelAll();
            }
        });

        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent MyIntent = new Intent(getApplicationContext(),
                //       MyMediaService.class);
                //MyIntent.putExtra("myaction", "stop");
                //startService(MyIntent);
                mp.pause();
                Stop.setEnabled(true);
                Play.setEnabled(true);
                Pause.setEnabled(false);
                nm.cancelAll();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                MyDialogFragment  f = new MyDialogFragment();
                f.show(getSupportFragmentManager(),"DIALOG");
                return true;
            case R.id.item2:
                finish();
                System.exit(0);
                return true;
        }

            return true;
    }


    @Override
    public void SendSong(String song) {
       //"http://searchgurbani.com/audio/sggs/1.mp3"
        //THATS THE LINK I USED TO TEST
        try {
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource( song);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.setText("Great song--Uknown artist");
        imageView.setImageResource(R.drawable.ic_action_name);
        //Intent MyIntent = new Intent(getApplicationContext(),
        //        MyMediaService.class);
        //MyIntent.putExtra("myaction", "first");
        //MyIntent.putExtra("song",song);
        //startService(MyIntent);
        Play.setVisibility(View.VISIBLE);
        Pause.setVisibility(View.VISIBLE);
        Stop.setVisibility(View.VISIBLE);
    }

    private void setReceiver() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FILTER_ACTION_KEY);

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        setReceiver();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

        }
    }

}

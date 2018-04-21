package com.websitesinseattle.timernotification2;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button button;
    private Timer timer;
    private TimerTask timerTask;

    final Handler handler = new Handler();

    //notification id, unique for the app
    public static final int NOTIFICATION_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the references
        button = findViewById(R.id.button);

        //set the listeners
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //instanciate a new timer
        timer = new Timer();

        initializeTimerTask();

        //schedule the timer - after 5000ms delay, run it every 5000ms
        timer.schedule(timerTask, 5000, 5000);
    }

    //Send notification using the NotificationCompat API.
    //need to add android.support.v4.app to dependencies for it to work

    // @RequiresApi(api = Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(){

        //each notification must have its notification channel as of version 0

        //create a channel

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelIdOne = "com.websitesinseattle.timernotification2";
        CharSequence nameOne = "channel1";
        String descriptionOne = "because we have to have one";

        int importanceOne = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channelOne = new NotificationChannel(channelIdOne, nameOne, importanceOne);
        channelOne.setDescription(descriptionOne);
        channelOne.enableLights(true);
        channelOne.setLightColor(Color.GREEN);
        channelOne.enableVibration(false);
        mNotificationManager.createNotificationChannel(channelOne);

        //create an intent that will be fired when the user clicks the notification

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //Use NotificationCompat.Builder to set up our notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelIdOne);

        //set the icon that will appear in the notification bar
        builder.setSmallIcon(R.mipmap.ic_launcher);

        //set the intent that will fire when the user taps the notification
        builder.setContentIntent(pendingIntent);

        //set the notification to auto-cancel
        //notification will disappear after the user taps it
        builder.setAutoCancel(true);

        //build the notification's appearance
        builder.setContentTitle("Notification Example");
        builder.setContentText("Notifications are an important part of Android development. ");
        builder.setSubText("Tap to view online documentation ");

        //display the notification icon in the notification bar
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    public void initializeTimerTask(){

        timerTask = new TimerTask(){

            @Override
            public void run(){
                //use a handler to display a toast with the current timestamp
                handler.post(new Runnable(){

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run(){

                        //get current timeStamp
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());

                        //display the notification
                        sendNotification();
                    }
                });
            }
        };
    }
}

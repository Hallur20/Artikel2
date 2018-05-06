package com.example.hvn15.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button seconds_delay_btn;
    Button send_while_closed_btn;
    Button simple_send_notification_btn;
    CountDownTimer countDownTimer;
    TextView status_textview;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seconds_delay_btn = (Button) findViewById(R.id.after_5_seconds_button);
        send_while_closed_btn = (Button) findViewById(R.id.even_if_closed_button);
        simple_send_notification_btn = (Button) findViewById(R.id.send_now_button);

        status_textview = (TextView) findViewById(R.id.status_textview);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")); //this intent opens the browser and then google.com
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0); //specifies an action to take in the future.



        seconds_delay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer = new CountDownTimer(5000,1000) { //set countdown to max 5 seconds with a 1 second interval,
                    @Override                                                                 // that means that each tick happens every 1 second.
                    public void onTick(long millisUntilFinished) {
                        status_textview.setText("notication is being sent... (will take 5 seconds)"); //kind of lazy but each tick just set this waiting message (could use millisUntilFinished in the message)
                    }

                    @Override
                    public void onFinish() {
                        status_textview.setText("notification sent!"); //when 5 seconds have passed, send this 'done' message.

                    }
                };
                countDownTimer.start(); //start the count down.

                Handler handler = new Handler(); //schedules messages and runnables to be executed at some point in the future
                handler.postDelayed(new Runnable() { //this thread gets executed after 5 seconds
                    @Override
                    public void run() {
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1"); //helps constructing the typical notification layouts
                        mBuilder.setContentIntent(pendingIntent); //sets so that if user clicks on notification, the intent that goes to google.com is being started
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher); //sets an image as an icon for the notification
                        mBuilder.setContentTitle("Delayed notification"); //sets the string to be shown as a title for the notification
                        mBuilder.setContentText("Hello World after 5 seconds"); //sets the string to be shown as the context text for the notification
                        Notification notification = mBuilder.build(); //we are done and we parse the build to be of the type 'Notification'

                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //has some tools for notifications, in this case we are looking for 'notify'

                        nm.notify(2, notification); //notify sends the notification
                    }
                }, 5000); //sets the rule: start thread after 5 seconds.
            }
        });
        send_while_closed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotificationsWhileClosed();
            }
        });
        simple_send_notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "2"); //helps constructing the typical notification layouts
                builder.setContentIntent(pendingIntent); //sets so that if user clicks on notification, the intent that goes to google.com is being started
                builder.setContentTitle("Simple Notification"); //sets the string to be shown as a title for the notification
                builder.setContentText("Hello World!"); //sets the string to be shown as the context text for the notification
                builder.setSmallIcon(R.mipmap.ic_launcher); //sets an image as an icon for the notification
                Notification notification = builder.build(); //we are done and we parse the build to be of the type 'Notification'
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //has some tools for notifications, in this case we are looking for 'notify'
                nm.notify(3, notification); //notify sends the notification
            }
        });
    }
    public void sendNotificationsWhileClosed(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "3"); //helps constructing the typical notification layouts
        builder.setContentIntent(pendingIntent); //sets so that if user clicks on notification, the intent that goes to google.com is being started
        builder.setContentTitle("Notification When App Is Closed"); //sets the string to be shown as a title for the notification
        builder.setContentText("hello, your app is not open!"); //sets the string to be shown as the context text for the notification
        builder.setSmallIcon(R.mipmap.ic_launcher); //sets an image as an icon for the notification
        Notification notification = builder.build(); //we are done and we parse the build to be of the type 'Notification'

        Intent notificationIntent = new Intent(this, NotificationPublisher.class); //this intent uses the notificationspublisher class to notify on receive
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification); //send data from mainactivity with the intent, this case the notification
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT); //specifies an action to take in the future.

        long futureInMillis = 7000L; //set the rules here. Currently we want to wait 7 seconds until notification is being sent
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE); //get the alarm manager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent2);//wake it up after 7 seconds! then do pendingIntent2!
    }

}

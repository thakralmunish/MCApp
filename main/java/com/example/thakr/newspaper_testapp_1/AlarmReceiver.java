package com.example.thakr.newspaper_testapp_1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

    public static Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Scheduler Running", Toast.LENGTH_SHORT).show();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int Size = Vocabulary.items.size();
        Random rand = new Random();
        if (Size != 0) {
            int n = rand.nextInt(Size) + 0;
            String Word = Vocabulary.items.get(n);
            String Meaning = Vocabulary.meanings.get(Word);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(Word.toUpperCase())
                    .setContentText("Remember this Word?")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Meaning))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);


            Notification notification = mBuilder.build();

            createNotificationChannel();
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(100, notification);

        }



    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TEST_CHANNEL";//getString(R.string.channel_name);
            String description = "TEST_DESCRIPTION";//getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MainActivity.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = MainActivity.notificationManager;
            //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
            notificationManager.createNotificationChannel(channel);
        }
    }



}


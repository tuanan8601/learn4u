package com.example.questans.service;

import static com.example.questans.application.App.CHANNEL_ID;
import static com.example.questans.broadcastreceiver.ScheduleBroadcastReceiver.DAILY;
import static com.example.questans.broadcastreceiver.ScheduleBroadcastReceiver.NOTE;
import static com.example.questans.broadcastreceiver.ScheduleBroadcastReceiver.SUBJECT;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.questans.R;
import com.example.questans.activity.schedule.ScheduleActivity;

import java.io.IOException;

public class ScheduleService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    AudioManager mAudioManager;
    int originalVolume;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.printf("Running!");
        Intent notificationIntent = new Intent(this, ScheduleActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        String title = "Lịch trình";
        if(!intent.getBooleanExtra(DAILY,false)&&intent.getStringExtra(SUBJECT)!=null)
            title = intent.getStringExtra(SUBJECT);
        String notice = intent.getStringExtra(NOTE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notice))
                .setContentText(notice)
                .setOngoing(false)
                .setSmallIcon(R.drawable.learn4u_bg)
                .setContentIntent(pendingIntent)
                .build();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000};
        vibrator.vibrate(pattern, -1);

        startForeground(1, notification);
        new CountDownTimer(15 * 60 * 1000, 10000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                try {
                    stopSelf();
                } catch (Exception ex) {

                }
            }
        }.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
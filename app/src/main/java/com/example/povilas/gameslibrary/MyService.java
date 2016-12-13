package com.example.povilas.gameslibrary;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyService extends Service {
    
    private final String TAG = "povilas.gameslibrary";
    private Handler handler = new Handler();
    private Runnable runnable;
    private NotificationCompat.Builder notification;
    private static final int uniqueID = 165516;

    public MyService() {
    }

    public void notificationBuilder(){
        notification.setSmallIcon(R.drawable.cast_ic_notification_0);
        notification.setTicker("Ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Title");
        notification.setContentText("Content");

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: " + System.currentTimeMillis());
                notificationBuilder();
                handler.postDelayed(this, 5000);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        handler.removeCallbacks(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

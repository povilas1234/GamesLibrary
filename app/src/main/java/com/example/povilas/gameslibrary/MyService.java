package com.example.povilas.gameslibrary;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyService extends Service {
    private final String TAG = "povilas.gameslibrary";
    private Handler handler = new Handler();
    private Runnable runnable;
    private NotificationCompat.Builder notification;
    private static final int uniqueID = 165516;

    private SharedPreferences pref;

    public MyService() {}

    private void getLatestSubscribedNews(final String date_time){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://gameslibrary.000webhostapp.com/LatestNews.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean somethingNew = jsonResponse.getBoolean("new");

                            Log.i("News", somethingNew+"");
                            if(somethingNew) {
                                notification = new NotificationCompat.Builder(getBaseContext());
                                notification.setAutoCancel(true);
                                notificationBuilder();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("News: ", String.valueOf(error));
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("date",date_time);
                params.put("subscription", pref.getString("subscriptions",""));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void notificationBuilder(){
        notification.setSmallIcon(R.drawable.ic_local_atm_black_24dp);
        notification.setTicker("Ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("News on yours subscriptions!");
        notification.setContentText("Click to read news...");
        notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(contentIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        runnable = new Runnable() {
            @Override
            public void run() {
                long mili = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                Date resultDate = new Date(mili);
                Log.i(TAG, "Time: " + sdf.format(resultDate));
                getLatestSubscribedNews(sdf.format(resultDate));
                handler.postDelayed(this, 10000);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: removeCallbacks();");
        handler.removeCallbacks(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package com.hmtbasdas.bseuot.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hmtbasdas.bseuot.R;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static com.hmtbasdas.bseuot.Utilities.App.CHANNEL_ID;

public class BSEUNotificationService extends Service {

    private RequestQueue requestQueue;
    private Response.Listener<String> listener;
    private String URL_Private;

    private StringRequest stringRequest;

    private PreferenceManager preferenceManager;
    private Notification notification;
    private NotificationManagerCompat notificationManagerCompat;

    private Document document;
    private Elements myElements;

    private Intent intent;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("Service", "onCreate");

        preferenceManager = new PreferenceManager(this);

        if (preferenceManager.getBoolean(Constants.KEY_UNI_PRIVATE_STATUS) && !preferenceManager.getString(Constants.KEY_STUDENT_DEPARTMENT_LINK).isEmpty()) {
            Log.e("Service", "Service Active");
            Log.e("Service", preferenceManager.getString(Constants.KEY_STUDENT_DEPARTMENT_LINK));

            URL_Private = preferenceManager.getString(Constants.KEY_STUDENT_DEPARTMENT_LINK);

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_Private));
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            onStart();
        } else {
            Log.e("Service", "Service Passive. Stopping");
            stopService(new Intent(this, BSEUNotificationService.class));
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void onStart() {
        Log.e("Service", "onStart");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (preferenceManager.getBoolean(Constants.KEY_UNI_PRIVATE_STATUS)) {
                    Log.e("Service", "Service is running...");

                    requestQueue = Volley.newRequestQueue(getApplicationContext());

                    stringRequest = new StringRequest(Request.Method.GET, URL_Private, listener, null);
                    requestQueue.add(stringRequest);

                    listenerSettings();
                    try {
                        Thread.sleep(300000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void listenerSettings() {
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        };
    }

    private void parseResponse(String response) {
        document = Jsoup.parse(response);
        myElements = document.getElementsByClass("so-panel").select("td");

        checkValues(myElements);
    }

    private void checkValues(Elements myElements) {
        if (!myElements.get(0).text().equals(preferenceManager.getString(Constants.KEY_STUDENT_DEPARTMENT_LAST_VALUE))) {
            Log.e("Service", myElements.get(0).text());
            preferenceManager.putString(Constants.KEY_STUDENT_DEPARTMENT_LAST_VALUE, myElements.get(0).text());
            sendNotification(myElements.get(0).text());
        }
    }

    private void sendNotification(String text) {
        notification = new NotificationCompat.Builder(
                this, CHANNEL_ID)
                .setContentTitle("Yeni Duyuru / " + preferenceManager.getString(Constants.KEY_STUDENT_DEPARTMENT))
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_round_campaign_24)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManagerCompat = NotificationManagerCompat.from(this);

        notificationManagerCompat.notify(200, notification);
    }
}
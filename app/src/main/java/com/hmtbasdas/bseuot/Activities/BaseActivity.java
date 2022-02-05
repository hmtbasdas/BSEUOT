package com.hmtbasdas.bseuot.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*Intent intent = new Intent(this, CustomNotificationService.class);
        stopService(intent);

        ContextCompat.startForegroundService(this, intent);*/
        Log.d("TAG", "app destroyed");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("TAG", "app on backPressed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "app on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "app on Paused");
    }
}

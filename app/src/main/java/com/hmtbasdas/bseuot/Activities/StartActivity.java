package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.Intents;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityStartBinding;

import java.util.Calendar;
import java.util.Objects;

public class StartActivity extends BaseActivity {

    private ActivityStartBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        autoLogin();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        auth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding.textVia.setText("Hamit BAŞDAŞ © " + Calendar.getInstance().get(Calendar.YEAR));

        Sprite sprite = new FoldingCube();
        binding.progressItems.setIndeterminateDrawable(sprite);
    }

    private void autoLogin() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferenceManager.checkString(Constants.KEY_STUDENT_MAIL) && preferenceManager.checkString(Constants.KEY_STUDENT_PASS) &&
                        auth.getCurrentUser() != null &&
                        Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {

                    auth.signInWithEmailAndPassword(preferenceManager.getString(Constants.KEY_STUDENT_MAIL), preferenceManager.getString(Constants.KEY_STUDENT_PASS))
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intents.goActivity(StartActivity.this, new NewsActivity());
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Intents.goActivity(StartActivity.this, new LoginActivity());
                                    finish();

                                }
                            });
                } else {
                    Intents.goActivity(StartActivity.this, new LoginActivity());
                    finish();
                }
            }
        }, 1600);
    }
}
package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.Intents;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityAddConfessionBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;

public class AddConfessionActivity extends AppCompatActivity {

    private ActivityAddConfessionBinding binding;

    private FirebaseFirestore database;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddConfessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init(){
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intents.goActivity(getApplicationContext(), new ConfessionActivity());
                finish();
            }
        });

        binding.sendConfessionContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.confessionContent.getText().toString().trim().isEmpty()){

                    binding.sendConfessionContent.setVisibility(View.INVISIBLE);
                    binding.progressItems.setVisibility(View.VISIBLE);

                    String time = String.valueOf(System.currentTimeMillis());

                    HashMap<String, Object> confessionMap = new HashMap<>();
                    confessionMap.put(Constants.KEY_CONFESSION_ID, time);
                    confessionMap.put(Constants.KEY_CONFESSION_UserID, preferenceManager.getString(Constants.KEY_STUDENT_ID));
                    confessionMap.put(Constants.KEY_CONFESSION_CONTENT, binding.confessionContent.getText().toString().trim());
                    confessionMap.put(Constants.KEY_CONFESSION_DATE, (new Date()).toString());
                    confessionMap.put(Constants.KEY_CONFESSION_STATUS, true);

                    database.collection(Constants.KEY_CONFESSION_COLLECTIONS)
                            .document(time)
                            .set(confessionMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Gönderildi", Toast.LENGTH_SHORT).show();
                                        Intents.goActivity(getApplicationContext(), new ConfessionActivity());
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intents.goActivity(getApplicationContext(), new ConfessionActivity());
        finish();
    }
}
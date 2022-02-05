package com.hmtbasdas.bseuot.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityContactBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ContactActivity extends BaseActivity {

    private ActivityContactBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        setValues();
    }

    private void init(){
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.sendMailContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.contentTo.getText().toString().trim().isEmpty() && !binding.mailTo.getText().toString().trim().isEmpty()){
                    binding.sendMailContent.setVisibility(View.INVISIBLE);
                    binding.progressItems.setVisibility(View.VISIBLE);

                    String time = String.valueOf(System.currentTimeMillis());

                    HashMap<String, Object> contactMap = new HashMap<>();
                    contactMap.put(Constants.KEY_STUDENT_MAIL, binding.mailTo.getText().toString().trim());
                    contactMap.put(Constants.KEY_STUDENT_ID, preferenceManager.getString(Constants.KEY_STUDENT_ID));
                    contactMap.put("Ad Soyad", preferenceManager.getString(Constants.KEY_STUDENT_NAME)+" " + preferenceManager.getString(Constants.KEY_STUDENT_SURNAME));
                    contactMap.put("Content", binding.contentTo.getText().toString().trim());

                    database.collection(getIntent().getStringExtra("ActivityType").toLowerCase().replaceAll("\\s+",""))
                            .document(time)
                            .set(contactMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    binding.progressItems.setVisibility(View.INVISIBLE);
                                    binding.sendMailContent.setVisibility(View.VISIBLE);

                                    Toast.makeText(getApplicationContext(), "Gönderildi", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Neden boş ki ?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setValues(){
        binding.pageTitle.setText(getIntent().getStringExtra("ActivityType"));
        if(getIntent().getStringExtra("ActivityType").trim().equals("Öneride Bulun")){
            binding.info.setText("Merhaba " + preferenceManager.getString(Constants.KEY_STUDENT_NAME) + "\nBen Hamit daha iyi bir üniversite deneyimi için bu uygulamayı geliştiriyorum. Birlikte geliştirmeye ne dersin ?\nÖnerilerini bekliyorum...");
        }
        else if(getIntent().getStringExtra("ActivityType").trim().equals("Hata Bildir")){
            binding.info.setText("Oops! Selam " + preferenceManager.getString(Constants.KEY_STUDENT_NAME) + "\nHata aldığın için üzgünüm :/ bana aldığın hatanın detaylarını anlatabilir misin ?");
        }
        else {
            binding.info.setText("Aman aman kimi görüyorum :)\nHoş geldin " + preferenceManager.getString(Constants.KEY_STUDENT_NAME) + "\nBana buradan ulaşabilirsin... <3");
        }
    }
}
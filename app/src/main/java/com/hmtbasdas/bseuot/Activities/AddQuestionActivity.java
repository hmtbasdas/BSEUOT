package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.Intents;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityAddQuestionBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;

public class AddQuestionActivity extends BaseActivity {

    private ActivityAddQuestionBinding binding;

    private FirebaseFirestore database;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQuestionBinding.inflate(getLayoutInflater());
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
                Intents.goActivity(getApplicationContext(), new QuestionsActivity());
                finish();
            }
        });

        binding.sendQuestionContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.questionTEXT.getText().toString().isEmpty() && !binding.questionTITLE.getText().toString().isEmpty()){
                    sendQuestion(binding.questionTITLE.getText().toString(), binding.questionTEXT.getText().toString());
                }
            }
        });
    }

    private void sendQuestion(String title, String text){
        binding.sendQuestionContent.setVisibility(View.INVISIBLE);
        binding.progressItems.setVisibility(View.VISIBLE);

        String time = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> questionMap = new HashMap<>();
        questionMap.put(Constants.KEY_QUESTION_ID, time);
        questionMap.put(Constants.KEY_QUESTION_TITLE, title);
        questionMap.put(Constants.KEY_QUESTION_TEXT, text);
        questionMap.put(Constants.KEY_QUESTION_DATE, (new Date()).toString());
        questionMap.put(Constants.KEY_QUESTION_UserID, preferenceManager.getString(Constants.KEY_STUDENT_ID));
        questionMap.put(Constants.KEY_QUESTION_STATUS, true);

        database.collection(Constants.KEY_QUESTION_COLLECTIONS)
                .document(time)
                .set(questionMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){

                            /*binding.questionTITLE.getText().clear();
                            binding.questionTEXT.getText().clear();

                            binding.progressItems.setVisibility(View.INVISIBLE);
                            binding.sendQuestionContent.setVisibility(View.VISIBLE);*/

                            Toast.makeText(getApplicationContext(), "Gönderildi", Toast.LENGTH_SHORT).show();
                            Intents.goActivity(getApplicationContext(), new QuestionsActivity());
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intents.goActivity(getApplicationContext(), new QuestionsActivity());
        finish();
    }
}
package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hmtbasdas.bseuot.Adapters.QuestionAdapter;
import com.hmtbasdas.bseuot.Alerts.AlertReportQuestion;
import com.hmtbasdas.bseuot.Listeners.QuestionListener;
import com.hmtbasdas.bseuot.Models.Question;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.Intents;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityQuestionsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class QuestionsActivity extends BaseActivity implements QuestionListener {

    private ActivityQuestionsBinding binding;

    private FirebaseFirestore database;

    private ArrayList<Question> questionArrayList;
    private QuestionAdapter questionAdapter;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        getQuestions();
    }

    private void init(){
        database = FirebaseFirestore.getInstance();

        preferenceManager = new PreferenceManager(getApplicationContext());

        questionArrayList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionArrayList, this);
        binding.questionsList.setAdapter(questionAdapter);
        binding.questionsList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intents.goActivity(getApplicationContext(), new AddQuestionActivity());
                finish();
            }
        });
    }

    private void getQuestions(){
        binding.progressItems.setVisibility(View.VISIBLE);
        binding.questionsList.setVisibility(View.INVISIBLE);
        database.collection(Constants.KEY_QUESTION_COLLECTIONS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(!questionArrayList.isEmpty()){
                                questionArrayList.clear();
                            }

                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                Question question = new Question(
                                        documentSnapshot.getString(Constants.KEY_QUESTION_ID),
                                        documentSnapshot.getString(Constants.KEY_QUESTION_TITLE),
                                        documentSnapshot.getString(Constants.KEY_QUESTION_TEXT),
                                        documentSnapshot.getString(Constants.KEY_QUESTION_DATE),
                                        documentSnapshot.getString(Constants.KEY_QUESTION_UserID),
                                        documentSnapshot.getBoolean(Constants.KEY_QUESTION_STATUS)
                                );
                                if(question.getQuestionSTATUS()){
                                    questionArrayList.add(question);
                                }
                            }
                            Collections.reverse(questionArrayList);

                            questionAdapter.notifyDataSetChanged();
                            binding.progressItems.setVisibility(View.INVISIBLE);
                            binding.questionsList.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onQuestionClicked(Question question) {
        Intent intent = new Intent(getApplicationContext(), QuestionDetailActivity.class);
        intent.putExtra(Constants.KEY_QUESTION_COLLECTIONS, question);
        startActivity(intent);
    }

    @Override
    public void onReportQuestionClicked(Question question) {
        AlertReportQuestion alertReportQuestion = new AlertReportQuestion(this, database, preferenceManager);
        alertReportQuestion.reportQuestion(question).show();
    }
}
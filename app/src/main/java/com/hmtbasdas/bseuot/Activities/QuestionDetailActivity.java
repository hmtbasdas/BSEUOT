package com.hmtbasdas.bseuot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hmtbasdas.bseuot.Models.Question;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.databinding.ActivityQuestionDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuestionDetailActivity extends BaseActivity {

    private ActivityQuestionDetailBinding binding;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        setQuestionDetails();
    }

    private void init(){
        question = (Question) getIntent().getSerializableExtra(Constants.KEY_QUESTION_COLLECTIONS);
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.doComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentListActivity.class);
                intent.putExtra(Constants.KEY_INTENT_COLLECTION, Constants.KEY_QUESTION_COLLECTIONS);
                intent.putExtra(Constants.KEY_INTENT_ID, question.getQuestionID());
                startActivity(intent);
            }
        });
    }

    private void setQuestionDetails(){
        binding.questionTITLE.setText(question.getQuestionTITLE());
        binding.questionTEXT.setText(question.getQuestionTEXT());
        binding.questionDATE.setText(getReadableDateTime(new Date(question.getQuestionDATE())));
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("dd/MM/yyyy, EEEE", Locale.getDefault()).format(date);
    }
}
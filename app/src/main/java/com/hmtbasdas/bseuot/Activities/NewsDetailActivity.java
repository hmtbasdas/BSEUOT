package com.hmtbasdas.bseuot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hmtbasdas.bseuot.Models.News;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.databinding.ActivityNewsDetailBinding;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends BaseActivity {

    private ActivityNewsDetailBinding binding;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        setNewsDetails();
    }

    private void init() {
        news = (News) getIntent().getSerializableExtra(Constants.KEY_NEWS_COLLECTIONS);
    }

    private void setListeners() {
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
                intent.putExtra(Constants.KEY_INTENT_COLLECTION, Constants.KEY_NEWS_COLLECTIONS);
                intent.putExtra(Constants.KEY_INTENT_ID, news.getNewsID());
                startActivity(intent);
            }
        });
    }

    private void setNewsDetails() {
        binding.newsTitle.setText(news.getNewsTITLE());
        Picasso.get().load(news.getNewsIMAGE()).into(binding.newsImage);
        binding.newsDescription.setText(news.getNewsDESC());
    }
}
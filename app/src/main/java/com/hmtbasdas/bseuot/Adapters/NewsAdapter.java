package com.hmtbasdas.bseuot.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hmtbasdas.bseuot.Listeners.UserListener;
import com.hmtbasdas.bseuot.Models.News;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.databinding.NewsItemBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private ArrayList<News> news;
    private final UserListener userListener;
    private FirebaseFirestore database;

    private int count = 0;

    public NewsAdapter(ArrayList<News> news, UserListener userListener, FirebaseFirestore database) {
        this.news = news;
        this.userListener = userListener;
        this.database = database;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        NewsItemBinding binding = NewsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.setData(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        NewsItemBinding binding;

        public MyViewHolder(@NonNull @NotNull NewsItemBinding itemBinding) {
            super(itemBinding.getRoot());

            binding = itemBinding;
        }

        void setData(News news){
            binding.textTitle.setText(news.getNewsTITLE());
            binding.textDesc.setText(news.getNewsDESC());
            Picasso.get().load(news.getNewsIMAGE()).into(binding.newsImage);
            getCommentNumber(news);
            binding.newsDate.setText(getReadableDateTime(new Date(news.getNewsDATE())));
            binding.newsEye.setText(String.valueOf(news.getNewsEYE()));
            binding.getRoot().setOnClickListener(v -> userListener.onNewsClicked(news));
        }

        private String getReadableDateTime(Date date){
            return new SimpleDateFormat("dd/MM/yyyy, EEEE", Locale.getDefault()).format(date);
        }

        private void getCommentNumber(News news){
            database.collection(Constants.KEY_COMMENT_COLLECTIONS)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                    if(news.getNewsID().equals(documentSnapshot.getString(Constants.KEY_COMMENT_ObjectID))
                                    && Constants.KEY_NEWS_COLLECTIONS.equals(documentSnapshot.getString(Constants.KEY_COMMENT_TYPE))){
                                        count++;
                                    }
                                }
                                binding.newsComment.setText(String.valueOf(count));
                                count = 0;
                            }
                        }
                    });
        }
    }

}

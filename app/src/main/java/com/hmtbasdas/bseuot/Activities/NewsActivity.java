package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hmtbasdas.bseuot.Adapters.NewsAdapter;
import com.hmtbasdas.bseuot.Listeners.UserListener;
import com.hmtbasdas.bseuot.Models.News;
import com.hmtbasdas.bseuot.R;

import com.hmtbasdas.bseuot.Services.BSEUNotificationService;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.Intents;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityNewsBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class NewsActivity extends BaseActivity implements UserListener {

    private ActivityNewsBinding binding;

    private PreferenceManager preferenceManager;

    private FirebaseFirestore database;
    private FirebaseAuth auth;

    private ArrayList<News> newsArrayList;
    private NewsAdapter newsAdapter;

    private Intent intent;

    private int eye = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(!foregroundServiceRunning()){
            intent = new Intent(this, BSEUNotificationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startService(intent);
            }
            else{
                startService(new Intent(this, BSEUNotificationService.class));
            }
        }

        init();
        setListeners();
        setUserValues();
        getNews();
    }

    private void init(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        preferenceManager = new PreferenceManager(getApplicationContext());

        Sprite sprite = new FoldingCube();
        binding.progressItems.setIndeterminateDrawable(sprite);

        binding.refreshLayout.setProgressBackgroundColorSchemeResource(R.color.background_color);
        binding.refreshLayout.setColorSchemeColors(Color.WHITE);

        newsArrayList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsArrayList, this, database);
        binding.listItems.setAdapter(newsAdapter);
        binding.listItems.setLayoutManager(new LinearLayoutManager(this));

        binding.navigationMenu.setItemIconTintList(null);
        binding.navigationUserMenu.setItemIconTintList(null);
    }

    private void setListeners(){
        binding.studentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.END);
                ImageView imageView = binding.navigationUserMenu.findViewById(R.id.studentIMAGE);
                TextView nameText = binding.navigationUserMenu.findViewById(R.id.name);
                TextView surnameText = binding.navigationUserMenu.findViewById(R.id.surname);

                nameText.setText(preferenceManager.getString(Constants.KEY_STUDENT_NAME));
                surnameText.setText(preferenceManager.getString(Constants.KEY_STUDENT_SURNAME));

                Picasso.get().load(preferenceManager.getString(Constants.KEY_STUDENT_IMAGE)).into(imageView);

                binding.navigationUserMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                        if(item.getTitle().equals("Profilimi Düzenle")){
                            Intents.goActivity(NewsActivity.this, new EditProfileActivity());
                            finish();
                        }
                        else if(item.getTitle().equals("Şifremi Değiştir")){
                            Intents.goActivity(NewsActivity.this, new ChangePasswordActivity());
                        }
                        else {
                            //stopService(intent);
                            preferenceManager.putBoolean(Constants.KEY_UNI_PRIVATE_STATUS, false);
                            auth.signOut();
                            preferenceManager.clear();
                            Intents.goActivity(getApplicationContext(), new LoginActivity());
                            finish();
                        }
                        return true;
                    }
                });
            }
        });

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews();
            }
        });

        binding.navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
                binding.navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                        Intent intent = new Intent(NewsActivity.this, Constants.KEY_ACTIVITIES[Integer.parseInt(item.getTitleCondensed().toString())].getClass());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("ActivityType",item.getTitle());
                        startActivity(intent);
                        return true;
                    }
                });
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<News> newNews = new ArrayList<>();
                for(News news:newsArrayList){
                    if(news.getNewsTITLE().toLowerCase().contains(newText.toLowerCase())){
                        newNews.add(news);
                    }
                }
                newsAdapter.setNews(newNews);
                newsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private Boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo: activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(BSEUNotificationService.class.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private void getNews(){
        binding.progressItems.setVisibility(View.VISIBLE);
        database.collection(Constants.KEY_NEWS_COLLECTIONS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(!newsArrayList.isEmpty()){
                                newsArrayList.clear();
                            }

                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                News news = new News(
                                        documentSnapshot.getString(Constants.KEY_NEWS_ID),
                                        Integer.parseInt(String.valueOf(documentSnapshot.getLong(Constants.KEY_NEWS_EYE))),
                                        documentSnapshot.getString(Constants.KEY_NEWS_TITLE),
                                        documentSnapshot.getString(Constants.KEY_NEWS_DESC),
                                        documentSnapshot.getString(Constants.KEY_NEWS_DATE),
                                        documentSnapshot.getString(Constants.KEY_NEWS_IMAGE),
                                        documentSnapshot.getBoolean(Constants.KEY_NEWS_STATUS)
                                );

                                if(news.getNewsSTATUS()){
                                    newsArrayList.add(news);
                                }
                            }
                            Collections.reverse(newsArrayList);

                            newsAdapter.notifyDataSetChanged();
                            binding.progressItems.setVisibility(View.INVISIBLE);
                            binding.listItems.setVisibility(View.VISIBLE);
                            binding.refreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void setUserValues(){
        binding.nameSurname.setText("Hoş geldin\n" + preferenceManager.getString(Constants.KEY_STUDENT_NAME) + " " + preferenceManager.getString(Constants.KEY_STUDENT_SURNAME));
        Picasso.get().load(preferenceManager.getString(Constants.KEY_STUDENT_IMAGE)).into(binding.studentImage);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onNewsClicked(News news) {
        binding.listItems.setVisibility(View.INVISIBLE);
        binding.progressItems.setVisibility(View.VISIBLE);

        database.collection(Constants.KEY_NEWS_COLLECTIONS)
                .whereEqualTo(Constants.KEY_NEWS_ID, news.getNewsID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            eye = Integer.parseInt(String.valueOf(documentSnapshot.getLong(Constants.KEY_NEWS_EYE)));

                            DocumentReference documentReference = database.collection(Constants.KEY_NEWS_COLLECTIONS).document(news.getNewsID());
                            documentReference.update(Constants.KEY_NEWS_EYE, ++eye)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Intent intent = new Intent(getApplicationContext(), NewsDetailActivity.class);
                                                intent.putExtra(Constants.KEY_NEWS_COLLECTIONS, news);
                                                startActivity(intent);

                                                binding.progressItems.setVisibility(View.INVISIBLE);
                                                binding.listItems.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });

                        }
                    }
                });
    }
}
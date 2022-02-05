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
import com.hmtbasdas.bseuot.Adapters.SchoolClubAdapter;
import com.hmtbasdas.bseuot.Listeners.SchoolClubListener;
import com.hmtbasdas.bseuot.Models.SchoolClub;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.databinding.ActivitySchoolClubsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class SchoolClubsActivity extends BaseActivity implements SchoolClubListener {

    private ActivitySchoolClubsBinding binding;

    private FirebaseFirestore database;

    private ArrayList<SchoolClub> schoolClubArrayList;
    private SchoolClubAdapter schoolClubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchoolClubsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        getSchoolClubs();
    }

    private void init(){
        database = FirebaseFirestore.getInstance();

        schoolClubArrayList = new ArrayList<>();
        schoolClubAdapter = new SchoolClubAdapter(schoolClubArrayList, this, database);
        binding.clubsList.setAdapter(schoolClubAdapter);
        binding.clubsList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getSchoolClubs(){
        binding.progressItems.setVisibility(View.VISIBLE);
        database.collection(Constants.KEY_SCHOOL_CLUB_COLLECTIONS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                SchoolClub schoolClub = new SchoolClub(
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_ID),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_WhatsappADDRESS),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_InstagramADDRESS),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_FacebookADDRESS),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_TwitterADDRESS),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_IMAGE),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_DATE),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_TITLE),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_CLUB_DESC),
                                        documentSnapshot.getBoolean(Constants.KEY_SCHOOL_CLUB_STATUS)
                                );
                                if(schoolClub.getClubSTATUS()){
                                    schoolClubArrayList.add(schoolClub);
                                }
                            }
                            schoolClubAdapter.notifyDataSetChanged();
                            binding.progressItems.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onSchoolClubClicked(SchoolClub schoolClub) {
        Intent intent = new Intent(getApplicationContext(), SchoolClubDetailActivity.class);
        intent.putExtra(Constants.KEY_SCHOOL_CLUB_COLLECTIONS, schoolClub);
        startActivity(intent);
    }
}
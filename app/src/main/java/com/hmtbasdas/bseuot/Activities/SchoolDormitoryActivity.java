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
import com.hmtbasdas.bseuot.Adapters.SchoolDormitoryAdapter;
import com.hmtbasdas.bseuot.Listeners.SchoolDormitoryListener;
import com.hmtbasdas.bseuot.Models.SchoolDormitory;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.databinding.ActivitySchoolDormitoryBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class SchoolDormitoryActivity extends BaseActivity implements SchoolDormitoryListener {

    private ActivitySchoolDormitoryBinding binding;

    private FirebaseFirestore database;

    private ArrayList<SchoolDormitory> schoolDormitories;
    private SchoolDormitoryAdapter schoolDormitoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchoolDormitoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        getDormitoryValues();
    }

    private void init(){
        database = FirebaseFirestore.getInstance();

        schoolDormitories = new ArrayList<>();
        schoolDormitoryAdapter = new SchoolDormitoryAdapter(schoolDormitories, this, database);
        binding.dormitoryList.setAdapter(schoolDormitoryAdapter);
        binding.dormitoryList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDormitoryValues(){
        binding.progressItems.setVisibility(View.VISIBLE);
        database.collection(Constants.KEY_SCHOOL_DORMITORY_COLLECTIONS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                SchoolDormitory schoolDormitory = new SchoolDormitory(
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_ID),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_TITLE),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_ADDRESS),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_TYPE),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_BED_NUMBERS),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_PRICE),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_MEAL),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_WEB_ADDRESS),
                                        documentSnapshot.getString(Constants.KEY_SCHOOL_DORMITORY_TELEPHONE_NUMBER),
                                        documentSnapshot.getBoolean(Constants.KEY_SCHOOL_DORMITORY_WIFI),
                                        documentSnapshot.getBoolean(Constants.KEY_SCHOOL_DORMITORY_STATUS)
                                );
                                if(schoolDormitory.getDormitorySTATUS()){
                                    schoolDormitories.add(schoolDormitory);
                                }
                            }
                            schoolDormitoryAdapter.notifyDataSetChanged();
                            binding.progressItems.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onSchoolDormitoryClicked(SchoolDormitory dormitory) {
        Intent intent = new Intent(getApplicationContext(), SchoolDormitoryDetailActivity.class);
        intent.putExtra(Constants.KEY_SCHOOL_DORMITORY_COLLECTIONS, dormitory);
        startActivity(intent);
    }
}
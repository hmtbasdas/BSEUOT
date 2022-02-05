package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hmtbasdas.bseuot.Models.SchoolDormitory;
import com.hmtbasdas.bseuot.Models.Vote;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivitySchoolDormitoryDetailBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class SchoolDormitoryDetailActivity extends BaseActivity {

    private ActivitySchoolDormitoryDetailBinding binding;
    private SchoolDormitory schoolDormitory;

    private PreferenceManager preferenceManager;

    private FirebaseFirestore database;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchoolDormitoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        setDormitoryValues();
    }

    private void init(){
        schoolDormitory = (SchoolDormitory) getIntent().getSerializableExtra(Constants.KEY_SCHOOL_DORMITORY_COLLECTIONS);

        database = FirebaseFirestore.getInstance();

        preferenceManager = new PreferenceManager(getApplicationContext());
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.likeDormitory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickVotesButton(true);
            }
        });

        binding.dislikeDormitory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickVotesButton(false);
            }
        });

        binding.doComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentListActivity.class);
                intent.putExtra(Constants.KEY_INTENT_COLLECTION, Constants.KEY_SCHOOL_DORMITORY_COLLECTIONS);
                intent.putExtra(Constants.KEY_INTENT_ID, schoolDormitory.getDormitoryID());
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDormitoryValues(){
        binding.dormitoryTitle.setText(schoolDormitory.getDormitoryTITLE());
        binding.bedNumber.setText(schoolDormitory.getDormitoryBedNUMBERS() + " Yatak");
        binding.mealNumber.setText(schoolDormitory.getDormitoryMeal() + " Öğün");
        if(schoolDormitory.getDormitoryWIFI()){
            binding.wifiCheck.setText("Var");
        }
        else {
            binding.wifiCheck.setText("Yok");
        }
        binding.address.setText(schoolDormitory.getDormitoryADDRESS());
        binding.webAddress.setText(schoolDormitory.getDormitoryWebADDRESS());
        binding.price.setText(schoolDormitory.getDormitoryPRICE() + " TL");
    }

    private void clickVotesButton(Boolean voteSTATUS) {
        String time = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> voteMap = new HashMap<>();
        voteMap.put(Constants.KEY_VOTE_ID, time);
        voteMap.put(Constants.KEY_VOTE_Object_ID, schoolDormitory.getDormitoryID());
        voteMap.put(Constants.KEY_VOTE_TYPE, Constants.KEY_SCHOOL_DORMITORY_COLLECTIONS);
        voteMap.put(Constants.KEY_VOTE_UserID, preferenceManager.getString(Constants.KEY_STUDENT_ID));
        voteMap.put(Constants.KEY_VOTE_STATUS, voteSTATUS);

        database.collection(Constants.KEY_VOTE_COLLECTIONS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                Vote vote = new Vote(
                                        documentSnapshot.getString(Constants.KEY_VOTE_ID),
                                        documentSnapshot.getString(Constants.KEY_VOTE_Object_ID),
                                        documentSnapshot.getString(Constants.KEY_VOTE_TYPE),
                                        documentSnapshot.getString(Constants.KEY_VOTE_UserID),
                                        documentSnapshot.getBoolean(Constants.KEY_VOTE_STATUS));

                                if (vote.getVoteUserID().equals(preferenceManager.getString(Constants.KEY_STUDENT_ID))
                                        && vote.getVoteTYPE().equals(Constants.KEY_SCHOOL_DORMITORY_COLLECTIONS)
                                        && vote.getVoteObjectID().equals(schoolDormitory.getDormitoryID())) {
                                    count++;
                                }
                            }
                            if (count > 0) {
                                Toast.makeText(getApplicationContext(), "Daha Önce Oy Kullandınız", Toast.LENGTH_SHORT).show();
                            } else {
                                database.collection(Constants.KEY_VOTE_COLLECTIONS)
                                        .document(time)
                                        .set(voteMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                                                    count = 0;
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }
}
package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hmtbasdas.bseuot.Models.SchoolClub;
import com.hmtbasdas.bseuot.Models.Vote;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivitySchoolClubDetailBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class SchoolClubDetailActivity extends BaseActivity {

    private ActivitySchoolClubDetailBinding binding;
    private SchoolClub schoolClub;

    private FirebaseFirestore database;

    private PreferenceManager preferenceManager;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchoolClubDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        setClubConnections();
        setClubValues();
    }

    private void init(){
        schoolClub = (SchoolClub) getIntent().getSerializableExtra(Constants.KEY_SCHOOL_CLUB_COLLECTIONS);

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

        binding.likeClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickVotesButton(true);
            }
        });

        binding.dislikeClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickVotesButton(false);
            }
        });

        binding.whatsappLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(schoolClub.getClubWhatsappADDRESS()));
                startActivity(i);
            }
        });

        binding.twitterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(schoolClub.getClubTwitterADDRESS()));
                startActivity(i);
            }
        });

        binding.facebookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(schoolClub.getClubFacebookADDRESS()));
                startActivity(i);
            }
        });

        binding.instagramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(schoolClub.getClubInstagramADDRESS()));
                startActivity(i);
            }
        });

        binding.doComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentListActivity.class);
                intent.putExtra(Constants.KEY_INTENT_COLLECTION, Constants.KEY_SCHOOL_CLUB_COLLECTIONS);
                intent.putExtra(Constants.KEY_INTENT_ID, schoolClub.getClubID());
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setClubValues(){
        binding.clubTitle.setText(schoolClub.getClubTITLE());
        binding.clubDesc.setText(schoolClub.getClubDESC());
        Picasso.get().load(schoolClub.getClubIMAGE()).into(binding.clubImage);
        binding.clubDate.setText(schoolClub.getClubDATE() + " döneminden beri aktif");
    }

    private void setClubConnections(){
        if(schoolClub.getClubFacebookADDRESS().isEmpty()){
            binding.facebookLayout.setVisibility(View.GONE);
        }
        if(schoolClub.getClubWhatsappADDRESS().isEmpty()){
            binding.whatsappLayout.setVisibility(View.GONE);
        }
        if(schoolClub.getClubInstagramADDRESS().isEmpty()){
            binding.instagramLayout.setVisibility(View.GONE);
        }
        if(schoolClub.getClubTwitterADDRESS().isEmpty()){
            binding.twitterLayout.setVisibility(View.GONE);
        }
    }

    private void clickVotesButton(Boolean voteSTATUS){
        String time = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> voteMap = new HashMap<>();
        voteMap.put(Constants.KEY_VOTE_ID, time);
        voteMap.put(Constants.KEY_VOTE_Object_ID, schoolClub.getClubID());
        voteMap.put(Constants.KEY_VOTE_TYPE, Constants.KEY_SCHOOL_CLUB_COLLECTIONS);
        voteMap.put(Constants.KEY_VOTE_UserID, preferenceManager.getString(Constants.KEY_STUDENT_ID));
        voteMap.put(Constants.KEY_VOTE_STATUS, voteSTATUS);

        database.collection(Constants.KEY_VOTE_COLLECTIONS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                Vote vote = new Vote(
                                        documentSnapshot.getString(Constants.KEY_VOTE_ID),
                                        documentSnapshot.getString(Constants.KEY_VOTE_Object_ID),
                                        documentSnapshot.getString(Constants.KEY_VOTE_TYPE),
                                        documentSnapshot.getString(Constants.KEY_VOTE_UserID),
                                        documentSnapshot.getBoolean(Constants.KEY_VOTE_STATUS));

                                if(vote.getVoteUserID().equals(preferenceManager.getString(Constants.KEY_STUDENT_ID))
                                        && vote.getVoteTYPE().equals(Constants.KEY_SCHOOL_CLUB_COLLECTIONS)
                                        && vote.getVoteObjectID().equals(schoolClub.getClubID())){
                                    count++;
                                }
                            }
                            if(count > 0){
                                Toast.makeText(getApplicationContext(), "Daha Önce Oy Kullandınız", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                database.collection(Constants.KEY_VOTE_COLLECTIONS)
                                        .document(time)
                                        .set(voteMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if(task.isSuccessful()){
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
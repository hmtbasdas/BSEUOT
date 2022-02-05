package com.hmtbasdas.bseuot.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hmtbasdas.bseuot.Adapters.ConfessionAdapter;
import com.hmtbasdas.bseuot.Alerts.AlertReportConfession;
import com.hmtbasdas.bseuot.Listeners.ReportConfessionListener;
import com.hmtbasdas.bseuot.Models.Confession;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.Intents;
import com.hmtbasdas.bseuot.databinding.ActivityConfessionBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ConfessionActivity extends BaseActivity implements ReportConfessionListener {

    private ActivityConfessionBinding binding;
    private FirebaseFirestore database;

    private ArrayList<Confession> confessionArrayList;
    private ConfessionAdapter confessionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        getConfessions();
    }

    private void init(){
        database = FirebaseFirestore.getInstance();

        confessionArrayList = new ArrayList<>();
        confessionAdapter = new ConfessionAdapter(confessionArrayList, this);
        binding.listConfessions.setLayoutManager(new LinearLayoutManager(this));
        binding.listConfessions.setAdapter(confessionAdapter);
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.addConfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intents.goActivity(getApplicationContext(), new AddConfessionActivity());
                finish();
            }
        });
    }

    private void getConfessions(){
        database.collection(Constants.KEY_CONFESSION_COLLECTIONS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(!confessionArrayList.isEmpty()){
                                confessionArrayList.clear();
                            }

                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                Confession confession = new Confession(
                                        documentSnapshot.getString(Constants.KEY_CONFESSION_ID),
                                        documentSnapshot.getString(Constants.KEY_CONFESSION_DATE),
                                        documentSnapshot.getString(Constants.KEY_CONFESSION_CONTENT),
                                        documentSnapshot.getString(Constants.KEY_CONFESSION_UserID),
                                        documentSnapshot.getBoolean(Constants.KEY_CONFESSION_STATUS)
                                );

                                if(confession.getConfessionSTATUS()){
                                    confessionArrayList.add(confession);
                                }
                            }
                            Collections.reverse(confessionArrayList);

                            confessionAdapter.notifyDataSetChanged();
                            binding.progressItems.setVisibility(View.INVISIBLE);
                            binding.listConfessions.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onClickReportConfession(Confession confession) {
        AlertReportConfession alertReportConfession = new AlertReportConfession(this, database);
        alertReportConfession.reportConfession(confession).show();
    }
}
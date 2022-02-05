package com.hmtbasdas.bseuot.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hmtbasdas.bseuot.Listeners.SchoolDormitoryListener;
import com.hmtbasdas.bseuot.Models.SchoolDormitory;
import com.hmtbasdas.bseuot.Models.Vote;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.databinding.SchoolDormitoryItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class SchoolDormitoryAdapter extends RecyclerView.Adapter<SchoolDormitoryAdapter.MyViewHolder> {

    private final ArrayList<SchoolDormitory> schoolDormitories;
    private final SchoolDormitoryListener schoolDormitoryListener;
    private final FirebaseFirestore database;
    private int countLike = 0;
    private int countDislike = 0;

    public SchoolDormitoryAdapter(ArrayList<SchoolDormitory> schoolDormitories, SchoolDormitoryListener schoolDormitoryListener, FirebaseFirestore database) {
        this.schoolDormitories = schoolDormitories;
        this.schoolDormitoryListener = schoolDormitoryListener;
        this.database = database;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SchoolDormitoryItemBinding schoolDormitoryItemBinding = SchoolDormitoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(schoolDormitoryItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.setData(schoolDormitories.get(position));
    }

    @Override
    public int getItemCount() {
        return schoolDormitories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        SchoolDormitoryItemBinding binding;

        public MyViewHolder(@NonNull @NotNull SchoolDormitoryItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        private void setData(SchoolDormitory schoolDormitory){
            binding.dormitoryTitle.setText(schoolDormitory.getDormitoryTITLE());
            setVoteData(schoolDormitory);

            if(schoolDormitory.getDormitoryTYPE().equals("erkek")){
                binding.dormitoryType.setTextColor(Color.parseColor("#2196F3"));
                binding.dormitoryType.setText("Erkekler için");
            }
            else if(schoolDormitory.getDormitoryTYPE().equals("kadın")){
                binding.dormitoryType.setTextColor(Color.parseColor("#E91E63"));
                binding.dormitoryType.setText("Kadınlar için");
            }

            binding.getRoot().setOnClickListener(v -> schoolDormitoryListener.onSchoolDormitoryClicked(schoolDormitory));
        }

        private void setVoteData(SchoolDormitory schoolDormitory) {
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

                                    if (vote.getVoteObjectID().equals(schoolDormitory.getDormitoryID())) {
                                        if(vote.getVoteSTATUS()){
                                            countLike++;
                                        }
                                        else {
                                            countDislike++;
                                        }
                                    }
                                }
                                binding.dormitoryLIKE.setText(String.valueOf(countLike));
                                binding.dormitoryDISLIKE.setText(String.valueOf(countDislike));
                                countLike = 0;
                                countDislike = 0;
                            }
                        }
                    });
        }
    }
}

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
import com.hmtbasdas.bseuot.Listeners.SchoolClubListener;
import com.hmtbasdas.bseuot.Models.SchoolClub;
import com.hmtbasdas.bseuot.Models.Vote;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.databinding.SchoolClubItemBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class SchoolClubAdapter extends RecyclerView.Adapter<SchoolClubAdapter.MyViewHolder> {

    private final ArrayList<SchoolClub> schoolClubArrayList;
    private final SchoolClubListener schoolClubListener;
    private final FirebaseFirestore database;
    private int countLike = 0;
    private int countDislike = 0;

    public SchoolClubAdapter(ArrayList<SchoolClub> schoolClubArrayList, SchoolClubListener schoolClubListener, FirebaseFirestore database) {
        this.schoolClubArrayList = schoolClubArrayList;
        this.schoolClubListener = schoolClubListener;
        this.database = database;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SchoolClubItemBinding schoolClubItemBinding = SchoolClubItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(schoolClubItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.setData(schoolClubArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return schoolClubArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        SchoolClubItemBinding binding;

        public MyViewHolder(@NonNull @NotNull SchoolClubItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        private void setData(SchoolClub schoolClub){
            binding.clubTitle.setText(schoolClub.getClubTITLE());
            binding.clubDesc.setText(schoolClub.getClubDESC());
            Picasso.get().load(schoolClub.getClubIMAGE()).into(binding.clubImage);
            setVoteData(schoolClub);
            binding.getRoot().setOnClickListener(v -> schoolClubListener.onSchoolClubClicked(schoolClub));
        }

        private void setVoteData(SchoolClub schoolClub){
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

                                    if (vote.getVoteObjectID().equals(schoolClub.getClubID())) {
                                        if(vote.getVoteSTATUS()){
                                            countLike++;
                                        }
                                        else {
                                            countDislike++;
                                        }
                                    }
                                }
                                binding.clubLIKE.setText(String.valueOf(countLike));
                                binding.clubDISLIKE.setText(String.valueOf(countDislike));
                            }
                        }
                    });
        }
    }
}

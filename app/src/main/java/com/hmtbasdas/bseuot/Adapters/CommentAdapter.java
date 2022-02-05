package com.hmtbasdas.bseuot.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Listeners.DeleteCommentListener;
import com.hmtbasdas.bseuot.Listeners.ReportCommentListener;
import com.hmtbasdas.bseuot.Models.Comment;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.CommentItemBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private final ArrayList<Comment> commentArrayList;
    private final ReportCommentListener listener;
    private final DeleteCommentListener deleteCommentListener;
    private final FirebaseFirestore database;
    private final PreferenceManager preferenceManager;

    public CommentAdapter(ArrayList<Comment> commentArrayList, ReportCommentListener listener, DeleteCommentListener deleteCommentListener, FirebaseFirestore database, PreferenceManager preferenceManager) {
        this.commentArrayList = commentArrayList;
        this.listener = listener;
        this.deleteCommentListener = deleteCommentListener;
        this.database = database;
        this.preferenceManager = preferenceManager;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CommentItemBinding commentItemBinding = CommentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(commentItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        database.collection(Constants.KEY_STUDENT_COLLECTIONS)
                .document(commentArrayList.get(position).getCommentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            commentArrayList.get(position).setCommentUserIMAGE(task.getResult().getString(Constants.KEY_STUDENT_IMAGE));
                            commentArrayList.get(position).setCommentUserNAME(task.getResult().getString(Constants.KEY_STUDENT_NAME) + " " + task.getResult().getString(Constants.KEY_STUDENT_SURNAME));

                            holder.setData(commentArrayList.get(position));
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CommentItemBinding binding;

        public MyViewHolder(@NonNull @NotNull CommentItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void setData(Comment comment){
            Picasso.get().load(comment.getCommentUserIMAGE()).into(binding.commentUserImage);
            binding.commentUserValue.setText(comment.getCommentUserNAME());
            binding.commentUserText.setText(comment.getCommentTEXT());
            binding.commentDate.setText(getReadableDateTime(new Date(comment.getCommentDATE())));
            binding.reportUser.setOnClickListener(v -> listener.onReportCommentClicked(comment));
            if(comment.getCommentUserID().equals(preferenceManager.getString(Constants.KEY_STUDENT_ID))){
                binding.deleteComment.setVisibility(View.VISIBLE);
            }
            else
            {
                binding.deleteComment.setVisibility(View.GONE);
            }
            binding.deleteComment.setOnClickListener(v -> deleteCommentListener.onDeleteCommentClicked(comment));
        }

        private String getReadableDateTime(Date date){
            return new SimpleDateFormat("dd/MM/yyyy, EEEE", Locale.getDefault()).format(date);
        }
    }
}

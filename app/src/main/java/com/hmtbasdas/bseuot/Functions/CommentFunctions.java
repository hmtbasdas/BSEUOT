package com.hmtbasdas.bseuot.Functions;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hmtbasdas.bseuot.Adapters.CommentAdapter;
import com.hmtbasdas.bseuot.Models.Comment;
import com.hmtbasdas.bseuot.R;
import com.hmtbasdas.bseuot.Utilities.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class CommentFunctions {

    private final FirebaseFirestore database;
    private final Context context;
    private final ArrayList<Comment> commentArrayList;
    private final Activity activity;
    private final CommentAdapter commentAdapter;
    private final String collection;
    private final String id;

    public CommentFunctions(FirebaseFirestore database, ArrayList<Comment> commentArrayList, CommentAdapter commentAdapter, String collection, String id, Context context, Activity activity) {
        this.database = database;
        this.context = context;
        this.commentArrayList = commentArrayList;
        this.activity = activity;
        this.commentAdapter = commentAdapter;
        this.collection = collection;
        this.id = id;
    }

    public void sendComment(String reference, HashMap<String, Object> customMap){
        database.collection(Constants.KEY_COMMENT_COLLECTIONS)
                .document(reference)
                .set(customMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            SpinKitView spinKitView = activity.findViewById(R.id.progressItems);
                            View view = activity.findViewById(R.id.progressView);
                            EditText editText = activity.findViewById(R.id.commentText);

                            spinKitView.setVisibility(View.INVISIBLE);
                            view.setVisibility(View.INVISIBLE);

                            editText.getText().clear();

                            getComments();
                        }
                        else {
                            Toast.makeText(context.getApplicationContext(),"Bir hata oluştu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getComments(){
        database.collection(Constants.KEY_COMMENT_COLLECTIONS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(!commentArrayList.isEmpty()){
                                commentArrayList.clear();
                            }
                            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){

                                Comment comment = new Comment();
                                
                                comment.setCommentID(documentSnapshot.getString(Constants.KEY_COMMENT_ID));
                                comment.setCommentTYPE(documentSnapshot.getString(Constants.KEY_COMMENT_TYPE));
                                comment.setCommentDATE(documentSnapshot.getString(Constants.KEY_COMMENT_DATE));
                                comment.setCommentTEXT(documentSnapshot.getString(Constants.KEY_COMMENT_TEXT));
                                comment.setCommentUserID(documentSnapshot.getString(Constants.KEY_COMMENT_UserID));
                                comment.setCommentObjectID(documentSnapshot.getString(Constants.KEY_COMMENT_ObjectID));
                                comment.setCommentSTATUS(documentSnapshot.getBoolean(Constants.KEY_COMMENT_STATUS));

                                if(comment.getCommentObjectID().equals(id)
                                        && comment.getCommentTYPE().equals(collection)
                                        && comment.getCommentSTATUS()){

                                    commentArrayList.add(comment);
                                }
                            }

                            LinearLayout emptyLayout = activity.findViewById(R.id.emptyLayout);
                            if(commentArrayList.isEmpty()){
                                emptyLayout.setVisibility(View.VISIBLE);
                            }
                            else {
                                emptyLayout.setVisibility(View.INVISIBLE);

                                Collections.reverse(commentArrayList);
                                commentAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }

    public void deleteComment(Comment comment){
        database.collection("comments")
                .document(comment.getCommentID())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(context.getApplicationContext(), "Yorumunuz Silindi", Toast.LENGTH_SHORT).show();
                        getComments();
                    }
                });
    }

    public void reportComment(String time, HashMap<String, Object> reportCommentMap){
        database.collection(Constants.KEY_REPORT_COMMENT_COLLECTIONS)
                .document(time)
                .set(reportCommentMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context.getApplicationContext(), "Raporlandı", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
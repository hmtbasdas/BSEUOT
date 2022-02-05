package com.hmtbasdas.bseuot.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Adapters.CommentAdapter;
import com.hmtbasdas.bseuot.Alerts.AlertReportComment;
import com.hmtbasdas.bseuot.Alerts.DeleteReportComment;
import com.hmtbasdas.bseuot.Functions.CommentFunctions;
import com.hmtbasdas.bseuot.Listeners.DeleteCommentListener;
import com.hmtbasdas.bseuot.Listeners.ReportCommentListener;
import com.hmtbasdas.bseuot.Models.Comment;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityCommentListBinding;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentListActivity extends AppCompatActivity implements ReportCommentListener, DeleteCommentListener {

    private RequestQueue requestQueue;
    private Response.Listener<String> listener;
    private Response.ErrorListener errorListener;
    private String URL = "https://time.is/Unix_time_converter";
    private StringRequest stringRequest;

    private ActivityCommentListBinding binding;

    private FirebaseFirestore database;

    private PreferenceManager preferenceManager;
    private CommentFunctions commentFunctions;
    private ArrayList<Comment> commentArrayList;
    private CommentAdapter commentAdapter;

    private String collection;
    private String id;

    private String Unix_Time;
    private String Formatted_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        getComments();
        setUserValues();
    }

    private void init(){
        database = FirebaseFirestore.getInstance();

        preferenceManager = new PreferenceManager(getApplicationContext());

        commentArrayList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentArrayList, this,this, database, preferenceManager);

        binding.commentListRecycler.setAdapter(commentAdapter);
        binding.commentListRecycler.setLayoutManager(new LinearLayoutManager(this));

        collection = getIntent().getStringExtra(Constants.KEY_INTENT_COLLECTION);
        id = getIntent().getStringExtra(Constants.KEY_INTENT_ID);

        commentFunctions = new CommentFunctions(database, commentArrayList, commentAdapter, collection, id, this, this);
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.commentText.getText().toString().isEmpty()){
                    binding.progressItems.setVisibility(View.VISIBLE);
                    binding.progressView.setVisibility(View.VISIBLE);

                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    listenerSettings();

                    stringRequest = new StringRequest(Request.Method.GET, URL, listener, errorListener);
                    requestQueue.add(stringRequest);
                }
                else {
                    Toast.makeText(CommentListActivity.this, "Gerçekten mi ?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUserValues(){
        Picasso.get().load(preferenceManager.getString(Constants.KEY_STUDENT_IMAGE)).into(binding.userImage);
    }

    private void sendComment(String commentText, String time1, String time2){

        HashMap<String, Object> commentMap = new HashMap<>();

        commentMap.put(Constants.KEY_COMMENT_ID, time1);
        commentMap.put(Constants.KEY_COMMENT_DATE, time2);
        commentMap.put(Constants.KEY_COMMENT_TYPE, collection);
        commentMap.put(Constants.KEY_COMMENT_TEXT, commentText);
        commentMap.put(Constants.KEY_COMMENT_UserID, preferenceManager.getString(Constants.KEY_STUDENT_ID));
        commentMap.put(Constants.KEY_COMMENT_ObjectID, id);
        commentMap.put(Constants.KEY_COMMENT_STATUS, true);

        commentFunctions.sendComment(Unix_Time, commentMap);
    }

    private void getComments(){
        commentFunctions.getComments();
    }


    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void listenerSettings(){
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getTime(response);
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Servise Erişilemedi", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void getTime(String response){
        Document document = Jsoup.parse(response);
        Element unix_time = document.getElementById("unix_time");
        Element formatted_time = document.getElementById("formatted_time");

        Unix_Time = unix_time.val();
        Formatted_Time = formatted_time.val();

        if(!Unix_Time.isEmpty() && !Formatted_Time.isEmpty()){
            closeKeyboard();
            sendComment(binding.commentText.getText().toString(), Unix_Time, Formatted_Time);
        }
    }

    @Override
    public void onReportCommentClicked(Comment comment) {
        AlertReportComment alertReportComment = new AlertReportComment(this, preferenceManager, commentFunctions);
        alertReportComment.reportComment(comment).show();
    }

    @Override
    public void onDeleteCommentClicked(Comment comment) {
        DeleteReportComment deleteReportComment = new DeleteReportComment(this, commentFunctions);
        deleteReportComment.deleteComment(comment).show();
        //getComments();
    }
}
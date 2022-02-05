package com.hmtbasdas.bseuot.Alerts;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Models.Question;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;

public class AlertReportQuestion {

    private final Context context;
    private final FirebaseFirestore database;

    private final PreferenceManager preferenceManager;

    public AlertReportQuestion(Context context, FirebaseFirestore database, PreferenceManager preferenceManager) {
        this.context = context;
        this.database = database;
        this.preferenceManager = preferenceManager;
    }

    public AlertDialog reportQuestion(Question question){
        String time = String.valueOf(System.currentTimeMillis());
        return new AlertDialog.Builder(context)
                .setTitle("Soruyu raporlamak istiyor musunuz ?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, Object> reportQuestionMap = new HashMap<>();
                        reportQuestionMap.put(Constants.KEY_REPORT_QUESTION_ID, time);
                        reportQuestionMap.put(Constants.KEY_REPORT_QUESTION_DATE, (new Date()).toString());
                        reportQuestionMap.put(Constants.KEY_REPORT_QUESTION_ORJ_ID, question.getQuestionID());
                        reportQuestionMap.put(Constants.KEY_REPORT_QUESTION_TITLE, question.getQuestionTITLE());
                        reportQuestionMap.put(Constants.KEY_REPORT_QUESTION_TEXT, question.getQuestionTEXT());
                        reportQuestionMap.put(Constants.KEY_REPORT_QUESTION_UserID, question.getQuestionUserID());
                        reportQuestionMap.put(Constants.KEY_REPORT_QUESTION_RUserID, preferenceManager.getString(Constants.KEY_STUDENT_ID));
                        reportQuestionMap.put(Constants.KEY_REPORT_QUESTION_STATUS, false);

                        database.collection(Constants.KEY_REPORT_QUESTION_COLLECTIONS)
                                .document(time)
                                .set(reportQuestionMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context.getApplicationContext(), "Raporlandı", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
    }
}

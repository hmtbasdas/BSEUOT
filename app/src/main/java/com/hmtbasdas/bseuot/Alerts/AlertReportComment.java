package com.hmtbasdas.bseuot.Alerts;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Functions.CommentFunctions;
import com.hmtbasdas.bseuot.Models.Comment;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;


import java.util.Date;
import java.util.HashMap;

public class AlertReportComment  {

    private final Context context;

    private final PreferenceManager preferenceManager;
    private final CommentFunctions commentFunctions;

    public AlertReportComment(Context context, PreferenceManager preferenceManager, CommentFunctions commentFunctions) {
        this.context = context;
        this.preferenceManager = preferenceManager;
        this.commentFunctions = commentFunctions;
    }

    public AlertDialog reportComment(Comment comment){
        String time = String.valueOf(System.currentTimeMillis());
        return new AlertDialog.Builder(context)
                .setTitle("Yorumu raporlamak istiyor musunuz ?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, Object> reportCommentMap = new HashMap<>();
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_ID, time);
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_TYPE, comment.getCommentTYPE());
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_ORJ_ID, comment.getCommentObjectID());
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_ObjectID, comment.getCommentID());
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_DATE, (new Date()).toString());
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_TEXT, comment.getCommentTEXT());
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_UserID, comment.getCommentUserID());
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_RUserID, preferenceManager.getString(Constants.KEY_STUDENT_ID));
                        reportCommentMap.put(Constants.KEY_REPORT_COMMENT_STATUS, false);

                        commentFunctions.reportComment(time, reportCommentMap);
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                }).create();
    }
}

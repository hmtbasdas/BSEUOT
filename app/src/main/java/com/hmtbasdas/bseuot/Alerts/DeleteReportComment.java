package com.hmtbasdas.bseuot.Alerts;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.hmtbasdas.bseuot.Functions.CommentFunctions;
import com.hmtbasdas.bseuot.Models.Comment;


public class DeleteReportComment {

    private final Context context;
    private final CommentFunctions commentFunctions;

    public DeleteReportComment(Context context, CommentFunctions commentFunctions) {
        this.context = context;
        this.commentFunctions = commentFunctions;
    }

    public AlertDialog deleteComment(Comment comment){
        return new AlertDialog.Builder(context)
                .setTitle("Yorumunuzu silmek mi istiyorsunuz ?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commentFunctions.deleteComment(comment);
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

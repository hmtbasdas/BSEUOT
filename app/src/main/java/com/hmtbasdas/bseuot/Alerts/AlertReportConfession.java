package com.hmtbasdas.bseuot.Alerts;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Models.Confession;
import com.hmtbasdas.bseuot.Utilities.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;

public class AlertReportConfession {

    private final Context context;
    private final FirebaseFirestore database;

    public AlertReportConfession(Context context, FirebaseFirestore database) {
        this.context = context;
        this.database = database;
    }

    public AlertDialog reportConfession(Confession confession) {
        String time = String.valueOf(System.currentTimeMillis());
        return new AlertDialog.Builder(context)
                .setTitle("İtirafı raporlamak istiyor musunuz ?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, Object> confessionMap = new HashMap<>();
                        confessionMap.put(Constants.KEY_REPORT_CONFESSION_ID, time);
                        confessionMap.put(Constants.KEY_REPORT_CONFESSION_CONTENT, confession.getConfessionCONTENT());
                        confessionMap.put(Constants.KEY_REPORT_CONFESSION_UserID, confession.getConfessionUserID());
                        confessionMap.put(Constants.KEY_REPORT_CONFESSION_DATE, (new Date()).toString());
                        confessionMap.put(Constants.KEY_REPORT_CONFESSION_STATUS, false);

                        database.collection(Constants.KEY_REPORT_CONFESSION_COLLECTIONS)
                                .document(time)
                                .set(confessionMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context.getApplicationContext(), "Raporlandı", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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

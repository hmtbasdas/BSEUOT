package com.hmtbasdas.bseuot.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class Intents {

    public static void goActivity(Context context, Activity activity){
        Intent intent = new Intent(context.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

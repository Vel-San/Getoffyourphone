package com.nephi.getoffyourphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class BootUpReceiver extends BroadcastReceiver {

    //Database
    DB_Helper db;


    @Override
    public void onReceive(Context context, Intent intent) {
        //DataBase Handler
        db = new DB_Helper(context);

        if(Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED))
        {
            db.set_Running("N");
        }

        Toast.makeText(context, "Phone Has Been Rebooted - GOYP Timer Service Fixed", Toast.LENGTH_LONG).show();
        Log.d("PhoneReboot", "Phone Has Been Rebooted");
    }

}

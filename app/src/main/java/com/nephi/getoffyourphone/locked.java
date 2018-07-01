package com.nephi.getoffyourphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by xerxes on 03.12.17.
 */

public class locked extends AppCompatActivity {

    TextView tv1;
    private BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = intent.getStringExtra("time");
            tv1.setText("Time left to unlock: " + str_time);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locked_app);
        init();
    }

    public void init() {
        tv1 = findViewById(R.id.locked_counter);
        //getSupportActionBar().hide();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver1, new IntentFilter(Timer_Service.str_receiver));
        Log.e("Lock_Screen", "ON");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver1);
        Log.e("Lock_Screen", "OFF");
    }

}

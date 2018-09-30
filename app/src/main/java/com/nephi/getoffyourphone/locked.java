package com.nephi.getoffyourphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by xerxes on 03.12.17.
 */

public class locked extends AppCompatActivity {

    TextView tv1;
    //Vibrator
    Vibrator v;
    long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

    private BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = intent.getStringExtra("time");
            tv1.setText(getString(R.string.time_left2) + str_time);
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

        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Start without a delay
        // Each element then alternates between vibrate, sleep, vibrate, sleep...
        assert v != null;



        //getSupportActionBar().hide();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // The '0' here means to repeat indefinitely
        // '0' is actually the index at which the pattern keeps repeating from (the start)
        // To repeat the pattern from any other point, you could increase the index, e.g. '1'
        v.vibrate(pattern, 0);
        registerReceiver(broadcastReceiver1, new IntentFilter(Timer_Service.str_receiver));
        Log.e("Lock_Screen", "ON");
    }

    @Override
    protected void onPause() {
        super.onPause();
        v.cancel();
        unregisterReceiver(broadcastReceiver1);
        Log.e("Lock_Screen", "OFF");
    }

}

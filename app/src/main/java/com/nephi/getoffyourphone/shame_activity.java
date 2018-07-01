package com.nephi.getoffyourphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by xerxes on 23.01.18.
 */

public class shame_activity extends AppCompatActivity {

    TextView tv1;
    ImageView toGif;
    //DH Helper
    DB_Helper db;
    //GIF
    //resource (drawable or raw)
    GifDrawable gif1;
    GifDrawable gif2;
    GifDrawable gif3;
    GifDrawable gif4;
    private BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = intent.getStringExtra("time");
            if (db.get_openCounter(1) == 5) {
                tv1.setText("You see that box ? That's you in the box. \nYou have no self control. \nGet Back to Work."
                        + "\n" + "Oh And, time left to unlock: " + str_time);
            } else if (db.get_openCounter(1) == 15) {
                tv1.setText("OH I'VE GOT YOU NOW!\nTOUCH THIS APP ONE MORE TIME.\nI DARE YOU, I DOUBLE DARE YOU!"
                        + "\n" + "Oh And, time left to unlock: " + str_time);
            } else if (db.get_openCounter(1) == 20) {
                tv1.setText("You're pressing my buttons here..\nWhy are you doing this ?\nIt's already locked & loaded now"
                        + "\n" + "Oh And, time left to unlock: " + str_time);
            } else if (db.get_openCounter(1) > 20) {
                tv1.setText("BRING IT ON !\nDODGE THIS\nDODGE THAT\nNO WAY TO RUN!!!"
                        + "\n" + "Oh And, time left to unlock: " + str_time);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shame_activity);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        //DB
        db = new DB_Helper(this);
        gif1 = new GifDrawable(getResources(), R.drawable.shame);
        gif2 = new GifDrawable(getResources(), R.drawable.gif2);
        gif3 = new GifDrawable(getResources(), R.drawable.gif3);
        gif4 = new GifDrawable(getResources(), R.drawable.gif4);

        tv1 = findViewById(R.id.locked_counter);
        toGif = findViewById(R.id.image_to_gif);

        if (db.get_openCounter(1) == 5) {
            toGif.setImageDrawable(gif1);
        } else if (db.get_openCounter(1) == 15) {
            toGif.setImageDrawable(gif2);
        } else if (db.get_openCounter(1) == 20) {
            toGif.setImageDrawable(gif3);
        } else if (db.get_openCounter(1) > 20) {
            toGif.setImageDrawable(gif4);
        }
        //getSupportActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver1, new IntentFilter(Timer_Service.str_receiver));
        Log.e("Shame_Screen", "ON");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver1);
        Log.e("Shame_Screen", "OFF");
    }

}


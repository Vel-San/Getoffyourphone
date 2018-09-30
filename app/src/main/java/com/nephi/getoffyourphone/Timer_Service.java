package com.nephi.getoffyourphone;

/**
 * Created by xerxes on 02.12.17.
 */

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.scottyab.rootbeer.RootBeer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;


public class Timer_Service extends Service {

    public static final long NOTIFY_INTERVAL = 1000;
    public static String str_receiver = "com.nephi.getoffyourphone.receiver";
    public String str_testing;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String strDate;
    Date date_current, date_diff;
    //Root Detector
    RootBeer rootbeer;
    //Con Manager
    WifiManager wifiManager;
    //DH Helper
    DB_Helper db;
    Intent intent;
    Intent lockIntent;
    Intent Shame;
    //Shame Int Counter
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Lock Screen launch
        lockIntent = new Intent(this, locked.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("H:M:ss");

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);
        intent = new Intent(str_receiver);

        //Root Detector
        rootbeer = new RootBeer(this);
        //Wifi Manager
        wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);

        //DB
        db = new DB_Helper(this);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        assert alarmService != null;
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
        Log.e("Service_Auto_Restart", "ON");
    }

    public String twoDatesBetweenTime() {


        try {
            date_current = simpleDateFormat.parse(strDate);
        } catch (Exception e) {

        }

        try {
            date_diff = simpleDateFormat.parse(db.get_Data(1));
        } catch (Exception e) {

        }

        try {


            long diff = date_current.getTime() - date_diff.getTime();
            int int_hours = Integer.valueOf(db.get_Hours(1));
            long int_timer;
            if (int_hours > 10) {
                int_timer = TimeUnit.MINUTES.toMillis(int_hours);
            } else {
                int_timer = TimeUnit.HOURS.toMillis(int_hours);
            }
            long long_hours = int_timer - diff;
            long diffSeconds2 = long_hours / 1000 % 60;
            long diffMinutes2 = long_hours / (60 * 1000) % 60;
            long diffHours2 = long_hours / (60 * 60 * 1000) % 24;


            if (long_hours >= 0) {
                str_testing = String.format("%d:%d:%02d", diffHours2, diffMinutes2, diffSeconds2);
                Log.e("TIME", str_testing);
                db.set_TimerFinish(0);
                //db.set_Running("Y");
                fn_update(str_testing);

            } else {
                stopService(new Intent(getApplicationContext(), Timer_Service.class));
                notification_update();
                db.set_TimerFinish(1);
                db.set_Running("N");
                //db.set_LockTime("");
                db.set_Hours("");
                db.set_Data("");
                //db.set_openCounter(0);
                db.set_on_off(0);
                switch (db.get_StateTable(1)){
                    case 1:
                        db.set_StateTitle("None");
                        unlockStateWifi();
                        break;
                    case 2:
                        db.set_StateTitle("None");
                        unlockStateWifi();
                        unlockStateData();
                        break;
                }
                mTimer.cancel();
            }
        } catch (Exception e) {
            stopService(new Intent(getApplicationContext(), Timer_Service.class));
            db.set_TimerFinish(1);
            db.set_Running("N");
            // db.set_LockTime("");
            db.set_Hours("");
            db.set_Data("");
            //db.set_openCounter(0);
            db.set_on_off(0);
            db.set_StateTitle("None");
            switch (db.get_StateTable(1)){
                case 1:
                    db.set_StateTitle("None");
                    unlockStateWifi();
                    break;
                case 2:
                    db.set_StateTitle("None");
                    unlockStateWifi();
                    unlockStateData();
                    break;
            }
            mTimer.cancel();
            mTimer.purge();
        }

        return "";

    }

    public void notification_update() {
//        Intent intent = new Intent(this, Main.class);
//        // use System.currentTimeMillis() to have a unique ID for the pending intent
//        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt(); // just use a counter in some util class...

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("notification_1", "Timer_Notification", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 500,});
            notificationChannel.enableVibration(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_1");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT) //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
                //.setDefaults(Notification.) // also requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher) // Required!
                .setContentTitle(getString(R.string.notification_title2))
                .setContentText(getString(R.string.notification_message))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_message)))
                .setVibrate(new long[]{0, 500});
                //.setAutoCancel(true);
        //.setOngoing(true)
        //.addAction(R.drawable.ic_clear_black_48dp, "Dismiss", dismissIntent);
        //.addAction(R.drawable.ic_action_boom, "Action!", someOtherPendingIntent);

        // Builds the notification and issues it.
        assert notificationManager != null;
        notificationManager.notify(313, builder.build());
    }

    private void LockApps() {
        if ((int) db.getAppsCount() != 0) {
            int count = (int) db.getAppsCount();
            for (int i = 1; i <= count; ++i) {
                if (printForegroundTask().equalsIgnoreCase(db.get_app_PKG(i))) {
                            startActivity(lockIntent);
                }
            }
        }
    }

    private void lock_State() throws Exception {
        if (db.get_on_off(1) == 1) {
            switch (db.get_StateTable(1)) {
                case 1:
                    assert wifiManager != null;
                    if (wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(false);
                    }
                    break;
                case 2:
                    assert wifiManager != null;
                    if (wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(false);
                    }
                    if (rootbeer.isRooted()) {
                        Process p;
                        try {
                            p = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(p.getOutputStream());
                            outputStream.writeBytes("svc data disable" + "\n");
                            outputStream.flush();
                            Log.e("Mobile Data", "Disabled");
                            outputStream.writeBytes("exit\n");
                            outputStream.flush();

                            p.waitFor();
                        } catch (IOException | InterruptedException e) {
                            Toast.makeText(this, "Root not detected for Airplane Mode", Toast.LENGTH_LONG).show();
                            throw new Exception(e);
                        }
                    }

            }

        }
    }

    public void unlockStateWifi() {
        assert wifiManager != null;
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public void unlockStateData() {

        if (rootbeer.isRooted()) {
            Process p;
            try {
                p = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(p.getOutputStream());
                outputStream.writeBytes("svc data enable" + "\n");
                outputStream.flush();
                Log.e("Mobile Data", "Enabled");
                outputStream.writeBytes("exit\n");
                outputStream.flush();
                p.waitFor();
            } catch (IOException | InterruptedException e) {
                Toast.makeText(this, "Root not detected for Airplane Mode", Toast.LENGTH_LONG).show();

            }
        }
    }

    private String printForegroundTask() {
        String currentApp = "";
        @SuppressLint("WrongConstant") UsageStatsManager usm = (UsageStatsManager) this.getSystemService("usagestats");
        long time = System.currentTimeMillis();
        assert usm != null;
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            assert am != null;
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.e("Foreground App", "Current App in foreground is: " + currentApp);
        return currentApp;
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(getApplicationContext(), Timer_Service.class));
        super.onDestroy();
        db.set_TimerFinish(1);
        db.set_Running("N");
//        db.set_LockTime("");
        db.set_Hours("");
        db.set_Data("");
//        db.set_openCounter(0);
        db.set_on_off(0);
        db.set_StateTitle("None");
        switch (db.get_StateTable(1)){
            case 1:
                db.set_StateTitle("None");
                unlockStateWifi();
                break;
            case 2:
                db.set_StateTitle("None");
                unlockStateWifi();
                unlockStateData();
                break;
        }
        Log.e("Timer Done", "Finish");
    }

    private void fn_update(String str_time) {
        intent.putExtra("time", str_time);
        sendBroadcast(intent);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    strDate = simpleDateFormat.format(calendar.getTime());
                    Log.e("strDate", strDate);
                    twoDatesBetweenTime();
                    LockApps();
                    if (db.get_once(1)==1){
                        db.set_once(0);
                        try {
                            lock_State();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        }

    }
}
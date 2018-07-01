//package com.nephi.getoffyourphone;
//
///**
// * Created by xerxes on 15.01.18.
// */
//
//import android.app.ActivityManager;
//import android.app.Service;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.IBinder;
//import android.os.Process;
//import android.util.Log;
//
//import java.util.List;
//
///**
// * Created by xinghui on 9/20/16.
// * <p>
// * calling this in your Application's onCreate
// * startService(new Intent(this, ReviverService.class));
// * <p>
// * BY THE WAY Don't Forget to Add the Service to the AndroidManifest.xml File.
// * <service android:name=".ReviverService"/>
// */
//public class ReviverService extends Service {
//
//
//    /**
//     * {@link Log#isLoggable(String, int)}
//     * <p>
//     * IllegalArgumentException is thrown if the tag.length() > 23.
//     */
//    private static final String TAG = "Timer_Service";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.e(TAG, "onCreate() for ServiceReviver called");
//
//        ensureCollectorRunning();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
//
//    private void ensureCollectorRunning() {
//        ComponentName collectorComponent = new ComponentName(this, /*NotificationListenerService Inheritance*/ Timer_Service.class);
//        Log.v(TAG, "ensureCollectorRunning collectorComponent: " + collectorComponent);
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        boolean collectorRunning = false;
//        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
//        if (runningServices == null) {
//            Log.w(TAG, "ensureCollectorRunning() runningServices is NULL");
//            return;
//        }
//        for (ActivityManager.RunningServiceInfo service : runningServices) {
//            if (service.service.equals(collectorComponent)) {
//                Log.w(TAG, "ensureCollectorRunning service - pid: " + service.pid + ", currentPID: " + Process.myPid() + ", clientPackage: " + service.clientPackage + ", clientCount: " + service.clientCount
//                        + ", clientLabel: " + ((service.clientLabel == 0) ? "0" : "(" + getResources().getString(service.clientLabel) + ")"));
//                if (service.pid == Process.myPid() /*&& service.clientCount > 0 && !TextUtils.isEmpty(service.clientPackage)*/) {
//                    collectorRunning = true;
//                }
//            }
//        }
//        if (collectorRunning) {
//            Log.e(TAG, "ensureCollectorRunning: collector is running");
//            return;
//        }
//        Log.e(TAG, "ensureCollectorRunning: collector not running, reviving...");
//        ReviveTimer();
//    }
//
//    private void ReviveTimer() {
//        Log.e(TAG, "ReviveTimer() called");
//        ComponentName thisComponent = new ComponentName(this, /*getClass()*/ Timer_Service.class);
//        PackageManager pm = getPackageManager();
//        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}

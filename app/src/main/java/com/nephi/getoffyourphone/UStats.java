package com.nephi.getoffyourphone;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UStats{
    private static String TAG;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    static int i;


    @SuppressWarnings("ResourceType")
    public void getStats(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        int interval = UsageStatsManager.INTERVAL_DAILY;
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();

        calendar.add(Calendar.DATE, 0);
        // set calendar to TODAY 00:00:00.000
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        assert usm != null;
        UsageEvents uEvents = usm.queryEvents(startTime,endTime);
        while (uEvents.hasNextEvent()){
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);

            if (e != null){
                Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
            }
        }
    }

    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();

        //Get today
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, 0);
        // set calendar to TODAY 00:00:00.000
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start: " + dateFormat.format(startTime) );
        Log.d(TAG, "Range end: " + dateFormat.format(endTime));

        return usm.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,startTime,endTime);
    }

    public static void printUsageStats(List<UsageStats> usageStatsList, int j){
        //DataBase Handler
        final DB_Helper db = new DB_Helper(selected_apps.appcontext1);
        for (i = 0; i < usageStatsList.size(); i++){
                if (usageStatsList.get(i).getPackageName().equals(db.get_app(j+1).get_PKG())){
                    String result = String.format("%02d min, %02d sec",
                            TimeUnit.MILLISECONDS.toMinutes(usageStatsList.get(i).getTotalTimeInForeground()),
                            TimeUnit.MILLISECONDS.toSeconds(usageStatsList.get(i).getTotalTimeInForeground()) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(usageStatsList.get(i).getTotalTimeInForeground()))
                    );
                    Log.d(TAG, "Pkg: " + usageStatsList.get(i).getPackageName() +  "\t" + "ForegroundTime: "
                            + result);
//                    Toast.makeText(selected_apps.appcontext1, "Pkg: " + usageStatsList.get(i).getPackageName() +  "\t" + "ForegroundTime: "
//                            + result, Toast.LENGTH_LONG).show();
                    db.set_Usage(result);
                    //Toast.makeText(selected_apps.appcontext1, selected_apps.appcontext1.getString(R.string.usage_dialog_message) + result, Toast.LENGTH_LONG).show();
                    return;
                }
        }
    }

    public static void printCurrentUsageStatus(Context context, int i){
        printUsageStats(getUsageStatsList(context), i);
    }
    @SuppressWarnings("ResourceType")
    public static UsageStatsManager getUsageStatsManager(Context context){
        return (UsageStatsManager) context.getSystemService("usagestats");
    }
}
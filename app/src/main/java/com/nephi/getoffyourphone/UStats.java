package com.nephi.getoffyourphone;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UStats{
    private static String TAG;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
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

    private static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        //Get today
        long endTime = calendar.getTimeInMillis();
        //Date endTimeTest = calendar.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, 0);
        // set calendar to TODAY 00:00:00.000
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        long startTime = calendar2.getTimeInMillis();
        //Date startTimeTest = calendar2.getTime();

        Log.d(TAG, "Range start: " + dateFormat.format(startTime) );
        Log.d(TAG, "Range end: " + dateFormat.format(endTime));

        //Toast.makeText(Main.appContext,"" + endTimeTest,Toast.LENGTH_LONG).show();
        //Toast.makeText(Main.appContext,"" + endTime,Toast.LENGTH_LONG).show();


        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
    }

    private static void printUsageStats(List<UsageStats> usageStatsList, int j){
        //DataBase Handler
        final DB_Helper db = new DB_Helper(selected_apps.appcontext1);
        for (i = 0; i < usageStatsList.size(); i++){
                if (usageStatsList.get(i).getPackageName().equals(db.get_app(j+1).get_PKG())){
                    String result = String.format("%02d h, %02d min, %02d sec",
                            TimeUnit.MILLISECONDS.toHours(usageStatsList.get(i).getTotalTimeInForeground()),
                            TimeUnit.MILLISECONDS.toMinutes(usageStatsList.get(i).getTotalTimeInForeground()) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(usageStatsList.get(i).getTotalTimeInForeground())),
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

    static void printCurrentUsageStatus(Context context, int i){
        printUsageStats(getUsageStatsList(context), i);
    }
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        return (UsageStatsManager) context.getSystemService("usagestats");
    }
}
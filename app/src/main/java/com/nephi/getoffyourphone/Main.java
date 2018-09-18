package com.nephi.getoffyourphone;

import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.danimahardhika.cafebar.CafeBar;
import com.danimahardhika.cafebar.CafeBarTheme;
import com.facebook.stetho.Stetho;
import com.franmontiel.attributionpresenter.AttributionPresenter;
import com.franmontiel.attributionpresenter.entities.Attribution;
import com.franmontiel.attributionpresenter.entities.License;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class Main extends DrawerActivity {
//        implements ForceUpdateChecker.OnUpdateNeededListener {

    //------------Google related------------
    public String h_value = "";
    public String c_value = "";
    String date_time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    //------------Variables------------
    //Database
    DB_Helper db;
    static Context appContext;
    //intents
    Intent lockIntent;
    //Arraylists for Apps
    ArrayList<Integer> preselectedApps = new ArrayList<>();
    ArrayList<MultiSelectModel> listOfApps = new ArrayList<>();
    List<String> LS;
    //Multi-Choice-Selector
    MultiSelectDialog multiSelectDialog;
    //strings
    String package_name;
    //String running;
    //views
    View aboutPage;
    Button Lock;
    TextView title_timer;
    //------------Service related------------
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = intent.getStringExtra("time");
            title_timer.setText(getString(R.string.time_left1)
                    + str_time
                    + "\n" + getString(R.string.selected_time) + db.get_Hours(1)
                    + getString(R.string.minutes_hours) + "\n" + getString(R.string.selected_counter) + db.get_openCounter(1) + getString(R.string.open_times));
            if (db.get_TimerFinish(1) == 1) {
                title_timer.setText(getString(R.string.not_running));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startService(new Intent(this, ReviverService.class));
        init();
    }

    public void init() {

        //------------Variables & Declarations------------
        //String
        package_name = getPackageName();

        //DataBase Handler
        db = new DB_Helper(this);
        appContext=getApplicationContext();
        //Locked_intent
        lockIntent = new Intent(this, locked.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //Title Timer
        title_timer = findViewById(R.id.title_timer);
        //Lock Button
        Lock = findViewById(R.id.sendButton);

        //------------Method Calls------------
        ActionBarMethod();
        permission_check();
        isIgnoringBattery();
//        update_check();
        first_Boot_check();
        try {
            LS = getInstalledComponentList();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        selection_all();
        set_drawer();
        PreSelect();
        about_page();
        //Title text change
        if (db.get_Running(1).equals("N")) {
            title_timer.setText(getString(R.string.not_running));
        }
        Stetho.initializeWithDefaults(this);

    }


    public void about_page() {
        //AboutPageView
        aboutPage = new AboutPage(Main.this)
                .isRTL(false)
                .setDescription(getString(R.string.dialog_description))
                //.setImage(R.drawable.dummy_image)
                //.addItem(new Element().setTitle("Version 6.2"))
                //.addItem(adsElement)
                //.addGroup("Connect with us")
                .addEmail("goyp.sup@outlook.com")
                //.addWebsite("REPLACE WITH UR OWN")
                //.addFacebook("REPLACE WITH UR OWN")
                //.addTwitter("REPLACE WITH UR OWN")
                //.addYoutube("REPLACE WITH UR OWN")
                .addPlayStore("com.nephi.getoffyourphone")
                //.addInstagram("REPLACE WITH UR OWN")
                .addGitHub("Alikaraki95")
                .addItem(getPrivacyP())
                .addItem(FDroid())
                .create();

    }

    Element getPrivacyP() {
        Element privacy = new Element();
        final String privacy_p = getString(R.string.privacy_element);
        privacy.setTitle(privacy_p);
        privacy.setIconDrawable(R.drawable.baseline_security_black_48);
        privacy.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        privacy.setIconNightTint(android.R.color.white);
        privacy.setGravity(Gravity.START);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://htmlpreview.github.io/?https://raw.githubusercontent.com/Alikaraki95/Getoffyourphone/master/privacy_policy.html"));
                startActivity(browserIntent);
            }
        });
        return privacy;
    }

    Element FDroid() {
        Element FDroid_e = new Element();
        final String FDroid = getString(R.string.FDroid_element);
        FDroid_e.setTitle(FDroid);
        FDroid_e.setIconDrawable(R.drawable.fdroid);
        FDroid_e.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        FDroid_e.setIconNightTint(android.R.color.white);
        FDroid_e.setGravity(Gravity.START);
        FDroid_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://f-droid.org/packages/com.nephi.getoffyourphone/"));
                startActivity(browserIntent);
            }
        });
        return FDroid_e;
    }

//    public void update_check() {
//        if (isAppUpdated(this)) {
//            CafeBar.builder(Main.this)
//                    .duration(CafeBar.Duration.MEDIUM)
//                    .content("App has been updated to version: v" + db.get_Vname(1))
//                    .maxLines(4)
//                    .theme(CafeBarTheme.Custom(Color.parseColor("#00C853")))
//                    .show();
//        }
//    }

    public void first_Boot_check() {
        //First launch and update check
        if (db.getFirstBootCount() == 0) {
            db.set_AllTimerData("", "N", 1, "", 0, "");
            db.set_defaultOpenCounter(0, 0);
//          saveVersionNameAndCode(this);
            db.set_FirstBoot("N");
            db.set_defaultUsage("XXX");
        }
    }

    public void permission_check() {
        //Usage Permission
        if (!isAccessGranted()) {
            new LovelyStandardDialog(Main.this)
                    .setTopColorRes(R.color.blue)
                    .setButtonsColorRes(R.color.black)
                    .setIcon(R.drawable.ic_perm_device_information_white_48dp)
                    .setTitle(getString(R.string.permission_check_title))
                    .setMessage(getString(R.string.permission_check_message))
                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    public void selection_all() {

        final SwipeSelector hours_selector = findViewById(R.id.hours_selector);
        final SwipeSelector counter_selector = findViewById(R.id.counter_selector);

        if (!db.get_LockTime(1).isEmpty() && !db.get_LockTime(1).equals("0")) {
            hours_selector.selectItemWithValue(db.get_LockTime(1));
        }

        if (db.get_openCounter(1) > 0) {

            counter_selector.selectItemWithValue(db.get_openCounter(1) + "_times");
        }


        Lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You would probably send these to your server for validation,
                // like: "http://example.com/api?size=" + selectedSize.getValue()
                // etc, but we'll just display a toast.
                if (db.get_Running(1).equals("N")) {
                    if (db.get_Selected(1) == 1) {
                        if (hours_selector.hasSelection()) {
                            new LovelyStandardDialog(Main.this)
                                    .setTopColorRes(R.color.blue)
                                    .setIcon(R.drawable.ic_lock)
                                    .setTitle(getString(R.string.lockBT_dialog_title))
                                    .setMessage(getString(R.string.lockBT_dialog_message))
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String toastMessage = "";
                                            if (hours_selector.hasSelection()) {
                                                if (db.get_Running(1).equals("N")) {
                                                    SwipeItem selected_hour = hours_selector.getSelectedItem();
                                                    h_value = selected_hour.getValue();
                                                    start_timer(h_value);
                                                    db.set_LockTime(h_value);
                                                    toastMessage += getString(R.string.hours) + selected_hour.getTitle();
                                                } else if (db.get_Running(1).equals("Y")) {
                                                    toastMessage += getString(R.string.toast_already_running);
                                                }
                                            } else {
                                                toastMessage += getString(R.string.toast_no_hour_selection);
                                            }

                                            if (counter_selector.hasSelection()) {
//                                                    if (db.get_openCounter(1) == 0) {
                                                SwipeItem selected_Counter = counter_selector.getSelectedItem();
                                                c_value = selected_Counter.getValue();
                                                switch (c_value) {
                                                    case "2_times":
                                                        db.set_openCounter(2);
                                                        toastMessage += getString(R.string.counter) + selected_Counter.getTitle();
                                                        break;
                                                    case "3_times":
                                                        db.set_openCounter(3);
                                                        toastMessage += getString(R.string.counter) + selected_Counter.getTitle();
                                                        break;
                                                    case "4_times":
                                                        db.set_openCounter(4);
                                                        toastMessage += getString(R.string.counter) + selected_Counter.getTitle();
                                                        break;
                                                    case "5_times":
                                                        db.set_openCounter(5);
                                                        toastMessage += getString(R.string.counter) + selected_Counter.getTitle();
                                                        break;
                                                }
//                                                    } else {
//                                                        toastMessage += "\nCounter already Selected, new Counter ignored.";
//                                                    }
                                            } else {
                                                toastMessage += getString(R.string.toast_no_counter_selection);
                                            }
                                            notification_update();
                                            CafeBar.builder(Main.this)
                                                    .duration(CafeBar.Duration.MEDIUM)
                                                    .content(toastMessage)
                                                    .maxLines(4)
                                                    .theme(CafeBarTheme.Custom(Color.parseColor("#1976D2")))
                                                    .show();

                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                }
                                            }, 2000);

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .show();
                        } else {
                            CafeBar.builder(Main.this)
                                    .duration(CafeBar.Duration.SHORT)
                                    .content(getString(R.string.cafebar_error))
                                    .maxLines(4)
                                    .theme(CafeBarTheme.Custom(Color.parseColor("#C62828")))
                                    .show();
                        }
                    } else if (db.get_Selected(1) == 0) {
                        CafeBar.builder(Main.this)
                                .duration(CafeBar.Duration.SHORT)
                                .content(getString(R.string.cafebar_error2))
                                .maxLines(4)
                                .theme(CafeBarTheme.Custom(Color.parseColor("#C62828")))
                                .show();
                    }
                } else if (db.get_Running(1).equals("Y")) {
                    CafeBar.builder(Main.this)
                            .duration(CafeBar.Duration.MEDIUM)
                            .content(getString(R.string.cafebar_error3))
                            .maxLines(4)
                            .theme(CafeBarTheme.Custom(Color.parseColor("#C62828")))
                            .show();
                }
            }
        });
    }

    public void start_timer(String hours) {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        date_time = simpleDateFormat.format(calendar.getTime());

        db.set_Data(date_time);
        db.set_Hours(hours.replaceAll("[\\D]", ""));
        db.set_LockTime(hours);
        db.set_Running("Y");

        Intent intent_service = new Intent(getApplicationContext(), Timer_Service.class);
        startService(intent_service);
    }

//    public void redirectStore(String updateUrl) {
//        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

    public boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode;
            assert appOpsManager != null;
            mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void isIgnoringBattery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(package_name)) {
                new LovelyStandardDialog(Main.this)
                        .setTopColorRes(R.color.blue)
                        .setButtonsColorRes(R.color.black)
                        .setIcon(R.drawable.ic_perm_device_information_white_48dp)
                        .setTitle(getString(R.string.battery_dialog_title))
                        .setMessage(getString(R.string.battery_dialog_message))
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//                                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                                //intent.setData(Uri.parse("package:" + package_name));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }

        }
    }

    public void ActionBarMethod() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    //Preselect for Pre-installed Apps db
    public void PreSelect() {
        if ((int) db.getAppsCount() != 0) {
            int count = (int) db.getAppsCount();
            for (int i = 1; i <= count; ++i) {
                preselectedApps.add(db.get_app(i).getS_id());
            }
        }
    }

    //GetApps, put List<String> if u uncomment return
    private List<String> getInstalledComponentList()
            throws PackageManager.NameNotFoundException {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
        List<String> componentList = new ArrayList<String>();
        String name;
        String pkg = "";
        int i = 0;
        for (ResolveInfo ri : ril) {
            if (ri.activityInfo != null) {
                Resources res = getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);
                if (ri.activityInfo.labelRes != 0) {
                    name = res.getString(ri.activityInfo.labelRes);
                } else {
                    name = ri.activityInfo.applicationInfo.loadLabel(
                            getPackageManager()).toString();
                }
                pkg = ri.activityInfo.packageName;
                componentList.add(pkg);
                i++;
                listOfApps.add(new MultiSelectModel(i, name));
            }
        }
        return componentList;
    }

    public void set_drawer() {

        //Drawer divider and item creator, this is called once only on every onCreate()
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_installed))
                        .setTextPrimary(getString(R.string.drawer_item1_text))
                        .setTextSecondary(getString(R.string.drawer_item1_text2))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, final long id, int position) {
                                //Checking if the lock is running or not
                                if (db.get_Running(1).equals("N")) {

                                    //Creating multi dialog
                                    multiSelectDialog = new MultiSelectDialog()
                                            .title(R.string.app_selector_title) //setting title for dialog
                                            .titleSize(20) //setting textSize
                                            .positiveText(getString(R.string.app_selector_apply)) //setting Submit text
                                            .negativeText(getString(R.string.app_selector_cancel)) //setting Cancel text
                                            .clearText(getString(R.string.app_selector_clear))
                                            .preSelectIDsList(preselectedApps) //List of ids that you need to be selected
                                            .multiSelectList(listOfApps) // the multi select model list with ids and name
                                            .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                                                @Override
                                                public void onDismiss(ArrayList<Integer> ids, String dataString) {
                                                    //Clear previous selections from DB and add the new ones
                                                    db.deleteAll();
                                                    if (ids.size() >= 1) {
                                                        //set_Selected means there is at least 1 item selected, set to true
                                                        db.set_Selected(1);
                                                    } else if (ids.size() < 1) {
                                                        //set to false
                                                        db.set_Selected(0);
                                                    }
                                                    //Adding IDs of selections and the respective PKG name
                                                    for (int i = 0; i < ids.size(); i++) {
                                                        db.add_apps(new apps(LS.get(ids.get(i) - 1)
                                                                , multiSelectDialog.Get_ID(listOfApps, ids.get(i) - 1)));
                                                        //Toast.makeText(Main.this,"Selected Ids : " + ids.get(i),Toast.LENGTH_SHORT).show();
                                                    }

                                                    //Showing result in a cafeBar
                                                    CafeBar.builder(Main.this)
                                                            .duration(CafeBar.Duration.SHORT)
                                                            .content(getString(R.string.selected_apps) + ids.size())
                                                            .maxLines(4)
                                                            .theme(CafeBarTheme.Custom(Color.parseColor("#1976D2")))
                                                            .show();
                                                }

                                                //onCancel do nothing
                                                @Override
                                                public void onCancel() {
                                                    Log.e("onCancel", "Dialog Dismissed without selection");
                                                }

                                                //I added this button to your library, this clears all
                                                //selections, tho, it needs an app restart to see it visually
                                                public void onClear() {
                                                    db.deleteAll();
                                                    db.set_Selected(0);
                                                    preselectedApps = new ArrayList<>();
                                                    CafeBar.builder(Main.this)
                                                            .duration(CafeBar.Duration.SHORT)
                                                            .content(getString(R.string.cafebar_error4))
                                                            .maxLines(4)
                                                            .theme(CafeBarTheme.Custom(Color.parseColor("#1976D2")))
                                                            .show();
                                                    Log.e("onClear", "Cleared Selections");
                                                }
                                            });
                                    multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
                                } else {
                                    CafeBar.builder(Main.this)
                                            .duration(CafeBar.Duration.MEDIUM)
                                            .content(getString(R.string.cafebar_error5))
                                            .maxLines(4)
                                            .theme(CafeBarTheme.Custom(Color.parseColor("#C62828")))
                                            .show();
                                }

                            }
                        })
        );

        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.baseline_save_white_48))
                        .setTextPrimary(getString(R.string.drawer_item6_text))
                        .setTextSecondary(getString(R.string.drawer_item6_text2))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                //Open selected_apps activity
                                Intent intent = new Intent(Main.this, selected_apps.class);
                                startActivity(intent);
                            }
                        })
        );

        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_help_outline_white_48dp))
                        .setTextPrimary(getString(R.string.drawer_item2_text))
                        .setTextSecondary(getString(R.string.drawer_item2_text2))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                //Toast.makeText(MainActivity.this, "Clicked first item #" + id, Toast.LENGTH_SHORT).show();
                                new LovelyCustomDialog(Main.this)
                                        .setTopColorRes(R.color.blue)
                                        .setTitle(getString(R.string.drawer_item2_dialog_title))
                                        .setMessage(getString(R.string.drawer_item2_dialog_message))
                                        .setIcon(R.drawable.ic_help_outline_white_48dp)
                                        //.configureView(/* ... */)
                                        //.setListener(R.id.ld_btn_yes, /* ... */)
                                        //.setInstanceStateManager(/* ... */)
                                        .show();
                            }
                        })
        );

        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.baseline_system_update_white_48))
                        .setTextPrimary(getString(R.string.drawer_item3_text))
                        .setTextSecondary(getString(R.string.drawer_item3_text2))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                new LovelyCustomDialog(Main.this)
                                        .setTopColorRes(R.color.blue)
                                        .setTitle(getString(R.string.drawer_item3_dialog_title))
                                        .setMessage(getString(R.string.changes))
                                        .setIcon(R.drawable.ic_fiber_new)
                                        //.configureView(/* ... */)
                                        //.setListener(R.id.ld_btn_yes, /* ... */)
                                        //.setInstanceStateManager(/* ... */)
                                        .show();
                                // demoRef.child("Recent Changes").setValue("Gibirish Text Here");
                            }
                        })
        );

        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_local_library))
                        .setTextPrimary(getString(R.string.drawer_item4_text))
                        .setTextSecondary(getString(R.string.drawer_item4_text2))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                libraries_used();
                                // demoRef.child("Recent Changes").setValue("Gibirish Text Here");

                            }
                        })
        );

        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_tag_faces))
                        .setTextPrimary(getString(R.string.drawer_item5_text))
                        .setTextSecondary(getString(R.string.drawer_item5_text2))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                if (aboutPage.getParent() != null)
                                    ((ViewGroup) aboutPage.getParent()).removeView(aboutPage);
                                new LovelyCustomDialog(Main.this)
                                        .setView(aboutPage)
                                        .setTopColorRes(R.color.blue)
                                        .setTitle(getString(R.string.drawer_item5_dialog_title))
                                        .setMessage(getString(R.string.drawer_item5_dialog_message))
                                        .setIcon(R.drawable.ic_tag_faces)
                                        //.configureView(/* ... */)
                                        //.setListener(R.id.ld_btn_yes, /* ... */)
                                        //.setInstanceStateManager(/* ... */)
                                        .show();
                                //this method bellow is related to firebase database
                                // demoRef.child("Recent Changes").setValue("Gibirish Text Here");

                            }
                        })

        );
        setDrawerTheme(
                new DrawerTheme(this)
                        .setBackgroundColorRes(R.color.colorAccent)
                        .setTextColorPrimaryRes(R.color.white)
                        .setTextColorSecondaryRes(R.color.grey)
                //.setTextColorPrimaryInverseRes(R.color.primary_text_inverse)
                //.setTextColorSecondaryInverseRes(R.color.secondary_text_inverse)
                //.setHighlightColorRes(R.color.highlight)
        );
    }

    public void libraries_used() {
        AttributionPresenter attributionPresenter = new AttributionPresenter.Builder(Main.this)
                .addAttributions(
                        new Attribution.Builder("Android-gif-drawable")
                                .addCopyrightNotice("Copyright (c) 2013 - present Karol Wrótniak, Droids on Roids")
                                .addLicense(License.MIT)
                                .setWebsite("https://github.com/koral--/android-gif-drawable")
                                .build()
                )
                .addAttributions(
                        new Attribution.Builder("Lovely Dialog")
                                .addCopyrightNotice("Copyright (c) 2016 Yaroslav Shevchuk")
                                .addLicense(License.APACHE)
                                .setWebsite("https://github.com/yarolegovich/LovelyDialog")
                                .build()
                )
                .addAttributions(
                        new Attribution.Builder("Material Drawer")
                                .addCopyrightNotice("Copyright (c) 2017 Jan Heinrich Reimer")
                                .addLicense(License.MIT)
                                .setWebsite("https://github.com/heinrichreimer/material-drawer")
                                .build()
                )
                .addAttributions(
                        new Attribution.Builder("AttributionPresenter")
                                .addCopyrightNotice("Copyright (c) 2017 Francisco José Montiel Navarro")
                                .addLicense(License.APACHE)
                                .setWebsite("https://github.com/franmontiel/AttributionPresenter")
                                .build()
                )
                .addAttributions(
                        new Attribution.Builder("SwipeSelector")
                                .addCopyrightNotice("Copyright (c) 2016 Iiro Krankka")
                                .addLicense(License.APACHE)
                                .setWebsite("https://github.com/roughike/SwipeSelector")
                                .build()
                )
                .addAttributions(
                        new Attribution.Builder("CafeBar")
                                .addCopyrightNotice("Copyright (c) 2017 Dani Mahardhika")
                                .addLicense(License.APACHE)
                                .setWebsite("https://github.com/danimahardhika/cafebar")
                                .build()
                )
                .addAttributions(
                        new Attribution.Builder("Android-About-Page")
                                .addCopyrightNotice("Copyright (c) 2016 Mehdi Sakout")
                                .addLicense(License.MIT)
                                .setWebsite("https://github.com/medyo/android-about-page")
                                .build()
                )
                .addAttributions(
                        new Attribution.Builder("Stetho")
                                .addCopyrightNotice("Copyright (c) 2015, Facebook, Inc.")
                                .addLicense(License.BSD_3)
                                .setWebsite("https://github.com/facebook/stetho")
                                .build()
                )
                .addAttributions(
                        new Attribution.Builder("Android-Multi-Select-Dialog")
                                .addCopyrightNotice("Copyright (c) 2017 Abubakker Moallim")
                                .addLicense(License.APACHE)
                                .setWebsite("https://github.com/abumoallim/Android-Multi-Select-Dialog")
                                .build()
                )


                .build();
        attributionPresenter.showDialog("Libraries Used");
    }

    public void notification_update() {
        Intent intent = new Intent(this, Main.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

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
                .setContentTitle(getString(R.string.notification_title1))
                .setContentText(getString(R.string.time_chosen) + db.get_Hours(1) + getString(R.string.minutes_hours) + ", " + getString(R.string.app_open1) + db.get_openCounter(1) + getString(R.string.open_times))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.time_chosen) + db.get_Hours(1) + getString(R.string.minutes_hours) + "\n" + getString(R.string.app_open2) + db.get_openCounter(1) + getString(R.string.open_times)))
                .setVibrate(new long[]{0, 500})
                //.setAutoCancel(true)
                .setContentIntent(pIntent);
        //.setOngoing(true)
        //.addAction(R.drawable.ic_clear_black_48dp, "Dismiss", dismissIntent);
        //.addAction(R.drawable.ic_action_boom, "Action!", someOtherPendingIntent);

        // Builds the notification and issues it.
        assert notificationManager != null;
        notificationManager.notify(313, builder.build());
    }

//    public void saveVersionNameAndCode(Context context) {
//        try {
//            PackageInfo packageInfo = context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(), 0);
//            int versionCode = packageInfo.versionCode;
//            String versionName = packageInfo.versionName;
//
//            Log.e("Version_Name_Save", "Saved N and C: " + versionName + " ||| " + versionCode);
//            db.EmptyTableVersion();
//            db.set_Vname_Vcode(versionName, versionCode);
//        } catch (Exception e) {
//        }
//    }

//    public boolean isAppUpdated(Context context) {
//        boolean result = false;
//        try {
//            PackageInfo packageInfo = context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(), 0);
//            int versionCode = packageInfo.versionCode;
//            String versionName = packageInfo.versionName;
//
//            String prevVersionName = db.get_Vname(1);
//            if (!prevVersionName.equals("") && !prevVersionName.equals(versionName)) {
//                Log.e("App_Update", "YES, V and C: " + versionName + " ||| " + versionCode);
//                result = true;
//                Answers.getInstance().logCustom(new CustomEvent("App_Updated")
//                        .putCustomAttribute("Updated?", "YES"));
//                saveVersionNameAndCode(this);
//                db.set_TimerFinish(1);
//                db.set_Running("N");
////                db.set_LockTime("");
//                db.set_Hours("");
//                db.set_Data("");
////                db.set_openCounter(0);
//                db.set_openTimes(0);
//            } else {
//                Log.e("App_Update", "NO, V and C: " + versionName + " ||| " + versionCode);
//            }
//
//        } catch (Exception e) {
//        }
//        return result;
//    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            super.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(Timer_Service.str_receiver));
        Log.e("Lock_Time", db.get_LockTime(1));
        Log.e("IsRunning?", db.get_Running(1));
        Log.e("Timer_Finish", String.valueOf(db.get_TimerFinish(1)));
        Log.e("Hours", "H: " + db.get_Hours(1));
        Log.e("Data", "D: " + db.get_Data(1));
        //Saved
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        Log.e("Lock_Time", db.get_LockTime(1));
        Log.e("IsRunning?", db.get_Running(1));
        Log.e("Timer_Finish", String.valueOf(db.get_TimerFinish(1)));
        Log.e("Hours", "H: " + db.get_Hours(1));
        Log.e("Data", "D: " + db.get_Data(1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
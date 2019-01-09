package com.nephi.getoffyourphone;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;

public class selected_apps extends AppCompatActivity {

    static Context appcontext1;
    //TextView
    TextView title;
    //Database
    DB_Helper db;
    //Variable to store app drawable in
    Drawable Aicon;
    //Variable to save version number of an app
    String version = "";
    String result;
    //Variable for app name
    String AppName = "";

    //App Icons array
    Drawable[] images;

    //App Names Array
    String[] name;

    //App Version Array
    String[] versionNumber;

    ListView lView;

    SelectedAppsAdapter lAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DefaultSettings.getTheme(this)) {
            //Change App Theme
            setTheme(R.style.AppTheme_Light);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_apps);

        //Does Everything
        init();
    }

    //Initialize OnCreate()
    public void init() {
        //TextView
        title = findViewById(R.id.selected_apps_title);

        //DataBase Handler
        db = new DB_Helper(this);
        appcontext1 = getApplicationContext();
        name = new String[(int) db.getAppsCount()];
        images = new Drawable[(int) db.getAppsCount()];
        versionNumber = new String[(int) db.getAppsCount()];
        title.setText(getString(R.string.custom_list_title) + "(" + String.valueOf(db.getAppsCount() + ")"));

        getIcons();
        getNames();
        getVersions();
        populate();
    }

    //Fill the ListView
    public void populate() {

        lView = (ListView) findViewById(R.id.selected_apps_list);

        lAdapter = new SelectedAppsAdapter(selected_apps.this, name, versionNumber, images);

        lView.setAdapter(lAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                //Toast.makeText(selected_apps.this, name[i]+" "+versionNumber[i], Toast.LENGTH_LONG).show();
                //Toast.makeText(selected_apps.this, "Stats coming soon", Toast.LENGTH_LONG).show();
                UStats.printCurrentUsageStatus(selected_apps.this, i);
                result = db.get_Usage(1);
                new LovelyStandardDialog(selected_apps.this)
                        .setTopColorRes(R.color.blue)
                        .setIcon(R.drawable.ic_perm_device_information_white_48dp)
                        .setTitle(name[i] + ":")
                        .setMessage(selected_apps.this.getString(R.string.usage_dialog_message) + result)
                        .setPositiveButton(selected_apps.this.getString(R.string.usage_dialog_bt1), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + db.get_app(i + 1).get_PKG()));
                                selected_apps.this.startActivity(intent);
                            }
                        })
                        .setNegativeButton(selected_apps.this.getString(R.string.usage_dialog_bt2), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openApp(selected_apps.this, db.get_app(i + 1).get_PKG());
                            }
                        })
                        .show();

//                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.setData(Uri.parse("package:" + db.get_app(i+1).get_PKG()));
//                startActivity(intent);
            }
        });
    }

    public boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new ActivityNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public Drawable[] getIcons() {
        int i;
        for (i = 0; i < images.length; i++) {
            try {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(db.get_app(i + 1).get_PKG(), PackageManager.GET_META_DATA);
                Aicon = applicationInfo.loadIcon(getPackageManager());
                images[i] = Aicon;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    public String[] getNames() {
        int i;
        for (i = 0; i < name.length; i++) {
            name[i] = db.get_app_PKG(i + 1);
            try {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(db.get_app(i + 1).get_PKG(), PackageManager.GET_META_DATA);
                AppName = applicationInfo.loadLabel(getPackageManager()).toString();
                name[i] = AppName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return name;
    }

    public String[] getVersions() {
        int i;
        for (i = 0; i < versionNumber.length; i++) {
            versionNumber[i] = db.get_app(i + 1).get_PKG();
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(db.get_app(i + 1).get_PKG(), PackageManager.GET_META_DATA);
                version = pInfo.versionName;
                versionNumber[i] = version;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionNumber;
    }
}

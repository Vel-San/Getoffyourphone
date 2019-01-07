package com.nephi.getoffyourphone;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.franmontiel.attributionpresenter.AttributionPresenter;
import com.franmontiel.attributionpresenter.entities.Attribution;
import com.franmontiel.attributionpresenter.entities.License;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        Fragment fragment = new SettingsHolder();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            transaction.add(R.id.settings_holder, fragment, "settings_screen");
        }

        transaction.commit();

    }


    // below inner class is a fragment, which must be called in the main activity
    public static class SettingsHolder extends PreferenceFragment {

        //Buttons
        Preference help;
        Preference recent_changes;
        Preference libraries;
        Preference about_me;
        //views
        View aboutPage;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Settings UI
            addPreferencesFromResource(R.xml.settings_ui);

            init();

        }

        public void init() {
            //Preference Buttons
            help = findPreference("help");
            recent_changes = findPreference("recent_changes");
            libraries = findPreference("libraries");
            about_me = findPreference("about_me");
            about_page();

            help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Show Help
                    new LovelyCustomDialog(getActivity())
                            .setTopColorRes(R.color.blue)
                            .setTitle(getString(R.string.drawer_item2_dialog_title))
                            .setMessage(getString(R.string.drawer_item2_dialog_message))
                            .setIcon(R.drawable.ic_help_outline_white_48dp)
                            //.configureView(/* ... */)
                            //.setListener(R.id.ld_btn_yes, /* ... */)
                            //.setInstanceStateManager(/* ... */)
                            .show();
                    return true;
                }
            });
            recent_changes.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Show Recent Changes
                    new LovelyCustomDialog(getActivity())
                            .setTopColorRes(R.color.blue)
                            .setTitle(getString(R.string.drawer_item3_dialog_title))
                            .setMessage(getString(R.string.changes))
                            .setIcon(R.drawable.ic_fiber_new)
                            //.configureView(/* ... */)
                            //.setListener(R.id.ld_btn_yes, /* ... */)
                            //.setInstanceStateManager(/* ... */)
                            .show();
                    return true;
                }
            });
            libraries.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Show Libraries
                    libraries_used();
                    return true;
                }
            });

            about_me.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Show AboutPage
                    if (aboutPage.getParent() != null)
                        ((ViewGroup) aboutPage.getParent()).removeView(aboutPage);
                    new LovelyCustomDialog(getActivity())
                            .setView(aboutPage)
                            .setTopColorRes(R.color.blue)
                            .setTitle(getString(R.string.drawer_item5_dialog_title))
                            .setMessage(getString(R.string.drawer_item5_dialog_message))
                            .setIcon(R.drawable.ic_tag_faces)
                            //.configureView(/* ... */)
                            //.setListener(R.id.ld_btn_yes, /* ... */)
                            //.setInstanceStateManager(/* ... */)
                            .show();
                    return true;
                }
            });

        }

        public void about_page() {
            //AboutPageView
            aboutPage = new AboutPage(this.getActivity())
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

        public void libraries_used() {
            AttributionPresenter attributionPresenter = new AttributionPresenter.Builder(this.getActivity())
                    .addAttributions(
                            new Attribution.Builder("Android-gif-drawable")
                                    .addCopyrightNotice("Copyright (c) 2013 - present Karol Wrótniak, Droids on Roids")
                                    .addLicense(License.MIT)
                                    .setWebsite("https://github.com/koral--/android-gif-drawable")
                                    .build()
                    )
                    .addAttributions(
                            new Attribution.Builder("Root Beer")
                                    .addCopyrightNotice("Copyright (c) 2015, Scott Alexander-Bown, Mat Rollings")
                                    .addLicense(License.APACHE)
                                    .setWebsite("https://github.com/scottyab/rootbeer")
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

    }
}

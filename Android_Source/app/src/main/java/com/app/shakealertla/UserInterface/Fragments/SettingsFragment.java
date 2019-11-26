package com.app.shakealertla.UserInterface.Fragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.app.shakealertla.R;
import com.app.shakealertla.ShakeAlertLA;
import com.app.shakealertla.UserInterface.Activities.HomeActivity;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.SharedPreferenceManager;

import java.util.Locale;

public class SettingsFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    ImageView language_popup, imgScreenshotDoNotDisturb;
    TextView language_textView;
    RadioButton english, spanish;
    RadioGroup radioGroup;
    Switch notificationSwitch;

    @Override
    public void initializeComponents(View rootView) {
        language_popup = rootView.findViewById(R.id.language_popup);
        imgScreenshotDoNotDisturb = rootView.findViewById(R.id.imgScreenshotDoNotDisturb);
        language_textView = rootView.findViewById(R.id.language_textView);
        radioGroup = rootView.findViewById(R.id.radioGroup);
        english = rootView.findViewById(R.id.english);
        spanish = rootView.findViewById(R.id.spanish);
        notificationSwitch = rootView.findViewById(R.id.notificationSwitch);
        notificationSwitch.setChecked(SharedPreferenceManager.shouldShowNotification());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//        radioGroup.clearCheck();
                if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_SPANISH)) {
                    spanish.setChecked(true);
                    imgScreenshotDoNotDisturb.setImageResource(R.mipmap.spanish_screenshot);
//            spanish.setChecked(true);
                } else {
                    english.setChecked(true);
//            english.setChecked(true);
                }
            }
        }, 500);

    }

    @Override
    public void doWorkOnVisible(View view) {
        super.doWorkOnVisible(view);
        if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_SPANISH))
            spanish.setChecked(true);
        else
            english.setChecked(true);
//            language_textView.setText("Español");
    }

    @Override
    public void setupListeners(View rootView) {
        language_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferenceManager.getInstance().setShowNotification(isChecked);
//                AppUtils.Toast("Updated");
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.english) {
                    if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_SPANISH)) {
                        SharedPreferenceManager.getInstance().setLanguage(ConfigConstants.LANGUAGE_ENGLISH);
//                    language_textView.setText("English");
                        /*changeLanguage(ShakeAlertLA.getContext(), "en");*/
//                        Locale locale = new Locale("en");
//                        Configuration config = ShakeAlertLA.getContext().getResources().getConfiguration();
//                        config.locale = locale;
//                        ShakeAlertLA.getContext().getResources().updateConfiguration(config, ShakeAlertLA.getContext().getResources().getDisplayMetrics());
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_ENGLISH)) {
                        SharedPreferenceManager.getInstance().setLanguage(ConfigConstants.LANGUAGE_SPANISH);
//                    language_textView.setText("Español");
                        /*changeLanguage(ShakeAlertLA.getContext(), ConfigConstants.LANGUAGE_SPANISH);*/
//                        Locale locale = new Locale(ConfigConstants.LANGUAGE_SPANISH);
//                        Configuration config = ShakeAlertLA.getContext().getResources().getConfiguration();
//                        config.locale = locale;
//                        ShakeAlertLA.getContext().getResources().updateConfiguration(config, ShakeAlertLA.getContext().getResources().getDisplayMetrics());
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                HomeActivity.pinpointManager = null;//force to restart
            }
        });
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.language_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.english: {
                if (SharedPreferenceManager.getLanguage() != null) {
                    SharedPreferenceManager.getInstance().setLanguage(null);
//                    language_textView.setText("English");
                    Locale locale = new Locale("en");
                    Configuration config = ShakeAlertLA.getContext().getResources().getConfiguration();
                    config.locale = locale;
                    ShakeAlertLA.getContext().getResources().updateConfiguration(config, ShakeAlertLA.getContext().getResources().getDisplayMetrics());
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                return true;
            }
            case R.id.spanish: {
                if (SharedPreferenceManager.getLanguage() == null) {
                    SharedPreferenceManager.getInstance().setLanguage(ConfigConstants.LANGUAGE_SPANISH);
//                    language_textView.setText("Español");
                    Locale locale = new Locale(ConfigConstants.LANGUAGE_SPANISH);
                    Configuration config = ShakeAlertLA.getContext().getResources().getConfiguration();
                    config.locale = locale;
                    ShakeAlertLA.getContext().getResources().updateConfiguration(config, ShakeAlertLA.getContext().getResources().getDisplayMetrics());
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                }
                return true;
            }
            default:
                return false;
        }
    }
}

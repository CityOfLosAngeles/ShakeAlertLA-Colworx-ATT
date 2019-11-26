package com.app.shakealertla.UserInterface.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.Window;

import com.app.shakealertla.Models.Earthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.Utils.SharedPreferenceManager;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        Window window = getWindow();
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintColor(getResources().getColor(R.color.transparent));
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
        setupComponents();
    }

    @Override
    public void initializeComponents() {
        // TODO: For Localization: https://code.tutsplus.com/tutorials/how-to-localize-an-android-application--cms-22154
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Delay for splash Screen
                Intent tourIntent = new Intent(SplashActivity.this, SharedPreferenceManager.shouldShowTour() ? TourActivity.class : HomeActivity.class);
                tourIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(tourIntent);
            }
        }, 500);
    }

    @Override
    public void setupListeners() {
//        Earthquakes earthquakes = new Earthquakes(R.mipmap.red_pin, "Earthquake! Earthquake!", "Expect severe shaking, Drop, cover, hold something", "6 Miles NE from Aguanga, CA", "Magnitude: 6.4", "Aug, 24th, 11:15PM");
//        sendNotification(earthquakes);
//        MediaPlayer mMediaPlayer = new MediaPlayer();
//        mMediaPlayer = MediaPlayer.create(this, R.raw.alert);
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setLooping(true);  // Set false if you don't want it to loop
//        mMediaPlayer.start();
    }
//
//    private void sendNotification(Earthquakes messageBody) {
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
////        String channelId = getString(R.string.default_notification_channel_id);
//        String channelId = "Location";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.city_of_la_logo)
//                        .setContentTitle(messageBody.text)
////                        .setColor(getResources().getColor(R.color.colorPrimary))
//                        .setContentText(messageBody.desc)
//                        .setAutoCancel(true)
////                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//        MediaPlayer mMediaPlayer = new MediaPlayer();
//        mMediaPlayer = MediaPlayer.create(this, R.raw.facebook_ringtone_pop);
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setLooping(false);  // Set false if you don't want it to loop
//        mMediaPlayer.start();
//    }
}

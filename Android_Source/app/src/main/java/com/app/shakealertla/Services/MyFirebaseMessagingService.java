package com.app.shakealertla.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.shakealertla.Models.Earthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.UserInterface.Activities.BaseActivity;
import com.app.shakealertla.UserInterface.Activities.HomeActivity;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.GsonUtils;
import com.app.shakealertla.Utils.SharedPreferenceManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import com.firebase.jobdispatcher.Constraint;
//import com.firebase.jobdispatcher.FirebaseJobDispatcher;
//import com.firebase.jobdispatcher.GooglePlayDriver;
//import com.firebase.jobdispatcher.Job;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //remoteMessage.getData()-->{type=0, missionId=-LETwMA34c6xjGymYAnd}

//
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//
//        }

        // Check if message contains a notification payload.

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//        sendNotification(remoteMessage.getNotification().getTitle());

//        pinpoint.jsonBody : {"Category":"test","Likelihood":"1.0000","MMI":"5.0000","LatitudeUnit":"deg","TimeStamp":"test","MagnitudeUnit":"Mw","LatitudeValue":"34.4160","Type":"test","EventOriginTimeStampUnit":"UTC","MessageOriginSystem":"test","DepthUnit":"km","MagnitudeValue":"6.6000","DepthValue":"8.4000","EventOriginTimeStampValue":"2018-11-26T11:16:43.000Z","startTime":"1543231008469","LongitudeValue":"-118.3700","Topic":"7","Polygon":["34.8805,-118.3700","34.7438,-117.9703","34.4147,-117.8069","34.0869,-117.9734","33.9515,-118.3700","34.0869,-118.7666","34.4147,-118.9331","34.7438,-118.7697","34.8805,-118.3700"],"LongitudeUnit":"deg"}
//        pinpoint.openApp : "true"
//        pinpoint.campaign.treatment_id : "0"
//        pinpoint.notification.title : "TITLE"
//        pinpoint.notification.body : "BODY"
//        pinpoint.campaign.campaign_id : "aa8dbbf45ebe494890c7624f9c442d8e"
//        pinpoint.notification.silentPush : "0"
//        pinpoint.campaign.campaign_activity_id :361812c1a9dc4d9dbbd78c4d9470f31d

        // Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().toString());
            Earthquakes earthquake = GsonUtils.fromJSON(remoteMessage.getData().get("pinpoint.jsonBody"), Earthquakes.class);
            earthquake.title = remoteMessage.getData().get("pinpoint.notification.title");
            earthquake.body = remoteMessage.getData().get("pinpoint.notification.body");
//            RecentEarthquakeService.addEarthQuake(earthquake);//Add in database // Commented By Shahzor
            sendNotification(earthquake);

        }


    }
    // [END receive_message]

//    /**
//     * Schedule a job using FirebaseJobDispatcher.
//     */
//    private void scheduleJob() {
//        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
//        // [END dispatch_job]
//    }
//
//    /**
//     * Handle time allotted to BroadcastReceivers.
//     */
//    private void handleNow() {
//        Log.d(TAG, "Short lived task is done.");
//    }

    LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * <p>
     * //     * @param messageBody FCM message body received.
     */
    private void sendNotification(Earthquakes earthquakes) {
        String title = earthquakes.title;
        String body = earthquakes.body;
        sendResult(title, body, earthquakes);
        if (BaseActivity.isVisible)
            return;
        if (SharedPreferenceManager.shouldShowNotification()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("alert", body);
            intent.putExtra("payload", earthquakes);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

//        String channelId = getString(R.string.default_notification_channel_id);
            String channelId = "Sendbird";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.app_icon)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
//                            .setSound(defaultSoundUri)
                            .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert))
                            .setContentIntent(pendingIntent);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.drawable.app_icon);
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            } else {
                notificationBuilder.setSmallIcon(R.drawable.app_icon);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "ShakeAlert Notification",
                        NotificationManager.IMPORTANCE_HIGH);
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();

//                channel.setDescription(msg);
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert),attributes);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(createID() /* ID of notification */, notificationBuilder.build());
//            MediaPlayer mMediaPlayer = new MediaPlayer();
//            mMediaPlayer = MediaPlayer.create(this, R.raw.alert);
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setLooping(false);  // Set false if you don't want it to loop
//            mMediaPlayer.start();
        }
    }

    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }
//    private int getNotificationIcon() {
//        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
//        return useWhiteIcon ? R.drawable.icon_silhouette : R.drawable.city_of_la_logo;
//    }

    /*==========Function to send Data to Activity===================*/
    static final public String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";
    static final public String COPA_TITLE = "com.controlj.copame.backend.COPAService.COPA_TITLE";
    static final public String COPA_MESSAGE = "com.controlj.copame.backend.COPAService.COPA_MSG";
    static final public String COPA_PAYLOAD = "com.controlj.copame.backend.COPAService.COPA_PAYLOAD";

    public void sendResult(String title, String message, Earthquakes earthquakes) {
        Intent intent = new Intent(COPA_RESULT);
        if (title != null && message != null) {
            intent.putExtra(COPA_TITLE, title);
            intent.putExtra(COPA_MESSAGE, message);
            intent.putExtra(COPA_PAYLOAD, earthquakes.toJSON(earthquakes));
            broadcaster.sendBroadcast(intent);
        } else AppUtils.Toast("Receriver: Null Object");
    }
}
package com.app.shakealertla.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        // Colworx : Below line is for refresh device token and use anywhere directly in code for get refreshed device token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
//    public static void sendRegistrationToServer(String token, String UserID) {
//
//        FirebaseDatabase.getInstance().getReference().child("Employer").child(UserID).child("employer_NotificationToken").setValue(token, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if (databaseError != null) {
//                    AppLog.e(getClass().getSimpleName(),"Data could not be saved " + databaseError.getMessage());
//                } else {
//                    AppLog.i(getClass().getSimpleName(), " Token updated");
//                }
//
//            }
//        });
//    }
//
//    public static void sendRegistrationToSendbirdServer(final String token) {
//        SendBird.registerPushTokenForCurrentUser(token, new SendBird.RegisterPushTokenWithStatusHandler() {
//            @Override
//            public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
//                if (e != null) {
//                    AppUtils.Toast("" + e.getCode() + ":" + e.getMessage());
//                    return;
//                }
//
//                if (pushTokenRegistrationStatus == SendBird.PushTokenRegistrationStatus.PENDING) {
//                    AppUtils.Toast("Connection required to register push token.");
//                }
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("sendBirdID",SendBird.getCurrentUser().getUserId());
//                map.put("sendBirdNick",SendBird.getCurrentUser().getNickname());
//                FirebaseDatabase.getInstance().getReference().child("Employer").child(FireBaseHelper.getUID()).updateChildren(map);
//            }
//        });
//    }
}
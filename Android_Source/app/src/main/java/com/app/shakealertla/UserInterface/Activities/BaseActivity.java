package com.app.shakealertla.UserInterface.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.app.shakealertla.Listener.NavigationRequestListener;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.Earthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.MyFirebaseMessagingService;
import com.app.shakealertla.Services.RecentEarthquakeService;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.ContextWrapper;
import com.app.shakealertla.Utils.FragmentUtils;
import com.app.shakealertla.Utils.GsonUtils;
import com.app.shakealertla.Utils.SharedPreferenceManager;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity implements NavigationRequestListener {

    private static final String TAG = "BaseActivity";
    Fragment currentFragment;
    public static boolean isVisible = false;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.layout_test);*/
//        Window window = this.getWindow();
//        Drawable background = this.getResources().getDrawable(R.drawable.main_gradient_background);
//        window.setBackgroundDrawable(background);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra(MyFirebaseMessagingService.COPA_TITLE);
                String message = intent.getStringExtra(MyFirebaseMessagingService.COPA_MESSAGE);
                Earthquakes earthquakes = GsonUtils.fromJSON(intent.getStringExtra(MyFirebaseMessagingService.COPA_PAYLOAD),Earthquakes.class);
                // do something here.
//                title = getString(R.string.earthquake_earthquake);
//                message = getString(R.string.earthquake_is_happening);
                showDialog(title, message, earthquakes);
                AppLog.d("Push Rate Segment ID: ", earthquakes.SegmentID);
                RecentEarthquakeService.setPushRate(earthquakes.SegmentID, new ServiceListener<String, String>() {
                    @Override
                    public void success(String success) {
                        AppLog.d("Push Rate: " ,success);
                    }

                    @Override
                    public void error(String error) {
                        AppLog.d("Push Rate: ", error);
                    }
                });
            }
        };
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                AppLog.d("BaseActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    //This method will hide keyboard when you click outside the EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }
    @Override
    public void onReplaceFragment(int containerId, Fragment fragment, boolean addToBackStack) {
        currentFragment = fragment;
        FragmentUtils.commitFragment(getSupportFragmentManager(), containerId, fragment, addToBackStack);
    }

    @Override
    public void onAddFragment(int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentUtils.addFragment(getSupportFragmentManager(), containerId, fragment, addToBackStack);
    }

    @Override
    public void onStartActivity(Intent intent) {
        startActivity(intent);
        onStartNewActivity();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        onStartNewActivity();
    }

    @Override
    public void onGoBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onLeaveThisActivity();
    }

    @Override
    public void finish() {
        super.finish();
        //override transition to skip the standard window transition
        onLeaveThisActivity();
    }

    protected void onStartNewActivity() {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    protected void onLeaveThisActivity() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    public void setupComponents() {

        initializeComponents();
        setupListeners();
    }

    public abstract void initializeComponents();

    public abstract void setupListeners();

    public void showDialog(/*Activity activity,*/ String title, final CharSequence message, final Earthquakes earthquakes) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (title != null)
            builder.setTitle(earthquakes.title);

        builder.setMessage(earthquakes.body);
        builder.setPositiveButton(R.string.view_on_map, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BaseActivity.this, HomeActivity.class);
                intent.putExtra("alert", message.toString());
                intent.putExtra("payload",earthquakes);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.dismiss, null);
        builder.setCancelable(false);
        builder.show();//todo Disable for testing
    }

    /**
     * To change Language Use ContextWrapper.wrap function
     * get Selected language from Prefs.
     */

    @Override
    protected void attachBaseContext(Context newBase) {

        Locale newLocale = new Locale(SharedPreferenceManager.getLanguage());
        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(MyFirebaseMessagingService.COPA_RESULT)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }
}

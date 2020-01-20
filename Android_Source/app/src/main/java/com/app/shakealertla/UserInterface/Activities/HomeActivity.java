package com.app.shakealertla.UserInterface.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.pinpoint.internal.core.PinpointContext;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfile;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfileLocation;
import com.app.shakealertla.Adapters.HomeTabstPagerAdapter;
import com.app.shakealertla.Listener.Callback;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.Earthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.RecentEarthquakeService;
import com.app.shakealertla.UserInterface.Fragments.Emergency_MapFragment;
import com.app.shakealertla.UserInterface.Fragments.HomeFragment;
import com.app.shakealertla.UserInterface.Fragments.RecoveryFragment;
import com.app.shakealertla.UserInterface.Fragments.SettingsFragment;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.SharedPreferenceManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
//        Earthquakes earthquakes = new Earthquakes();
//        earthquakes.title = "¡Terremoto! ¡Terremoto!";
//        earthquakes.body = "Espere fuerte sacudida, agáchese, cúbrase, sujétase . ¡Protéjase ahora!";
//        showDialog("¡Terremoto! ¡Terremoto!","Espere fuerte sacudida, agáchese, cúbrase, sujétase . ¡Protéjase ahora!",earthquakes);
//        Earthquakes earthquakes = new Earthquakes();
//        earthquakes.title = "Earthquake! Earthquake!";
//        earthquakes.body = "Expect strong shaking. Drop, cover, and hold on. Protect yourself now!";
//        showDialog("Earthquake! Earthquake!","Expect strong shaking. Drop, cover, and hold on. Protect yourself now!",earthquakes);/
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (HomeActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (checkLocationPermission()) {
                initAWS();
                setupComponents();
            }
        } else {
            initAWS();
            setupComponents();
        }
    }

    private AWSCredentialsProvider credentialsProvider;
    private AWSConfiguration configuration;
    String LOG_TAG = this.getClass().getSimpleName();
    public static PinpointManager pinpointManager = null;
    String deviceToken;

    /**
     * Colworx : AWS Configuration
     */
    private void initAWS() {
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                // Obtain the reference to the AWSCredentialsProvider and AWSConfiguration objects
                credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
                configuration = AWSMobileClient.getInstance().getConfiguration();

                // Use IdentityManager#getUserID to fetch the identity id.
                IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
                    @Override
                    public void onIdentityId(String identityId) {
                        AppLog.d("HomeActivity", "Identity ID = " + identityId);

                        // Use IdentityManager#getCachedUserID to
                        //  fetch the locally cached identity id.
                        final String cachedIdentityId =
                                IdentityManager.getDefaultIdentityManager().getCachedUserID();
//                        AppUtils.Toast(cachedIdentityId);

                    }

                    @Override
                    public void handleError(Exception exception) {
                        AppLog.d("HomeActivity", "Error in retrieving the identity" + exception);
                    }
                });
            }
        }).execute();


        if (pinpointManager == null) {
            final PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    getApplicationContext(),
                    AWSMobileClient.getInstance().getCredentialsProvider(),
                    AWSMobileClient.getInstance().getConfiguration());
            pinpointManager = new PinpointManager(pinpointConfig);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        deviceToken = FirebaseInstanceId.getInstance().getToken();
//                                InstanceID.getInstance(HomeActivity.this).getToken(
//                                        "855957751218",
//                                        GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                        AppLog.e("Device Token", deviceToken);
                        pinpointManager.getNotificationClient().registerDeviceToken(deviceToken);
//                        pinpointManager.getSessionClient().startSession();
//                        logEvent();
                                //.registerGCMDeviceToken(deviceToken);
                        if (!SmartLocation.with(HomeActivity.this).location().state().isGpsAvailable())
                            GpsLocationpopup();
                        else
                            getLocation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void GpsLocationpopup() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        AppLog.i(LOG_TAG, "All location settings are satisfied.");
                        AppUtils.Toast("Enabled");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        AppLog.i(LOG_TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(HomeActivity.this, 101);
                        } catch (IntentSender.SendIntentException e) {
                            AppLog.i(LOG_TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        AppLog.i(LOG_TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        AppUtils.Toast("Unavailable");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 101:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
//                        AppUtils.Toast(states.isLocationPresent() + "");
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
//                        AppUtils.Toast("Canceled");
                        finish();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void getLocation() {
//        RecentEarthquakeService.RegisterDevice(deviceToken, 123, 123, new ServiceListener<String, String>() {
//            @Override
//            public void success(String segmentID) {
////                        final PinpointManager pinpoint = Application.pinpointManager;
//                final List<String> interestsLists = Arrays.asList(segmentID);
//                EndpointProfile endpointProfile = pinpointManager.getTargetingClient().currentEndpoint();
//                endpointProfile.addAttribute("BlockIndex", interestsLists);
//                pinpointManager.getTargetingClient().updateEndpointProfile(endpointProfile);
//            }
//
//            @Override
//            public void error(String error) {
//                AppUtils.Toast(error);
//            }
//        });
        startLocationListener(HomeActivity.this, new Callback<LatLng>() {
            @Override
            public void callback(final LatLng latLng) {
                double latitude = SharedPreferenceManager.getLatitude();
                double longitude = SharedPreferenceManager.getLongitude();
                double fiveMiles = 8.04672;
                latitude = 0;///////////////////////////////////////////////////////////////////
                if (latitude == 0 || AppUtils.distanceInKM(latitude, longitude, latLng.latitude, latLng.longitude) > fiveMiles) {
                    RecentEarthquakeService.RegisterDevice(deviceToken, latLng.latitude, latLng.longitude, new ServiceListener<String, String>() {
                        @Override
                        public void success(String segmentID) {
//                        final PinpointManager pinpoint = Application.pinpointManager;
                            AppLog.e("Segment ID: ",segmentID);

//                            AppLog.e("Segment EndpointProfile: ",endpointProfile.toString());
                            final List<String> interestsLists = Arrays.asList(segmentID);
                            final List<String> removeInterestsLists = Arrays.asList("0");
                            if (pinpointManager!=null) {
                                EndpointProfile endpointProfile = pinpointManager.getTargetingClient().currentEndpoint();
                                EndpointProfileLocation endpointProfileLocation = new EndpointProfileLocation(pinpointManager.getPinpointContext());
                                endpointProfileLocation.setCountry("USA");
                                endpointProfile.setLocation(endpointProfileLocation);
                                if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_ENGLISH)) {
                                    endpointProfile.addAttribute("Block-EN-", interestsLists);
                                    endpointProfile.addAttribute("Block-ES-", removeInterestsLists);
                                } else {
                                    endpointProfile.addAttribute("Block-EN-", removeInterestsLists);
                                    endpointProfile.addAttribute("Block-ES-", interestsLists);
                                }
                                endpointProfile.getDemographic().setLocale(new Locale(endpointProfile.getDemographic().getLocale().getLanguage(), "USA"));
                                pinpointManager.getTargetingClient().updateEndpointProfile(endpointProfile);
                            }
                            //Saving Location to Prefs
                            SharedPreferenceManager.getInstance().saveLocation(latLng.latitude, latLng.longitude);
                        }

                        @Override
                        public void error(String error) {
//                            AppUtils.Toast(error);
                        }
                    });
                }
            }
        });

    }

    //    TabLayout tabLayout;
    BottomNavigationViewEx navigation;
    public ViewPager viewPager;
    View ticker, toolbar;

    //    TextView title;
    String Screens[];

    @Override
    public void initializeComponents() {
        Screens = getResources().getStringArray(R.array.screens);
        ticker = findViewById(R.id.ticker);

//        toolbar = findViewById(R.id.toolbar);
//        title = findViewById(R.id.title);

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment());
        Emergency_MapFragment emergency_mapFragment = new Emergency_MapFragment();
        fragments.add(emergency_mapFragment);
        fragments.add(new RecoveryFragment());
        fragments.add(new SettingsFragment());
        if (getIntent().hasExtra("alert")) {
            Bundle bundle = new Bundle();
            bundle.putString("alert", getIntent().getStringExtra("alert"));
            Earthquakes earthquakes = (Earthquakes) getIntent().getSerializableExtra("payload");
            bundle.putSerializable("payload", earthquakes);
            emergency_mapFragment.setArguments(bundle);
            AppLog.d("Push Rate Segment ID: ", earthquakes.SegmentID);
            RecentEarthquakeService.setPushRate(earthquakes.SegmentID, new ServiceListener<String, String>() {
                @Override
                public void success(String success) {
                    AppLog.d("Push Rate: ", success);
                }

                @Override
                public void error(String error) {
                    AppLog.d("Push Rate: ", error);
                }
            });
        }
        // Get the ViewPager and set it's PagerAdapter so that it can display items;
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new HomeTabstPagerAdapter(getSupportFragmentManager(), HomeActivity.this, fragments));

        // TODO: TABLAYOUT LINK https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout#add-icons-to-tablayout
        // Give the TabLayout the ViewPager
//        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);

        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setupWithViewPager(viewPager);
        navigation.setTextSize(11f);
        ticker.setVisibility(View.GONE);

        if (getIntent().hasExtra("alert")) {
            viewPager.setCurrentItem(1);
            navigation.setBackgroundColor(getResources().getColor(R.color.darkColorPrimaryDark));
            ticker.setBackgroundColor(getResources().getColor(R.color.darkColorPrimaryDark));
            ticker.setVisibility(View.VISIBLE);
//            toolbar.setBackgroundColor(getResources().getColor(R.color.darkColorPrimary));
//            title.setText(Screens[1]);
        }

    }

    @Override
    public void setupListeners() {
//        showDialog(getString(R.string.earthquake_earthquake),getString(R.string.earthquake_is_happening));
        // configure icons
        final int[] imageResId = {
                R.mipmap.un_select_home,
                R.mipmap.un_select_emergency,
                R.mipmap.un_select_recovery,
                R.mipmap.un_select_settings};

        final int[] selectedResId = {
                R.mipmap.home,
                R.mipmap.emergency,
                R.mipmap.recovery,
                R.mipmap.settings};

//        for (int i = 0; i < imageResId.length; i++) {
//            tabLayout.getTabAt(i).setIcon(imageResId[i]);
//        }

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                for (int i = 0; i < imageResId.length; i++) {
//                    tabLayout.getTabAt(i).setIcon(imageResId[i]);
//                }
//                tab.setIcon(selectedResId[tab.getPosition()]);
//                if (tab.getPosition() == 1) {
//                    tabLayout.setBackgroundColor(getResources().getColor(R.color.darkColorPrimary));
//                    ticker.setBackgroundColor(getResources().getColor(R.color.darkColorPrimaryDark));
//                    toolbar.setBackgroundColor(getResources().getColor(R.color.darkColorPrimary));
//                    title.setText(Screens[tab.getPosition()]);
//                } else {
//                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    ticker.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    title.setText(Screens[tab.getPosition()]);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

//        mTextMessage = (TextView) findViewById(R.id.message);

    }

    private void startLocationListener(Context context, final Callback<LatLng> callback) {

        long mLocTrackingInterval = 1000 * 10;
        float trackingDistance = 15;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
//                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);

        SmartLocation.with(context)
                .location().oneFix()
                .config(LocationParams.BEST_EFFORT)
//                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {

                        /**
                         * Colworx : Hardcoded Location values (latitude and longitude) when Testing Notification or Register device in LA location
                         * callback.callback(new LatLng(34.052235, -118.243683));
                         * Colworx : Current Location for Pro or not for testing
                         */
                        callback.callback(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                });
    }

//    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ColorStateList colorNormal = ColorStateList.valueOf(getResources().getColor(R.color.grey_300));
            ColorStateList colorActive = ColorStateList.valueOf(getResources().getColor(R.color.white));
            ColorStateList colorEmergency = ColorStateList.valueOf(getResources().getColor(R.color.darkColorPrimary));
//            for (int i = 0; i < navigation.getItemCount(); i++) {
//                    tabLayout.getTabAt(i).setIcon(imageResId[i]);
//                navigation.getIconAt(i).setImageTintList(colorNormal);
//            }
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navigation.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    ticker.setBackgroundColor(getResources().getColor(R.color.transparent));
                    ticker.setVisibility(View.GONE);
//                    navigation.getIconAt(0).setImageTintList(colorActive);
                    return true;
                case R.id.navigation_quakemap:
                    navigation.setBackgroundColor(getResources().getColor(R.color.darkColorPrimaryDark));
                    ticker.setBackgroundColor(getResources().getColor(R.color.darkColorPrimaryDark));
                    ticker.setVisibility(View.VISIBLE);
//                    navigation.getIconAt(1).setImageTintList(colorEmergency);
                    return true;
                case R.id.navigation_recovery:
                    navigation.setBackgroundColor(getResources().getColor(R.color.recoverycolorPrimary));
                    ticker.setBackgroundColor(getResources().getColor(R.color.recoverycolorPrimary));
                    ticker.setVisibility(View.VISIBLE);
//                    navigation.getIconAt(2).setImageTintList(colorActive);
                    return true;
                case R.id.navigation_settings:
                    navigation.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    ticker.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    ticker.setVisibility(View.VISIBLE);
//                    navigation.getIconAt(3).setImageTintList(colorActive);
                    return true;
            }
            return false;
        }
    };


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /**
     * Colworx : Check Location Permission
     */
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else return true;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    initAWS();
                    setupComponents();
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                AppUtils.Toast("permission denied");
                finish();
            }
        }
    }

    public void logEvent() {
        final AnalyticsEvent event =
                pinpointManager.getAnalyticsClient().createEvent("EventName")
                        .withAttribute("DemoAttribute1", "DemoAttributeValue1")
                        .withAttribute("DemoAttribute2", "DemoAttributeValue2")
                        .withMetric("DemoMetric1", Math.random());

        pinpointManager.getAnalyticsClient().recordEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        pinpointManager.getSessionClient().stopSession();
//        pinpointManager.getAnalyticsClient().submitEvents();
    }
}

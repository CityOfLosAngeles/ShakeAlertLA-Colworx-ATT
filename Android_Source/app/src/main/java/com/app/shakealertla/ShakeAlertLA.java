package com.app.shakealertla;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.android.volley.toolbox.HurlStack;
import com.app.shakealertla.HttpUtils.HttpRequestHandler;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.SharedPreferenceManager;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
//import android.support.multidex.MultiDex;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
//import com.twitter.sdk.android.Twitter;
//import com.twitter.sdk.android.core.TwitterAuthConfig;
//import io.fabric.sdk.android.Fabric;


public class ShakeAlertLA extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "62UstOrcpGR1U2WlMGszbepZB";
//    private static final String TWITTER_SECRET = "AjPVSiK7wnwifj4Kuz40YdrqZHkGD9fZAJEW4HAIQCF46Ae2NB";


    public static final String TAG = ShakeAlertLA.class.getSimpleName();

    private static Context context;
    private static ShakeAlertLA mInstance;
    public String systemLangugage;

    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(this);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));
        mInstance = this;
        context = this.getApplicationContext();
        systemLangugage = Resources.getSystem().getConfiguration().locale.getLanguage();
        if (!(systemLangugage.matches(ConfigConstants.LANGUAGE_ENGLISH) || systemLangugage.matches(ConfigConstants.LANGUAGE_SPANISH))) {
            systemLangugage = ConfigConstants.LANGUAGE_ENGLISH;
            SharedPreferenceManager.getInstance().setLanguage(systemLangugage);
        } else {//System language is english or spanish
            if (SharedPreferenceManager.getLanguage() == null)
                SharedPreferenceManager.getInstance().setLanguage(systemLangugage);
        }
        if (SharedPreferenceManager.getLanguage() != null) {
            Locale locale = new Locale(SharedPreferenceManager.getLanguage());
            Configuration config = context.getResources().getConfiguration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
//        FacebookSdk.sdkInitialize(context);
//        AppEventsLogger.activateApp(this);
//        getCertificates();
        handleSSLHandshake();
        HttpRequestHandler.setAndroidContext(this);

        //Overriding default fonts
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "Helvetica45Default.ttf");
//        FontsOverride.setDefaultFont(this, "SERIF", "Helvetica47.ttf");
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", "HelveticaNeue-CondensedBold.otf");

//        catchAllUncaughtException();
    }

    public static synchronized ShakeAlertLA getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return context;
    }




    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
/*
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    if (arg0.equalsIgnoreCase("earthquake.usgs.gov") ||
                            arg0.equalsIgnoreCase("services7.arcgis.com") ||
                            arg0.equalsIgnoreCase("shakealertlaapi.com") ||
                            arg0.equalsIgnoreCase("shakealert-la.firebaseio.com")) {
                        return true;
                    } else {
                        return false;
                    }


//                    return true;
                }
            });
*/
        } catch (Exception ignored) {
        }
    }
}

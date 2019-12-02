package com.app.shakealertla.UserInterface.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shakealertla.Models.Earthquakes;
import com.app.shakealertla.Models.RecentEarthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.ShelterService;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.SharedPreferenceManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EarthquakeDetailsActivity extends BaseActivity {

    Earthquakes earthquake;
    RecentEarthquakes recentEarthquake; // Worked By Shahzor
    String comesFrom = ""; // Worked By Shahzor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_details);
        if (getIntent().hasExtra("earthquake")){
            earthquake = (Earthquakes) getIntent().getSerializableExtra("earthquake");
        }else if(getIntent().hasExtra("recentearthquake")){
            recentEarthquake = (RecentEarthquakes) getIntent().getSerializableExtra("recentearthquake");
            comesFrom = getIntent().getStringExtra("ComesFrom"); // Worked By Shahzor
        }
        setupComponents();
    }

    ImageView back;
    TextView titleTV;
    TextView magnitude, time, location, latitude, longitude,intensity;
    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        titleTV = findViewById(R.id.title);
        if(comesFrom.equals("")){
            titleTV.setText(earthquake.body);
        }
        magnitude=findViewById(R.id.magnitude);
        time=findViewById(R.id.time);
        location=findViewById(R.id.location);
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        intensity=findViewById(R.id.intensity);
    }

    @Override
    public void setupListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Worked By Shahzor Start
        if(!comesFrom.equals("")){
            location.setText(recentEarthquake.Topic);
            magnitude.setText(""+recentEarthquake.getMagnitude());

            Date convertDateToUTC = dateToUTC(new Date(Long.valueOf(recentEarthquake.startTime)));
//            time.setText(AppUtils.formatDate("MMM dd, yyyy, kk:mm:ss", convertDateToUTC)+ " (UTC)");

            long convertDateToUTCInLong = convertDateToUTC.getTime();

            // If date shown in PST format
            TimeZone pacificTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
            long apiResponseTime = new Date(convertDateToUTCInLong).getTime();
            long convertTimeToPST = apiResponseTime + pacificTimeZone.getOffset(apiResponseTime);
            Date pstDate = new Date(Long.valueOf(convertTimeToPST));
            time.setText(AppUtils.formatDate("MMM dd, yyyy, hh:mm:ss a", pstDate) +" (PST)");

            latitude.setText(recentEarthquake.LatitudeValue);
            longitude.setText(recentEarthquake.LongitudeValue);

        }else{
            magnitude.setText(""+earthquake.getMagnitude());
            time.setText(AppUtils.formatDate("MMM dd, yyyy, hh:mm a", new Date(Long.valueOf(earthquake.startTime))));
            location.setText(ShelterService.getLocation(EarthquakeDetailsActivity.this, new LatLng(Double.valueOf(earthquake.LatitudeValue), Double.valueOf(earthquake.LongitudeValue))));
            latitude.setText(earthquake.LatitudeValue);
            longitude.setText(earthquake.LongitudeValue);
        }
        // Worked By Shahzor End

//        magnitude.setText(""+earthquake.getMagnitude()); // Commented By Shahzor
//        time.setText(AppUtils.formatDate("MMM dd, yyyy, hh:mm a", new Date(Long.valueOf(earthquake.startTime)))); // Commented By Shahzor
//        location.setText(ShelterService.getLocation(EarthquakeDetailsActivity.this, new LatLng(Double.valueOf(earthquake.LatitudeValue), Double.valueOf(earthquake.LongitudeValue)))); // Commented By Shahzor
//        latitude.setText(earthquake.LatitudeValue); // Commented By Shahzor
//        longitude.setText(earthquake.LongitudeValue); // Commented By Shahzor
//        intensity.setText(getIntensity(earthquake.MMI));
    }

    private String getIntensity(String MMI){
        if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_ENGLISH))
            return getIntensity_EN(MMI);
        else
            return getIntensity_ES(MMI);
    }

    // Colworx : Get Intensity in English based on MMI of Earthquake
    private static String getIntensity_EN(String MMI) {
        String[] intensity = {"Weak","Light","Moderate","Strong","Very Strong","Servere","Violent","Extreme"};
        if((MMI.equals("2.0000")) || (MMI.equals("3.0000"))) {
            return intensity[0];
        } else if(MMI.equals("4.0000")) {
            return intensity[1];
        }else if(MMI.equals("5.0000")) {
            return intensity[2];
        }else if(MMI.equals("6.0000")) {
            return intensity[3];
        }else if(MMI.equals("7.0000")) {
            return intensity[4];
        }else if(MMI.equals("8.0000")) {
            return intensity[5];
        }else if(MMI.equals("9.0000")) {
            return intensity[6];
        }else if(MMI.equals("10.0000")) {
            return intensity[7];
        }
        return "";
    }

    // Colworx : Get Intensity in Spanish based on MMI of Earthquake
    private static String getIntensity_ES(String MMI) {
        String[] intensity = {"Débiles","Ligero","Moderar","Fuerte","Muy fuerte","Grave","Violento","Extremo"};
        if((MMI.equals("2.0000")) || (MMI.equals("3.0000"))) {
            return intensity[0];
        } else if(MMI.equals("4.0000")) {
            return intensity[1];
        }else if(MMI.equals("5.0000")) {
            return intensity[2];
        }else if(MMI.equals("6.0000")) {
            return intensity[3];
        }else if(MMI.equals("7.0000")) {
            return intensity[4];
        }else if(MMI.equals("8.0000")) {
            return intensity[5];
        }else if(MMI.equals("9.0000")) {
            return intensity[6];
        }else if(MMI.equals("10.0000")) {
            return intensity[7];
        }
        return "";
    }

    // Colworx : Convert date into UTC
    public static Date dateToUTC(Date date){
        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

}

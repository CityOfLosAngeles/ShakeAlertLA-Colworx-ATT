package com.app.shakealertla.UserInterface.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.error.VolleyError;
import com.app.shakealertla.Adapters.HomeTabstPagerAdapter;
import com.app.shakealertla.HttpUtils.RestAPI;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.RecentEarthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.UserInterface.Fragments.RecentEarthquakes_ListFragmentAPI;
import com.app.shakealertla.UserInterface.Fragments.RecentEarthquakes_MapFragmentAPI;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.ConfigConstants;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentEarthquakesActivity extends BaseActivity {

    private TabLayout tabLayout;
    static ArrayList<RecentEarthquakes> recentEarthquakesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_earthquakes);
        setupComponents();
    }

    ImageView back;
    public ImageView sort;
    @Override
    public void initializeComponents() {
        back = findViewById(R.id.back);
        sort = findViewById(R.id.sort);
        recentEarthquakesArrayList = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
//        fragments.add(new RecentEarthquakes_ListFragment()); // Commented by Shahzor
//        fragments.add(new RecentEarthquakes_MapFragment()); // Commented by Shahzor
        fragments.add(new RecentEarthquakes_ListFragmentAPI()); // Worked by Shahzor
        fragments.add(new RecentEarthquakes_MapFragmentAPI()); // Worked by Shahzor
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new HomeTabstPagerAdapter(getSupportFragmentManager(), RecentEarthquakesActivity.this, fragments));

        // TODO: TABLAYOUT LINK https://github.com/codepath/android_guides/wiki/Google-Play-Style-Tabs-using-TabLayout#add-icons-to-tablayout
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        String[] stringArray = getResources().getStringArray(R.array.recent_eq_tabs);
        for (int i = 0; i < stringArray.length; i++) {
            String string = stringArray[i];
            tabLayout.getTabAt(i).setContentDescription(string);
        }
    }

    @Override
    public void setupListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // configure icons
        final int[] imageResId = {
                R.mipmap.un_select_list,
                R.mipmap.un_select_map};

        final int[] selectedResId = {
                R.mipmap.list,
                R.mipmap.map};

        for (int i = 0; i < imageResId.length; i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < imageResId.length; i++) {
                    tabLayout.getTabAt(i).setIcon(imageResId[i]);
                }
                tab.setIcon(selectedResId[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static void getRecentEarthQuakeList(final Context context, final String comesFrom, double latitude, double longitude, int radius, final ServiceListener<String, String> listener) {

        String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String startDate = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date2 = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        startDate = format.format(date2);

        try {
            RestAPI.GetRequest("RecentEarthquakeService",
                    ConfigConstants.RECENT_EARTH_QUAKE_URL +
                            "?format=geojson&latitude=" + latitude + "&longitude=" + longitude +
                            "&maxradiuskm=" + radius + "&limit=" + "300" + "&minmagnitude=" + "3&orderby=time&starttime=" + startDate,
                    new ServiceListener<JSONObject, VolleyError>() {
                        @Override
                        public void success(JSONObject success) {
                            AppLog.d("result", success.toString());

                            try {
                                JSONArray jsonArray = success.getJSONArray("features");

                                if (jsonArray.length() == 0) {
//                                    Toast.makeText(RecentEarthquakesActivity.this, "No any recent Earthquake", Toast.LENGTH_SHORT).show();

                                } else {
                                    recentEarthquakesArrayList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject dataObject = jsonArray.getJSONObject(i);
                                        JSONObject propertiesJsonObject = dataObject.getJSONObject("properties");
                                        JSONObject geometryJsonObject = dataObject.getJSONObject("geometry");
                                        JSONArray coordinatesJsonArray = geometryJsonObject.getJSONArray("coordinates");

                                        RecentEarthquakes earthquakes = new RecentEarthquakes();

                                        for (int j = 0; j < coordinatesJsonArray.length(); j++) {
                                            earthquakes.LongitudeValue = coordinatesJsonArray.get(0).toString();
                                            earthquakes.LatitudeValue = coordinatesJsonArray.get(1).toString();
                                        }

                                        earthquakes.MagnitudeValue = new DecimalFormat("##.#").format(Double.valueOf(propertiesJsonObject.getString("mag")));
                                        earthquakes.startTime = propertiesJsonObject.getString("time");
                                        earthquakes.Topic = propertiesJsonObject.getString("place");

//                                        LatLng position = new LatLng(Double.valueOf(earthquakes.LatitudeValue), Double.valueOf(earthquakes.LongitudeValue));
//                                        String countryName = getCountryName(context, position);

/*
                                        if (countryName.equals("United States")) {
                                            recentEarthquakesArrayList.add(earthquakes);
                                        } else {
                                            continue;
                                        }
*/

                                        recentEarthquakesArrayList.add(earthquakes);

                                    }

//                                    adapter.notifyDataSetChanged();
//                                    SortRecentEarthquakeAPI.byTime(recentEarthquakesArrayList);

                                    RecentEarthquakes_ListFragmentAPI.filterRecentEarthquakesList(recentEarthquakesArrayList);

                                    listener.success("Success");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void error(VolleyError error) {
                            if (error.getMessage() != null) {
                                if (error.networkResponse.statusCode == -1)
                                    listener.error("Please check your internet connection.");
                                else {
                                    AppLog.d("result", error.getMessage());
                                    listener.error(error.getMessage());
                                }
                            } else {
                                String res = "";
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    res = new String(error.networkResponse.data);
                                    listener.error(res);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCountryName(Context context, LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String country = "";

            if(addresses != null && addresses.size() > 0){
                country = addresses.get(0).getCountryName();
            }

            return country;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}

package com.app.shakealertla.UserInterface.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.shakealertla.HttpUtils.RestAPI;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.RecentEarthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.UserInterface.Activities.EarthquakeDetailsActivity;
import com.app.shakealertla.UserInterface.Activities.RecentEarthquakesActivity;
import com.app.shakealertla.Utils.AnimUtils;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.Sort;
import com.app.shakealertla.Utils.SortRecentEarthquakeAPI;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class RecentEarthquakes_ListFragmentAPI extends BaseFragment implements PopupMenu.OnMenuItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_earthquakes_list, container, false);
    }

    ListView listView;
    static ArrayList<RecentEarthquakes> recentEarthquakesArrayList;
    SwipeRefreshLayout pullToRefresh;
    static RecoveryAdapter adapter;
    ImageView sort;
    private ProgressDialog dialog;

    @Override
    public void initializeComponents(View rootView) {
        sort = ((RecentEarthquakesActivity) getActivity()).sort;
        recentEarthquakesArrayList = new ArrayList<RecentEarthquakes>();
        dialog = new ProgressDialog(getActivity());
//        earthquakesArrayList.addAll(RecentEarthquakeService.getEarthQuakes()); // Commented By Shahzor

        // Worked By Shahzor Start
        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();
        RecentEarthquakesActivity.getRecentEarthQuakeList(getActivity(), "RecentEarthquakesList", Double.parseDouble("34.052235"), Double.parseDouble("-118.243683"), 800, new ServiceListener<String, String>() {
            @Override
            public void success(String SuccessListener) {
                dialog.dismiss();

            }

            @Override
            public void error(String ErrorListener) {
//                AppUtils.Toast(ErrorListener);
                dialog.dismiss();
            }
        });
        // Worked By Shahzor End

//        earthquakesArrayList.add(new Earthquakes(R.mipmap.red_pin, "Earthquake! Earthquake!", getString(R.string.earth_quake_earth_quake_expect_severe_shaking), "6 Miles NE from Aguanga, CA", "4.8", "Aug 24, 2018, 11:15 PM"));
//        earthquakesArrayList.add(new Earthquakes(R.mipmap.yellow_pin, "Earthquake! Earthquake!", "Expect severe shaking, Drop, cover, hold something", "6 Miles NE from Aguanga, CA", "7.3", "Aug 24, 2018, 11:15 PM"));
//        earthquakesArrayList.add(new Earthquakes(R.mipmap.green_pin, "No Shaking!", "Earthquake - no shaking expecting at your area", "6 Miles NE from Aguanga, CA", "6.0", "Aug 24, 2018, 11:15 PM"));
//        earthquakesArrayList.add(new Earthquakes(R.mipmap.green_pin, "No Shaking!", "Earthquake - no shaking expecting at your area", "6 Miles NE from Aguanga, CA", "6", "Aug 24, 2018, 11:15 PM"));
//        earthquakesArrayList.add(new Earthquakes(R.mipmap.red_pin, "Earthquake! Earthquake!", "Expect severe shaking, Drop, cover, hold something", "6 Miles NE from Aguanga, CA", "6.4", "Aug 24, 2018, 11:15 PM"));
        listView = rootView.findViewById(R.id.recentListView);
        pullToRefresh = rootView.findViewById(R.id.pullToRefresh);

        adapter = new RecoveryAdapter(getActivity(), R.layout.recent_earthquakes_list_item, recentEarthquakesArrayList);
        listView.setAdapter(adapter);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecentEarthquakesActivity.getRecentEarthQuakeList(getActivity(), "RecentEarthquakesList", Double.parseDouble("34.052235"), Double.parseDouble("-118.243683"), 800, new ServiceListener<String, String>() {
                    @Override
                    public void success(String SuccessListener) {
                        pullToRefresh.setRefreshing(false);

                        RecentEarthquakes_MapFragmentAPI.filterRecentEarthquakesMap(getActivity(), recentEarthquakesArrayList);
                    }

                    @Override
                    public void error(String ErrorListener) {
//                        AppUtils.Toast(ErrorListener);
                    }
                });
            }
        });

//        FloatingButtonSample(rootView);
    }

    private void FloatingButtonSample(final View view) {
//        https://github.com/Clans/FloatingActionButton
        final FloatingActionMenu floatingActionMenu = view.findViewById(R.id.menu);
//        floatingActionMenu.setIconAnimated(false);//Disable animation

        floatingActionMenu.setIconToggleAnimatorSet(AnimUtils.getFloatingMenuAnimation(floatingActionMenu));

        floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                floatingActionMenu.getMenuIconView().setImageResource(opened
                        ? R.mipmap.cancel : R.mipmap.sort_btn);
            }
        });
        final FloatingActionButton byTime = view.findViewById(R.id.byTime);
        FloatingActionButton byMagnitude = view.findViewById(R.id.byMagnitude);
        byTime.setLabelColors(getColor(R.color.grey_100), getColor(R.color.grey_300), getColor(R.color.grey_500));
        byTime.setLabelTextColor(getColor(R.color.grey_700));
        byTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                AppUtils.Toast("By Time Clicked");
            }
        });
        byMagnitude.setLabelColors(getColor(R.color.grey_100), getColor(R.color.grey_300), getColor(R.color.grey_500));
        byMagnitude.setLabelTextColor(getColor(R.color.grey_700));
        byMagnitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                SortRecentEarthquakeAPI.byMagnitude(recentEarthquakesArrayList);
                adapter.notifyDataSetChanged();
                AppUtils.Toast("By Magnitude Clicked");
            }
        });
    }

    private int getColor(int ColorResID) {
        return getResources().getColor(ColorResID);
    }

    @Override
    public void setupListeners(View rootView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent earthquakeDetailsInent = new Intent(getActivity(), EarthquakeDetailsActivity.class);
                earthquakeDetailsInent.putExtra("recentearthquake", recentEarthquakesArrayList.get(position));
                earthquakeDetailsInent.putExtra("ComesFrom", "RecentEarthQuakes");
                startActivity(earthquakeDetailsInent);
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.sort_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.byTime: {
                SortRecentEarthquakeAPI.byTime(recentEarthquakesArrayList);
                adapter.notifyDataSetChanged();
                listView.setSelectionAfterHeaderView();
                return true;
            }
            case R.id.byMagnitude: {
                SortRecentEarthquakeAPI.byMagnitude(recentEarthquakesArrayList);
                adapter.notifyDataSetChanged();
                listView.setSelectionAfterHeaderView();
                return true;
            }
            default:
                return false;
        }
    }

    public class RecoveryAdapter extends ArrayAdapter<RecentEarthquakes> {

        private ArrayList<RecentEarthquakes> timelineArrayList;
        private Context context;
        private int mResource;


        public RecoveryAdapter(Context context, int mResource, ArrayList<RecentEarthquakes> timelineArrayList) {
            super(context, mResource, timelineArrayList);
            this.timelineArrayList = timelineArrayList;
            this.mResource = mResource;
            this.context = context;
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = convertView;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(mResource, parent, false);
                result = convertView;
            }

            RecentEarthquakes earthquakes = timelineArrayList.get(position);
            ImageView imageView = result.findViewById(R.id.image);
            TextView tv_title = result.findViewById(R.id.text);
            TextView tv_desc = result.findViewById(R.id.desc);
            TextView tv_location = result.findViewById(R.id.location);
            TextView tv_magnitude = result.findViewById(R.id.magnitude);
            TextView tv_time = result.findViewById(R.id.time);

//            tv_title.setText(earthquakes.title); // Commented By Shahzor
//            tv_desc.setText(earthquakes.body); // Commented By Shahzor
//            tv_location.setText(ShelterService.getLocation(getActivity(), new LatLng(Double.valueOf(earthquakes.LatitudeValue), Double.valueOf(earthquakes.LongitudeValue)))); // Commented By Shahzor
//            tv_magnitude.setText(getString(R.string.magnitude_dot) + " " +earthquakes.getMagnitude()); // Commented By Shahzor
//            tv_time.setText(AppUtils.formatDate("MMM dd, yyyy, hh:mm a", new Date(Long.valueOf(earthquakes.startTime)))); // Commented By Shahzor

            tv_location.setText(earthquakes.Topic); // Worked By Shahzor
            tv_magnitude.setText(getString(R.string.magnitude_dot) + " " + earthquakes.getMagnitude()); // Worked By Shahzor

            // If date shown in UTC format
            Date convertDateToUTC = dateToUTC(new Date(Long.valueOf(earthquakes.startTime)));
//            tv_time.setText(AppUtils.formatDate("MMM dd, yyyy, kk:mm:ss", convertDateToUTC) + " (UTC)"); // Worked By Shahzor

            long convertDateToUTCInLong = convertDateToUTC.getTime();

            Date date = new Date(Long.valueOf(earthquakes.startTime));

            // If date shown in PST format
            TimeZone pacificTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
            long apiResponseTime = new Date(convertDateToUTCInLong).getTime();
            long convertTimeToPST = apiResponseTime + pacificTimeZone.getOffset(apiResponseTime);
            Date pstDate = new Date(Long.valueOf(convertTimeToPST));
            tv_time.setText(AppUtils.formatDate("MMM dd, yyyy, hh:mm:ss a", pstDate) +" (PST)"); // Worked By Shahzor

            double magnitude = Double.valueOf(earthquakes.getMagnitude());
            int icon = 0;
            if (magnitude <= 3.9) {
                icon = R.mipmap.green_pin;
            } else if (magnitude >= 4.0 && magnitude < 5.0) {
                icon = R.mipmap.yellow_pin;
            } else if (magnitude >= 5.0) {
                icon = R.mipmap.red_pin;
            }
//            Glide.with(ShakeAlertLA.getContext()).load(icon).into(imageView);
            imageView.setImageResource(icon);
            return result;
        }
    }

    /**
     * Colworx : Filter Recent Earthquakes List
     */
    public static void filterRecentEarthquakesList(ArrayList<RecentEarthquakes> recentEarthquakes) {
        recentEarthquakesArrayList.clear();
        for (RecentEarthquakes activities : recentEarthquakes) {
            recentEarthquakesArrayList.add(activities);
        }
//        SortRecentEarthquakeAPI.byTime(recentEarthquakesArrayList);
        adapter.notifyDataSetChanged();
    }

    /**
     * Colworx : Convert date into UTC
     */
    public static Date dateToUTC(Date date){
        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

}

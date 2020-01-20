package com.app.shakealertla.UserInterface.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.error.VolleyError;
import com.app.shakealertla.HttpUtils.RestAPI;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.RecentEarthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.UserInterface.Activities.EarthquakeDetailsActivity;
import com.app.shakealertla.UserInterface.Activities.RecentEarthquakesActivity;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.ConfigConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecentEarthquakes_MapFragmentAPI extends BaseFragment implements OnMapReadyCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_ashelter__map, container, false);
    }

/*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new RecentEarthQuakes().execute();
    }
*/

    private static GoogleMap mMap;
    static ArrayList<RecentEarthquakes> earthquakesArrayList; // Worked by Shahzor
    ArrayList<RecentEarthquakes> recentEarthquakesArrayList; // Worked by Shahzor
    static String recentItemLatitude = "";
    static String recentItemLongitude = "";
    boolean isFragmentVisible = true;
    boolean isMapReady = false;
    boolean isCameraMapMoved = false;
    boolean isFirstTimeMapMoved = false;
    static boolean isCameraMapDrag = true;
    static int radiusInKM = 800;
    static int newRadius = 800;
    float currentZoomLevel;
    float zoomLevel;
    float oldZoomLevel;
    static ArrayList<Marker> markerArrayList;
    ArrayList<RecentEarthquakes> updatedMarkerArrayList;

    @Override
    public void initializeComponents(View rootView) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        earthquakesArrayList = new ArrayList<RecentEarthquakes>(); // Worked by Shahzor
        recentEarthquakesArrayList = new ArrayList<RecentEarthquakes>(); // Worked by Shahzor
        markerArrayList = new ArrayList<Marker>(); // Worked by Shahzor
        updatedMarkerArrayList = new ArrayList<RecentEarthquakes>(); // Worked by Shahzor
    }

    @Override
    public void setupListeners(View rootView) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

/*
        LatLngBounds LOS_ANGELES_BOUNDS = new LatLngBounds(new LatLng(33.244209724738866, -118.80579125136136),
                new LatLng(35.09928839505256, -117.00120873749256));
        mMap.setLatLngBoundsForCameraTarget(LOS_ANGELES_BOUNDS);
*/

        isMapReady = true;

        LatLng LA = new LatLng(34.052235, -118.243683);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LA, 12f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                RecentEarthquakes eq = (RecentEarthquakes) marker.getTag();
                Intent earthquakeDetailsInent = new Intent(getActivity(), EarthquakeDetailsActivity.class);
                earthquakeDetailsInent.putExtra("recentearthquake", eq);
                earthquakeDetailsInent.putExtra("ComesFrom", "RecentEarthQuakes");
                startActivity(earthquakeDetailsInent);
            }
        });


        // Worked By Shahzor Start
/*
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                if(cameraPosition.zoom > 18.0) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });
*/

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                if (isCameraMapMoved) {

                    if (isFirstTimeMapMoved) {
                        isFirstTimeMapMoved = false;

                        LatLng bounds = mMap.getProjection().getVisibleRegion().farLeft;
                        LatLngBounds bounds2 = mMap.getProjection().getVisibleRegion().latLngBounds;
                        LatLng northeast = bounds2.northeast;
                        LatLng southwest = bounds2.southwest;

                        double boundsLatitude = bounds.latitude;
                        double boundsLongitude = bounds.longitude;
                        double northeastLatitude = northeast.latitude;
                        double northeastLongitude = northeast.longitude;
                        double southwestLatitude = southwest.latitude;
                        double southwestLongitude = southwest.longitude;

                        LatLng target = mMap.getCameraPosition().target;

                        double targetLatitude = target.latitude;
                        double targetLongitude = target.longitude;

                        double getDistance = getDistanceInKM(34.052235, -118.243683, northeastLatitude, northeastLongitude);
//                double getDistance = getDistanceInKM(northeastLatitude, northeastLongitude, southwestLatitude, southwestLongitude);
                        radiusInKM = Integer.parseInt(String.valueOf(Math.round(getDistance * 100) / 100));

                        currentZoomLevel = mMap.getCameraPosition().zoom;

/*
                        if(mMap.getCameraPosition().zoom > zoomLevel){
//                        Log.d("Zoom Level", ""+mMap.getCameraPosition().zoom);
                            isCameraMapDrag = false;
                        }
*/

                        if (!isCameraMapDrag) {
                            if (radiusInKM > newRadius) {
                                newRadius = radiusInKM;
                                if (newRadius < 4000) {
                                    new RecentEarthQuakes("NextTime", radiusInKM, 34.052235, -118.243683).execute();
                                }

                            }
                        } else {
                            LatLngBounds latLongBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                            updatedMarkerArrayList.clear();

                            for (int i = 0; i < markerArrayList.size(); i++) {

                                LatLng location = new LatLng(Double.parseDouble(earthquakesArrayList.get(i).LatitudeValue),
                                        Double.parseDouble(earthquakesArrayList.get(i).LongitudeValue));

                                if (latLongBounds.contains(location)) {
                                    updatedMarkerArrayList.add(earthquakesArrayList.get(i));
                                }
                            }

                            RecentEarthquakes_ListFragmentAPI.filterRecentEarthquakesList(updatedMarkerArrayList);
                        }

                    }

                    System.out.print("");

                }

            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

//                zoomLevel = cameraPosition.zoom;
//                if(isFirstTimeMapMoved){
                if (zoomLevel > cameraPosition.zoom) {
                    zoomLevel = cameraPosition.zoom;
                    isCameraMapDrag = false;
                } else {
                    isCameraMapDrag = true;
                }
//                }
            }
        });

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == REASON_GESTURE) {
                    isFirstTimeMapMoved = true;
//                    isCameraMapDrag = true;
                } else if (i == REASON_DEVELOPER_ANIMATION) {
//                    currentZoomLevel = mMap.getCameraPosition().zoom;
                    if (isCameraMapDrag) {
//                        zoomLevel = mMap.getCameraPosition().zoom;
                        zoomLevel = (float) 7.160286;
                    }
                    isFirstTimeMapMoved = false;
                }
            }
        });

/*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

*/
/*
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                Calendar calendar = null;
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, -1);
                    Date date2 = calendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateOutput = format.format(date2);
                    Log.d("Check", "For Debug");

                Log.d("Check", "For Debug");
*//*


            }
        });
*/
        // Worked By Shahzor End

    }

    /**
     * Colworx : Call Rest API for get Recent EarthQuakes List
     */
    public void getRecentEarthQuakeList(final String comesFrom, double latitude, double longitude, int radius, final ServiceListener<String, String> listener) {

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
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                                if (jsonArray.length() == 0) {
//                                    AppUtils.Toast("No any recent Earthquake");

                                } else {
                                    mMap.clear();
                                    earthquakesArrayList.clear();
                                    markerArrayList.clear();
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
//                                        String countryName = RecentEarthquakesActivity.getCountryName(getActivity(), position);

/*
                                        if (countryName.equals("United States")) {
                                            earthquakesArrayList.add(earthquakes);
                                        } else {
                                            continue;
                                        }
*/

                                        earthquakesArrayList.add(earthquakes);

                                    }

                                    if (comesFrom.equals("NextTime")) {
                                        RecentEarthquakes_ListFragmentAPI.filterRecentEarthquakesList(earthquakesArrayList);
                                    }

                                    if (earthquakesArrayList.size() == 0) {
                                        // Add a marker in Sydney and move the camera
                                        LatLng LA = new LatLng(34.052235, -118.243683);
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LA, 12f));
                                    } else {
                                        int currentPosition = 1;
                                        for (RecentEarthquakes earthquake : earthquakesArrayList) {
                                            double mmi = Double.valueOf(earthquake.getMagnitude());
                                            int icon = 0;
                                            if (mmi <= 4.0) {
                                                icon = R.mipmap.green_pin;
                                            } else if (mmi >= 4.0 && mmi < 5.0) {
                                                icon = R.mipmap.yellow_pin;
                                            } else if (mmi >= 5.0) {
                                                icon = R.mipmap.red_pin;
                                            }

                                            LatLng position = new LatLng(Double.valueOf(earthquake.LatitudeValue), Double.valueOf(earthquake.LongitudeValue));
                                            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(earthquake.Topic));
                                            marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
                                            marker.setTag(earthquake);

//                                            MarkerOptions markerOptions = new MarkerOptions();
//                                            markerOptions.position(position);
//                                            mMap.addMarker(markerOptions);
                                            builder.include(position);

                                            markerArrayList.add(marker);

                                            if (currentPosition == 1) {
                                                recentItemLatitude = earthquake.LatitudeValue;
                                                recentItemLongitude = earthquake.LongitudeValue;
                                            }
                                            currentPosition++;

                                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                                // Use default InfoWindow frame
                                                @Override
                                                public View getInfoWindow(Marker arg0) {
                                                    return null;
                                                }

                                                // Defines the contents of the InfoWindow
                                                @Override
                                                public View getInfoContents(Marker arg0) {

                                                    // Getting view from the layout file info_window_layout
                                                    View v = getLayoutInflater().inflate(R.layout.recent_earthquakes_tooltip, null);

                                                    // Getting the position from the marker
                                                    LatLng latLng = arg0.getPosition();

                                                    // Getting reference to the TextView
                                                    TextView title = (TextView) v.findViewById(R.id.title);
                                                    TextView desc = (TextView) v.findViewById(R.id.desc);
                                                    TextView mag = (TextView) v.findViewById(R.id.magnitude);
                                                    TextView time = (TextView) v.findViewById(R.id.time);
                                                    TextView adrs = (TextView) v.findViewById(R.id.adrs);

                                                    RecentEarthquakes eq = (RecentEarthquakes) arg0.getTag();
                                                    title.setText(eq.Topic);
                                                    title.setContentDescription(eq.Topic);
                                                    desc.setText(eq.Topic);
                                                    desc.setContentDescription(eq.Topic);
                                                    mag.setText(getString(R.string.magnitude_dot) + " " + eq.getMagnitude());
                                                    mag.setContentDescription(getString(R.string.magnitude_dot) + " " + eq.getMagnitude());

                                                    Date convertDateToUTC = dateToUTC(new Date(Long.valueOf(eq.startTime)));
//                                                    time.setText(AppUtils.formatDate("MMM dd, yyyy, kk:mm:ss", convertDateToUTC)+ " (UTC)");
//                                                    time.setContentDescription(AppUtils.formatDate("MMM dd, yyyy, kk:mm:ss", convertDateToUTC)+ " (UTC)");

                                                    long convertDateToUTCInLong = convertDateToUTC.getTime();

                                                    // If date shown in PST format
                                                    TimeZone pacificTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
                                                    long apiResponseTime = new Date(convertDateToUTCInLong).getTime();
                                                    long convertTimeToPST = apiResponseTime + pacificTimeZone.getOffset(apiResponseTime);
                                                    Date pstDate = new Date(Long.valueOf(convertTimeToPST));
                                                    time.setText(AppUtils.formatDate("MMM dd, yyyy, hh:mm:ss a", pstDate) +" (PST)");
                                                    time.setContentDescription(AppUtils.formatDate("MMM dd, yyyy, hh:mm:ss a", pstDate) +" (PST)");

                                                    title.setText(eq.Topic);
                                                    title.setContentDescription(eq.Topic);
                                                    // Returning the view containing InfoWindow contents
                                                    return v;
                                                }
                                            });

                                        }

/*
                                        final int count = 1;
                                        for (RecentEarthquakes recentEarthquakes : SortRecentEarthquakeAPI.byTime2(earthquakesArrayList)) {
                                            if (count == 1) {
                                                recentItemLatitude = recentEarthquakes.LatitudeValue;
                                                recentItemLongitude = recentEarthquakes.LongitudeValue;
                                                break;
                                            }
                                        }
*/

                                        LatLngBounds bounds = builder.build();
                                        int zoomWidth = getResources().getDisplayMetrics().widthPixels;
                                        int zoomHeight = getResources().getDisplayMetrics().heightPixels;
                                        int zoomPadding = 0;
                                        if (comesFrom.equals("NextTime")) {
                                            zoomPadding = (int) (zoomWidth * 0.20); // offset from edges of the map 12% of screen
                                        } else {
                                            zoomPadding = (int) (zoomWidth * 0.28); // offset from edges of the map 12% of screen
                                        }
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, zoomWidth,
                                                zoomHeight - 100, zoomPadding));

//                                        LatLng firstItemLatLng = new LatLng(Double.valueOf(recentItemLatitude), Double.valueOf(recentItemLongitude));
//                                        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(firstItemLatLng, 10);
//                                        mMap.moveCamera(cameraPosition);
//                                        mMap.animateCamera(cameraPosition);

                                    }

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

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            if (isFragmentVisible && isMapReady) {
                new RecentEarthQuakes("FirstTime", 800, 34.052235, -118.243683).execute();
            }
        }
    }

    /**
     * Colworx : Call Rest API for get Recent EarthQuakes List with Async Task Background thread class
     */
    class RecentEarthQuakes extends AsyncTask<Void, Integer, String> {

        int radius;
        String comesFrom;
        double latitude, longitude;
        private ProgressDialog dialog;

        public RecentEarthQuakes(String comesFrom, int radius, double latitude, double longitude) {
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
            this.comesFrom = comesFrom;
            dialog = new ProgressDialog(getActivity());
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);

            dialog.setMessage(getString(R.string.loading));
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(Void... arg0) {

            getRecentEarthQuakeList(comesFrom, latitude, longitude, radius, new ServiceListener<String, String>() {
                @Override
                public void success(String SuccessListener) {
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    mMap.getUiSettings().setZoomGesturesEnabled(true);
                    dialog.dismiss();
                    if (comesFrom.equals("FirstTime")) {
                        isFragmentVisible = false;
                        isCameraMapMoved = true;
                    }
                }

                @Override
                public void error(String ErrorListener) {
//                    AppUtils.Toast(ErrorListener);
                    dialog.dismiss();
                }
            });

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

/*
    private double getMapVisibleRadius() {
        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();

        float[] distanceWidth = new float[1];
        float[] distanceHeight = new float[1];

        LatLng farRight = visibleRegion.farRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng nearLeft = visibleRegion.nearLeft;

        Location.distanceBetween(
                (farLeft.latitude + nearLeft.latitude) / 2,
                farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2,
                farRight.longitude,
                distanceWidth
        );

        Location.distanceBetween(
                farRight.latitude,
                (farRight.longitude + farLeft.longitude) / 2,
                nearRight.latitude,
                (nearRight.longitude + nearLeft.longitude) / 2,
                distanceHeight
        );

        double radiusInMeters = Math.sqrt(Math.pow(distanceWidth[0], 2) + Math.pow(distanceHeight[0], 2)) / 2;
        return radiusInMeters;
    }
*/

    /**
     * Colworx : Get Distance in KM between two locations
     */
    private double getDistanceInKM(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double temp = 6371 * c;
        temp = temp * 0.621;
        return temp;
    }

    /**
     * Colworx : Filter Recent Earthquakes Map List
     */
    public static void filterRecentEarthquakesMap(final Context context, ArrayList<RecentEarthquakes> recentEarthquakes) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        earthquakesArrayList.clear();
        markerArrayList.clear();
        radiusInKM = 800;
        newRadius = 800;
        isCameraMapDrag = true;

        if (recentEarthquakes.size() == 0) {
            // Add a marker in Sydney and move the camera
            LatLng LA = new LatLng(34.052235, -118.243683);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LA, 12f));
        } else {
            mMap.clear();
            int currentPosition = 1;
            for (RecentEarthquakes earthquake : recentEarthquakes) {
                double mmi = Double.valueOf(earthquake.getMagnitude());
                int icon = 0;
                if (mmi <= 4.0) {
                    icon = R.mipmap.green_pin;
                } else if (mmi >= 4.0 && mmi < 5.0) {
                    icon = R.mipmap.yellow_pin;
                } else if (mmi >= 5.0) {
                    icon = R.mipmap.red_pin;
                }

                LatLng position = new LatLng(Double.valueOf(earthquake.LatitudeValue), Double.valueOf(earthquake.LongitudeValue));
                Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(earthquake.Topic));
                marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
                marker.setTag(earthquake);

//                                            MarkerOptions markerOptions = new MarkerOptions();
//                                            markerOptions.position(position);
//                                            mMap.addMarker(markerOptions);
                builder.include(position);

                earthquakesArrayList.add(earthquake);
                markerArrayList.add(marker);

                if (currentPosition == 1) {
                    recentItemLatitude = earthquake.LatitudeValue;
                    recentItemLongitude = earthquake.LongitudeValue;
                }
                currentPosition++;

            }

/*
            final int count = 1;
            for (RecentEarthquakes recentEarthquakes2 : SortRecentEarthquakeAPI.byTime2(recentEarthquakes)) {
                if (count == 1) {
                    recentItemLatitude = recentEarthquakes2.LatitudeValue;
                    recentItemLongitude = recentEarthquakes2.LongitudeValue;
                    break;
                }
            }
*/

            LatLngBounds bounds = builder.build();
            int zoomWidth = context.getResources().getDisplayMetrics().widthPixels;
            int zoomHeight = context.getResources().getDisplayMetrics().heightPixels;
            int zoomPadding = 0;
            zoomPadding = (int) (zoomWidth * 0.28); // offset from edges of the map 12% of screen
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, zoomWidth,
                    zoomHeight - 100, zoomPadding));

//                                        LatLng firstItemLatLng = new LatLng(Double.valueOf(recentItemLatitude), Double.valueOf(recentItemLongitude));
//                                        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(firstItemLatLng, 10);
//                                        mMap.moveCamera(cameraPosition);
//                                        mMap.animateCamera(cameraPosition);
        }

    }

/*
    private double getMapVisibleRadius2() {
        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();

        float[] diagonalDistance = new float[1];

        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;

        Location.distanceBetween(
                farLeft.latitude,
                farLeft.longitude,
                nearRight.latitude,
                nearRight.longitude,
                diagonalDistance
        );

        return diagonalDistance[0] / 2;
    }
*/

    @Override
    public void onPause() {
        super.onPause();
        radiusInKM = 800;
        newRadius = 800;
        isCameraMapDrag = true;
    }

    // Colworx : Convert date into UTC
    public static Date dateToUTC(Date date){
        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

}

package com.app.shakealertla.UserInterface.Fragments;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.shakealertla.Models.Earthquakes;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.ShelterService;
import com.app.shakealertla.UserInterface.Activities.EarthquakeDetailsActivity;
import com.app.shakealertla.Utils.AppUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Emergency_MapFragment extends BaseFragment implements OnMapReadyCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.emergency_map_fragment, container, false);
    }

    private GoogleMap mMap;
    FragmentTransaction ft;
    SupportMapFragment mapFragment;

    @Override
    public void initializeComponents(View rootView) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.INVISIBLE);
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        ft = fm.beginTransaction();
//        ft.hide(mapFragment).commit();
    }

    @Override
    public void setupListeners(View rootView) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if (getArguments() != null) {
//            try {
            final Earthquakes earthquake = (Earthquakes) getArguments().getSerializable("payload");
//                final JSONObject jsonObject = new JSONObject(getArguments().getString("alert"));
            // Add a marker in Sydney and move the camera
//                LatLng sydney = new LatLng(jsonObject.getDouble("Latitude"), jsonObject.getDouble("Longitude"));
//            LatLng sydney = new LatLng(SharedPreferenceManager.getLatitude(), SharedPreferenceManager.getLongitude());
            LatLng sydney = new LatLng(Double.valueOf(earthquake.LatitudeValue), Double.valueOf(earthquake.LongitudeValue));
            MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("Marker in Sydney");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_notification_overlay));
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(earthquake);

//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17f));
//            addPulsatingEffect(sydney, 100);

            drawPolygon(earthquake, sydney);

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
                    View v = getLayoutInflater().inflate(R.layout.emergency_tooltip, null);

                    // Getting the position from the marker
                    LatLng latLng = arg0.getPosition();

                    // Getting reference to the TextView
                    TextView title = (TextView) v.findViewById(R.id.title);
                    TextView desc = (TextView) v.findViewById(R.id.desc);
                    TextView magnitude = (TextView) v.findViewById(R.id.magnitude);
                    TextView time = (TextView) v.findViewById(R.id.time);
                    TextView adrs = (TextView) v.findViewById(R.id.adrs);

//                        try {
                    Earthquakes earthquake = (Earthquakes) arg0.getTag();
                    title.setText(earthquake.title);
                    title.setContentDescription(earthquake.title);
                    desc.setText(earthquake.body);
                    desc.setContentDescription(earthquake.body);
                    magnitude.setText(getString(R.string.magnitude_dot) + " " + new DecimalFormat("##.##").format(Double.valueOf(earthquake.MagnitudeValue)));
                    magnitude.setContentDescription(getString(R.string.magnitude_dot) + " " +new DecimalFormat("##.##").format(Double.valueOf(earthquake.MagnitudeValue)));
                    time.setText("Time: " + AppUtils.formatDate("MMM dd, yyyy, hh:mm a", new Date(Long.valueOf(earthquake.startTime))));
                    time.setContentDescription("Time: " + AppUtils.formatDate("MMM dd, yyyy, hh:mm a", new Date(Long.valueOf(earthquake.startTime))));
                    title.setText(ShelterService.getLocation(getActivity(), new LatLng(Double.valueOf(earthquake.LatitudeValue), Double.valueOf(earthquake.LongitudeValue))));
                    title.setContentDescription(ShelterService.getLocation(getActivity(), new LatLng(Double.valueOf(earthquake.LatitudeValue), Double.valueOf(earthquake.LongitudeValue))));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    // Returning the view containing InfoWindow contents
                    return v;

                }
            });
            addPulsatingEffect(sydney, 100);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Earthquakes eq = (Earthquakes) marker.getTag();
                    Intent earthquakeDetailsInent = new Intent(getActivity(), EarthquakeDetailsActivity.class);
                    earthquakeDetailsInent.putExtra("earthquake", eq);
                    startActivity(earthquakeDetailsInent);
                }
            });
//            ft.show(mapFragment).commit();
            mapFragment.getView().setVisibility(View.VISIBLE);
        } else {
            LatLng LA = new LatLng(34.052235, -118.243683);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LA, 12f));
//            ft.show(mapFragment).commit();
            mapFragment.getView().setVisibility(View.VISIBLE);
        }
        mMap.setMyLocationEnabled(true);
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick (Marker marker){
//
//                return true;
//            }
//            });


    }

    private void drawPolygon(Earthquakes earthquake, LatLng sydney) {
        // Add polylines and polygons to the map. This section shows just
        // a single polyline. Read the rest of the tutorial to learn more.
        ArrayList<ArrayList<String>> polygons = earthquake.Polygons;
        Collections.reverse(polygons);
        Collections.reverse(earthquake.Colors);
        for (int i = 0; i < polygons.size(); i++) {
            int color = Color.parseColor(earthquake.Colors.get(i));
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            ArrayList<String> polygon = polygons.get(i);
            PolygonOptions rectOptions = new PolygonOptions()
                    .addAll(earthquake.getPolygonCordinates(polygon)).strokeColor(Color.parseColor(earthquake.Colors.get(i))).fillColor(Color.argb(127, r, g, b));
// Get back the mutable Polygon
            mMap.addPolygon(rectOptions);
        }

//        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
//                .clickable(true).addAll(earthquake.getPolygonCordinates(earthquake.Polygon)));
//        polyline1.setColor(Color.RED);
//                .add(
//                        new LatLng(-35.016, 143.321),
//                        new LatLng(-34.747, 145.592),
//                        new LatLng(-34.364, 147.891),
//                        new LatLng(-33.501, 150.217),
//                        new LatLng(-32.306, 149.248),
//                        new LatLng(-32.491, 147.309)));

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (ArrayList<String> polygon : earthquake.Polygons) {
            for (final LatLng latLng : earthquake.getPolygonCordinates(polygon)) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                //the include method will calculate the min and max bound.
                builder.include(latLng);
            }
        }

        final LatLngBounds bounds = builder.build();
        final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
        final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
        final int zoomPadding = (int) (zoomWidth * 0.10); // offset from edges of the map 12% of screen
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, zoomWidth,
                zoomHeight, zoomPadding));

        // Set listeners for click events.
//        googleMap.setOnPolylineClickListener(this);
//        googleMap.setOnPolygonClickListener(this);
    }

    private Circle lastUserCircle;
    private long pulseDuration = 1000;
    private ValueAnimator lastPulseAnimator;

    private void addPulsatingEffect(final LatLng userLatlng, int radius) {
//        int red
        if (lastPulseAnimator != null) {
            lastPulseAnimator.cancel();
            Log.d("onLocationUpdated: ", "cancelled");
        }
        if (lastUserCircle != null)
            lastUserCircle.setCenter(userLatlng);
        lastPulseAnimator = valueAnimate(radius, pulseDuration, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (lastUserCircle != null)
                    lastUserCircle.setRadius((Float) animation.getAnimatedValue());
                else {
                    lastUserCircle = mMap.addCircle(new CircleOptions()
                            .center(userLatlng)
                            .radius((Float) animation.getAnimatedValue())
                            .strokeColor(Color.TRANSPARENT)
                            .fillColor(Color.BLUE));
                }
                lastUserCircle.setFillColor(adjustAlpha(Color.RED, 1 - animation.getAnimatedFraction()));
            }
        });

    }

    protected ValueAnimator valueAnimate(float accuracy, long duration, ValueAnimator.
            AnimatorUpdateListener updateListener) {
        Log.d("valueAnimate: ", "called");
        ValueAnimator va = ValueAnimator.ofFloat(0, accuracy);
        va.setDuration(duration);
        va.addUpdateListener(updateListener);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setRepeatMode(ValueAnimator.RESTART);

        va.start();
        return va;
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}

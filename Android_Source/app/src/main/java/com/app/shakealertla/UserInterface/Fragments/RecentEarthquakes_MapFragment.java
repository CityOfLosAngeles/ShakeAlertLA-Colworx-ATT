package com.app.shakealertla.UserInterface.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.shakealertla.Models.Earthquakes;
import com.app.shakealertla.Models.Shelters;
import com.app.shakealertla.R;
import com.app.shakealertla.Services.RecentEarthquakeService;
import com.app.shakealertla.Services.ShelterService;
import com.app.shakealertla.UserInterface.Activities.EarthquakeDetailsActivity;
import com.app.shakealertla.Utils.AppUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

public class RecentEarthquakes_MapFragment extends BaseFragment implements OnMapReadyCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_ashelter__map, container, false);
    }

    private GoogleMap mMap;

    @Override
    public void initializeComponents(View rootView) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void setupListeners(View rootView) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<Earthquakes> earthquakes = RecentEarthquakeService.getEarthQuakes();
        if (earthquakes.size() == 0) {
            // Add a marker in Sydney and move the camera
            LatLng LA = new LatLng(34.052235, -118.243683);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LA, 12f));
        } else {
            for (Earthquakes earthquake : earthquakes) {
                double mmi = Double.valueOf(earthquake.MMI);
                int icon = 0;
                if (mmi <= 4.0) {
                    icon = R.mipmap.green_pin;
                } else if (mmi > 4.0 && mmi < 5.0) {
                    icon = R.mipmap.yellow_pin;
                } else if (mmi >= 5.0) {
                    icon = R.mipmap.red_pin;
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (final Earthquakes eq : earthquakes) {
                    LatLng position = new LatLng(Double.valueOf(eq.LatitudeValue), Double.valueOf(eq.LongitudeValue));
                    Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(eq.title));
                    marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
                    marker.setTag(eq);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                    //the include method will calculate the min and max bound.
                    builder.include(position);
                }

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

                        Earthquakes eq = (Earthquakes) arg0.getTag();
                        title.setText(eq.title);
                        title.setContentDescription(eq.title);
                        desc.setText(eq.body);
                        desc.setContentDescription(eq.body);
                        mag.setText(getString(R.string.magnitude_dot) + " " +eq.MagnitudeValue);
                        mag.setContentDescription(getString(R.string.magnitude_dot) + " " +eq.MagnitudeValue);
                        time.setText(AppUtils.formatDate("MMM dd, yyyy, hh:mm a",new Date(Long.valueOf(eq.startTime))));
                        time.setContentDescription(AppUtils.formatDate("MMM dd, yyyy, hh:mm a",new Date(Long.valueOf(eq.startTime))));
                        title.setText(ShelterService.getLocation(getActivity(), new LatLng(Double.valueOf(eq.LatitudeValue), Double.valueOf(eq.LongitudeValue))));
                        title.setContentDescription(ShelterService.getLocation(getActivity(), new LatLng(Double.valueOf(eq.LatitudeValue), Double.valueOf(eq.LongitudeValue))));
                        // Returning the view containing InfoWindow contents
                        return v;
                    }
                });

                final LatLngBounds bounds = builder.build();
                final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
                final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
                final int zoomPadding = (int) (zoomWidth * 0.10); // offset from edges of the map 12% of screen
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, zoomWidth,
                        zoomHeight, zoomPadding));
            }

        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Earthquakes eq = (Earthquakes) marker.getTag();
                Intent earthquakeDetailsInent = new Intent(getActivity(), EarthquakeDetailsActivity.class);
                earthquakeDetailsInent.putExtra("earthquake", eq);
                startActivity(earthquakeDetailsInent);
            }
        });
    }
}
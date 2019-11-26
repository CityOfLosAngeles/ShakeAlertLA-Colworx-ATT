package com.app.shakealertla.UserInterface.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.shakealertla.Models.Shelters;
import com.app.shakealertla.R;
import com.app.shakealertla.UserInterface.Activities.FindAshelterActivity;
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

import java.util.ArrayList;

public class FindAshelter_MapFragment extends BaseFragment implements OnMapReadyCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_ashelter__map, container, false);
    }

    ArrayList<Shelters.Shelter> shelterArrayList;
    private GoogleMap mMap;

    @Override
    public void initializeComponents(View rootView) {
        shelterArrayList = ((FindAshelterActivity) getActivity()).sheltersArray.features;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void setupListeners(View rootView) {

    }

    public void selectMarker(String shelterName){
        for (Marker marker : markers) {
            Shelters.Shelter shelter = (Shelters.Shelter) marker.getTag();
            if (shelter.attributes.FacilityName.matches(shelterName)){
                marker.showInfoWindow();mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 250, null);
            }
        }
    }

    private ArrayList<Marker> markers = new ArrayList<>();
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        markers.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (shelterArrayList != null && shelterArrayList.size() > 0) {
            for (final Shelters.Shelter shelter : shelterArrayList) {
                LatLng position = new LatLng(shelter.geometry.y, shelter.geometry.x);
                Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(shelter.attributes.FacilityName));
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.find_pin));
                marker.setTag(shelter);
                markers.add(marker);
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
                    View v = getLayoutInflater().inflate(R.layout.shelter_tooltip, null);

                    // Getting the position from the marker
                    LatLng latLng = arg0.getPosition();

                    // Getting reference to the TextView
                    TextView title = (TextView) v.findViewById(R.id.title);
                    TextView desc = (TextView) v.findViewById(R.id.desc);

                    Shelters.Shelter shelter = (Shelters.Shelter) arg0.getTag();
                    title.setText(shelter.attributes.FacilityName);
                    title.setContentDescription(shelter.attributes.FacilityName);
                    desc.setText(shelter.address);
                    desc.setContentDescription(shelter.address);
                    // Returning the view containing InfoWindow contents
//                    v.setContentDescription("Title: "+shelter.attributes.FacilityName+"\nAddress: "+shelter.address);
//                    v.announceForAccessibility("Title: "+shelter.attributes.FacilityName+"\nAddress: "+shelter.address);
                    return v;
                }
            });

            final LatLngBounds bounds = builder.build();
            final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
            final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
            final int zoomPadding = (int) (zoomWidth * 0.10); // offset from edges of the map 12% of screen
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, zoomWidth,
                    zoomHeight, zoomPadding));
        }else {
            LatLng LA = new LatLng(34.052235,-118.243683);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LA, 12f));
        }
    }
}

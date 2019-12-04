package com.app.shakealertla.Utils;

import android.content.Context;
import android.location.Location;

import com.app.shakealertla.Listener.Callback;
import com.google.android.gms.maps.model.LatLng;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class SingleShotLocation {
    private void startLocationListener(Context context, final Callback<LatLng> callback) {

        long mLocTrackingInterval = 1000 *10;
        float trackingDistance = 15;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);

        SmartLocation.with(context)
                .location()
                .config(LocationParams.BEST_EFFORT)
                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        callback.callback(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                });
    }
}

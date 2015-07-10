package com.primankaden.stay63.bl;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.primankaden.stay63.Stay63Application;

import java.util.ArrayList;
import java.util.List;

public class GeoBusinessLogic {
    private final static String TAG = "GeoBusinessLogic";
    private static GeoBusinessLogic instance;
    private LocationManager manager;
    private List<LocationListener> listeners;
    private android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            for (LocationListener listener : listeners) {
                listener.onLocationChanged(location);
            }
            lastKnownLocation = location;
            Log.d(TAG, "Location updated, " + listeners.size() + " listeners are notified");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public static GeoBusinessLogic getInstance() {
        if (instance == null) {
            instance = new GeoBusinessLogic();
        }
        return instance;
    }

    private GeoBusinessLogic() {
        manager = (LocationManager) Stay63Application.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        listeners = new ArrayList<>();
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    private Location lastKnownLocation;
    public static final double SAMARA_LATITUDE = 53.2031359;
    public static final double SAMARA_LONGITUDE = 50.1593843;

    public LatLng getCurrentLatLng() {
        if (lastKnownLocation == null) {
            return new LatLng(SAMARA_LATITUDE, SAMARA_LONGITUDE);
        }
        return new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
    }

    public interface LocationListener {
        void onLocationChanged(Location location);
    }

    public void registerListener(LocationListener listener) {
        if (listeners.isEmpty()) {
            manager.requestLocationUpdates(getSafeProvider(), 0, 0, locationListener);
        }
        listeners.add(listener);
    }

    public void unregisterListener(LocationListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
        if (listeners.isEmpty()) {
            manager.removeUpdates(locationListener);
        }
    }

    public String getSafeProvider() {
        String provider = manager.getBestProvider(new Criteria(), true);
        if (provider == null || provider.isEmpty()) {
            provider = LocationManager.GPS_PROVIDER;
        }
        return provider;
    }
}

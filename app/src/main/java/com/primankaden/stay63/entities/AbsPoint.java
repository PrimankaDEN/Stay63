package com.primankaden.stay63.entities;

import com.google.android.gms.maps.model.LatLng;

public abstract class AbsPoint {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public abstract String getTitle();

    public abstract String getId();

    public LatLng getLatLng() {
        return new LatLng(getLatitude(), getLongitude());
    }
}

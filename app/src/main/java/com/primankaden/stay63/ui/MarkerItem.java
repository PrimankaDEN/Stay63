package com.primankaden.stay63.ui;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {
    private LatLng pos;

    @Override
    public LatLng getPosition() {
        return pos;
    }

    public MarkerItem(LatLng pos) {
        this.pos = pos;
    }
}

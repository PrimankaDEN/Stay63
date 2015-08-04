package com.primankaden.stay63.entities.marker;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primankaden.stay63.entities.AbsPoint;

public abstract class AbsMarker {
    private String title;
    private String id;
    private LatLng position;
    public AbsMarker(AbsPoint point) {
        title = point.getTitle();
        id = point.getId();
        position = point.getLatLng();
    }

    public final MarkerOptions getMarker() {
        return new MarkerOptions()
                .title(getTitle())
                .snippet(getId())
                .icon(getIcon())
                .position(getPosition());
    }

    public final void saveOptionsTo(Marker to){
        to.setPosition(getPosition());
        to.setTitle(getTitle());
        to.setSnippet(getId());
        to.setIcon(getIcon());
        to.setVisible(true);
    }

    protected LatLng getPosition() {
        return position;
    }

    protected String getTitle() {
        return title;
    }

    protected String getId() {
        return id;
    }

    protected abstract BitmapDescriptor getIcon();
}

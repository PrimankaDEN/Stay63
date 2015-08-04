package com.primankaden.stay63.ui.controllers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.primankaden.stay63.entities.marker.AbsMarker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MapMarkerController {
    private List<Marker> mapMarkers = new LinkedList<>();
    private List<AbsMarker> markers;
    private MapMarkerControllerListener listener;

    public MapMarkerController(MapMarkerControllerListener listener) {
        this.listener = listener;
    }

    public interface MapMarkerControllerListener {
        GoogleMap getMap();
    }

    public void setMarkers(List<AbsMarker> markers) {
        this.markers = markers;
    }

    public void drawMarkers() {
        if (listener == null) {
            return;
        }
        GoogleMap map = listener.getMap();
        if (map == null) {
            return;
        }
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        Iterator<Marker> mapMarkerIterator = mapMarkers.iterator();
        Iterator<AbsMarker> markerIterator = markers.iterator();
        List<Marker> addedMarkers = new ArrayList<>();
        AbsMarker marker;
        while (markerIterator.hasNext()) {
            marker = markerIterator.next();
            if (isMarkerVisible(bounds, marker)) {
                if (mapMarkerIterator.hasNext()) {
                    marker.saveOptionsTo(mapMarkerIterator.next());
                } else {
                    addedMarkers.add(map.addMarker(marker.getMarker()));
                }
            }
        }
        while (mapMarkerIterator.hasNext()) {
            mapMarkerIterator.next().setVisible(false);
        }
        mapMarkers.addAll(addedMarkers);
    }

    private boolean isMarkerVisible(LatLngBounds bounds, AbsMarker marker) {
        return marker.getPosition().latitude > bounds.southwest.latitude
                && marker.getPosition().latitude < bounds.northeast.latitude
                && marker.getPosition().longitude > bounds.southwest.longitude
                && marker.getPosition().longitude < bounds.northeast.longitude;
    }


}

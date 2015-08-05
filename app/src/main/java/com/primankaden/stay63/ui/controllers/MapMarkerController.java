package com.primankaden.stay63.ui.controllers;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.primankaden.stay63.entities.marker.AbsMarker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MapMarkerController {
    private static final String TAG = "MapMarkerController";
    private List<Marker> mapMarkers = new LinkedList<>();
    private List<AbsMarker> markers;
    private MapMarkerControllerListener listener;
    private static final int MAX_CASHED_MARKER_COUNT = 50;

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
        Log.d(TAG, "refreshing markers on map");
        Date starDate = new Date();
        if (listener == null) {
            return;
        }
        GoogleMap map = listener.getMap();
        if (map == null) {
            return;
        }
        int markerCounter = 0;
        int visibleMarkerCounter = 0;
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
                markerCounter++;
                visibleMarkerCounter++;
            }
        }
        while (mapMarkerIterator.hasNext()) {
            if (markerCounter > MAX_CASHED_MARKER_COUNT) {
                mapMarkerIterator.next().remove();
                mapMarkerIterator.remove();
            } else {
                mapMarkerIterator.next().setVisible(false);
            }
            markerCounter++;
        }
        mapMarkers.addAll(addedMarkers);
        Log.d(TAG, "Complete for " + (new Date().getTime() - starDate.getTime()) + " milliseconds, "
                + visibleMarkerCounter + " markers are visible"
                + mapMarkers.size() + " markers are cashed and ");
    }

    private boolean isMarkerVisible(LatLngBounds bounds, AbsMarker marker) {
        return marker.getPosition().latitude > bounds.southwest.latitude
                && marker.getPosition().latitude < bounds.northeast.latitude
                && marker.getPosition().longitude > bounds.southwest.longitude
                && marker.getPosition().longitude < bounds.northeast.longitude;
    }
}

package com.primankaden.stay63.ui.controllers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
        Iterator<Marker> mapMarkerIterator = mapMarkers.iterator();
        Iterator<AbsMarker> markerIterator = markers.iterator();
        List<Marker> addedMarkers = new ArrayList<>();
        while (markerIterator.hasNext()) {
            if (mapMarkerIterator.hasNext()) {
                markerIterator.next().saveOptionsTo(mapMarkerIterator.next());
            } else {
                addedMarkers.add(map.addMarker(markerIterator.next().getMarker()));
            }
        }
        while (mapMarkerIterator.hasNext()) {
            mapMarkerIterator.next().setVisible(false);
        }
        mapMarkers.addAll(addedMarkers);
    }


}

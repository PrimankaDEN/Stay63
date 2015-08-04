package com.primankaden.stay63.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.primankaden.stay63.entities.marker.AbsMarker;
import com.primankaden.stay63.loaders.GlobalMarkerListLoader;
import com.primankaden.stay63.loaders.Loaders;
import com.primankaden.stay63.loaders.LocalMarkerListLoader;

import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment
public class FullScreenMapFragment extends LandingMapFragment {
    private static final String TAG = "FullScreenMapFragment";
    private String lastClickedId = "";
    private boolean isFirstTimeFinished = false;
    private final static int MAX_ZOOM = 19;

    @Override
    protected void moveCameraToIfNeeded(GoogleMap map, LatLng to) {
        if (!isFirstTimeFinished) {
            super.moveCameraToIfNeeded(map, to);
            isFirstTimeFinished = true;
        }
    }

    /*@Override
    protected boolean onMarkerClick(Marker marker) {
        if ("cluster".equals(marker.getSnippet())) {
            float zoom = (getMap().getCameraPosition().zoom + 0.5f);
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoom > MAX_ZOOM ? MAX_ZOOM : zoom));
            initLoader();
        }
        return super.onMarkerClick(marker);
    }*/
    private double lastZoom;

    @Override
    protected Bundle prepareArgs() {
        return GlobalMarkerListLoader.prepareArgs(getMap().getCameraPosition().zoom, new Bundle());
    }

    @Override
    protected int getLoaderId() {
        return Loaders.GLOBAL_MARKER_LIST;
    }

    @Override
    public Loader<List<AbsMarker>> onCreateLoader(int id, Bundle args) {
        return new GlobalMarkerListLoader(getActivity(), args);
    }

    @Override
    protected void changeMapSettings(GoogleMap map) {
        /*map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (lastClickedId.equals(marker.getSnippet()) && !YOU_MARKER_ID.equals(marker.getSnippet())) {
                    StopInfoActivity.startMe(FullScreenMapFragment.this.getActivity(), marker.getSnippet());
                } else {
                    marker.showInfoWindow();
                }
                lastClickedId = marker.getSnippet();
                return true;
            }
        });
        */
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (lastZoom != cameraPosition.zoom) {
                    initLoader();
                }
                lastZoom = cameraPosition.zoom;
                Log.d(TAG, "camera changed");
            }
        });
        setInfoWindowAdapter(map);
    }
}

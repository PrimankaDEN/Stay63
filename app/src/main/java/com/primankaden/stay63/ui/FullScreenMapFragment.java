package com.primankaden.stay63.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.primankaden.stay63.entities.marker.AbsMarker;
import com.primankaden.stay63.loaders.GlobalMarkerListLoader;
import com.primankaden.stay63.loaders.Loaders;
import com.primankaden.stay63.ui.controllers.DiscreteScaleController;

import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment
public class FullScreenMapFragment extends AbsMapFragment implements DiscreteScaleController.OnScaleChangedListener{
    private static final String TAG = "FullScreenMapFragment";
    private boolean isFirstTimeFinished = false;
    private DiscreteScaleController scaleController;

    @Override
    protected void init() {
        super.init();
        scaleController = new DiscreteScaleController(this);
    }

    @Override
    protected void onLocationChanged(Location location) {

    }

    @Override
    protected void moveCameraToIfNeeded(GoogleMap map, LatLng to) {
        if (!isFirstTimeFinished) {
            super.moveCameraToIfNeeded(map, to);
            isFirstTimeFinished = true;
        }
    }

    @Override
    protected Bundle prepareLoaderArgs() {
        return GlobalMarkerListLoader.prepareArgs(getMap().getCameraPosition().zoom, new Bundle());
    }

    @Override
    protected int getLoaderId() {
        return Loaders.GLOBAL_MARKER_LIST;
    }

    @Override
    public Loader<List<AbsMarker>> getLoader(Bundle args) {
        return new GlobalMarkerListLoader(getActivity(), args);
    }

    @Override
    protected void changeMapSettings(GoogleMap map) {
        super.changeMapSettings(map);
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                scaleController.notifyScaleChanged(cameraPosition.zoom);
                Log.d(TAG, "camera changed");
            }
        });
    }

    @Override
    public void onScaleChanged(double discreteScale) {
        //TODO restart loader here
        initLoader();
    }
}

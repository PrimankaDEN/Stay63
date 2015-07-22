package com.primankaden.stay63.ui;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.EFragment;

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
                initLoader();
                Log.d(TAG, "camera changed");
            }
        });
        setInfoWindowAdapter(map);
    }
}

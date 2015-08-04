package com.primankaden.stay63.ui;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.primankaden.stay63.R;
import com.primankaden.stay63.loaders.Loaders;
import com.primankaden.stay63.loaders.LocalMarkerListLoader;

import org.androidannotations.annotations.EFragment;

@EFragment
public class LandingMapFragment extends AbsMapFragment {
    private static final String TAG = "LandingMapFragment";

    @Override
    protected int getLoaderId() {
        return Loaders.MARKER_LIST_LOADER;
    }

    @Override
    protected AsyncTaskLoader getLoader(Bundle args) {
        return new LocalMarkerListLoader(getActivity(), args);
    }

    @Override
    protected void onLocationChanged(Location location) {
        initLoader();
    }

    @Override
    protected void changeMapSettings(GoogleMap map) {
        if (map == null) {
            return;
        }
        UiSettings sets = map.getUiSettings();
        sets.setScrollGesturesEnabled(false);
        sets.setRotateGesturesEnabled(false);
        sets.setZoomGesturesEnabled(false);
        sets.setZoomControlsEnabled(false);
        sets.setTiltGesturesEnabled(false);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                FullScreenMapActivity.startMe(getActivity());
            }
        });
        setInfoWindowAdapter(map);
    }

    @Override
    protected Bundle prepareLoaderArgs() {
        return LocalMarkerListLoader.prepareArgs(getMap().getProjection().getVisibleRegion().latLngBounds, new Bundle());
    }

    protected void setInfoWindowAdapter(GoogleMap map) {
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.v_map_info_window, null);
                ((TextView) v.findViewById(R.id.info_text_view)).setText(marker.getTitle());
                return v;
            }
        });
    }
}

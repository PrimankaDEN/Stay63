package com.primankaden.stay63.ui;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.primankaden.stay63.R;
import com.primankaden.stay63.bl.GeoBusinessLogic;
import com.primankaden.stay63.entities.marker.AbsMarker;
import com.primankaden.stay63.loaders.Loaders;
import com.primankaden.stay63.loaders.LocalMarkerListLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

@EFragment
public class LandingMapFragment extends SupportMapFragment implements OnMapReadyCallback, android.support.v4.app.LoaderManager.LoaderCallbacks<List<AbsMarker>> {
    private static final String TAG = "LandingMapFragment";
    private GeoBusinessLogic.LocationListener listener;
    private static final float MAP_SCALE = 14;
    private List<AbsMarker> list = new ArrayList<>();

    @AfterViews
    protected void init() {
        listener = getLocationListener();
        changeMapSettings(getMap());
        getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return LandingMapFragment.this.onMarkerClick(marker);
            }
        });
        getMapAsync(this);
    }

    protected GeoBusinessLogic.LocationListener getLocationListener() {
        return new GeoBusinessLogic.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                initLoader();
            }
        };
    }

    protected void initLoader() {
        if (getMap() != null && getMap().getProjection().getVisibleRegion().latLngBounds != null) {
            Bundle b = prepareArgs();
            Loader loader = getActivity().getSupportLoaderManager().getLoader(getLoaderId());
            if (loader == null) {
                getActivity().getSupportLoaderManager().restartLoader(getLoaderId(), b, LandingMapFragment.this).forceLoad();
            } else {
                ((LocalMarkerListLoader) loader).setArgs(b);
                getActivity().getSupportLoaderManager().initLoader(getLoaderId(), b, LandingMapFragment.this).forceLoad();
            }
        }
    }

    protected Bundle prepareArgs(){
        return  LocalMarkerListLoader.prepareArgs(getMap().getProjection().getVisibleRegion().latLngBounds, new Bundle());
    }

    protected int getLoaderId(){
        return Loaders.MARKER_LIST_LOADER;
    }

    @Override
    public void onResume() {
        initLoader();
        super.onResume();
        GeoBusinessLogic.getInstance().registerListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        GeoBusinessLogic.getInstance().unregisterListener(listener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap == null) {
            return;
        }
        googleMap.clear();
        moveCameraToIfNeeded(googleMap, GeoBusinessLogic.getInstance().getCurrentLatLng());
        drawMarkers(googleMap, list);
    }

    protected void drawMarkers(GoogleMap map, List<AbsMarker> markerList) {
        for (AbsMarker m : markerList) {
            map.addMarker(m.getMarker());
        }
    }

    protected void moveCameraToIfNeeded(GoogleMap map, LatLng to) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(to, MAP_SCALE));
    }

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

    protected boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
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

    @Override
    public Loader<List<AbsMarker>> onCreateLoader(int id, Bundle args) {
        return new LocalMarkerListLoader(this.getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<AbsMarker>> loader, List<AbsMarker> data) {
        Log.d(TAG, "loaded " + data.size() + " stops");
        list = data;
        onMapReady(this.getMap());
    }

    @Override
    public void onLoaderReset(Loader<List<AbsMarker>> loader) {
        Log.d(TAG, "loader was reset");
    }
}

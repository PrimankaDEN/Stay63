package com.primankaden.stay63.ui;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primankaden.stay63.R;
import com.primankaden.stay63.bl.GeoBusinessLogic;
import com.primankaden.stay63.bl.StopBusinessLogic;
import com.primankaden.stay63.entities.FullStop;
import com.primankaden.stay63.loaders.Loaders;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

@EFragment
public class LandingMapFragment extends MapFragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<List<FullStop>> {
    private static final String TAG = "LandingMapFragment";
    private GeoBusinessLogic.LocationListener listener;
    private static final float MAP_SCALE = 14;
    private static final int STOPS_COUNT = 10;
    private List<FullStop> list = new ArrayList<>();
    protected static final String YOU_MARKER_ID = "You position";

    @AfterViews
    protected void init() {
        getActivity().getLoaderManager().initLoader(Loaders.NEAREST_STOP_LIST_LOADER, new Bundle(), LandingMapFragment.this).forceLoad();
        listener = new GeoBusinessLogic.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getActivity().getLoaderManager().restartLoader(Loaders.NEAREST_STOP_LIST_LOADER, new Bundle(), LandingMapFragment.this).forceLoad();
            }
        };
        changeMapSettings(getMap());
        getMapAsync(this);
    }

    @Override
    public void onResume() {
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
        LatLng currentCoords = GeoBusinessLogic.getInstance().getCurrentLatLng();
        googleMap.clear();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoords, MAP_SCALE));
        googleMap.addMarker(new MarkerOptions().title(getActivity().getString(R.string.current_postition)).position(currentCoords).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).snippet(YOU_MARKER_ID));
        for (FullStop stop : list) {
            googleMap.addMarker(new MarkerOptions().title(stop.getTitle()).position(stop.getLatLng()).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)).snippet(stop.getId()));
        }
    }

    protected void changeMapSettings(GoogleMap map) {
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

    protected void setInfoWindowAdapter(GoogleMap map){
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
    public Loader<List<FullStop>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<FullStop>>(this.getActivity()) {
            @Override
            public List<FullStop> loadInBackground() {
                return StopBusinessLogic.getInstance().getNearestStops(STOPS_COUNT);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<FullStop>> loader, List<FullStop> data) {
        Log.d(TAG, "loaded " + data.size() + " stops");
        list = data;
        onMapReady(this.getMap());
    }

    @Override
    public void onLoaderReset(Loader<List<FullStop>> loader) {
        Log.d(TAG, "loader was reset");
    }
}

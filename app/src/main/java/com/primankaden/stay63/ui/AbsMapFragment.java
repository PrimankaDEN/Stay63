package com.primankaden.stay63.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.primankaden.stay63.bl.GeoBusinessLogic;
import com.primankaden.stay63.entities.marker.AbsMarker;
import com.primankaden.stay63.loaders.ParametrizedLoader;
import com.primankaden.stay63.ui.controllers.MapMarkerController;
import com.primankaden.stay63.ui.controllers.MapMarkerController.MapMarkerControllerListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment
public abstract class AbsMapFragment extends SupportMapFragment implements OnMapReadyCallback,
        android.support.v4.app.LoaderManager.LoaderCallbacks<List<AbsMarker>>,
        MapMarkerControllerListener {
    private static final String TAG = "AbsMarkerController";
    private static final float MAP_SCALE = 10;
    private GeoBusinessLogic.LocationListener geoListener;
    private MapMarkerController markerController;

    @AfterViews
    protected void init() {
        getMapAsync(this);
        geoListener = new GeoBusinessLogic.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                AbsMapFragment.this.onLocationChanged(location);
            }
        };
        markerController = new MapMarkerController(this);
    }

    protected abstract void onLocationChanged(Location location);

    protected abstract void changeMapSettings(GoogleMap map);

    protected abstract Bundle prepareLoaderArgs();

    protected abstract int getLoaderId();

    @Override
    public Loader<List<AbsMarker>> onCreateLoader(int i, Bundle bundle) {
        return getLoader(bundle);
    }

    protected abstract AsyncTaskLoader getLoader(Bundle args);

    @Override
    public void onLoadFinished(Loader<List<AbsMarker>> loader, List<AbsMarker> absMarkerList) {
        Log.d(TAG, "loaded " + absMarkerList.size() + " markers");
        markerController.setMarkers(absMarkerList);
        markerController.drawMarkers();
    }

    @Override
    public void onLoaderReset(Loader<List<AbsMarker>> loader) {
        Log.d(TAG, "loader was reset");
    }

    protected void moveCameraToIfNeeded(GoogleMap map, LatLng to) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(to, MAP_SCALE));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap == null) {
            return;
        }
        changeMapSettings(googleMap);
        moveCameraToIfNeeded(googleMap, GeoBusinessLogic.getInstance().getCurrentLatLng());
    }

    @Override
    public void onResume() {
        initLoader();
        super.onResume();
        GeoBusinessLogic.getInstance().registerListener(geoListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        GeoBusinessLogic.getInstance().unregisterListener(geoListener);
    }

    protected final void initLoader() {
        Log.d(TAG, "Init loader");
        if (getMap() != null && getMap().getProjection().getVisibleRegion().latLngBounds != null) {
            Bundle b = prepareLoaderArgs();
            Loader loader = getActivity().getSupportLoaderManager().getLoader(getLoaderId());
            if (loader == null) {
                getActivity().getSupportLoaderManager().restartLoader(getLoaderId(), b, AbsMapFragment.this).forceLoad();
            } else {
                ((ParametrizedLoader) loader).setArgs(b);
                getActivity().getSupportLoaderManager().initLoader(getLoaderId(), b, AbsMapFragment.this).forceLoad();
            }
        }
    }
}

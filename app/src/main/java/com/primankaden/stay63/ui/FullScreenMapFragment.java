package com.primankaden.stay63.ui;

import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.primankaden.stay63.bl.StopBusinessLogic;
import com.primankaden.stay63.entities.FullStop;

import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment
public class FullScreenMapFragment extends LandingMapFragment {
    private static final int STOPS_COUNT = 25;
    private Marker lastClicked = null;

    @Override
    protected void changeMapSettings(GoogleMap map) {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //TODO how to identify markers?
                //TODO start activity after second tap
                if (marker == lastClicked) {
                    StopInfoActivity.startMe(FullScreenMapFragment.this.getActivity(), "");
                } else {
                    marker.showInfoWindow();
                }
                lastClicked = marker;
                return true;
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
}

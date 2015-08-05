package com.primankaden.stay63.ui;

import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.primankaden.stay63.bl.StopBusinessLogic;
import com.primankaden.stay63.entities.FullStop;

import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment
public class FullScreenMapFragment extends LandingMapFragment {
    private static final int STOPS_COUNT = 25;
    private String lastClickedId = "";

    @Override
    public Loader<List<FullStop>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<FullStop>>(this.getActivity()) {
            @Override
            public List<FullStop> loadInBackground() {
                return StopBusinessLogic.getInstance().getAllFullStops();
            }
        };
    }
}

package com.primankaden.stay63.loaders;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.google.android.gms.maps.model.LatLngBounds;
import com.primankaden.stay63.bl.MarkerBusinessLogic;
import com.primankaden.stay63.entities.marker.AbsMarker;

import java.util.List;

public class MarkerListLoader extends AsyncTaskLoader<List<AbsMarker>> {
    public static final String SOUTH_TAG = "south";
    public static final String NORTH_TAG = "north";
    public static final String WEST_TAG = "west";
    public static final String EAST_TAG = "east";
    private double south;
    private double north;
    private double west;
    private double east;

    public MarkerListLoader(Context c, Bundle args) {
        super(c);
        unpackArgs(args);
    }

    public void setArgs(Bundle b) {
        unpackArgs(b);
    }

    @Override
    public List<AbsMarker> loadInBackground() {
        List<AbsMarker> result;
        MarkerBusinessLogic instance = MarkerBusinessLogic.getInstance();
        result = instance.getPointsBetweenCords(south, north, west, east);
        result.add(instance.getUserMarker());
        return result;
    }

    private void unpackArgs(Bundle args) {
        if (args.containsKey(SOUTH_TAG)) {
            south = args.getDouble(SOUTH_TAG);
        }
        if (args.containsKey(NORTH_TAG)) {
            north = args.getDouble(NORTH_TAG);
        }
        if (args.containsKey(WEST_TAG)) {
            west = args.getDouble(WEST_TAG);
        }
        if (args.containsKey(EAST_TAG)) {
            east = args.getDouble(EAST_TAG);
        }
    }

    public static Bundle prepareArgs(LatLngBounds bound, Bundle b) {
        b.putDouble(SOUTH_TAG, bound.southwest.latitude);
        b.putDouble(NORTH_TAG, bound.northeast.latitude);
        b.putDouble(WEST_TAG, bound.southwest.longitude);
        b.putDouble(EAST_TAG, bound.northeast.longitude);
        return b;
    }
}

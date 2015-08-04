package com.primankaden.stay63.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.primankaden.stay63.bl.MarkerBusinessLogic;
import com.primankaden.stay63.entities.AbsPoint;
import com.primankaden.stay63.entities.marker.AbsMarker;

import java.util.Date;
import java.util.List;

public class GlobalMarkerListLoader extends AsyncTaskLoader<List<AbsMarker>> {
    private final static String SCALE_TAG = "scale";
    private static final String TAG = "GlobalMarkerListLoader";
    private double scale;

    public GlobalMarkerListLoader(Context context, Bundle args) {
        super(context);
        unpackArgs(args);
    }

    public void setArgs(Bundle b) {
        unpackArgs(b);
    }

    @Override
    public List<AbsMarker> loadInBackground() {
        Date startDate = new Date();
        List<AbsMarker> result = MarkerBusinessLogic.getInstance().getPointsForScale(scale);
        result.add(MarkerBusinessLogic.getInstance().getUserMarker());
        Log.d(TAG, "Complete for " + (new Date().getTime() - startDate.getTime()) + " milliseconds");
        return result;
    }

    public static Bundle prepareArgs(double scale, Bundle bundle) {
        bundle.putDouble(SCALE_TAG, scale);
        return bundle;
    }

    private void unpackArgs(Bundle args) {
        if (args.containsKey(SCALE_TAG)) {
            scale = args.getDouble(SCALE_TAG);
        }
    }
}

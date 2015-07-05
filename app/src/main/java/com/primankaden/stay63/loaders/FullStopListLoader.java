package com.primankaden.stay63.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;

import com.primankaden.stay63.bl.GeoBusinessLogic;
import com.primankaden.stay63.bl.StopBusinessLogic;
import com.primankaden.stay63.entities.FullStop;

import java.util.ArrayList;
import java.util.List;

public class FullStopListLoader extends AsyncTaskLoader<List<FullStop>> {

    public FullStopListLoader(Context context) {
        super(context);
    }

    @Override
    public List<FullStop> loadInBackground() {
        return StopBusinessLogic.getInstance().getAllFullStops();
    }
}

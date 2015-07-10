package com.primankaden.stay63.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.primankaden.stay63.bl.StopBusinessLogic;
import com.primankaden.stay63.entities.FullStop;

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

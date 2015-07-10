package com.primankaden.stay63.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.primankaden.stay63.R;
import com.primankaden.stay63.bl.GeoBusinessLogic;
import com.primankaden.stay63.entities.FullStop;
import com.primankaden.stay63.loaders.FullStopListLoader;
import com.primankaden.stay63.loaders.Loaders;
import com.primankaden.stay63.ui.adapters.LandingAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.a_landing)
public class LandingActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<FullStop>> {
    private final static String TAG = "LandingActivity";
    @ViewById(R.id.progress_bar)
    protected ProgressBar bar;
    @ViewById(R.id.landing_list_view)
    protected ListView listView;
    private LandingAdapter adapter;
    private GeoBusinessLogic.LocationListener listener;

    @AfterViews
    protected void init() {
        adapter = new LandingAdapter(this);
        View headerView = getLayoutInflater().inflate(R.layout.v_map_header, null);
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
        listener = new GeoBusinessLogic.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                adapter.updateLocation();
                adapter.notifyDataSetChanged();
            }
        };
        bar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        LoaderManager manager = this.getSupportLoaderManager();
        manager.initLoader(Loaders.FULL_STOP_LIST_LOADER, null, LandingActivity.this).forceLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GeoBusinessLogic.getInstance().registerListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GeoBusinessLogic.getInstance().unregisterListener(listener);
    }

    @Override
    public Loader<List<FullStop>> onCreateLoader(int id, Bundle args) {
        return new FullStopListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<FullStop>> loader, List<FullStop> data) {
        Log.d(TAG, "loaded data size " + data.size());
        adapter.setList(data);
        adapter.notifyDataSetChanged();
        bar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<FullStop>> loader) {
        bar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }
}

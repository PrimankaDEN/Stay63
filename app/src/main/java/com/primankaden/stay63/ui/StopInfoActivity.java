package com.primankaden.stay63.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.primankaden.stay63.R;
import com.primankaden.stay63.bl.StopBusinessLogic;
import com.primankaden.stay63.bl.TransportBusinessLogic;
import com.primankaden.stay63.entities.FullStop;
import com.primankaden.stay63.entities.Transport;
import com.primankaden.stay63.loaders.Loaders;
import com.primankaden.stay63.ui.adapters.ArrivedBusAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.a_stop_info)
public class StopInfoActivity extends Activity implements LoaderManager.LoaderCallbacks<StopInfoActivity.LoadData> {
    private static final String TAG = "StopInfoActivity";
    @Extra
    protected String stopId;
    @ViewById(R.id.list_view)
    protected ListView list;
    @ViewById(R.id.title_text_view)
    protected TextView title;
    @ViewById(R.id.progress_bar)
    protected ProgressBar bar;
    @ViewById(R.id.content)
    protected LinearLayout content;
    private ArrivedBusAdapter adapter;

    public static void startMe(Activity from, String stopId) {
        StopInfoActivity_.intent(from).stopId(stopId).start();
    }

    public class LoadData {
        public FullStop stop;
        public List<Transport> list;
    }

    @AfterViews
    protected void init() {
        getLoaderManager().initLoader(Loaders.TRANSPORT_LIST_LOADER, null, this).forceLoad();
        adapter = new ArrivedBusAdapter(this);
        list.setAdapter(adapter);
        bar.setVisibility(View.VISIBLE);
        content.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<LoadData> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<LoadData>(this) {
            @Override
            public LoadData loadInBackground() {
                LoadData data = new LoadData();
                data.stop = StopBusinessLogic.getInstance().getById(stopId);
                data.list = TransportBusinessLogic.getInstance().getByStopId(stopId);
                return data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<LoadData> loader, LoadData data) {
        adapter.setList(data.list);
        setTitle(data.stop.getTitle());
        title.setText(data.stop.getDirection());
        adapter.notifyDataSetChanged();
        bar.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<LoadData> loader) {

    }
}

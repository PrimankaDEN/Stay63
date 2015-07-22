package com.primankaden.stay63.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.primankaden.stay63.R;
import com.primankaden.stay63.bl.GeoBusinessLogic;
import com.primankaden.stay63.bl.StopBusinessLogic;
import com.primankaden.stay63.entities.FullStop;
import com.primankaden.stay63.ui.StopInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class LandingAdapter extends BaseAdapter {
    private Activity context;

    public void setList(List<FullStop> list) {
        this.list = list;
    }

    public void updateLocation() {
        StopBusinessLogic.getInstance().sortByLocation(list, GeoBusinessLogic.getInstance().getCurrentLocation());
    }

    private List<FullStop> list = new ArrayList<>();

    public LandingAdapter(Activity context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            v = inflater.inflate(R.layout.v_landing_row, null);
        }
        ((TextView) v.findViewById(R.id.title)).setText(((FullStop) getItem(position)).getTitle());
        ((TextView) v.findViewById(R.id.direction)).setText(((FullStop) getItem(position)).getDirection());
        ((TextView) v.findViewById(R.id.adjacent_street)).setText(((FullStop) getItem(position)).getAdjacentStreet());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopInfoActivity.startMe(context, ((FullStop) getItem(position)).getId());
            }
        });
        return v;
    }
}

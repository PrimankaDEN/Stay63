package com.primankaden.stay63.ui.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.primankaden.stay63.R;
import com.primankaden.stay63.entities.Transport;

import java.util.ArrayList;
import java.util.List;

public class ArrivedBusAdapter extends BaseAdapter {
    private List<Transport> list = new ArrayList<>();
    private Activity context;

    public ArrivedBusAdapter(Activity context) {
        this.context = context;
    }

    public void setList(List<Transport> list) {
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = context.getLayoutInflater().inflate(R.layout.v_arrived_bus_row, null, false);
        }
        ((TextView) view.findViewById(R.id.bus_number)).setText(context.getString(R.string.bus_number_title, list.get(position).getNumber()));
        ((TextView) view.findViewById(R.id.time_arrive)).setText(context.getString(R.string.time_arrived_pattern, list.get(position).getTime()));
        return view;
    }
}

package com.primankaden.stay63.entities;

import com.primankaden.stay63.R;
import com.primankaden.stay63.Stay63Application;

import java.util.ArrayList;
import java.util.List;

public class ClusterPoint extends AbsPoint {
    public List<AbsPoint> getStops() {
        return stops;
    }

    public AbsPoint get(int i) {
        return stops.get(i);
    }

    public void addAll(List<AbsPoint> stops) {
        this.stops = stops;
        double newLatitude = 0;
        double newLongitude = 0;
        for (AbsPoint p : stops) {
            newLatitude += p.getLatitude();
            newLongitude += p.getLongitude();
        }
        if (!stops.isEmpty()) {
            newLatitude /= stops.size();
            newLongitude /= stops.size();
        }
        setLatitude(newLatitude);
        setLongitude(newLongitude);
    }

    public void add(AbsPoint stop) {
        if (isEmpty()) {
            setLatitude(stop.getLatitude());
            setLongitude(stop.getLongitude());
        } else {
            setLatitude((getLatitude() + stop.getLatitude()) / 2);
            setLongitude((getLongitude() + stop.getLongitude()) / 2);
        }
        stops.add(stop);
    }

    private List<AbsPoint> stops = new ArrayList<>();

    public int size() {
        return stops.size();
    }

    public boolean isEmpty() {
        return stops.isEmpty();
    }

    @Override
    public String getTitle() {
        return String.format(Stay63Application.getAppContext().getString(R.string.cluster_title_pattern), get(0).getTitle(), size() - 1);
    }

    //TODO
    @Override
    public String getId() {
        return "Cluster";
    }
}

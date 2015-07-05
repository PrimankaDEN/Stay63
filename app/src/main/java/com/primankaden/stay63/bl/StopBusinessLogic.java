package com.primankaden.stay63.bl;

import android.location.Location;
import android.util.Log;

import com.primankaden.stay63.XmlUtils;
import com.primankaden.stay63.entities.FullStop;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StopBusinessLogic {
    private static final String TAG = "StopBusinessLogic";
    private final static long UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(5);
    private static StopBusinessLogic instance;
    private final StopRepository repository;

    private StopBusinessLogic() {
        repository = new StopRepository();
    }

    public static StopBusinessLogic getInstance() {
        if (instance == null) {
            instance = new StopBusinessLogic();
        }
        return instance;
    }

    public List<FullStop> getNearestStops(int count) {
        List<FullStop> result = new ArrayList<FullStop>();
        List<FullStop> list = getAllFullStops();
        sortByLocation(list, GeoBusinessLogic.getInstance().getLastKnownLocation());
        for (int i = 0; i < count && i < list.size(); i++) {
            result.add(list.get(i));
        }
        return result;
    }

    public List<FullStop> getAllFullStops() {
        syncFullStopList();
        return repository.getFullStopList();
    }

    public void syncFullStopList() {
        if (isFullStopSynced()) {
            Log.d(TAG, "sync canceled");
            return;
        }
        List<FullStop> list = new ArrayList<>();
        String stopsXml = PublicAPI.getXml(PublicAPI.CLASSIFIER_FULL_STOPS);
        Document doc = XmlUtils.getDomElement(stopsXml);
        if (doc != null) {
            NodeList nl = doc.getElementsByTagName("stop");
            for (int i = 0; i < nl.getLength(); i++) {
                FullStop stop = new FullStop();
                stop.init((Element) nl.item(i));
                list.add(stop);
            }
        }
        repository.saveFullStopList(list);
        Log.d(TAG, "sync complete");
    }

    public List<FullStop> sortByLocation(List<FullStop> list, Location location) {
        if (location == null) {
            return list;
        }
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();

        Collections.sort(list, new Comparator<FullStop>() {
            @Override
            public int compare(FullStop lhs, FullStop rhs) {
                double lRes = Math.sqrt(Math.pow(latitude - lhs.getLatitude(), 2) + Math.pow(longitude - lhs.getLongitude(), 2));
                double rRes = Math.sqrt(Math.pow(latitude - rhs.getLatitude(), 2) + Math.pow(longitude - rhs.getLongitude(), 2));
                return lRes < rRes ? -1 : 1;
            }
        });
        return list;
    }

    public boolean isFullStopSynced() {
        return Math.abs(repository.getFullStopUpdateTime() - new Date().getTime()) < UPDATE_INTERVAL;
    }

    public FullStop getById(String id) {
        List<FullStop> list = getAllFullStops();
        for (FullStop stop : list) {
            if (stop.getId().equals(id)) {
                return stop;
            }
        }
        return null;
    }
}

package com.primankaden.stay63.bl;

import android.util.Log;

import com.primankaden.stay63.XmlUtils;
import com.primankaden.stay63.entities.FullStop;
import com.primankaden.stay63.entities.Transport;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TransportBusinessLogic {
    private final static long UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(1);
    private static final String TAG = "TransportBusinessLogic";
    private static TransportBusinessLogic instance;

    private TransportBusinessLogic() {
        repository = new TransportRepository();
    }

    public static TransportBusinessLogic getInstance() {
        if (instance == null) {
            instance = new TransportBusinessLogic();
        }
        return instance;
    }

    private final TransportRepository repository;

    public List<Transport> getByStopId(String stopId) {
        syncTransportList(stopId);
        return repository.getByStopId(stopId);
    }

    private void syncTransportList(String stopId) {
        if (isSynced(stopId)) {
            Log.d(TAG, "sync canceled");
            return;
        }
        String response = PublicAPI.getTransportXml(stopId, 10);
        Document doc = XmlUtils.getDomElement(response);
        List<Transport> list = new ArrayList<>();
        if (doc != null) {
            NodeList nl = doc.getElementsByTagName("transport");
            for (int i = 0; i < nl.getLength(); i++) {
                Transport tr = new Transport();
                tr.init((Element) nl.item(i));
                list.add(tr);
            }
        }
        repository.setByStopId(stopId, list);

    }

    public boolean isSynced(String stopId) {
        return Math.abs(repository.getTimeUpdated(stopId) - new Date().getTime()) < UPDATE_INTERVAL;
    }
}

package com.primankaden.stay63.bl;

import com.primankaden.stay63.entities.Transport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransportRepository {
    private final HashMap<String, List<Transport>> container;
    private final HashMap<String, Long> timeUpdated;

    public TransportRepository() {
        container = new HashMap<>();
        timeUpdated = new HashMap<>();
    }

    public List<Transport> getByStopId(String stopId) {
        if (container.containsKey(stopId)) {
            return container.get(stopId);
        } else {
            return new ArrayList<Transport>();
        }
    }

    public void setByStopId(String stopId, List<Transport> list) {
        container.put(stopId, list);
        timeUpdated.put(stopId, new Date().getTime());
    }

    public Long getTimeUpdated(String stopId) {
        if (timeUpdated.containsKey(stopId)) {
            return timeUpdated.get(stopId);
        } else {
            return 0l;
        }
    }
}

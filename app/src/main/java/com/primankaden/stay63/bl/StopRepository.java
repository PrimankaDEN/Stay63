package com.primankaden.stay63.bl;

import com.primankaden.stay63.entities.FullStop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StopRepository {
    private final List<FullStop> fullStopList = new ArrayList<>();
    private long fullStopUpdateTime = 0;

    public long getFullStopUpdateTime() {
        return fullStopUpdateTime;
    }

    public List<FullStop> getFullStopList() {
        return fullStopList;
    }

    public void saveFullStopList(List<FullStop> fullStopList) {
        this.fullStopList.clear();
        this.fullStopList.addAll(fullStopList);
        fullStopUpdateTime = new Date().getTime();
    }
}

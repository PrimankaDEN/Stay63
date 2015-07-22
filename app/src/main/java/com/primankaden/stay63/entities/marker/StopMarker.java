package com.primankaden.stay63.entities.marker;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.primankaden.stay63.entities.AbsPoint;

public class StopMarker extends AbsMarker{
    public StopMarker(AbsPoint point) {
        super(point);
    }

    @Override
    protected BitmapDescriptor getIcon() {
        return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
    }
}

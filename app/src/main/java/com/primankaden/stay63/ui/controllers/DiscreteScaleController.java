package com.primankaden.stay63.ui.controllers;

import android.util.Log;

public class DiscreteScaleController {
    private static final String TAG = "DiscreteScaleController";

    public interface OnScaleChangedListener{
        public void onScaleChanged(double discreteScale);
    }
    private int lastLoadedScale;
    private OnScaleChangedListener listener;

    public DiscreteScaleController(OnScaleChangedListener listener){
        this.listener = listener;
    }

    public void notifyScaleChanged(double scale){
        int newScale = (int) scale;
        if (lastLoadedScale!=newScale && listener!=null){
            listener.onScaleChanged(newScale);
            Log.d(TAG, "scale changed");
        } else{
            Log.d(TAG, "scale did not change");
        }
    }

}

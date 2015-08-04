package com.primankaden.stay63.ui;

public class DiscreteScaleController {
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
        }
    }

}

package com.primankaden.stay63.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.primankaden.stay63.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.a_fullscreen_map)
public class FullScreenMapActivity extends FragmentActivity {
    public static void startMe(Context from) {
        FullScreenMapActivity_.intent(from).start();
    }
}

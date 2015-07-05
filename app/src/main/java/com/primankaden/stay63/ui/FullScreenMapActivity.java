package com.primankaden.stay63.ui;

import android.app.Activity;
import android.content.Context;

import com.primankaden.stay63.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.a_fullscreen_map)
public class FullScreenMapActivity extends Activity {
    public static void startMe(Context from) {
        FullScreenMapActivity_.intent(from).start();
    }
}

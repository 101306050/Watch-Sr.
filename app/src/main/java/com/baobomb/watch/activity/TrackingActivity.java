package com.baobomb.watch.activity;

import android.app.Activity;
import android.os.Bundle;

import com.baobomb.watch.R;

/**
 * Created by baobomb on 2015/10/17.
 */
public class TrackingActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

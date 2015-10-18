package com.baobomb.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.baobomb.watch.R;
import com.baobomb.watch.list.TrackAdapter;
import com.baobomb.watch.parse.ParseUtil;
import com.baobomb.watch.util.dialog.CheckDialog;
import com.baobomb.watch.util.dialog.EditDialog;
import com.baobomb.watch.util.dialog.Progress;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button track;
    EditDialog editDialog;
    CheckDialog checkDialog;
    Progress progress;
    ListView trackList;
    TrackAdapter trackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackList = (ListView) findViewById(R.id.trackList);
        track = (Button) findViewById(R.id.track);
        track.setOnClickListener(this);
        editDialog = new EditDialog();
        editDialog.bind(this);
        checkDialog = new CheckDialog();
        checkDialog.bind(this);
        progress = new Progress();
        progress.bind(this);
        trackAdapter = new TrackAdapter(this);
        trackList.setAdapter(trackAdapter);
        trackList.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ParseUser.getCurrentUser() != null) {
            initTrackList();
        }
    }

    public void initTrackList() {
        progress.showProgress("確認裝置", "驗證中....");
        ParseUtil.saveInstallation();
        List<String> trackIds = new ArrayList<>();
        trackIds = ParseUtil.getTrackId();
        if (trackIds != null && trackIds.size() > 0) {
            trackAdapter.setItems(trackIds);
        }
        progress.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.track:
                editDialog.showDialog("請輸入裝置ID", new EditDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClick(String condition) {
                        progress.showProgress("搜尋並連結裝置", "驗證中...");
                        ParseUtil.trackChannel(condition, new ParseUtil.OnTrackListener() {
                            @Override
                            public void onFinish() {
                                progress.dismiss();
                                checkDialog.showDialog("成功", "成功追蹤裝置", null);
                                initTrackList();
                            }

                            @Override
                            public void onFail(String error) {
                                progress.dismiss();
                                checkDialog.showDialog("失敗", "裝置不存在或網路發生錯誤", null);
                            }
                        });
                    }
                });
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.setClass(this, TrackingActivity.class);
        intent.putExtra("TrackId", trackAdapter.getItem(i));
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

package com.baobomb.watch.activity;

import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baobomb.watch.R;
import com.baobomb.watch.parse.ParseUtil;
import com.baobomb.watch.parse.model.UserRestrict;
import com.baobomb.watch.parse.model.WatchLocation;
import com.baobomb.watch.util.dialog.CheckDialog;
import com.baobomb.watch.util.dialog.EditDialog;
import com.baobomb.watch.util.dialog.Progress;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baobomb on 2015/10/17.
 */
public class TrackingActivity extends FragmentActivity {

    String id;
    TextView watchID, watchName, latitude, longitude, altitude, restrict, restrictLngText,
            restrictLatText, restrictMetersText;
    RelativeLayout titleLayout, restrictLayout;
    ArrayList<WatchLocation> newLocations = new ArrayList<>();
    private GoogleMap mMap;
    boolean isRestrictMode = false;
    EditDialog editDialog;
    CheckDialog checkDialog;
    Progress progress;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        restrictLayout = (RelativeLayout) findViewById(R.id.restrictLayout);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        watchID = (TextView) findViewById(R.id.watchID);
        watchName = (TextView) findViewById(R.id.watchName);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        altitude = (TextView) findViewById(R.id.altitude);
        restrictLngText = (TextView) findViewById(R.id.restrictLng);
        restrictLatText = (TextView) findViewById(R.id.restrictLat);
        restrictMetersText = (TextView) findViewById(R.id.restrictMeters);
        restrict = (TextView) findViewById(R.id.restrict);
        restrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRestrictMode) {
                    titleLayout.setVisibility(View.GONE);
                    restrictLayout.setVisibility(View.VISIBLE);
                    isRestrictMode = true;
                }

            }
        });
        setUpMapIfNeeded();
        editDialog = new EditDialog();
        editDialog.bind(this);
        checkDialog = new CheckDialog();
        checkDialog.bind(this);
        progress = new Progress();
        progress.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String notiID = getIntent().getStringExtra("Noti");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        id = getIntent().getStringExtra("TrackId");
        watchID.setText("ID : " + id);
        setUpWatchDetail(id);
        ParseUtil.getWatchLocation(id, new ParseUtil.OnGetLocationListener() {
            @Override
            public void onSuccess(List<WatchLocation> watchLocations) {
                newLocations.clear();
                if (watchLocations.size() > 5) {
                    for (int i = watchLocations.size() - 5; i < watchLocations.size(); i++) {
                        newLocations.add(watchLocations.get(i));
                    }
                } else {
                    for (int i = 0; i < watchLocations.size(); i++) {
                        newLocations.add(watchLocations.get(i));
                    }
                }

                for (int i = 0; i < newLocations.size(); i++) {
                    Log.d("Bao", newLocations.get(i).getCreatedAt().toString());
                }

                if (newLocations.size() > 0) {
                    setUpMap(newLocations.get(newLocations.size() - 1));
                    try {
                        addMarker(newLocations);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onNone() {

            }
        });
    }

    private void setUpWatchDetail(String userID) {
        ParseUtil.getUser(userID, new ParseUtil.OnGetUserListener() {
            @Override
            public void onSuccess(ParseUser parseUser) {
                watchName.setText("Name : " + parseUser.getUsername());
                user = parseUser;
                ParseUtil.getRestrict(user, new ParseUtil.OnRestrictGetListener() {
                    @Override
                    public void onSuccess(UserRestrict userRestrict) {
                        restrictLngText.setText("中心點經度 : "+userRestrict.getLongitude());
                        restrictLatText.setText("中心點緯度 : "+userRestrict.getLatitude());
                        restrictMetersText.setText("活動距離 : "+userRestrict.getMeters());
                    }

                    @Override
                    public void onNone() {

                    }
                });
            }

            @Override
            public void onNone() {

            }
        });

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (isRestrictMode) {
                        final double lat = latLng.latitude;
                        final double lng = latLng.longitude;
                        checkDialog.showDialog("確認", "選定活動中心點為 : " + String.valueOf((int) lat) +
                                ", " + String.valueOf((int) lng), new CheckDialog.OnDialogClickListener() {
                            @Override
                            public void onDialogClick(Boolean result) {
                                if (result) {
                                    editDialog.showDialog("請設定可活動半徑",
                                            new EditDialog.OnDialogClickListener() {
                                                @Override
                                                public void onDialogClick(String condition) {
                                                    if (condition != null) {
                                                        progress.showProgress("請稍後", "設定中...");
                                                        ParseUtil.setRestrict(String.valueOf(lat)
                                                                , String.valueOf(lng),
                                                                condition, user, new ParseUtil.OnRestrictListener() {
                                                                    @Override
                                                                    public void onSuccess() {
                                                                        checkDialog
                                                                                .showAlertDialog
                                                                                        ("成功", "限制成功", null);
                                                                        progress.dismiss();
                                                                    }

                                                                    @Override
                                                                    public void onNone() {
                                                                        checkDialog
                                                                                .showAlertDialog
                                                                                        ("警告", "限制失敗", null);
                                                                        progress.dismiss();
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void setUpMap(WatchLocation location) {
        // 建立位置的座標物件
        LatLng place = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(
                location.getLongitude()));
        // 移動地圖
        latitude.setText("緯度 : " + location.getLatitude());
        longitude.setText("經度 : " + location.getLongitude());
        altitude.setText("海拔 : " + location.getAltitude());
        try {
            moveMap(place);
        } catch (Exception e) {
        }

    }

    private void addMarker(ArrayList<WatchLocation> locations) {
        for (int i = 0; i < locations.size(); i++) {
            LatLng place = new LatLng(Double.parseDouble(locations.get(i).getLatitude()),
                    Double.parseDouble(locations.get(i).getLongitude()));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(place)
                    .title(locations.get(i).getCreatedAt().toString())
                    .snippet("地點" + String.valueOf(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);
        }

        PolylineOptions polylineOpt = new PolylineOptions();
        for (int j = 0; j < locations.size(); j++) {
            polylineOpt.add(new LatLng(Double.parseDouble(locations.get(j).getLatitude()),
                    Double.parseDouble(locations.get(j).getLongitude())));
        }

        polylineOpt.color(Color.BLUE);
        Polyline polyline = mMap.addPolyline(polylineOpt);
        polyline.setWidth(10);
    }

    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();
        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onBackPressed() {
        if (isRestrictMode) {
            titleLayout.setVisibility(View.VISIBLE);
            restrictLayout.setVisibility(View.GONE);
            isRestrictMode = false;
        } else {
            super.onBackPressed();
        }
    }
}

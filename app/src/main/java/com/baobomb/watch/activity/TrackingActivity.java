package com.baobomb.watch.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.baobomb.watch.R;
import com.baobomb.watch.parse.ParseUtil;
import com.baobomb.watch.parse.model.WatchLocation;
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
    TextView watchID, watchName, latitude, longitude, altitude;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        watchID = (TextView) findViewById(R.id.watchID);
        watchName = (TextView) findViewById(R.id.watchName);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        altitude = (TextView) findViewById(R.id.altitude);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        id = getIntent().getStringExtra("TrackId");
        watchID.setText("ID : " + id);
        setUpWatchDetail(id);
        ParseUtil.getWatchLocation(id, new ParseUtil.OnGetLocationListener() {
            @Override
            public void onSuccess(List<WatchLocation> watchLocations) {
                ArrayList<WatchLocation> newLocations = new ArrayList<>();
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
}

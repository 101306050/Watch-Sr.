package com.baobomb.watch.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by baobomb on 2015/10/20.
 */
@ParseClassName("watchlocation")
public class WatchLocation extends ParseObject {
    public String getLatitude() {
        return getString("latitude");
    }

    public void setLatitude(String latitude) {
        put("latitude", latitude);
    }

    public String getLongitude() {
        return getString("longitude");
    }

    public void setLongitude(String longitude) {
        put("longitude", longitude);
    }

    public String getWatchID() {
        return getString("watchid");
    }

    public void setWatchID(String watchid) {
        put("watchid", watchid);
    }
}

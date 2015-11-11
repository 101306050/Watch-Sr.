package com.baobomb.watch.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by baobomb on 2015/10/20.
 */
@ParseClassName("userRestrict")
public class UserRestrict extends ParseObject {


    public String getLatitude() {
        return getString("restrictLat");
    }

    public void setLatitude(String latitude) {
        put("restrictLat", latitude);
    }

    public String getLongitude() {
        return getString("restrictLon");
    }

    public void setLongitude(String longitude) {
        put("restrictLon", longitude);
    }

    public String getMeters() {
        return getString("restrictMeters");
    }

    public void setMeters(String meters) {
        put("restrictMeters", meters);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser parseUser) {
        put("user", parseUser);
    }
}

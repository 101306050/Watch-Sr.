package com.baobomb.watch.list;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by baobomb on 2015/10/8.
 */
@ParseClassName("trackPosition")
public class TrackPosition extends ParseObject {
    public String getID() {
        return getString("id");
    }

    public void setID(String value) {
        put("id", value);
    }

    public String getPosition1() {
        return getString("position1");
    }

    public void setPosition1(String value) {
        put("position1", value);
    }

    public String getPosition2() {
        return getString("position2");
    }

    public void setPosition2(String value) {
        put("position2", value);
    }

    public String getPosition3() {
        return getString("position3");
    }

    public void setPosition3(String value) {
        put("position3", value);
    }

    public String getPosition4() {
        return getString("position4");
    }

    public void setPosition4(String value) {
        put("position4", value);
    }

    public String getPosition5() {
        return getString("position5");
    }

    public void setPosition5(String value) {
        put("position5", value);
    }

}

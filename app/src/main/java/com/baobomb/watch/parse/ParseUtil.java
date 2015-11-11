package com.baobomb.watch.parse;

import android.util.Log;

import com.baobomb.watch.parse.model.UserRestrict;
import com.baobomb.watch.parse.model.WatchLocation;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by baobomb on 2015/10/13.
 */
public class ParseUtil {
    public static void login(String name, String password, final OnLoginListener onLoginListener) {
        ParseUser.logInInBackground(name, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    onLoginListener.onFinish();
                } else {
                    onLoginListener.onFail();
                }
            }
        });
    }

    public static boolean logout() {
        ParseUser.logOut();
        if (ParseUser.getCurrentUser() == null) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveInstallation() {
        if (ParseUser.getCurrentUser() != null) {
            ParseInstallation ins = ParseInstallation.getCurrentInstallation();
            ins.put("user", ParseUser.getCurrentUser());
            ins.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.d("", e.toString());
                    }
                }
            });
        }
    }

    public static void checkId(String id, final OnCheckListener onCheckListener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        onCheckListener.onExit();
                    } else {
                        onCheckListener.onNone();
                    }
                } else {
                    onCheckListener.onNone();
                }
            }
        });
    }

    public static void trackChannel(final String trackID, final OnTrackListener onTrackListener) {

        checkId(trackID, new OnCheckListener() {
            @Override
            public void onExit() {
                ParsePush.subscribeInBackground(trackID, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            onTrackListener.onFinish();
                        } else {
                            onTrackListener.onFail(e.toString());
                        }
                    }
                });
            }

            @Override
            public void onNone() {
                onTrackListener.onFail("ID不存在");
            }
        });
    }

    public static void getWatchLocation(String watchid, final OnGetLocationListener onGetLocationListener) {
        ParseQuery<WatchLocation> query = ParseQuery.getQuery(WatchLocation.class);
        query.whereEqualTo("watchid", watchid);
        query.findInBackground(new FindCallback<WatchLocation>() {
            @Override
            public void done(List<WatchLocation> watchLocations, ParseException e) {
                if (e == null && watchLocations.size() > 0) {
                    onGetLocationListener.onSuccess(watchLocations);
                } else {
                    onGetLocationListener.onNone();
                }
            }
        });
    }

    public static void getUser(String watchid, final OnGetUserListener onGetUserListener) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", watchid);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null && parseUsers != null && parseUsers.size() > 0) {
                    onGetUserListener.onSuccess(parseUsers.get(0));
                } else {
                    onGetUserListener.onNone();
                }
            }
        });
    }

    public static void signUp(String name, String password, String email, final OnSignUpListener onSignUpListener) {
        JSONArray jsonArray = new JSONArray();
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        user.put("usertype", "mobile");
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    saveInstallation();
                    onSignUpListener.onFinish();
                } else {
                    onSignUpListener.onFail();
                }
            }
        });
    }

    public static void getRestrict(ParseUser parseUser, final OnRestrictGetListener onRestrictGetListener) {
        checkUserRestricted(parseUser, new OnUserRestrictedCheckListener() {
            @Override
            public void onSuccess(UserRestrict userRestrict) {
                onRestrictGetListener.onSuccess(userRestrict);
            }

            @Override
            public void onNone() {
                onRestrictGetListener.onNone();
            }
        });
    }

    public static void setRestrict(final String lat, final String lng, final String restrictMeters,
                                   final ParseUser parseUser, final OnRestrictListener onRestrictListener) {
        checkUserRestricted(parseUser, new OnUserRestrictedCheckListener() {
            @Override
            public void onSuccess(UserRestrict userRestrict) {
                userRestrict.setLatitude(lat);
                userRestrict.setLongitude(lng);
                userRestrict.setMeters(restrictMeters);
                userRestrict.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        onRestrictListener.onSuccess();
                    }
                });
            }

            @Override
            public void onNone() {
                UserRestrict userRestrict = new UserRestrict();
                userRestrict.setUser(parseUser);
                userRestrict.setLatitude(lat);
                userRestrict.setLongitude(lng);
                userRestrict.setMeters(restrictMeters);
                userRestrict.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        onRestrictListener.onSuccess();
                    }
                });
            }
        });
    }

    public static void checkUserRestricted(ParseUser parseUser, final OnUserRestrictedCheckListener
            onUserRestrictedCheckListener) {
        ParseQuery<UserRestrict> query = ParseQuery.getQuery(UserRestrict.class);
        query.whereEqualTo("user", parseUser);
        query.findInBackground(new FindCallback<UserRestrict>() {
            @Override
            public void done(List<UserRestrict> userRestricts, ParseException e) {
                if (userRestricts != null && userRestricts.size() > 0) {
                    onUserRestrictedCheckListener.onSuccess(userRestricts.get(0));
                } else {
                    onUserRestrictedCheckListener.onNone();
                }
            }
        });
    }

    public static List<String> getTrackId() {
        List<String> trackIds;
        trackIds = ParseInstallation.getCurrentInstallation().getList("channels");
        return trackIds;
    }

    public static void checkWatchUpdate(final OnCheckTimeListener onCheckTimeListener) {
        final List<String> users = getTrackId();
        if (users != null) {
            for (int i = 0; i < users.size(); i++) {
                final int p = i;
                getWatchLocation(users.get(i), new OnGetLocationListener() {
                    @Override
                    public void onSuccess(List<WatchLocation> watchLocations) {
                        long createTime = watchLocations.get(watchLocations.size() - 1).getCreatedAt
                                ().getTime();
                        onCheckTimeListener.onSuccess(createTime, users.get(p));
                    }

                    @Override
                    public void onNone() {

                    }
                });
            }
        }
    }

    public static void checkPosition(String id, String lat, String lng, final OnRestrictCheckListener onRestrictCheckListener) {
        final double nowLat = Double.parseDouble(lat);
        final double notLng = Double.parseDouble(lng);
        getUser(id, new OnGetUserListener() {
            @Override
            public void onSuccess(ParseUser parseUser) {
                getRestrict(parseUser, new OnRestrictGetListener() {
                    @Override
                    public void onSuccess(UserRestrict userRestrict) {
                        double restrictLat = Double.parseDouble(userRestrict.getLatitude());
                        double restrictLng = Double.parseDouble(userRestrict.getLongitude());
                        double distance = Distance(notLng, nowLat, restrictLat, restrictLng);
                        if (distance > 1000) {
                            onRestrictCheckListener.onOut(distance);
                        } else {
                            onRestrictCheckListener.onIn();
                        }
                    }

                    @Override
                    public void onNone() {
                        onRestrictCheckListener.onUnknow();
                    }
                });
            }

            @Override
            public void onNone() {
                onRestrictCheckListener.onUnknow();
            }
        });
    }

    public static double Distance(double longitude1, double latitude1, double longitude2,
                                  double latitude2) {
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;
        return distance;
    }

    public static abstract class OnLoginListener {
        public abstract void onFinish();

        public abstract void onFail();
    }


    public static abstract class OnTrackListener {
        public abstract void onFinish();

        public abstract void onFail(String error);
    }

    public static abstract class OnSignUpListener {
        public abstract void onFinish();

        public abstract void onFail();
    }


    public static abstract class OnCheckListener {
        public abstract void onExit();

        public abstract void onNone();
    }

    public static abstract class OnGetLocationListener {
        public abstract void onSuccess(List<WatchLocation> watchLocations);

        public abstract void onNone();
    }

    public static abstract class OnGetUserListener {
        public abstract void onSuccess(ParseUser parseUser);

        public abstract void onNone();
    }

    public static abstract class OnCheckTimeListener {
        public abstract void onSuccess(long time, String id);

        public abstract void onNone();
    }

    public static abstract class OnUserRestrictedCheckListener {
        public abstract void onSuccess(UserRestrict userRestrict);

        public abstract void onNone();
    }

    public static abstract class OnRestrictListener {
        public abstract void onSuccess();

        public abstract void onNone();
    }

    public static abstract class OnRestrictGetListener {
        public abstract void onSuccess(UserRestrict userRestrict);

        public abstract void onNone();
    }


    public static abstract class OnRestrictCheckListener {
        public abstract void onOut(double distance);

        public abstract void onIn();

        public abstract void onUnknow();
    }
}

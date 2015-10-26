package com.baobomb.watch.parse;

import android.util.Log;

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

    public static List<String> getTrackId() {
        List<String> trackIds;
        trackIds = ParseInstallation.getCurrentInstallation().getList("channels");
        return trackIds;
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
}

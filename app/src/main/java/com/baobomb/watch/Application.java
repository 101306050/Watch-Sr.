package com.baobomb.watch;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by baobomb on 2015/10/13.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "zgukLmrcLTc23zoUmnz9Brx4xtwzB2zwZdYk5lpB", "ctt8RVwUoOXeLJvu7JFuo0Cfu3GEeEdqeRZkp3cc");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

package com.glen.mysimple.application;

import android.app.Application;

/**
 * Created by Gln on 2017/6/12.
 * @function
 */
public class MyApplication extends Application {
    private static MyApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static MyApplication getInstance() {
        return mApplication;
    }
}

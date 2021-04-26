package com.baihe.serialdebughelper;

import android.app.Application;
import android.content.Context;

import cn.wch.lib.ch34xMultiManager;

public class MyApplication extends Application {
    public static ch34xMultiManager ch34xmag;
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static Context getAppContext() {
        return myApplication.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return myApplication;
    }
}

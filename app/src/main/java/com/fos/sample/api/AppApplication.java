package com.fos.sample.api;


import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * APPLICATION
 */
public class AppApplication extends Application {
    private static AppApplication appApplication;
    public static String PACKAGE_NAME;

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
        PACKAGE_NAME = getPackageName();
        init();
    }



    private void init() {
        ZXingLibrary.initDisplayOpinion(this);
    }

    public AppApplication() {
        super();
    }

    public static AppApplication self(){
        return appApplication;
    }



}

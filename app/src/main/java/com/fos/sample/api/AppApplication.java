package com.fos.sample.api;


import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

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
        SpeechUtility.createUtility(appApplication, SpeechConstant.APPID +"=5bab1996");
    }

    public AppApplication() {
        super();
    }

    public static AppApplication self(){
        return appApplication;
    }



}

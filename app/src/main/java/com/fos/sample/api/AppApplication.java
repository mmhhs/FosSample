package com.fos.sample.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.fos.fosmvp.common.base.BaseApplication;
import com.fos.fosmvp.common.utils.LogUtils;

/**
 * Created by mmh on 2018/7/17.
 */
public class AppApplication extends BaseApplication implements Application.ActivityLifecycleCallbacks {
    private static AppApplication appApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
        AppConstants.init();
        LogUtils.e("---------------start fosmvp----------------"+ appApplication);
    }

    public static Context getAppContext() {
        return appApplication;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}

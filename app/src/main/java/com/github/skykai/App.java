package com.github.skykai;

import android.app.Application;
import android.content.Context;

import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.github.skykai.util.BoxingGlideLoader;

/**
 * Created by sky on 17/2/28.
 */

public class App extends Application {
    private static Context appContext;

    public static Context getContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        IBoxingMediaLoader loader = new BoxingGlideLoader();
        BoxingMediaLoader.getInstance().init(loader);
    }
}

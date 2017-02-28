package com.github.skykai;

import android.app.Application;

import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;

/**
 * Created by sky on 17/2/28.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IBoxingMediaLoader loader = new BoxingGlideLoader();
        BoxingMediaLoader.getInstance().init(loader);
    }
}

package com.github.skykai.ui;

import com.github.skykai.App;

/**
 * Created by sky on 17/3/1.
 */

public class ViewUtils {

    public static int dip2px(float dpValue) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth() {
        return App.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return App.getContext().getResources().getDisplayMetrics().heightPixels;
    }
}

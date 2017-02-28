package com.github.skykai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bilibili.boxing.AbsBoxingActivity;
import com.bilibili.boxing.AbsBoxingViewFragment;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing_impl.ui.BoxingViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 17/2/28.
 */

public class PhotoPickerActivity extends AbsBoxingActivity {
    private BoxingViewFragment mPickerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
    }

    @NonNull
    @Override
    public AbsBoxingViewFragment onCreateBoxingView(ArrayList<BaseMedia> medias) {
        mPickerFragment = (BoxingViewFragment) getSupportFragmentManager().findFragmentByTag(BoxingViewFragment.TAG);
        if (mPickerFragment == null) {
            mPickerFragment = (BoxingViewFragment) BoxingViewFragment.newInstance().setSelectedBundle(medias);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, mPickerFragment, BoxingViewFragment.TAG).commit();
        }
        return mPickerFragment;
    }

    @Override
    public void onBoxingFinish(Intent intent, @Nullable List<BaseMedia> medias) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}

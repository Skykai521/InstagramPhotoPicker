package com.github.skykai.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bilibili.boxing.AbsBoxingActivity;
import com.bilibili.boxing.AbsBoxingViewFragment;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.github.skykai.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 17/2/28.
 */

public class PhotoPickerActivity extends AbsBoxingActivity {
    private PhotoPickerFragment pickerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
    }

    @NonNull
    @Override
    public AbsBoxingViewFragment onCreateBoxingView(ArrayList<BaseMedia> medias) {
        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag(PhotoPickerFragment.TAG);
        if (pickerFragment == null) {
            BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG);
            pickerFragment = PhotoPickerFragment.newInstance();
            pickerFragment.setPickerConfig(config);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, pickerFragment, PhotoPickerFragment.TAG).commit();
        }
        return pickerFragment;
    }

    @Override
    public void onBoxingFinish(Intent intent, @Nullable List<BaseMedia> medias) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}

package com.github.skykai.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilibili.boxing.AbsBoxingViewFragment;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing_impl.adapter.BoxingMediaAdapter;
import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import com.github.skykai.R;
import com.github.skykai.view.CoordinatorLinearLayout;
import com.github.skykai.view.CoordinatorRecyclerView;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by sky on 17/2/28.
 */

public class PhotoPickerFragment extends AbsBoxingViewFragment {
    public static final String TAG = "com.github.skykai.ui.PhotoPickerFragment";
    private static final int GRID_COUNT = 3;
    private static final int TOP_REMAIN_HEIGHT = 48;
    private Toolbar toolbar;
    private PhotoView photoView;
    private TextView albumTextView;
    private CoordinatorLinearLayout parentLayout;
    private CoordinatorRecyclerView photoRecyclerView;
    private BoxingMediaAdapter mediaAdapter;

    public static PhotoPickerFragment newInstance() {
        return new PhotoPickerFragment();
    }

    @Override
    public void onCreateWithSelectedMedias(Bundle bundle, @Nullable List<BaseMedia> selectedMedias) {
        mediaAdapter = new BoxingMediaAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        parentLayout = (CoordinatorLinearLayout) view.findViewById(R.id.parent_layout);
        photoRecyclerView = (CoordinatorRecyclerView) view.findViewById(R.id.photo_recycler_view);
        albumTextView = (TextView) view.findViewById(R.id.album_text_view);
        photoView = (PhotoView) view.findViewById(R.id.photo_view);
        setLayoutSize();
        initToolBar();
        initRecyclerView();
    }

    private void setLayoutSize() {
        int topViewHeight = ViewUtils.dip2px(TOP_REMAIN_HEIGHT) + ViewUtils.getScreenWidth();
        int topBarHeight = ViewUtils.dip2px(TOP_REMAIN_HEIGHT);
        parentLayout.setTopViewParam(topViewHeight, topBarHeight);
        photoView.getLayoutParams().height = ViewUtils.getScreenWidth();
        photoRecyclerView.getLayoutParams().height = ViewUtils.getScreenHeight() - topBarHeight;
        parentLayout.getLayoutParams().height = topViewHeight + ViewUtils.getScreenHeight() - topBarHeight;
        photoRecyclerView.setCoordinatorListener(parentLayout);
    }

    private void initToolBar() {
        if (getActivity() instanceof AppCompatActivity) {
            final AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), GRID_COUNT);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        photoRecyclerView.setLayoutManager(gridLayoutManager);
        photoRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(com.bilibili.boxing_impl.R.dimen.media_margin), GRID_COUNT));
        mediaAdapter.setOnMediaClickListener(new OnMediaClickListener());
        photoRecyclerView.setAdapter(mediaAdapter);
    }

    @Override
    public void showMedia(@Nullable List<BaseMedia> medias, int allCount) {
        if (medias == null || medias.size() == 0) return;
        mediaAdapter.addAllData(medias);
        displayPhoto(medias.get(0));
    }

    private class OnMediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            displayPhoto((BaseMedia) v.getTag());
        }
    }

    private void displayPhoto(BaseMedia media) {
        String path = media.getPath();
        BoxingMediaLoader.getInstance().displayRaw(photoView, path, null);
    }

    @Override
    public void startLoading() {
        loadMedias();
        loadAlbum();
    }
}

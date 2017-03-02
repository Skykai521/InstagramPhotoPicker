package com.github.skykai.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sky on 17/3/1.
 */
public class CoordinatorRecyclerView extends RecyclerView {
    private int dragDistanceY;
    private boolean scrollTop;
    private float downPositionY;
    private CoordinatorListener coordinatorListener;

    public CoordinatorRecyclerView(Context context) {
        super(context);
    }

    public CoordinatorRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CoordinatorRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null == coordinatorListener) {
            return super.onTouchEvent(ev);
        }
        final int action = ev.getAction();
        final int y = (int) ev.getRawY();
        final int x = (int) ev.getRawX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downPositionY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (downPositionY - y);
                boolean deal;
                if (isScrollTop(ev)) {
                    deal = coordinatorListener.onCoordinateScroll(x, y, 0, deltaY + Math.abs(dragDistanceY), true);
                } else {
                    deal = coordinatorListener.onCoordinateScroll(x, y, 0, deltaY, isScrollTop(ev));
                }
                if (deal) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                scrollTop = false;
                if (coordinatorListener.isBeingDragged()) {
                    coordinatorListener.onSwitch();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isScrollTop(MotionEvent ev) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            if (gridLayoutManager.findFirstVisibleItemPosition() == 0 &&
                    gridLayoutManager.findViewByPosition(0).getTop() == gridLayoutManager.getTopDecorationHeight(gridLayoutManager.findViewByPosition(0))) {
                if (!scrollTop) {
                    dragDistanceY = (int) (downPositionY - ev.getRawY());
                    scrollTop = true;
                }
                return true;
            }
        }
        return false;
    }

    public void setCoordinatorListener(CoordinatorListener listener) {
        this.coordinatorListener = listener;
    }
}

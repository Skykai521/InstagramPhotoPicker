package com.github.skykai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by sky on 17/3/1.
 */
public class CoordinatorLinearLayout extends LinearLayout implements CoordinatorListener {
    public static int DEFAULT_DURATION = 500;
    private int state = WHOLE_STATE;
    private int topBarHeight;
    private int topViewHeight;
    private int minScrollToTop;
    private int minScrollToWhole;
    private int maxScrollDistance;
    private float lastPositionY;
    private boolean beingDragged;
    private Context context;
    private OverScroller scroller;

    public CoordinatorLinearLayout(Context context) {
        this(context, null);
    }

    public CoordinatorLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoordinatorLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        scroller = new OverScroller(context);
    }

    public void setTopViewParam(int topViewHeight, int topBarHeight) {
        this.topViewHeight = topViewHeight;
        this.topBarHeight = topBarHeight;
        this.maxScrollDistance = this.topViewHeight - this.topBarHeight;
        this.minScrollToTop = this.topBarHeight;
        this.minScrollToWhole = maxScrollDistance - this.topBarHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int y = (int) ev.getY();
                lastPositionY = y;
                if (state == COLLAPSE_STATE && y < topBarHeight) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final int y = (int) ev.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastPositionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (lastPositionY - y);
                if (state == COLLAPSE_STATE && deltaY < 0) {
                    beingDragged = true;
                    setScrollY(maxScrollDistance + deltaY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (beingDragged) {
                    onSwitch();
                    return true;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onCoordinateScroll(int x, int y, int deltaX, int deltaY, boolean isScrollToTop) {
        if (y < topViewHeight && state == WHOLE_STATE && getScrollY() < getScrollRange()) {
            beingDragged = true;
            setScrollY(topViewHeight - y);
            return true;
        } else if (isScrollToTop && state == COLLAPSE_STATE && deltaY < 0) {
            beingDragged = true;
            setScrollY(maxScrollDistance + deltaY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSwitch() {
        if (state == WHOLE_STATE) {
            if (getScrollY() >= minScrollToTop) {
                switchToTop();
            } else {
                switchToWhole();
            }
        } else if (state == COLLAPSE_STATE) {
            if (getScrollY() <= minScrollToWhole) {
                switchToWhole();
            } else {
                switchToTop();
            }
        }
    }

    @Override
    public boolean isBeingDragged() {
        return beingDragged;
    }

    public void switchToWhole() {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        scroller.startScroll(0, getScrollY(), 0, -getScrollY(), DEFAULT_DURATION);
        postInvalidate();
        state = WHOLE_STATE;
        beingDragged = false;
    }

    public void switchToTop() {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        scroller.startScroll(0, getScrollY(), 0, getScrollRange() - getScrollY(), DEFAULT_DURATION);
        postInvalidate();
        state = COLLAPSE_STATE;
        beingDragged = false;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            setScrollY(scroller.getCurrY());
            postInvalidate();
        }
    }

    private int getScrollRange() {
        return maxScrollDistance;
    }
}

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
    private static final String TAG = "CoordinatorLinearLayout";
    public static int DEFAULT_DURATION = 500;
    public int mState = WHOLE_STATE;
    //顶部View高度
    private int mTopViewHeight;
    //最大滑动距离
    private int mMaxScrollDistance;
    //滑动到顶部时TopView还剩余的高度
    private int mTopBarHeight;
    private OverScroller mScroller;
    private Context mContext;
    private boolean mIsBeingDragged = false;
    private int mMinScrollToTop;
    private int mMinScrollToWhole;
    private float mLastPositionY;

    public CoordinatorLinearLayout(Context context) {
        this(context, null);
    }

    public CoordinatorLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoordinatorLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mScroller = new OverScroller(mContext);
    }

    public void setTopViewParam(int topViewHeight, int topBarHeight) {
        this.mTopViewHeight = topViewHeight;
        this.mTopBarHeight = topBarHeight;
        this.mMaxScrollDistance = mTopViewHeight - mTopBarHeight;
        this.mMinScrollToTop = mTopBarHeight;
        this.mMinScrollToWhole = mMaxScrollDistance - mTopBarHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int y = (int) ev.getY();
                mLastPositionY = y;
                if (mState == TOP_STATE && y < mTopBarHeight) {
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
                mLastPositionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (mLastPositionY - y);
                if (mState == TOP_STATE && deltaY < 0) {
                    mIsBeingDragged = true;
                    setScrollY(mMaxScrollDistance + deltaY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    onSwitch();
                    return true;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onCoordinateScroll(int x, int y, int deltaX, int deltaY, boolean isScrollToTop) {
        if (y < mTopViewHeight && mState == WHOLE_STATE && getScrollY() < getScrollRange()) {
            //Log.d(TAG, "getScrollY : " + getScrollY() + " getScrollRange : " + getScrollRange());
            mIsBeingDragged = true;
            setScrollY(mTopViewHeight - y);
            return true;
        }  else if (isScrollToTop && mState == TOP_STATE && deltaY < 0) {
            mIsBeingDragged = true;
            setScrollY(mMaxScrollDistance + deltaY);
            return true;
        }  else {
            return false;
        }
    }

    @Override
    public void onSwitch() {
        //Log.d(TAG, "onSwitch , mState : " + mState);
        if (mState == WHOLE_STATE) {
            if (getScrollY() >= mMinScrollToTop) {
                switchToTop();
            } else {
                switchToWhole();
            }
        } else if (mState == TOP_STATE) {
            if (getScrollY() <= mMinScrollToWhole) {
                switchToWhole();
            } else {
                switchToTop();
            }
        }
    }

    @Override
    public boolean isBeingDragged() {
        return mIsBeingDragged;
    }

    public void switchToWhole() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        mScroller.startScroll(0, getScrollY(), 0, - getScrollY(), DEFAULT_DURATION);
        postInvalidate();
        mState = WHOLE_STATE;
        mIsBeingDragged = false;
    }

    public void switchToTop() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        mScroller.startScroll(0, getScrollY(), 0, getScrollRange() - getScrollY(), DEFAULT_DURATION);
        postInvalidate();
        mState = TOP_STATE;
        mIsBeingDragged = false;
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            setScrollY(mScroller.getCurrY());
            postInvalidate();
        }
    }

    private int getScrollRange() {
        return mMaxScrollDistance;
    }
}

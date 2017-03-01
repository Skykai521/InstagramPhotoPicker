package com.github.skykai.view;

/**
 * Created by sky on 17/3/1.
 */
public interface CoordinatorListener {
    int WHOLE_STATE = 0;
    int TOP_STATE = 1;
    boolean onCoordinateScroll(int x, int y, int deltaX, int deltaY, boolean isScrollToTop);
    void onSwitch();
    boolean isBeingDragged();
}

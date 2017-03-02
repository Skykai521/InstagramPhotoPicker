package com.github.skykai.view;

/**
 * Created by sky on 17/3/1.
 */
public interface CoordinatorListener {
    /**
     *  View complete show state.
     */
    int WHOLE_STATE = 0;

    /**
     * View collapse state.
     */
    int COLLAPSE_STATE = 1;

    /**
     * The current view is being dragged.
     * @return true if being dragged
     */
    boolean isBeingDragged();

    boolean onCoordinateScroll(int x, int y, int deltaX, int deltaY, boolean isScrollToTop);

    void onSwitch();
}

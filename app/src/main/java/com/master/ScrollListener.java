package com.master;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * @author Daisw
 */
public class ScrollListener implements OnScrollListener {

//	private static final String TAG = BackScrollManager.class.getSimpleName();

    private HeaderView mCarousel;
    private int mPageIndex;
    private boolean isScroll = false;
    private OnScrollListener mOnScrollListener;

    public ScrollListener(HeaderView carouselHeader, int pageIndex) {

        mCarousel = carouselHeader;
        mPageIndex = pageIndex;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {

        mOnScrollListener = onScrollListener;
    }

    public interface OnScrollListener {

        void onScroll(float distance);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mCarousel == null || mCarousel.isTabCarouselIsAnimating())
            return;

        if (firstVisibleItem != 0) {

            if (isScroll)
                mCarousel.moveToYCoordinate(mPageIndex, -mCarousel.getAllowedVerticalScrollLength());
            return;
        }

        View topView = view.getChildAt(firstVisibleItem);
        if (topView == null)
            return;

        float y = view.getChildAt(firstVisibleItem).getTop();
        float amtToScroll = Math.max(y, -mCarousel.getAllowedVerticalScrollLength());
        if (isScroll) {

            mCarousel.moveToYCoordinate(mPageIndex, amtToScroll);

            if (mOnScrollListener != null)
                mOnScrollListener.onScroll(amtToScroll);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case SCROLL_STATE_IDLE:

                isScroll = false;
                break;
            case SCROLL_STATE_TOUCH_SCROLL:

                isScroll = true;
                break;
            case SCROLL_STATE_FLING:

                isScroll = true;
                break;
            default:
                break;
        }
    }
}

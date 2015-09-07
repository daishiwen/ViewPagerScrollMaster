package com.master;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

/**
 * @author Daisw
 */
public class HeaderView extends FrameLayout {

//	private static final String TAG = GuideJnDetailHeaderView.class.getSimpleName();

    private boolean mIsTabDock = true;

    /**
     * Number of tabs
     */
    private static final int TAB_COUNT = 3;

    /**
     * First tab index
     */
    public static final int TAB_INDEX_FIRST = 0;

    /**
     * Second tab index
     */
    public static final int TAB_INDEX_SECOND = 1;

    /**
     * Third tab index
     */
    public static final int TAB_INDEX_THIRD = 2;

    /**
     * Y coordinate of the tab at the given index was selected
     */
    private static final float[] Y_COORDINATE = new float[TAB_COUNT];

    /**
     * Tab height as defined as a fraction of the screen width
     */
    private final float mTabHeightScreenFraction;

    /**
     * Height of the tab label
     */
    private final int mTabDisplayLabelHeight;

    /**
     * Height of the shadow under the tab carousel
     */
    private final int mTabShadowHeight;

    /**
     * Height of the titlebar
     */
    private final int mTitlebarHeight;

    /**
     * Used to determine is the carousel is animating
     */
    private boolean mTabCarouselIsAnimating;

    /**
     * Allowed vertical scroll length
     */
    private int mAllowedVerticalScrollLength = Integer.MIN_VALUE;

    /**
     * The last scrolled position
     */
    private int mLastScrollPosition = Integer.MIN_VALUE;

    private int mCurrentTabIndex;

    /**
     * @param context The {@link Context} to use
     * @param attrs   The attributes of the XML tag that is inflating the view
     */
    public HeaderView(Context context, AttributeSet attrs) {

        super(context, attrs);

        Resources res = getResources();

        // Height of the title bar
        mTitlebarHeight = res.getDimensionPixelSize(R.dimen.titlebar_height);
        // Height of the header view
        mTabHeightScreenFraction = res.getFraction(R.fraction.tab_height_screen_percentage, 1, 1);
        // Height of the tab
        mTabDisplayLabelHeight = res.getDimensionPixelSize(R.dimen.carousel_label_height);
        // Height of the tab shadow
        mTabShadowHeight = res.getDimensionPixelSize(R.dimen.carousel_image_shadow_height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int tabHeight = Math.round(screenWidth * mTabHeightScreenFraction) + mTabShadowHeight;

        super.onMeasure(measureExact(screenWidth), measureExact(tabHeight));

        if (mIsTabDock)
            mAllowedVerticalScrollLength = tabHeight - (mTabDisplayLabelHeight + mTabShadowHeight);
        else
            mAllowedVerticalScrollLength = tabHeight;
        setMeasuredDimension(resolveSize(screenWidth, widthMeasureSpec), resolveSize(tabHeight, heightMeasureSpec));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {

        super.onScrollChanged(x, y, oldX, oldY);

        if (mLastScrollPosition == x)
            return;

        mLastScrollPosition = x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    public void setTabDock(boolean isTabDock) {

        mIsTabDock = isTabDock;
    }

    /**
     * @return True if the carousel is currently animating, false otherwise
     */
    public boolean isTabCarouselIsAnimating() {

        return mTabCarouselIsAnimating;
    }

    /**
     * Reset the carousel to the start position
     */
    public void reset() {

        storeYCoordinate(TAB_INDEX_FIRST, 0);
        storeYCoordinate(TAB_INDEX_SECOND, 0);
        storeYCoordinate(TAB_INDEX_THIRD, 0);
    }

    /**
     * Store this information as the last requested Y coordinate for the given tabIndex.
     *
     * @param tabIndex The tab index being stored
     * @param y        The Y cooridinate to move to
     */
    public void storeYCoordinate(int tabIndex, float y) {

        Y_COORDINATE[tabIndex] = y + mTitlebarHeight;
    }

    /**
     * Returns the stored Y coordinate of this view the last time the user was on the selected tab given by tabIndex.
     *
     * @param tabIndex The tab index use to return the Y value
     */
    public float getStoredYCoordinateForTab(int tabIndex) {

        return Y_COORDINATE[tabIndex];
    }

    /**
     * Restore the Y position of this view to the last manually requested value. This can be done after the parent has been re-laid out again, where this view's position could have been lost if the
     * view laid outside its parent's bounds.
     *
     * @param duration The duration of the animation
     * @param tabIndex The index to restore
     */
    public void restoreYCoordinate(int duration, int tabIndex) {

        float storedYCoordinate = getStoredYCoordinateForTab(tabIndex);

        if (storedYCoordinate == 0)
            storedYCoordinate = (float) mTitlebarHeight;

        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "y", storedYCoordinate);
        animator.addListener(mTabCarouselAnimatorListener);
        animator.setInterpolator(interpolator);
        animator.setDuration(duration);
        animator.start();
    }

    private void restoreYCoordinate(int tabIndex) {

        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "y", getStoredYCoordinateForTab(tabIndex));
        animator.addListener(mTabCarouselAnimatorListener);
        animator.setInterpolator(interpolator);
        animator.setDuration(0);
        animator.start();
    }

    /**
     * Request that the view move to the given Y coordinate. Also store the Y coordinate as the last requested Y coordinate for the given tabIndex.
     *
     * @param tabIndex The tab index being stored
     * @param y        The Y cooridinate to move to
     */
    public void moveToYCoordinate(int tabIndex, float y) {

        if (tabIndex == mCurrentTabIndex && y != getStoredYCoordinateForTab(tabIndex)) {

            storeYCoordinate(tabIndex, y);
            restoreYCoordinate(tabIndex);
        }
    }

    public void setCurrentTab(int index) {

        mCurrentTabIndex = index;
    }

    /**
     * Returns the number of pixels that this view can be scrolled vertically while still allowing the tab labels to still show
     */
    public int getAllowedVerticalScrollLength() {

        return mAllowedVerticalScrollLength;
    }

    /**
     * @param size The size of the measure specification
     * @return The measure specifiction based on
     */
    private int measureExact(int size) {

        return MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
    }

    /**
     * This listener keeps track of whether the tab carousel animation is currently going on or not, in order to prevent other simultaneous changes to the Y position of the tab carousel which can
     * cause flicker.
     */
    private Animator.AnimatorListener mTabCarouselAnimatorListener = new Animator.AnimatorListener() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onAnimationCancel(Animator animation) {

            mTabCarouselIsAnimating = false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onAnimationEnd(Animator animation) {

            mTabCarouselIsAnimating = false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onAnimationRepeat(Animator animation) {

            mTabCarouselIsAnimating = true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onAnimationStart(Animator animation) {

            mTabCarouselIsAnimating = true;
        }
    };
}

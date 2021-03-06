package com.master;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author Daisw
 */
public class MainFragmentActivity extends FragmentActivity implements OnClickListener {

    private HeaderView mCarouselContainer;
    private RadioGroup mRadioGroup;
    private ExViewPager mViewPager;
    private CommonFragmentPagerAdapter mPagerAdapter;
    public float[] Y_COORDINATE = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main_container);

        initData();
        initTitleView();
        initContentView();
    }

    protected void initData() {

        mPagerAdapter = new CommonFragmentPagerAdapter(this);
    }

    protected void initTitleView() {

    }

    protected void initContentView() {

        initHeadInfoViews();
        initTabViews();
    }

    private void initHeadInfoViews() {

        mCarouselContainer = (HeaderView) findViewById(R.id.carousel_header);
        mCarouselContainer.setTabDock(false);
        mCarouselContainer.reset();
    }

    private void initTabViews() {

        mRadioGroup = (RadioGroup) findViewById(R.id.mRgFloatTabs);
        mRadioGroup.check(R.id.mRbAbout);// 默认选中关于
        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.mRbAbout) {

                    mViewPager.setCurrentItem(0);
                } else if (checkedId == R.id.mRbComment) {

                    mViewPager.setCurrentItem(1);
                } else {

                    mViewPager.setCurrentItem(2);
                }
            }
        });

        mPagerAdapter.add(ListFragment1.class, new Bundle());
        mPagerAdapter.add(ListFragment2.class, new Bundle());
        mPagerAdapter.add(ListFragment3.class, new Bundle());

        mViewPager = (ExViewPager) findViewById(R.id.carousel_pager);
        mViewPager.setScrollEnabled(false);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                mCarouselContainer.setCurrentTab(position);

                if (position == 0) {

                    mRadioGroup.check(R.id.mRbAbout);
                } else if (position == 1) {

                    mRadioGroup.check(R.id.mRbComment);
                } else {

                    mRadioGroup.check(R.id.mRbRelevant);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_SETTLING) {

                    int currentIndex = mViewPager.getCurrentItem();
                    float distance = mCarouselContainer.getStoredYCoordinateForTab(currentIndex);

                    if (distance != Y_COORDINATE[currentIndex]) {

                        ListView lv = ((ListFragment) mPagerAdapter.getItem(currentIndex)).getListView();
                        float scrollDistance = distance - Y_COORDINATE[currentIndex];
                        lv.smoothScrollBy(-(int) scrollDistance, 0);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mRbAbout:

                if (mViewPager != null)
                    mViewPager.setCurrentItem(0, true);
                break;
            case R.id.mRbComment:

                if (mViewPager != null)
                    mViewPager.setCurrentItem(1, true);
                break;
            case R.id.mRbRelevant:

                if (mViewPager != null)
                    mViewPager.setCurrentItem(2, true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (mCarouselContainer != null) {

            mCarouselContainer.reset();
            mCarouselContainer = null;
        }
    }
}

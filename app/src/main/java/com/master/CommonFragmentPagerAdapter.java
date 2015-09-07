package com.master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daisw
 */
public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentActivity mFragmentActivity;
    private List<Fragment> mFragments;

    public CommonFragmentPagerAdapter(FragmentActivity fragmentActivity) {

        super(fragmentActivity.getSupportFragmentManager());

        mFragmentActivity = fragmentActivity;
        mFragments = new ArrayList<>();
    }

    /**
     * Method that adds a new fragment class to the viewer (the fragment is internally instantiate)
     *
     * @param className The full qualified name of fragment class.
     * @param params    The instantiate params.
     */
    public void add(Class<? extends Fragment> className, Bundle params) {

        add(Fragment.instantiate(mFragmentActivity, className.getName(), params));
    }

    public void add(Fragment fragment) {

        mFragments.add(fragment);
    }

    @Override
    public int getCount() {

        return mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

//		super.destroyItem(container, position, object);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {

//		super.destroyItem(container, position, object);
    }
}
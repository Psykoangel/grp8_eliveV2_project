package fr.group8.elive.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by chriis on 20/01/2016.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    public int getCount() {
        return this.fragments.size();
    }
}

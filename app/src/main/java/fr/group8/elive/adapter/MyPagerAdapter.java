package fr.group8.elive.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by chriis on 20/01/2016.
 */
public class MyPagerAdapter {
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

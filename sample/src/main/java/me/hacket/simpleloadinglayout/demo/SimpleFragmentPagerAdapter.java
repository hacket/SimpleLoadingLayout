package me.hacket.simpleloadinglayout.demo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[];
    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, String[] titles) {
        super(fm);
        this.context = context;
        this.tabTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        PageFragment pageFragment = PageFragmentAutoBundle.createFragmentBuilder(position + 1).build();
        return pageFragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

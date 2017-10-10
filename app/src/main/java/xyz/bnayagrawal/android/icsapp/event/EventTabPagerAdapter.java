package xyz.bnayagrawal.android.icsapp.event;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by binay on 10/9/2017.
 */

public class EventTabPagerAdapter extends FragmentPagerAdapter {
    int tabCount;

    public EventTabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragmentEvent tabEvent = new TabFragmentEvent();
                return tabEvent;
            case 1:
                TabFragmentWorkshop tabWorkshop = new TabFragmentWorkshop();
                return tabWorkshop;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

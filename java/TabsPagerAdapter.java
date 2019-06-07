package com.example.sankba.infogroup;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index)
    {
        switch (index)
        {
            case 0:
                return new frag_UpcomingEvents();
            case 1:
                return new frag_PastEvents();
        }

        return null;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}

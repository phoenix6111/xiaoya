package wanghaisheng.com.xiaoya.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;


/**
 * Created by sheng on 2016/4/13.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<String> titles;

    public PagerAdapter(FragmentManager fm,List<String> titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}

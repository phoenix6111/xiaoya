package wanghaisheng.com.xiaoya.ui.meitu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import wanghaisheng.com.xiaoya.ui.meizi.MeiziLargePicFragment;

/**
 * Created by sheng on 2016/5/8.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> urls;
    private String groupId;

    public PagerAdapter(FragmentManager fm, ArrayList<String> datas, String groupId) {
        super(fm);
        this.urls = datas;
        this.groupId = groupId;
    }

    @Override
    public Fragment getItem(int position) {
        return MeiziLargePicFragment.newInstance(position,urls.get(position),groupId);
    }

    @Override
    public int getCount() {
        return urls.size();
    }
}

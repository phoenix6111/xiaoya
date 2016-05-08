package wanghaisheng.com.xiaoya.ui.meizi;

import android.support.v4.app.Fragment;

import java.util.List;

import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.ui.BaseTopNagigationFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;
import wanghaisheng.com.xiaoya.utils.ArrayUtils;

/**
 * Created by sheng on 2016/5/6.
 * 首页所有人的列表
 */
public class BaseMeiziFragment extends BaseTopNagigationFragment {

    public static BaseMeiziFragment newInstance() {
        BaseMeiziFragment fragment = new BaseMeiziFragment();

        return fragment;
    }


    @Override
    protected PagerAdapter initPagerAdapter() {
        //将title类的数组转换成list
        List<String> titles = ArrayUtils.arrayToList(MeiziApi.CHANNEL_TITLE);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {
                MeiziHomeListFragment fragment = MeiziHomeListFragment.newInstance(MeiziApi.CHANNEL_TAG[position]);
                return fragment;
            }
        };

        return pagerAdapter;
    }
}

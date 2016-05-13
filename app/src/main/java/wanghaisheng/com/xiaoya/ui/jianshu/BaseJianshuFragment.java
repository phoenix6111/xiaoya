package wanghaisheng.com.xiaoya.ui.jianshu;

import android.support.v4.app.Fragment;

import java.util.List;

import wanghaisheng.com.xiaoya.api.jianshu.JianshuApi;
import wanghaisheng.com.xiaoya.ui.BaseTopNagigationFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;
import wanghaisheng.com.xiaoya.utils.ArrayUtils;

/**
 * Created by sheng on 2016/5/10.
 */
public class BaseJianshuFragment extends BaseTopNagigationFragment {

    public static BaseJianshuFragment newInstance() {
        return new BaseJianshuFragment();
    }

    @Override
    protected PagerAdapter initPagerAdapter() {
        List<String> titles = ArrayUtils.arrayToList(JianshuApi.CHANNEL_NAME);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(),titles) {
            @Override
            public Fragment getItem(int position) {
                JianshuPagerListFragment fragment = JianshuPagerListFragment.newInstance(JianshuApi.CHANNEL_TAG[position]);

                return fragment;
            }
        };

        return pagerAdapter;
    }
}

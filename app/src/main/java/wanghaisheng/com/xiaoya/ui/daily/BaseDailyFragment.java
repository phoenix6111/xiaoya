package wanghaisheng.com.xiaoya.ui.daily;

import android.support.v4.app.Fragment;

import java.util.List;

import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.ui.BaseTopNagigationFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;
import wanghaisheng.com.xiaoya.utils.ArrayUtils;

/**
 * Created by sheng on 2016/4/19.
 */
public class BaseDailyFragment extends BaseTopNagigationFragment {
    @Override
    protected PagerAdapter initPagerAdapter() {
        //将title类的数组转换成list
        List<String> titles = ArrayUtils.arrayToList(DailyApi.THEME_NAME);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(),titles) {
            @Override
            public Fragment getItem(int position) {

                return StoryListFragment.newInstance(DailyApi.THEME_ID[position]);
            }
        };

        return pagerAdapter;
    }


    public static BaseDailyFragment newInstance() {
        return new BaseDailyFragment();
    }

}

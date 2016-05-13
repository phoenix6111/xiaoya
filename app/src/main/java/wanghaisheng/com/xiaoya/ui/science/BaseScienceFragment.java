package wanghaisheng.com.xiaoya.ui.science;

import android.support.v4.app.Fragment;

import java.util.List;

import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.api.science.ScienceApi;
import wanghaisheng.com.xiaoya.ui.BaseTopNagigationFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;
import wanghaisheng.com.xiaoya.ui.daily.StoryListFragment;
import wanghaisheng.com.xiaoya.ui.movie.MovieListFragment;
import wanghaisheng.com.xiaoya.utils.ArrayUtils;

/**
 * Created by sheng on 2016/4/16.
 */
public class BaseScienceFragment extends BaseTopNagigationFragment {
    private static final String TAG = "BaseScienceFragment";
    @Override
    protected PagerAdapter initPagerAdapter() {
        //将title类的数组转换成list
        List<String> titles = ArrayUtils.arrayToList(ScienceApi.CHANNEL_TITLE);
        titles.add(DailyApi.THEME_NAME[0]);
        titles.add("电影");
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                if (position==8) {
                    fragment = StoryListFragment.newInstance(DailyApi.THEME_ID[0]);
                } else if(position==9){
                    fragment = MovieListFragment.newInstance();
                } else {
                    fragment = ScienceListFragment.newInstance(ScienceApi.CHANNEL_TAG[position]);
                }

                return fragment;
            }
        };

        return pagerAdapter;
    }

    public static BaseScienceFragment newInstance() {
        return new BaseScienceFragment();
    }


}

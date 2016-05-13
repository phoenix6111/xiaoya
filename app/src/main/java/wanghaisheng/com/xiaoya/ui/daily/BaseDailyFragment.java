package wanghaisheng.com.xiaoya.ui.daily;

import android.support.v4.app.Fragment;

import java.util.Arrays;
import java.util.List;

import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.ui.BaseTopNagigationFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;
import wanghaisheng.com.xiaoya.ui.movie.MovieListFragment;

/**
 * Created by sheng on 2016/4/19.
 */
public class BaseDailyFragment extends BaseTopNagigationFragment {
    @Override
    protected PagerAdapter initPagerAdapter() {
        //将title类的数组转换成list
//        List<String> titles = ArrayUtils.arrayToList(DailyApi.THEME_NAME);
        List<String> titles = Arrays.asList("知乎","电影");
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(),titles) {
            @Override
            public Fragment getItem(int position) {
                if(position==0) {
                    return StoryListFragment.newInstance(DailyApi.THEME_ID[0]);
                }

                return MovieListFragment.newInstance();
            }
        };

        return pagerAdapter;
    }


    public static BaseDailyFragment newInstance() {
        return new BaseDailyFragment();
    }

}

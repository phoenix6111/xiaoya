package wanghaisheng.com.xiaoya.ui.gaoxiao;

import android.support.v4.app.Fragment;

import java.util.List;

import wanghaisheng.com.xiaoya.api.gaoxiao.GaoxiaoApi;
import wanghaisheng.com.xiaoya.ui.BaseTopNagigationFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;
import wanghaisheng.com.xiaoya.utils.ArrayUtils;

/**
 * Created by sheng on 2016/5/13.
 */
public class BaseGaoxiaoFragment extends BaseTopNagigationFragment {

    public static BaseGaoxiaoFragment newInstance() {
        BaseGaoxiaoFragment fragment = new BaseGaoxiaoFragment();

        return fragment;
    }


    @Override
    protected PagerAdapter initPagerAdapter() {
        //将title类的数组转换成list
        List<String> titles = ArrayUtils.arrayToList(GaoxiaoApi.TAGS);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {
                GaoxiaoHomePagerListFragment fragment = GaoxiaoHomePagerListFragment.newInstance(GaoxiaoApi.TAGS[position]);
                return fragment;
            }
        };

        return pagerAdapter;
    }
}

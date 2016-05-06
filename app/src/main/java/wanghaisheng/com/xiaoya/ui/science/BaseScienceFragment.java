package wanghaisheng.com.xiaoya.ui.science;

import android.support.v4.app.Fragment;

import java.util.List;

import wanghaisheng.com.xiaoya.api.science.ScienceApi;
import wanghaisheng.com.xiaoya.ui.BaseTopNagigationFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;
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
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {
                ScienceListFragment fragment = ScienceListFragment.newInstance(ScienceApi.CHANNEL_TAG[position]);

                return fragment;
            }
        };

        return pagerAdapter;
    }

    public static BaseScienceFragment newInstance() {
        return new BaseScienceFragment();
    }

    /*@Override
    public void onResume() {
        super.onResume();
        LogUtils.v(TAG,"onResume.........................");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.v(TAG,"onViewCreated............................");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.v(TAG,"onPause....................");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.v(TAG,"onDestroyView...........................");
    }*/
}

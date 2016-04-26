package wanghaisheng.com.xiaoya.ui.collection;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseTopNagigationFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;

/**
 * Created by sheng on 2016/4/20.
 */
public class CollectionTopNavigationFragment extends BaseTopNagigationFragment {
    public List<String> collectionTitles = new ArrayList<>();

    public static CollectionTopNavigationFragment newInstance() {
        return new CollectionTopNavigationFragment();
    }

    @Override
    protected PagerAdapter initPagerAdapter() {
        collectionTitles.add(getString(R.string.str_daily));
        collectionTitles.add(getString(R.string.str_science));
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(),collectionTitles) {
            @Override
            public Fragment getItem(int position) {
//                LogUtils.v("position............................"+position);
                if(position ==0) {
                    return DailyCollectionFragment.newInstance();
                }else {
                    return ScienceCollectionFragment.newInstance();
                }
            }
        };

        return pagerAdapter;
    }

}

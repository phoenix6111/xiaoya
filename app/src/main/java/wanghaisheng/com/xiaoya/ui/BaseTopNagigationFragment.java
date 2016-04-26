package wanghaisheng.com.xiaoya.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import wanghaisheng.com.xiaoya.R;

/**
 * Created by sheng on 2016/4/16.
 */
public abstract class BaseTopNagigationFragment extends Fragment {
    protected View parentView;
    protected ViewPager viewPager;
    protected SmartTabLayout smartTabLayout;
    protected PagerAdapter pagerAdapter;

    protected abstract PagerAdapter initPagerAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = View.inflate(getContext(), R.layout.layout_top_navigation,null);
        viewPager = (ViewPager) parentView.findViewById(R.id.inner_viewpager);
        pagerAdapter = initPagerAdapter();
        viewPager.setAdapter(pagerAdapter);

        smartTabLayout = (SmartTabLayout) parentView.findViewById(R.id.tab_layout);
        smartTabLayout.setVisibility(View.VISIBLE);
        smartTabLayout.setViewPager(viewPager);

        return parentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getChildFragmentManager().getFragments()!=null) {
            getChildFragmentManager().getFragments().clear();
        }
    }


}

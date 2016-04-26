package wanghaisheng.com.xiaoya.ui.daily;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.presenter.daily.StoryListPresenter;
import wanghaisheng.com.xiaoya.presenter.daily.TopDailyView;
import wanghaisheng.com.xiaoya.ui.BaseFragment;
import wanghaisheng.com.xiaoya.ui.PagerAdapter;

/**
 * Created by sheng on 2016/4/13.
 */
public class TopDailyFragment extends BaseFragment implements TopDailyView {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private CirclePageIndicator indicator;
    private CollapsingToolbarLayout collapsingToolbar;

    private List<Story> topStories = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    @Inject
    DailyApi dailyApi;
    @Inject
    StoryListPresenter presenter;

    Handler handler = new Handler();

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.daily_top_item_fragment;
        //return R.layout.daily_top_layout;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        collapsingToolbar = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);

        pagerAdapter = initPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                collapsingToolbar.setTitle(topStories.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        presenter.attachView(this);
        presenter.loadTopStories();

        showContent(true);
    }

    public static TopDailyFragment newInstance() {
        TopDailyFragment topDailyFragment = new TopDailyFragment();

        return topDailyFragment;
    }

    private PagerAdapter initPagerAdapter() {
        //titles = getStoryTitles(topStories);
        //LogUtils.d(titles);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {
                TopStoryFragment topStoryFragment = TopStoryFragment.newInstance(topStories.get(position));

                return topStoryFragment;
            }
        };

        return pagerAdapter;
    }

    private List<String> getStoryTitles(List<Story> topStories) {
        titles.clear();
        for(int i=0; i<topStories.size(); i++) {
            titles.add(topStories.get(i).getTitle());
        }
        return titles;
    }

    @Override
    public void renderStories(final List<Story> stories) {
//        LogUtils.v(stories);
        topStories.clear();
        topStories.addAll(stories);
        LogUtils.v("after render stories.....................................");
        LogUtils.v(stories);
        //修改titles的值
        titles.clear();
        titles = getStoryTitles(stories);
        LogUtils.v("after render stories...........titles..............");
        LogUtils.v(titles);
        collapsingToolbar.setTitle(topStories.get(0).getTitle());
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getChildFragmentManager().getFragments()!=null) {
            getChildFragmentManager().getFragments().clear();
        }
    }


}

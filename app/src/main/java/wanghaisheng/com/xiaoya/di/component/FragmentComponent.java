package wanghaisheng.com.xiaoya.di.component;

import android.app.Activity;

import dagger.Component;
import wanghaisheng.com.xiaoya.di.scopes.PerFragment;
import wanghaisheng.com.xiaoya.di.module.FragmentModule;
import wanghaisheng.com.xiaoya.ui.BaseFragment;
import wanghaisheng.com.xiaoya.ui.BaseRecyclerFragment;
import wanghaisheng.com.xiaoya.ui.collection.DailyCollectionFragment;
import wanghaisheng.com.xiaoya.ui.collection.ScienceCollectionFragment;
import wanghaisheng.com.xiaoya.ui.daily.DailyListFragment;
import wanghaisheng.com.xiaoya.ui.daily.TopDailyFragment;
import wanghaisheng.com.xiaoya.ui.daily.TopStoryFragment;
import wanghaisheng.com.xiaoya.ui.movie.MovieListFragment;
import wanghaisheng.com.xiaoya.ui.other.BrowserFragment;
import wanghaisheng.com.xiaoya.ui.science.ScienceListFragment;
import wanghaisheng.com.xiaoya.ui.setting.SettingFragment;

/**
 * Created by sheng on 2016/4/13.
 */

@PerFragment
@Component(modules = {FragmentModule.class},dependencies = AppComponent.class)
public interface FragmentComponent {
    Activity getActiviy();

    void inject(BaseFragment fragment);

    void inject(TopDailyFragment topDailyFragment);

    void inject(DailyListFragment dailyListFragment);

    void inject(TopStoryFragment topStoryFragment);

    //void inject(BaseListFragment listFragment);
    void inject(ScienceListFragment scienceListFragment);

    void inject(MovieListFragment movieListFragment);

    void inject(SettingFragment settingFragment);

    void inject(DailyCollectionFragment dailyCollectionFragment);

    void inject(ScienceCollectionFragment scienceCollectionFragment);

    void inject(BrowserFragment browserFragment);

    void inject(BaseRecyclerFragment baseRecyclerFragment);

}

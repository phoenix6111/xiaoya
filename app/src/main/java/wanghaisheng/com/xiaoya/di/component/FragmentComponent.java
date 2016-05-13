package wanghaisheng.com.xiaoya.di.component;

import android.app.Activity;

import dagger.Component;
import wanghaisheng.com.xiaoya.di.scopes.PerFragment;
import wanghaisheng.com.xiaoya.di.module.FragmentModule;
import wanghaisheng.com.xiaoya.ui.BaseFragment;
import wanghaisheng.com.xiaoya.ui.BaseRecyclerFragment;
import wanghaisheng.com.xiaoya.ui.collection.DailyCollectionFragment;
import wanghaisheng.com.xiaoya.ui.collection.ScienceCollectionFragment;
import wanghaisheng.com.xiaoya.ui.daily.StoryListFragment;
import wanghaisheng.com.xiaoya.ui.gaoxiao.GaoxiaoHomeListFragment;
import wanghaisheng.com.xiaoya.ui.jianshu.JianshuListFragment;
import wanghaisheng.com.xiaoya.ui.meitu.MeituBeautyHomeListFragment;
import wanghaisheng.com.xiaoya.ui.meitu.MeituFunnyHomeListFragment;
import wanghaisheng.com.xiaoya.ui.meitu.MeituPersonListFragment;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziHomeListFragment;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziLargePicFragment;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziPersonListFragment;
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

    void inject(StoryListFragment dailyListFragment);

    //void inject(BaseListFragment listFragment);
    void inject(ScienceListFragment scienceListFragment);

    void inject(MovieListFragment movieListFragment);

    void inject(SettingFragment settingFragment);

    void inject(DailyCollectionFragment dailyCollectionFragment);

    void inject(ScienceCollectionFragment scienceCollectionFragment);

    void inject(BrowserFragment browserFragment);

    void inject(BaseRecyclerFragment baseRecyclerFragment);

    void inject(MeiziHomeListFragment meiziHomeListFragment);

    void inject(MeiziPersonListFragment meiziPersonListFragment);

    void inject(MeiziLargePicFragment meiziLargePicFragment);

    void inject(JianshuListFragment jianshuListFragment);

    void inject(MeituBeautyHomeListFragment meituBeautyHomeListFragment);

    void inject(MeituFunnyHomeListFragment meituFunnyHomeListFragment);

    void inject(MeituPersonListFragment meituPersonListFragment);

    void inject(GaoxiaoHomeListFragment gaoxiaoHomeListFragment);

}

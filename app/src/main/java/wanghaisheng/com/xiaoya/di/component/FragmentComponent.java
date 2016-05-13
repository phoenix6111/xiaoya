package wanghaisheng.com.xiaoya.di.component;

import android.app.Activity;

import dagger.Component;
import wanghaisheng.com.xiaoya.di.scopes.PerFragment;
import wanghaisheng.com.xiaoya.di.module.FragmentModule;
import wanghaisheng.com.xiaoya.ui.BaseFragment;
import wanghaisheng.com.xiaoya.ui.BaseRecyclerFragment;
import wanghaisheng.com.xiaoya.ui.collection.DailyCollectionFragmentPager;
import wanghaisheng.com.xiaoya.ui.collection.ScienceCollectionFragmentPager;
import wanghaisheng.com.xiaoya.ui.daily.StoryPagerListFragment;
import wanghaisheng.com.xiaoya.ui.gaoxiao.GaoxiaoHomePagerListFragment;
import wanghaisheng.com.xiaoya.ui.jianshu.JianshuPagerListFragment;
import wanghaisheng.com.xiaoya.ui.meitu.MeituBeautyHomePagerListFragment;
import wanghaisheng.com.xiaoya.ui.meitu.MeituFunnyHomePagerListFragment;
import wanghaisheng.com.xiaoya.ui.meitu.MeituPersonPagerListFragment;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziHomePagerListFragment;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziLargePicFragment;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziPersonPagerListFragment;
import wanghaisheng.com.xiaoya.ui.movie.MoviePagerListFragment;
import wanghaisheng.com.xiaoya.ui.other.BrowserFragment;
import wanghaisheng.com.xiaoya.ui.science.SciencePagerListFragment;
import wanghaisheng.com.xiaoya.ui.setting.SettingFragment;
import wanghaisheng.com.xiaoya.ui.weiarticle.WeiArticleListFragment;

/**
 * Created by sheng on 2016/4/13.
 */

@PerFragment
@Component(modules = {FragmentModule.class},dependencies = AppComponent.class)
public interface FragmentComponent {
    Activity getActiviy();

    void inject(BaseFragment fragment);

    void inject(StoryPagerListFragment dailyListFragment);

    //void inject(BasePagerListFragment listFragment);
    void inject(SciencePagerListFragment scienceListFragment);

    void inject(MoviePagerListFragment movieListFragment);

    void inject(SettingFragment settingFragment);

    void inject(DailyCollectionFragmentPager dailyCollectionFragment);

    void inject(ScienceCollectionFragmentPager scienceCollectionFragment);

    void inject(BrowserFragment browserFragment);

    void inject(BaseRecyclerFragment baseRecyclerFragment);

    void inject(MeiziHomePagerListFragment meiziHomeListFragment);

    void inject(MeiziPersonPagerListFragment meiziPersonListFragment);

    void inject(MeiziLargePicFragment meiziLargePicFragment);

    void inject(JianshuPagerListFragment jianshuListFragment);

    void inject(MeituBeautyHomePagerListFragment meituBeautyHomeListFragment);

    void inject(MeituFunnyHomePagerListFragment meituFunnyHomeListFragment);

    void inject(MeituPersonPagerListFragment meituPersonListFragment);

    void inject(GaoxiaoHomePagerListFragment gaoxiaoHomeListFragment);

    void inject(WeiArticleListFragment weiArticleListFragment);

}

package wanghaisheng.com.xiaoya.di.component;

import android.app.Activity;

import dagger.Component;
import wanghaisheng.com.xiaoya.ui.MainActivity;
import wanghaisheng.com.xiaoya.SplashActivity;
import wanghaisheng.com.xiaoya.di.scopes.PerActivity;
import wanghaisheng.com.xiaoya.di.module.ActivityModule;
import wanghaisheng.com.xiaoya.ui.BaseActivity;
import wanghaisheng.com.xiaoya.ui.BaseDetailActivity;
import wanghaisheng.com.xiaoya.ui.daily.StoryDetailActivity;
import wanghaisheng.com.xiaoya.ui.daily.ThemeStoryDetailActivity;
import wanghaisheng.com.xiaoya.ui.me.FeedbackActivity;
import wanghaisheng.com.xiaoya.ui.movie.MovieDetailActivity;
import wanghaisheng.com.xiaoya.ui.other.BrowserActivity;
import wanghaisheng.com.xiaoya.ui.science.ScienceDetailActivity;


/**
 * Created by sheng on 2016/4/13.
 */
@PerActivity
@Component(modules = {ActivityModule.class},dependencies = AppComponent.class)
public interface ActivityComponent {
    Activity getActivityContext();

    void inject(BaseActivity baseActivity);

    void inject(SplashActivity splashActivity);

    void inject(MainActivity activity);

    void inject(BaseDetailActivity activity);

    void inject(StoryDetailActivity activity);

    void inject(ThemeStoryDetailActivity activity);

    void inject(ScienceDetailActivity scienceDetailActivity);

    void inject(MovieDetailActivity movieDetailActivity);

    void inject(BrowserActivity browserActivity);

    void inject(FeedbackActivity feedbackActivity);



//    void inject()
}

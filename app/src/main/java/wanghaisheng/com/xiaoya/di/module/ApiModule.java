package wanghaisheng.com.xiaoya.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.api.feedback.FeedbackApi;
import wanghaisheng.com.xiaoya.api.guokr.ScienceApi;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.utils.RequestHelper;
import wanghaisheng.com.xiaoya.utils.SettingPrefHelper;

/**
 * Created by sheng on 2016/4/13.
 */

@Module
public class ApiModule {

    @Provides
    @Singleton
    public DailyApi provideDailyApi() {
        return new DailyApi();
    }

    @Provides
    @Singleton
    public ScienceApi provideScienceApi() {
        return new ScienceApi();
    }

    @Provides
    @Singleton
    public MovieApi provideMovieApi() {
        return new MovieApi();
    }

    @Provides
    @Singleton
    public FeedbackApi provideFeedbackApi(RequestHelper requestHelper, SettingPrefHelper settingPrefHelper) {
        return new FeedbackApi(requestHelper,settingPrefHelper);
    }

}

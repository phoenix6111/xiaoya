package wanghaisheng.com.xiaoya.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.api.feedback.FeedbackApi;
import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.api.science.ArticleApi;
import wanghaisheng.com.xiaoya.api.science.ScienceApi;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.datasource.DailyData;
import wanghaisheng.com.xiaoya.datasource.MeiziData;
import wanghaisheng.com.xiaoya.datasource.MeiziPersonData;
import wanghaisheng.com.xiaoya.datasource.MovieData;
import wanghaisheng.com.xiaoya.datasource.ScienceData;
import wanghaisheng.com.xiaoya.utils.RequestHelper;
import wanghaisheng.com.xiaoya.utils.SettingPrefHelper;

/**
 * Created by sheng on 2016/4/13.
 */

@Module
public class ApiModule {

    @Provides
    @Singleton
    public DailyApi provideDailyApi(OkHttpClient client) {
        return new DailyApi(client);
    }

    @Provides
    @Singleton
    public ScienceApi provideScienceApi(OkHttpClient client) {
        return new ScienceApi(client);
    }

    @Provides
    @Singleton
    public ArticleApi provideArticleApi(OkHttpClient client) {
        return new ArticleApi(client);
    }

    @Provides
    @Singleton
    public MovieApi provideMovieApi(OkHttpClient client) {
        return new MovieApi(client);
    }

    @Provides
    @Singleton
    public MeiziApi provideMeizeApi(OkHttpClient client) {
        return new MeiziApi(client);
    }

    @Provides
    @Singleton
    public FeedbackApi provideFeedbackApi(RequestHelper requestHelper, SettingPrefHelper settingPrefHelper) {
        return new FeedbackApi(requestHelper,settingPrefHelper);
    }

    @Provides
    @Singleton
    public MovieData provideMovieData(CacheManager cacheManager, MovieApi movieApi) {
        return new MovieData(cacheManager,movieApi);
    }

    @Provides
    @Singleton
    public ScienceData provideScienceData(CacheManager cacheManager,ScienceApi scienceApi) {
        return new ScienceData(cacheManager,scienceApi);
    }

    @Provides
    @Singleton
    public DailyData provideDailyData(CacheManager cacheManager,DailyApi dailyApi) {
        return new DailyData(cacheManager,dailyApi);
    }

    @Provides
    @Singleton
    public MeiziData provideMeiziData(CacheManager cacheManager, MeiziApi meiziApi) {
        return new MeiziData(cacheManager,meiziApi);
    }

    @Provides
    @Singleton
    public MeiziPersonData provideMeiziPersonData(CacheManager cacheManager,MeiziApi meiziApi) {
        return new MeiziPersonData(cacheManager,meiziApi);
    }

}

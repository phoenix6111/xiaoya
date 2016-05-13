package wanghaisheng.com.xiaoya.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.api.feedback.FeedbackApi;
import wanghaisheng.com.xiaoya.api.gaoxiao.GaoxiaoApi;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuApi;
import wanghaisheng.com.xiaoya.api.meitu.MeituApi;
import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.api.science.ArticleApi;
import wanghaisheng.com.xiaoya.api.science.ScienceApi;
import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticleApi;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.datasource.DailyData;
import wanghaisheng.com.xiaoya.datasource.GaoxiaoData;
import wanghaisheng.com.xiaoya.datasource.JianshuData;
import wanghaisheng.com.xiaoya.datasource.MeituHomeData;
import wanghaisheng.com.xiaoya.datasource.MeituPersonData;
import wanghaisheng.com.xiaoya.datasource.MeiziHomeData;
import wanghaisheng.com.xiaoya.datasource.MeiziPersonData;
import wanghaisheng.com.xiaoya.datasource.MovieData;
import wanghaisheng.com.xiaoya.datasource.ScienceData;
import wanghaisheng.com.xiaoya.datasource.WeiArticleData;
import wanghaisheng.com.xiaoya.db.ContentDao;
import wanghaisheng.com.xiaoya.db.MeituPictureDao;
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
    public MeiziHomeData provideMeiziData(CacheManager cacheManager, MeiziApi meiziApi) {
        return new MeiziHomeData(cacheManager,meiziApi);
    }

    @Provides
    @Singleton
    public MeiziPersonData provideMeiziPersonData(CacheManager cacheManager, MeiziApi meiziApi, ContentDao contentDao) {
        return new MeiziPersonData(cacheManager,meiziApi,contentDao);
    }

    @Provides
    @Singleton
    public JianshuApi provideJianshuApi(OkHttpClient okHttpClient) {
        return new JianshuApi(okHttpClient);
    }

    @Provides
    @Singleton
    public JianshuData provideJianshuData(CacheManager cacheManager,JianshuApi jianshuApi) {
        return new JianshuData(cacheManager,jianshuApi);
    }

    @Provides
    @Singleton
    public MeituApi provideMeituApi(OkHttpClient client) {
        return new MeituApi(client);
    }

    @Provides
    @Singleton
    public MeituHomeData provideMeituData(CacheManager cacheManager, MeituApi meituApi) {
        return new MeituHomeData(cacheManager,meituApi);
    }

    @Provides
    @Singleton
    public MeituPersonData provideMeituPersonData(CacheManager cacheManager, MeituApi meituApi, MeituPictureDao pictureDao) {
        return new MeituPersonData(cacheManager,meituApi,pictureDao);
    }

    @Provides
    @Singleton
    public GaoxiaoApi provideGaoxiaoApi(OkHttpClient okHttpClient)  {
        return new GaoxiaoApi(okHttpClient);
    }

    @Provides
    @Singleton
    public GaoxiaoData provideGaoxiaoData(GaoxiaoApi gaoxiaoApi,CacheManager cacheManager) {
        return new GaoxiaoData(gaoxiaoApi,cacheManager);
    }

    @Provides
    @Singleton
    public WeiArticleApi provideWeiArticleApi(OkHttpClient client) {
        return new WeiArticleApi(client);
    }

    @Provides
    @Singleton
    public WeiArticleData provideWeiArticleData(CacheManager cacheManager,WeiArticleApi weiArticleApi) {
        return new WeiArticleData(cacheManager,weiArticleApi);
    }
}

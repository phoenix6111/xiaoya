package wanghaisheng.com.xiaoya.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import wanghaisheng.com.xiaoya.AppContext;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.datasource.MeiziPersonData;
import wanghaisheng.com.xiaoya.db.ContentDao;
import wanghaisheng.com.xiaoya.navigator.Navigator;
import wanghaisheng.com.xiaoya.api.feedback.FeedbackApi;
import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.api.science.ArticleApi;
import wanghaisheng.com.xiaoya.api.science.ScienceApi;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.datasource.DailyData;
import wanghaisheng.com.xiaoya.datasource.MeiziHomeData;
import wanghaisheng.com.xiaoya.datasource.MovieData;
import wanghaisheng.com.xiaoya.datasource.ScienceData;
import wanghaisheng.com.xiaoya.db.ArticleCollectionDao;
import wanghaisheng.com.xiaoya.db.DBArticleDao;
import wanghaisheng.com.xiaoya.db.DBStoryDao;
import wanghaisheng.com.xiaoya.db.MovieCollectionDao;
import wanghaisheng.com.xiaoya.db.MovieDao;
import wanghaisheng.com.xiaoya.db.StoryCollectionDao;
import wanghaisheng.com.xiaoya.di.module.ApiModule;
import wanghaisheng.com.xiaoya.di.module.AppModule;
import wanghaisheng.com.xiaoya.di.module.DbModule;
import wanghaisheng.com.xiaoya.di.module.HelperModule;
import wanghaisheng.com.xiaoya.utils.CacheHelper;
import wanghaisheng.com.xiaoya.utils.ConfigHelper;
import wanghaisheng.com.xiaoya.utils.DataCleanHelper;
import wanghaisheng.com.xiaoya.utils.FileHelper;
import wanghaisheng.com.xiaoya.utils.FormatHelper;
import wanghaisheng.com.xiaoya.utils.NetWorkHelper;
import wanghaisheng.com.xiaoya.utils.OkHttpHelper;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;
import wanghaisheng.com.xiaoya.utils.RequestHelper;
import wanghaisheng.com.xiaoya.utils.ResourceHelper;
import wanghaisheng.com.xiaoya.utils.SecurityHelper;
import wanghaisheng.com.xiaoya.utils.SettingPrefHelper;
import wanghaisheng.com.xiaoya.utils.ShareHelper;
import wanghaisheng.com.xiaoya.utils.StringHelper;
import wanghaisheng.com.xiaoya.utils.ToastHelper;
import wanghaisheng.com.xiaoya.utils.ToastUtil;
import wanghaisheng.com.xiaoya.widget.XiaoYaWebView;


/**
 * Created by sheng on 2016/4/13.
 */

@Component(modules = {AppModule.class, ApiModule.class, HelperModule.class, DbModule.class})
@Singleton //用singleton表明该component所对应的module有singleton方法
public interface AppComponent {
    //给其它Component提供依赖时，必须有该对象的返回值
    Context getContext();

    Navigator getNavigator();
    ToastUtil getToastUtil();

    OkHttpClient getOkHttpClient();

    NetWorkHelper getNetWorkHelper();
    SecurityHelper getSecurityHelper();
    FileHelper getFileHelper();
    ConfigHelper getConfigHelper();
    OkHttpHelper getOkHttpHelper();
    ResourceHelper getResourceHelper();
    ToastHelper getToastHelper();
    SettingPrefHelper getSettingPrefHelper();
    CacheHelper getCacheHelper();
    DataCleanHelper getDataCleanHelper();
    FormatHelper getFormatHelper();
    ShareHelper getShareHelper();
    StringHelper getStringHelper();
    RequestHelper getRequestHelper();
    PrefsUtil getPrefsUtil();
    CacheManager getCacheManager();

    //db
    DBStoryDao getDBStoryDao();
    StoryCollectionDao getDBCollectionDao();
    DBArticleDao getDBArticleDao();
    ArticleCollectionDao getArticleCollectionDao();
    MovieDao getMovieDao();
    MovieCollectionDao getMovieCollectionDao();
    ContentDao getContentDao();

    //API
    DailyApi getDailyApi();
    ScienceApi getScienceApi();
    ArticleApi getArticleApi();
    MovieApi getMovieApi();
    FeedbackApi getFeedbackApi();
    MeiziApi getMeiziApi();
    MovieData getMovieData();
    ScienceData getScienceData();
    DailyData getDailyData();
    MeiziHomeData getMeiziData();
    MeiziPersonData getMeiziPersonData();

    void inject(AppContext appContext);

    void inject(XiaoYaWebView webView);

}

package wanghaisheng.com.xiaoya.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wanghaisheng.com.xiaoya.db.ArticleCollectionDao;
import wanghaisheng.com.xiaoya.db.ContentDao;
import wanghaisheng.com.xiaoya.db.DBArticleDao;
import wanghaisheng.com.xiaoya.db.DBStoryDao;
import wanghaisheng.com.xiaoya.db.DaoMaster;
import wanghaisheng.com.xiaoya.db.DaoSession;
import wanghaisheng.com.xiaoya.db.MovieCollectionDao;
import wanghaisheng.com.xiaoya.db.MovieDao;
import wanghaisheng.com.xiaoya.db.StoryCollectionDao;

/**
 * Created by sheng on 2016/4/14.
 */

@Module
public class DbModule {

    @Provides
    @Singleton
    DaoMaster.DevOpenHelper provideDevOpenHelper(Context context) {
        return new DaoMaster.DevOpenHelper(context, "xiaoya.db", null);
    }

    @Provides
    @Singleton
    DaoMaster provideDaoMaster(DaoMaster.DevOpenHelper helper) {
        return new DaoMaster(helper.getWritableDatabase());
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession(DaoMaster master) {
        return master.newSession();
    }

    @Provides
    @Singleton
    DBStoryDao provideDBStoryDao(DaoSession session) {
        return session.getDBStoryDao();
    }

    @Provides
    @Singleton
    StoryCollectionDao provideCollectionDao(DaoSession session) {
        return session.getStoryCollectionDao();
    }

    @Provides
    @Singleton
    DBArticleDao getArticleDao(DaoSession session) {
        return session.getDBArticleDao();
    }
    @Provides
    @Singleton
    ArticleCollectionDao getArticleCollectionDao(DaoSession session) {
        return session.getArticleCollectionDao();
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(DaoSession session) {
        return session.getMovieDao();
    }
    @Provides
    @Singleton
    MovieCollectionDao provideMovieCollectionDao(DaoSession session) {
        return session.getMovieCollectionDao();
    }

    @Provides
    @Singleton
    ContentDao provideContentDao(DaoSession daoSession) {
        return daoSession.getContentDao();
    }


}

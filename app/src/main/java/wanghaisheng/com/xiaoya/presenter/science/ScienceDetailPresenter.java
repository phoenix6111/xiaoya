package wanghaisheng.com.xiaoya.presenter.science;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.guokr.ScienceApi;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.db.ArticleCollection;
import wanghaisheng.com.xiaoya.db.ArticleCollectionDao;
import wanghaisheng.com.xiaoya.db.DBArticle;
import wanghaisheng.com.xiaoya.db.DBArticleDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseDetailPresenter;

/**
 * Created by sheng on 2016/4/16.
 */
public class ScienceDetailPresenter extends BaseDetailPresenter<Article,ScienceDetailView> {

    @Inject
    ScienceApi scienceApi;
    @Inject
    DBArticleDao articleDao;
    @Inject
    ArticleCollectionDao articleCollectionDao;

    @Inject @Singleton
    public ScienceDetailPresenter() {}

    @Override
    public void loadEntityDetail(int entityId, Context context) {

    }

    //收藏article
    public void collectEntity(final Article article) {
        DBArticle dbArticle = convertArticleToDBArticle(article);
        dbArticle.setIs_collected(true);
        articleDao.insertOrReplace(dbArticle);
        ArticleCollection articleCollection = convertDBArticleToArticleCollection(dbArticle);
        articleCollectionDao.insertOrReplace(articleCollection);

        iView.collectSuccess();
    }

    /**
     * 检测是否article已经收藏
     * @param article
     * @return
     */
    public void checkIfCollected(final Article article) {
        Observable<ArticleCollection> articleCollectionObservable = Observable.create(new Observable.OnSubscribe<ArticleCollection>() {
            @Override
            public void call(Subscriber<? super ArticleCollection> subscriber) {
                subscriber.onNext(articleCollectionDao.load((long) article.getId()));
            }
        });

        articleCollectionObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<ArticleCollection>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        LogUtils.d(e.getMessage());
                                    }

                                    @Override
                                    public void onNext(ArticleCollection articleCollection) {
                                        iView.updateCollectionFlag(null!=articleCollection);
                                    }
                                });

    }

    /**
     * 取消收藏article
     * @param article
     */
    public void unCollectEntity(final Article article) {
        DBArticle dbArticle = convertArticleToDBArticle(article);
        articleDao.delete(dbArticle);
        ArticleCollection articleCollection = convertDBArticleToArticleCollection(dbArticle);
        articleCollectionDao.delete(articleCollection);

        iView.uncollectSuccess();
    }

    /**
     * 复制DBArticle中的属性到ArticleCollection
     * @param
     * @return
     */
    private ArticleCollection convertDBArticleToArticleCollection(DBArticle article) {
        ArticleCollection articleCollection = new ArticleCollection();
        articleCollection.setId(article.getId());
        articleCollection.setTitle(article.getTitle());
        articleCollection.setImage(article.getImage());
        articleCollection.setInfo(article.getInfo());
        articleCollection.setChannel_keys(article.getChannel_keys());
        articleCollection.setDescription(article.getDescription());
        articleCollection.setUrl(article.getUrl());
        articleCollection.setComment_count(article.getComment_count());

        return articleCollection;
    }

    private DBArticle convertArticleToDBArticle(Article article) {
        DBArticle dbArticle = new DBArticle();
        dbArticle.setId((long) article.getId());
        dbArticle.setTitle(article.getTitle());
        dbArticle.setInfo(article.getInfo());
        dbArticle.setUrl(article.getUrl());
        dbArticle.setComment_count(article.getReplies_count());
        dbArticle.setChannel_keys(article.getChannel_keys()[0]);
        dbArticle.setDescription(article.getSummary());
        if(null != article.getImage_info()) {
            dbArticle.setImage(article.getImage_info().getUrl());
        }

        return dbArticle;

    }



    @Override
    public void detachView() {
        if (iView!=null) {
            iView = null;
        }
    }
}

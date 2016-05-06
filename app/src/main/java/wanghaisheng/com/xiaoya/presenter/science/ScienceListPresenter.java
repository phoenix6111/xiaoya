package wanghaisheng.com.xiaoya.presenter.science;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.api.science.ScienceApi;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.beans.Science;
import wanghaisheng.com.xiaoya.datasource.ScienceData;
import wanghaisheng.com.xiaoya.db.ArticleCollection;
import wanghaisheng.com.xiaoya.db.ArticleCollectionDao;
import wanghaisheng.com.xiaoya.db.DBArticle;
import wanghaisheng.com.xiaoya.db.DBArticleDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/4/16.
 */
public class ScienceListPresenter extends BaseListPresenter<Article, ScienceListView> {
    @Inject
    ScienceApi scienceApi;

    public static final int limit = 20;

    @Inject
    DBArticleDao articleDao;
    @Inject
    ArticleCollectionDao articleCollectionDao;
//    @Inject
//    CacheManager cacheManager;
    @Inject
    ScienceData scienceData;

    @Inject
    @Singleton
    public ScienceListPresenter() {}

    /**
     * 第一次加载数据时调用，三级缓存：memory,disk,network
     * @param channel
     */
    public void firstLoadData(final String channel) {
        iView.showLoading();

        LogUtils.d(scienceData.getDataSourceText());

        subscription = scienceData.subscribeData(channel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Science>() {
                    @Override
                    public void onCompleted() {
                        iView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(e);
                        iView.hideLoading();
                        if(e instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }

                    @Override
                    public void onNext(Science science) {
                        iView.hideLoading();
                        iView.renderFirstLoadData(science);
                    }
                });
    }


    /**
     * 加载最新数据
     * @param channel
     */
    public void loadNewestData(String channel) {
        subscription = scienceData.network(channel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Science>() {
                    @Override
                    public void call(Science science) {
                        iView.refreshComplete(science);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        if (throwable instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }
                });
    }

    private int getResultOffset(Science science) {
        //如果API返回的总数据量大于offset+limit，则表示还有数据，否则表示没有更多数据
        if (science.getTotal() > science.getOffset() + science.getLimit()) {
            return science.getOffset() + science.getLimit();
        }

        return -1;
    }

    /**
     * 加载更多
     * @param channel
     * @param offset
     */
    public void loadMoreData(String channel, int offset) {
        subscription = scienceApi.getScienceByChannel(channel, offset, limit)
                .compose(SchedulersCompat.<Science>applyIoSchedulers())
                .subscribe(new Action1<Science>() {
                    @Override
                    public void call(Science science) {
                        science.setOffset(getResultOffset(science));
                        iView.loadMoreComplete(science);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable.getStackTrace());
                        if (throwable instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }
                });
    }

    private DBArticle convertArticleToDBArticle(Article article) {
        DBArticle dbArticle = new DBArticle();
        dbArticle.setId((long) article.getId());
        dbArticle.setTitle(article.getTitle());
        dbArticle.setUrl(article.getUrl());
        dbArticle.setDate_published(article.getDate_published());
        if (null != article.getImage_info()) {
            dbArticle.setImage(article.getImage_info().getUrl());
        }
        dbArticle.setDescription(article.getSummary());
        dbArticle.setChannel_keys(article.getChannel_keys()[0]);
        dbArticle.setComment_count(article.getReplies_count());
        dbArticle.setInfo(article.getInfo());
        dbArticle.setIs_collected(article.getIs_collected());

        return dbArticle;
    }

    private ArticleCollection convertArticleToArticleCollection(Article article) {
        ArticleCollection articleCollection = new ArticleCollection();
        articleCollection.setId((long) article.getId());
        articleCollection.setTitle(article.getTitle());
        articleCollection.setUrl(article.getUrl());
        articleCollection.setDate_published(article.getDate_published());
        if (null != article.getImage_info()) {
            articleCollection.setImage(article.getImage_info().getUrl());
        }
        articleCollection.setDescription(article.getSummary());
        articleCollection.setChannel_keys(article.getChannel_keys()[0]);
        articleCollection.setComment_count(article.getReplies_count());
        articleCollection.setInfo(article.getInfo());

        return articleCollection;
    }

    private List<DBArticle> convertDBArticles(List<Article> articles) {
        List<DBArticle> dbArticles = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            dbArticles.add(convertArticleToDBArticle(articles.get(i)));
        }

        return dbArticles;
    }

    private Article convertDBArticleToArticle(DBArticle dbArticle) {
        Article article = new Article();
        article.setId(Integer.valueOf(dbArticle.getId() + ""));
        article.setTitle(dbArticle.getTitle());
        article.setInfo(dbArticle.getInfo());
        article.setChannel_keys(new String[]{dbArticle.getChannel_keys()});
        Article.Image_info info = article.new Image_info();
        info.setUrl(dbArticle.getImage());
        article.setImage_info(info);
        article.setDate_published(dbArticle.getDate_published());
        article.setUrl(dbArticle.getUrl());
        article.setSummary(dbArticle.getDescription());
        article.setReplies_count(dbArticle.getComment_count());
        article.setIs_collected(dbArticle.getIs_collected());

        return article;
    }

    private List<Article> convertArticles(List<DBArticle> dbArticles) {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < dbArticles.size(); i++) {
            articles.add(convertDBArticleToArticle(dbArticles.get(i)));
        }

        return articles;
    }

    @Override
    public void detachView() {
        if (null != subscription) {
            subscription.unsubscribe();
        }

        if (null == iView) {
            iView = null;
        }
    }

}

package wanghaisheng.com.xiaoya.presenter.science;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.guokr.ScienceApi;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.beans.Science;
import wanghaisheng.com.xiaoya.db.ArticleCollection;
import wanghaisheng.com.xiaoya.db.ArticleCollectionDao;
import wanghaisheng.com.xiaoya.db.DBArticle;
import wanghaisheng.com.xiaoya.db.DBArticleDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;

/**
 * Created by sheng on 2016/4/16.
 */
public class ScienceListPresenter extends BaseListPresenter<Article,ScienceListView> {
    @Inject
    ScienceApi scienceApi;

    public static final int limit = 20;

    @Inject
    DBArticleDao articleDao;
    @Inject
    ArticleCollectionDao articleCollectionDao;

    @Inject @Singleton
    public ScienceListPresenter(){}

    /**
     * 从数据库加载
     * @param channel science的channel
     */
    public void loadFromDb(final String channel) {
        //LogUtils.v("load newest stories...........................................................");
        //先从数据库里面查
        iView.showLoading();
        Observable.create(new Observable.OnSubscribe<List<Article>>() {
            @Override
            public void call(Subscriber<? super List<Article>> subscriber) {
                subscriber.onNext(loadNewestFromDb(channel));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Article>>() {
                    @Override
                    public void call(List<Article> articles) {
                        LogUtils.v(articles);
                        iView.hideLoading();
                        iView.renderDbData(articles);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable.getMessage());
                        iView.error("数据访问异常，请重试");
                        iView.hideLoading();
                    }
                });

    }

    /**
     * 一开始从数据库里面查，显示到界面，然后再从网络查最新的数据
     */
    private List<Article> loadNewestFromDb(String channel) {
//        LogUtils.v("channel keys ==========>"+channel);
        List<DBArticle> dbArticles = articleDao.queryBuilder().where(DBArticleDao.Properties.Channel_keys.eq(channel)).list();
//        LogUtils.v(dbArticles);
        return convertArticles(dbArticles);
    }


    /**
     * 从API查询最新数据
     * @param channel
     * @param showLoading
     */
    public void loadNewFromNet(String channel, final boolean showLoading) {
        if(showLoading) {
            iView.showLoading();
        }
        scienceApi.getScienceByChannel(channel,0,limit)
                .subscribe(new Subscriber<Science>() {
                    @Override
                    public void onCompleted() {
                        //如果是第一次从远程加载，则不显示loadmore的下拉框
                        if(showLoading) {
                            iView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(showLoading) {
                            iView.hideLoading();
                        }
                        LogUtils.d(e.getMessage());
                        iView.error("数据访问异常，请重试");
                    }

                    @Override
                    public void onNext(Science science) {
                        int resultOffset = getResultOffset(science);
                        iView.setExtraData(resultOffset);
                        List<Article> datas = science.getResult();
//                        LogUtils.d(datas);
                        if(showLoading) {
                            iView.renderNetData(datas);
//                            LogUtils.v("it the first  inner inner+++++++++++" + isFirst + ".....................");
                        } else {
                            iView.refreshComplete(datas);
                        }

                        saveToDb(datas);
                    }
                });
    }

    private int getResultOffset(Science science) {
        //如果API返回的总数据量大于offset+limit，则表示还有数据，否则表示没有更多数据
        if(science.getTotal()>science.getOffset()+science.getLimit()) {
            return science.getOffset()+science.getLimit();
        }

        return -1;
    }


    public void loadMoreData(String channel,int offset) {
        scienceApi.getScienceByChannel(channel,offset,limit)
                .subscribe(new Subscriber<Science>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.error("数据访问异常，请重试");
                    }

                    @Override
                    public void onNext(Science science) {
                        iView.loadMoreComplete(science.getResult());
                    }
                });
    }

    private DBArticle convertArticleToDBArticle(Article article) {
        DBArticle dbArticle = new DBArticle();
        dbArticle.setId((long) article.getId());
        dbArticle.setTitle(article.getTitle());
        dbArticle.setUrl(article.getUrl());
        dbArticle.setDate_published(article.getDate_published());
        if(null != article.getImage_info()) {
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
        if(null != article.getImage_info()) {
            articleCollection.setImage(article.getImage_info().getUrl());
        }
        articleCollection.setDescription(article.getSummary());
        articleCollection.setChannel_keys(article.getChannel_keys()[0]);
        articleCollection.setComment_count(article.getReplies_count());
        articleCollection.setInfo(article.getInfo());

        return articleCollection;
    }

    private void saveToDb(List<Article> articles) {
        if(articles == null || articles.isEmpty()){
            return;
        }

        //删除daily表的全部数据
        String channel = articles.get(0).getChannel_keys()[0];
        List<DBArticle> oldArticles = articleDao.queryBuilder().where(DBArticleDao.Properties.Channel_keys.eq(channel)).list();
        articleDao.deleteInTx(oldArticles);

        final List<DBArticle> dbArticles = convertDBArticles(articles);
        //批量保存daily数据
        articleDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<dbArticles.size(); i++){
                    DBArticle dbArticle = dbArticles.get(i);
                    articleDao.insertOrReplace(dbArticle);
                }
            }
        });
    }

    private List<DBArticle> convertDBArticles(List<Article> articles) {
        List<DBArticle> dbArticles = new ArrayList<>();
        for(int i=0; i<articles.size(); i++) {
            dbArticles.add(convertArticleToDBArticle(articles.get(i)));
        }

        return dbArticles;
    }

    private Article convertDBArticleToArticle(DBArticle dbArticle) {
        Article article = new Article();
        article.setId(Integer.valueOf(dbArticle.getId()+""));
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
        for(int i=0; i<dbArticles.size(); i++) {
            articles.add(convertDBArticleToArticle(dbArticles.get(i)));
        }

        return articles;
    }

    @Override
    public void detachView() {

    }

}

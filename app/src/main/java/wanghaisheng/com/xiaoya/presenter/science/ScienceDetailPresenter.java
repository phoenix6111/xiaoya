package wanghaisheng.com.xiaoya.presenter.science;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.api.science.ArticleApi;
import wanghaisheng.com.xiaoya.api.science.ScienceApi;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.beans.ArticleResult;
import wanghaisheng.com.xiaoya.db.ArticleCollection;
import wanghaisheng.com.xiaoya.db.ArticleCollectionDao;
import wanghaisheng.com.xiaoya.db.DBArticle;
import wanghaisheng.com.xiaoya.db.DBArticleDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseDetailPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/4/16.
 */
public class ScienceDetailPresenter extends BaseDetailPresenter<ScienceDetailView> {

    @Inject
    ScienceApi scienceApi;
    @Inject
    DBArticleDao articleDao;
    @Inject
    ArticleCollectionDao articleCollectionDao;
    @Inject
    ArticleApi articleApi;

    @Inject @Singleton
    public ScienceDetailPresenter() {}

    @Override
    public void loadEntityDetail(int entityId) {
        Subscription subscription = articleApi.getArticleDetail(entityId)
                .compose(SchedulersCompat.<ArticleResult>applyIoSchedulers())
                .subscribe(new Action1<ArticleResult>() {
                    @Override
                    public void call(ArticleResult result) {
//                        LogUtils.v(article);
                        if(null != iView) {
                            String webPageStr = buildPageStr(result.getResult());
//                        LogUtils.v(webPageStr);
                            iView.renderWebview(webPageStr);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(null != iView) {
                            if (throwable instanceof NetworkErrorException) {
                                iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                            } else {
                                iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                            }
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    private String buildPageStr(Article article) {
        StringBuilder sb = new StringBuilder();
        sb.append("<head>\n" +
                "    <meta charset=\"UTF-8\"/>\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge,chrome=1\"/>\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width,initial-scale=1.0,maximum-scale=1,user-scalable=no\"/>");
        sb.append("<title>")
                .append(article.getTitle())
                .append("</title>");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"article_detail.css\" /></head><body></head>\n" +
                "<body>\n" +
                "<div class=\"container article-page\">\n" +
                "    <div class=\"main\">\n" +
                "        <div class=\"content\">\n" +
                "<div class=\"content-th\">\n" +
                        "                <h1 itemprop=\"http://purl.org/dc/terms/title\" id=\"articleTitle\">"+article.getTitle()+"</h1>\n" +
                        "                <p itemprop=\"http://rdfs.org/sioc/ns#note\" class=\"ghide\"></p>\n" +
                        "                <div class=\"content-th-info\">\n" +
                        "                        <a itemprop=\"http://rdfs.org/sioc/ns#has_creator\" title=\"Zach St. George\" href=\"\" data-ukey=\"\">Zach St. George</a>\n" +
                        "                    <span>发表于 &nbsp;"+article.getDate_published()+"</span>             \n" +
                        "                </div>\n" +
                        "            </div>"+

                "            <div class=\"content-txt\" id=\"articleContent\">\n" +
                "                <div class=\"document\">");
        sb.append(article.getContent());
        sb.append("</div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>");

        return sb.toString();
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

        Subscription subscription = articleCollectionObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<ArticleCollection>() {
                                    @Override
                                    public void call(ArticleCollection articleCollection) {
                                        iView.updateCollectionFlag(null != articleCollection);
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        LogUtils.d(throwable);
                                    }
                                });

        compositeSubscription.add(subscription);

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

}

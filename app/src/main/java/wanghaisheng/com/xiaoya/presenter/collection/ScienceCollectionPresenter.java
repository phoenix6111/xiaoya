package wanghaisheng.com.xiaoya.presenter.collection;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.db.ArticleCollection;
import wanghaisheng.com.xiaoya.db.ArticleCollectionDao;
import wanghaisheng.com.xiaoya.presenter.base.Presenter;

/**
 * Created by sheng on 2016/4/20.
 */
public class ScienceCollectionPresenter extends Presenter<Article,ScienceCollectionView> {
    private static final String TAG = "ScienceCollectionPresenter";

    @Inject
    ArticleCollectionDao articleCollectionDao;

    @Inject
    public ScienceCollectionPresenter() {}

    public void loadArticleFromDb(final int page) {
        iView.showLoading();
        Observable<List<Article>> listObservable = Observable.create(new Observable.OnSubscribe<List<Article>>() {
            @Override
            public void call(Subscriber<? super List<Article>> subscriber) {
                subscriber.onNext(loadArticle());
                subscriber.onCompleted();
            }
        });
        Subscription subscription = listObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Article>>() {
                    @Override
                    public void onCompleted() {
                        if(null != iView) {
                            iView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.v(e);
                        if(null != iView) {
                            iView.hideLoading();
                            iView.error("数据访问异常，请稍后重试");
                        }
                    }

                    @Override
                    public void onNext(List<Article> articles) {
                        /*LogUtils.v(TAG,"print ScienceCollectionPresenter................");
                        LogUtils.v(articles);*/
                        if(null != iView) {
                            iView.renderArticles(page,articles);
                        }
                    }
                });

        compositeSubscription.add(subscription);
    }

    public List<Article> loadArticle() {
        List<ArticleCollection> articleCollections = articleCollectionDao.loadAll();
        List<Article> articles = convertArticleCollectionToArticle(articleCollections);
        return articles;
    }

    public List<Article> convertArticleCollectionToArticle(List<ArticleCollection> articleCollections) {
        List<Article> articles = new ArrayList<>();
        /*LogUtils.v(TAG,"article collections....................");
        LogUtils.v(articleCollections);*/
        for(ArticleCollection articleCollection : articleCollections) {
            LogUtils.v(articleCollection);
            Article article = new Article();
            article.setId(Integer.valueOf(articleCollection.getId()+""));
            article.setTitle(articleCollection.getTitle());
            article.setUrl(articleCollection.getUrl());
            article.setInfo(articleCollection.getInfo());
            article.setSummary(articleCollection.getDescription());
            article.setChannel_keys(new String[]{articleCollection.getChannel_keys()});
            if(!TextUtils.isEmpty(articleCollection.getImage())) {
                Article.Image_info image_info = article.new Image_info();
                image_info.setUrl(articleCollection.getImage());
                article.setImage_info(image_info);
            }
            LogUtils.v(article);
            articles.add(article);
        }

        return articles;
    }
}

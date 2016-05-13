package wanghaisheng.com.xiaoya.datasource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticle;
import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticleApi;
import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticleWrapper;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/13.
 */
public class WeiArticleData extends Data {

    List<WeiArticle> memoryDatas = new ArrayList<>();

    CacheManager cacheManager;
    WeiArticleApi weiArticleApi;

    public WeiArticleData(CacheManager cacheManager,WeiArticleApi weiArticleApi){
        this.cacheManager = cacheManager;
        this.weiArticleApi = weiArticleApi;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<List<WeiArticle>> momory() {
        Observable observable = Observable.create(new Observable.OnSubscribe<List<WeiArticle>>() {
            @Override
            public void call(Subscriber<? super List<WeiArticle>> subscriber) {
                subscriber.onNext(memoryDatas);
                subscriber.onCompleted();
                setDataSource(DATA_SOURCE_MEMORY);
            }
        });

        return observable;
    }

    /**
     * disk级缓存
     * @return
     */
    public Observable<List<WeiArticle>> disk() {
        Observable<List<WeiArticle>> observable = Observable.create(new Observable.OnSubscribe<List<WeiArticle>>() {
            @Override
            public void call(Subscriber<? super List<WeiArticle>> subscriber) {
//                LogUtils.d("load from disk");
                if(cacheManager.isExistDataCache(getCacheKey("wei"))) {
                    List<WeiArticle> items = (List<WeiArticle>) cacheManager.readObject(getCacheKey("wei"));
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(new ArrayList<WeiArticle>());
                    subscriber.onCompleted();
                }

                setDataSource(DATA_SOURCE_DISK);
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<WeiArticle>>() {
                    @Override
                    public void call(List<WeiArticle> movies) {
//                LogUtils.d("disk save to memory");
                        if(!ListUtils.isEmpty(movies)) {
                            memoryDatas = movies;
                        }
                    }
                });

        return observable;
    }

    public Observable<List<WeiArticle>> network() {
//        LogUtils.d("enter network..............");
        return weiArticleApi.getWeiArticleWrapper(0)
                .subscribeOn(Schedulers.io())
                //.map(GankBeautyResultToItemsMapper.getInstance())
                .map(new Func1<WeiArticleWrapper, List<WeiArticle>>() {
                    @Override
                    public List<WeiArticle> call(WeiArticleWrapper wrapper) {
                        return wrapper.getResult().getList();
                    }
                })
                .doOnNext(new Action1<List<WeiArticle>>() {
                    @Override
                    public void call(List<WeiArticle> movies) {
                        if(!ListUtils.isEmpty(movies)) {
                            cacheManager.saveObject((Serializable) movies,getCacheKey("wei"));
                            memoryDatas = movies;
                        }
                        setDataSource(DATA_SOURCE_NETWORK);
                    }
                });

    }


    public Observable<List<WeiArticle>> subscribeData() {
        return Observable.concat(momory(),disk(),network())
                .first(new Func1<List<WeiArticle>, Boolean>() {
                    @Override
                    public Boolean call(List<WeiArticle> movies) {
//                        LogUtils.d("first................");
                        return !ListUtils.isEmpty(movies);
                    }
                });
    }


    /**
     * 生成cache的key
     * @param channel
     * @return
     */
    public String getCacheKey(String channel) {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(channel).toString();
    }

    protected String getCacheKeyPrefix(){
        return "wei_article";
    }

}

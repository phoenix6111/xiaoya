package wanghaisheng.com.xiaoya.datasource;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuApi;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuContentResult;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/11.
 */
public class JianshuData extends Data {
    JianshuContentResult[] cache = new JianshuContentResult[JianshuApi.CHANNEL_TAG.length];

    CacheManager cacheManager;
    JianshuApi jianshuApi;

    public JianshuData(CacheManager cacheManager,JianshuApi jianshuApi) {
        this.cacheManager = cacheManager;
        this.jianshuApi = jianshuApi;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<JianshuContentResult> memory(String channel) {
        final int i = getIndex(channel);

        Observable observable = Observable.create(new Observable.OnSubscribe<JianshuContentResult>() {
            @Override
            public void call(Subscriber<? super JianshuContentResult> subscriber) {
                subscriber.onNext(cache[i]);
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
    public Observable<JianshuContentResult> disk(final String channel) {
        final int i = getIndex(channel);
        final String cacheKey = getCacheKey(channel);
        Observable<JianshuContentResult> observable = Observable.create(new Observable.OnSubscribe<JianshuContentResult>() {
            @Override
            public void call(Subscriber<? super JianshuContentResult> subscriber) {
//                LogUtils.d("load from disk");
                if(cacheManager.isExistDataCache(cacheKey)&&cacheManager.isCacheDataFailure(cacheKey)) {
                    JianshuContentResult items = (JianshuContentResult) cacheManager.readObject(cacheKey);
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }

                setDataSource(DATA_SOURCE_DISK);
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Action1<JianshuContentResult>() {
                    @Override
                    public void call(JianshuContentResult datas) {
//                LogUtils.d("disk save to memory");
                        if(null != datas && (!ListUtils.isEmpty(datas.getContents()))) {
                            cache[i] = datas;
                        }
                    }
                });

        return observable;
    }

    /**
     * 从网络加载并保存到硬盘和内存
     * @param channel
     * @return
     */
    public Observable<JianshuContentResult> network(final String channel) {
        //获取channel在channel_tag数组中的索引
//        int i = Arrays.binarySearch(DailyApi.THEME_ID,themeId);
        final int i = getIndex(channel);

        return jianshuApi.getIndexPageContentResult(channel)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<JianshuContentResult>() {
                    @Override
                    public void call(JianshuContentResult datas) {
                        //将数据保存进disk和memory
                        if(null!=datas&&!ListUtils.isEmpty(datas.getContents())) {
                            cacheManager.saveObject(datas,getCacheKey(channel));
                            cache[i] = datas;
                        }
                        setDataSource(DATA_SOURCE_NETWORK);
                    }
                });


    }

    /**
     * 根据channel获取数据
     * @param channel
     * @return
     */
    public Observable<JianshuContentResult> subscribeData(String channel) {
        return Observable.concat(
                memory(channel)
                ,disk(channel)
                ,network(channel))
                .first(new Func1<JianshuContentResult, Boolean>() {
                    @Override
                    public Boolean call(JianshuContentResult datas) {
//                        LogUtils.d("first................");
                        if((null!=datas)&&(!ListUtils.isEmpty(datas.getContents()))) {
                            return true;
                        }
                        return false;
                    }
                });
    }


    private int getIndex(String channel) {
        int index = -1;
        for (int i = 0; i< JianshuApi.CHANNEL_TAG.length; i++) {
            if(channel.equals(JianshuApi.CHANNEL_TAG[i])) {
                index = i;
                continue;
            }
        }

        return index;
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
        return "jianshu";
    }

}

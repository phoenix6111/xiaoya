package wanghaisheng.com.xiaoya.datasource;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.science.ScienceApi;
import wanghaisheng.com.xiaoya.beans.Science;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.presenter.science.ScienceListPresenter;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/5.
 */
public class ScienceData extends Data {
    Science[] cache = new Science[ScienceApi.CHANNEL_TAG.length];

    CacheManager cacheManager;
    ScienceApi scienceApi;


    public ScienceData(CacheManager cacheManager,ScienceApi scienceApi){
        this.cacheManager = cacheManager;
        this.scienceApi = scienceApi;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<Science> momory(String channel) {
        final int i = getIndex(channel);

        Observable observable = Observable.create(new Observable.OnSubscribe<Science>() {
            @Override
            public void call(Subscriber<? super Science> subscriber) {
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
    public Observable<Science> disk(final String channel) {
        final int i = getIndex(channel);
        final String cacheKey = getCacheKey(channel);
        Observable<Science> observable = Observable.create(new Observable.OnSubscribe<Science>() {
            @Override
            public void call(Subscriber<? super Science> subscriber) {
//                LogUtils.d("load from disk");
                if(cacheManager.isExistDataCache(cacheKey)) {
                    Science items = (Science) cacheManager.readObject(cacheKey);
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }

                setDataSource(DATA_SOURCE_DISK);
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Action1<Science>() {
            @Override
            public void call(Science science) {
//                LogUtils.d("disk save to memory");
                if(null != science && (!ListUtils.isEmpty(science.getResult()))) {
                    cache[i] = science;
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
    public Observable<Science> network(final String channel) {
        //获取channel在channel_tag数组中的索引
//        int i = Arrays.binarySearch(DailyApi.THEME_ID,themeId);
        final int i = getIndex(channel);

        return scienceApi.getScienceByChannel(channel,0, ScienceListPresenter.limit)
                .subscribeOn(Schedulers.io())
//                .map(GankBeautyResultToItemsMapper.getInstance())
                .doOnNext(new Action1<Science>() {
                    @Override
                    public void call(Science science) {
                        //将数据保存进disk和memory
                        if(null!=science&&!ListUtils.isEmpty(science.getResult())) {
                            cacheManager.saveObject(science,getCacheKey(channel));
                            cache[i] = science;
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
    public Observable<Science> subscribeData(String channel) {
        return Observable.concat(momory(channel),disk(channel),network(channel))
                .first(new Func1<Science, Boolean>() {
                    @Override
                    public Boolean call(Science science) {
//                        LogUtils.d("first................");
                        if((null!=science)&&(!ListUtils.isEmpty(science.getResult()))) {
                            return true;
                        }
                        return false;
                    }
                });
    }


    private int getIndex(String channel) {
        int index = -1;
        for (int i=0; i<ScienceApi.CHANNEL_TAG.length; i++) {
            if(channel.equals(ScienceApi.CHANNEL_TAG[i])) {
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
        return "science";
    }


}

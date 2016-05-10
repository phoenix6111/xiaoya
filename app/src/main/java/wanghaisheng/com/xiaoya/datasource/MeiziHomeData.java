package wanghaisheng.com.xiaoya.datasource;

import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.db.Group;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziHomeData extends Data {
    List<Group>[] cache = new List[MeiziApi.CHANNEL_TAG.length];

    private CacheManager cacheManager;
    private MeiziApi meiziApi;

    public MeiziHomeData(CacheManager cacheManager, MeiziApi meiziApi) {
        this.cacheManager = cacheManager;
        this.meiziApi = meiziApi;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<List<Group>> momory(String channel) {
        final int i = getIndex(channel);

        Observable observable = Observable.create(new Observable.OnSubscribe<List<Group>>() {
            @Override
            public void call(Subscriber<? super List<Group>> subscriber) {
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
    public Observable<List<Group>> disk(final String channel) {
        final int i = getIndex(channel);
        final String cacheKey = getCacheKey(channel);
        Observable<List<Group>> observable = Observable.create(new Observable.OnSubscribe<List<Group>>() {
            @Override
            public void call(Subscriber<? super List<Group>> subscriber) {
//                LogUtils.d("load from disk");
                if(cacheManager.isExistDataCache(cacheKey)&&cacheManager.isCacheDataFailure(cacheKey)) {
                    List<Group> items = (List<Group>) cacheManager.readObject(cacheKey);
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }

                setDataSource(DATA_SOURCE_DISK);
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<Group>>() {
                    @Override
                    public void call(List<Group> datas) {
//                LogUtils.d("disk save to memory");
                        if(!ListUtils.isEmpty(datas)) {
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
    public Observable<List<Group>> network(final String channel) {
        //获取channel在channel_tag数组中的索引
        final int i = getIndex(channel);
        final String tempChannel = (channel.equals("home"))?"":channel;

        return meiziApi.getGroup(tempChannel, 1)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<Group>>() {
                    @Override
                    public void call(List<Group> groups) {
                        if(!ListUtils.isEmpty(groups)) {
                            cacheManager.saveObject((Serializable) groups,getCacheKey(channel));
                            cache[i] = groups;
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
    public Observable<List<Group>> subscribeData(String channel) {
        return Observable.concat(momory(channel)
                ,disk(channel)
                ,network(channel))
                .first(new Func1<List<Group>, Boolean>() {
                    @Override
                    public Boolean call(List<Group> datas) {
//                        LogUtils.d("first................");
                        if(!ListUtils.isEmpty(datas)) {
                            return true;
                        }
                        return false;
                    }
                });
    }


    private int getIndex(String channel) {
        int index = -1;
        for (int i = 0; i< MeiziApi.CHANNEL_TAG.length; i++) {
            if(channel.equals(MeiziApi.CHANNEL_TAG[i])) {
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
        return "meizi";
    }

}

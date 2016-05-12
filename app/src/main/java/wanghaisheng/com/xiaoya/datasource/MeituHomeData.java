package wanghaisheng.com.xiaoya.datasource;

import java.io.Serializable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.meitu.MeituApi;
import wanghaisheng.com.xiaoya.api.meitu.MeituGalleryResult;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituHomeData extends Data {
    MeituGalleryResult[] cache = new MeituGalleryResult[MeituApi.TOTAL_TAG.length];

    CacheManager cacheManager;
    MeituApi meituApi;

    public MeituHomeData(CacheManager cacheManager, MeituApi meituApi) {
        this.cacheManager = cacheManager;
        this.meituApi = meituApi;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<MeituGalleryResult> memory(String tag) {

        final int i = getBeautyIndex(tag);

        Observable observable = Observable.create(new Observable.OnSubscribe<MeituGalleryResult>() {
            @Override
            public void call(Subscriber<? super MeituGalleryResult> subscriber) {
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
    public Observable<MeituGalleryResult> disk(final String tag) {
        final int i = getBeautyIndex(tag);
        final String cacheKey = getCacheKey(tag);
        Observable<MeituGalleryResult> observable = Observable.create(new Observable.OnSubscribe<MeituGalleryResult>() {
            @Override
            public void call(Subscriber<? super MeituGalleryResult> subscriber) {
//                LogUtils.d("load from disk");
                if(cacheManager.isExistDataCache(cacheKey)&&cacheManager.isCacheDataFailure(cacheKey)) {
                    MeituGalleryResult items = (MeituGalleryResult) cacheManager.readObject(cacheKey);
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }

                setDataSource(DATA_SOURCE_DISK);
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Action1<MeituGalleryResult>() {
                    @Override
                    public void call(MeituGalleryResult datas) {
//                LogUtils.d("disk save to memory");
                        if(null != datas && (!ListUtils.isEmpty(datas.getList()))) {
                            cache[i] = datas;
                        }
                    }
                });

        return observable;
    }

    /**
     * 从网络加载并保存到硬盘和内存
     * @param tag
     * @return
     */
    public Observable<MeituGalleryResult> network(final String channel, final String tag) {
        //获取channel在channel_tag数组中的索引
//        int i = Arrays.binarySearch(DailyApi.THEME_ID,themeId);
        final int i = getBeautyIndex(tag);

        if(MeituApi.CHANNEL_ID[0].equals(channel)) {
            return meituApi.getBeautyMeituGallery(tag,0)
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Action1<MeituGalleryResult>() {
                        @Override
                        public void call(MeituGalleryResult datas) {
                            //将数据保存进disk和memory
                            if(null != datas && !ListUtils.isEmpty(datas.getList())) {
                                cacheManager.saveObject((Serializable) datas,getCacheKey(tag));
                                cache[i] = datas;
                            }
                            setDataSource(DATA_SOURCE_NETWORK);
                        }
                    });
        } else if(MeituApi.CHANNEL_ID[2].equals(channel)) {
            return meituApi.getFunnyMeituGallery(tag,0)
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Action1<MeituGalleryResult>() {
                        @Override
                        public void call(MeituGalleryResult datas) {
                            //将数据保存进disk和memory
                            if(null != datas && !ListUtils.isEmpty(datas.getList())) {
                                cacheManager.saveObject((Serializable) datas,getCacheKey(tag));
                                cache[i] = datas;
                            }
                            setDataSource(DATA_SOURCE_NETWORK);
                        }
                    });
        }

        return null;
    }

    /**
     * 根据channel获取数据
     * @param tag
     * @return
     */
    public Observable<MeituGalleryResult> subscribeBeautyData(String channel,String tag) {
        return Observable.concat(
                memory(tag)
                , disk(tag)
                , network(channel,tag))
                .first(new Func1<MeituGalleryResult, Boolean>() {
                    @Override
                    public Boolean call(MeituGalleryResult meituGalleryResult) {
                        if(null != meituGalleryResult && !ListUtils.isEmpty(meituGalleryResult.getList())) {
                            return true;
                        }
                        return false;
                    }
                });
    }


    private int getBeautyIndex(String tag) {
        int index = -1;
        String[] tags = meituApi.getTotalTags();
        for (int i = 0; i< tags.length; i++) {
            if(tags[i].equals(tag)) {
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
        return "meitu";
    }

}

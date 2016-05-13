package wanghaisheng.com.xiaoya.datasource;

import java.io.Serializable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.gaoxiao.GaoxiaoApi;
import wanghaisheng.com.xiaoya.beans.GaoxiaoPicResult;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/13.
 */
public class GaoxiaoData extends Data {
    GaoxiaoPicResult[] cache = new GaoxiaoPicResult[GaoxiaoApi.TAGS.length];

    GaoxiaoApi gaoxiaoApi;
    CacheManager cacheManager;

    public GaoxiaoData(GaoxiaoApi gaoxiaoApi,CacheManager cacheManager) {
        this.gaoxiaoApi = gaoxiaoApi;
        this.cacheManager = cacheManager;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<GaoxiaoPicResult> memory(String tag) {

        final int i = getBeautyIndex(tag);

        Observable observable = Observable.create(new Observable.OnSubscribe<GaoxiaoPicResult>() {
            @Override
            public void call(Subscriber<? super GaoxiaoPicResult> subscriber) {
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
    public Observable<GaoxiaoPicResult> disk(final String tag) {
        final int i = getBeautyIndex(tag);
        final String cacheKey = getCacheKey(tag);
        Observable<GaoxiaoPicResult> observable = Observable.create(new Observable.OnSubscribe<GaoxiaoPicResult>() {
            @Override
            public void call(Subscriber<? super GaoxiaoPicResult> subscriber) {
//                LogUtils.d("load from disk");
                if(cacheManager.isExistDataCache(cacheKey)&&cacheManager.isCacheDataFailure(cacheKey)) {
                    GaoxiaoPicResult items = (GaoxiaoPicResult) cacheManager.readObject(cacheKey);
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }

                setDataSource(DATA_SOURCE_DISK);
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Action1<GaoxiaoPicResult>() {
                    @Override
                    public void call(GaoxiaoPicResult datas) {
//                LogUtils.d("disk save to memory");
                        if(null != datas && (!ListUtils.isEmpty(datas.getAll_items()))) {
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
    public Observable<GaoxiaoPicResult> network(final String tag) {
        //获取channel在channel_tag数组中的索引
//        int i = Arrays.binarySearch(DailyApi.THEME_ID,themeId);
        final int i = getBeautyIndex(tag);
        return gaoxiaoApi.getGaoxiaoPicResult(tag,0)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<GaoxiaoPicResult>() {
                    @Override
                    public void call(GaoxiaoPicResult datas) {
                        //将数据保存进disk和memory
                        if(null != datas && !ListUtils.isEmpty(datas.getAll_items())) {
                            cacheManager.saveObject((Serializable) datas,getCacheKey(tag));
                            cache[i] = datas;
                        }
                        setDataSource(DATA_SOURCE_NETWORK);
                    }
                });

    }

    /**
     * 根据channel获取数据
     * @param tag
     * @return
     */
    public Observable<GaoxiaoPicResult> subscribeBeautyData(String tag) {
        return Observable.concat(
                memory(tag)
                , disk(tag)
                , network(tag))
                .first(new Func1<GaoxiaoPicResult, Boolean>() {
                    @Override
                    public Boolean call(GaoxiaoPicResult meituGalleryResult) {
                        if(null != meituGalleryResult && !ListUtils.isEmpty(meituGalleryResult.getAll_items())) {
                            return true;
                        }
                        return false;
                    }
                });
    }


    private int getBeautyIndex(String tag) {
        int index = -1;
        for (int i = 0; i< GaoxiaoApi.TAGS.length; i++) {
            if(GaoxiaoApi.TAGS[i].equals(tag)) {
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

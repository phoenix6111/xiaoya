package wanghaisheng.com.xiaoya.datasource;

import com.apkfuns.logutils.LogUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.beans.Daily;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/5.
 */
public class DailyData extends Data{

    Daily[] cache = new Daily[DailyApi.THEME_ID.length];

    CacheManager cacheManager;
    DailyApi dailyApi;


    public DailyData(CacheManager cacheManager,DailyApi dailyApi){
        this.cacheManager = cacheManager;
        this.dailyApi = dailyApi;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<Daily> momory(int themeId) {
        final int i = getIndex(themeId);

        Observable observable = Observable.create(new Observable.OnSubscribe<Daily>() {
            @Override
            public void call(Subscriber<? super Daily> subscriber) {
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
    public Observable<Daily> disk(int themeId) {
        final int i = getIndex(themeId);
        final String themeStr = DailyApi.THEME_ID[i]+"";
        final String cacheKey = getCacheKey(themeStr);
        Observable<Daily> observable = Observable.create(new Observable.OnSubscribe<Daily>() {
            @Override
            public void call(Subscriber<? super Daily> subscriber) {
//                LogUtils.d("load from disk");
                if(cacheManager.isExistDataCache(cacheKey)) {
                    Daily items = (Daily) cacheManager.readObject(cacheKey);
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }

                setDataSource(DATA_SOURCE_DISK);
            }
        }).doOnNext(new Action1<Daily>() {
            @Override
            public void call(Daily daily) {
//                LogUtils.d("disk save to memory");
                if(null != daily && (!ListUtils.isEmpty(daily.getStories()))) {
                    cache[i] = daily;
                }
            }
        }).subscribeOn(Schedulers.io());

        return observable;
    }

    /**
     * 从网络加载并保存到硬盘和内存
     * @param themeId
     * @return
     */
    public Observable<Daily> network(int themeId) {
        //获取channel在channel_tag数组中的索引
//        int i = Arrays.binarySearch(DailyApi.THEME_ID,themeId);
        final int i = getIndex(themeId);
        LogUtils.d("themeId............"+themeId);
        if(themeId == DailyApi.THEME_ID[0]) {
            return dailyApi.getDaily(0).subscribeOn(Schedulers.io())
                    .map(new Func1<Daily, Daily>() {
                        @Override
                        public Daily call(Daily daily) {
                            if(i>0) {
                                List<Story> stories = daily.getStories();
                                stories.remove(0);
                                daily.setStories(stories);
                            }
                            return daily;
                        }
                    })
                    .doOnNext(new Action1<Daily>() {
                        @Override
                        public void call(Daily daily) {
                            if(null != daily && !ListUtils.isEmpty(daily.getStories())) {
                                cache[i] = daily;
                                cacheManager.saveObject(daily,getCacheKey("hot"));
                            }
                            setDataSource(DATA_SOURCE_NETWORK);
                        }
                    });
        } else {

            return dailyApi.getDailyByTheme(themeId).subscribeOn(Schedulers.io())
                    .map(new Func1<Daily, Daily>() {
                        @Override
                        public Daily call(Daily daily) {
                            if (i > 0) {
                                List<Story> stories = daily.getStories();
                                stories.remove(0);
                                daily.setStories(stories);
                            }
                            return daily;
                        }
                    })
                    .doOnNext(new Action1<Daily>() {
                        @Override
                        public void call(Daily daily) {
                            if (null != daily && !ListUtils.isEmpty(daily.getStories())) {
                                cache[i] = daily;
                                cacheManager.saveObject(daily, getCacheKey("theme"));
                            }
                            setDataSource(DATA_SOURCE_NETWORK);
                        }
                    });
        }

    }

    /**
     * 根据themeId获取数据
     * @param themeId
     * @return
     */
    public Observable<Daily> subscribeData(int themeId) {
        return Observable.concat(momory(themeId),disk(themeId),network(themeId))
                .first(new Func1<Daily, Boolean>() {
                    @Override
                    public Boolean call(Daily daily) {
//                        LogUtils.d("first................");
                        if((null!=daily)&&(!ListUtils.isEmpty(daily.getStories()))) {
                            return true;
                        }
                        return false;
                    }
                });
    }


    /**
     * 获取指定的themeId在DailyApi.themid中的索引
     * @param themeId
     * @return
     */
    private int getIndex(int themeId) {
//        LogUtils.d("themeId............"+themeId);
        int index = -1;
        for(int i=0; i<DailyApi.THEME_ID.length; i++) {
            if(themeId==DailyApi.THEME_ID[i]) {
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
        return "daily";
    }
}

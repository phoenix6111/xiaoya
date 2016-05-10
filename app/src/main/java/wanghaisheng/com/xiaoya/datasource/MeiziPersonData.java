package wanghaisheng.com.xiaoya.datasource;

import com.apkfuns.logutils.LogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.Exception.NoDataException;
import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.db.Content;
import wanghaisheng.com.xiaoya.db.ContentDao;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziPersonData extends Data {
    //缓存对象
    CacheBean<List<Content>> cache = new CacheBean<>();

    private CacheManager cacheManager;
    private MeiziApi meiziApi;
    private ContentDao contentDao;

    public MeiziPersonData(CacheManager cacheManager,MeiziApi meiziApi,ContentDao contentDao) {
        this.cacheManager = cacheManager;
        this.meiziApi = meiziApi;
        this.contentDao = contentDao;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<List<Content>> momory(final String groupId) {

        Observable observable = Observable.create(new Observable.OnSubscribe<List<Content>>() {
            @Override
            public void call(Subscriber<? super List<Content>> subscriber) {
                if(groupId.equals(cache.getKey())&&(!ListUtils.isEmpty(cache.getT()))) {
                    subscriber.onNext(cache.getT());
                } else {
                    subscriber.onNext(new ArrayList<Content>());
                }

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
    public Observable<List<Content>> database(final String groupId) {
        Observable<List<Content>> observable = Observable.create(new Observable.OnSubscribe<List<Content>>() {
            @Override
            public void call(Subscriber<? super List<Content>> subscriber) {
//                LogUtils.d("load from disk");
                /*if(cacheManager.isExistDataCache(cacheKey)) {
                    List<Content> items = (List<Content>) cacheManager.readObject(cacheKey);
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }*/
                //从数据库加载
                List<Content> contents = contentDao.queryBuilder().where(ContentDao.Properties.Groupid.eq(groupId)).list();
                if(!ListUtils.isEmpty(contents)) {
                    subscriber.onNext(contents);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new NoDataException("no data"));
                }
                setDataSource(DATA_SOURCE_DISK);
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<Content>>() {
                    @Override
                    public void call(List<Content> datas) {
//                LogUtils.d("disk save to memory");
                        if(!ListUtils.isEmpty(datas)) {
                            cache.setT(datas);
                            cache.setKey(getCacheKey(groupId));
                        }
                    }
                });

        return observable;
    }

    /**
     * 分别从memory和disk中检测是否存在缓存
     * @param groupId
     * @return
     */
    public boolean isCacheExists(String groupId) {
        if(groupId.equals(cache.getKey())) {
            return true;
        }

        if(cacheManager.isExistDataCache(getCacheKey(groupId))) {
            return true;
        }

        return false;
    }

    public void saveToCache(List<Content> contents,String groupId) {
        if(!ListUtils.isEmpty(contents)) {
            cache.setKey(groupId);
            cache.setT(contents);
            cacheManager.saveObject((Serializable) contents,getCacheKey(groupId));
        }
    }

    /**
     * 从网络加载,因为第一次获取图片信息时候要逐张获取，所以很慢，在客户端显示的时候可以一张一张显示
     * @param groupId
     * @return
     */
    public Observable<Content> loadFromNetwork(final String groupId) {
        //获取channel在channel_tag数组中的索引
//        int i = Arrays.binarySearch(DailyApi.THEME_ID,themeId);
        LogUtils.d("meiziPersonData.........groupId........."+groupId);
        return meiziApi.getContents(groupId)
                .subscribeOn(Schedulers.io());

    }

    public Observable<List<Content>> loadFromCache(String groupId) {
        return Observable.concat(
                momory(groupId)
                ,database(groupId)
//                ,network(groupId)
                )
                .first(new Func1<List<Content>, Boolean>() {
                    @Override
                    public Boolean call(List<Content> datas) {
//                        LogUtils.d("meizi person data first................");
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
     * @param groupId
     * @return
     */
    public String getCacheKey(String groupId) {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(groupId).toString();
    }

    protected String getCacheKeyPrefix(){
        return "meizi_person";
    }
}

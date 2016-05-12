package wanghaisheng.com.xiaoya.datasource;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.meitu.MeituApi;
import wanghaisheng.com.xiaoya.api.meitu.MeituPictureResult;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.db.MeituPicture;
import wanghaisheng.com.xiaoya.db.MeituPictureDao;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/12.
 */
public class MeituPersonData extends Data {
    //缓存对象
    CacheBean<List<MeituPicture>> cache = new CacheBean<>();

    private CacheManager cacheManager;
    private MeituApi meituApi;
    private MeituPictureDao pictureDao;

    public MeituPersonData(CacheManager cacheManager,MeituApi meiziApi,MeituPictureDao pictureDao) {
        this.cacheManager = cacheManager;
        this.meituApi = meiziApi;
        this.pictureDao = pictureDao;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<List<MeituPicture>> momory(final String id) {

        Observable observable = Observable.create(new Observable.OnSubscribe<List<MeituPicture>>() {
            @Override
            public void call(Subscriber<? super List<MeituPicture>> subscriber) {
                if(id.equals(cache.getKey())&&(!ListUtils.isEmpty(cache.getT()))) {
                    subscriber.onNext(cache.getT());
                } else {
                    subscriber.onNext(new ArrayList<MeituPicture>());
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
    public Observable<List<MeituPicture>> database(final String id) {
        Observable<List<MeituPicture>> observable = Observable.create(new Observable.OnSubscribe<List<MeituPicture>>() {
            @Override
            public void call(Subscriber<? super List<MeituPicture>> subscriber) {

                //从数据库加载
                List<MeituPicture> contents = pictureDao.queryBuilder().where(MeituPictureDao.Properties.GroupId.eq(id)).list();
                /*if(!ListUtils.isEmpty(contents)) {
                    subscriber.onNext(contents);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new NoDataException("no data"));
                }*/
                subscriber.onNext(contents);
                subscriber.onCompleted();
                setDataSource(DATA_SOURCE_DISK);
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<MeituPicture>>() {
                    @Override
                    public void call(List<MeituPicture> datas) {
//                LogUtils.d("disk save to memory");
                        if(!ListUtils.isEmpty(datas)) {
                            cache.setT(datas);
                            cache.setKey(getCacheKey(id));
                            saveToDatabase(datas);
                        }
                    }
                });

        return observable;
    }

    public void saveToDatabase(List<MeituPicture> pictures) {
        for (MeituPicture picture : pictures) {
            pictureDao.insertOrReplace(picture);
        }
    }

    /**
     * 从网络加载
     * @param id
     * @return
     */
    public Observable<List<MeituPicture>> network(final String id) {
        //获取channel在channel_tag数组中的索引
//        int i = Arrays.binarySearch(DailyApi.THEME_ID,themeId);
        return meituApi.getMeituPicture(id)
                .subscribeOn(Schedulers.io())
                .map(new Func1<MeituPictureResult, List<MeituPicture>>() {
                    @Override
                    public List<MeituPicture> call(MeituPictureResult meituPictureResult) {
//                        LogUtils.d("print person list data....");
//                        LogUtils.d(meituPictureResult.getList());
                        return meituPictureResult.getList();
                    }
                });

    }

    public Observable<List<MeituPicture>> loadData(String id) {
        return Observable.concat(
                momory(id)
                ,database(id)
                ,network(id)
        )
                .first(new Func1<List<MeituPicture>, Boolean>() {
                    @Override
                    public Boolean call(List<MeituPicture> datas) {
//                        LogUtils.d("meizi person data first................");
                        if(!ListUtils.isEmpty(datas)) {
                            return true;
                        }
                        return false;
                    }
                });
    }


    /**
     * 生成cache的key
     * @param
     * @return
     */
    public String getCacheKey(String id) {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(id).toString();
    }

    protected String getCacheKeyPrefix(){
        return "meitu";
    }
}

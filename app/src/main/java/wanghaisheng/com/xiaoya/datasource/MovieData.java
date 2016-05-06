package wanghaisheng.com.xiaoya.datasource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.beans.MovieList;
import wanghaisheng.com.xiaoya.cache.CacheManager;
import wanghaisheng.com.xiaoya.db.Movie;
import wanghaisheng.com.xiaoya.presenter.movie.MovieListPresenter;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/5.
 */
public class MovieData extends Data{
    List<Movie> memoryDatas = new ArrayList<>();

    CacheManager cacheManager;
    MovieApi movieApi;

    public MovieData(CacheManager cacheManager,MovieApi movieApi){
        this.cacheManager = cacheManager;
        this.movieApi = movieApi;
    }

    /**
     * 内存级缓存
     * @return
     */
    public Observable<List<Movie>> momory() {
        Observable observable = Observable.create(new Observable.OnSubscribe<List<Movie>>() {
            @Override
            public void call(Subscriber<? super List<Movie>> subscriber) {
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
    public Observable<List<Movie>> disk() {
        Observable<List<Movie>> observable = Observable.create(new Observable.OnSubscribe<List<Movie>>() {
            @Override
            public void call(Subscriber<? super List<Movie>> subscriber) {
//                LogUtils.d("load from disk");
                if(cacheManager.isExistDataCache(getCacheKey("cache"))) {
                    List<Movie> items = (List<Movie>) cacheManager.readObject(getCacheKey("cache"));
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(new ArrayList<Movie>());
                    subscriber.onCompleted();
                }

                setDataSource(DATA_SOURCE_DISK);
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<Movie>>() {
            @Override
            public void call(List<Movie> movies) {
//                LogUtils.d("disk save to memory");
                if(!ListUtils.isEmpty(movies)) {
                    memoryDatas = movies;
                }
            }
        });

        return observable;
    }

    public Observable<List<Movie>> network() {
//        LogUtils.d("enter network..............");
        return movieApi.getMovieByType(0, MovieListPresenter.LIMIT)
                .subscribeOn(Schedulers.io())
                //.map(GankBeautyResultToItemsMapper.getInstance())
                .map(new Func1<MovieList, List<Movie>>() {
                    @Override
                    public List<Movie> call(MovieList movieList) {
                        return movieList.getData().getMovies();
                    }
                })
                .doOnNext(new Action1<List<Movie>>() {
                    @Override
                    public void call(List<Movie> movies) {
                        if(!ListUtils.isEmpty(movies)) {
                            cacheManager.saveObject((Serializable) movies,getCacheKey("cache"));
                            memoryDatas = movies;
                        }
                        setDataSource(DATA_SOURCE_NETWORK);
                    }
                });

    }


    public Observable<List<Movie>> subscribeData() {
        return Observable.concat(momory(),disk(),network())
                .first(new Func1<List<Movie>, Boolean>() {
                    @Override
                    public Boolean call(List<Movie> movies) {
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
        return "movie";
    }
}

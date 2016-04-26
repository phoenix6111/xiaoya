package wanghaisheng.com.xiaoya.presenter.movie;

import com.apkfuns.logutils.LogUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.beans.MovieList;
import wanghaisheng.com.xiaoya.db.Movie;
import wanghaisheng.com.xiaoya.db.MovieDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;

/**
 * Created by sheng on 2016/4/18.
 */
public class MovieListPresenter extends BaseListPresenter<MovieList,MoveListView>{
    //每页显示的数目
    public static final int LIMIT = 20;
    private boolean isFirst = true;
    //查询API时的offset
    //private int offset = 0;

    @Inject
    MovieApi movieApi;
    @Inject
    MovieDao movieDao;

    @Inject @Singleton
    public MovieListPresenter() {}

    /**
     * 根据themeId从数据库查询数据
     */
    public void loadFromDb() {
        //LogUtils.v("load newest stories...........................................................");
        //先从数据库里面查
        iView.showLoading();
        Observable.create(new Observable.OnSubscribe<List<Movie>>() {
            @Override
            public void call(Subscriber<? super List<Movie>> subscriber) {
                subscriber.onNext(loadNewestFromDb());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Movie>>() {
                    @Override
                    public void call(List<Movie> movies) {
                        iView.hideLoading();
                        iView.renderDbData(movies);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable.getMessage());
                        iView.error("数据访问异常，请重试");
                        iView.hideLoading();
                    }
                });

    }

    /**
     * 一开始从数据库里面查，显示到界面，然后再从网络查最新的数据
     */
    private List<Movie> loadNewestFromDb() {
        List<Movie> movies = movieDao.loadAll();
        return movies;
    }

    //从网络加载最新数据
    public void loadNewFromNet(final int offset, final int limit, final boolean showLoading) {
        if(showLoading) {
            iView.showLoading();
        }
        movieApi.getMovieByType(offset,limit)
                .subscribe(new Subscriber<MovieList>() {
                    public void onCompleted() {
                        //如果是第一次从远程加载，则不显示loadmore的下拉框
                        if(showLoading) {
                            iView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.hideLoading();
                        LogUtils.d(e.getMessage());
                        iView.error("数据访问异常，请重试");
                    }

                    @Override
                    public void onNext(MovieList movieList) {
                        //offset = movie.getOffset();
                        List<Movie> datas = movieList.getData().getMovies();
//                        LogUtils.d(datas);
                        if(isFirst) {
                            isFirst = false;
                            iView.renderNetData(datas);
                            //将数据保存进数据库
                            saveToDb(datas);
//                            LogUtils.v("it the first  inner inner+++++++++++" + isFirst + ".....................");
                        } else {
                            iView.refreshComplete(datas);
                        }

                        //offset += limit;
                    }
                });
    }

    public void loadMoreData(int offset,int limit) {
        movieApi.getMovieByType(offset,limit)
                .subscribe(new Subscriber<MovieList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.error("数据访问异常，请重试");
                    }

                    @Override
                    public void onNext(MovieList movieList) {
                        iView.loadMoreComplete(movieList.getData().getMovies());
                    }
                });
    }

    private void saveToDb(final List<Movie> movies) {
        if(movies == null || movies.isEmpty()){
            return;
        }

        //删除daily表的全部数据
        movieDao.deleteAll();

        //批量保存daily数据
        movieDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<movies.size(); i++){
                    Movie movie = movies.get(i);
                    movieDao.insertOrReplace(movie);
                }
            }
        });
    }



    @Override
    public void detachView() {
        if(iView != null) {
            iView = null;
        }
    }


}

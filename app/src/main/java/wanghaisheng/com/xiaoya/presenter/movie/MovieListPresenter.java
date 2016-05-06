package wanghaisheng.com.xiaoya.presenter.movie;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.beans.MovieList;
import wanghaisheng.com.xiaoya.datasource.MovieData;
import wanghaisheng.com.xiaoya.db.Movie;
import wanghaisheng.com.xiaoya.db.MovieDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/4/18.
 */
public class MovieListPresenter extends BaseListPresenter<MovieList,MoveListView>{
    //每页显示的数目
    public static final int LIMIT = 20;
    //查询API时的offset
    //private int offset = 0;

    @Inject
    MovieApi movieApi;
    @Inject
    MovieDao movieDao;
    @Inject
    MovieData movieData;

    @Inject @Singleton
    public MovieListPresenter() {}

    /**
     * 第一次加载数据时调用，三级缓存：memory,disk,network
     */
    public void firstLoadData() {
        iView.showLoading();
        LogUtils.d(movieData.getDataSourceText());
        subscription = movieData.subscribeData().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Movie>>() {
                    @Override
                    public void call(List<Movie> movies) {
                        iView.hideLoading();
                        iView.renderFirstLoadData(movies);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        iView.hideLoading();
                        if(throwable instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }
                });

    }


    /**
     * 加载最新数据
     */
    public void loadNewestData() {
        subscription = movieData.network()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Movie>>() {
                    @Override
                    public void call(List<Movie> datas) {
//                        LogUtils.d("loadNewestData........onNext..........");
                        iView.refreshComplete(datas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        if (throwable instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }
                });
    }


    /**
     * 加载更多
     * @param offset
     */
    public void loadMoreData(int offset) {
        subscription = movieApi.getMovieByType(offset,LIMIT)
                .compose(SchedulersCompat.<MovieList>applyIoSchedulers())
                .subscribe(new Action1<MovieList>() {
                    @Override
                    public void call(MovieList movieList) {
                        iView.loadMoreComplete(movieList.getData().getMovies(),movieList.getData().isHasNext());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        if (throwable instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }
                });
    }



}

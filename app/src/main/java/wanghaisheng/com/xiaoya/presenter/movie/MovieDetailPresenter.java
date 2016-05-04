package wanghaisheng.com.xiaoya.presenter.movie;

import javax.inject.Inject;
import javax.inject.Singleton;

import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.db.Movie;
import wanghaisheng.com.xiaoya.db.MovieCollection;
import wanghaisheng.com.xiaoya.db.MovieCollectionDao;
import wanghaisheng.com.xiaoya.db.MovieDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseDetailPresenter;

/**
 * Created by sheng on 2016/4/18.
 */
public class MovieDetailPresenter extends BaseDetailPresenter<Movie,MovieDetailView> {
    @Inject
    MovieDao movieDao;
    @Inject
    MovieCollectionDao movieCollectionDao;
    @Inject
    MovieApi movieApi;

    @Inject @Singleton
    public MovieDetailPresenter(){}

    @Override
    public void loadEntityDetail(int entityId) {

    }

    public String getMovieDetailUrl(String movieId) {
        return movieApi.getMovieDetailUrl(movieId);
    }

    @Override
    public void collectEntity(Movie movie) {
        movie.setIs_collected(true);
        movieDao.insertOrReplace(movie);
        MovieCollection movieCollection = convertMovieToMovieCollection(movie);
        movieCollectionDao.insertOrReplace(movieCollection);

        iView.collectSuccess();
    }

    @Override
    public void detachView() {
        if(null != iView) {
            iView = null;
        }
    }

    private MovieCollection convertMovieToMovieCollection(Movie movie) {
        MovieCollection movieCollection = new MovieCollection();
        movieCollection.setId(movie.getId());
        movieCollection.setShowInfo(movie.getShowInfo());
        movieCollection.setScm(movie.getScm());
        movieCollection.setDir(movie.getDir());
        movieCollection.setStar(movie.getStar());
        movieCollection.setStar(movie.getCat());
        movieCollection.setWish(movie.getWish());
        movieCollection.setValue3d(movie.getValue3d());
        movieCollection.setNm(movie.getNm());
        movieCollection.setImax(movie.getImax());
        movieCollection.setSnum(movie.getSnum());
        movieCollection.setSc(movie.getSc());
        movieCollection.setVer(movie.getVer());
        movieCollection.setRt(movie.getRt());
        movieCollection.setImg(movie.getImg());
        movieCollection.setDur(movie.getDur());

        return movieCollection;

    }
}

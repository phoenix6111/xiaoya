package wanghaisheng.com.xiaoya.api.movie;

import rx.Observable;
import wanghaisheng.com.xiaoya.api.BaseApi;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.beans.MovieList;

/**
 * Created by sheng on 2016/4/18.
 */
public class MovieApi extends BaseApi{
//    public static final String BASE_URL = "http://m.maoyan.com/movie/list.json?type=hot&offset=0&limit=1000";
    public static final String BASE_URL = "http://m.maoyan.com/movie/";
    public static final String Base_Detail_URL = "http://m.maoyan.com/movie/%s?_v_=yes";
    public static final String[] TYPE_TAGS = {"hot"};
    public static final String[] TYPE_NAMES = {"热映"};
    public static final int LIMIT = 20;

    private MovieService movieService;

    public MovieApi() {
        super(BASE_URL);
        movieService = retrofit.create(MovieService.class);
    }

    public Observable<MovieList> getMovieByType(int offset, int limit) {
        return movieService.getMovieByType(TYPE_TAGS[0],offset,limit)
                .compose(SchedulersCompat.<MovieList>applyIoSchedulers());
    }

    public static String getMovieDetailUrl(String movieId) {
        return String.format(Base_Detail_URL,movieId);
    }
}

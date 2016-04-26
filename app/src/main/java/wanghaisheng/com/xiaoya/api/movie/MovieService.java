package wanghaisheng.com.xiaoya.api.movie;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import wanghaisheng.com.xiaoya.beans.MovieList;

/**
 * Created by sheng on 2016/4/18.
 */
public interface MovieService {

    @GET("list.json")
    Observable<MovieList> getMovieByType(@Query("type") String type, @Query("offset") int offset, @Query("limit") int limit);
}

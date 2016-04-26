package wanghaisheng.com.xiaoya.api.guokr;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import wanghaisheng.com.xiaoya.beans.Science;

/**
 * Created by sheng on 2016/4/16.
 */
public interface ScienceService{

    @GET("article.json")
    Observable<Science> getArticleByChannel(@Query("retrieve_type") String retrieveType, @Query("channel_key") String channelKey, @Query("offset") int offset, @Query("limit") int limit);


}

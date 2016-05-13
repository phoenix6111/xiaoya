package wanghaisheng.com.xiaoya.api.weixingjingxuan;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sheng on 2016/5/13.
 */
public interface WeiArticleService {

    @GET("query")
    Observable<WeiArticleWrapper> getWeiarticleWrapper(@Query("pno") int pageNum,@Query("ps") int pageSize,@Query("key") String key);
}

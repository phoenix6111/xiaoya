package wanghaisheng.com.xiaoya.api.gaoxiao;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import wanghaisheng.com.xiaoya.beans.GaoxiaoPicResult;

/**
 * Created by sheng on 2016/5/13.
 */
public interface GaoxiaoService {

    @GET("getAllRecomPicByTag.jsp")
    Observable<GaoxiaoPicResult> getGaoxiaoPicResult(@Query("category") String cagtegory,@Query("tag") String tag,@Query("start") long start,@Query("len") int len);
}

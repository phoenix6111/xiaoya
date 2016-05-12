package wanghaisheng.com.xiaoya.api.jianshu;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sheng on 2016/5/10.
 */
public interface JianshuService {
    //获取首页的数据
    @GET("trending/now")
    Observable<JianshuContentResult> getIndexPageData();

    //获取首页中下一页的数据，此url要从上一次解析的数据中获得
    @GET("{nextPageUrl}")
    Observable<JianshuContentResult> getIndexNextPageData(@Path("nextPageUrl") String nextPageUrl);

    @GET("{detailUrl}")
    Observable<String> getDetailContent(@Path("detailUrl") String detailUrl);
}

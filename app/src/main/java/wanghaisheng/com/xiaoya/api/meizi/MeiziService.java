package wanghaisheng.com.xiaoya.api.meizi;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;
import wanghaisheng.com.xiaoya.beans.GroupResult;
import wanghaisheng.com.xiaoya.db.Group;

/**
 * Created by sheng on 2016/5/6.
 */
public interface MeiziService {

    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36" +
            "(KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36")
    @GET("{type}/page/{page}")
    Observable<List<Group>> getGroup(@Path("type") String type, @Path("page") int page);

    /*@Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36" +
            "(KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36")
    @GET("{groupid}/{index}")
    Observable<List<Content>> getContent(@Path("groupid") String groupid, @Path("index") int index);*/

    @GET("{groupid}")
    Observable<GroupResult> getContentResult(@Path("groupid") String groupid);

}

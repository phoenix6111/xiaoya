package wanghaisheng.com.xiaoya.api.meitu;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sheng on 2016/5/11.
 */
public interface MeituService {

    /**
     * 根据gallery id 获得全部图片数据
     * @param id
     * @return
     */
    @GET("zj")
    Observable<MeituGalleryResult> getMeituGallery(@Query("ch") String channel,@Query("t1") String tag,@Query("listtype") String listtype,@Query("sn") int nextIndex);

    @GET("zvj")
    Observable<MeituPictureResult> getMeituPicture(@Query("id") String galleryId);
}

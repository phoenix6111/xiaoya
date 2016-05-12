package wanghaisheng.com.xiaoya.api.meitu;

import okhttp3.OkHttpClient;
import rx.Observable;
import wanghaisheng.com.xiaoya.api.BaseApi;
import wanghaisheng.com.xiaoya.utils.ArrayUtils;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituApi extends BaseApi {
    public static final String BASE_URL = "http://image.so.com/";//  http://image.so.com/zj?ch=beauty&t1=595&sn=0&listtype=hot
    //   http://image.so.com/zvj?ch=beauty&t1=595&id=cee8c6970b40fc86f0401f21ff4debeb

    public static final String IMAGE_PREFIX = "http://tnfs.tngou.net/image";

    public static final String[] TYPE_ID = {"new","hot"};
    public static final String[] TYPE_NAME = {"最新","最热"};

    public static final String[] CHANNEL_ID = {"beauty","news","funny","video"};
    public static final String[] CHANNEL_NAME = {"美女","图说世界","搞笑","图解电影"};

    public static final String[] TOTAL_TAG = {"595","625","600","602","603","596","601","598","604","1993","1983","1991","1989","1984","1990","1992"};

    public static final String[] BEAUTY_TAG_ID = {"595","625","600","602","603","596","601","598","604"};
    public static final String[] BEAUTY_TAG_NAME = {"萌女","粉嫩","车模","时装秀","街拍","婚纱","showgirl","cosplay","主播秀"};
    public static final String[] FUNNY_TAG_ID = {"1993","1983","1991","1989","1984","1990","1992"};
    public static final String[] FUNNY_TAG_NAME = {"热门","gif","奇趣","恶搞","神吐槽","内涵","萌宠"};

    public static final int LIMIT = 20;

    private MeituService meituService;

    public MeituApi(OkHttpClient client) {
        super(BASE_URL,client);
        meituService = retrofit.create(MeituService.class);
    }

    public String[] getTotalTags() {
        return ArrayUtils.concat(BEAUTY_TAG_ID,FUNNY_TAG_ID);
    }

    /**
     * 根据类别和页码显示图片列表
     * @return
     */
    public Observable<MeituGalleryResult> getMeituGallery(String channel,String tag,String type, int nextIndex) {
        String nType;
        if(null == type) {
            nType = TYPE_ID[0];
        } else {
            nType = type;
        }

        String nChannel;
        if(null == channel) {
            nChannel = CHANNEL_ID[0];
        } else {
            nChannel = channel;
        }

        return meituService.getMeituGallery(channel,tag,type,nextIndex);
    }

    /**
     * 获取”美女“对的 data
     * @param tag
     * @param nextIndex
     * @return
     */
    public Observable<MeituGalleryResult> getBeautyMeituGallery(String tag,int nextIndex) {
        return getMeituGallery(CHANNEL_ID[0],tag,TYPE_ID[0],nextIndex);
    }

    public Observable<MeituGalleryResult> getFunnyMeituGallery(String tag,int nextIndex) {
        return getMeituGallery(CHANNEL_ID[2],tag,TYPE_ID[0],nextIndex);
    }

    /**
     * 根据Gallery对应的 id 获取对应的picture组合
     * @param id
     * @return
     */
    public Observable<MeituPictureResult> getMeituPicture(String id) {
        return meituService.getMeituPicture(id);
    }

    /**
     * 根据gallery id 获得全部图片数据
     * @param id
     * @return
     */
    /*public Observable<MeituGallery> getMeituGallery(@Query("id") String id) {
        return meituService.getMeituGallery(id);
    }*/
}

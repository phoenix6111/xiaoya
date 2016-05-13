package wanghaisheng.com.xiaoya.api.weixingjingxuan;

import okhttp3.OkHttpClient;
import rx.Observable;
import wanghaisheng.com.xiaoya.Constants;
import wanghaisheng.com.xiaoya.api.BaseApi;

/**
 * Created by sheng on 2016/5/13.
 */
public class WeiArticleApi extends BaseApi{
    public static final String BASE_URL = "http://v.juhe.cn/weixin/";

    public static final int LIMIT = 20;

    private WeiArticleService weiArticleService;

    public WeiArticleApi(OkHttpClient client) {
        super(BASE_URL,client);
        weiArticleService = retrofit.create(WeiArticleService.class);
    }

    public Observable<WeiArticleWrapper> getWeiArticleWrapper(int pageNum) {
        return weiArticleService.getWeiarticleWrapper(pageNum,LIMIT, Constants.JUHE_WEIXING_APP_KEY);
    }

}

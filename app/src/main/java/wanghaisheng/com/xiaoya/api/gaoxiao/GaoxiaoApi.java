package wanghaisheng.com.xiaoya.api.gaoxiao;

import okhttp3.OkHttpClient;
import rx.Observable;
import wanghaisheng.com.xiaoya.api.BaseApi;
import wanghaisheng.com.xiaoya.beans.GaoxiaoPicResult;

/**
 * Created by sheng on 2016/5/13.
 */
public class GaoxiaoApi extends BaseApi{

    public static final String BASE_URL = "http://pic.sogou.com/pics/channel/";
    public static final String CATEGORY = "搞笑";
    public static final int LIMIT = 15;
    public static final String[] TAGS = {"全部","搞笑人物","招牌标语","萌宠","恶搞","屌丝","错觉","雅蠛蝶"
            ,"情侣","奇葩","熊孩子","雷人","暴走漫画","山寨","找亮点","创意","有爱","邪恶",
            "PS","喵星人","萌货","卖萌","蛋疼","夫妻","碉堡了","汪星人","搞笑精品","艺术","没节操","损友","漫画"};

    private GaoxiaoService gaoxiaoService;

    public GaoxiaoApi(OkHttpClient client) {
        super(BASE_URL,client);
        gaoxiaoService = retrofit.create(GaoxiaoService.class);
    }

    /**
     * 获取 GaoxiaoPicResult
     * @param tag 标签
     * @param start 开始索引
     * @return
     */
    public Observable<GaoxiaoPicResult> getGaoxiaoPicResult(String tag,int start) {
        return gaoxiaoService.getGaoxiaoPicResult(CATEGORY,tag,start,LIMIT);
    }
}

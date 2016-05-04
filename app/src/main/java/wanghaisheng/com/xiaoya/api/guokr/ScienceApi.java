package wanghaisheng.com.xiaoya.api.guokr;

import rx.Observable;
import wanghaisheng.com.xiaoya.api.BaseApi;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.beans.Science;

/**
 * Created by sheng on 2016/4/16.
 */
public class ScienceApi extends BaseApi{
    public static final String BASE_URL = "http://www.guokr.com/apis/minisite/";
    public static final String[] CHANNEL_TAG = {"hot","frontier","review","interview","visual","brief","fact","techb"};
    public static final String[] CHANNEL_TITLE = {"热点","前沿","评论","专访","视觉","速读","谣言粉碎机","商业科技"};


    private ScienceService scienceService;

    public ScienceApi() {
        super(BASE_URL);
        scienceService = retrofit.create(ScienceService.class);
    }

    /**
     * 根据channelKey获取 Science数据
     * @param channelKey
     * @param offset 分页的offset
     * @param limit 每页显示的数目
     * @return
     */
    public Observable<Science> getScienceByChannel(String channelKey,int offset,int limit) {
        return scienceService.getArticleByChannel("by_channel",channelKey,offset,limit)
                    .compose(SchedulersCompat.<Science>applyIoSchedulers());
    }


}

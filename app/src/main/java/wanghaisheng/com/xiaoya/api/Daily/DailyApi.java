package wanghaisheng.com.xiaoya.api.Daily;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.BaseApi;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.beans.Daily;
import wanghaisheng.com.xiaoya.beans.Story;

/**
 * Created by sheng on 2016/4/12.
 * 基类API
 */

public class DailyApi extends BaseApi {
    private static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    public static final int[] THEME_ID = {1,11,4,5,10};
    public static final String[] THEME_NAME = {
            "最新推荐","不许无聊",
            "设计日报","大公司日报","互联网安全"
    };
//    public static final String THEME_DAILY_BASE_URL = "http://news-at.zhihu.com/api/4/theme/:id";
//    public static final String THEME_DAILY_BASE_URL = "http://news-at.zhihu.com/api/4/";
    private DailyService dailyService;

    @Inject
    public DailyApi() {
        super(BASE_URL);
        dailyService = retrofit.create(DailyService.class);
    }

    /**
     * 根据query date返回不同的Observable对象
     * @param datetime
     * @return
     */
    public Observable<Daily> getDaily(final int datetime) {
        Observable<Daily> observable = datetime > 0 ? dailyService.getBefore(datetime) : dailyService.getLatest();
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        /**
         * 为了避免重复编写以下代码，用了compose
         * observable.subscribeOn(Schedulers.io())
         .  observeOn(AndroidSchedulers.mainThread());
         */
        //return observable.compose(SchedulersCompat.<Daily>applyIoSchedulers());
    }

    public Observable<Story> getStory(final int story_id) {
        return dailyService.getStory(story_id)
            .compose(SchedulersCompat.<Story>applyIoSchedulers());
    }

    /**
     * 根据theme查询story
     * @param themeId
     * @return
     */
    public Observable<Daily> getDailyByTheme(int themeId) {
        return dailyService.getDailyByTheme(themeId)
                .compose(SchedulersCompat.<Daily>applyIoSchedulers());
    }






}

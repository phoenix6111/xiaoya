package wanghaisheng.com.xiaoya.api.Daily;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import wanghaisheng.com.xiaoya.beans.Daily;
import wanghaisheng.com.xiaoya.beans.Story;

/**
 * Created by sheng on 2016/4/12.
 */
public interface DailyService {

    @GET("news/latest")
    Observable<Daily> getLatest();

    @GET("stories/before/{datetime}?client=0")
    Observable<Daily> getBefore(@Path("datetime") int datetime);

    @GET("story/{story_id}")
    Observable<Story> getStory(@Path("story_id") int storyId);

    @GET("theme/{theme_id}")
    Observable<Daily> getDailyByTheme(@Path("theme_id") int themeId);
}

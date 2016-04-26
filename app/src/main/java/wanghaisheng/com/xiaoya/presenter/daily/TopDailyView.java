package wanghaisheng.com.xiaoya.presenter.daily;

import java.util.List;

import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.presenter.base.BaseView;

/**
 * Created by sheng on 2016/4/14.
 */
public interface TopDailyView extends BaseView {
    public void renderStories(List<Story> stories);
}

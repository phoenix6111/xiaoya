package wanghaisheng.com.xiaoya.presenter.daily;

import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.presenter.base.BaseDetailView;

/**
 * Created by sheng on 2016/4/15.
 */
public interface StoryDetailView extends BaseDetailView{
    //从服务器取到story的详细数据之后

    void renderEntityView(Story story);

    void renderWebview(String webPageStr);

}

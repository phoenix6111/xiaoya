package wanghaisheng.com.xiaoya.presenter.daily;

import wanghaisheng.com.xiaoya.beans.Daily;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/4/14.
 */
public interface StoryListView extends BaseListView{
    void renderFirstLoadData(Daily datas);
    void refreshComplete(Daily datas);
    void loadMoreComplete(Daily datas);
}

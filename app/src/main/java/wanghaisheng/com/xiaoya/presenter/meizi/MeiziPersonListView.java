package wanghaisheng.com.xiaoya.presenter.meizi;

import java.util.List;

import wanghaisheng.com.xiaoya.db.Content;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/7.
 */
public interface MeiziPersonListView extends BaseListView {
    void renderNetworkData(Content content);
    void renderCacheData(List<Content> datas);
    void refreshComplete(List<Content> datas);
//    void loadMoreComplete(List<Content> datas);
}

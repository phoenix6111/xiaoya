package wanghaisheng.com.xiaoya.presenter.meizi;

import java.util.List;

import wanghaisheng.com.xiaoya.db.Group;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/7.
 */
public interface MeiziHomeListView extends BaseListView{
    void renderFirstLoadData(List<Group> datas);
    void refreshComplete(List<Group> datas);
    void loadMoreComplete(List<Group> datas);
}

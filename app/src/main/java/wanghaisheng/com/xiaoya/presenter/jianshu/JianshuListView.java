package wanghaisheng.com.xiaoya.presenter.jianshu;

import wanghaisheng.com.xiaoya.api.jianshu.JianshuContentResult;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/10.
 */
public interface JianshuListView extends BaseListView {
    void renderFirstLoadData(JianshuContentResult datas);
    void refreshComplete(JianshuContentResult datas);
    void loadMoreComplete(JianshuContentResult datas);

}

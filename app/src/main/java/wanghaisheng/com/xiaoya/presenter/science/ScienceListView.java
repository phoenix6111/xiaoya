package wanghaisheng.com.xiaoya.presenter.science;

import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/4/16.
 */
public interface ScienceListView extends BaseListView<Article>{

    void setExtraData(int offset);
}

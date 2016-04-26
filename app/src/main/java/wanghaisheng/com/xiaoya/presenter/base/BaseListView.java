package wanghaisheng.com.xiaoya.presenter.base;

import java.util.List;

/**
 * Created by sheng on 2016/4/15.
 */
public interface BaseListView<T> extends BaseView{
    public void renderNetData(List<T> datas);
    public void renderDbData(List<T> datas);
    public void refreshComplete(List<T> datas);
    public void loadMoreComplete(List<T> datas);

    public void hideLoading();

    public void showLoading();

    public void error(String error);
}

package wanghaisheng.com.xiaoya.presenter.collection;

import java.util.List;

import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.presenter.base.BaseView;

/**
 * Created by sheng on 2016/4/20.
 */
public interface DailyCollectionView extends BaseView{

    public void renderStories(int page, List<Story> stories);

    public void hideLoading();

    public void showLoading();

    public void error(String err);
}

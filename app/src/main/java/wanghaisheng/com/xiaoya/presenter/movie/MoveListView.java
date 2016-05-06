package wanghaisheng.com.xiaoya.presenter.movie;

import java.util.List;

import wanghaisheng.com.xiaoya.db.Movie;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/4/18.
 */
public interface MoveListView extends BaseListView {
    void renderFirstLoadData(List<Movie> datas);
    void refreshComplete(List<Movie> datas);
    void loadMoreComplete(List<Movie> datas,boolean hasMore);
}

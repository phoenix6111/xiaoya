package wanghaisheng.com.xiaoya.presenter.meitu;

import java.util.List;

import wanghaisheng.com.xiaoya.db.MeituPicture;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/12.
 */
public interface MeituPersonListView extends BaseListView {
    void renderData(List<MeituPicture> datas);
    void refreshdata(List<MeituPicture> datas);
//    void loadMoreComplete(List<Content> datas);
}

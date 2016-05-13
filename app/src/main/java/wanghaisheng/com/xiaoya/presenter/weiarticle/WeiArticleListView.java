package wanghaisheng.com.xiaoya.presenter.weiarticle;

import java.util.List;

import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticle;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/13.
 */
public interface WeiArticleListView extends BaseListView {
    void renderFirstLoadData(List<WeiArticle> datas);
    void refreshComplete(List<WeiArticle> datas);
    void loadMoreComplete(List<WeiArticle> datas);
}

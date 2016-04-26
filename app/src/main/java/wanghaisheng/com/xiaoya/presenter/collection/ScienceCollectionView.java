package wanghaisheng.com.xiaoya.presenter.collection;

import java.util.List;

import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.presenter.base.BaseView;

/**
 * Created by sheng on 2016/4/20.
 */
public interface ScienceCollectionView extends BaseView {
    public void renderArticles(int page, List<Article> articles);

    public void hideLoading();

    public void showLoading();

    public void error(String error);
}

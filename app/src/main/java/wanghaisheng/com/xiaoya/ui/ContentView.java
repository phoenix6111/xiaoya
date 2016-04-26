package wanghaisheng.com.xiaoya.ui;

import java.util.List;

import wanghaisheng.com.xiaoya.presenter.base.BaseView;

public interface ContentView extends BaseView {

    void showLoading();

    void hideLoading();

    void renderContent(String url, List<String> urls);

    void renderShare(String share, String url);

    void isCollected(boolean isCollected);

    void onError(String error);

}

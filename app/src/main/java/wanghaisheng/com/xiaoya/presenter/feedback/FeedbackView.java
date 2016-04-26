package wanghaisheng.com.xiaoya.presenter.feedback;

import wanghaisheng.com.xiaoya.presenter.base.BaseView;

/**
 * Created by sheng on 2016/4/22.
 */
public interface FeedbackView extends BaseView {
    void hideLoading();

    void showLoading();

    void postSuccess();

    void postFailure(String msg);
}

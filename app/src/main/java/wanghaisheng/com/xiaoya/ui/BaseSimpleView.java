package wanghaisheng.com.xiaoya.ui;

/**
 * Created by sheng on 2016/5/7.
 */
public interface BaseSimpleView {
    void showLoading();

    void hideLoading();

    void error(int errorType,String errMsg);

}

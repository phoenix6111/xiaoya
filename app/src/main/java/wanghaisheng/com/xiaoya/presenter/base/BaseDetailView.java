package wanghaisheng.com.xiaoya.presenter.base;

/**
 * Created by sheng on 2016/4/16.
 */
public interface BaseDetailView<T> extends BaseView{
    //从服务器取到story的详细数据之后
    //public void renderEntityView(T t) ;

    void collectSuccess();

    void uncollectSuccess();

    void showLoading();

    void hideLoading();

    void onError(String error);

    void updateCollectionFlag(boolean ifCollected);

}

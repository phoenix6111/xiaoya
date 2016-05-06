package wanghaisheng.com.xiaoya.presenter.base;

/**
 * Created by sheng on 2016/4/16.
 */
public interface BaseDetailView<T> extends BaseView{
    //从服务器取到story的详细数据之后
    //public void renderEntityView(T t) ;

    int ERROR_TYPE_NETWORK = 1;
    int ERROR_TYPE_NODATA = 2;
    int ERROR_TYPE_NODATA_ENABLE_CLICK = 3;
    int ERROR_TYPE_UNKNOWN = 4;

    void collectSuccess();

    void uncollectSuccess();

    void showLoading();

    void hideLoading();

    void error(int errorType,String errMsg);

    void updateCollectionFlag(boolean ifCollected);

}

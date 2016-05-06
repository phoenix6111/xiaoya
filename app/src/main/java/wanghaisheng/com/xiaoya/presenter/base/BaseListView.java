package wanghaisheng.com.xiaoya.presenter.base;

/**
 * Created by sheng on 2016/4/15.
 */
public interface BaseListView extends BaseView{
    public static final int ERROR_TYPE_NETWORK = 1;
    public static final int ERROR_TYPE_NODATA = 2;
    public static final int ERROR_TYPE_NODATA_ENABLE_CLICK = 3;
    public static final int ERROR_TYPE_UNKNOWN = 4;

    /*
    public void renderFirstLoadData(List<T> datas);
    public void refreshComplete(List<T> datas);
    public void loadMoreComplete(List<T> datas);
*/
    public void hideLoading();

    public void showLoading();

    public void error(int errorType,String errMsg);
}

package wanghaisheng.com.xiaoya.presenter;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import rx.functions.Action1;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;
import wanghaisheng.com.xiaoya.presenter.base.BaseView;

/**
 * Created by sheng on 2016/5/7.
 * 错误处理类，
 */
public class ErrorHandlerAction implements Action1<Throwable> {
    private BaseView mBaseView;
    public ErrorHandlerAction(BaseView baseView) {
        this.mBaseView = baseView;
    }

    @Override
    public void call(Throwable throwable) {
        LogUtils.d(throwable);
        if(null != mBaseView) {
            if (throwable instanceof NetworkErrorException) {
                mBaseView.error(BaseListView.ERROR_TYPE_NETWORK,null);
            } else {
                mBaseView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
            }
        }
    }
}

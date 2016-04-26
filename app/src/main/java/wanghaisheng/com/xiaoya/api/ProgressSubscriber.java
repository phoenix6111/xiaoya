package wanghaisheng.com.xiaoya.api;

import android.content.Context;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by sheng on 2016/4/10.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements OnProgressCancelListener {
    private static final String TAG = "ProgressSubscriber";
    //http请求状态处理类
    private ProgressDialogHandler progressHandler;

    //activity处理数据接口
    private SubscriberOnNextListener onNextListener;
    private Context context;
    public ProgressSubscriber(Context context) {
        this.context = context;
//        this.onNextListener = onNextListener;

        this.progressHandler = new ProgressDialogHandler(context,this,true);
    }

    private void showProgressDialog() {
        if(null != progressHandler) {
            progressHandler.obtainMessage(ProgressDialogHandler.SHOW_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if(null != progressHandler) {
            progressHandler.obtainMessage(ProgressDialogHandler.DISMISS_DIALOG).sendToTarget();
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
        //Toast.makeText(context, "Start Get Top Movie", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
        //Toast.makeText(context, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            LogUtils.d(e.getMessage());
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onCancel() {
        //解除订阅
        if(this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}

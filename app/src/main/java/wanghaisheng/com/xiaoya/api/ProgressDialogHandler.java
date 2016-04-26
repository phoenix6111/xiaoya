package wanghaisheng.com.xiaoya.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * Created by sheng on 2016/4/10.
 */
public class ProgressDialogHandler extends Handler {
    //handler的what值，供外部调用
    public static final int SHOW_DIALOG = 100;
    public static final int DISMISS_DIALOG = 101;
    private ProgressDialog progressDialog;
    //dialog是否可cancel
    private boolean cancelable;

    private OnProgressCancelListener onProgressCancelListener;
    private Context context;

    public ProgressDialogHandler(Context context,OnProgressCancelListener listener,boolean cancelable) {
        super();
        this.context = context;
        this.onProgressCancelListener = listener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog() {
        if(null == progressDialog) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(cancelable);

            if(cancelable) {
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        onProgressCancelListener.onCancel();
                    }
                });
            }

            if(!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if(null != progressDialog) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}

package wanghaisheng.com.xiaoya.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.apkfuns.logutils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.AppContext;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.di.component.DaggerServiceComponent;
import wanghaisheng.com.xiaoya.di.module.ServiceModule;
import wanghaisheng.com.xiaoya.utils.FileHelper;
import wanghaisheng.com.xiaoya.utils.ToastUtil;

/**
 * Created by sheng on 2016/5/9.
 */
public class SaveAllImageService extends IntentService {
    private static final String TAG = "SaveAllImageService";

    public static final String ARG_GROUPID = "group_id";
    public static final String ARG_URLS = "arg_urls";
    public static final String ARG_TITLE = "arg_title";

    private String title;
    private String groupId;
    private ArrayList<String> urls;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;

    @Inject
    FileHelper fileHelper;
    @Inject
    ToastUtil toastUtil;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.d("服务初始化。。。");

        DaggerServiceComponent.builder().appComponent(((AppContext)getApplication()).getAppComponent())
                                    .serviceModule(new ServiceModule(this))
                                    .build()
                                    .inject(this);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public SaveAllImageService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initDatas(intent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(R.string.msg_download_image))
                .setContentText(getString(R.string.msg_download_progress))
                .setSmallIcon(R.drawable.download);
        for(int i=0; i<urls.size(); i++) {
            String url = urls.get(i);
            try {
                Bitmap bitmap = Picasso.with(getApplicationContext()).load(url).get();
                fileHelper.saveimages(bitmap,groupId,groupId+(i+1));
            } catch (Exception e) {
                LogUtils.d(e);
                toastUtil.showToast("保存图片失败"+i);
            }
            mBuilder.setProgress(urls.size(),i+1,false);
            notificationManager.notify(1,mBuilder.build());
        }

        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                toastUtil.showToast(String.format(getString(R.string.msg_download_success),title));
            }
        });

    }

    private void initDatas(Intent intent) {
        LogUtils.d("print initdatas............................................");
        urls = intent.getStringArrayListExtra(ARG_URLS);
        title = intent.getStringExtra(ARG_TITLE);
        LogUtils.d("title............"+title);
        groupId = intent.getStringExtra(ARG_GROUPID);
    }
}

package wanghaisheng.com.xiaoya.presenter.meizi;

import android.net.Uri;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.presenter.base.Presenter;
import wanghaisheng.com.xiaoya.utils.FileHelper;

/**
 * Created by sheng on 2016/5/9.
 */
public class MeiziLargePicPresenter extends Presenter<MeiziLargePicView> {
    @Inject
    FileHelper fileHelper;


    @Inject
    public MeiziLargePicPresenter() {}

    /**
     * 保存图片进本地
     * @param imgUrl
     */
    public void saveLargePic(final String imgUrl, final String name) {

        Subscription subs = fileHelper.saveImagesAndGetPathObservable(imgUrl, "meizi",name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Uri>() {
                @Override
                public void call(Uri uri) {
                    if(null != iView) {
                        iView.onImageSaved(true,uri.getPath());
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    LogUtils.d(throwable);
                    if(null != iView) {
                        iView.onImageSaved(false,"图片保存失败");
                    }
                }
            });

        compositeSubscription.add(subs);
    }
}

package wanghaisheng.com.xiaoya.presenter.gaoxiao;

import android.accounts.NetworkErrorException;
import android.net.Uri;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.api.gaoxiao.GaoxiaoApi;
import wanghaisheng.com.xiaoya.beans.GaoxiaoPicResult;
import wanghaisheng.com.xiaoya.datasource.GaoxiaoData;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;
import wanghaisheng.com.xiaoya.utils.FileHelper;

/**
 * Created by sheng on 2016/5/13.
 */
public class GaoxiaoHomeListPresenter extends BaseListPresenter<GaoxiaoHomeListView> {

    @Inject
    GaoxiaoApi gaoxiaoApi;
    @Inject
    GaoxiaoData gaoxiaoData;
    @Inject
    FileHelper fileHelper;

    @Inject
    public GaoxiaoHomeListPresenter() {}

    /**
     * 保存图片进本地
     * @param imgUrl
     */
    public void saveLargePic(final String imgUrl, final String name) {

        Subscription subs = fileHelper.saveImagesAndGetPathObservable(imgUrl, "meitu",name)
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

    /**
     * 第一次加载数据，按照 memory->disk->network的顺序查找
     * @param tag
     */
    public void firstLoadData(String tag) {
        Subscription subscription = gaoxiaoData.subscribeBeautyData(tag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GaoxiaoPicResult>() {
                    @Override
                    public void call(GaoxiaoPicResult datas) {
                        if(null != iView) {
//                            LogUtils.d(jianshuContentResult.getContents());
                            iView.hideLoading();
                            iView.renderFirstLoadData(datas);
//                            LogUtils.d(jianshuContentResult);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        if(null != iView) {
                            iView.hideLoading();
                            if(throwable instanceof NetworkErrorException) {
                                iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                            } else {
                                iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                            }
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    /**
     * 根据tag刷新数据
     * @param tag
     */
    public void loadNewestData(String tag) {
        Subscription subscription = gaoxiaoData.network(tag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GaoxiaoPicResult>() {
                    @Override
                    public void call(GaoxiaoPicResult datas) {
//                        LogUtils.d("loadNewestData........onNext..........");
                        if(null != iView) {
                            iView.refreshComplete(datas);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        if(null != iView) {
                            if (throwable instanceof NetworkErrorException) {
                                iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                            } else {
                                iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                            }
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    /**
     * 加载更多
     * @param tag
     * @param nextIndex
     */
    public void loadMoreData(String tag,int nextIndex) {
//        LogUtils.d("channel..........."+channel+"         page"+page);
        Subscription subscription = gaoxiaoApi.getGaoxiaoPicResult(tag,nextIndex)
                .compose(SchedulersCompat.<GaoxiaoPicResult>applyIoSchedulers())
                .subscribe(new Action1<GaoxiaoPicResult>() {
                    @Override
                    public void call(GaoxiaoPicResult datas) {
                        if (null != iView) {
                            iView.loadMoreComplete(datas);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(null != iView) {
                            if (throwable instanceof NetworkErrorException) {
                                iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                            } else {
                                iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                            }
                        }
                    }
                });
        compositeSubscription.add(subscription);

    }

}

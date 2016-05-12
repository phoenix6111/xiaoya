package wanghaisheng.com.xiaoya.presenter.meitu;

import android.accounts.NetworkErrorException;
import android.net.Uri;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.api.meitu.MeituApi;
import wanghaisheng.com.xiaoya.api.meitu.MeituGalleryResult;
import wanghaisheng.com.xiaoya.datasource.MeituHomeData;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;
import wanghaisheng.com.xiaoya.utils.FileHelper;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituHomeListPresenter extends BaseListPresenter<MeituHomeListView> {
    @Inject
    MeituApi meituApi;
    @Inject
    MeituHomeData meituData;
    @Inject
    FileHelper fileHelper;

    @Inject
    public MeituHomeListPresenter() {}

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
     * @param channel
     */
    public void firstLoadData(String channel,String tag) {
        Subscription subscription = meituData.subscribeBeautyData(channel,tag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MeituGalleryResult>() {
                    @Override
                    public void call(MeituGalleryResult datas) {
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
     * 根据channel刷新数据
     * @param channel
     */
    public void loadNewestData(String channel,String tag) {
        Subscription subscription = meituData.network(channel,tag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MeituGalleryResult>() {
                    @Override
                    public void call(MeituGalleryResult datas) {
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
     * @param channel
     * @param tag
     */
    public void loadMoreData(String channel,String tag,int nextIndex) {
//        LogUtils.d("channel..........."+channel+"         page"+page);
        if(MeituApi.CHANNEL_ID[0].equals(channel)) {
            Subscription subscription = meituApi.getBeautyMeituGallery(tag,nextIndex)
                    .compose(SchedulersCompat.<MeituGalleryResult>applyIoSchedulers())
                    .subscribe(new Action1<MeituGalleryResult>() {
                        @Override
                        public void call(MeituGalleryResult datas) {
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
        } else {
            Subscription subscription = meituApi.getFunnyMeituGallery(tag,nextIndex)
                    .compose(SchedulersCompat.<MeituGalleryResult>applyIoSchedulers())
                    .subscribe(new Action1<MeituGalleryResult>() {
                        @Override
                        public void call(MeituGalleryResult datas) {
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

}

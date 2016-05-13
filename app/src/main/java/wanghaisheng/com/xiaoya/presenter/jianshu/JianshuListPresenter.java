package wanghaisheng.com.xiaoya.presenter.jianshu;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuApi;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuContentResult;
import wanghaisheng.com.xiaoya.datasource.JianshuData;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuListPresenter extends BaseListPresenter<JianshuListView> {
    @Inject
    JianshuApi jianshuApi;
    @Inject
    JianshuData jianshuData;

    @Inject
    public JianshuListPresenter(){}

    /**
     * 第一次加载数据，按照 memory->disk->network的顺序查找
     * @param channel
     */
    public void firstLoadData(String channel) {
        LogUtils.d("jianshu list presenter "+jianshuData.getDataSourceText());
        Subscription subscription = jianshuData.subscribeData(channel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JianshuContentResult>() {
                    @Override
                    public void call(JianshuContentResult jianshuContentResult) {
                        if(null != iView) {
//                            LogUtils.d(jianshuContentResult.getContents());
                            iView.hideLoading();
                            iView.renderFirstLoadData(jianshuContentResult);
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
    public void loadNewestData(String channel) {
        Subscription subscription = jianshuData.network(channel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JianshuContentResult>() {
                    @Override
                    public void call(JianshuContentResult datas) {
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
     * @param url 下一页的url
     */
    public void loadMoreData(String url) {
//        LogUtils.d("channel..........."+channel+"         page"+page);
        Subscription subscription = jianshuApi.getNextPageContentResult(url)
                .compose(SchedulersCompat.<JianshuContentResult>applyIoSchedulers())
                .subscribe(new Action1<JianshuContentResult>() {
                    @Override
                    public void call(JianshuContentResult datas) {
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

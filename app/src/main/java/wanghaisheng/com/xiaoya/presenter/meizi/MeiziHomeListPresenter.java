package wanghaisheng.com.xiaoya.presenter.meizi;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.datasource.MeiziHomeData;
import wanghaisheng.com.xiaoya.db.Group;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziHomeListPresenter extends BaseListPresenter<MeiziHomeListView> {

    @Inject
    MeiziHomeData meiziHomeData;
    @Inject
    MeiziApi meiziApi;

    @Inject
    public MeiziHomeListPresenter(){}

    public void firstLoadData(String channel) {
        iView.showLoading();

        Subscription subscription = meiziHomeData.subscribeData(channel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<Group>>() {
                @Override
                public void call(List<Group> groups) {
                    if(null != iView) {
                        iView.hideLoading();
                        iView.renderFirstLoadData(groups);
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
     * 加载最新数据
     */
    public void loadNewestData(String channel) {
        Subscription subscription = meiziHomeData.network(channel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Group>>() {
                    @Override
                    public void call(List<Group> datas) {
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
     * @param page
     */
    public void loadMoreData(String channel,int page) {
//        LogUtils.d("channel..........."+channel+"         page"+page);
        Subscription subscription = meiziApi.getGroup(channel,page)
                .compose(SchedulersCompat.<List<Group>>applyIoSchedulers())
                .subscribe(new Action1<List<Group>>() {
                    @Override
                    public void call(List<Group> datas) {
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

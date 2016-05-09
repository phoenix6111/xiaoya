package wanghaisheng.com.xiaoya.presenter.meizi;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.datasource.MeiziPersonData;
import wanghaisheng.com.xiaoya.db.Content;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziPersonListPresenter extends BaseListPresenter<Content,MeiziPersonListView> {
    @Inject
    MeiziPersonData meiziData;
    @Inject
    MeiziApi meiziApi;

    @Inject
    public MeiziPersonListPresenter(){}

    public void loadFromNet(final String groupId) {
//        iView.showLoading();
        LogUtils.d("meizipersonlistpresenter..firstloaddata...groupId"+groupId);
        final List<Content> contents = new ArrayList<>();
        Subscription subscription = meiziData.loadFromNetwork(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Content>() {
                               @Override
                               public void call(Content content) {
                                   if (null != iView) {
                                       iView.hideLoading();
                                       iView.renderNetworkData(content);
                                   }
                                   contents.add(content);
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
                           }
                        , new Action0() {
                            @Override
                            public void call() {
                                meiziData.saveToCache(contents, groupId);
                            }
                        });
        compositeSubscription.add(subscription);
    }

    public void loadFromCache(String groupId) {
//        iView.showLoading();
        Subscription subscription = meiziData.loadFromCache(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Content>>() {
                    @Override
                    public void call(List<Content> contents) {
                        if(null != iView) {
                            iView.renderCacheData(contents);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    /**
     * 检测是否存在缓存，如不存在缓存则从网络加载
     * @param groupId
     * @return
     */
    public boolean isCacheExist(String groupId) {
        return meiziData.isCacheExists(meiziData.getCacheKey(groupId));
    }

    /**
     * 加载最新数据
     *//*
    public void loadNewestData(String groupId) {
        subscription = meiziData.network(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Content>>() {
                    @Override
                    public void call(List<Content> datas) {
//                        LogUtils.d("loadNewestData........onNext..........");
                        iView.refreshComplete(datas);
                    }
                }, new ErrorHandlerAction(iView));
    }*/

}

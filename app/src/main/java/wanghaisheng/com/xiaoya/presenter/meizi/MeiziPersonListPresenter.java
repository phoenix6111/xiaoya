package wanghaisheng.com.xiaoya.presenter.meizi;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;

import com.apkfuns.logutils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Call;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.Exception.NoDataException;
import wanghaisheng.com.xiaoya.api.meizi.MeiziApi;
import wanghaisheng.com.xiaoya.datasource.MeiziPersonData;
import wanghaisheng.com.xiaoya.db.Content;
import wanghaisheng.com.xiaoya.db.ContentDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziPersonListPresenter extends BaseListPresenter<MeiziPersonListView> {
    @Inject
    MeiziPersonData meiziData;
    @Inject
    MeiziApi meiziApi;
    @Inject
    ContentDao contentDao;

    @Inject
    public MeiziPersonListPresenter(){}

    /**
     * 从网络加载
     * @param groupId
     */
    public void loadFromNet(final String groupId) {
        meiziApi.getContents(groupId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Content>() {
                    @Override
                    public void call(final Content content) {
                        LogUtils.d("load url.........."+content.getUrl());
                        OkHttpUtils.get().url(content.getUrl())
                                .build().execute(new BitmapCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                LogUtils.d(e);
                                if(null != iView) {
                                    if (e instanceof NetworkErrorException) {
                                        iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                                    } else {
                                        iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                                    }
                                }
                            }

                            @Override
                            public void onResponse(Bitmap bitmap) {
                                content.setImagewidth(bitmap.getWidth());
                                content.setImageheight(bitmap.getHeight());
                                if (null != iView) {
                                    iView.hideLoading();
                                    iView.renderNetworkData(content);
                                }
                                contentDao.insert(content);
                            }
                        });
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
    }

    public void loadFromCache(String groupId) {
//        iView.showLoading();
        Subscription subscription = meiziData.loadFromCache(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Content>>() {
                    @Override
                    public void call(List<Content> contents) {
                        if(null != iView) {
                            iView.hideLoading();
                            iView.renderCacheData(contents);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        if(null != iView) {
                            if(throwable instanceof NoDataException) {
                                iView.renderCacheData(new ArrayList<Content>());
                            } else if (throwable instanceof NetworkErrorException) {
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
        subscription = meiziHomeData.network(groupId)
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

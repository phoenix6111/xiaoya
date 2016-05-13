package wanghaisheng.com.xiaoya.presenter.weiarticle;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticle;
import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticleApi;
import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticleWrapper;
import wanghaisheng.com.xiaoya.datasource.WeiArticleData;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/13.
 */
public class WeiArticleListPresenter extends BaseListPresenter<WeiArticleListView> {
    @Inject
    WeiArticleApi weiArticleApi;
    @Inject
    WeiArticleData weiArticleData;


    @Inject
    public WeiArticleListPresenter() {}

    /**
     * 第一次加载数据时调用，三级缓存：memory,disk,network
     */
    public void firstLoadData() {
        iView.showLoading();
        LogUtils.d(weiArticleData.getDataSourceText());
        Subscription subscription = weiArticleData.subscribeData().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<WeiArticle>>() {
                    @Override
                    public void call(List<WeiArticle> movies) {
                        if(null != iView) {
                            iView.hideLoading();
                            iView.renderFirstLoadData(movies);
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
    public void loadNewestData() {
        Subscription subscription = weiArticleData.network()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<WeiArticle>>() {
                    @Override
                    public void call(List<WeiArticle> datas) {
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
     * @param pageNum 页码
     */
    public void loadMoreData(int pageNum) {
        Subscription subscription = weiArticleApi.getWeiArticleWrapper(pageNum)
                .compose(SchedulersCompat.<WeiArticleWrapper>applyIoSchedulers())
                .map(new Func1<WeiArticleWrapper, List<WeiArticle>>() {
                    @Override
                    public List<WeiArticle> call(WeiArticleWrapper wrapper) {
                        return wrapper.getResult().getList();
                    }
                })
                .subscribe(new Action1<List<WeiArticle>>() {
                    @Override
                    public void call(List<WeiArticle> datas) {
                        if(null != iView) {
                            iView.loadMoreComplete(datas);
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
}

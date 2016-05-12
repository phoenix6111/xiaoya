package wanghaisheng.com.xiaoya.presenter.meitu;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.datasource.MeituPersonData;
import wanghaisheng.com.xiaoya.db.MeituPicture;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/12.
 */
public class MeituPersonListPresenter extends BaseListPresenter<MeituPersonListView> {
    @Inject
    MeituPersonData meituPersonData;

    @Inject
    public MeituPersonListPresenter(){}

    /**
     * 从网络加载
     * @param groupId
     */
    public void firstLoadData(final String groupId) {

        Subscription subscription = meituPersonData.network(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MeituPicture>>() {
                    @Override
                    public void call(final List<MeituPicture> pictures) {
//                        LogUtils.d(pictures);
                        LogUtils.d(iView);
                        if(null != iView) {
//                            LogUtils.d("render iview...");
                            iView.hideLoading();
//                            LogUtils.d("meitu person list presenter");
//                            LogUtils.d(pictures);
                            iView.renderData(pictures);
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

    public void refreshData(final String groupId) {
        Subscription subscription = meituPersonData.network(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MeituPicture>>() {
                    @Override
                    public void call(final List<MeituPicture> pictures) {
                        if(null != iView) {
                            iView.refreshdata(pictures);
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

package wanghaisheng.com.xiaoya.presenter.base;

import android.support.annotation.NonNull;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sheng on 2016/4/14.
 */
public abstract class Presenter<T,V> {
    protected V iView;
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();//这个是持有订阅  用于生命周期
//    protected WeakReference<V>

    public void attachView(@NonNull V view) {
        this.iView = view;
    }

    public void detachView() {
        if(null != compositeSubscription && compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        if(null != iView) {
            iView = null;
        }
    }

}

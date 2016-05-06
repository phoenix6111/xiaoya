package wanghaisheng.com.xiaoya.presenter.base;

import rx.Subscription;

/**
 * Created by sheng on 2016/4/16.
 */
public abstract class BaseListPresenter<T,V> extends Presenter<T,V>{
    protected Subscription subscription;

    @Override
    public void detachView() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        if(null == iView) {
            iView = null;
        }
    }
}

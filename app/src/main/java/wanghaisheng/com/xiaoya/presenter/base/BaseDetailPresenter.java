package wanghaisheng.com.xiaoya.presenter.base;

import rx.Subscription;

/**
 * Created by sheng on 2016/4/16.
 */
public abstract class BaseDetailPresenter<T,V> extends Presenter<T,V>{

    public abstract void loadEntityDetail(int entityId);
    public abstract void collectEntity(final T entity);

    protected Subscription subscription;

    @Override
    public void detachView() {
        if(null != iView) {
            iView = null;
        }

        if(null != subscription && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}

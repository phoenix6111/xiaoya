package wanghaisheng.com.xiaoya.presenter.base;

import android.support.annotation.NonNull;

/**
 * Created by sheng on 2016/4/14.
 */
public abstract class Presenter<T,V> {
    protected V iView;

    public void attachView(@NonNull V view) {
        this.iView = view;
    }

    public abstract void detachView();
}

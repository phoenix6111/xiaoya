package wanghaisheng.com.xiaoya.presenter.base;

import android.content.Context;

/**
 * Created by sheng on 2016/4/16.
 */
public abstract class BaseDetailPresenter<T,V> extends Presenter<T,V>{

    public abstract void loadEntityDetail(int entityId, Context context);
    public abstract void collectEntity(final T entity);
}

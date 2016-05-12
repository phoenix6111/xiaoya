package wanghaisheng.com.xiaoya.presenter.base;

/**
 * Created by sheng on 2016/4/16.
 */
public abstract class BaseDetailPresenter<V> extends Presenter<V>{

    public abstract void loadEntityDetail(int entityId);

}

package wanghaisheng.com.xiaoya.datasource;

/**
 * Created by sheng on 2016/5/7.
 */
public class CacheBean<T> {
    T t;
    String key;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

package wanghaisheng.com.xiaoya.beans;

import java.io.Serializable;

/**
 * Created by sheng on 2016/5/1.
 */
public class ArticleResult implements Serializable {
    private boolean ok;
    private Article result;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Article getResult() {
        return result;
    }

    public void setResult(Article result) {
        this.result = result;
    }
}

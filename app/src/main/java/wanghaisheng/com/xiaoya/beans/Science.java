package wanghaisheng.com.xiaoya.beans;

import java.util.Date;
import java.util.List;

/**
 * Created by sheng on 2016/4/16.
 */
public class Science {
    private int offset;
    private int limit;
    private boolean ok;
    private int total;
    private Date date;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private List<Article> result;

    public List<Article> getResult() {
        return result;
    }

    public void setResult(List<Article> result) {
        this.result = result;
    }
}

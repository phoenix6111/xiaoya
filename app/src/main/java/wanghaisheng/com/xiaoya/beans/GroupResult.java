package wanghaisheng.com.xiaoya.beans;

/**
 * Created by sheng on 2016/5/7.
 */
public class GroupResult {
    private int count;
    private String indexImageUrl;

    public GroupResult() {
    }

    public GroupResult(int count, String indexImageUrl) {
        this.count = count;
        this.indexImageUrl = indexImageUrl;
    }

    public String getIndexImageUrl() {
        return indexImageUrl;
    }

    public void setIndexImageUrl(String indexImageUrl) {
        this.indexImageUrl = indexImageUrl;
    }

    public int getCount() {

        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package wanghaisheng.com.xiaoya.beans;

/**
 * Created by sheng on 2016/5/7.
 */
public class GroupResult {
    private int count;
    private String indexImageUrl;
    private String title;

    public GroupResult() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GroupResult(int count, String indexImageUrl,String title) {
        this.count = count;
        this.indexImageUrl = indexImageUrl;
        this.title = title;
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

package wanghaisheng.com.xiaoya.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sheng on 2016/5/13.
 */
public class GaoxiaoPicResult implements Serializable {
    private String category;
    private String tag;
    private int startIndex;
    private int maxEnd;
    private Object newsResult;

    private List<GaoxiaoPic> all_items;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getMaxEnd() {
        return maxEnd;
    }

    public void setMaxEnd(int maxEnd) {
        this.maxEnd = maxEnd;
    }

    public Object getNewsResult() {
        return newsResult;
    }

    public void setNewsResult(Object newsResult) {
        this.newsResult = newsResult;
    }

    public List<GaoxiaoPic> getAll_items() {
        return all_items;
    }

    public void setAll_items(List<GaoxiaoPic> all_items) {
        this.all_items = all_items;
    }


}

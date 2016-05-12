package wanghaisheng.com.xiaoya.api.jianshu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuContentResult implements Serializable{
    //文章内容列表
    public List<JianshuContent> contents;
    //下一页url
    public String nextUrl;

    public JianshuContentResult() {
    }

    public JianshuContentResult(List<JianshuContent> contents, String nextUrl) {
        this.contents = contents;
        this.nextUrl = nextUrl;
    }

    public List<JianshuContent> getContents() {
        return contents;
    }

    public void setContents(List<JianshuContent> contents) {
        this.contents = contents;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}

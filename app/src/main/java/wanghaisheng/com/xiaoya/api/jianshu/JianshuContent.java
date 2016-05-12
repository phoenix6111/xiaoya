package wanghaisheng.com.xiaoya.api.jianshu;

import java.io.Serializable;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuContent implements Serializable{
    private int id;
    private String title;
    private String date;
    private String url;
    private long readCount;
    private long likeCount;
    private String imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
//        this.date = DateUtils.reformatDate(date);
        if(null != date) {
            StringBuffer tmpStr = new StringBuffer(date);
            if(date.length() >20){
                tmpStr.setCharAt(10,' ');
                this.date = tmpStr.substring(0,19);
            }

        }
//        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

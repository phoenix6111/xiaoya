package wanghaisheng.com.xiaoya.api.meitu;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituPicture {
    @SerializedName("imageid")
    private String imageId;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("pic_url")
    private String picUrl;
    @SerializedName("pic_pageurl")
    private String picPageurl;
    @SerializedName("pic_height")
    private int picHeight;
    @SerializedName("pic_width")
    private int picWidth;
    @SerializedName("pic_size")
    private String picSize;
    @SerializedName("pic_title")
    private String picTitle;
    @SerializedName("pic_desc")
    private String picDesc;
    @SerializedName("ins_time")
    private String insTime;
    private int index;
    @SerializedName("qhimg_url")
    private String imgUrl;
    @SerializedName("qhimg_thumb_url")
    private String imgThumbUrl;
    @SerializedName("qhimg_thumb_width")
    private int imgThumbWidth;
    @SerializedName("qhimg_thumb_height")
    private int imgThumbHeight;
    @SerializedName("downurl")
    private String downUrl;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicPageurl() {
        return picPageurl;
    }

    public void setPicPageurl(String picPageurl) {
        this.picPageurl = picPageurl;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public String getPicSize() {
        return picSize;
    }

    public void setPicSize(String picSize) {
        this.picSize = picSize;
    }

    public String getPicTitle() {
        return picTitle;
    }

    public void setPicTitle(String picTitle) {
        this.picTitle = picTitle;
    }

    public String getPicDesc() {
        return picDesc;
    }

    public void setPicDesc(String picDesc) {
        this.picDesc = picDesc;
    }

    public String getInsTime() {
        return insTime;
    }

    public void setInsTime(String insTime) {
        this.insTime = insTime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgThumbUrl() {
        return imgThumbUrl;
    }

    public void setImgThumbUrl(String imgThumbUrl) {
        this.imgThumbUrl = imgThumbUrl;
    }

    public int getImgThumbWidth() {
        return imgThumbWidth;
    }

    public void setImgThumbWidth(int imgThumbWidth) {
        this.imgThumbWidth = imgThumbWidth;
    }

    public int getImgThumbHeight() {
        return imgThumbHeight;
    }

    public void setImgThumbHeight(int imgThumbHeight) {
        this.imgThumbHeight = imgThumbHeight;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }
}

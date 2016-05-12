package wanghaisheng.com.xiaoya.api.meitu;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituGallery implements Serializable{
    private String id;
    private String imageid;
    @SerializedName("group_title")
    private String groupTitle;
    private String grpseq;
    @SerializedName("total_count")
    private String totalCount;
    private int index;
    @SerializedName("qhimg_url")
    private String imgUrl;
    @SerializedName("qhimg_thumb_url")
    private String imgThumbUrl;
    @SerializedName("qhimg_width")
    private int imgWidth;
    @SerializedName("qhimg_height")
    private int imgHeight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGrpseq() {
        return grpseq;
    }

    public void setGrpseq(String grpseq) {
        this.grpseq = grpseq;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
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

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }
}

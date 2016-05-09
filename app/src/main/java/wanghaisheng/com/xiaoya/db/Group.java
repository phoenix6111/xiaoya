package wanghaisheng.com.xiaoya.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "GROUP".
 */
public class Group implements java.io.Serializable {

    private Long id;
    private Integer count;
    private Integer width;
    private Integer height;
    private Integer order;
    private Integer color;
    private String groupid;
    private String date;
    private String imageurl;
    private String url;
    private String title;
    private String type;
    private Long viewCount;
    private Boolean iscollected;

    public Group() {
    }

    public Group(Long id) {
        this.id = id;
    }

    public Group(Long id, Integer count, Integer width, Integer height, Integer order, Integer color, String groupid, String date, String imageurl, String url, String title, String type, Long viewCount, Boolean iscollected) {
        this.id = id;
        this.count = count;
        this.width = width;
        this.height = height;
        this.order = order;
        this.color = color;
        this.groupid = groupid;
        this.date = date;
        this.imageurl = imageurl;
        this.url = url;
        this.title = title;
        this.type = type;
        this.viewCount = viewCount;
        this.iscollected = iscollected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Boolean getIscollected() {
        return iscollected;
    }

    public void setIscollected(Boolean iscollected) {
        this.iscollected = iscollected;
    }

}
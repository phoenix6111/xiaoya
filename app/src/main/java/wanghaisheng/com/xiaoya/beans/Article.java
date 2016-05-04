package wanghaisheng.com.xiaoya.beans;

import java.io.Serializable;

/**
 * Created by sheng on 2016/4/16.
 */
public class Article extends Entity implements Serializable{
    private int id;
    private Author author;
    private String date_published;
    private int replies_count;
    private Image_info image_info = new Image_info();
    private String url;
    private String title;
    private String summary;
    /*
            self define
         */
    private String Info;
    private String[] channel_keys;

    private String small_image;

    public String getSmall_image() {
        return small_image;
    }

    public void setSmall_image(String small_image) {
        this.small_image = small_image;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String resource_url;
    private String content;

    private boolean is_collected;

    class Author implements Serializable {
        String nickname;
        public String getNickname() {
            return nickname;
        }
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    public class Image_info implements Serializable{
        String url;
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
    }

    /*public String getPublishDate() {
        String[] tempStr = this.date_published.split("T");
        String day = tempStr[0];
        String date = tempStr[1].split("\\+")[0];

        String result = day +" "+date;

        return result;
    }*/

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    public Image_info getImage_info() {
        return image_info;
    }

    public void setImage_info(Image_info image_info) {
        this.image_info = image_info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getChannel_keys() {
        return channel_keys;
    }

    public void setChannel_keys(String[] channel_keys) {
        this.channel_keys = channel_keys;
    }

    public boolean is_collected() {
        return is_collected;
    }

    public String getDate_published() {
        if(null != date_published) {
            StringBuffer tmpStr = new StringBuffer(date_published);
            if(date_published.length() >20){
                tmpStr.setCharAt(10,' ');
                date_published = tmpStr.substring(0,19);
            }
            return date_published;
        }

        return null;
    }

    public void setDate_published(String date_published) {
        this.date_published = date_published;
    }

    public int getReplies_count() {
        return replies_count;
    }

    public void setReplies_count(int replies_count) {
        this.replies_count = replies_count;
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


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getInfo() {
        if(Info == null) return toString();
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public boolean getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(boolean is_collected) {
        this.is_collected = is_collected;
    }

}

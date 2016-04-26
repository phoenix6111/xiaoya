package wanghaisheng.com.xiaoya.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheng on 2016/4/12.
 */
public class Story extends Entity implements Parcelable {
    public int id;

    public String title;

    public int type = 0;

    public List<String> images = new ArrayList<>();

    public String image;

    public String body;

    public List<String> css;

    public String share_url;

    public boolean isCollected;

    public Story() {
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeStringList(this.images);
        dest.writeString(this.image);
        dest.writeString(this.body);
        dest.writeStringList(this.css);
        dest.writeString(this.share_url);
        dest.writeByte(isCollected ? (byte) 1 : (byte) 0);
    }

    protected Story(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.type = in.readInt();
        this.images = in.createStringArrayList();
        this.image = in.readString();
        this.body = in.readString();
        this.css = in.createStringArrayList();
        this.share_url = in.readString();
        this.isCollected = in.readByte() != 0;
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}

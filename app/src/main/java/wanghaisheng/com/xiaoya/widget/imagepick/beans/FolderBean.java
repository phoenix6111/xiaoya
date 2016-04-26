package wanghaisheng.com.xiaoya.widget.imagepick.beans;

/**
 * Created by sheng on 2016/1/1.
 */
public class FolderBean {
    private String path;
    private String name;
    private int imgCount;
    private String firstImgPath;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

        int lastIndexOf = this.path.lastIndexOf("/");
        this.name = this.path.substring(lastIndexOf+1);
    }

    public String getName() {
        return name;
    }

    public int getImgCount() {
        return imgCount;
    }

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }
}

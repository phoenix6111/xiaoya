package wanghaisheng.com.xiaoya.api.meitu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituGalleryResult implements Serializable{

    private boolean end;
    private int count;
    private int lastid;

    private List<MeituGallery> list;

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLastid() {
        return lastid;
    }

    public void setLastid(int lastid) {
        this.lastid = lastid;
    }

    public List<MeituGallery> getList() {
        return list;
    }

    public void setList(List<MeituGallery> list) {
        this.list = list;
    }


}

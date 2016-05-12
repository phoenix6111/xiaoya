package wanghaisheng.com.xiaoya.api.meitu;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import wanghaisheng.com.xiaoya.db.MeituPicture;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituPictureResult {

    private int dspcnt;
    @SerializedName("group_title")
    private String groupTitle;
    private int count;

    private List<MeituPicture> list;

    public int getDspcnt() {
        return dspcnt;
    }

    public void setDspcnt(int dspcnt) {
        this.dspcnt = dspcnt;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MeituPicture> getList() {
        return list;
    }

    public void setList(List<MeituPicture> list) {
        this.list = list;
    }


}

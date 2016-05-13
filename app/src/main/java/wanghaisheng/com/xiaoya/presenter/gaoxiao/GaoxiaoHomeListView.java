package wanghaisheng.com.xiaoya.presenter.gaoxiao;

import wanghaisheng.com.xiaoya.beans.GaoxiaoPicResult;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/13.
 */
public interface GaoxiaoHomeListView extends BaseListView{
    void renderFirstLoadData(GaoxiaoPicResult datas);
    void refreshComplete(GaoxiaoPicResult datas);
    void loadMoreComplete(GaoxiaoPicResult datas);

    /**
     * 当图片保存完成时
     * @param imgPath 图片的保存路径
     */
    void onImageSaved(boolean imgSaved,String imgPath) ;
}

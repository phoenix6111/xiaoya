package wanghaisheng.com.xiaoya.presenter.meitu;

import wanghaisheng.com.xiaoya.api.meitu.MeituGalleryResult;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/11.
 */
public interface MeituHomeListView extends BaseListView {
    void renderFirstLoadData(MeituGalleryResult datas);
    void refreshComplete(MeituGalleryResult datas);
    void loadMoreComplete(MeituGalleryResult datas);

    /**
     * 当图片保存完成时
     * @param imgPath 图片的保存路径
     */
    void onImageSaved(boolean imgSaved,String imgPath) ;
}

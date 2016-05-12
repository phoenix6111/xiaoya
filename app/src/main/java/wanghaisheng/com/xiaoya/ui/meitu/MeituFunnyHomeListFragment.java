package wanghaisheng.com.xiaoya.ui.meitu;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.meitu.MeituApi;
import wanghaisheng.com.xiaoya.api.meitu.MeituGallery;
import wanghaisheng.com.xiaoya.api.meitu.MeituGalleryResult;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.fresco.MySimpleDraweeView;
import wanghaisheng.com.xiaoya.navigator.Navigator;
import wanghaisheng.com.xiaoya.presenter.meitu.MeituHomeListPresenter;
import wanghaisheng.com.xiaoya.presenter.meitu.MeituHomeListView;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.utils.ToastUtil;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituFunnyHomeListFragment extends BaseListFragment<MeituGallery> implements MeituHomeListView{

    private boolean hasload = false;
    private String currentImageUrl;

    private String tag;
    private int nextIndex;
    public static final String ARG_TAG = "arg_tag";

    public static final String CHANNEL_TAG = MeituApi.CHANNEL_ID[2];

    @Inject
    MeituHomeListPresenter presenter;
    @Inject
    Navigator navigator;
    @Inject
    ToastUtil toastUtil;

    public static MeituFunnyHomeListFragment newInstance(String tag) {
        MeituFunnyHomeListFragment fragment = new MeituFunnyHomeListFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(ARG_CHANNEL,channel);
        bundle.putString(ARG_TAG,tag);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager(View view) {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public CommonAdapter<MeituGallery> initAdapter() {
        return new CommonAdapter<MeituGallery>(getActivity(), R.layout.meitu_funny_home_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final MeituGallery gallery, int position) {
                holder.setText(R.id.meizi_tv_title,gallery.getGroupTitle());
                final MySimpleDraweeView simpleDraweeView = holder.getView(R.id.meizi_img);

                //setSize(gallery.getImgThumbUrl(),gallery.getImgWidth(),gallery.getImgHeight(),imageView);
                /*Uri uri = Uri.parse(gallery.getImgUrl());
                simpleDraweeView.setImageURI(uri);
                float ratio = (float)gallery.getImgWidth()/(float)gallery.getImgHeight();
                LogUtils.d("ratio.........."+ratio);
                simpleDraweeView.setAspectRatio(ratio);*/
                simpleDraweeView.setAutoPlayAnimations(true)
                            .setDraweeViewUrl(gallery.getImgUrl())
                            .setWidthAndHeight(gallery.getImgWidth(),gallery.getImgHeight());

                holder.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        MaterialDialog mDialog = new MaterialDialog.Builder(getActivity()).content("保存图片")
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        presenter.saveLargePic(gallery.getImgUrl(), gallery.getId());
                                        dialog.dismiss();
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        return true;
                    }
                });

                /*Picasso.with(getActivity()).load(gallery.getImgThumbUrl())
                        .tag("1")
                        .config(Bitmap.Config.RGB_565)
                        .into(imageView);*/
            }
        };
    }

    //修改图片尺寸
    public void setSize(String url,int width,int height,SimpleDraweeView mDraweeView) {
        Uri uri = Uri.parse(url);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                .setResizeOptions(new ResizeOptions(width, height))

                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeView.getController())
                .setAutoPlayAnimations(true)
                .setImageRequest(request)
                .build();

        mDraweeView.setController(controller);
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewestData(CHANNEL_TAG,tag);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadMoreData(CHANNEL_TAG,tag,nextIndex);
        }
    }

    @Override
    public void onReloadClicked() {
        if(checkNetWork()) {
            if(null != presenter) {
                presenter.firstLoadData(CHANNEL_TAG,tag);
            }
        }
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        tag = getArguments().getString(ARG_TAG);
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {
        if(null != presenter) {
            presenter.attachView(this);
            presenter.firstLoadData(CHANNEL_TAG,tag);
        }
    }

    @Override
    public void renderFirstLoadData(MeituGalleryResult datas) {
        if(null != datas && !ListUtils.isEmpty(datas.getList())) {
            nextIndex = datas.getLastid();
            mDatas.clear();
            mDatas.addAll(datas.getList());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshComplete(MeituGalleryResult datas) {
        onRefreshComplete();
        if(null != datas && !ListUtils.isEmpty(datas.getList())) {
            nextIndex = datas.getLastid();
            mDatas.clear();
            mDatas.addAll(datas.getList());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMoreComplete(MeituGalleryResult datas) {
        onLoadMoreComplete();
        if(null != datas && !ListUtils.isEmpty(datas.getList())) {
            nextIndex = datas.getLastid();
            mDatas.addAll(datas.getList());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onImageSaved(boolean imgSaved, String imgPath) {
        if(imgSaved) {
            toastUtil.showToast(String.format(getString(R.string.msg_image_saved),imgPath));
        } else {
            toastUtil.showToast(imgPath);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
    }
}

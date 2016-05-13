package wanghaisheng.com.xiaoya.ui.meitu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.meitu.MeituApi;
import wanghaisheng.com.xiaoya.api.meitu.MeituGallery;
import wanghaisheng.com.xiaoya.api.meitu.MeituGalleryResult;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.navigator.Navigator;
import wanghaisheng.com.xiaoya.presenter.meitu.MeituHomeListPresenter;
import wanghaisheng.com.xiaoya.presenter.meitu.MeituHomeListView;
import wanghaisheng.com.xiaoya.ui.BasePagerListFragment;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.utils.ViewUtils;
import wanghaisheng.com.xiaoya.widget.meizi.RadioImageView;

/**
 * Created by sheng on 2016/5/11.
 */
public class MeituBeautyHomePagerListFragment extends BasePagerListFragment<MeituGallery> implements MeituHomeListView{

    private boolean hasload = false;
    private String currentImageUrl;

    private String channel;
    private String tag;
    private int nextIndex;
    public static final String ARG_TAG = "arg_tag";

    public static final String CHANNEL_TAG = MeituApi.CHANNEL_ID[0];

    @Inject
    MeituHomeListPresenter presenter;
    @Inject
    Navigator navigator;

    public static MeituBeautyHomePagerListFragment newInstance(String tag) {
        MeituBeautyHomePagerListFragment fragment = new MeituBeautyHomePagerListFragment();
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
        return new CommonAdapter<MeituGallery>(getActivity(), R.layout.meitu_home_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final MeituGallery gallery, int position) {
                holder.setText(R.id.meizi_tv_title,gallery.getGroupTitle());
                final RadioImageView imageView = holder.getView(R.id.meizi_img);
                imageView.setOriginalSize(gallery.getImgWidth(), gallery.getImgHeight());
                holder.setText(R.id.total,"共"+gallery.getTotalCount()+" 张");
                Picasso.with(getActivity()).load(gallery.getImgThumbUrl())
                        .tag("1")
                        .config(Bitmap.Config.RGB_565)
                        .into(imageView);

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),MeituPersonListActivity.class);

                        Bitmap bitmap = null;
                        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
                        if (bd != null) {
                            bitmap = bd.getBitmap();
                        }

                        intent.putExtra(MeituPersonListActivity.ARG_GROUPID,gallery.getId());
                        LogUtils.d("meizihomelistfragment....groupid.."+gallery.getId());
                        intent.putExtra(MeituPersonListActivity.ARG_TITLE,gallery.getGroupTitle());
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(v, bitmap, 0, 0);
                        navigator.start(getActivity(),intent,options.toBundle());
                        if (bitmap != null && !bitmap.isRecycled()) {
                            intent.putExtra(MeituPersonListActivity.ARG_COLOR, ViewUtils.getPaletteColor(bitmap));
                        }

                    }
                });
            }
        };
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
            addOrReplace(datas.getList());
        }
    }

    @Override
    public void onImageSaved(boolean imgSaved, String imgPath) {

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

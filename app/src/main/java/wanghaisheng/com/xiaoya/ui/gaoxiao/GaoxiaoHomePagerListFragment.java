package wanghaisheng.com.xiaoya.ui.gaoxiao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.gaoxiao.GaoxiaoApi;
import wanghaisheng.com.xiaoya.beans.GaoxiaoPic;
import wanghaisheng.com.xiaoya.beans.GaoxiaoPicResult;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.fresco.MySimpleDraweeView;
import wanghaisheng.com.xiaoya.navigator.Navigator;
import wanghaisheng.com.xiaoya.presenter.gaoxiao.GaoxiaoHomeListPresenter;
import wanghaisheng.com.xiaoya.presenter.gaoxiao.GaoxiaoHomeListView;
import wanghaisheng.com.xiaoya.ui.BasePagerListFragment;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziLargePicActivity;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.utils.ToastUtil;

/**
 * Created by sheng on 2016/5/13.
 */
public class GaoxiaoHomePagerListFragment extends BasePagerListFragment<GaoxiaoPic> implements GaoxiaoHomeListView {

    private boolean hasload = false;

    private String tag;
    private int nextIndex;
    public static final String ARG_TAG = "arg_tag";

    @Inject
    GaoxiaoHomeListPresenter presenter;
    @Inject
    ToastUtil toastUtil;
    @Inject
    Navigator navigator;

    public static GaoxiaoHomePagerListFragment newInstance(String tag) {
        GaoxiaoHomePagerListFragment fragment = new GaoxiaoHomePagerListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TAG,tag);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager(View view) {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public CommonAdapter<GaoxiaoPic> initAdapter() {
        return new CommonAdapter<GaoxiaoPic>(getActivity(), R.layout.meitu_funny_home_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final GaoxiaoPic gallery, final int position) {
                holder.setText(R.id.meizi_tv_title,gallery.getTitle());
                final MySimpleDraweeView simpleDraweeView = holder.getView(R.id.meizi_img);

                simpleDraweeView.setAutoPlayAnimations(true)
                        .setDraweeViewUrl(gallery.getPic_url())
                        .setWidthAndHeight(gallery.getWidth(),gallery.getHeight());

                holder.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        new MaterialDialog.Builder(getActivity()).content("保存图片")
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        presenter.saveLargePic(gallery.getPic_url(), gallery.getId()+"");
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

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(MeiziLargePicActivity.ARG_INEX,position);
                        bundle.putString(MeiziLargePicActivity.ARG_GROUPID,gallery.getId()+"");
                        ArrayList<String> urls = new ArrayList<>();
                        for(GaoxiaoPic pic : mDatas) {
                            urls.add(pic.getPic_url());
                        }
                        bundle.putStringArrayList(MeiziLargePicActivity.ARG_URLS,urls);
                        navigator.openMeiziLargePicActivity(getActivity(),bundle);
                    }
                });
            }
        };
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewestData(tag);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadMoreData(tag,nextIndex);
        }
    }

    @Override
    public void onReloadClicked() {
        if(checkNetWork()) {
            if(null != presenter) {
                presenter.firstLoadData(tag);
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
            presenter.firstLoadData(tag);
        }
    }

    @Override
    public void renderFirstLoadData(GaoxiaoPicResult datas) {
        if(null != datas && !ListUtils.isEmpty(datas.getAll_items())) {
            nextIndex = GaoxiaoApi.LIMIT;
            mDatas.clear();
            mDatas.addAll(datas.getAll_items());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshComplete(GaoxiaoPicResult datas) {
        onRefreshComplete();
        if(null != datas && !ListUtils.isEmpty(datas.getAll_items())) {
            nextIndex = GaoxiaoApi.LIMIT;
            mDatas.clear();
            mDatas.addAll(datas.getAll_items());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMoreComplete(GaoxiaoPicResult datas) {
        onLoadMoreComplete();
        if(null != datas && !ListUtils.isEmpty(datas.getAll_items())) {
            nextIndex += GaoxiaoApi.LIMIT;
            addOrReplace(datas.getAll_items());
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
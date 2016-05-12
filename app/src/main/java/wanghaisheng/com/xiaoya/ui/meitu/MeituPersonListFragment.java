package wanghaisheng.com.xiaoya.ui.meitu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewTreeObserver;

import com.apkfuns.logutils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.db.MeituPicture;
import wanghaisheng.com.xiaoya.navigator.Navigator;
import wanghaisheng.com.xiaoya.presenter.meitu.MeituPersonListPresenter;
import wanghaisheng.com.xiaoya.presenter.meitu.MeituPersonListView;
import wanghaisheng.com.xiaoya.service.SaveAllImageService;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.ui.meizi.ISaveAllImage;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziLargePicActivity;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziPersonListActivity;
import wanghaisheng.com.xiaoya.ui.meizi.PagerResultView;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.widget.meizi.RadioImageView;

/**
 * Created by sheng on 2016/5/12.
 */
public class MeituPersonListFragment extends BaseListFragment<MeituPicture> implements MeituPersonListView,PagerResultView,ISaveAllImage {
    //    private String url;
    private String groupId;
    private String title;

    private boolean hasload = false;

    @Inject
    MeituPersonListPresenter presenter;
    @Inject
    Navigator navigator;

    public static MeituPersonListFragment newInstance(String groupId, String title) {
        LogUtils.d("MeiziPersonFragment  new groupId...."+groupId);
        MeituPersonListFragment fragment = new MeituPersonListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MeiziPersonListActivity.ARG_GROUPID,groupId);
        bundle.putString(MeiziPersonListActivity.ARG_TITLE,title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CommonAdapter<MeituPicture> initAdapter() {
        return new CommonAdapter<MeituPicture>(getActivity(), R.layout.meizi_person_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final MeituPicture picture, final int position) {
                if(null != picture) {
                    RadioImageView imageView = holder.getView(R.id.meizi_img);
                    imageView.setOriginalSize(picture.getPicWidth(), picture.getPicHeight());
                    Picasso.with(getActivity()).load(picture.getImgThumbUrl())
                            .tag("1").config(Bitmap.Config.RGB_565)
                            .into(imageView);
                    ViewCompat.setTransitionName(imageView, picture.getImgUrl());

                    holder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(MeiziLargePicActivity.ARG_INEX,position);
                            bundle.putString(MeiziLargePicActivity.ARG_GROUPID,picture.getGroupId());
                            ArrayList<String> urls = new ArrayList<>();
                            for(MeituPicture cont : mDatas) {
                                urls.add(cont.getImgUrl());
                            }
                            bundle.putStringArrayList(MeiziLargePicActivity.ARG_URLS,urls);
                            navigator.openMeiziLargePicActivity(getActivity(),bundle);
                        }
                    });
                }
            }
        };
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.refreshData(groupId);
        }
    }

    @Override
    public void onLoadMoreData() {
        setCanLoadMore(false);

    }

    @Override
    public void onReloadClicked() {
        if(checkNetWork()) {
            if(null != presenter) {
                presenter.refreshData(groupId);
            }
        }
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
//        url = getArguments().getString(MeiziPersonListActivity.ARG_URL);
        groupId = getArguments().getString(MeituPersonListActivity.ARG_GROUPID);
        title = getArguments().getString(MeituPersonListActivity.ARG_TITLE);
    }

    @Override
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager(View view) {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {
        if(null != presenter) {
            presenter.attachView(this);
            presenter.firstLoadData(groupId);
        }

        //因为个人的图片信息有限，故是一次全部加载进mdatas，不设置加载更多
        setCanLoadMore(false);
    }

    @Override
    public void renderData(List<MeituPicture> datas) {
        if(!ListUtils.isEmpty(datas)) {
            int lastIndex = mDatas.size();
            mDatas.addAll(lastIndex,datas);
            mAdapter.notifyItemChanged(lastIndex);
        }
    }

    @Override
    public void refreshdata(List<MeituPicture> datas) {
        onRefreshComplete();
        LogUtils.d("refresh complete");
        if(!ListUtils.isEmpty(datas)) {
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
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

    @Override
    public void handlerResult(Bundle reenterState) {
        myRecyclerView.scrollToPosition(reenterState.getInt(MeiziLargePicActivity.ARG_INEX, 0));
        myRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                myRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                myRecyclerView.requestLayout();
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

    @Override
    public void saveAllImage() {
        LogUtils.d("fragment 一键保存");
        ArrayList<String> urls = new ArrayList<>();
        for(MeituPicture con : mDatas) {
            urls.add(con.getImgUrl());
        }

        Intent saveIntent = new Intent(getActivity(),SaveAllImageService.class);
        saveIntent.putExtra(SaveAllImageService.ARG_TITLE,title);
        saveIntent.putExtra(SaveAllImageService.ARG_GROUPID,groupId);
        saveIntent.putStringArrayListExtra(SaveAllImageService.ARG_URLS,urls);

        getActivity().startService(saveIntent);
    }

}

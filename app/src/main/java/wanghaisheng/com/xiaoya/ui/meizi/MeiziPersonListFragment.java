package wanghaisheng.com.xiaoya.ui.meizi;

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
import wanghaisheng.com.xiaoya.db.Content;
import wanghaisheng.com.xiaoya.navigator.Navigator;
import wanghaisheng.com.xiaoya.presenter.meizi.MeiziPersonListPresenter;
import wanghaisheng.com.xiaoya.presenter.meizi.MeiziPersonListView;
import wanghaisheng.com.xiaoya.service.SaveAllImageService;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.widget.meizi.RadioImageView;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziPersonListFragment extends BaseListFragment<Content> implements MeiziPersonListView,PagerResultView,ISaveAllImage{
//    private String url;
    private String groupId;
    private String title;

    private boolean hasload = false;

    @Inject
    MeiziPersonListPresenter presenter;
    @Inject
    Navigator navigator;

    private Picasso picasso;

    public static MeiziPersonListFragment newInstance(String url,String groupId,String title) {
        LogUtils.d("MeiziPersonFragment  new groupId...."+groupId);
        MeiziPersonListFragment fragment = new MeiziPersonListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MeiziPersonListActivity.ARG_URL,url);
        bundle.putString(MeiziPersonListActivity.ARG_GROUPID,groupId);
        bundle.putString(MeiziPersonListActivity.ARG_TITLE,title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CommonAdapter<Content> initAdapter() {
        return new CommonAdapter<Content>(getActivity(), R.layout.meizi_person_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Content content, final int position) {
                if(null != content) {
                    RadioImageView imageView = holder.getView(R.id.meizi_img);
                    //imageView.setOriginalSize(content.getImagewidth(), content.getImageheight());
                    Picasso.with(getActivity()).load(content.getUrl())
                            .tag("1").config(Bitmap.Config.RGB_565)
                            .into(imageView);
                    ViewCompat.setTransitionName(imageView, content.getUrl());

                    holder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(MeiziLargePicActivity.ARG_INEX,position);
                            bundle.putString(MeiziLargePicActivity.ARG_GROUPID,content.getGroupid());
                            ArrayList<String> urls = new ArrayList<>();
                            for(Content cont : mDatas) {
                                urls.add(cont.getUrl());
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
            presenter.loadFromCache(groupId);
        }
    }

    @Override
    public void onLoadMoreData() {
        setCanLoadMore(false);
        /*if(checkNetWork()&&(null !=presenter)) {
            presenter.loadMoreData(groupId);
        }*/
    }

    @Override
    public void onReloadClicked() {
        if(checkNetWork()) {
            if(null != presenter) {
                presenter.loadFromCache(groupId);
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
        groupId = getArguments().getString(MeiziPersonListActivity.ARG_GROUPID);
        title = getArguments().getString(MeiziPersonListActivity.ARG_TITLE);
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

            if(presenter.isCacheExist(groupId)) {
                presenter.loadFromCache(groupId);
            } else {
                presenter.loadFromNet(groupId);
            }
        }

        //因为个人的图片信息有限，故是一次全部加载进mdatas，不设置加载更多
        setCanLoadMore(false);
    }

    @Override
    public void renderNetworkData(Content content) {
        if(null != content) {
            int lastIndex = mDatas.size();
            mDatas.add(lastIndex,content);
            mAdapter.notifyItemChanged(lastIndex);
        }
    }

    @Override
    public void renderCacheData(List<Content> datas) {
        if(!ListUtils.isEmpty(datas)) {
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshComplete(List<Content> datas) {
        onRefreshComplete();
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
        for(Content con : mDatas) {
            urls.add(con.getUrl());
        }

        Intent saveIntent = new Intent(getActivity(),SaveAllImageService.class);
        saveIntent.putExtra(SaveAllImageService.ARG_TITLE,title);
        saveIntent.putExtra(SaveAllImageService.ARG_GROUPID,groupId);
        saveIntent.putStringArrayListExtra(SaveAllImageService.ARG_URLS,urls);

        getActivity().startService(saveIntent);
    }
}

package wanghaisheng.com.xiaoya.ui.meizi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.db.Group;
import wanghaisheng.com.xiaoya.navigator.Navigator;
import wanghaisheng.com.xiaoya.presenter.meizi.MeiziHomeListPresenter;
import wanghaisheng.com.xiaoya.presenter.meizi.MeiziHomeListView;
import wanghaisheng.com.xiaoya.ui.BasePagerListFragment;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.utils.ViewUtils;
import wanghaisheng.com.xiaoya.widget.meizi.RadioImageView;

/**
 * Created by sheng on 2016/5/6.
 */
public class MeiziHomePagerListFragment extends BasePagerListFragment<Group> implements MeiziHomeListView{
    private String type;
    private int page = 2;
    private boolean hasload = false;
    private String currentImageUrl;

    private String channel;
    public static final String ARG_CHANNEL = "arg_channel";

    @Inject
    MeiziHomeListPresenter presenter;
    @Inject
    Navigator navigator;

    public static MeiziHomePagerListFragment newInstance(String channel) {
        MeiziHomePagerListFragment fragment = new MeiziHomePagerListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CHANNEL,channel);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager(View view) {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public CommonAdapter<Group> initAdapter() {
        return new CommonAdapter<Group>(getActivity(), R.layout.meizi_home_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Group group,int position) {
                holder.setText(R.id.meizi_tv_title,group.getTitle());
                final RadioImageView imageView = holder.getView(R.id.meizi_img);
                imageView.setOriginalSize(group.getWidth(), group.getHeight());
                Picasso.with(getActivity()).load(group.getImageurl())
                        .tag("1")
                        .into(imageView);
                ViewCompat.setTransitionName(imageView, group.getUrl());

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),MeiziPersonListActivity.class);

                        Bitmap bitmap = null;
                        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
                        if (bd != null) {
                            bitmap = bd.getBitmap();
                        }

                        intent.putExtra(MeiziPersonListActivity.ARG_GROUPID,group.getGroupid());
                        LogUtils.d("meizihomelistfragment....groupid.."+group.getGroupid());
                        intent.putExtra(MeiziPersonListActivity.ARG_TITLE,group.getTitle());
                        intent.putExtra(MeiziPersonListActivity.ARG_URL,group.getUrl());
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(v, bitmap, 0, 0);
                        navigator.start(getActivity(),intent,options.toBundle());
                        if (bitmap != null && !bitmap.isRecycled()) {
                            intent.putExtra(MeiziPersonListActivity.ARG_COLOR, ViewUtils.getPaletteColor(bitmap));
                        }
//                        Intent intent = new Intent(getActivity(),MeiziPersonListActivity.class);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                    }
                });
            }
        };
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewestData(channel);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadMoreData(channel,page);
        }
    }

    @Override
    public void onReloadClicked() {
        if(checkNetWork()) {
            if(null != presenter) {
                presenter.firstLoadData(channel);
            }
        }
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        channel = getArguments().getString(ARG_CHANNEL);
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {
        if(null != presenter) {
            presenter.attachView(this);
            presenter.firstLoadData(channel);
        }
    }

    @Override
    public void renderFirstLoadData(List<Group> datas) {
        if(!ListUtils.isEmpty(datas)) {
            page = 2;
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshComplete(List<Group> datas) {
        onRefreshComplete();
        if(!ListUtils.isEmpty(datas)) {
            page = 2;
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMoreComplete(List<Group> datas) {
        onLoadMoreComplete();
        if(!ListUtils.isEmpty(datas)) {
            page += 1;
            addOrReplace(datas);
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

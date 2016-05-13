package wanghaisheng.com.xiaoya.ui;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;
import wanghaisheng.com.xiaoya.ui.empty.EmptyLayout;

//import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

/**
 * Created by sheng on 2016/4/14.
 */
public abstract class BaseListFragment<T> extends BaseLazyFragment implements BaseListView {

    protected SwipeToLoadLayout swipeToLoadLayout;
    protected RecyclerView myRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected CommonAdapter<T> mAdapter;

    protected EmptyLayout emptyLayout;
    protected int mStoreEmptyState = -1;//保存EmptyLayout的状态信息

    //recyclerview中的数据
    protected List<T> mDatas = new ArrayList<>();

    protected boolean firstLoad = true;

    //是否是滚动状态
    protected boolean isScrolling;

    //初始化UI相关的属性
    @Override
    public void beforeInitView(View view) {
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_layout);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mLayoutManager = getRecyclerViewLayoutManager(view);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = initAdapter();
        myRecyclerView.setAdapter(mAdapter);

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMoreData();
            }
        });
        myRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                //当停止滑动时加载图片
                isScrolling = !(newState == RecyclerView.SCROLL_STATE_IDLE);
                if (!isScrolling) {
                    mAdapter.notifyDataSetChanged();
                }
                LogUtils.d("isScrolling....."+isScrolling);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }

                super.onScrollStateChanged(recyclerView,newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtils.d("scrolling...........");
            }
        });


        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReloadClicked();
            }
        });
    }


    //设置recyclerview的布局管理器，如果子类要改变recyclerview的布局，则修改此方法的返回值
    public RecyclerView.LayoutManager getRecyclerViewLayoutManager(View view) {
        return new LinearLayoutManager(view.getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }

    @Override
    public void hideLoading() {
        emptyLayout.dismiss();
        swipeToLoadLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        emptyLayout.setNetworkLoading();
        swipeToLoadLayout.setVisibility(View.GONE);
    }

    @Override
    public void error(int errorType,String errMsg) {
        switch (errorType) {
            case BaseListView.ERROR_TYPE_NETWORK:
                emptyLayout.setNetworkError();
                break;
            case BaseListView.ERROR_TYPE_NODATA:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodata();
                break;
            case BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodataEnableClick();
                break;
            default:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodata();
                break;
        }
        swipeToLoadLayout.setVisibility(View.GONE);
    }

    //初始化适配器
    public abstract CommonAdapter<T> initAdapter();

    public boolean checkNetWork() {
//        LogUtils.v("checknetwork ................");
        if(!netWorkHelper.isNetworkAvailable()) {
            LogUtils.v("network error..................");
            emptyLayout.setNetworkError();
            swipeToLoadLayout.setVisibility(View.GONE);
            return false;
        }

        return true;
    }

    //当数据刷新完成时调用
    public void onRefreshComplete() {
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    public void onLoadMoreComplete() {
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    public abstract void onRefreshData();

    public abstract void onLoadMoreData();

    public void setCanRefresh(boolean canRefresh) {
        swipeToLoadLayout.setRefreshEnabled(canRefresh);
    }

    public void setCanLoadMore(boolean canLoadMore) {
        swipeToLoadLayout.setLoadMoreEnabled(canLoadMore);
    }

    public abstract void onReloadClicked();

    /**
     * 上拉加载更多时，统一处理添加数据
     * @param datas
     */
    public void addOrReplace(List<T> datas) {
        int lastIndex = mDatas.size();
        mDatas.addAll(lastIndex,datas);
        mAdapter.notifyItemRangeChanged(lastIndex,datas.size());
    }

}

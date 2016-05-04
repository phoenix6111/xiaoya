package wanghaisheng.com.xiaoya.ui;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;
import wanghaisheng.com.xiaoya.utils.ListUtils;

//import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

/**
 * Created by sheng on 2016/4/14.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements BaseListView<T> {

    protected SwipeToLoadLayout swipeToLoadLayout;
    //    protected PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    protected RecyclerView myRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected CommonAdapter<T> mAdapter;

    //recyclerview中的数据
    protected List<T> mDatas = new ArrayList<>();

    protected boolean firstLoad = true;

    protected View rootView;

    //初始化UI相关的属性
    @Override
    public void initUI(View view) {
        mFragmentComponent.inject(this);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mLayoutManager = getRecyclerViewLayoutManager(view);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = initAdapter();
//        LogUtils.v("after get init adapter.......................................");
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
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
        showContent(true);
    }

    @Override
    public void showLoading() {
        showProgress(true);
    }

    @Override
    public void error(String error) {
        setErrorText(error);
        showError(true);
    }

    public abstract CommonAdapter<T> initAdapter();

    public void refreshComplete(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
        onRefreshComplete();
    }

    public void loadMoreComplete(List<T> datas) {
        if (ListUtils.isEmpty(datas)) {
            if (swipeToLoadLayout.isLoadingMore()) {
                swipeToLoadLayout.setRefreshing(false);
            }

            setCanLoadMore(false);
            return;
        }
        mDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
        onLoadMoreComplete();
    }

    public void renderNetData(List<T> datas) {
        //LogUtils.v("print renderstory list........................................");
        mDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
        onRefreshComplete();
    }

    public boolean checkNetWork() {
        if(!netWorkHelper.isAvailableNetwork()) {
            error("无网络连接，请连接后重试。。");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据从数据库获取的数据刷新界面
     *
     * @param datas
     */
    public void renderDbData(List<T> datas) {
//        LogUtils.v(datas);
        if (ListUtils.isEmpty(datas)) {
            LogUtils.v("into empty.......................................");
            loadNewFromNet();
        } else {
//            LogUtils.d("from database not empty...");
            mDatas.clear();
            mDatas.addAll(datas);
//            LogUtils.d(mDatas);
            mAdapter.notifyDataSetChanged();
            //如果是第一次加载，就等5S后从远程刷新数据

            if(checkNetWork()) {
                //5秒后执行查询网络数据
                Observable.timer(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtils.d(e.getMessage());
                                showError(true);
                            }

                            @Override
                            public void onNext(Long aLong) {
                                loadNewFromNet();
                            }
                        });
                firstLoad = false;
            }


        }
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

    public abstract void loadNewFromNet();


    public abstract void onRefreshData();

    public abstract void onLoadMoreData();

    public void setCanRefresh(boolean canRefresh) {
        swipeToLoadLayout.setRefreshEnabled(canRefresh);

    }

    public void setCanLoadMore(boolean canLoadMore) {
        swipeToLoadLayout.setLoadMoreEnabled(canLoadMore);
    }


}

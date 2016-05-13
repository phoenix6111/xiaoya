package wanghaisheng.com.xiaoya.ui.weiarticle;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.weixingjingxuan.WeiArticle;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.presenter.weiarticle.WeiArticleListPresenter;
import wanghaisheng.com.xiaoya.presenter.weiarticle.WeiArticleListView;
import wanghaisheng.com.xiaoya.ui.BasePagerListFragment;
import wanghaisheng.com.xiaoya.ui.other.BrowserActivity;
import wanghaisheng.com.xiaoya.utils.ImageUtil;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;

/**
 * Created by sheng on 2016/5/13.
 */
public class WeiArticleListFragment extends BasePagerListFragment<WeiArticle> implements WeiArticleListView {
    public static final String ARG_MOVIE = "movie";

    public static final String ARG_MOVIE_LIST_FIRST_LOAD = "MOVIE_list_first_load";

    @Inject
    ImageUtil imageUtil;
    @Inject
    WeiArticleListPresenter presenter;
    @Inject
    PrefsUtil prefsUtil;
    private int pageNum;

    public static WeiArticleListFragment newInstance() {
        return new WeiArticleListFragment();
    }

    @Override
    public CommonAdapter<WeiArticle> initAdapter() {
        return mAdapter = new CommonAdapter<WeiArticle>(getActivity(), R.layout.story_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final WeiArticle article, int position) {
                SimpleDraweeView imageView = holder.getView(R.id.item_iv_pic);
                imageView.setImageURI(Uri.parse(article.getFirstImg()));
                holder.setText(R.id.item_tv_title,article.getTitle());

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BrowserActivity.startActivity(getActivity(), article.getUrl());
                    }
                });
            }
        };
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewestData();
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadMoreData(pageNum);
        }
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void initData() {
//        LogUtils.d("Movie List Fragment initData...........");

        this.firstLoad = prefsUtil.get(ARG_MOVIE_LIST_FIRST_LOAD,false);

        if(null != presenter) {
            presenter.attachView(this);
            presenter.firstLoadData();
        }

    }

    @Override
    public void onReloadClicked() {
//        presenter.loadFromDb();
        if(checkNetWork()) {
            pageNum = 1;
            if(null != presenter) {
                presenter.loadNewestData();
            }
        }
    }


    @Override
    public void onDestroy() {
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void renderFirstLoadData(List<WeiArticle> datas) {
        if(null != datas) {
            pageNum = 2;
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshComplete(List<WeiArticle> datas) {
        LogUtils.d("refreshComplete................");
        onRefreshComplete();
        if(null != datas) {
            pageNum = 2;
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMoreComplete(List<WeiArticle> datas) {
        onLoadMoreComplete();

        if(!ListUtils.isEmpty(datas)) {
            pageNum += 1;
            addOrReplace(datas);
        } else {
            setCanLoadMore(false);
        }
    }
}
package wanghaisheng.com.xiaoya.ui.science;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.beans.Science;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.presenter.science.ScienceListPresenter;
import wanghaisheng.com.xiaoya.presenter.science.ScienceListView;
import wanghaisheng.com.xiaoya.ui.BasePagerListFragment;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;

/**
 * Created by sheng on 2016/4/15.
 */
public class SciencePagerListFragment extends BasePagerListFragment<Article> implements ScienceListView {
    public static final String ARG_CHANNEL = "channel";
    public static final String ARG_ARTICLE = "article";

    @Inject
    ScienceListPresenter presenter;
    @Inject
    PrefsUtil prefsUtil;

    //调用API的offset
    private int offset;
    private String channel;//article所属的channel
    private boolean hasMore;//是否还有更多

    public static final String ARG_DATAS = "arg_datas";

    /**
     * 根据channel初始化fragment
     * @param channel
     * @return
     */
    public static SciencePagerListFragment newInstance(String channel) {
        SciencePagerListFragment fragment = new SciencePagerListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CHANNEL,channel);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
//        LogUtils.d("getBundler..................");
        this.channel = bundle.getString(ARG_CHANNEL);
        if(null != bundle.getSerializable(ARG_DATAS)) {
            LogUtils.d("bundler not null........................");
            this.mDatas = (List<Article>) bundle.getSerializable(ARG_DATAS);
        }
    }

    @Override
    public CommonAdapter<Article> initAdapter() {
        mAdapter = new CommonAdapter<Article>(getActivity(), R.layout.science_item_layout2,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Article article,int position) {
                holder.setText(R.id.article_title,article.getTitle());
                holder.setText(R.id.comment_count,article.getReplies_count()+"");
                /*holder.setText(R.id.article_summary,article.getSummary());
                if(!TextUtils.isEmpty(article.getDate_published())) {
                    holder.setText(R.id.article_pubdate,article.getDate_published());
                }*/
                SimpleDraweeView imageView = holder.getView(R.id.article_image);
                if(null != article.getImage_info()&&!isScrolling) {
                    //imageUtil.loadImage(getActivity(),article.getImage_info().getUrl(),imageView);
                    imageView.setImageURI(Uri.parse(article.getImage_info().getUrl()));
                }

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),ScienceDetailActivity.class);
                        intent.putExtra(ARG_ARTICLE,article);
                        LogUtils.v(".....science list on clicked.................");
                        LogUtils.v(article);
                        startActivity(intent);
                    }
                });
            }
        };

        return mAdapter;
    }

    /**
     * 当下拉刷新数据时
     */
    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewestData(channel);
        }
    }

    /**
     * 当上拉加载更多时
     */
    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadMoreData(channel,offset);
        }
    }


    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {
//        LogUtils.d("sciencelistfragment initData................");

        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        if(null != presenter) {
            presenter.attachView(this);
            presenter.firstLoadData(channel);
        }

    }

    @Override
    public void onReloadClicked() {
//        presenter.loadFromDb(channel);
        if(checkNetWork()) {
            if(null != presenter) {
                presenter.firstLoadData(channel);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.d("SciencePagerListFragment onSaveInstancestate...............");
        outState.putSerializable(ARG_DATAS, (Serializable) mDatas);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
        offset = 0;

    }

    /**
     * 当第一次加载数据完成时调用
     * @param datas
     */
    @Override
    public void renderFirstLoadData(Science datas) {
//        LogUtils.d("renderFirstLoadData......");
//        LogUtils.d(datas);
        if(null != datas) {
            offset = ScienceListPresenter.limit;
            if (null != datas.getResult()) {
                mDatas.clear();
                mDatas.addAll(datas.getResult());
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 当刷新完成时调用
     * @param datas
     */
    @Override
    public void refreshComplete(Science datas) {
        onRefreshComplete();
        if(null != datas) {
            offset = ScienceListPresenter.limit;
            if (null != datas.getResult()) {
                mDatas.clear();
                mDatas.addAll(datas.getResult());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadMoreComplete(Science datas) {
        onLoadMoreComplete();
        if(null != datas) {
            offset = datas.getOffset();
            if (null != datas.getResult()) {
                addOrReplace(datas.getResult());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = getArguments();
        bundle.putSerializable(ARG_DATAS, (Serializable) mDatas);
    }
}

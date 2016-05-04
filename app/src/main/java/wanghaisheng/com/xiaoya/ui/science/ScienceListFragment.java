package wanghaisheng.com.xiaoya.ui.science;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.presenter.science.ScienceListPresenter;
import wanghaisheng.com.xiaoya.presenter.science.ScienceListView;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;

/**
 * Created by sheng on 2016/4/15.
 */
public class ScienceListFragment extends BaseListFragment<Article> implements ScienceListView {
    public static final String ARG_CHANNEL = "channel";
    private String channel;
    public static final String ARG_ARTICLE = "article";

    public static final String ARG_SCIENCE_LIST_FIRST_LOAD = "science_list_first_load";

    @Inject
    ScienceListPresenter presenter;
    @Inject
    PrefsUtil prefsUtil;

    //调用API的offset
    private int offset;

    public static final String ARG_DATAS = "arg_datas";

    /**
     * 根据channel初始化fragment
     * @param channel
     * @return
     */
    public static ScienceListFragment newInstance(String channel) {
        ScienceListFragment fragment = new ScienceListFragment();
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
    public void getBundle(Bundle bundle) {
        LogUtils.d("getBundler..................");
        this.channel = bundle.getString(ARG_CHANNEL);
        if(null != bundle.getSerializable(ARG_DATAS)) {
            LogUtils.d("bundler not null........................");
            this.mDatas = (List<Article>) bundle.getSerializable(ARG_DATAS);
        }
    }


    @Override
    public CommonAdapter<Article> initAdapter() {
//        LogUtils.v("sciencelistfragment ......................init adapter.........");
        mAdapter = new CommonAdapter<Article>(getActivity(), R.layout.science_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Article article) {
                holder.setText(R.id.article_title,article.getTitle());
                holder.setText(R.id.comment_count,article.getReplies_count()+"");
                holder.setText(R.id.article_summary,article.getSummary());
                if(!TextUtils.isEmpty(article.getDate_published())) {
                    holder.setText(R.id.article_pubdate,article.getDate_published());
                }
                SimpleDraweeView imageView = holder.getView(R.id.article_image);
                if(null != article.getImage_info()) {
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

//        LogUtils.v("after new adapter ................");
//        LogUtils.v(mAdapter);

        return mAdapter;
    }

    @Override
    public void loadNewFromNet() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewFromNet(channel,true);
        }
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewFromNet(channel,false);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadMoreData(channel,offset);
        }
    }

    @Override
    public void initData() {
        LogUtils.d("sciencelistfragment initData................");

        this.firstLoad = prefsUtil.get(ARG_SCIENCE_LIST_FIRST_LOAD,false);
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        presenter.attachView(this);
        //一开始从数据库加载缓存数据
//        if(ListUtils.isEmpty(mDatas)) {
//            LogUtils.d("science list load from db................");
            if(null != presenter) {
                presenter.loadFromDb(channel);
            }
//        }
        //presenter.loadNewFromNet(channel);
    }

    @Override
    public void onReloadClicked() {
        super.onReloadClicked();
//        presenter.loadFromDb(channel);
        if(checkNetWork()) {
            presenter.loadNewFromNet(channel,true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.d("ScienceListFragment onSaveInstancestate...............");
        outState.putSerializable(ARG_DATAS, (Serializable) mDatas);
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
    public void setExtraData(int tempOffset) {
        if(tempOffset>0) {
            this.offset = tempOffset;
        } else {
            this.offset = this.offset + ScienceListPresenter.limit;
            setCanLoadMore(false);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = getArguments();
        bundle.putSerializable(ARG_DATAS, (Serializable) mDatas);
    }

}

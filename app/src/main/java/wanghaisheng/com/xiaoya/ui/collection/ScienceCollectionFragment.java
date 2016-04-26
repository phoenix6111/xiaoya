package wanghaisheng.com.xiaoya.ui.collection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.presenter.collection.ScienceCollectionPresenter;
import wanghaisheng.com.xiaoya.presenter.collection.ScienceCollectionView;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.ui.science.ScienceDetailActivity;
import wanghaisheng.com.xiaoya.ui.science.ScienceListFragment;

/**
 * Created by sheng on 2016/4/19.
 */
public class ScienceCollectionFragment extends BaseListFragment<Article> implements ScienceCollectionView{
    public static final String ARG_ARTICLE = "article";
    private int page = 1;

    @Inject
    ScienceCollectionPresenter presenter;

    public static ScienceCollectionFragment newInstance() {
        return new ScienceCollectionFragment();
    }

    @Override
    public CommonAdapter<Article> initAdapter() {
        mAdapter = new CommonAdapter<Article>(getActivity(), R.layout.science_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Article article) {
                holder.setText(R.id.article_title,article.getTitle());
                holder.setText(R.id.comment_count,article.getReplies_count()+"");
                ImageView imageView = holder.getView(R.id.article_image);
                if(null != article.getImage_info()) {
                    //imageUtil.loadImage(getActivity(),article.getImage_info().getUrl(),imageView);
                    imageView.setImageURI(Uri.parse(article.getImage_info().getUrl()));
                }

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),ScienceDetailActivity.class);
                        intent.putExtra(ScienceListFragment.ARG_ARTICLE,article);
                        LogUtils.v(".....science list on clicked.................");
                        LogUtils.v(article);
                        startActivity(intent);
                    }
                });
            }
        };

        return mAdapter;
    }

    @Override
    public void loadNewFromNet() {

    }

    @Override
    public void onRefreshData() {
    }

    @Override
    public void onLoadMoreData() {
        setCanLoadMore(false);
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initData() {
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        presenter.attachView(this);
        presenter.loadArticleFromDb(page);
        setCanLoadMore(false);
    }

    @Override
    public void renderArticles(int page, List<Article> articles) {
        page += page;
//        LogUtils.v(articles);
        mDatas.clear();
        mDatas.addAll(articles);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReloadClicked() {
        super.onReloadClicked();
        presenter.loadArticleFromDb(page);
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

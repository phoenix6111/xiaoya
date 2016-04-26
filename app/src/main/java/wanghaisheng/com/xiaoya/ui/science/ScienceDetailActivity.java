package wanghaisheng.com.xiaoya.ui.science;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.presenter.science.ScienceDetailPresenter;
import wanghaisheng.com.xiaoya.presenter.science.ScienceDetailView;
import wanghaisheng.com.xiaoya.ui.BaseDetailActivity;
import wanghaisheng.com.xiaoya.utils.DisplayHelper;

/**
 * Created by sheng on 2016/4/16.
 */
public class ScienceDetailActivity extends BaseDetailActivity implements ScienceDetailView {
    @Bind(R.id.science_avatar)
    SimpleDraweeView topPicView;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private Article article;
    private String imgUrl;

    @Inject
    ScienceDetailPresenter presenter;

    @Override
    public void getDatas(Bundle savedInstanceState) {
        article = (Article) getIntent().getSerializableExtra(ScienceListFragment.ARG_ARTICLE);
    }

    @Override
    public void collect() {
        if(!isCollected) {
            presenter.collectEntity(article);
        } else {
            presenter.unCollectEntity(article);
        }
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.science_detail;
    }

    @Override
    public void initUIAndDatas() {
        initToolbar(mToolbar);
        ButterKnife.bind(this);
        presenter.attachView(this);

        //将webview上移，去除广告
        int height = DisplayHelper.getScreenHeight(mAppContext);
        int marginHeight = (int)(height*0.2f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,-marginHeight,0,0);
        webView.setLayoutParams(layoutParams);

        imgUrl = article.getImage_info().getUrl();
        //imageUtil.loadImage(this,imgUrl,topPicView);
        topPicView.setImageURI(Uri.parse(imgUrl));
        collapsingToolbarLayout.setTitle(article.getTitle());
        //检测是否有网络连接再加载数据
        if(checkNetWork()) {
            webView.loadUrl(article.getUrl());
            showLoading();
        }

        //presenter.loadEntityDetail(story.getId(),this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        presenter.checkIfCollected(article);
        return true;
    }

    @Override
    public void onUpdatePager(int page, int total) {

    }

    @Override
    protected void onDestroy() {
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
        super.onDestroy();
    }
}

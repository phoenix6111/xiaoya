package wanghaisheng.com.xiaoya.ui.daily;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.presenter.daily.StoryDetailPresenter;
import wanghaisheng.com.xiaoya.presenter.daily.StoryDetailView;
import wanghaisheng.com.xiaoya.ui.BaseDetailActivity;

/**
 * Created by sheng on 2016/4/21.
 */
public class ThemeStoryDetailActivity extends BaseDetailActivity implements StoryDetailView {
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.story_avatar)
    SimpleDraweeView storyAvatar;

    @Inject
    StoryDetailPresenter presenter;

    private Story story;

    @Override
    public void getDatas(Bundle savedInstanceState) {
        story = getIntent().getParcelableExtra(DailyListFragment.ARG_STORY);
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
        return R.layout.story_theme_detail;
    }

    @Override
    public void initUIAndDatas() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        initToolbar(mToolbar);
        collapsingToolbarLayout.setTitle(story.getTitle());

        /*//将webview上移，去除广告
        int height = DisplayHelper.getScreenHeight(mAppContext);
        int marginHeight = (int)(height*0.12f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,-marginHeight,0,0);
        webView.setLayoutParams(layoutParams);*/

        LogUtils.v(story);

        if(null != story.getImages()) {
            //imageUtil.loadImage(this,story.getImages().get(0),storyAvatar);
            storyAvatar.setImageURI(Uri.parse(story.getImages().get(0)));
        } else if(null != story.getImage()) {
            //imageUtil.loadImage(this,story.getImage(),storyAvatar);
            storyAvatar.setImageURI(Uri.parse(story.getImage()));
        }

        if(!netWorkHelper.isAvailableNetwork() ) {
            onError("网络连接错误，请连接网络后重试！");
        } else {
            presenter.loadEntityDetail(story.getId(),this);
        }


    }

    @Override
    public void onUpdatePager(int page, int total) {

    }


    @Override
    public void collect() {
        if(!isCollected) {
            presenter.collectEntity(story);
        } else {
            presenter.unCollectEntity(story);
        }
    }

    @Override
    public void renderEntityView(Story story) {
        if(!netWorkHelper.isAvailableNetwork() ) {
            onError("网络连接错误，请连接网络后重试！");
            return;
        }

        webView.loadUrl(story.getShare_url());
        //当webview加载网页信息时显示progress
        showLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        presenter.checkIfCollected(story);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
    }
}

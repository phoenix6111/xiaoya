package wanghaisheng.com.xiaoya.ui.daily;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

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
 * Created by sheng on 2016/4/15.
 */
public class StoryDetailActivity extends BaseDetailActivity implements StoryDetailView {
    /*@Bind(R.id.story_title)
    TextView titleView;*/
    @Bind(R.id.img_avatar)
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
        return R.layout.common_detail;
    }

    @Override
    public void initUIAndDatas() {
        ButterKnife.bind(this);
        initToolbar(mToolbar);
        presenter.attachView(this);

        if(null != story.getImages()) {
            //imageUtil.loadImage(this,story.getImages().get(0),storyAvatar);
            storyAvatar.setImageURI(Uri.parse(story.getImages().get(0)));
        } else if(null != story.getImage()) {
            //imageUtil.loadImage(this,story.getImage(),storyAvatar);
            storyAvatar.setImageURI(Uri.parse(story.getImage()));
        }

        if(checkNetWork()) {
            presenter.loadEntityDetail(story.getId());
        }

        presenter.checkIfCollected(story);
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

    /**
     * 根据presenter获取的网页html代码，加载网页数据
     * @param webPageStr
     */
    @Override
    public void renderWebview(String webPageStr) {
        webView.loadDataWithBaseURL("file:///android_asset/",webPageStr,"text/html", "utf-8", null);
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

        if(webView != null) {
            webView.removeCallBack();
            webView.destroy();
        }

        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
    }
}

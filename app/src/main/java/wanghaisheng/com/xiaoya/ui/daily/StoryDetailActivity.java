package wanghaisheng.com.xiaoya.ui.daily;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.Bind;
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
        story = (Story) getIntent().getSerializableExtra(StoryListFragment.ARG_STORY);
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
    public void collect() {
        if(!isCollected) {
            presenter.collectEntity(story);
        } else {
            presenter.unCollectEntity(story);
        }

    }

    @Override
    public void onReloadClick() {
        if(checkNetWork()&&null!=presenter) {
            presenter.loadEntityDetail(story.getId());
        }
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
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }

        super.onDestroy();
        System.exit(0);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
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
}

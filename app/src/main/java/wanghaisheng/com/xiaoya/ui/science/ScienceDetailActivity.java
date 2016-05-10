package wanghaisheng.com.xiaoya.ui.science;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.beans.Article;
import wanghaisheng.com.xiaoya.presenter.science.ScienceDetailPresenter;
import wanghaisheng.com.xiaoya.presenter.science.ScienceDetailView;
import wanghaisheng.com.xiaoya.ui.BaseDetailActivity;

/**
 * Created by sheng on 2016/4/16.
 */
public class ScienceDetailActivity extends BaseDetailActivity implements ScienceDetailView{
    private static final String TAG = "ScienceDetailActivity";

    @Bind(R.id.img_avatar)
    SimpleDraweeView topPicView;

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
    public void onReloadClick() {
        if(checkNetWork()) {
            presenter.loadEntityDetail(article.getId());
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
        return R.layout.common_detail;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        presenter.checkIfCollected(article);
        return true;
    }

    @Override
    protected void onDestroy() {
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
        super.onDestroy();
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
    public void initView() {

    }

    @Override
    public void initData() {
        presenter.attachView(this);

        if(null != article.getImage_info()) {
            imgUrl = article.getImage_info().getUrl();
            //imageUtil.loadImage(this,imgUrl,topPicView);
            topPicView.setImageURI(Uri.parse(imgUrl));
        }
        //检测是否有网络连接再加载数据
        if(checkNetWork()) {
            presenter.loadEntityDetail(article.getId());
        }
    }
}

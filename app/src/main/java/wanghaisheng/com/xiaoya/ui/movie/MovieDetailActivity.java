package wanghaisheng.com.xiaoya.ui.movie;

import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.db.Movie;
import wanghaisheng.com.xiaoya.presenter.movie.MovieDetailPresenter;
import wanghaisheng.com.xiaoya.presenter.movie.MovieDetailView;
import wanghaisheng.com.xiaoya.ui.BaseDetailActivity;
import wanghaisheng.com.xiaoya.widget.XiaoYaWebView;

/**
 * Created by sheng on 2016/4/18.
 */
public class MovieDetailActivity extends BaseDetailActivity implements MovieDetailView,XiaoYaWebView.OnScrollChangedCallback {
    @Inject
    MovieDetailPresenter presenter;

    private Movie movie;

    @Override
    public void collect() {
        presenter.collectEntity(movie);
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        this.movie = (Movie) getIntent().getSerializableExtra(MovieListFragment.ARG_MOVIE);
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
        return R.layout.movie_detail;
    }

    @Override
    public void initUIAndDatas() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        initToolbar(mToolbar);

        //将webview上移，去除广告
        /*int height = DisplayHelper.getScreenHeight(mAppContext);
        int marginHeight = (int)(height*0.175f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,-marginHeight,0,0);
        LogUtils.d("margin top .........."+marginHeight);
        webView.setLayoutParams(layoutParams);*/

        String detailUrl = presenter.getMovieDetailUrl(movie.getId()+"");
        webView.loadUrl(detailUrl);

        showLoading();
    }

    @Override
    public void onUpdatePager(int page, int total) {

    }

    @Override
    public void onScroll(int dx, int dy) {
        LogUtils.d("dx===>"+dx+"........dy===>"+dy);
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

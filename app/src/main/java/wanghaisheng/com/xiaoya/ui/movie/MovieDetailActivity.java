package wanghaisheng.com.xiaoya.ui.movie;

import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;

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
    public void onReloadClick() {

    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        this.movie = (Movie) getIntent().getSerializableExtra(MoviePagerListFragment.ARG_MOVIE);
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

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        presenter.attachView(this);

        String detailUrl = presenter.getMovieDetailUrl(movie.getId()+"");
        webView.loadUrl(detailUrl);

        showLoading();
    }
}

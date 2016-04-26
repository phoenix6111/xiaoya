package wanghaisheng.com.xiaoya.ui.movie;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.db.Movie;
import wanghaisheng.com.xiaoya.presenter.movie.MoveListView;
import wanghaisheng.com.xiaoya.presenter.movie.MovieListPresenter;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.ui.other.BrowserActivity;
import wanghaisheng.com.xiaoya.utils.ImageUtil;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;

/**
 * Created by sheng on 2016/4/17.
 */
public class MovieListFragment extends BaseListFragment<Movie> implements MoveListView {
    public static final String ARG_MOVIE = "movie";

    public static final String ARG_MOVIE_LIST_FIRST_LOAD = "MOVIE_list_first_load";

    @Inject
    ImageUtil imageUtil;
    @Inject
    MovieListPresenter presenter;
    @Inject
    PrefsUtil prefsUtil;
    private int offset = 0;


    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public CommonAdapter<Movie> initAdapter() {
        return mAdapter = new CommonAdapter<Movie>(getActivity(), R.layout.movie_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Movie movie) {
                SimpleDraweeView imageView = holder.getView(R.id.movie_image);
                //imageUtil.loadImage(getActivity(),movie.getImg(),imageView);
                if(null != movie.getImg()) {
                    imageView.setImageURI(Uri.parse(movie.getImg()));
                    //imageUtil.loadImage(getActivity(),movie.getImg(),imageView);
                }
                holder.setText(R.id.movie_title,movie.getNm());
                holder.setText(R.id.movie_score,movie.getSc()+"分");
                holder.setText(R.id.movie_info,movie.getScm());
                holder.setText(R.id.movie_dur,movie.getDur()+"分钟");
                holder.setText(R.id.movie_show_info,movie.getShowInfo());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.v("movie item clicked...................");
                        /*Intent intent = new Intent(getActivity(),MovieDetailActivity.class);
                        intent.putExtra(ARG_MOVIE,movie);
                        startActivity(intent);*/
                        BrowserActivity.startActivity(getActivity(), MovieApi.getMovieDetailUrl(movie.getId()+""));
                    }
                });
            }
        };
    }

    @Override
    public void loadNewFromNet() {
        if(checkNetWork()) {
            offset = 0;
            presenter.loadNewFromNet(offset,MovieListPresenter.LIMIT,true);
            offset += MovieListPresenter.LIMIT;
        }
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()) {
            offset = 0;
            presenter.loadNewFromNet(offset,MovieListPresenter.LIMIT,false);
            offset += MovieListPresenter.LIMIT;
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()) {
            presenter.loadMoreData(offset,MovieListPresenter.LIMIT);
            offset += MovieListPresenter.LIMIT;
        }
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
        this.firstLoad = prefsUtil.get(ARG_MOVIE_LIST_FIRST_LOAD,false);
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        presenter.attachView(this);
        //一开始从数据库加载缓存数据
        presenter.loadFromDb();
        //presenter.loadNewFromNet(offset,MovieListPresenter.LIMIT);

    }

    @Override
    public void onReloadClicked() {
        super.onReloadClicked();
//        presenter.loadFromDb();
        if(checkNetWork()) {
            offset = 0;
            presenter.loadNewFromNet(0,MovieListPresenter.LIMIT,true);
        }
    }


    @Override
    public void onDestroy() {
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
        super.onDestroy();
    }
}

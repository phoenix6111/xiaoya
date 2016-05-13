package wanghaisheng.com.xiaoya.ui.movie;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.movie.MovieApi;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.db.Movie;
import wanghaisheng.com.xiaoya.presenter.movie.MoveListView;
import wanghaisheng.com.xiaoya.presenter.movie.MovieListPresenter;
import wanghaisheng.com.xiaoya.ui.BasePagerListFragment;
import wanghaisheng.com.xiaoya.ui.other.BrowserActivity;
import wanghaisheng.com.xiaoya.utils.ImageUtil;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;

/**
 * Created by sheng on 2016/4/17.
 */
public class MoviePagerListFragment extends BasePagerListFragment<Movie> implements MoveListView {
    public static final String ARG_MOVIE = "movie";

    public static final String ARG_MOVIE_LIST_FIRST_LOAD = "MOVIE_list_first_load";

    @Inject
    ImageUtil imageUtil;
    @Inject
    MovieListPresenter presenter;
    @Inject
    PrefsUtil prefsUtil;
    private int offset = 0;
    private boolean hasMore = true;

    public static MoviePagerListFragment newInstance() {
        return new MoviePagerListFragment();
    }

    @Override
    public CommonAdapter<Movie> initAdapter() {
        return mAdapter = new CommonAdapter<Movie>(getActivity(), R.layout.movie_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Movie movie,int position) {
                SimpleDraweeView imageView = holder.getView(R.id.movie_image);
                //imageUtil.loadImage(getActivity(),movie.getImg(),imageView);
                if(null != movie.getImg()&&!isScrolling) {
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
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewestData();
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            if(hasMore) {
                presenter.loadMoreData(offset);
            } else {
                setCanLoadMore(false);
            }
        }
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void initData() {
//        LogUtils.d("Movie List Fragment initData...........");

        this.firstLoad = prefsUtil.get(ARG_MOVIE_LIST_FIRST_LOAD,false);

        if(null != presenter) {
            presenter.attachView(this);
            presenter.firstLoadData();
        }

    }

    @Override
    public void onReloadClicked() {
//        presenter.loadFromDb();
        if(checkNetWork()) {
            offset = 0;
            if(null != presenter) {
                presenter.loadNewestData();
            }
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

    @Override
    public void renderFirstLoadData(List<Movie> datas) {
        if(null != datas) {
            offset = MovieListPresenter.LIMIT+1;
            hasMore = true;
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshComplete(List<Movie> datas) {
        LogUtils.d("refreshComplete................");
        onRefreshComplete();
        if(null != datas) {
            offset = MovieListPresenter.LIMIT+1;
            hasMore = true;
            mDatas.clear();
            mDatas.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMoreComplete(List<Movie> datas, boolean hasMore) {
        this.hasMore = hasMore;
        onLoadMoreComplete();

        if(!hasMore) {
            setCanLoadMore(false);
        }

        if(!ListUtils.isEmpty(datas)) {
            offset = offset+MovieListPresenter.LIMIT;
            addOrReplace(datas);
        } else {
            setCanLoadMore(false);
        }
    }
}

package wanghaisheng.com.xiaoya.ui.daily;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.presenter.daily.DailyListView;
import wanghaisheng.com.xiaoya.presenter.daily.StoryListPresenter;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.utils.ListUtils;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;

//import com.zhy.base.adapter.ViewHolder;

/**
 * Created by sheng on 2016/4/14.
 */
public class DailyListFragment extends BaseListFragment<Story> implements DailyListView {
    public static final String ARG_STORY = "story";
    public static final String ARG_THEME = "theme";
    public static final String ARG_DAILY_LIST_FIRST_LOAD = "daily_list_first_load";
    //主题ID
    private int themeId = -1;

    @Inject
    StoryListPresenter presenter;
    @Inject
    PrefsUtil prefsUtil;

    private String lastDateTime;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtils.d("onsaveinstancestate................");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public static DailyListFragment newInstance(int themeId) {
        DailyListFragment fragment = new DailyListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_THEME,themeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_list;
    }

    @Override
    public void getBundle(Bundle bundle) {
        this.themeId = getArguments().getInt(ARG_THEME);
//        LogUtils.v("themeId........................."+themeId);
    }

    @Override
    public CommonAdapter<Story> initAdapter() {
        mAdapter = new CommonAdapter<Story>(getActivity(),R.layout.story_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Story story) {
//                LogUtils.v(" title.........................."+story.getTitle());
                holder.setText(R.id.item_tv_title,story.getTitle());
//                imageUtil.loadImage(getActivity(),story.getImages().get(0),(ImageView)holder.getView(R.id.item_iv_pic));
                SimpleDraweeView imageView = holder.getView(R.id.item_iv_pic);

                if(!ListUtils.isEmpty(story.getImages())) {
                    //LogUtils.v("url str====="+story.getImages()+" ===>"+(story.getImages().get(0)==null));
                    Uri uri = Uri.parse(story.getImages().get(0));
                    //imageUtil.loadImage(getActivity(),story.getImages().get(0),(ImageView)holder.getView(R.id.item_iv_pic));
                    imageView.setImageURI(uri);
                }
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.v(" daily list onclicked..........................");
                        Intent intent = new Intent(getActivity(),StoryDetailActivity.class);
                        intent.putExtra(ARG_STORY,story);
                        startActivity(intent);
                    }
                });
            }
        };
        return mAdapter;
    }

    @Override
    public void loadNewFromNet() {
        if(checkNetWork() && (null != presenter)) {
            presenter.loadNewestFromNet(themeId,true);
        }
    }

    @Override
    public void initData() {
        LogUtils.d("DailyListFragment initData.............");

        this.firstLoad = prefsUtil.get(ARG_DAILY_LIST_FIRST_LOAD,false);

        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        presenter.attachDailyListView(this);

        //一开始从数据库加载缓存数据
        presenter.loadFromDb(themeId);
        //presenter.loadNewestFromNet(themeId);

        if(themeId != DailyApi.THEME_ID[0]) {
            setCanLoadMore(false);
        }
    }

    public String getLoadKey() {
        return ARG_DAILY_LIST_FIRST_LOAD+DailyListFragment.class.getSimpleName();
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewestFromNet(themeId,false);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            if(themeId!=1) {
                setCanLoadMore(false);
                onLoadMoreComplete();
                return;
            }
            presenter.loadMoreStories();
        }
    }

    @Override
    public void onReloadClicked() {
        if (checkNetWork()) {
            super.onReloadClicked();
            presenter.loadFromDb(themeId);
        }
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

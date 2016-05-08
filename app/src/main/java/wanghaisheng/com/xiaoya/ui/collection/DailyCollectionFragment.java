package wanghaisheng.com.xiaoya.ui.collection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.presenter.collection.DailyCollectionPresenter;
import wanghaisheng.com.xiaoya.presenter.collection.DailyCollectionView;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.ui.daily.StoryDetailActivity;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/4/19.
 */
public class DailyCollectionFragment extends BaseListFragment<Story> implements DailyCollectionView{
    public static final String ARG_STORY = "story";

    @Inject
    DailyCollectionPresenter presenter;
    private int page = 1;

    public static DailyCollectionFragment newInstance() {
        return new DailyCollectionFragment();
    }

    @Override
    public CommonAdapter<Story> initAdapter() {
        mAdapter = new CommonAdapter<Story>(getActivity(), R.layout.story_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final Story story,int position) {
                holder.setText(R.id.item_tv_title,story.getTitle());
//                imageUtil.loadImage(getActivity(),story.getImages().get(0),(ImageView)holder.getView(R.id.item_iv_pic));
                if(!ListUtils.isEmpty(story.getImages())) {
                    //imageUtil.loadImage(getActivity(),story.getImages().get(0),(ImageView)holder.getView(R.id.item_iv_pic));
                    SimpleDraweeView draweeView = holder.getView(R.id.item_iv_pic);
                    draweeView.setImageURI(Uri.parse(story.getImages().get(0)));
                }
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        LogUtils.v(" daily list onclicked..........................");
                        Intent intent = new Intent(getActivity(),StoryDetailActivity.class);
                        intent.putExtra("story",story);
                        startActivity(intent);
                    }
                });
            }
        };
        return mAdapter;
    }


    @Override
    public void onRefreshData() {
        presenter.loadStoryFromDb(1);
    }

    @Override
    public void onLoadMoreData() {
        swipeToLoadLayout.setRefreshEnabled(false);
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

    }

    @Override
    public void initData() {
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        presenter.attachView(this);
        presenter.loadStoryFromDb(page);

        setCanLoadMore(false);
    }

    @Override
    public void renderStories(int page, List<Story> stories) {
        this.page = page;
        //LogUtils.d("DailyCollectionFragment.....................");
        //LogUtils.d(stories);
        mDatas.clear();
        this.mDatas.addAll(stories);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void error(String err) {

    }

    @Override
    public void onReloadClicked() {
        presenter.loadStoryFromDb(page);
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

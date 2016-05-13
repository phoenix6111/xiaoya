package wanghaisheng.com.xiaoya.ui.jianshu;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuContent;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuContentResult;
import wanghaisheng.com.xiaoya.component.baseadapter.ViewHolder;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.CommonAdapter;
import wanghaisheng.com.xiaoya.component.baseadapter.recyclerview.DividerItemDecoration;
import wanghaisheng.com.xiaoya.navigator.Navigator;
import wanghaisheng.com.xiaoya.presenter.jianshu.JianshuListPresenter;
import wanghaisheng.com.xiaoya.presenter.jianshu.JianshuListView;
import wanghaisheng.com.xiaoya.ui.BaseListFragment;
import wanghaisheng.com.xiaoya.utils.DateUtils;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuListFragment extends BaseListFragment<JianshuContent> implements JianshuListView{
    public static final String ARG_CHANNEL = "arg_channel";

    private String channel;//频道
    private String nextPageUrl;//下一页URL

    @Inject
    JianshuListPresenter presenter;
    @Inject
    Navigator navigator;

    public static JianshuListFragment newInstance(String channel) {
        JianshuListFragment fragment = new JianshuListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CHANNEL,channel);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public CommonAdapter<JianshuContent> initAdapter() {
        return new CommonAdapter<JianshuContent>(getActivity(), R.layout.jianshu_item_layout,mDatas) {
            @Override
            public void convert(ViewHolder holder, final JianshuContent content, int position) {
                holder.setText(R.id.item_tv_title,content.getTitle());
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = simple.parse(content.getDate());
                    String resultDate = DateUtils.format(date);
                    holder.setText(R.id.tv_date,resultDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.setText(R.id.tv_read,""+content.getReadCount());
                SimpleDraweeView image = holder.getView(R.id.item_iv_pic);
                image.setImageURI(Uri.parse(content.getImgUrl()));

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(JianshuDetailActivity.ARG_JIANSHU_DETAIL,content);

                        navigator.openJianshuDetailActivity(getActivity(),bundle);
                    }
                });
            }
        };
    }

    @Override
    public void onRefreshData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadNewestData(channel);
        }
    }

    @Override
    public void onLoadMoreData() {
        if(checkNetWork()&&(null !=presenter)) {
            presenter.loadMoreData(nextPageUrl);
        }
    }

    @Override
    public void onReloadClicked() {
        if(null != presenter) {
            presenter.attachView(this);
            LogUtils.d("channel.........."+channel);
            presenter.firstLoadData(channel);
        }
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        channel = getArguments().getString(ARG_CHANNEL);
    }

    @Override
    public void initView(View view) {
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void initData() {
        if(null != presenter) {
            presenter.attachView(this);
//            LogUtils.d("channel.........."+channel);
            presenter.firstLoadData(channel);
        }
    }

    @Override
    public void renderFirstLoadData(JianshuContentResult datas) {
//        LogUtils.d("first load data...");
        if (null != datas) {
//            LogUtils.d("nextPageUrl....    "+nextPageUrl);
            String pageUrl = datas.getNextUrl();
            if(null != pageUrl) {
                nextPageUrl = pageUrl;
//                LogUtils.d("nextPageUrl.... not null   "+nextPageUrl);
            }
            List<JianshuContent> result = datas.getContents();
            if(!ListUtils.isEmpty(result)) {
                mDatas.addAll(result);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void refreshComplete(JianshuContentResult datas) {
        onRefreshComplete();
        if(null != datas) {
            String pageUrl = datas.getNextUrl();
            if(null != pageUrl) {
                nextPageUrl = pageUrl;
                LogUtils.d("nextPageUrl.... not null   "+nextPageUrl);
            }
            List<JianshuContent> contents = datas.getContents();
            if(!ListUtils.isEmpty(contents)) {
                mDatas.clear();
                mDatas.addAll(contents);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadMoreComplete(JianshuContentResult datas) {
        onLoadMoreComplete();
        if(null != datas) {
            String pageUrl = datas.getNextUrl();
            if(null != pageUrl) {
                nextPageUrl = pageUrl;
            }

            List<JianshuContent> contents = datas.getContents();
            if(!ListUtils.isEmpty(contents)) {
                addOrReplace(contents);
            }
        }
    }
}

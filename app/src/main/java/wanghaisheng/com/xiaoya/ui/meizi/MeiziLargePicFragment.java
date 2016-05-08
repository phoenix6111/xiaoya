package wanghaisheng.com.xiaoya.ui.meizi;

import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.squareup.picasso.Picasso;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseFragment;
import wanghaisheng.com.xiaoya.widget.view.TouchImageView;

/**
 * Created by sheng on 2016/5/8.
 */
public class MeiziLargePicFragment extends BaseFragment {
    public static final String ARG_INDEX = "arg_index";
    public static final String ARG_URL = "arg_url";
    public static final String ARG_GROUPID = "arg_groupid";

    private int index;
    private String groupId;
    private String url;

    TouchImageView imageView;

    public static MeiziLargePicFragment newInstance(int index,String url,String groupId) {
        MeiziLargePicFragment fragment = new MeiziLargePicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_INDEX,index);
        bundle.putString(ARG_URL,url);
        bundle.putString(ARG_GROUPID,groupId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_large_pic;
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        this.index = getArguments().getInt(ARG_INDEX);
        this.groupId = getArguments().getString(ARG_GROUPID);
        this.url = getArguments().getString(ARG_URL);
        LogUtils.d("print urls..............");
        LogUtils.d(url);
    }

    @Override
    public void beforeInitView(View view) {

    }

    @Override
    public void initView(View view) {
        this.imageView = (TouchImageView) view.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().supportFinishAfterTransition();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("url.........."+url);
        Picasso.with(getActivity()).load(url).into(imageView);
    }
}

package wanghaisheng.com.xiaoya.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import wanghaisheng.com.xiaoya.AppContext;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.di.component.DaggerFragmentComponent;
import wanghaisheng.com.xiaoya.di.component.FragmentComponent;
import wanghaisheng.com.xiaoya.di.module.FragmentModule;
import wanghaisheng.com.xiaoya.utils.ImageUtil;
import wanghaisheng.com.xiaoya.utils.NetWorkHelper;

/**
 * Created by sheng on 2016/4/13.
 */
public abstract class BaseFragment extends Fragment implements BaseFragmentInterface{
    protected FragmentComponent mFragmentComponent;

    @Inject
    protected ImageUtil imageUtil;
    @Inject
    protected NetWorkHelper netWorkHelper;


    //初始化dagger2注入
    public abstract void initInjector();

    //获取layout文件的id
    public abstract int getLayoutId();

    //得到Activity传进来的值
    public abstract void getSavedBundle(Bundle bundle);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(((AppContext)getActivity().getApplication()).getAppComponent())
                .build();
        mFragmentComponent.inject(this);
        initInjector();

        ButterKnife.bind(this,view);

        beforeInitView(view);

        initView(view);
        initData();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(null != args) {
            getSavedBundle(args);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }


    public int getThemeColor(Context mContext) {
        int materialBlue = mContext.getResources().getColor(R.color.md_green_500);
        return resolveColor(mContext, R.attr.colorPrimary, materialBlue);
    }

    private int resolveColor(Context mContext, @AttrRes int attr, int fallback) {
        TypedArray a = mContext.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, fallback);
        } finally {
            a.recycle();
        }
    }




}

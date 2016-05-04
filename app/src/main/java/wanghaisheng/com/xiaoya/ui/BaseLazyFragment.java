package wanghaisheng.com.xiaoya.ui;

import android.os.Bundle;
import android.view.View;

import wanghaisheng.com.xiaoya.AppContext;
import wanghaisheng.com.xiaoya.di.component.DaggerFragmentComponent;
import wanghaisheng.com.xiaoya.di.module.FragmentModule;

/**
 * Created by sheng on 2016/5/3.
 */
public abstract class BaseLazyFragment extends BaseFragment {
    private boolean isVisiable = true;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(((AppContext) getActivity().getApplication()).getApplicationComponent())
                .build();
        initInjector();
        getBundle(getArguments());
        initUI(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        }
    }

    private void onVisible() {
        if (isVisiable && isPrepare) {
            initData();
            isVisiable = false;
        }
    }
}

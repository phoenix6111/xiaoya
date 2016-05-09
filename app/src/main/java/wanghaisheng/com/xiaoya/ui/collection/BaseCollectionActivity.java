package wanghaisheng.com.xiaoya.ui.collection;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseSwipeBackActivity;

/**
 * Created by sheng on 2016/4/19.
 */
public class BaseCollectionActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void getDatas(Bundle savedInstanceState) {

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
//        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_content_toolbar_layout;
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);
        setTitle("收藏");
    }

    @Override
    public void initData() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content,CollectionTopNavigationFragment.newInstance()).commit();
    }
}

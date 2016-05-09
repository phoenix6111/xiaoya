package wanghaisheng.com.xiaoya.ui.meizi;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseSwipeBackActivity;

/**
 * Created by sheng on 2016/5/8.
 */
public class FuliActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.content)
    FrameLayout content;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

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
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_content_toolbar_layout;
    }

    @Override
    public void initView() {
        initToolbar(mToolbar);
        setTitle("福利");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content,BaseMeiziFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }
}

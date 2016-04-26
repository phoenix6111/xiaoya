package wanghaisheng.com.xiaoya.ui.collection;

import android.os.Bundle;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseActivity;

/**
 * Created by sheng on 2016/4/19.
 */
public class BaseCollectionActivity extends BaseActivity {

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
    public void initUiAndListener() {
        initToolbar(mToolbar);
        setTitle("收藏");
        getSupportFragmentManager().beginTransaction().replace(R.id.content,CollectionTopNavigationFragment.newInstance()).commit();
    }
}
